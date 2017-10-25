package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.exception.HandleException;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thanhnt77 11/05/2016
 * Service xuat tra hang cho doi tac
 */
@Service
public class BaseStockExpToPartnerService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockExpToPartnerService.class);
    private final Integer HASHMAP_KEY_NUMBER_SUCCESS_RECORD_IM2 = 1; //key hashMap (so luong ban ghi import thanh cong cua im2)
    private final Integer HASHMAP_KEY_NUMBER_SUCCESS_RECORD_IM1 = 3; //key hashMap (so luong ban ghi import thanh cong cua im1)
    private final Integer HASHMAP_KEY_TOTAL_RECORD_IM2 = 2; //key hashMap (so luong ban ghi bi loi cua im 2)
    private final Integer HASHMAP_KEY_TOTAL_RECORD_IM1 = 4; //key hashMap (so luong ban ghi bi loi cua im1)

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTransRepo stockTransRepo;
    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }

    /**
     * ham xu ly xuat kho cho doi tac
     * @author thanhnt77
     * @param stockTransFullDTO
     * @param staffDTO
     * @param typeExport
     * @param serialList
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public BasePartnerMessage createTransToPartner(StockTransFullDTO stockTransFullDTO, StaffDTO staffDTO, String typeExport,
                                                   String serialList, RequiredRoleMap requiredRoleMap) throws HandleException, LogicException, Exception {
        BasePartnerMessage message = new BasePartnerMessage();
        //xu ly validate empty
        validateEmptyData(stockTransFullDTO, typeExport, serialList);

        Date sysDate = getSysDate(em);
        //insert bang stockTrans
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setToOwnerId(stockTransFullDTO.getToOwnerId());
        stockTransDTO.setToOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
        stockTransDTO.setFromOwnerId(stockTransFullDTO.getFromOwnerId());
        stockTransDTO.setFromOwnerType(DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()));
        stockTransDTO.setCreateDatetime(sysDate);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        stockTransDTO.setReasonId(Const.STOCK_TRANS.EXPORT_STOCK_FROM_PARTNER_REASON_ID);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setNote(getText("mn.stock.partner.export.stock"));
        stockTransDTO.setCheckErp(stockTransFullDTO.getSyncERP());
        StockTransDTO stockTransDTOSave = stockTransService.save(stockTransDTO);

        //insert bang stockTransAction
        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStockTransId(stockTransDTOSave.getStockTransId());
        stockTransActionDTO1.setActionCode(stockTransFullDTO.getActionCode());
        stockTransActionDTO1.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionDTO1.setNote(getText("mn.stock.partner.export.stock"));
        stockTransActionDTO1.setCreateDatetime(sysDate);
        stockTransActionDTO1.setCreateUser(staffDTO.getStaffCode());
        stockTransActionDTO1.setActionStaffId(staffDTO.getStaffId());
        StockTransActionDTO stockTransActionDTOSave =  stockTransActionService.save(stockTransActionDTO1);

        StockTransActionDTO stockTransActionDTO2 = DataUtil.cloneBean(stockTransActionDTO1);
        stockTransActionDTO2.setActionCode(null);
        stockTransActionDTO2.setNote(null);
        stockTransActionDTO2.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionService.save(stockTransActionDTO2);

        StockTransActionDTO stockTransActionDTO3 = DataUtil.cloneBean(stockTransActionDTO2);
        stockTransActionDTO3.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockTransActionDTO3);

        //insert data bang stocktransDetail
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setStockTransId(stockTransDTOSave.getStockTransId());
        stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
        stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
        stockTransDetailDTO.setPrice(null);
        stockTransDetailDTO.setAmount(null);
        stockTransDetailDTO.setCreateDatetime(sysDate);

        StockTransDetailDTO stockTransDetailDTOSave = stockTransDetailService.save(stockTransDetailDTO);

        //insert bang theo so luong
        Long totalSucess = 0L;
        Long totalRecord = 0L;
        //check xem co xu ly xoa IM1
        boolean isDeleteIM1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isDeleteIM1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }
        if (Const.EXPORT_PARTNER.EXP_BY_QUANTITY.equals(typeExport)) {
            totalSucess = DataUtil.safeToLong(stockTransFullDTO.getStrQuantity());
            totalRecord = totalSucess;
            //insert bang theo dai so
        } else if (Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE.equals(typeExport)) {
            HashMap resultTmp  = exportDataBySerialRange(stockTransDTOSave.getStockTransId(), stockTransDetailDTOSave.getStockTransDetailId(), stockTransFullDTO,
                    stockTransFullDTO.getFromSerial(), stockTransFullDTO.getToSerial(), sysDate, isDeleteIM1);
            //xu ly check xoa dong bo o IM1, voi IM2 neu xoa ko thong nhat thi rolbak bao loi luon
            Long totalSucessIM2 = DataUtil.safeToLong(resultTmp.get(HASHMAP_KEY_NUMBER_SUCCESS_RECORD_IM2));
            Long totalSucessIM1 = DataUtil.safeToLong(resultTmp.get(HASHMAP_KEY_NUMBER_SUCCESS_RECORD_IM1));
            if (isDeleteIM1 && !DataUtil.safeEqual(totalSucessIM1, totalSucessIM2)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.delete.serial.im1.error", stockTransFullDTO.getFromSerial(), stockTransFullDTO.getToSerial());
            }

            totalSucess = totalSucessIM2;
            totalRecord = DataUtil.safeToLong(resultTmp.get(HASHMAP_KEY_TOTAL_RECORD_IM2));
            if (totalSucess != null && totalSucess.intValue() <= 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.record.error", totalRecord);
            }
            //insert bang theo file
        } else if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeExport)) {
            boolean isNumberSerial = Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId());
            String[] arrSerial = serialList.split("&");
            for (String s : arrSerial) {
                String[] tmpArrSerial = s.split(":");
                String fromSerial = tmpArrSerial[0];
                String toSerial = tmpArrSerial[1];
                boolean isCheck;
                if(!isNumberSerial) {
                    if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                        continue;
                    }
                    BigInteger fromSerialInput = new BigInteger(fromSerial);
                    BigInteger toSerialInput = new BigInteger(toSerial);
                    Long quantity = toSerialInput.subtract(fromSerialInput).longValue() + 1L;
                    isCheck = quantity.intValue() > 0;
                } else {
                    isCheck = fromSerial.equals(toSerial);
                }
                if (isCheck) {
                    HashMap resultTmp = exportDataBySerialRange(stockTransDTOSave.getStockTransId(), stockTransDetailDTOSave.getStockTransDetailId(), stockTransFullDTO,
                            fromSerial, toSerial, sysDate, isDeleteIM1);
                    //xu ly check xoa dong bo o IM1, voi IM2 neu xoa ko thong nhat thi rolbak bao loi luon
                    Long totalSucessIM2 = DataUtil.safeToLong(resultTmp.get(HASHMAP_KEY_NUMBER_SUCCESS_RECORD_IM2));
                    Long totalSucessIM1 = DataUtil.safeToLong(resultTmp.get(HASHMAP_KEY_NUMBER_SUCCESS_RECORD_IM1));
                    if (isDeleteIM1 && !DataUtil.safeEqual(totalSucessIM1, totalSucessIM2)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.delete.serial.im1.error", fromSerial, toSerial);
                    }

                    totalSucess += totalSucessIM2;
                    totalRecord += DataUtil.safeToLong(resultTmp.get(HASHMAP_KEY_TOTAL_RECORD_IM2));
                }
            }
            if (totalSucess != null && totalSucess.intValue() <= 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.record.error", totalRecord);
            }
        }
        //update lai stockTransDetail
        stockTransDetailDTOSave.setQuantity(totalSucess);
        stockTransDetailService.save(stockTransDetailDTOSave);
        //insert stockTotal, stockTotalAudit
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setExportStock(true);
        flagStockDTO.setInsertStockTotalAudit(true);
        flagStockDTO.setExpAvailableQuantity(-1L);
        flagStockDTO.setExpCurrentQuantity(-1L);
        //validate so luong dap ung IM2
        baseValidateService.doQuantityAvailableValidate(stockTransDTOSave, Lists.newArrayList(stockTransDetailDTOSave));
        //validate so luong thuc te IM2
        baseValidateService.doCurrentQuantityValidate(stockTransDTOSave, Lists.newArrayList(stockTransDetailDTOSave));
        //xu ly luu kho
        doSaveStockTotal(stockTransDTOSave, Lists.newArrayList(stockTransDetailDTOSave), flagStockDTO, stockTransActionDTO1);

        //cap nhap stock num
        StaffDTO staff = staffService.findOne(staffDTO.getStaffId());
        if (staff != null) {
            staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1L);
            staffService.save(staff);
        }
        String[] arrParam = {DataUtil.safeToString(totalSucess), DataUtil.safeToString(totalRecord)};
        message.setSuccess(true);
        message.setKeyMsg("stock.export.to.partner.record.sucess");
        message.setParamsMsg(arrParam);
        message.setDescription(getTextParam("stock.export.to.partner.record.sucess", DataUtil.safeToString(totalSucess), DataUtil.safeToString(totalRecord)));
        message.setStockTransActionId(stockTransActionDTOSave.getStockTransActionId());
        return message;
    }

    /**
     * ham xu ly xoa serial va insert stocktransSerial
     * @author thanhnt
     * @param stockTransId
     * @param stockTransDetailId
     * @param stockTransFullDTO
     * @param fromSerial
     * @param toSerial
     * @param sysDate
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    private HashMap exportDataBySerialRange(Long stockTransId, Long stockTransDetailId, StockTransFullDTO stockTransFullDTO, String fromSerial, String toSerial,
                                            Date sysDate, boolean isDeleteIM1) throws LogicException, Exception {
        HashMap result = new HashMap();
        boolean isCharacterSerial = Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId())
                || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(stockTransFullDTO.getProductOfferTypeId());
        boolean isHandset = Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId());
        //xu ly xoa o IM 2
        int executeResultIM2 = stockTransRepo.deleteSerialExportRange(stockTransFullDTO.getTableName(), fromSerial, toSerial, stockTransFullDTO.getProdOfferId(),
                stockTransFullDTO.getStateId(), stockTransFullDTO.getFromOwnerId(), isCharacterSerial);
        result.put(HASHMAP_KEY_NUMBER_SUCCESS_RECORD_IM2, executeResultIM2);
        if (isHandset) {
            result.put(HASHMAP_KEY_TOTAL_RECORD_IM2, 1L);
        } else {
            Long quantity = new BigInteger(toSerial).subtract(new BigInteger(fromSerial)).longValue() + 1L;
            result.put(HASHMAP_KEY_TOTAL_RECORD_IM2, quantity);
        }
        //xu ly xoa serial IM1
        if (isDeleteIM1) {
            String tableNameIM1 = Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(stockTransFullDTO.getProductOfferTypeId()) ?
                    Const.STOCK_TYPE.STOCK_ACCESSORIES_NAME : IMServiceUtil.getTableNameByOfferType(stockTransFullDTO.getProductOfferTypeId());
            int executeResultIM1 = stockTransRepo.deleteSerialIM1ExportRange(tableNameIM1, fromSerial, toSerial, stockTransFullDTO.getProdOfferId(),
                    stockTransFullDTO.getStateId(), stockTransFullDTO.getFromOwnerId(), isHandset);
            result.put(HASHMAP_KEY_NUMBER_SUCCESS_RECORD_IM1, executeResultIM1);
            if (isHandset) {
                result.put(HASHMAP_KEY_TOTAL_RECORD_IM1, 1L);
            } else {
                Long quantity = new BigInteger(toSerial).subtract(new BigInteger(fromSerial)).longValue() + 1L;
                result.put(HASHMAP_KEY_TOTAL_RECORD_IM1, quantity);
            }
        }

        //insert data bang stockTransSerial
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setStockTransDetailId(stockTransDetailId);
        stockTransSerialDTO.setStateId(stockTransFullDTO.getStateId());
        stockTransSerialDTO.setStockTransId(stockTransId);
        stockTransSerialDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
        stockTransSerialDTO.setFromSerial(fromSerial);
        stockTransSerialDTO.setToSerial(toSerial);
        stockTransSerialDTO.setQuantity(DataUtil.safeToLong(executeResultIM2));
        stockTransSerialDTO.setCreateDatetime(sysDate);
        stockTransSerialService.save(stockTransSerialDTO);
        return result;
    }

    private void validateEmptyData(StockTransFullDTO stockTransFullDTO, String typeExport, String serialList) throws LogicException, Exception {
        //xu ly validate empty chung
        if (DataUtil.isNullOrEmpty(stockTransFullDTO.getActionCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.code.field.not.blank");
        }
        stockTransFullDTO.setActionCode(stockTransFullDTO.getActionCode().trim());
        if (!DataUtil.validateStringByPattern(stockTransFullDTO.getActionCode(), Const.REGEX.CODE_REGEX) || stockTransFullDTO.getActionCode().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.order.transCode.error.format.msg");
        }
        ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(stockTransFullDTO.getProductOfferTypeId());
        if (productOfferTypeDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.prodType.not.valid");
        }
        if (!Const.STOCK_TYPE.STOCK_NO_SERIAL.equals(stockTransFullDTO.getProductOfferTypeId()) &&  DataUtil.isNullOrEmpty(productOfferTypeDTO.getTableName())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.not.support.input.serial");
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransFullDTO.getProdOfferId());
        if (productOfferingDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.prod.not.valid");
        }

        stockTransFullDTO.setTableName(productOfferTypeDTO.getTableName());
        stockTransFullDTO.setProdOfferCode(productOfferingDTO.getCode());

        //validate empty so luong
        if (Const.EXPORT_PARTNER.EXP_BY_QUANTITY.equals(typeExport)) {
            if (DataUtil.isNullOrEmpty(stockTransFullDTO.getStrQuantity()) || DataUtil.safeToLong(stockTransFullDTO.getStrQuantity()) < 0L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.order.stock.number.format.msg");
            }
            //validate empty dai serial
        } else if (Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE.equals(typeExport)) {
            String fromSerial = stockTransFullDTO.getFromSerial();
            String toSerial = stockTransFullDTO.getToSerial();
            if (DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.serial.valid.check.empty");
            }
            if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId())) {
                if (!DataUtil.safeEqual(stockTransFullDTO.getFromSerial(), stockTransFullDTO.getToSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.serial.valid.hanset.valid.file");
                }
            } else {
                if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.serial.valid.format", productOfferingDTO.getName());
                }
                BigInteger fromSerialInput = new BigInteger(fromSerial);
                BigInteger toSerialInput = new BigInteger(toSerial);
                BigInteger result = toSerialInput.subtract(fromSerialInput);
                if (result.compareTo(BigInteger.ZERO) < 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.validate.inspect.fromSerial.less.toSerial");
                }
                if (DataUtil.isNullOrEmpty(stockTransFullDTO.getStrQuantity()) || !DataUtil.validateStringByPattern(stockTransFullDTO.getStrQuantity(), Const.REGEX.NUMBER_REGEX)
                        || DataUtil.safeToLong(stockTransFullDTO.getStrQuantity()).compareTo(0L) <= 0 ) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.order.stock.number.format.msg");
                }
                BigInteger tmpQuantity = result.add(BigInteger.ONE);
                if (tmpQuantity.compareTo(new BigInteger(stockTransFullDTO.getStrQuantity())) != 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.number.valid.serial");
                }
            }
        } else if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeExport)) {
            if (DataUtil.isNullOrEmpty(serialList)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "search.isdn.file.empty");
            }
        }
    }
}


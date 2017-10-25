package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.StaffIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.exception.HandleException;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.repo.StockTransSerialRepo;
import com.viettel.bccs.inventory.service.*;
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
import java.util.List;

/**
 * Created by thanhnt77 11/05/2016
 * Service xuat tra hang cho doi tac
 */
@Service
public class BaseStockExpToPartnerBalanceService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockExpToPartnerBalanceService.class);

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
    private OptionSetValueService optionSetValueService;
    @Autowired
    private BaseValidateService baseValidateService;

    @Autowired
    private StockTransSerialRepo stockTransSerialRepo;

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
     * @throws com.viettel.fw.Exception.LogicException
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public BasePartnerMessage createTransToPartner(StockTransFullDTO stockTransFullDTO, StaffDTO staffDTO, String typeExport, String serialList, RequiredRoleMap requiredRoleMap) throws HandleException, LogicException, Exception {
        BasePartnerMessage message = new BasePartnerMessage();
        //xu ly validate empty

        validateEmptyData(stockTransFullDTO, typeExport, serialList, staffDTO);

        Date sysDate = getSysDate(em);
        //insert bang stockTrans
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setToOwnerId(stockTransFullDTO.getToOwnerId());
        stockTransDTO.setToOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
        stockTransDTO.setFromOwnerId(stockTransFullDTO.getFromOwnerId());
        stockTransDTO.setFromOwnerType(DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()));
        stockTransDTO.setCreateDatetime(sysDate);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        stockTransDTO.setReasonId(Const.STOCK_TRANS.EXPORT_STOCK_PROCESS_REASON_ID);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setNote(getText("mn.stock.partner.export.stock"));
        stockTransDTO.setCheckErp(stockTransFullDTO.getSyncERP());
        StockTransDTO stockTransDTOSave = stockTransService.save(stockTransDTO);

        //insert bang stockTransAction
        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStockTransId(stockTransDTOSave.getStockTransId());
        stockTransActionDTO1.setActionCode(stockTransFullDTO.getActionCode().trim());
        stockTransActionDTO1.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionDTO1.setNote(stockTransFullDTO.getNote());
        stockTransActionDTO1.setCreateDatetime(sysDate);
        stockTransActionDTO1.setCreateUser(staffDTO.getStaffCode());
        stockTransActionDTO1.setActionStaffId(staffDTO.getStaffId());
        StockTransActionDTO stockTransActionDTOSave = stockTransActionService.save(stockTransActionDTO1);

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
        stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
        stockTransDetailDTO.setPrice(null);
        stockTransDetailDTO.setAmount(null);
        stockTransDetailDTO.setCreateDatetime(sysDate);

        StockTransDetailDTO stockTransDetailDTOSave = stockTransDetailService.save(stockTransDetailDTO);

        //insert bang theo so luong
        Long totalSucess = 0L;
        Long totalRecord = 0L;
        //check xem co xu ly xoa IM1
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList();
        if (Const.EXPORT_PARTNER.EXP_BY_QUANTITY.equals(typeExport)) {
            totalSucess = DataUtil.safeToLong(stockTransFullDTO.getStrQuantity());
            totalRecord = totalSucess;
            //insert bang theo dai so
        } else if (Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE.equals(typeExport)) {
            //xu ly check ton tai dai serial
            String fromSerial = stockTransFullDTO.getFromSerial();
            String toSerial = stockTransFullDTO.getToSerial();
            List<String> lsSerialExist = stockTransSerialRepo.getListSerialExist(fromSerial, toSerial, stockTransFullDTO.getProdOfferId(),
                    stockTransFullDTO.getFromOwnerId(), DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()), stockTransFullDTO.getTableName(), Const.GOODS_STATE.NEW);
            int countSerial = lsSerialExist.size();

            if (!DataUtil.safeEqual(stockTransFullDTO.getQuantity(), countSerial)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.partner.balance.fromSerial.toSerial.invalid", fromSerial, toSerial);
            }

            StockTransSerialDTO serialDTO = new StockTransSerialDTO();
            serialDTO.setFromSerial(fromSerial);
            serialDTO.setToSerial(toSerial);
            serialDTO.setQuantity(stockTransFullDTO.getQuantity());
            serialDTO.setStateId(stockTransFullDTO.getStateId());
            serialDTO.setStockTransId(stockTransDTOSave.getStockTransId());
            serialDTO.setCreateDatetime(stockTransDTOSave.getCreateDatetime());
            serialDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
            serialDTO.setStockTransDetailId(stockTransDetailDTOSave.getStockTransDetailId());
            StockTransSerialDTO stockTransSerialDTOSave = stockTransSerialService.save(serialDTO);
            lstSerial.add(stockTransSerialDTOSave);
            totalSucess = DataUtil.safeToLong(countSerial);

            //insert bang theo file
        } else if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeExport)) {
            String[] arrSerial = serialList.split("&");
            Long quantity ;
            for (String s : arrSerial) {
                String[] tmpArrSerial = s.split(":");
                String fromSerial = tmpArrSerial[0];
                String toSerial = tmpArrSerial[1];
                List<String> lsSerialExist = stockTransSerialRepo.getListSerialExist(fromSerial, toSerial, stockTransFullDTO.getProdOfferId(),
                        stockTransFullDTO.getFromOwnerId(), DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()), stockTransFullDTO.getTableName(), Const.GOODS_STATE.NEW);
                int countSerial = lsSerialExist.size();
                if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId())) {
                    quantity = 1L;
                } else {
                    quantity = new BigInteger(toSerial).subtract(new BigInteger(fromSerial)).longValue() + 1L;
                }
                if (!DataUtil.safeEqual(quantity, countSerial)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.partner.balance.fromSerial.toSerial.invalid", fromSerial, toSerial);
                }
                StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                serialDTO.setFromSerial(fromSerial);
                serialDTO.setToSerial(toSerial);
                serialDTO.setQuantity(quantity);
                serialDTO.setStateId(stockTransFullDTO.getStateId());
                serialDTO.setStockTransId(stockTransDTOSave.getStockTransId());
                serialDTO.setCreateDatetime(stockTransDTOSave.getCreateDatetime());
                serialDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                serialDTO.setStockTransDetailId(stockTransDetailDTOSave.getStockTransDetailId());
                StockTransSerialDTO stockTransSerialDTOSave = stockTransSerialService.save(serialDTO);
                lstSerial.add(stockTransSerialDTOSave);
                totalSucess += countSerial;
            }
        }
        //update lai stockTransDetail
        stockTransDetailDTOSave.setQuantity(totalSucess);
        stockTransDetailService.save(stockTransDetailDTOSave);
        //insert stockTotal, stockTotalAudit
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setExportStock(true);
        flagStockDTO.setInsertStockTotalAudit(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);

        //validate hang hoa ko vuot qua tong han muc kho xuat
        baseValidateService.doQuantityAvailableValidate(stockTransDTOSave, Lists.newArrayList(stockTransDetailDTOSave));

        //xu ly tru kho
        doSaveStockTotal(stockTransDTOSave, Lists.newArrayList(stockTransDetailDTOSave), flagStockDTO, stockTransActionDTO1);

        //xu ly update stock_x voi mat hang co serial
        if (!Const.EXPORT_PARTNER.EXP_BY_QUANTITY.equals(typeExport)) {
            stockTransDetailDTOSave.setLstStockTransSerial(lstSerial);
            stockTransDetailDTOSave.setTableName(stockTransFullDTO.getTableName());
            flagStockDTO.setSerialListFromTransDetail(true);
            stockTransDetailDTOSave.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());
            stockTransDetailDTO.setCheckSerial(DataUtil.safeToLong(stockTransFullDTO.getCheckSerial()));
            flagStockDTO.setNewStatus(Const.STATE_STATUS.SALE);
            flagStockDTO.setOldStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            doSaveStockGoods(stockTransDTOSave, Lists.newArrayList(stockTransDetailDTOSave), flagStockDTO);
        }

        //cap nhap stock num
        doIncreaseStockNum(stockTransActionDTOSave, Const.STOCK_TRANS.TRANS_CODE_PX, requiredRoleMap);

        String[] arrParam = {DataUtil.safeToString(totalSucess), DataUtil.safeToString(totalRecord)};
        message.setSuccess(true);
        message.setKeyMsg("create.partner.balance.sucess");
        message.setParamsMsg(arrParam);
        message.setDescription(getTextParam("create.partner.balance.sucess"));
        return message;
    }

    private void validateEmptyData(StockTransFullDTO stockTransFullDTO, String typeExport, String serialList, StaffDTO staffDTO) throws LogicException, Exception {
        //xu ly validate empty chung


        String actionCode = DataUtil.safeToString(stockTransFullDTO.getActionCode()).trim();
        if (DataUtil.isNullOrEmpty(actionCode)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.transCode.require.msg");
        } else if (!DataUtil.validateStringByPattern(stockTransFullDTO.getActionCode(), Const.REGEX.CODE_REGEX) || stockTransFullDTO.getActionCode().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.transCode.error.format.msg");
        } else if (DataUtil.safeEqual(staffDTO.getShopId(), Const.VT_SHOP_ID)
                && !staffService.validateTransCode(actionCode, staffDTO.getStaffId(), Const.STOCK_TRANS_TYPE.EXPORT)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.expNote.invalid");
        }


        ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(stockTransFullDTO.getProductOfferTypeId());
        if (productOfferTypeDTO == null || !Const.STATUS.ACTIVE.equals(productOfferTypeDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.prodType.not.valid");
        }
        if (!Const.STOCK_TYPE.STOCK_NO_SERIAL.equals(stockTransFullDTO.getProductOfferTypeId()) &&  DataUtil.isNullOrEmpty(productOfferTypeDTO.getTableName())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.not.support.input.serial");
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransFullDTO.getProdOfferId());
        if (productOfferingDTO == null || !Const.STATUS.ACTIVE.equals(productOfferingDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.prod.not.valid");
        }

        stockTransFullDTO.setTableName(productOfferTypeDTO.getTableName());
        stockTransFullDTO.setProdOfferCode(productOfferingDTO.getCode());

        //validate empty so luong
        if (Const.EXPORT_PARTNER.EXP_BY_QUANTITY.equals(typeExport)) { //case14
            if (DataUtil.isNullOrEmpty(stockTransFullDTO.getStrQuantity()) || DataUtil.safeToLong(stockTransFullDTO.getStrQuantity()) < 0L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.order.stock.number.format.msg");
            }// case 15
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
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "input.list.product.file.number.valid.serial");
                }
                if (tmpQuantity.compareTo(new BigInteger("500000")) > 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.serial.valid.range");
                }
            }
        } else if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeExport)) {
            if (DataUtil.isNullOrEmpty(serialList)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "search.isdn.file.empty");
            }
        }
    }
}


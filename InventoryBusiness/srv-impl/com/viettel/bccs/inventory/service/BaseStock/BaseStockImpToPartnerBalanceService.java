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
import java.util.*;

/**
 * Created by thanhnt77 11/05/2016
 * Service nhap tra hang can kho cho doi tac
 */
@Service
public class BaseStockImpToPartnerBalanceService extends BaseStockPartnerService {

    public static final Logger logger = Logger.getLogger(BaseStockImpToPartnerBalanceService.class);

    private final Integer HASHMAP_KEY_NUMBER_SUCCESS_RECORD = 1; //key hashMap (so luong ban ghi import thanh cong)
    private final Integer HASHMAP_KEY_TOTAL_RECORD = 2; //key hashMap (so luong ban ghi bi loi)
    private final Integer HASHMAP_KEY_ERROR_LIST_SERIAL = 3; //key hashMap list serial va noi dung loi

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
    private ShopService shopService;
    @Autowired
    private StaffIm1Service staffIm1Service;
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
     *
     * @param stockTransFullDTO
     * @param staffDTO
     * @param typeImport
     * @param serialList
     * @return
     * @throws com.viettel.fw.Exception.LogicException
     * @throws Exception
     * @author thanhnt77
     */
    @Transactional(rollbackFor = Exception.class)
    public BasePartnerMessage createTransToPartner(StockTransFullDTO stockTransFullDTO, StaffDTO staffDTO, String typeImport,
                                                   String serialList, RequiredRoleMap requiredRoleMap) throws HandleException, LogicException, Exception {
        BasePartnerMessage message = new BasePartnerMessage();
        stockTransFullDTO.setActionStaffCode(staffDTO.getStaffCode());
        //xu ly validate empty
        validateInput(stockTransFullDTO, typeImport, serialList);

        Date sysDate = getSysDate(em);
        //insert bang stockTrans
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(stockTransFullDTO.getFromOwnerId());
        stockTransDTO.setFromOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
        stockTransDTO.setToOwnerId(stockTransFullDTO.getToOwnerId());
        stockTransDTO.setToOwnerType(DataUtil.safeToLong(stockTransFullDTO.getToOwnerType()));
        stockTransDTO.setCreateDatetime(sysDate);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        stockTransDTO.setReasonId(Const.STOCK_TRANS.IMPORT_STOCK_PROCESS_REASON_ID);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setNote(getText("mn.stock.partner.import.stock"));
        stockTransDTO.setCheckErp(stockTransFullDTO.getSyncERP());
        StockTransDTO stockTransDTOSave = stockTransService.save(stockTransDTO);

        //insert bang stockTransAction
        StockTransActionDTO stockTransActionDTO1 = new StockTransActionDTO();
        stockTransActionDTO1.setStockTransId(stockTransDTOSave.getStockTransId());
        stockTransActionDTO1.setActionCode(stockTransFullDTO.getActionCode());
        stockTransActionDTO1.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionDTO1.setNote(stockTransFullDTO.getNote());
        stockTransActionDTO1.setCreateDatetime(sysDate);
        stockTransActionDTO1.setCreateUser(staffDTO.getStaffCode());
        stockTransActionDTO1.setActionStaffId(staffDTO.getStaffId());
        StockTransActionDTO stockTransActionDTOSave = stockTransActionService.save(stockTransActionDTO1);

        StockTransActionDTO stockTransActionDTO2 = DataUtil.cloneBean(stockTransActionDTO1);
        stockTransActionDTO2.setActionCode(null);
        stockTransActionDTO2.setNote(null);
        stockTransActionDTO2.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockTransActionDTO2);

        //insert data bang stocktransDetail
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setStockTransId(stockTransDTOSave.getStockTransId());
        stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
        stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
        stockTransDetailDTO.setQuantity(0L);//tam thoi set = 0
        stockTransDetailDTO.setPrice(null);
        stockTransDetailDTO.setAmount(null);
        stockTransDetailDTO.setCreateDatetime(sysDate);

        StockTransDetailDTO stockTransDetailDTOSave = stockTransDetailService.save(stockTransDetailDTO);

        Long totalSucess = 0L;
        Long totalRecord = 0L;
        List<String> lsSerialError = Lists.newArrayList();
        //check xem co xu ly xoa IM1
        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        //TODO
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }
        //insert bang theo so luong
        //TODO
        if (Const.EXPORT_PARTNER.EXP_BY_QUANTITY.equals(typeImport)) {
            totalSucess = DataUtil.safeToLong(stockTransFullDTO.getStrQuantity());
            totalRecord = totalSucess;

        } else if (Const.EXPORT_PARTNER.EXP_BY_SERIAL_RANGE.equals(typeImport)) {
            if (Const.STOCK_TYPE.STOCK_NO_SERIAL.equals(stockTransFullDTO.getProductOfferTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.not.support.input.serial.file");
            }
            //nhap theo dai
            Map mapValid = importDataBySerialRange(stockTransDTOSave.getStockTransId(), stockTransDetailDTOSave.getStockTransDetailId(), stockTransFullDTO, stockTransDTOSave.getCreateDatetime(), isCheckIm1);
            totalRecord = DataUtil.safeToLong(mapValid.get(HASHMAP_KEY_TOTAL_RECORD));
            totalSucess = DataUtil.safeToLong(mapValid.get(HASHMAP_KEY_NUMBER_SUCCESS_RECORD));
        } else if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeImport)) {
            if (Const.STOCK_TYPE.STOCK_NO_SERIAL.equals(stockTransFullDTO.getProductOfferTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.not.support.input.serial.file");
            }
            //nhap theo file
            Map mapValid = importDataByFile(stockTransDTOSave.getStockTransId(), stockTransDetailDTOSave.getStockTransDetailId(), stockTransFullDTO, stockTransDTOSave.getCreateDatetime(), serialList, isCheckIm1);

            lsSerialError = (List<String>) mapValid.get(HASHMAP_KEY_ERROR_LIST_SERIAL);
            totalRecord = DataUtil.safeToLong(mapValid.get(HASHMAP_KEY_TOTAL_RECORD));
            totalSucess = DataUtil.safeToLong(mapValid.get(HASHMAP_KEY_NUMBER_SUCCESS_RECORD));
        }
        //ko update/insert serial nao thi bao loi
        if (DataUtil.safeEqual(totalSucess, 0L)) {
            throw new HandleException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.import.to.partner.balance.serial.import", lsSerialError);
        }
        //update lai stockTransDetail
        stockTransDetailDTOSave.setQuantity(totalSucess);
        stockTransDetailService.save(stockTransDetailDTOSave);
        //insert stockTotal, stockTotalAudit
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setImportStock(true);
        flagStockDTO.setInsertStockTotalAudit(true);
        flagStockDTO.setImpAvailableQuantity(1L);
        flagStockDTO.setImpCurrentQuantity(1L);
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);

        //xu ly cong kho
        doSaveStockTotal(stockTransDTOSave, Lists.newArrayList(stockTransDetailDTOSave), flagStockDTO, stockTransActionDTO1);

        //cap nhap stock num
        doIncreaseStockNum(stockTransActionDTOSave, Const.STOCK_TRANS.TRANS_CODE_PN, requiredRoleMap);

        String[] arrParam = {DataUtil.safeToString(totalSucess), DataUtil.safeToString(totalRecord)};
        message.setSuccess(true);
        message.setKeyMsg("create.partner.balance.sucess.import");
        message.setParamsMsg(arrParam);
        message.setDescription(getTextParam("create.partner.balance.sucess.import", DataUtil.safeToString(totalSucess), DataUtil.safeToString(totalRecord)));
        message.setLsSerialErorr(lsSerialError);
        return message;
    }

    /**
     * ham xu ly xoa serial va insert stocktransSerial
     *
     * @param stockTransId
     * @param stockTransDetailId
     * @param stockTransFullDTO
     * @param sysDate
     * @return
     * @throws com.viettel.fw.Exception.LogicException
     * @throws Exception
     * @author thanhnt
     */
    @Transactional(rollbackFor = Exception.class)
    private Map importDataBySerialRange(Long stockTransId, Long stockTransDetailId, StockTransFullDTO stockTransFullDTO, Date sysDate, boolean isCheckIm1) throws LogicException, Exception {

        Map mapValid = new HashMap<>();
        String fromSerial = stockTransFullDTO.getFromSerial();
        String toSerial = stockTransFullDTO.getToSerial();

        boolean isHandset = Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId()) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(stockTransFullDTO.getProductOfferTypeId());

        BigInteger countUpdate;

        String tableName = stockTransFullDTO.getTableName();

        List<BigInteger> lstAll = new ArrayList();
        if (!isHandset) {
            BigInteger fromSerialNum = new BigInteger(fromSerial);
            BigInteger toSerialNum = new BigInteger(toSerial);
            countUpdate = toSerialNum.subtract(fromSerialNum).add(BigInteger.ONE);
            // Danh sach tat cac dai serial
            for (BigInteger iter = fromSerialNum; iter.compareTo(toSerialNum) <= 0; iter = iter.add(BigInteger.ONE)) {
                lstAll.add(iter);
            }
        } else {
            countUpdate = BigInteger.ONE;
        }
        //update stock_x
        int updateCount = updateStockX(tableName, Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(), Const.STOCK_GOODS.STATUS_SALE, Const.STOCK_GOODS.STATUS_NEW,
                stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), fromSerial, toSerial, stockTransId, false, false);

        if (isCheckIm1 && updateCount != 0) {
            int updateCountIm1 = updateStockXIM1(tableName, Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(), Const.STOCK_GOODS.STATUS_SALE, Const.STOCK_GOODS.STATUS_NEW,
                    stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), fromSerial, toSerial, stockTransId, false, false);
            if (updateCount != updateCountIm1) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.to.partner.balance.update.im1.invalid", fromSerial, toSerial, stockTransFullDTO.getProdOfferName());
            }
        }
        //check so luong serial update trong DB va nhap vao xem co dong nhat, neu ko dong nhat thi xu ly insert stock-x
        if (!DataUtil.safeToString(countUpdate).equals(DataUtil.safeToString(updateCount))) {
            if (!isHandset) {
                List<String> lsSerialExist = stockTransSerialRepo.getListSerialExist(fromSerial, toSerial, stockTransFullDTO.getProdOfferId(), null, null, stockTransFullDTO.getTableName(), null);
                int countSerial = lsSerialExist.size();
                //Check nếu số bản ghi cập nhật != số serial tồn tại --> Có serial ở trạng thái != 0
                if (countSerial > 0 && !DataUtil.safeEqual(updateCount, countSerial)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.partner.balance.fromSerial.toSerial.exist.db");
                }
                List<BigInteger> lsExistSerialExistDB = Lists.newArrayList();
                lsSerialExist.stream().forEach(x -> lsExistSerialExistDB.add(new BigInteger(x)));
                //lay ra danh sach cac serial k ton tai
                lstAll.removeAll(lsExistSerialExistDB);

                //xu ly insert stock_x voi nhung serial ko ton tai
                if (!DataUtil.isNullOrEmpty(lstAll)) {
                    int totalCountInsert = 0;
                    int totalCountInsertIm1 = 0;
                    for (BigInteger serial : lstAll) {
                        totalCountInsert += insertStockX(tableName, Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(), Const.STOCK_GOODS.STATUS_NEW,
                                stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), serial.toString(), stockTransFullDTO.getActionStaffCode(), sysDate, stockTransId, stockTransFullDTO.getTelectomServiceId());

                        if (isCheckIm1) {
                            totalCountInsertIm1 += insertStockXIM1(tableName, Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(), Const.STOCK_GOODS.STATUS_NEW,
                                    stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), serial.toString(), stockTransFullDTO.getActionStaffCode(), sysDate, stockTransId, stockTransFullDTO.getTelectomServiceId());
                        }
                    }
                    if (totalCountInsert != totalCountInsertIm1) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.to.partner.balance.update.im1.invalid", fromSerial, toSerial, stockTransFullDTO.getProdOfferName());
                    }
                    updateCount += totalCountInsert;
                    if (!DataUtil.safeToString(countUpdate).equals(DataUtil.safeToString(updateCount))) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.to.partner.balance.update.serial.invalid");
                    }
                }
            } else {
               /* List<String> lsSerialExist = stockTransSerialRepo.getListSerialExist(fromSerial, toSerial, stockTransFullDTO.getProdOfferId(), null, null, stockTransFullDTO.getTableName(), null);
                int countSerial = lsSerialExist.size();
                //Check nếu số bản ghi cập nhật != số serial tồn tại --> Có serial ở trạng thái != 0
                if (countSerial > 0 && !DataUtil.safeEqual(updateCount, countSerial)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.partner.balance.fromSerial.toSerial.exist.db");
                }*/
                int insertStockX = insertStockX(tableName, Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(), Const.STOCK_GOODS.STATUS_NEW,
                        stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), fromSerial, stockTransFullDTO.getActionStaffCode(), sysDate, stockTransId, stockTransFullDTO.getTelectomServiceId());
                if (insertStockX <= 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.partner.balance.fromSerial.toSerial.exist.db");
                }

                //TODO không được vì insertStockX đã bắt ở trên
                if (isCheckIm1 && insertStockX != 0) {
                    int insertStockXIm1 = insertStockXIM1(tableName, Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(), Const.STOCK_GOODS.STATUS_NEW,
                            stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), fromSerial, stockTransFullDTO.getActionStaffCode(), sysDate, stockTransId, stockTransFullDTO.getTelectomServiceId());
                    if (insertStockXIm1 <= 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.partner.balance.fromSerial.toSerial.exist.db.bccs1");
                    }
                    if (!DataUtil.safeToString(insertStockX).equals(DataUtil.safeToString(insertStockXIm1))) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.to.partner.balance.update.serial.invalid");
                    }
                }
                updateCount += insertStockX;
            }
        }

        //update stock_detail

        //thuc hien insert stock_trans_serial
        StockTransSerialDTO serialDTO = new StockTransSerialDTO();
        serialDTO.setFromSerial(fromSerial);
        serialDTO.setToSerial(toSerial);
        serialDTO.setQuantity(countUpdate.longValue());
        serialDTO.setStateId(stockTransFullDTO.getStateId());
        serialDTO.setStockTransId(stockTransId);
        serialDTO.setCreateDatetime(sysDate);
        serialDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
        serialDTO.setStockTransDetailId(stockTransDetailId);
        stockTransSerialService.save(serialDTO);

        mapValid.put(HASHMAP_KEY_NUMBER_SUCCESS_RECORD, updateCount);
        mapValid.put(HASHMAP_KEY_TOTAL_RECORD, countUpdate);

        return mapValid;
    }

    /**
     * ham xu ly xoa serial va insert stocktransSerial
     *
     * @param stockTransId
     * @param stockTransDetailId
     * @param stockTransFullDTO
     * @param sysDate
     * @return
     * @throws com.viettel.fw.Exception.LogicException
     * @throws Exception
     * @author thanhnt
     */
    @Transactional(rollbackFor = Exception.class)
    private Map importDataByFile(Long stockTransId, Long stockTransDetailId, StockTransFullDTO stockTransFullDTO, Date sysDate, String serialList, boolean isCheckIm1) throws LogicException, Exception {
        Map mapValid = new HashMap<>();

        Long totalSucess = 0L;

        String[] arrSerial = serialList.split(" ");
        List<String> lsSerialError = Lists.newArrayList();
        Long totalRecord = 0L;
        for (String serial : arrSerial) {
            if (DataUtil.isNullOrEmpty(serial)) {
                continue;
            }
            totalRecord++;
            if (!DataUtil.validateStringByPattern(serial, Const.REGEX.SERIAL_REGEX) || serial.getBytes("UTF-8").length > 20) {
                lsSerialError.add(serial + " " + getText("stock.import.to.partner.balance.serial.length") + "\n");
                continue;
            }
            if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId())) {
                if (!DataUtil.validateStringByPattern(serial, Const.REGEX.NUMBER_REGEX)) {
                    lsSerialError.add(serial + " " + getText("stock.import.to.partner.balance.serial.not.handset.invalid") + "\n");
                    continue;
                }
            }
            //update stock_x
            int updateStock = updateStockX(stockTransFullDTO.getTableName(), Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(),
                    Const.STOCK_GOODS.STATUS_SALE, Const.STOCK_GOODS.STATUS_NEW, stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), serial, serial, stockTransId, true, true);

            //update thanh cong
            if (updateStock == 1) {
                //update bcc2 thanh cong se update bccs1
                if (isCheckIm1) {
                    int updateStockIm1 = updateStockXIM1(stockTransFullDTO.getTableName(), Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(),
                            Const.STOCK_GOODS.STATUS_SALE, Const.STOCK_GOODS.STATUS_NEW, stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), serial, serial, stockTransId, true, true);
                    if (updateStock != updateStockIm1) {
                        lsSerialError.add(serial + " " + getText("stock.import.to.partner.balance.serial.update.im1") + "\n");
                        continue;
                    }
                }
            } else {
                // update that bai xu ly insert stock_x
                int insertStock = insertStockX(stockTransFullDTO.getTableName(), Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(),
                        Const.STOCK_GOODS.STATUS_NEW, stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), serial, stockTransFullDTO.getActionStaffCode(),
                        sysDate, stockTransId, stockTransFullDTO.getTelectomServiceId());
                if (insertStock != 1) {
                    lsSerialError.add(serial + " " + getText("stock.import.to.partner.balance.serial.insert") + "\n");
                    continue;
                }
                if (isCheckIm1) {
                    int insertStockIm1 = insertStockXIM1(stockTransFullDTO.getTableName(), Const.OWNER_TYPE.SHOP_LONG, stockTransFullDTO.getToOwnerId(),
                            Const.STOCK_GOODS.STATUS_NEW, stockTransFullDTO.getStateId(), stockTransFullDTO.getProdOfferId(), serial, stockTransFullDTO.getActionStaffCode(),
                            sysDate, stockTransId, stockTransFullDTO.getTelectomServiceId());
                    if (insertStockIm1 != 1) {
                        lsSerialError.add(serial + " " + getText("stock.import.to.partner.balance.serial.insert.im1") + "\n");
                        continue;
                    }
                }
            }
            totalSucess++;
            StockTransSerialDTO serialDTO = new StockTransSerialDTO();
            serialDTO.setFromSerial(serial);
            serialDTO.setToSerial(serial);
            serialDTO.setQuantity(1L);
            serialDTO.setStateId(stockTransFullDTO.getStateId());
            serialDTO.setStockTransId(stockTransId);
            serialDTO.setCreateDatetime(sysDate);
            serialDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
            serialDTO.setStockTransDetailId(stockTransDetailId);
            stockTransSerialService.save(serialDTO);
        }

        mapValid.put(HASHMAP_KEY_NUMBER_SUCCESS_RECORD, totalSucess);
        mapValid.put(HASHMAP_KEY_TOTAL_RECORD, totalRecord);
        mapValid.put(HASHMAP_KEY_ERROR_LIST_SERIAL, lsSerialError);
        return mapValid;
    }

    private void validateInput(StockTransFullDTO stockTransFullDTO, String typeExport, String serialList) throws LogicException, Exception {
        //xu ly validate empty chung
        if (DataUtil.isNullOrEmpty(stockTransFullDTO.getActionCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.code.field.not.blank");
        }
        stockTransFullDTO.setActionCode(stockTransFullDTO.getActionCode().trim());
        if (!DataUtil.validateStringByPattern(stockTransFullDTO.getActionCode(), Const.REGEX.CODE_REGEX) || stockTransFullDTO.getActionCode().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.order.transCode.error.format.msg");
        }

        //check ton tai kho
        ShopDTO shopDTO = shopService.findOne(stockTransFullDTO.getToOwnerId());
        if (shopDTO == null || !Const.STATUS_ACTIVE.equals(shopDTO.getStatus())) {
            throw new LogicException("04", "warranty.create.underlying.fromOwnerId.imp.shop.notfound", stockTransFullDTO.getToOwnerId());
        }

        ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(stockTransFullDTO.getProductOfferTypeId());
        if (productOfferTypeDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.prodType.not.valid");
        }
        if (!Const.STOCK_TYPE.STOCK_NO_SERIAL.equals(stockTransFullDTO.getProductOfferTypeId()) && DataUtil.isNullOrEmpty(productOfferTypeDTO.getTableName())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.export.to.partner.not.support.input.serial");
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransFullDTO.getProdOfferId());
        if (productOfferingDTO == null || !Const.STATUS.ACTIVE.equals(productOfferingDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.prod.not.valid");
        }
        stockTransFullDTO.setTelectomServiceId(productOfferingDTO.getTelecomServiceId());
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
                        || DataUtil.safeToLong(stockTransFullDTO.getStrQuantity()).compareTo(0L) <= 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.order.stock.number.format.msg");
                }
                BigInteger tmpQuantity = result.add(BigInteger.ONE);
                if (tmpQuantity.compareTo(new BigInteger(stockTransFullDTO.getStrQuantity())) != 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "input.list.product.file.number.valid.serial");
                }
                if (tmpQuantity.compareTo(new BigInteger("1000")) > 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.range2", "1000");
                }
            }
        } else if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeExport)) {
            if (DataUtil.isNullOrEmpty(serialList)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "search.isdn.file.empty");
            }
        }
    }


}


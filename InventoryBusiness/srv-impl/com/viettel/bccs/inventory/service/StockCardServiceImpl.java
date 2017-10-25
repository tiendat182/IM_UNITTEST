package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.cc.dto.CardInfoProvisioningDTO;
import com.viettel.bccs.cc.dto.HistoryInforCardNumberDTO;
import com.viettel.bccs.im1.dto.VcRequestDTO;
import com.viettel.bccs.im1.service.VcRequestService;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockCard;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.repo.StockCardRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

@Service
public class StockCardServiceImpl extends BaseServiceImpl implements StockCardService {

    private final BaseMapper<StockCard, StockCardDTO> mapper = new BaseMapper(StockCard.class, StockCardDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;
    private String strStateIdAfter;

    @Autowired
    private StockCardRepo repository;
    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private InventoryExternalService inventoryExternalService;

    public static final Logger logger = Logger.getLogger(StockCardService.class);
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CustomerCareWs customerCareWs;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private VcRequestService vcRequestService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReportChangeGoodsService reportChangeGoodsService;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockCardDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockCardDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockCardDTO save(StockCardDTO stockCardDTO) throws Exception {
        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(stockCardDTO)));
    }

    @WebMethod
    public List<StockCardDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSaveStockCard(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, StockTransDetailDTO stockTransDetail, Long newStatus, Long oldStatus,
                                Long newOwnerType, Long oldOwnerType, Long newOwnerId, Long oldOwnerId) throws LogicException, Exception {

//        List<StockTransSerialDTO> lstSerial = stockTransDetail.getLstStockTransSerial();
        List<StockTransSerialDTO> lstSerial;
        if (flagStockDTO.isSerialListFromTransDetail()) {
            lstSerial = stockTransDetail.getLstStockTransSerial();
        } else {
            lstSerial = stockTransSerialService.findByStockTransDetailId(stockTransDetail.getStockTransDetailId());
        }
        if (DataUtil.isNullOrEmpty(lstSerial)) {
            return;
        }
        strStateIdAfter = stockTransDetail.getStrStateIdAfter();
        for (StockTransSerialDTO stockTransSerial : lstSerial) {
            List<StockCard> stockCardList = findStockCard(stockTransDetail, oldStatus, oldOwnerType, oldOwnerId,
                    stockTransSerial.getFromSerial(), stockTransSerial.getToSerial());

            if (DataUtil.isNullOrEmpty(stockCardList) || stockCardList.size() > 1) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(stockTransDetail.getProdOfferId());
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockCard.validate.empty", offeringDTO.getName());
            }

            updateStockCard(flagStockDTO, stockTransDTO, stockTransActionDTO, mapper.toDtoBean(stockCardList.get(0)), stockTransSerial.getFromSerial(), stockTransSerial.getToSerial(), newStatus, newOwnerType, newOwnerId);
        }
    }

    @Override
    public boolean checkNotExistInVC(Long prodOfferId, String fromSerial, String toSerial) throws LogicException, Exception {
        Long checkStatusVT = repository.checkNotExistInVC(prodOfferId, fromSerial, toSerial);
        if (checkStatusVT > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertStockCardFromSaled(Long prodOfferId, String serial) throws LogicException, Exception {
        repository.insertStockCardFromSaled(prodOfferId, serial);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            repository.insertStockCardFromSaledIm1(prodOfferId, serial);
        }
    }

    @Override
    public void insertStockCardFromSaledIm1(Long prodOfferId, String serial) throws LogicException, Exception {
        repository.insertStockCardFromSaledIm1(prodOfferId, serial);
    }

    @Override
    public boolean checkStockCardIm1(Long prodOfferId, String serial) throws LogicException, Exception {
        return repository.checkStockCardIm1(prodOfferId, serial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StockCard> findStockCard(StockTransDetailDTO stockTransDetail, Long oldStatus, Long oldOwnerType,
                                         Long oldOwnerId, String fromSerial, String toSerial) throws LogicException, Exception {
        return repository.findStockCard(stockTransDetail, oldStatus, oldOwnerType, oldOwnerId, fromSerial, toSerial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockCard(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, StockCardDTO stockCard, String fromSerial, String toSerial,
                                Long newStatus, Long newOwnerType, Long newOwnerId) throws LogicException, Exception {

        StockCardDTO stockCardUpdate = DataUtil.cloneBean(stockCard);

        //Neu ton tai dai moi tu dau dai --> insert dai moi
        if (DataUtil.safeToLong(fromSerial) > DataUtil.safeToLong(stockCard.getFromSerial())) {
            StockCardDTO stockCardInsert = DataUtil.cloneBean(stockCard);
            stockCardInsert.setId(null);
            stockCardInsert.setFromSerial(stockCardUpdate.getFromSerial());
            Long toSerialNew = DataUtil.safeToLong(fromSerial) - 1L;
            stockCardInsert.setToSerial(formatSerial(stockCard.getToSerial().length(), toSerialNew));
            stockCardInsert.setQuantity(DataUtil.safeToLong(stockCardInsert.getToSerial()) - DataUtil.safeToLong(stockCardInsert.getFromSerial()) + 1);
            stockCardInsert.setCreateDate(stockTransActionDTO.getCreateDatetime());
            stockCardInsert.setCreateUser(stockTransActionDTO.getCreateUser());

            save(stockCardInsert);

            stockCardUpdate.setFromSerial(formatSerial(stockCard.getFromSerial().length(), DataUtil.safeToLong(fromSerial)));
        }

        //Neu ton tai dai moi den cuoi dai --> insert dai moi
        if (DataUtil.safeToLong(toSerial) < DataUtil.safeToLong(stockCard.getToSerial())) {
            StockCardDTO stockCardInsert = DataUtil.cloneBean(stockCard);
            stockCardInsert.setId(null);
            Long fromSerialNew = DataUtil.safeToLong(toSerial) + 1L;
            stockCardInsert.setFromSerial(formatSerial(stockCard.getFromSerial().length(), fromSerialNew));
            stockCardInsert.setToSerial(stockCardUpdate.getToSerial());
            stockCardInsert.setQuantity(DataUtil.safeToLong(stockCardInsert.getToSerial()) - DataUtil.safeToLong(stockCardInsert.getFromSerial()) + 1);
            stockCardInsert.setCreateDate(stockTransActionDTO.getCreateDatetime());
            stockCardInsert.setCreateUser(stockTransActionDTO.getCreateUser());

            save(stockCardInsert);

            stockCardUpdate.setToSerial(formatSerial(stockCard.getToSerial().length(), DataUtil.safeToLong(toSerial)));
        }

        stockCardUpdate.setStatus(newStatus);
        if (!DataUtil.isNullOrZero(newOwnerId) && !DataUtil.isNullOrZero(newOwnerType)) {
            stockCardUpdate.setOwnerId(newOwnerId);
            stockCardUpdate.setOwnerType(newOwnerType);
        }

        if (Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
            stockCardUpdate.setStateId(DataUtil.safeToLong(strStateIdAfter));
            stockCardUpdate.setCreateDate(stockTransActionDTO.getCreateDatetime());
        } else if (Const.REASON_TYPE.EXPORT_RESCUE.equals(flagStockDTO.getNote())) {
            stockCardUpdate.setStateId(Const.GOODS_STATE.RESCUE);
            stockCardUpdate.setCreateDate(stockTransActionDTO.getCreateDatetime());
        } else if (!DataUtil.isNullOrZero(flagStockDTO.getStateIdForReasonId())) {
            stockCardUpdate.setStateId(flagStockDTO.getStateIdForReasonId());
        }
        stockCardUpdate.setQuantity(DataUtil.safeToLong(stockCardUpdate.getToSerial()) - DataUtil.safeToLong(stockCardUpdate.getFromSerial()) + 1);
        if (flagStockDTO.isUpdateAccountBalance() && !DataUtil.isNullObject(stockTransDTO.getBankplusStatus())) {
            stockCardUpdate.setBankplusStatus(stockTransDTO.getBankplusStatus());
        }
        save(stockCardUpdate);
    }

    private String formatSerial(int serialLength, Long serial) {
        int prefix = serialLength - serial.toString().length();
        String serialFormat = prefix == 0 ? "%d" : ("%0" + String.valueOf(serialLength) + "d");
        return String.format(serialFormat, serial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePincodeForStockCard(Long ownerId, String serial, Long prodOfferId, String pincode, String updateType, StaffDTO staffDTO) throws LogicException, Exception {
        //check mat hang ton tai
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
            if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
            }
        }
        String tempPincodeEncrypt = DataUtil.encryptByDES64(pincode.trim());
        int result = repository.updatePincodeForStockCard(ownerId, serial, prodOfferId, tempPincodeEncrypt, updateType);
        if (result < 1) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.update.pincode.card.update.error");
        }
        //update IM1
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                result = repository.updatePincodeForStockCardIM1(ownerId, serial, prodOfferId, tempPincodeEncrypt, updateType);
                if (result < 1) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.update.pincode.card.update.error.im1");
                }
                //luu action log
                Long actionId = getSequence(emIm1, "bccs_im.action_log_seq");
                if (DataUtil.safeEqual(updateType, Const.UPDATE_PINCODE_CARD.TYPE_NEW)) {
                    updateType = Const.UPDATE_PINCODE_CARD.ACTION_TYPE_ADD_PINCODE;
                } else {
                    updateType = Const.UPDATE_PINCODE_CARD.ACTION_TYPE_UPDATE_PINCODE;
                }
                result = repository.saveActionLog(actionId, updateType, serial, staffDTO.getStaffCode(), staffDTO.getIpAddress(), prodOfferId);
                if (result < 1) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.update.pincode.card.update.error.im1");
                }
                result = repository.saveActionLogDetail(actionId, tempPincodeEncrypt, serial);
                if (result < 1) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.update.pincode.card.update.error.im1");
                }
            }
        }
    }

    public int updateListPincodeForStockCard(List<UpdatePincodeDTO> lstPincodes, Long ownerId, Long prodOfferId, String updateType, StaffDTO staffDTO) throws LogicException, Exception {
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
            if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
            }
        }
        int result = 0;
        Connection conn = null;
        Connection connIM1 = null;
        try {
            conn = IMCommonUtils.getDBIMConnection();
            conn.setAutoCommit(false);
            result = repository.updateListPincodeForStockCard(conn, lstPincodes, ownerId, prodOfferId, updateType);
            //update IM1
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    connIM1 = IMCommonUtils.getDBIMConnectionIM1();
                    connIM1.setAutoCommit(false);
                    repository.updateListPincodeForStockCardIM1(connIM1, lstPincodes, ownerId, prodOfferId, updateType, staffDTO);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            DbUtil.close(conn);
            DbUtil.close(connIM1);
        }
        return result;
    }

    @Override
    public boolean checkInfoStockCard(String pincode, Long ownerId, String serial, Long prodOfferId, String updateType, Long stateId) throws Exception {
        return repository.checkInfoStockCard(pincode, ownerId, serial, prodOfferId, updateType, stateId);

    }

    @Override
    public StockTotalResultDTO getQuantityInEcomStock(String prodOfferCode) throws LogicException, Exception {
        return repository.getQuantityInEcomStock(prodOfferCode);
    }

    public List<Long> getStatusSerialCardSale(String serial) throws LogicException, Exception {
        return repository.getStatusSerialCardSale(serial);
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseMessage unlockG6(String imei) throws LogicException, Exception {

        BaseMessage baseMessage = inventoryExternalService.verifyUnlockG6(imei);
        if (!DataUtil.safeEqual(baseMessage.getKeyMsg(), "1")) {
            return baseMessage;
        }
        Object[] arr = stockHandsetService.getInfomationSerial(imei.trim());
        if (arr == null) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode(Const.UNLOCK_G6.FAILED);
            baseMessage.setDescription(getText("g6.quantity.verify.unlock.handset"));
            return baseMessage;
        }
        if (arr[0] == null || arr[1] == null || arr[2] == null) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode(Const.UNLOCK_G6.FAILED);
            baseMessage.setDescription(getText("g6.quantity.verify.unlock.handset"));
            return baseMessage;
        }
        Long prodOfferId = DataUtil.safeToLong(arr[0].toString());
        String unlockCode = decryptedCode(arr[1].toString());
        String serial = arr[2].toString();

        boolean save = stockHandsetService.saveUnlockG6(serial, prodOfferId);
        if (!save) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode(Const.UNLOCK_G6.FAILED);
            baseMessage.setDescription(getText("g6.quantity.verify.unlock.save"));
            return baseMessage;
        }
        baseMessage.setSuccess(true);
        baseMessage.setErrorCode(Const.UNLOCK_G6.SUCCESS);
        baseMessage.setDescription(unlockCode);
        return baseMessage;
    }

    public static String decryptedCode(String unlockcode) {
        StringBuilder code = new StringBuilder();
        try {
            byte asciiConverse;
            byte[] asciiArray = unlockcode.getBytes("ASCII");
            int lengthArrayConverse = asciiArray.length / 2;
            for (int i = 0; i < lengthArrayConverse; i++) {
                asciiConverse = asciiArray[i * 2 + 1];
                if (i % 2 == 1) {
                    asciiConverse = (byte) (asciiConverse - 49);
                }
                code.append((char) (asciiConverse));
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
        return code.toString();
    }


    @Transactional(rollbackFor = Exception.class)
    public void exchangeCardBankplus(ExchangeCardBankplusDTO exchangeCardBankplusDTO, Long shopId, StaffDTO staffDTO) throws LogicException, Exception {
        if (exchangeCardBankplusDTO.getSaleDate().after(new Date())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "utilities.exchange.card.bankplus.sale.date.after.now");
        }
        if (!DataUtil.checkDigit(exchangeCardBankplusDTO.getSerialError())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.exchange.card.bankplus.digit.serial.error");
        }
        if (!DataUtil.checkDigit(exchangeCardBankplusDTO.getSerialChange())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.exchange.card.bankplus.digit.serial.change");
        }
        if (DataUtil.safeEqual(exchangeCardBankplusDTO.getSerialChange(), exchangeCardBankplusDTO.getSerialError())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.exchange.card.bankplus.serial.change.equal.error");
        }
        if (!DataUtil.checkDigit(exchangeCardBankplusDTO.getIsdn())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "utilities.exchange.card.bankplus.digit.isdn");
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(exchangeCardBankplusDTO.getProdOfferId());
        if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS_INACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.required.stock.exist");
        }
        //
        boolean subOfViettel;
        subOfViettel = commonService.checkIsdnVietel(exchangeCardBankplusDTO.getIsdn());
        if (!subOfViettel) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.isdn.not.viettel", exchangeCardBankplusDTO.getIsdn());
        }
        List<LockupTransactionCardDTO> lstTransactions = repository.getSaleTranInfo(exchangeCardBankplusDTO);
        if (DataUtil.isNullOrEmpty(lstTransactions)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.not.transaction");
        }
        LockupTransactionCardDTO lockupTransactionCardDTO = lstTransactions.get(0);
        if (!DataUtil.safeEqual(formatIsdn(lockupTransactionCardDTO.getIsdn()), formatIsdn(exchangeCardBankplusDTO.getIsdn()))) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.not.transaction.isdn", exchangeCardBankplusDTO.getIsdn());
        }
        long toSerial = 0L;
        long fromSerial = 0L;
        long tempSerialFaile = 0L;
        if (!DataUtil.isNullOrEmpty(lockupTransactionCardDTO.getFromSerial()) && !DataUtil.isNullOrEmpty(lockupTransactionCardDTO.getToSerial())) {
            toSerial = DataUtil.safeToLong(lockupTransactionCardDTO.getToSerial());
            fromSerial = DataUtil.safeToLong(lockupTransactionCardDTO.getFromSerial());
            tempSerialFaile = DataUtil.safeToLong(exchangeCardBankplusDTO.getSerialError());
        }
        if (DataUtil.safeEqual(toSerial, 0L) || DataUtil.safeEqual(fromSerial, 0L)
                || DataUtil.safeEqual(tempSerialFaile, 0L) || fromSerial > tempSerialFaile || tempSerialFaile > toSerial) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.serial.not.sale", exchangeCardBankplusDTO.getSerialError());
        }
        StockCardDTO stockCardError = repository.getInfoStockCard(exchangeCardBankplusDTO.getSerialError(), shopId, 0L, null);//da ban
        StockCardDTO stockCardExchange = repository.getInfoStockCard(exchangeCardBankplusDTO.getSerialChange(), shopId, 1L, Const.STATE_STATUS.NEW);//con trong kho
        if (DataUtil.isNullObject(stockCardExchange)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.serial.exchange.not.found.in.stock", exchangeCardBankplusDTO.getSerialChange());
        }
        if (DataUtil.isNullObject(stockCardError)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.serial.error.not.found.in.stock", exchangeCardBankplusDTO.getSerialError());
        }
        if (!DataUtil.safeEqual(stockCardExchange.getProdOfferId(), stockCardError.getProdOfferId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.serial.prodOffer.not.match", exchangeCardBankplusDTO.getSerialError(), exchangeCardBankplusDTO.getSerialChange());
        }
        if (DataUtil.isNullOrEmpty(stockCardExchange.getPinCode())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.serial.pincode", exchangeCardBankplusDTO.getSerialChange());
        }
        List<ReasonDTO> lstReasonDTOs = reasonService.getLsReasonByCode("EXCHANGE_PINCODE");
        if (DataUtil.isNullOrEmpty(lstReasonDTOs)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.reason.pincode");
        }
        ReasonDTO reasonDTO = lstReasonDTOs.get(0);
        ShopDTO userShop = shopService.findOne(staffDTO.getShopId());
        //hoangnt: neu mat hang the cao data thi ko check lock tong dai
        boolean isCardData = false;
        if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD)) {
            // Neu mat hang truyen vao la the cao data thi khong active tong dai.
            List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_MODEL_CARD_DATA);
            if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)) {
                for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                    if (DataUtil.safeEqual(optionSetValueDTO.getValue(), productOfferingDTO.getCode())) {
                        isCardData = true;
                        break;
                    }
                }
            }
        }
        // Check the cao hong da bi khoa chua. Neu chua bi khoa thi thong bao khoa the truoc khi
        boolean lockCardSucess = true;
        // Lay thong tin the cao
        // Kiem tra xem lock tren CC chua
        if (!isCardData) {
            CardNumberLockDTO cardNumberLockDTO = customerCareWs.getHistoryCardNumber(exchangeCardBankplusDTO.getSerialError());
            if (DataUtil.isNullObject(cardNumberLockDTO) || !DataUtil.safeEqual(cardNumberLockDTO.getStatus(), Const.STATUS_ACTIVE)) {
                HistoryInforCardNumberDTO historyInforCardNumberDTO = customerCareWs.getInforCardNumber(exchangeCardBankplusDTO.getSerialError(),
                        Const.CC_WS.REG_TYPE_LOCK_CARD_OCS, false, null, null);
                if (historyInforCardNumberDTO != null && "0".equals(historyInforCardNumberDTO.getErrorCode())) {
                    CardInfoProvisioningDTO cardInfoProvisioningDTO = historyInforCardNumberDTO.getCardInfoProvisioningDTO();
                    // Goi tong dai thanh cong.
                    if (cardInfoProvisioningDTO != null && cardInfoProvisioningDTO.isResult() &&
                            DataUtil.safeEqual(cardInfoProvisioningDTO.getResponeCode(), Const.CC_WS.RESPONSE_CODE_SUCCESS)) {
                        String valueCard = historyInforCardNumberDTO.getCardInfoProvisioningDTO().getCardValue();
                        String dateUsed = historyInforCardNumberDTO.getCardInfoProvisioningDTO().getDateUsed();
                        if (DataUtil.isNullObject(valueCard)) {
                            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "lockcard.error.no.value.provisioning");
                        }
                        if (!DataUtil.isNullObject(dateUsed)) {
                            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "lockcard.error.used.in.provisioning");
                        }
                        // Thuc hien khoa the
                        BaseMessage baseMessage = customerCareWs.lockedCard(staffDTO.getName(), exchangeCardBankplusDTO.getSerialError(), userShop.getShopCode(),
                                reasonDTO.getReasonName(), staffDTO.getTel(), staffDTO.getIpAddress(), valueCard);
                        if (baseMessage != null && DataUtil.safeEqual(baseMessage.getErrorCode(), Const.CC_WS.RESPONSE_CODE_SUCCESS)) {
                            lockCardSucess = true;
                        } else {
                            if (baseMessage != null && !DataUtil.isNullObject(baseMessage.getDescription())) {
                                logger.error("error lock cc: " + baseMessage.getDescription());
                                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "CC: " + baseMessage.getDescription());
                            }
                        }
                    } else {
                        if (cardInfoProvisioningDTO != null && !cardInfoProvisioningDTO.isResult() &&
                                !DataUtil.safeEqual(cardInfoProvisioningDTO.getResponeCode(), Const.CC_WS.RESPONSE_CODE_SUCCESS)) {
                            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "lockcard.error.in.provisioning");
                        }
                    }
                } else {
                    if (historyInforCardNumberDTO != null && !"0".equals(historyInforCardNumberDTO.getErrorCode())
                            && !DataUtil.isNullObject(historyInforCardNumberDTO.getErrorDescription())) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "CC: " + historyInforCardNumberDTO.getErrorDescription());
                    }
                }
            }
            if (!lockCardSucess) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.card.not.block", exchangeCardBankplusDTO.getSerialError());
            }
        }
        //Thuc hien xuat the cao doi va nhap the cao hong
        Date currentDate = getSysDate(em);
        //Giao dich xuat
        StockTransDTO stockTransExport = getStockTransExport(shopId, reasonDTO.getReasonId(), currentDate);
        StockTransDTO stockTransSave = stockTransService.save(stockTransExport);
        StockTotalMessage resultStockTotal = stockTotalService.changeStockTotal(shopId, Const.OWNER_TYPE.SHOP_LONG, exchangeCardBankplusDTO.getProdOfferId(),
                Const.STATE_STATUS.NEW, -1L, -1L, 0L, staffDTO.getStaffId(), reasonDTO.getReasonId(), stockTransSave.getStockTransId(), currentDate, "", stockTransSave.getStockTransType(), Const.SourceType.STOCK_TRANS);
        if (!resultStockTotal.isSuccess()) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.pincode.not.availble");
        }
        //stock_trans_action 2,3,6
        StockTransActionDTO stockTransActionExport = getStockTransActionExp(staffDTO, currentDate, stockTransSave.getStockTransId());
        StockTransActionDTO stockTransActionExportSave = stockTransActionService.save(stockTransActionExport);
        stockTransActionExportSave.setActionCode("XDHH00" + stockTransActionExportSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionExportSave);
        stockTransActionExportSave.setStockTransActionId(null);
        stockTransActionExportSave.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionService.save(stockTransActionExportSave);
        stockTransActionExportSave.setStockTransActionId(null);
        stockTransActionExportSave.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockTransActionExportSave);
        StockTransDetailDTO stockTransDetailExp = getStockTransDetailExp(stockTransSave.getStockTransId(), exchangeCardBankplusDTO.getProdOfferId(), currentDate);
        StockTransDetailDTO stockTransDetailSave = stockTransDetailService.save(stockTransDetailExp);
        StockTransSerialDTO stockTransSerialExp = getStockTransSerialExp(stockTransSave.getStockTransId(), stockTransDetailSave.getStockTransDetailId(), exchangeCardBankplusDTO.getProdOfferId(), exchangeCardBankplusDTO.getSerialChange(), currentDate);
        stockTransSerialService.save(stockTransSerialExp);
        //Giao dich nhap
        StockTransDTO stockTransImport = getStockTransImport(stockTransSave.getStockTransId(), shopId, reasonDTO.getReasonId(), currentDate);
        StockTransDTO stockTransSaveImp = stockTransService.save(stockTransImport);
        resultStockTotal = stockTotalService.changeStockTotal(shopId, Const.OWNER_TYPE.SHOP_LONG, exchangeCardBankplusDTO.getProdOfferId(),
                Const.STATE_STATUS.DAMAGE, 1L, 1L, 0L, staffDTO.getStaffId(), reasonDTO.getReasonId(), stockTransSaveImp.getStockTransId(), currentDate, "", stockTransSaveImp.getStockTransType(), Const.SourceType.STOCK_TRANS);
        if (!resultStockTotal.isSuccess()) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "utilities.exchange.card.bankplus.stock.total.error");
        }
        //stock_trans_action 5,6
        StockTransActionDTO stockTransActionImp = getStockTransActionImp(staffDTO, currentDate, stockTransSaveImp.getStockTransId());
        StockTransActionDTO stockTransActionImpSave = stockTransActionService.save(stockTransActionImp);
        stockTransActionImpSave.setActionCode("NDHH00" + stockTransActionImpSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionImpSave);
        stockTransActionImpSave.setStockTransActionId(null);
        stockTransActionImpSave.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockTransActionImpSave);
        StockTransDetailDTO stockTransDetailImp = getStockTransDetailImp(stockTransSaveImp.getStockTransId(), exchangeCardBankplusDTO.getProdOfferId(), currentDate);
        StockTransDetailDTO stockTransDetailimpSave = stockTransDetailService.save(stockTransDetailImp);
        StockTransSerialDTO stockTransSerialImp = getStockTransSerialImp(stockTransSaveImp.getStockTransId(), stockTransDetailimpSave.getStockTransDetailId(), exchangeCardBankplusDTO.getProdOfferId(), exchangeCardBankplusDTO.getSerialError(), currentDate);
        stockTransSerialService.save(stockTransSerialImp);
        //Luu thong tin VC_request, neu the cao data thi bao qua
        if (!isCardData) {
            VcRequestDTO vcRequestDTO = new VcRequestDTO();
            vcRequestDTO.setCreateTime(getSysDate(emIm1));
            vcRequestDTO.setUserId(staffDTO.getStaffCode());
            vcRequestDTO.setFromSerial(normalSerialCard(exchangeCardBankplusDTO.getSerialChange()));
            vcRequestDTO.setToSerial(normalSerialCard(exchangeCardBankplusDTO.getSerialChange()));
            vcRequestDTO.setStaffId(staffDTO.getStaffId());
            vcRequestDTO.setShopId(staffDTO.getShopId());
            vcRequestDTO.setTransId(stockTransSave.getStockTransId());

            vcRequestDTO.setRequestType(Const.SALE_TRANS.REQUEST_TYPE_CHANGE_GOODS);
            vcRequestDTO.setStatus(0L);
            VcRequestDTO result = vcRequestService.create(vcRequestDTO);
            if (DataUtil.isNullObject(result)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.create.vc.request.error");
            }
        }
        //Luu thong tin report_Change_good
        ReportChangeGoodsDTO report = new ReportChangeGoodsDTO();
        report.setId(getSequence(em, "REPORT_CHANGE_GOODS_SEQ"));
        report.setShopId(staffDTO.getShopId());
        report.setShopCode(userShop.getShopCode());
        report.setShopName(userShop.getName());
        report.setStaffId(staffDTO.getStaffId());
        report.setStaffCode(staffDTO.getStaffCode());
        report.setStaffName(staffDTO.getName());
        report.setProdOfferId(productOfferingDTO.getProductOfferingId());
        report.setProdOfferCode(productOfferingDTO.getCode());
        report.setProdOfferName(productOfferingDTO.getName());
        report.setSerialNew(normalSerialCard(exchangeCardBankplusDTO.getSerialChange()));
        report.setSerialOld(normalSerialCard(exchangeCardBankplusDTO.getSerialError()));
        report.setCreateDate(currentDate);
        report.setTelecomServiceId(productOfferingDTO.getTelecomServiceId());
        report.setStockTransId(stockTransSave.getStockTransId());
        if (!DataUtil.isNullObject(productOfferingDTO)
                && !DataUtil.isNullObject(productOfferingDTO.getProductOfferTypeId())
                && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD)) {
            report.setReasonId(reasonDTO.getReasonId());
        }
        reportChangeGoodsService.create(report);

    }


    private StockTransDTO getStockTransExport(Long shopId, Long reasonId, Date currentDate) {
        StockTransDTO stockTransExport = new StockTransDTO();
        stockTransExport.setCreateDatetime(currentDate);
        stockTransExport.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTransExport.setFromOwnerId(shopId);
        stockTransExport.setToOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransExport.setToOwnerId(0L);
        stockTransExport.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransExport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransExport.setReasonId(reasonId);
        stockTransExport.setCreateDatetime(currentDate);
        stockTransExport.setNote(getText("stock.change.damaged.stock.trans.note.export"));
        return stockTransExport;
    }

    private StockTransActionDTO getStockTransActionExp(StaffDTO staffDTO, Date currentDate, Long stockTransId) throws Exception {
        StockTransActionDTO stockTransActionExp = new StockTransActionDTO();
        stockTransActionExp.setCreateDatetime(currentDate);
        stockTransActionExp.setStockTransId(stockTransId);
        stockTransActionExp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionExp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionExp.setNote(getText("stock.change.damaged.stock.trans.note.export"));
        return stockTransActionExp;
    }

    private StockTransDetailDTO getStockTransDetailExp(Long stockTransId, Long prodOfferId, Date currentDate) {
        StockTransDetailDTO expTransDetailDTO = new StockTransDetailDTO();
        expTransDetailDTO.setStockTransId(stockTransId);
        expTransDetailDTO.setProdOfferId(prodOfferId);
        expTransDetailDTO.setStateId(Const.STATE_STATUS.NEW);
        expTransDetailDTO.setQuantity(1L);
        expTransDetailDTO.setCreateDatetime(currentDate);
        return expTransDetailDTO;
    }

    private StockTransSerialDTO getStockTransSerialExp(Long stockTransId, Long stockTransDetailId, Long prodOfferId, String serialExchange, Date currentDate) {
        StockTransSerialDTO expStockSerialDTO = new StockTransSerialDTO();
        expStockSerialDTO.setProdOfferId(prodOfferId);
        expStockSerialDTO.setStateId(Const.STATE_STATUS.NEW);
        expStockSerialDTO.setStockTransDetailId(stockTransDetailId);
        expStockSerialDTO.setStockTransId(stockTransId);
        expStockSerialDTO.setFromSerial(serialExchange);
        expStockSerialDTO.setToSerial(serialExchange);
        expStockSerialDTO.setQuantity(1L);
        expStockSerialDTO.setCreateDatetime(currentDate);
        return expStockSerialDTO;
    }

    private StockTransDTO getStockTransImport(Long fromStockTransId, Long shopId, Long reasonId, Date currentDate) {
        StockTransDTO stockTransExport = new StockTransDTO();
        stockTransExport.setCreateDatetime(currentDate);
        stockTransExport.setFromStockTransId(fromStockTransId);
        stockTransExport.setFromOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransExport.setFromOwnerId(0L);
        stockTransExport.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTransExport.setToOwnerId(shopId);
        stockTransExport.setStockTransType(Const.STOCK_TRANS_TYPE.IMPORT);
        stockTransExport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransExport.setReasonId(reasonId);
        stockTransExport.setCreateDatetime(currentDate);
        stockTransExport.setNote(getText("stock.change.damaged.stock.trans.note.import"));
        return stockTransExport;
    }

    private StockTransActionDTO getStockTransActionImp(StaffDTO staffDTO, Date currentDate, Long stockTransId) throws Exception {
        StockTransActionDTO stockTransActionExp = new StockTransActionDTO();
        stockTransActionExp.setCreateDatetime(currentDate);
        stockTransActionExp.setStockTransId(stockTransId);
        stockTransActionExp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionExp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionExp.setNote(getText("stock.change.damaged.stock.trans.note.import"));
        return stockTransActionExp;
    }

    private StockTransDetailDTO getStockTransDetailImp(Long stockTransId, Long prodOfferId, Date currentDate) {
        StockTransDetailDTO expTransDetailDTO = new StockTransDetailDTO();
        expTransDetailDTO.setStockTransId(stockTransId);
        expTransDetailDTO.setProdOfferId(prodOfferId);
        expTransDetailDTO.setStateId(Const.STATE_STATUS.DAMAGE);
        expTransDetailDTO.setQuantity(1L);
        expTransDetailDTO.setCreateDatetime(currentDate);
        return expTransDetailDTO;
    }

    private StockTransSerialDTO getStockTransSerialImp(Long stockTransId, Long stockTransDetailId, Long prodOfferId, String serialError, Date currentDate) {
        StockTransSerialDTO expStockSerialDTO = new StockTransSerialDTO();
        expStockSerialDTO.setProdOfferId(prodOfferId);
        expStockSerialDTO.setStateId(Const.STATE_STATUS.DAMAGE);
        expStockSerialDTO.setStockTransDetailId(stockTransDetailId);
        expStockSerialDTO.setStockTransId(stockTransId);
        expStockSerialDTO.setFromSerial(serialError);
        expStockSerialDTO.setToSerial(serialError);
        expStockSerialDTO.setQuantity(1L);
        expStockSerialDTO.setCreateDatetime(currentDate);
        return expStockSerialDTO;
    }

    private String normalSerialCard(String serial) {
        Long lSerial = null;
        try {
            lSerial = Long.parseLong(serial);
            serial = String.valueOf(lSerial);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (serial != null && !"".equals(serial.trim())) {
            while (serial.length() < 11) {
                serial = "0" + serial;
            }
        }
        return serial;
    }

    private String formatIsdn(String isdn) {
        if (DataUtil.isNullOrEmpty(isdn)) {
            return isdn;
        }
        while (isdn.startsWith("0")) {
            isdn = isdn.substring(1);
        }
        return isdn;
    }

    @Override
    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serial) throws Exception {
        if (!DataUtil.isNullOrEmpty(serial)) {
            //try {
            return repository.getSerialList(serial);
            //} catch (Exception e) {
            //    logger.debug(e.getMessage(), e);
            //    return null;
            //}
        }
        return null;
    }

}

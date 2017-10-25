package com.viettel.bccs.inventory.service.BaseIsdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.StockIsdnIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by thetm1 on 9/12/2015.
 * Description Xuat kho (da lap phieu xuat)
 * Detail + Update stock_trans
 * + Khong luu stock_trans_detail (-)
 * + Insert stock_trans_action
 * + Khong luu stock_trans_serial (-)
 * + Khong cap nhat stock_total (-)
 * + Khong cap nhat chi tiet serial (doSaveStockGoods) (-)
 */

@Service
public class BaseStockIsdnExpService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockIsdnExpService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em1;

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private NumberActionService numberActionService;
    @Autowired
    private NumberActionDetailService numberActionDetailService;
    @Autowired
    private StockIsdnTransService stockIsdnTransService;
    @Autowired
    private StockIsdnTransDetailService stockIsdnTransDetailService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransRepo stockTransRepo;
    @Autowired
    private StockIsdnIm1Service stockIsdnIm1Service;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap lenh xuat
        // sinhhv sua theo anh bac chuyen tu 3 -> 6
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setIsAutoGen(null);
        stockTransDTO.setTransport(null);

        //cap nhat kho xuat stock_number
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
        //xoa note trong xuat kho
        stockTransDTO.setNote(null);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        //Validate cac truong trong giao dich
        doOrderValidate(stockTransDTO);

        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);
    }

    public void doOrderValidate(StockTransDTO stockTransDTO) throws LogicException, Exception {
        //1. Validate kho xuat
        if (DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getFromOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.from.stock.not.null");
        }

        ShopDTO fromShop = shopService.findOne(stockTransDTO.getFromOwnerId());
        if (DataUtil.isNullObject(fromShop) || !fromShop.getStatus().equals(Const.STATUS_ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.from.stock.invalid");
        }
        if (DataUtil.isNullObject(fromShop.getChannelTypeId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.deliver.stock.chanel.null");
        }

        //2. Validate kho nhan
        if (DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.to.stock.not.null");
        }

        ShopDTO toShop = shopService.findOne(stockTransDTO.getToOwnerId());
        if (DataUtil.isNullObject(toShop) || !toShop.getStatus().equals(Const.STATUS_ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.to.stock.invalid");
        }
//
//        List<VShopStaffDTO> importShopList = shopService.getListShopStaff(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString(), Const.VT_UNIT.VT);
//        if (DataUtil.isNullOrEmpty(importShopList)) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
//        }

        if (DataUtil.safeEqual(stockTransDTO.getFromOwnerId(), stockTransDTO.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromtoStock.equal");
        }

        //4. kiem tra ly do co ton tai khong
        if (DataUtil.isNullOrZero(stockTransDTO.getReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.note.reason.export.require.msg");
        }

        ReasonDTO reason = reasonService.findOne(stockTransDTO.getReasonId());
        if (DataUtil.isNullObject(reason) || DataUtil.isNullOrZero(reason.getReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.reason.notExists");
        }

        //5. kiem tra ghi chu co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getNote()) && stockTransDTO.getNote().length() > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }

        //6. kiem tra trang thai phieu
        StockTransDTO stockCheck = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(stockCheck) || DataUtil.isNullObject(stockCheck.getStatus()) || !stockCheck.getStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "transfer.isdn.status.invalid");

        }
    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTotal(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetailDTO, FlagStockDTO flagStockDTO, StockTransActionDTO stockTransActionDTO) throws Exception {

    }

    @Override
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        if (DataUtil.isNullOrZero(flagStockDTO.getOldStatus()) || DataUtil.isNullOrZero(flagStockDTO.getNewStatus())) {
            return;
        }

        int count;
        BigInteger fromIsdn;
        BigInteger toIsdn;
        List<StockIsdnTransDTO> lstStockTransIsdn = Lists.newArrayList();
        List<StockIsdnTransDetailDTO> lstStockTransIsdnDetail = Lists.newArrayList();
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {

            String tableName = stockTransDetail.getTableName();

            if (DataUtil.isNullOrEmpty(tableName)) {
                continue;
            }

            //them vao bang stock_isdn_trans
            StockIsdnTransDTO stockIsdnTransDTO = new StockIsdnTransDTO();
            Long stockIsdnTransId = DbUtil.getSequence(em, "STOCK_ISDN_TRANS_SEQ");

            String code = genCode(stockIsdnTransId);
            stockIsdnTransDTO.setStockIsdnTransCode(code);
            stockIsdnTransDTO.setFromOwnerType(DataUtil.safeToLong(stockTransDTO.getFromOwnerType()));
            stockIsdnTransDTO.setFromOwnerId(DataUtil.safeToLong(stockTransDTO.getFromOwnerId()));
            stockIsdnTransDTO.setFromOwnerCode(stockTransDTO.getFromOwnerCode());
            stockIsdnTransDTO.setFromOwnerName(stockTransDTO.getFromOwnerName());
            stockIsdnTransDTO.setToOwnerType(stockTransDTO.getToOwnerType());
            stockIsdnTransDTO.setToOwnerId(stockTransDTO.getToOwnerId());
            stockIsdnTransDTO.setToOwnerName(stockTransDTO.getToOwnerName());
            stockIsdnTransDTO.setToOwnerCode(stockTransDTO.getToOwnerCode());
            stockIsdnTransDTO.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            stockIsdnTransDTO.setReasonId(stockTransDTO.getReasonId());
            stockIsdnTransDTO.setQuantity(stockTransDetail.getQuantity());
            stockIsdnTransDTO.setStockTypeId(stockTransDetail.getProdOfferTypeId());
            stockIsdnTransDTO.setStockTypeName(stockTransDetail.getTableName());
            stockIsdnTransDTO.setReasonName(stockTransDTO.getReasonName());
            stockIsdnTransDTO.setReasonCode(stockTransDTO.getReasonCode());
            stockIsdnTransDTO.setCreatedTime(DbUtil.getSysDate(em));
            stockIsdnTransDTO.setCreatedUserId(stockTransDTO.getStaffId());
            stockIsdnTransDTO.setCreatedUserCode(stockTransDTO.getUserCreate());
            stockIsdnTransDTO.setCreatedUserName(stockTransDTO.getUserName());
            stockIsdnTransDTO.setCreatedUserIp(stockTransDTO.getCreateUserIpAdress());
            stockIsdnTransDTO.setLastUpdatedTime(DbUtil.getSysDate(em));
            stockIsdnTransDTO.setLastUpdatedUserId(stockTransDTO.getStaffId());
            stockIsdnTransDTO.setLastUpdatedUserCode(stockTransDTO.getUserCreate());
            stockIsdnTransDTO.setLastUpdatedUserName(stockTransDTO.getUserName());
            stockIsdnTransDTO.setLastUpdatedUserIp(stockTransDTO.getCreateUserIpAdress());
            stockIsdnTransDTO = stockIsdnTransService.create(stockIsdnTransDTO);
            StringBuilder sqlUpdate = new StringBuilder("");
            sqlUpdate.append("UPDATE    stock_number ");
            sqlUpdate.append("   SET   ");

            sqlUpdate.append("     owner_type = #newOwnerType ");
            sqlUpdate.append("     ,owner_id = #newOwnerId ");
            sqlUpdate.append("     ,last_update_user = #last_update_user ");
            sqlUpdate.append("     ,last_update_ip_address = #last_update_ip_address ");
            sqlUpdate.append("     ,last_update_time = sysdate ");
            sqlUpdate.append(" WHERE    prod_offer_id = #prod_offer_id ");
            sqlUpdate.append("   AND    owner_type = #oldOwnerType ");
            sqlUpdate.append("   AND    owner_id = #oldOwnerId ");
            sqlUpdate.append("AND   to_number(isdn) >= #fromIsdn ");
            sqlUpdate.append("AND   to_number(isdn) <= #toIsdn ");
            sqlUpdate.append("AND   status = #status ");

            List<StockTransSerialDTO> lstStockTransSerial = stockTransDetail.getLstStockTransSerial();

            if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {

                    fromIsdn = new BigInteger(stockTransSerial.getFromSerial());
                    toIsdn = new BigInteger(stockTransSerial.getToSerial());
                    //cap nhat stock_number
                    Query query = em.createNativeQuery(sqlUpdate.toString());

                    query.setParameter("newOwnerType", stockTransDTO.getToOwnerType());
                    query.setParameter("newOwnerId", stockTransDTO.getToOwnerId());
                    query.setParameter("last_update_user", stockTransDTO.getUserCreate());
                    query.setParameter("last_update_ip_address", stockTransDTO.getCreateUserIpAdress());
                    query.setParameter("prod_offer_id", stockTransDetail.getProdOfferId());
                    query.setParameter("oldOwnerType", stockTransDTO.getFromOwnerType());
                    query.setParameter("oldOwnerId", stockTransDTO.getFromOwnerId());
                    query.setParameter("fromIsdn", fromIsdn);
                    query.setParameter("toIsdn", toIsdn);
                    query.setParameter("status", Const.STATUS_ACTIVE);

                    count = query.executeUpdate();
                    //them log vao number_action
                    NumberActionDTO action = new NumberActionDTO();
                    action.setFromIsdn(fromIsdn.toString());
                    action.setTelecomServiceId(stockTransDetail.getProdOfferTypeId());
                    action.setToIsdn(toIsdn.toString());
                    action.setActionType(Const.NUMBER_ACTION_TYPE.DISTRIBUTE);
                    action.setUserCreate(stockTransDTO.getUserCreate());
                    action.setUserIpAddress(stockTransDTO.getCreateUserIpAdress());
                    action.setReasonId(stockTransDTO.getReasonId());
                    action.setNote(stockTransDTO.getNote());
                    action.setCreateDate(DbUtil.getSysDate(em));
                    NumberActionDTO numberAction = numberActionService.create(action);
                    //them log thay doi owner_type vao number_action_detail
                    NumberActionDetailDTO detailOwnerType = new NumberActionDetailDTO();
                    detailOwnerType.setNumberActionId(numberAction.getNumberActionId());
                    detailOwnerType.setColumnName("OWNER_TYPE");
                    detailOwnerType.setOldValue(stockTransDTO.getFromOwnerType().toString());
                    detailOwnerType.setNewValue(stockTransDTO.getToOwnerType().toString());
                    detailOwnerType.setOldDetailValue(stockTransDTO.getFromOwnerType().toString());
                    detailOwnerType.setNewDetailValue(stockTransDTO.getToOwnerType().toString());
                    //them log thay doi owner_id vao number_action_detail
                    numberActionDetailService.create(detailOwnerType);
                    NumberActionDetailDTO detailOwnerId = new NumberActionDetailDTO();
                    detailOwnerId.setNumberActionId(numberAction.getNumberActionId());
                    detailOwnerId.setColumnName("OWNER_ID");
                    detailOwnerId.setOldValue(stockTransDTO.getFromOwnerId().toString());
                    detailOwnerId.setNewValue(stockTransDTO.getToOwnerId().toString());
                    detailOwnerId.setOldDetailValue(stockTransDTO.getFromOwnerCode() + "-" + stockTransDTO.getFromOwnerName());
                    detailOwnerId.setNewDetailValue(stockTransDTO.getToOwnerCode() + "-" + stockTransDTO.getToOwnerName());
                    numberActionDetailService.create(detailOwnerId);
                    //them vao bang stock_isdn_trans_detail
                    StockIsdnTransDetailDTO stockIsdnTransDetailDTO = new StockIsdnTransDetailDTO();
                    stockIsdnTransDetailDTO.setStockIsdnTransId(stockIsdnTransDTO.getStockIsdnTransId());
                    stockIsdnTransDetailDTO.setFromIsdn(fromIsdn.toString());
                    stockIsdnTransDetailDTO.setToIsdn(toIsdn.toString());
                    stockIsdnTransDetailDTO.setQuantity(stockTransSerial.getQuantity());
                    stockIsdnTransDetailDTO.setCreatedTime(DbUtil.getSysDate(em));
                    stockIsdnTransDetailService.create(stockIsdnTransDetailDTO);
                    //clone bean StockIsdnTransDetailDTO to sync im1
                    StockIsdnTransDetailDTO stockIsdnTransDetailDTO1 = DataUtil.cloneBean(stockIsdnTransDetailDTO);

                    stockIsdnTransDetailDTO1.setStockIsdnTransId(stockIsdnTransId);
                    lstStockTransIsdnDetail.add(stockIsdnTransDetailDTO1);
                    if (count != (toIsdn.subtract(fromIsdn).intValue() + 1)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.quantity.serial.detail");
                    }
                }

            }
            StockIsdnTransDTO stockIsdnTransDTO1 = DataUtil.cloneBean(stockIsdnTransDTO);

            //clone bean StockIsdnTransDetailDTO to sync im1
            stockIsdnTransDTO1.setStockIsdnTransId(stockIsdnTransId);
            if (!DataUtil.isNullObject(stockTransDetail.getProdOfferTypeId())) {
                if (stockTransDetail.getProdOfferTypeId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
                    stockIsdnTransDTO1.setStockTypeName("STOCK_ISDN_MOBILE");
                } else if (stockTransDetail.getProdOfferTypeId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
                    stockIsdnTransDTO1.setStockTypeName("STOCK_ISDN_HOMEPHONE");
                } else {
                    stockIsdnTransDTO1.setStockTypeName("STOCK_ISDN_PSTN");
                }
            } else {
                throw new LogicException("", "export.isdn.has.error.im1");
            }
            lstStockTransIsdn.add(stockIsdnTransDTO1);
        }
        //dong bo voi im1
        try {

            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    stockIsdnIm1Service.doSaveStockIsdn(lstStockTransIsdn, lstStockTransDetail, lstStockTransIsdnDetail);
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void doUnlockUser(StockTransDTO stockTransDTO) throws Exception {
        stockTransRepo.unlockUserInfo(stockTransDTO.getStockTransId());
    }

    @Override
    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        StockTransActionDTO stockTransActionClone = DataUtil.cloneBean(stockTransActionDTO);
        stockTransActionClone.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        super.doSaveStockTransAction(stockTransDTO, stockTransActionClone);
        return super.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
    }

    private String genCode(Long id) {
        String transCode = String.format("GD%011d", id);
        return transCode;
    }
}

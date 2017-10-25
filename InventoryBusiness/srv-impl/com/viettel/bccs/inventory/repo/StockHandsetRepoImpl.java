package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockHandset;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.StockCardService;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockHandset.COLUMNS;
import com.viettel.bccs.inventory.model.QStockHandset;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockHandsetRepoImpl implements StockHandsetRepoCustom {

    public static final Logger logger = Logger.getLogger(StockHandsetRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM1;
    @Autowired
    private StockCardService stockCardService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferingService productOfferingService;

    private final BaseMapper<StockHandset, StockHandsetDTO> mapper = new BaseMapper(StockHandset.class, StockHandsetDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockHandset stockHandset = QStockHandset.stockHandset;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CONNECTIONDATE:
                        expression = DbUtil.createDateExpression(stockHandset.connectionDate, filter);
                        break;
                    case CONNECTIONSTATUS:
                        expression = DbUtil.createLongExpression(stockHandset.connectionStatus, filter);
                        break;
                    case CONNECTIONTYPE:
                        expression = DbUtil.createStringExpression(stockHandset.connectionType, filter);
                        break;
                    case CONTRACTCODE:
                        expression = DbUtil.createStringExpression(stockHandset.contractCode, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockHandset.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockHandset.createUser, filter);
                        break;
                    case DEPOSITPRICE:
                        expression = DbUtil.createStringExpression(stockHandset.depositPrice, filter);
                        break;
                    case EXPORTTOCOLLDATE:
                        expression = DbUtil.createDateExpression(stockHandset.exportToCollDate, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockHandset.id, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockHandset.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockHandset.ownerType, filter);
                        break;
                    case PARTNERID:
                        expression = DbUtil.createLongExpression(stockHandset.partnerId, filter);
                        break;
                    case POCODE:
                        expression = DbUtil.createStringExpression(stockHandset.poCode, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockHandset.prodOfferId, filter);
                        break;
                    case SALEDATE:
                        expression = DbUtil.createDateExpression(stockHandset.saleDate, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(stockHandset.serial, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockHandset.stateId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockHandset.status, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(stockHandset.telecomServiceId, filter);
                        break;
                    case TVMSCADID:
                        expression = DbUtil.createStringExpression(stockHandset.tvmsCadId, filter);
                        break;
                    case TVMSMACID:
                        expression = DbUtil.createStringExpression(stockHandset.tvmsMacId, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(stockHandset.updateDatetime, filter);
                        break;
                }
                if (expression != null) {
                    result = result.and(expression);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.info("Result Predicate :: " + (result != null ? result.toString() : ""));
        logger.info("Exiting predicates");
        return result;
    }

    @Override
    public Date findExportDateBySerial(Long productOfferId, String serial) throws Exception, LogicException {

        StringBuilder builder = new StringBuilder("");

        builder.append("SELECT a.* ");
        builder.append("  FROM stock_trans_serial a, stock_trans b, shop c");
        builder.append(" WHERE a.stock_trans_id = b.stock_trans_id");
        builder.append("   AND b.from_owner_id = c.shop_id");
        builder.append("   AND b.from_owner_type = #fromOwnerType");
        builder.append("   AND a.from_serial = #fromSerial");
        builder.append("   AND a.to_serial = #toSerial");
        builder.append("   AND a.prod_offer_id = #prodOfferId");
        builder.append("   AND c.parent_shop_id = #parentShopId ");
        builder.append("  ORDER BY a.create_datetime");

        Query query = em.createNativeQuery(builder.toString(), StockTransSerial.class);
        query.setParameter("fromOwnerType", Const.OWNER_TYPE.SHOP);
        query.setParameter("fromSerial", serial);
        query.setParameter("toSerial", serial);
        query.setParameter("prodOfferId", productOfferId);
        query.setParameter("parentShopId", Const.L_VT_SHOP_ID);

        List<StockTransSerial> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return lst.get(0).getCreateDatetime();
        }
        return null;
    }

    @Override
    public BaseMessage updateUctt(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long
            productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);

        StringBuilder builder = new StringBuilder("update stock_handset  set prod_offer_id=#productOfferId ");
        builder.append(" ,update_datetime =#updateDatetime, owner_Id =#ownerId, owner_Type =#ownerType    ");
        builder.append(" ,state_id =#stateId, status =#status  ");
        builder.append(" where serial=#serial ");
        builder.append("   and prod_offer_id=#oldProductOfferId ");
        Query qUpdate = em.createNativeQuery(builder.toString());
        qUpdate.setParameter("productOfferId", productOfferIdUCTT);
        qUpdate.setParameter("updateDatetime", updateDatetime);
        qUpdate.setParameter("ownerId", ownerId);
        qUpdate.setParameter("ownerType", ownerType);
        qUpdate.setParameter("stateId", Const.GOODS_STATE.BROKEN);
        qUpdate.setParameter("status", 1L);
        qUpdate.setParameter("serial", serialRecover);
        qUpdate.setParameter("oldProductOfferId", productOfferId);

        int i = qUpdate.executeUpdate();
        if (i == 0) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stockHandSet.error.update.revoke.serial");
        }

        return result;
    }

    @Override
    public BaseMessage updateUcttIm1(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long
            productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);

        StringBuilder builder = new StringBuilder("update bccs_im.stock_handset  set stock_model_id=#stockModelId ");
        builder.append("  ,owner_Id =#ownerId, owner_Type =#ownerType    ");
        builder.append(" ,state_id =#stateId, status =#status  ");
        builder.append(" where serial=#serial ");
        builder.append("  and  stock_model_id=#oldStockModelId ");
        Query qUpdate = emIM1.createNativeQuery(builder.toString());
        qUpdate.setParameter("stockModelId", productOfferIdUCTT);
        qUpdate.setParameter("ownerId", ownerId);
        qUpdate.setParameter("ownerType", ownerType);
        qUpdate.setParameter("stateId", Const.GOODS_STATE.BROKEN);
        qUpdate.setParameter("status", 1L);
        qUpdate.setParameter("serial", serialRecover);
        qUpdate.setParameter("oldStockModelId", productOfferId);

        int i = qUpdate.executeUpdate();
        if (i == 0) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stockHandSet1.error.update.revoke.serial", serialRecover);
        }

        return result;
    }

    @Override
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                 FlagStockDTO flagStockDTO) throws Exception, LogicException {
        if (DataUtil.isNullObject(flagStockDTO.getOldStatus()) || DataUtil.isNullObject(flagStockDTO.getNewStatus())) {
            return;
        }

        int count;
        BigInteger fromSerial = null;
        BigInteger toSerial = null;
        Long configStockCard = 0L;
        List<OptionSetValueDTO> lstConfigStockCard = optionSetValueService.getByOptionSetCode("STOCK_CARD_STRIP");
        if (!DataUtil.isNullOrEmpty(lstConfigStockCard) && !DataUtil.isNullObject(lstConfigStockCard.get(0).getValue())) {
            configStockCard = DataUtil.safeToLong(lstConfigStockCard.get(0).getValue());
        }
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {

            String tableName = stockTransDetail.getTableName();

            if (DataUtil.isNullOrEmpty(tableName)) {
                continue;
            }
            if (Const.STOCK_TYPE.STOCK_CARD.equals(tableName) && Const.STOCK_CARD_STRIP.equals(configStockCard)) {
                StockTransActionDTO actionDTO = new StockTransActionDTO();

                Date createDate = stockTransDetail.getCreateDatetime();
                actionDTO.setCreateUser(stockTransDTO.getUserName());
                actionDTO.setCreateDatetime(createDate);

                Long oldOwnerType = null, oldOwnerId = null;
                Long newOwnerType = null, newOwnerId = null;
                if (!DataUtil.isNullOrZero(flagStockDTO.getStateId())) {
                    if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
                        oldOwnerType = stockTransDTO.getFromOwnerType();
                        oldOwnerId = stockTransDTO.getFromOwnerId();
                    } else {
                        oldOwnerType = 2L;
                        newOwnerType = 2L;
                        if (!DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())) {
                            oldOwnerId = stockTransDTO.getToOwnerId();
                            newOwnerId = stockTransDTO.getFromOwnerId();
                        }
                    }
                }


                stockCardService.doSaveStockCard(flagStockDTO, stockTransDTO, actionDTO, stockTransDetail, flagStockDTO.getNewStatus(), flagStockDTO.getOldStatus(),
                        newOwnerType, oldOwnerType, newOwnerId, oldOwnerId);
                continue;
            }

            StringBuilder sqlUpdate = new StringBuilder("");
            sqlUpdate.append("UPDATE    ").append(tableName).append(" ");
            sqlUpdate.append("   SET    status = #newStatus ");
            if (Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
                sqlUpdate.append("     ,owner_type = #oldOwnerType, owner_Id = #oldOwnerId ");
            }
            if (!DataUtil.isNullOrZero(flagStockDTO.getStateId())) {
                sqlUpdate.append("     ,state_id = #stateId ");
            }
            if (!DataUtil.isNullObject(stockTransDetail.getCreateDatetime())) {
                sqlUpdate.append("     ,update_datetime = #updateDatetime ");
            }

            sqlUpdate.append(" WHERE    prod_offer_id = #prodOfferId ");
            if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
                sqlUpdate.append("   AND    owner_type = #oldOwnerType ");
                sqlUpdate.append("   AND    owner_id = #oldOwnerId ");
            } else {
                sqlUpdate.append("   AND    owner_type = #newOwnerType ");
                sqlUpdate.append("   AND    owner_id = #newOwnerId ");

            }

            if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getProdOfferTypeId())) {
                sqlUpdate.append("AND   serial = #serial ");
            } else {
                sqlUpdate.append("AND   to_number(serial) >= #fromSerial ");
                sqlUpdate.append("AND   to_number(serial) <= #toSerial ");
            }

            sqlUpdate.append("    AND   status = #oldStatus ");

            List<StockTransSerialDTO> lstStockTransSerial = stockTransDetail.getLstStockTransSerial();
            if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                int total = 0;
                for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                    if (!Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getProdOfferTypeId())) {
                        fromSerial = new BigInteger(stockTransSerial.getFromSerial());
                        toSerial = new BigInteger(stockTransSerial.getToSerial());
                    }

                    Query query = em.createNativeQuery(sqlUpdate.toString());

                    query.setParameter("newStatus", flagStockDTO.getNewStatus());
                    if (!DataUtil.isNullOrZero(flagStockDTO.getStateId())) {
                        if (!Const.REASON_TYPE.RETRIEVE_STOCK.equals(flagStockDTO.getNote())) {
                            query.setParameter("stateId", flagStockDTO.getStateId());
                            query.setParameter("oldOwnerType", stockTransDTO.getFromOwnerType());
                            query.setParameter("oldOwnerId", stockTransDTO.getFromOwnerId());
                        } else {
                            query.setParameter("stateId", stockTransDetail.getStrStateIdAfter());
                            query.setParameter("oldOwnerType", "2");
                            query.setParameter("newOwnerType", "2");
                            if (!DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())) {
                                query.setParameter("oldOwnerId", stockTransDTO.getToOwnerId());
                                query.setParameter("newOwnerId", stockTransDTO.getFromOwnerId());
                            }
                        }
                    }
                    query.setParameter("oldStatus", flagStockDTO.getOldStatus());
                    query.setParameter("prodOfferId", stockTransDetail.getProdOfferId());
                    if (!DataUtil.isNullObject(stockTransDetail.getCreateDatetime())) {
                        query.setParameter("updateDatetime", stockTransDetail.getCreateDatetime());
                    }

                    if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getProdOfferTypeId())) {
                        query.setParameter("serial", stockTransSerial.getFromSerial());
                    } else {
                        query.setParameter("fromSerial", fromSerial);
                        query.setParameter("toSerial", toSerial);
                    }

                    count = query.executeUpdate();

                    if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getProdOfferTypeId())) {
                        if (count != 1) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.quantity.serial", stockTransSerial.getFromSerial());
                        }
                    }
//                    else {
//                        if (count != (toSerial.subtract(fromSerial).intValue() + 1)) {
//                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.quantity.serial");
//                        }
//                    }
                    total += count;
                }
//                //tam thoi de trong nay ... - chua nhap duoc chi tiet serial
//                if (stockTransDetail.getQuantity() != total) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.quantity.stockDetail");
//                }
            }
        }
    }

    public int updateForStockRescue(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(" UPDATE stock_handset");
        builder.append(" SET state_id = #stateId,update_datetime = sysdate");
        builder.append(" WHERE     owner_id = #ownerId");
        builder.append(" AND owner_type = 2");
        builder.append(" and serial = #serial");
        builder.append(" and status = 1");
        builder.append(" and prod_offer_id = #prodOfferId");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stateId", stateId);
        query.setParameter("ownerId", ownerId);
        query.setParameter("serial", serial);
        query.setParameter("prodOfferId", prodOfferId);
        return query.executeUpdate();
    }

    public int updateForStockRescueIM1(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(" UPDATE bccs_im.stock_handset");
        builder.append(" SET state_id = #stateId");
        builder.append(" WHERE     owner_id = #ownerId");
        builder.append(" AND owner_type = 2");
        builder.append(" and serial = #serial");
        builder.append(" and status = 1");
        builder.append(" and stock_model_id = #prodOfferId");
        Query query = emIM1.createNativeQuery(builder.toString());
        query.setParameter("stateId", stateId);
        query.setParameter("ownerId", ownerId);
        query.setParameter("serial", serial);
        query.setParameter("prodOfferId", prodOfferId);
        return query.executeUpdate();
    }

    public int updateStockHandsetIM1(Long stateIdAf, Long ownerIdAf, Long ownerTypeAf, Long statusAf, Long stateId, Long ownerId, Long ownerType, Long status, String serial, Long prodOfferId) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(" UPDATE bccs_im.stock_handset SET ");
        if (!DataUtil.isNullObject(statusAf)) {
            builder.append(" status = #statusNew");
        }
        if (!DataUtil.isNullObject(stateIdAf) && !DataUtil.isNullObject(ownerIdAf) && !DataUtil.isNullObject(ownerTypeAf)) {
            builder.append(" state_id = #stateIdNew,owner_id = #ownerIdNew, owner_type = #ownerTypeNew");
        }
        builder.append(" WHERE     owner_id = #ownerId");
        builder.append(" AND owner_type = #ownerType");
        builder.append(" and serial = #serial");
        builder.append(" and stock_model_id = #prodOfferId");
        builder.append(" and status = #status");
        if (!DataUtil.isNullObject(stateId)) {
            builder.append(" and state_id = #stateId");
        }
        Query query = emIM1.createNativeQuery(builder.toString());
        if (!DataUtil.isNullObject(statusAf)) {
            query.setParameter("statusNew", statusAf);
        }
        if (!DataUtil.isNullObject(stateIdAf) && !DataUtil.isNullObject(ownerIdAf) && !DataUtil.isNullObject(ownerTypeAf)) {
            query.setParameter("stateIdNew", stateIdAf);
            query.setParameter("ownerIdNew", ownerIdAf);
            query.setParameter("ownerTypeNew", ownerTypeAf);
        }
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("serial", serial);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("status", status);
        if (!DataUtil.isNullObject(stateId)) {
            query.setParameter("stateId", stateId);
        }
        return query.executeUpdate();
    }

    public boolean updateSerialGpon(String serial, String stockGPon, Long prodOfferId) throws Exception {
        String sqlUpdate = new String("update stock_handset set serial_gpon = #gpon, update_datetime = sysdate where prod_offer_id = #prodOfferId and serial = #serial");
        Query queryUpdate = em.createNativeQuery(sqlUpdate.toString());
        queryUpdate.setParameter("gpon", stockGPon);
        queryUpdate.setParameter("prodOfferId", prodOfferId);
        queryUpdate.setParameter("serial", serial);
        int result = queryUpdate.executeUpdate();
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateSerialGponIM1(String serial, String stockGPon, Long stockModelId) throws Exception {
        String sqlUpdate = new String("update bccs_im.stock_handset set serial_gpon = #gpon where stock_model_id = #stockModelId and serial = #serial");
        Query queryUpdate = emIM1.createNativeQuery(sqlUpdate.toString());
        queryUpdate.setParameter("gpon", stockGPon);
        queryUpdate.setParameter("stockModelId", stockModelId);
        queryUpdate.setParameter("serial", serial);
        int result = queryUpdate.executeUpdate();
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateSerialMultiScreen(String serial, String cadId, String macId, Long prodOfferId) throws Exception {
        try {
            String sqlUpdate = new String("update stock_handset SET TVMS_CAD_ID = trim(#cadId), TVMS_MAC_ID = trim(#macId), update_datetime = sysdate where prod_offer_id = #prodOfferId and serial = trim(#serial)");
            Query queryUpdate = em.createNativeQuery(sqlUpdate.toString());
            queryUpdate.setParameter("cadId", cadId);
            queryUpdate.setParameter("macId", macId);
            queryUpdate.setParameter("prodOfferId", prodOfferId);
            queryUpdate.setParameter("serial", serial);
            int result = queryUpdate.executeUpdate();
            if (result == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean updateSerialMultiScreenIM1(String serial, String cadId, String macId, Long stockModelId) throws Exception {
        try {
            String sqlUpdate = new String("update bccs_im.stock_handset SET TVMS_CAD_ID = trim(#cadId), TVMS_MAC_ID = trim(#macId) where stock_model_id = #stockModelId and serial = trim(#serial)");
            Query queryUpdate = emIM1.createNativeQuery(sqlUpdate.toString());
            queryUpdate.setParameter("cadId", cadId);
            queryUpdate.setParameter("macId", macId);
            queryUpdate.setParameter("stockModelId", stockModelId);
            queryUpdate.setParameter("serial", serial);
            int result = queryUpdate.executeUpdate();
            if (result == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public StockHandsetDTO checkExsitStockHandset(Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, String updateType) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("select * from stock_handset where prod_offer_id = #prodOfferId and serial = #serial and owner_id = #ownerId and owner_type = #ownerType and state_id = #stateId");
        if (DataUtil.safeEqual(updateType, Const.UPDATE_PINCODE_CARD.TYPE_NEW)) {
            sqlQuery.append(" and license_key is null");
        } else {
            sqlQuery.append(" and license_key is not null");
        }
        Query query = em.createNativeQuery(sqlQuery.toString(), StockHandset.class);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("stateId", stateId);
        List<StockHandset> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return mapper.toDtoBean(lst.get(0));
        }
        return null;
    }

    @Override
    public int updateListLicenseKey(Connection conn, List<UpdateLicenseDTO> lstUpdateLicenseDTOs, Long ownerId, Long prodOfferId, String updateType) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        PreparedStatement stmUpdate = null;
        try {
            sqlQuery.append("update stock_handset set license_key = ?,update_datetime = sysdate ");
            sqlQuery.append(" where prod_offer_id = ? and serial = ? and owner_id = ? and owner_type = ? and state_id = ? ");
            if (DataUtil.safeEqual(updateType, Const.UPDATE_PINCODE_CARD.TYPE_NEW)) {
                sqlQuery.append(" and license_key is null");
            } else {
                sqlQuery.append(" and license_key is not null");
            }
            stmUpdate = conn.prepareStatement(sqlQuery.toString());
            Long numberBatch = 0L;
            int count = 0;
            for (int i = 0; i < lstUpdateLicenseDTOs.size(); i++) {
                if (DataUtil.isNullOrEmpty(lstUpdateLicenseDTOs.get(i).getDesc())) {
                    stmUpdate.setString(1, lstUpdateLicenseDTOs.get(i).getLicenseKey());
                    stmUpdate.setLong(2, prodOfferId);
                    stmUpdate.setString(3, lstUpdateLicenseDTOs.get(i).getSerial());
                    stmUpdate.setLong(4, ownerId);
                    stmUpdate.setLong(5, Const.OWNER_TYPE.STAFF_LONG);
                    stmUpdate.setLong(6, Const.STATE_STATUS.NEW);
                    stmUpdate.addBatch();
                    count++;
                    if (i % Const.BATCH_SIZE_1000 == 0 && i > 0) {
                        try {
                            stmUpdate.executeBatch();
                            //so ban ghi insert thanh cong
                            conn.commit();
                            ++numberBatch;
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                            conn.rollback();
                        }
                    }
                }
            }

            //Xu ly voi so ban ghi con lai
            Long numberRemainRecord = lstUpdateLicenseDTOs.size() - numberBatch * Const.BATCH_SIZE_1000;
            if (numberRemainRecord > 0) {
                try {
                    stmUpdate.executeBatch();
                    conn.commit();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    conn.rollback();
                }
            }
            return count;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            if (stmUpdate != null) {
                stmUpdate.close();
            }
        }
    }

    @Override
    public StockHandsetDTO getStockHandset(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long stateId) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append("select sh.* from stock_handset sh, product_offering po " +
                " where sh.prod_offer_id = po.prod_offer_id and sh.prod_offer_id = #prodOfferId and sh.serial = #serial and po.status = #status ");
        if (!DataUtil.isNullOrZero(ownerType) && !DataUtil.isNullOrZero(ownerId)) {
            sqlQuery.append(" and sh.owner_type = #ownerType");
            sqlQuery.append(" and sh.owner_id = #ownerId");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            sqlQuery.append(" and sh.state_id = #stateId");
        }
        Query query = em.createNativeQuery(sqlQuery.toString(), StockHandset.class);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);
        query.setParameter("status", Const.STATUS_ACTIVE);
        if (!DataUtil.isNullOrZero(ownerType) && !DataUtil.isNullOrZero(ownerId)) {
            query.setParameter("ownerType", ownerType);
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        List<StockHandset> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return mapper.toDtoBean(lst.get(0));
        }
        return null;
    }


    public Long getQuantityStockX(Long ownerId, Long ownerType, Long prodOfferId, List<Serial> lstSerial, Long status, Long stateId) throws LogicException, Exception {
        if (ownerId == null
                || ownerType == null
                || prodOfferId == null
                || status == null
                || lstSerial == null
                || lstSerial.isEmpty()) {
            return 0L;
        }
        Long amountTotal = 0L;

        try {
            ProductOfferingDTO productOffering = productOfferingService.findOne(prodOfferId);
            if (DataUtil.isNullObject(productOffering)) {
                return 0L;
            }

            String tableName = IMServiceUtil.getTableNameByOfferType(productOffering.getProductOfferTypeId());
            StringBuilder strQuery = new StringBuilder("");
            strQuery.append("SELECT   NVL (count(1), 0) quantity,NVL (count(1), 0)");
            strQuery.append("  FROM   ").append(tableName);
            strQuery.append(" WHERE       prod_offer_id = #prodOfferId");
            strQuery.append("         AND owner_id = #ownerId");
            strQuery.append("         AND owner_type = #ownerType");
            if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOffering.getProductOfferTypeId()) || Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(productOffering.getProductOfferTypeId())) {
                strQuery.append("         AND  serial >= #fromSerial");
                strQuery.append("         AND  serial <= #toSerial");
            } else {
                strQuery.append("         AND to_number(serial) >= #fromSerial");
                strQuery.append("         AND to_number(serial) <= #toSerial");
            }
            strQuery.append("         AND status = #status");
            strQuery.append("         AND state_id = #stateId");
            BigInteger fromSerial;
            BigInteger toSerial;
            for (Serial serialToDo : lstSerial) {
                Query query = em.createNativeQuery(strQuery.toString());
                query.setParameter("prodOfferId", prodOfferId);
                query.setParameter("ownerId", ownerId);
                query.setParameter("ownerType", ownerType);
                if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(productOffering.getProductOfferTypeId()) || Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(productOffering.getProductOfferTypeId())) {
                    query.setParameter("fromSerial", serialToDo.getFromSerial());
                    query.setParameter("toSerial", serialToDo.getToSerial());
                } else {
                    fromSerial = new BigInteger(serialToDo.getFromSerial());
                    toSerial = new BigInteger(serialToDo.getToSerial());
                    query.setParameter("fromSerial", fromSerial);
                    query.setParameter("toSerial", toSerial);
                }
                query.setParameter("status", status);
                query.setParameter("stateId", stateId);

                List<Object[]> lst = query.getResultList();
                if (lst != null && !lst.isEmpty()) {
                    for (Object[] object : lst) {
                        amountTotal += DataUtil.safeToLong(object[0]);
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
        return amountTotal;
    }


    public StockHandsetDTO getStockModelSoPin(String serial) {
        try {

            if (DataUtil.isNullOrEmpty(serial)) {
                return null;
            }

            StringBuilder strQuery = new StringBuilder();
            strQuery.append("  SELECT sh.prod_offer_id, sh.so_pin, sh.serial ");
            strQuery.append("    FROM bccs_im.stock_handset sh, bccs_im.product_offering po ");
            strQuery.append("   WHERE 1=1 ");
            strQuery.append("     AND sh.prod_offer_id = po.prod_offer_id");
            strQuery.append("     AND sh.prod_offer_id IS NOT NULL");
            strQuery.append("     AND po.status = 1");
            strQuery.append("     AND sh.serial = #serial ");

            Query query = em.createNativeQuery(strQuery.toString());
            query.setParameter("serial", serial);

            List<Object[]> lstStockHandset = query.getResultList();

            if (!DataUtil.isNullOrEmpty(lstStockHandset)) {
                Object[] item = lstStockHandset.get(0);
                StockHandsetDTO stockHandset = new StockHandsetDTO();

                int i = 0;
                stockHandset.setProdOfferId(DataUtil.safeToLong(item[i++]));
                stockHandset.setSoPin(DataUtil.safeToString(item[i++]));
                stockHandset.setSerial(DataUtil.safeToString(item[i]));

                return stockHandset;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
        return null;
    }

    public boolean checkExistSerial(String serial) {
        try {
            if (DataUtil.isNullOrEmpty(serial)) {
                return false;
            }
            String sSerial;
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("  select serial from stock_handset where serial = #serial ");

            Query query = em.createNativeQuery(strQuery.toString());
            for (int i = 0; i < 10; i++) {
                sSerial = serial + String.valueOf(i);
                query.setParameter("serial", sSerial);
                List lstResult = query.getResultList();
                if (lstResult != null && lstResult.size() > 0) {
                    return true;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return false;
    }

    public boolean checkUnlockSerial(String serial) {
        try {
            if (DataUtil.isNullOrEmpty(serial)) {
                return false;
            }
            String sSerial;
            List<String> lstSerial = Lists.newArrayList();

            for (int i = 0; i < 10; i++) {
                sSerial = serial + String.valueOf(i);
                lstSerial.add(sSerial);
            }
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("  select serial from unlock_g6 where serial " + DbUtil.createInQuery("serial", lstSerial));

            Query query = em.createNativeQuery(strQuery.toString());
            DbUtil.setParamInQuery(query, "serial", lstSerial);
            List lstResult = query.getResultList();
            if (lstResult != null && lstResult.size() > 0) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return false;
    }

    public Object[] getInfomationSerial(String serial) {
        try {
            String sSerial;
            List<String> lstSerial = Lists.newArrayList();

            for (int i = 0; i < 10; i++) {
                sSerial = serial + String.valueOf(i);
                lstSerial.add(sSerial);
            }
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("  select prod_offer_id, unlock_code, serial from stock_handset where serial " + DbUtil.createInQuery("serial", lstSerial) + " and unlock_code is not null ");
            Query query = em.createNativeQuery(strQuery.toString());
            DbUtil.setParamInQuery(query, "serial", lstSerial);
            List lstResult = query.getResultList();
            if (lstResult != null && lstResult.size() > 0) {
                return (Object[]) lstResult.get(0);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
        return null;
    }

    public boolean saveUnlockG6(String serial, Long prodOfferId) {
        try {
            StringBuilder strBuff = new StringBuilder();
            strBuff.append("INSERT INTO unlock_g6 ");
            strBuff.append("            (id, serial, ");
            strBuff.append("             stock_model_id, ");
            strBuff.append("             channel, ");
            strBuff.append("             create_date) ");
            strBuff.append("     VALUES (unlock_g6_seq.NEXTVAL, #serial, #prodOfferid, #channel, sysdate) ");

            Query queryObject = em.createNativeQuery(strBuff.toString());
            queryObject.setParameter("serial", serial.trim());
            queryObject.setParameter("prodOfferid", prodOfferId);
            queryObject.setParameter("channel", 1L);
            queryObject.executeUpdate();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public void insertStockHandset(StockHandsetDTO stockHandsetDTO) throws LogicException, Exception {
        StringBuilder sqlInsert = new StringBuilder("");
        sqlInsert.append(" INSERT INTO bccs_im.stock_handset (id");
        sqlInsert.append("                                 ,stock_model_id ");
        sqlInsert.append("                                 ,owner_id, owner_type, create_date ");
        sqlInsert.append("                                 ,serial, status ");
        sqlInsert.append("                                 ,state_id ");
        sqlInsert.append("                                 ,check_dial,create_user ");
        sqlInsert.append("                                 )");
        sqlInsert.append("   VALUES   (#id ");
        sqlInsert.append("             ,#stock_model_id");
        sqlInsert.append("             ,#owner_id");
        sqlInsert.append("             ,#owner_type");
        sqlInsert.append("             ,sysdate");
        sqlInsert.append("             ,#serial");
        sqlInsert.append("             ,#status");
        sqlInsert.append("             ,#state_id");
        sqlInsert.append("             ,#check_dial,#createUser");
        sqlInsert.append("             )");
        Query query = emIM1.createNativeQuery(sqlInsert.toString());
        query.setParameter("id", stockHandsetDTO.getId());
        query.setParameter("stock_model_id", stockHandsetDTO.getProdOfferId());
        query.setParameter("owner_id", stockHandsetDTO.getOwnerId());
        query.setParameter("owner_type", stockHandsetDTO.getOwnerType());
        query.setParameter("serial", stockHandsetDTO.getSerial());
        query.setParameter("status", stockHandsetDTO.getStatus());
        query.setParameter("state_id", stockHandsetDTO.getStateId());
        query.setParameter("check_dial", 0L);
        String createUser = "";
        if (!DataUtil.isNullOrEmpty(stockHandsetDTO.getCreateUser())) {
            createUser = stockHandsetDTO.getCreateUser();
        }
        query.setParameter("createUser", createUser);
        int i = query.executeUpdate();
        if (i != 1) {
            throw new Exception("insert.stock.handset.action.err");
        }
    }

    public int deleteStockHandset(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, Long stateId) throws LogicException, Exception {
        StringBuilder sqlDelete = new StringBuilder("delete from stock_handset where prod_offer_id = #prodOfferId and serial = #serial and owner_id =#ownerId and owner_type = #ownerType and status = #status");
        sqlDelete.append("       and state_id = #stateId");
        Query query = em.createNativeQuery(sqlDelete.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("status", status);
        query.setParameter("stateId", stateId);
        return query.executeUpdate();
    }

    public int deleteStockHandsetIM1(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, Long stateId) throws LogicException, Exception {
        StringBuilder sqlDelete = new StringBuilder("delete from bccs_im.stock_handset where stock_model_id = #prodOfferId and serial = #serial and owner_id =#ownerId and owner_type = #ownerType and status = #status");
        sqlDelete.append("       and state_id = #stateId");
        Query query = emIM1.createNativeQuery(sqlDelete.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("status", status);
        query.setParameter("stateId", stateId);
        return query.executeUpdate();
    }

    @Override
    public StockHandset findBySerialForUpdate(String serial, Long prodOfferId, Long ownerId) throws Exception {
        if (!DataUtil.isNullOrEmpty(serial) && prodOfferId != null && ownerId != null) {
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(
                    "select * \n" +
                            "FROM stock_handset \n" +
                            "WHERE \n" +
                            "serial like #serial \n" +
                            "and prod_offer_id = #prodOfferId \n" +
                            "and owner_type = #ownerType \n" +
                            "and owner_id = #ownerId \n" +
                            "and state_id = #stateId "
            );
            Query query = em.createNativeQuery(strQuery.toString(), StockHandset.class);
            query.setParameter("serial", serial);
            query.setParameter("prodOfferId", prodOfferId);
            query.setParameter("ownerType", Const.OWNER_TYPE_STAFF);
            query.setParameter("ownerId", ownerId);
            query.setParameter("stateId", Const.STATE_EXPIRE);

            List<StockHandset> queryResult = query.getResultList();

            if (!DataUtil.isNullOrEmpty(queryResult)) {
                return queryResult.get(0);
            }
        }
        return null;
    }

    public StampDTO getStampInformation(Long prodOfferId, String fromSerial) {
        StringBuilder strQuery = new StringBuilder(" ");
        strQuery.append(" SELECT a.code, ");
        strQuery.append("        a.name, ");
        strQuery.append("        b.owner_code, ");
        strQuery.append("        b.owner_name, ");
        strQuery.append("        b.owner_id, ");
        strQuery.append("        b.owner_type, ");
        strQuery.append("        b.shop_path, ");
        strQuery.append("        b.contact_mobile, ");
        strQuery.append("        c.status, ");
        strQuery.append("        c.serial, ");
        strQuery.append(" (CASE ");
        strQuery.append("               WHEN b.owner_type = 2 ");
        strQuery.append("               THEN ");
        strQuery.append("                   (SELECT   shop_code ");
        strQuery.append("                      FROM   shop ");
        strQuery.append("                     WHERE   shop_id = b.parent_shop_id) ");
        strQuery.append("           END) ");
        strQuery.append("              AS shop_code, ");
        strQuery.append("              (CASE ");
        strQuery.append("               WHEN b.owner_type = 2 ");
        strQuery.append("               THEN ");
        strQuery.append("                   (SELECT   name ");
        strQuery.append("                      FROM   shop ");
        strQuery.append("                     WHERE   shop_id = b.parent_shop_id) ");
        strQuery.append("           END) ");
        strQuery.append("              AS shop_name, ");
        strQuery.append("              (CASE ");
        strQuery.append("               WHEN b.owner_type = 2 ");
        strQuery.append("               THEN ");
        strQuery.append("               b.parent_shop_id ");
        strQuery.append("           END) ");
        strQuery.append("              AS shop_id, ");
        strQuery.append("              (CASE ");
        strQuery.append("               WHEN b.owner_type = 2 ");
        strQuery.append("               THEN ");
        strQuery.append("                   (SELECT   shop_path ");
        strQuery.append("                      FROM   shop ");
        strQuery.append("                     WHERE   shop_id = b.parent_shop_id) ");
        strQuery.append("           END) ");
        strQuery.append("              AS shop_path, ");
        strQuery.append("        c.update_datetime ");
        strQuery.append("   FROM product_offering a, ");
        strQuery.append("        mv_shop_staff b, ");
        strQuery.append("        stock_handset c ");
        strQuery.append(" WHERE     a.prod_offer_id = c.prod_offer_id ");
        strQuery.append("        AND b.owner_type = c.owner_type ");
        strQuery.append("        AND b.owner_id = c.owner_id ");
        strQuery.append("        AND a.prod_offer_id = #prodOfferId ");
        strQuery.append("         AND TO_NUMBER (c.serial) = TO_NUMBER(#fromSerial)");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("fromSerial", fromSerial);
        List listResult = query.getResultList();
        StampDTO stampDTO;
        if (!DataUtil.isNullOrEmpty(listResult)) {
            for (Object o : listResult) {
                Object[] obj = (Object[]) o;
                int index = 0;
                stampDTO = new StampDTO();
                stampDTO.setStockModelCode(DataUtil.safeToString(obj[index++]));
                stampDTO.setStockModelName(DataUtil.safeToString(obj[index++]));
                stampDTO.setOwnerCode(DataUtil.safeToString(obj[index++]));
                stampDTO.setOwnerName(DataUtil.safeToString(obj[index++]));
                stampDTO.setOwnerId(DataUtil.safeToLong(obj[index++]));
                stampDTO.setOwnerType(DataUtil.safeToLong(obj[index++]));
                stampDTO.setShopPath(DataUtil.safeToString(obj[index++]));
                stampDTO.setPhoneNumber(DataUtil.safeToString(obj[index++]));
                stampDTO.setStatus(DataUtil.safeToLong(obj[index++]));
                stampDTO.setFromSerial(DataUtil.safeToString(obj[index++]));
                stampDTO.setParrentShopCode(DataUtil.safeToString(obj[index++]));
                stampDTO.setParrentShopName(DataUtil.safeToString(obj[index++]));
                Object shopId = obj[index++];
                stampDTO.setParrentShopId(shopId == null ? null : DataUtil.safeToLong(shopId));
                stampDTO.setParrentShopPath(DataUtil.safeToString(obj[index++]));
                stampDTO.setUpdateDatetime(obj[index] == null ? null : (Date) obj[index]);
                return stampDTO;
            }
        }
        return null;
    }
}
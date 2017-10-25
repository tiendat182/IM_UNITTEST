package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockCard;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockCard.COLUMNS;
import com.viettel.bccs.inventory.model.QStockCard;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockCardRepoImpl implements StockCardRepoCustom {

    public static final Logger logger = Logger.getLogger(StockCardRepoCustom.class);
    private final BaseMapper<StockCard, StockCardDTO> mapper = new BaseMapper(StockCard.class, StockCardDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager emSale;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockCard stockCard = QStockCard.stockCard;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockCard.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockCard.createUser, filter);
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(stockCard.fromSerial, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockCard.id, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockCard.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockCard.ownerType, filter);
                        break;
                    case POCODE:
                        expression = DbUtil.createStringExpression(stockCard.poCode, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockCard.prodOfferId, filter);
                        break;
                    case SALEDATE:
                        expression = DbUtil.createDateExpression(stockCard.saleDate, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockCard.stateId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockCard.status, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(stockCard.telecomServiceId, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(stockCard.toSerial, filter);
                        break;
                    case VCSTATUS:
                        expression = DbUtil.createLongExpression(stockCard.vcStatus, filter);
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
    public List<StockCard> findStockCard(StockTransDetailDTO stockTransDetail, Long oldStatus, Long oldOwnerType,
                                         Long oldOwnerId, String fromSerial, String toSerial) throws LogicException, Exception {

        List<Object> paramLst = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("");

        sqlQuery.append("SELECT  *    ");
        sqlQuery.append("  FROM  stock_card    ");
        sqlQuery.append(" WHERE  1=1 ");

        if (!DataUtil.isNullOrZero(stockTransDetail.getProdOfferId())) {
            sqlQuery.append("   AND  prod_offer_id = ? ");
            paramLst.add(stockTransDetail.getProdOfferId());
        }
        if (!DataUtil.isNullOrZero(oldOwnerType)) {
            sqlQuery.append("   AND  owner_type = ? ");
            paramLst.add(oldOwnerType);
        }
        if (!DataUtil.isNullOrZero(oldOwnerId)) {
            sqlQuery.append("   AND  owner_id = ? ");
            paramLst.add(oldOwnerId);
        }
        if (!DataUtil.isNullOrEmpty(fromSerial)) {
            sqlQuery.append("   AND  to_number(from_serial) <= ? ");
            paramLst.add(fromSerial);
        }
        if (!DataUtil.isNullOrEmpty(toSerial)) {
            sqlQuery.append("   AND  to_number(to_serial) >= ? ");
            paramLst.add(toSerial);
        }
        if (!DataUtil.isNullOrZero(oldStatus)) {
            sqlQuery.append("   AND  status = ? ");
            paramLst.add(oldStatus);
        }
        if (!DataUtil.isNullOrZero(stockTransDetail.getStateId())) {
            sqlQuery.append("   AND  state_id = ? ");
            paramLst.add(stockTransDetail.getStateId());
        }

        Query query = em.createNativeQuery(sqlQuery.toString(), StockCard.class);

        for (int i = 0; i < paramLst.size(); i++) {
            query.setParameter(i + 1, paramLst.get(i));

        }

        return query.getResultList();
    }

    @Override
    public Long checkNotExistInVC(Long prodOfferId, String fromSerial, String toSerial) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append(" SELECT   count(1) as amountTotal");
        sqlQuery.append(" FROM   stock_card");
        sqlQuery.append("  WHERE       prod_offer_id = ? ");
        sqlQuery.append("           AND TO_NUMBER (serial) >= TO_NUMBER (?) ");
        sqlQuery.append("           AND TO_NUMBER (serial) <= TO_NUMBER (?) ");
        sqlQuery.append("           AND vc_status = 0 ");
        Query queryObject = em.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter(1, prodOfferId);
        queryObject.setParameter(2, fromSerial);
        queryObject.setParameter(3, toSerial);
        return (Long) queryObject.getSingleResult();
    }

    @Override
    public void insertStockCardFromSaled(Long prodOfferId, String serial) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder(" ");
        StringBuilder sqlSelect = new StringBuilder(" ");
        StockCardDTO stockCardDTO = null;
        //
        sqlSelect.append(" SELECT   id, stock_model_id, serial, owner_id, owner_type, create_date, status, telecom_service_id, state_id,");
        sqlSelect.append("  null user_session_id, create_user, null deposit_price, null sale_date, null stock_trans_id, null contract_code, null po_code, null vc_status from bccs_im.his_stock_card ");
        sqlSelect.append(" where serial = #serial and stock_model_id = #prodOfferId and rownum < 2");
        Query querySelect = emIm1.createNativeQuery(sqlSelect.toString());
        querySelect.setParameter("serial", serial);
        querySelect.setParameter("prodOfferId", prodOfferId);
        List<Object[]> lst = querySelect.getResultList();
        if (lst != null && !lst.isEmpty()) {
            Object[] object = lst.get(0);
            stockCardDTO = new StockCardDTO();
            int index = 0;
            stockCardDTO.setId(DataUtil.safeToLong(object[index++]));
            stockCardDTO.setProdOfferId(DataUtil.safeToLong(object[index++]));
            stockCardDTO.setFromSerial(DataUtil.safeToString(object[index++]));
            stockCardDTO.setOwnerId(DataUtil.safeToLong(object[index++]));
            stockCardDTO.setOwnerType(DataUtil.safeToLong(object[index++]));
            Date createDate = (Date) object[index++];
            stockCardDTO.setCreateDate(createDate);
            stockCardDTO.setStatus(DataUtil.safeToLong(object[index++]));
            stockCardDTO.setTelecomServiceId(DataUtil.safeToLong(object[index++]));
            stockCardDTO.setStateId(DataUtil.safeToLong(object[index++]));
            stockCardDTO.setCreateUser(DataUtil.safeToString(object[index]));
        }
        if (!DataUtil.isNullObject(stockCardDTO)) {
            sqlQuery.append(" INSERT INTO STOCK_CARD (id, prod_offer_id, serial, owner_id, owner_type, create_date, status, telecom_service_id, ");
            sqlQuery.append("       state_id, user_session_id, create_user, deposit_price, sale_date, stock_trans_id, contract_code, po_code, vc_status)");
            sqlQuery.append("  VALUES(#id,#prod_offer_id,#serial, #owner_id, #owner_type, #create_date, #status, #telecom_service_id,");
            sqlQuery.append(" #state_id, null, #create_user, null, null, null, null, null, null)");
            Query queryObject = em.createNativeQuery(sqlQuery.toString());
            queryObject.setParameter("id", stockCardDTO.getId());
            queryObject.setParameter("prod_offer_id", stockCardDTO.getProdOfferId());
            queryObject.setParameter("serial", stockCardDTO.getFromSerial());
            queryObject.setParameter("owner_id", stockCardDTO.getOwnerId());
            queryObject.setParameter("owner_type", stockCardDTO.getOwnerType());
            queryObject.setParameter("create_date", stockCardDTO.getCreateDate());
            queryObject.setParameter("status", stockCardDTO.getStatus());
            queryObject.setParameter("telecom_service_id", stockCardDTO.getTelecomServiceId());
            queryObject.setParameter("state_id", stockCardDTO.getStateId());
            queryObject.setParameter("create_user", stockCardDTO.getCreateUser());
            queryObject.executeUpdate();
        }
    }

    @Override
    public void insertStockCardFromSaledIm1(Long prodOfferId, String serial) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append("insert into bccs_im.STOCK_CARD (ID,STOCK_MODEL_ID,SERIAL,CARD_TYPE,AMOUNT_TYPE,OWNER_ID,OWNER_TYPE,CREATE_DATE,EXPIRED_DATE,STATUS,TELECOM_SERVICE_ID,STATE_ID,CHECK_DIAL,DIAL_STATUS,USER_SESSION_ID,CREATE_USER,OWNER_RECEIVER_ID,OWNER_RECEIVER_TYPE,RECEIVER_NAME,DEPOSIT_PRICE,SALE_DATE,CONNECTION_TYPE,CONNECTION_STATUS,CONNECTION_DATE,PINCODE,STOCK_TRANS_ID,CONTRACT_CODE,PO_CODE,SERIAL_REAL,VC_STATUS)");
        sqlQuery.append(" (select ID,STOCK_MODEL_ID,SERIAL,CARD_TYPE,AMOUNT_TYPE,OWNER_ID,OWNER_TYPE,CREATE_DATE,EXPIRED_DATE,STATUS,TELECOM_SERVICE_ID,STATE_ID,CHECK_DIAL,DIAL_STATUS,USER_SESSION_ID,CREATE_USER,OWNER_RECEIVER_ID,OWNER_RECEIVER_TYPE,RECEIVER_NAME,DEPOSIT_PRICE,SALE_DATE,CONNECTION_TYPE,CONNECTION_STATUS,CONNECTION_DATE,PINCODE,STOCK_TRANS_ID,CONTRACT_CODE,PO_CODE,SERIAL_REAL,VC_STATUS  ");
        sqlQuery.append(" from bccs_im.v_old_stock_card where serial = #serial and stock_model_id = #prodOfferId and rownum < 2)");
        Query queryObject = emIm1.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("serial", serial);
        queryObject.setParameter("prodOfferId", prodOfferId);
        queryObject.executeUpdate();
    }

    @Override
    public boolean checkStockCardIm1(Long prodOfferId, String serial) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append(" SELECT   1 ");
        sqlQuery.append(" FROM   bccs_im.stock_card");
        sqlQuery.append("  WHERE       stock_model_id = ? ");
        sqlQuery.append("           AND TO_NUMBER (serial) = TO_NUMBER (?) ");
        Query queryObject = emIm1.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter(1, prodOfferId);
        queryObject.setParameter(2, serial);
        List lst = queryObject.getResultList();
        if (DataUtil.isNullOrEmpty(lst)) {
            return false;
        }
        return true;
    }

    @Override
    public int updatePincodeForStockCard(Long ownerId, String serial, Long prodOfferId, String pincode, String updateType) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append("update stock_card set pincode = #pincode,update_datetime = sysdate ");
        sqlQuery.append(" where  owner_type = 1 and state_id = 1 and status =1 and owner_id = #ownerId and TO_NUMBER(serial) = TO_NUMBER (#serial) ");
        sqlQuery.append(" and prod_offer_id= #prodOfferId ");
        if (DataUtil.safeEqual(updateType, Const.UPDATE_PINCODE_CARD.TYPE_NEW)) {
            sqlQuery.append(" and pincode is null");
        } else {
            sqlQuery.append(" and pincode is not null");
        }
        Query queryObject = em.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("pincode", pincode);
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("serial", serial);
        queryObject.setParameter("prodOfferId", prodOfferId);
        return queryObject.executeUpdate();
    }

    @Override
    public int updatePincodeForStockCardIM1(Long ownerId, String serial, Long prodOfferId, String pincode, String updateType) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append("update bccs_im.stock_card set pincode = #pincode ");
        sqlQuery.append(" where  owner_type = 1 and state_id = 1 and status =1 and owner_id = #ownerId and TO_NUMBER(serial) = TO_NUMBER (#serial) ");
        sqlQuery.append(" and stock_model_id= #prodOfferId ");
        if (DataUtil.safeEqual(updateType, Const.UPDATE_PINCODE_CARD.TYPE_NEW)) {
            sqlQuery.append(" and pincode is null");
        } else {
            sqlQuery.append(" and pincode is not null");
        }
        Query queryObject = emIm1.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("pincode", pincode);
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("serial", serial);
        queryObject.setParameter("prodOfferId", prodOfferId);
        return queryObject.executeUpdate();
    }

    public int saveActionLog(Long actionId, String actionType, String serial, String actionUser, String actionIp, Long prodOfferId) throws Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append(" INSERT INTO bccs_im.action_log(action_id,");
        sqlQuery.append("                        action_type,");
        sqlQuery.append("                        description,");
        sqlQuery.append("                        action_user,");
        sqlQuery.append("                        action_date,");
        sqlQuery.append("                        action_ip,");
        sqlQuery.append("                        object_id)");
        sqlQuery.append(" VALUES (#actionId, #actionType, #serial, #actionUser, sysdate, #actionIp, #prodOfferId)");
        Query queryObject = emIm1.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("actionId", actionId);
        queryObject.setParameter("actionType", actionType);
        queryObject.setParameter("serial", serial);
        queryObject.setParameter("actionUser", actionUser);
        queryObject.setParameter("actionIp", actionIp);
        queryObject.setParameter("prodOfferId", prodOfferId);
        return queryObject.executeUpdate();
    }

    public int saveActionLogDetail(Long actionId, String pincode, String serial) throws Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append(" INSERT INTO bccs_im.action_log_detail(action_detail_id,");
        sqlQuery.append("                                      action_id,");
        sqlQuery.append("                                      table_name,");
        sqlQuery.append("                                      column_name,");
        sqlQuery.append("                                      old_value,");
        sqlQuery.append("                                      new_value,");
        sqlQuery.append("                                      description,");
        sqlQuery.append("                                      action_date)");
        sqlQuery.append(" VALUES (bccs_im.action_log_detail_seq.NEXTVAL, #actionId,'STOCK_CARD','PINCODE',#pincode, #pincode, #serial,sysdate)");
        Query queryObject = emIm1.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("actionId", actionId);
        queryObject.setParameter("pincode", pincode);
        queryObject.setParameter("serial", serial);
        return queryObject.executeUpdate();
    }

    @Override
    public int updateListPincodeForStockCard(Connection conn, List<UpdatePincodeDTO> lstPincodes, Long ownerId, Long prodOfferId, String updateType) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        PreparedStatement stmUpdate = null;

        try {
            sqlQuery.append("update stock_card set pincode = ?,update_datetime = sysdate ");
            sqlQuery.append(" where  owner_type = 1 and state_id = 1 and status =1 and owner_id = ? and TO_NUMBER(serial) = TO_NUMBER (?) ");
            sqlQuery.append(" and prod_offer_id= ? ");
            if (DataUtil.safeEqual(updateType, Const.UPDATE_PINCODE_CARD.TYPE_NEW)) {
                sqlQuery.append(" and pincode is null");
            } else {
                sqlQuery.append(" and pincode is not null");
            }
            stmUpdate = conn.prepareStatement(sqlQuery.toString());
            Long numberBatch = 0L;
            int count = 0;
            for (int i = 0; i < lstPincodes.size(); i++) {
                if (DataUtil.isNullOrEmpty(lstPincodes.get(i).getDesc())) {
                    String tempPincodeEncrypt = DataUtil.encryptByDES64(lstPincodes.get(i).getPincode().trim());
                    stmUpdate.setString(1, tempPincodeEncrypt);
                    stmUpdate.setLong(2, ownerId);
                    stmUpdate.setString(3, lstPincodes.get(i).getSerial());
                    stmUpdate.setLong(4, prodOfferId);
                    stmUpdate.addBatch();
                    if (i % Const.DEFAULT_BATCH_SIZE == 0 && i > 0) {
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
            Long numberRemainRecord = lstPincodes.size() - numberBatch * Const.DEFAULT_BATCH_SIZE;
            if (numberRemainRecord > 0) {
                try {
                    stmUpdate.executeBatch();
                    conn.commit();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    conn.rollback();
                }
            }
            return count + numberRemainRecord.intValue();
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
    public int updateListPincodeForStockCardIM1(Connection conn, List<UpdatePincodeDTO> lstPincodes, Long ownerId, Long prodOfferId, String updateType, StaffDTO staffDTO) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        StringBuilder sqlActionLog = new StringBuilder("");
        StringBuilder sqlActionLogDetail = new StringBuilder("");
        PreparedStatement stmUpdate = null;
        PreparedStatement stmSaveActionLog = null;
        PreparedStatement stmSaveActionLogDetail = null;
        try {
            sqlQuery.append("update bccs_im.stock_card set pincode = ? ");
            sqlQuery.append(" where  owner_type = 1 and state_id = 1 and status =1 and owner_id = ? and TO_NUMBER(serial) = TO_NUMBER (?) ");
            sqlQuery.append(" and stock_model_id= ? ");
            if (DataUtil.safeEqual(updateType, Const.UPDATE_PINCODE_CARD.TYPE_NEW)) {
                sqlQuery.append(" and pincode is null");
                updateType = Const.UPDATE_PINCODE_CARD.ACTION_TYPE_ADD_PINCODE;
            } else {
                sqlQuery.append(" and pincode is not null");
                updateType = Const.UPDATE_PINCODE_CARD.ACTION_TYPE_UPDATE_PINCODE;
            }
            stmUpdate = conn.prepareStatement(sqlQuery.toString());
            //action log
            sqlActionLog.append(" INSERT INTO bccs_im.action_log(action_id,");
            sqlActionLog.append("                        action_type,");
            sqlActionLog.append("                        description,");
            sqlActionLog.append("                        action_user,");
            sqlActionLog.append("                        action_date,");
            sqlActionLog.append("                        action_ip,");
            sqlActionLog.append("                        object_id)");
            sqlActionLog.append(" VALUES (?, ?, ?, ?, sysdate, ?, ?)");
            stmSaveActionLog = conn.prepareStatement(sqlActionLog.toString());

            //action log detail
            sqlActionLogDetail.append(" INSERT INTO bccs_im.action_log_detail(action_detail_id,");
            sqlActionLogDetail.append("                                      action_id,");
            sqlActionLogDetail.append("                                      table_name,");
            sqlActionLogDetail.append("                                      column_name,");
            sqlActionLogDetail.append("                                      old_value,");
            sqlActionLogDetail.append("                                      new_value,");
            sqlActionLogDetail.append("                                      description,");
            sqlActionLogDetail.append("                                      action_date)");
            sqlActionLogDetail.append(" VALUES (bccs_im.action_log_detail_seq.NEXTVAL, ?,'STOCK_CARD','PINCODE',?, ?, ?,sysdate)");
            stmSaveActionLogDetail = conn.prepareStatement(sqlActionLogDetail.toString());
            //
            Long numberBatch = 0L;
            int count = 0;
            for (int i = 0; i < lstPincodes.size(); i++) {
                if (DataUtil.isNullOrEmpty(lstPincodes.get(i).getDesc())) {
                    Long actionId = DbUtil.getSequence(emIm1, "bccs_im.action_log_seq");
                    String tempPincodeEncrypt = DataUtil.encryptByDES64(lstPincodes.get(i).getPincode().trim());
                    stmUpdate.setString(1, tempPincodeEncrypt);
                    stmUpdate.setLong(2, ownerId);
                    stmUpdate.setString(3, lstPincodes.get(i).getSerial());
                    stmUpdate.setLong(4, prodOfferId);
                    stmUpdate.addBatch();
                    //action log
                    stmSaveActionLog.setLong(1, actionId);
                    stmSaveActionLog.setString(2, updateType);
                    stmSaveActionLog.setString(3, lstPincodes.get(i).getSerial());
                    stmSaveActionLog.setString(4, staffDTO.getStaffCode());
                    stmSaveActionLog.setString(5, staffDTO.getIpAddress());
                    stmSaveActionLog.setLong(6, prodOfferId);
                    stmSaveActionLog.addBatch();
                    //action log detail
                    stmSaveActionLogDetail.setLong(1, actionId);
                    stmSaveActionLogDetail.setString(2, tempPincodeEncrypt);
                    stmSaveActionLogDetail.setString(3, tempPincodeEncrypt);
                    stmSaveActionLogDetail.setString(4, lstPincodes.get(i).getSerial());
                    stmSaveActionLogDetail.addBatch();
                    //
                    count++;
                    if (i % Const.DEFAULT_BATCH_SIZE == 0 && i > 0) {
                        try {
                            stmUpdate.executeBatch();
                            stmSaveActionLog.executeBatch();
                            stmSaveActionLogDetail.executeBatch();
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
            Long numberRemainRecord = lstPincodes.size() - numberBatch * Const.DEFAULT_BATCH_SIZE;
            if (numberRemainRecord > 0) {
                try {
                    stmUpdate.executeBatch();
                    stmSaveActionLog.executeBatch();
                    stmSaveActionLogDetail.executeBatch();
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
            if (stmSaveActionLog != null) {
                stmSaveActionLog.close();
            }
            if (stmSaveActionLogDetail != null) {
                stmSaveActionLogDetail.close();
            }
        }
    }

    @Override
    public boolean checkInfoStockCard(String pincode, Long ownerId, String serial, Long prodOfferId, String updateType, Long stateId) throws Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append("select * from  stock_card");
        sqlQuery.append(" where  owner_type = 1 and status =1 and owner_id = #ownerId and TO_NUMBER(serial) = TO_NUMBER (#serial) ");
        sqlQuery.append(" and prod_offer_id = #prodOfferId and state_id= #stateId");
        if (DataUtil.safeEqual(updateType, Const.UPDATE_PINCODE_CARD.TYPE_NEW)) {
            sqlQuery.append(" and pincode is null");
        } else {
            sqlQuery.append(" and pincode is not null");
        }
        Query queryObject = em.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("serial", serial);
        queryObject.setParameter("prodOfferId", prodOfferId);
        queryObject.setParameter("stateId", stateId);
        List lst = queryObject.getResultList();
        if (DataUtil.isNullOrEmpty(lst)) {
            return false;
        }
        return true;
    }

    @Override
    public StockCardDTO getInfoStockCard(String serial, Long ownerId, Long status, Long stateId) throws Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append("select pincode,prod_offer_id from  stock_card");
        sqlQuery.append(" where  owner_type = 1 and status = #status and owner_id = #ownerId and TO_NUMBER(serial) = TO_NUMBER (#serial) ");
        if (!DataUtil.isNullOrZero(stateId)) {
            sqlQuery.append("  and state_id= #stateId");
        }
        Query queryObject = em.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("ownerId", ownerId);
        queryObject.setParameter("serial", serial);
        queryObject.setParameter("status", status);
        if (!DataUtil.isNullOrZero(stateId)) {
            queryObject.setParameter("stateId", stateId);
        }
        List<Object[]> lst = queryObject.getResultList();
        if (lst != null && !lst.isEmpty()) {
            Object[] object = lst.get(0);
            StockCardDTO stockCardDTO = new StockCardDTO();
            stockCardDTO.setPinCode(DataUtil.safeToString(object[0]));
            stockCardDTO.setProdOfferId(DataUtil.safeToLong(object[1]));
            return stockCardDTO;
        }
        return null;
    }

    public StockTotalResultDTO getQuantityInEcomStock(String prodOfferCode) throws LogicException, Exception {
        StockTotalResultDTO stockTotalResultDTO = new StockTotalResultDTO();
        stockTotalResultDTO.setQuantity(0L);
        stockTotalResultDTO.setStockModelCode(prodOfferCode);
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append("SELECT count(*) AS quantity, sm.prod_offer_id, sm.name AS stock_model_name");
        sqlQuery.append(" FROM stock_card st, product_offering sm");
        sqlQuery.append(" WHERE st.status = 1 AND st.state_id = 1 AND st.owner_type = 1");
        sqlQuery.append(" AND st.owner_id in (SELECT shop_id FROM shop WHERE status = 1 AND shop_code = ");
        sqlQuery.append(" (SELECT   sv.VALUE");
        sqlQuery.append(" FROM   option_set s, option_set_value sv");
        sqlQuery.append(" WHERE       s.id = sv.option_set_id");
        sqlQuery.append(" AND s.status = 1");
        sqlQuery.append(" AND sv.status = 1");
        sqlQuery.append(" AND s.code = 'SHOP_TMDT'))");
        sqlQuery.append(" AND st.prod_offer_id = sm.prod_offer_id AND sm.status = 1 AND LOWER(sm.code) = #prodOfferCode");
        sqlQuery.append(" AND st.pincode IS NOT NULL");
        sqlQuery.append(" GROUP BY sm.prod_offer_id, sm.name");
        Query queryObject = em.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("prodOfferCode", prodOfferCode.toLowerCase());
        List<Object[]> lst = queryObject.getResultList();
        if (lst != null && !lst.isEmpty()) {
            Object[] object = lst.get(0);
            stockTotalResultDTO.setQuantity(DataUtil.safeToLong(object[0]));
            stockTotalResultDTO.setStockModelCode(prodOfferCode);
            stockTotalResultDTO.setStockModelName(DataUtil.safeToString(object[2]));
            stockTotalResultDTO.setResponseCode("0");
        }
        return stockTotalResultDTO;

    }

    public List<Long> getStatusSerialCardSale(String serial) throws LogicException, Exception {
        List<Long> lstStatus = Lists.newArrayList();
        StringBuilder sqlQuery = new StringBuilder("");
        sqlQuery.append(" SELECT   a.status status, a.serial");
        sqlQuery.append("   FROM   stock_card a,");
        sqlQuery.append("          product_offering pr,");
        sqlQuery.append("          option_set s,");
        sqlQuery.append("          option_set_value sv");
        sqlQuery.append("  WHERE       TO_NUMBER (a.serial) = #serial");
        sqlQuery.append(" AND pr.code = sv.VALUE");
        sqlQuery.append(" AND pr.prod_offer_id = a.prod_offer_id");
        sqlQuery.append("          AND s.id = sv.option_set_id");
        sqlQuery.append("          AND s.status = 1");
        sqlQuery.append("          AND sv.status = 1");
        sqlQuery.append("          AND s.code = 'STOCK_MODEL_CARD_DATA'");
        Query queryObject = em.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("serial", serial.trim());
        List<Object[]> lst = queryObject.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                lstStatus.add(DataUtil.safeToLong(object[0]));
            }
        }
        return lstStatus;
    }

    public List<LockupTransactionCardDTO> getSaleTranInfo(ExchangeCardBankplusDTO exchangeCardBankplusDTO) throws Exception {
        StringBuilder sqlQuery = new StringBuilder("");
        List<LockupTransactionCardDTO> lstResult = Lists.newArrayList();
        sqlQuery.append(" SELECT a.contract_no  as isdn, c.from_serial  as fromSerial, c.to_serial as toSerial");
        sqlQuery.append("   FROM sale_trans a, sale_trans_detail b, sale_trans_serial c");
        sqlQuery.append("          WHERE 1=1 ");
        sqlQuery.append("          AND a.sale_trans_type = 29");
        sqlQuery.append("          AND a.status in (2,3) AND a.is_adjust is null");
        sqlQuery.append("          AND a.sale_trans_id = b.sale_trans_id");
        sqlQuery.append("          AND b.sale_trans_detail_id = c.sale_trans_detail_id");
        sqlQuery.append("          AND a.sale_trans_date >= trunc(#saleDate)");
        sqlQuery.append("          AND a.sale_trans_date < trunc(#saleDate) + 1");
        sqlQuery.append("          AND b.sale_trans_date >= trunc(#saleDate)");
        sqlQuery.append("          AND b.sale_trans_date < trunc(#saleDate) + 1");
        sqlQuery.append("          AND c.sale_trans_date >= trunc(#saleDate)");
        sqlQuery.append("          AND c.sale_trans_date < trunc(#saleDate) + 1");
        sqlQuery.append("          AND b.stock_model_id = #prodOfferId");
        sqlQuery.append("          AND c.from_serial = #fromSerial");
        Query queryObject = emSale.createNativeQuery(sqlQuery.toString());
        queryObject.setParameter("saleDate", exchangeCardBankplusDTO.getSaleDate());
        queryObject.setParameter("prodOfferId", exchangeCardBankplusDTO.getProdOfferId());
        queryObject.setParameter("fromSerial", exchangeCardBankplusDTO.getSerialError());
        List<Object[]> lst = queryObject.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                LockupTransactionCardDTO lockupTransactionCardDTO = new LockupTransactionCardDTO();
                lockupTransactionCardDTO.setIsdn(DataUtil.safeToString(object[0]));
                lockupTransactionCardDTO.setFromSerial(DataUtil.safeToString(object[1]));
                lockupTransactionCardDTO.setToSerial(DataUtil.safeToString(object[2]));
                lstResult.add(lockupTransactionCardDTO);
            }
        }
        return lstResult;
    }

    @Override
    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serialList) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
                "SELECT    sc.serial,\n" +
                        "  (SELECT  price \n" +
                        "    FROM  product_offer_price pr \n" +
                        "    WHERE  pr.PRICE_TYPE_ID = 1 \n" +
                        "      AND pr.STATUS = 1 \n" +
                        "      AND pr.PRICE_POLICY_ID = 1 \n" +
                        "      AND (pr.CREATE_DATETIME IS NULL \n" +
                        "      OR TRUNC (pr.CREATE_DATETIME) <= TRUNC (SYSDATE)) \n" +
                        "      AND (pr.EXPIRE_DATETIME IS NULL \n" +
                        "      OR TRUNC (pr.EXPIRE_DATETIME) >= TRUNC (SYSDATE)) \n" +
                        "      AND pr.prod_offer_id = sc.prod_offer_id \n" +
                        "      AND ROWNUM = 1) AS price, \n" +
                        "  TO_CHAR (sc.sale_date, 'yyyy-MM-dd HH24:mi:ss') AS sale_trans_date, \n" +
                        "  receiver_name AS staff_code, \n" +
                        "  DECODE ( owner_receiver_type, 2, \n" +
                        "    (SELECT ar.name \n" +
                        "      FROM  staff st, shop sh, area ar \n" +
                        "      WHERE st.shop_id = sh.shop_id \n" +
                        "        AND st.staff_id = sc.owner_receiver_id \n" +
                        "        AND sh.province = ar.area_code AND ROWNUM = 1 ), \n" +
                        "    (SELECT ar.name \n" +
                        "      FROM shop sh, area ar \n" +
                        "      WHERE sh.shop_id = sc.owner_receiver_id \n" +
                        "        AND sh.province = ar.area_code AND ROWNUM = 1)) AS name, \n" +
                        "  DECODE ( owner_receiver_type, 2, \n" +
                        "    (SELECT  sh.province \n" +
                        "      FROM  staff st, shop sh \n" +
                        "      WHERE  st.shop_id = sh.shop_id \n" +
                        "        AND st.staff_id = sc.owner_receiver_id AND ROWNUM = 1), \n" +
                        "    (SELECT  province \n" +
                        "      FROM shop \n" +
                        "      WHERE shop_id = sc.owner_receiver_id AND ROWNUM = 1)) AS province, \n" +
                        "  DECODE (sc.status,0,'0', '1') \n" +
                        "FROM stock_card sc \n" +
                        "WHERE 1 = 1 AND  to_number(sc.serial) " + DbUtil.createInQuery("serialList", serialList)
        );

        Query query = em.createNativeQuery(strQuery.toString());
        DbUtil.setParamInQuery(query, "serialList", serialList);
        List queryResult = query.getResultList();
        List<LookupSerialCardAndKitByFileDTO> result = Lists.newArrayList();
        if (!queryResult.isEmpty()) {
            for (Object obj : queryResult) {
                //try {
                result.add(valueDTO(obj));
                //} catch (Exception e) {
                //    logger.error(e.getMessage(), e);
                //}
            }
        }
        return result;
    }

    public LookupSerialCardAndKitByFileDTO valueDTO(Object obj) throws Exception {
        Object[] objects = (Object[]) obj;
        LookupSerialCardAndKitByFileDTO valueDTO = new LookupSerialCardAndKitByFileDTO();
        valueDTO.setSerial(DataUtil.safeToString(objects[0]));
        valueDTO.setPrice(safeToString(objects[1]));
        if (objects[2] != null) {
            String dateExpStr = DataUtil.safeToString(objects[2]);
            DateFormat df = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
            Date dateExp = df.parse(dateExpStr);
            valueDTO.setDateExp(dateExp);
        } else {
            valueDTO.setDateExp(null);
        }
        valueDTO.setStaffCode(DataUtil.safeToString(objects[3]));
        valueDTO.setProvinceName(DataUtil.safeToString(objects[4]));
        valueDTO.setProvinceCode(DataUtil.safeToString(objects[5]));
        valueDTO.setNote(DataUtil.safeToString(objects[6]));
        return valueDTO;
    }

    public String safeToString(Object obj) {
        return obj == null ? null : obj.toString();
    }

}
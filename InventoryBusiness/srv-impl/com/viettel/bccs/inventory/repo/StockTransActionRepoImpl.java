package com.viettel.bccs.inventory.repo;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockTransAction.COLUMNS;
import com.viettel.bccs.inventory.model.QStockTransAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockTransActionRepoImpl implements StockTransActionRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransActionRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransAction stockTransAction = QStockTransAction.stockTransAction;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONCODE:
                        expression = DbUtil.createStringExpression(stockTransAction.actionCode, filter);
                        break;
                    case ACTIONSTAFFID:
                        expression = DbUtil.createLongExpression(stockTransAction.actionStaffId, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockTransAction.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockTransAction.createUser, filter);
                        break;
                    case DOCUMENTSTATUS:
                        expression = DbUtil.createStringExpression(stockTransAction.documentStatus, filter);
                        break;
                    case LOGCMDCODE:
                        expression = DbUtil.createStringExpression(stockTransAction.logCmdCode, filter);
                        break;
                    case LOGNOTECODE:
                        expression = DbUtil.createStringExpression(stockTransAction.logNoteCode, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockTransAction.note, filter);
                        break;
                    case REGIONOWNERID:
                        expression = DbUtil.createLongExpression(stockTransAction.regionOwnerId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(stockTransAction.status, filter);
                        break;
                    case STOCKTRANSACTIONID:
                        expression = DbUtil.createLongExpression(stockTransAction.stockTransActionId, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransAction.stockTransId, filter);
                        break;
                    case ACTIONCODESHOP:
                        expression = DbUtil.createStringExpression(stockTransAction.actionCodeShop, filter);
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
    public Long getSequence() throws Exception {
        return DbUtil.getSequence(em, "STOCK_TRANS_ACTION_SEQ");
    }

    @Override
    public StockTransAction findStockTransAction(StockTransActionDTO stockTransActionDTO) {
        List params = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("select * from stock_trans_action a where action_code !='SYSTEM'");
        if (!DataUtil.isNullObject(stockTransActionDTO.getStockTransId())) {
            builder.append(" and a.stock_trans_id = ?");
            params.add(stockTransActionDTO.getStockTransId());
        }
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getCreateUser())) {
            builder.append(" and a.create_user=?");
            params.add(stockTransActionDTO.getCreateUser());
        }
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCode())) {
            builder.append(" and a.action_code=?");
            params.add(stockTransActionDTO.getActionCode());
        }
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getStatus())) {
            builder.append(" and a.status=?");
            params.add(stockTransActionDTO.getStatus());
        }
        if (!DataUtil.isNullObject(stockTransActionDTO.getCreateDatetime())) {
            builder.append(" and a.create_datetime >= trunc(?) ");
            params.add(stockTransActionDTO.getCreateDatetime());
        }
        builder.append(" order by a.create_datetime desc fetch first 1 rows only ");

        Query query = em.createNativeQuery(builder.toString(), StockTransAction.class);
        int index = 0;
        for (Object obj : params) {
            query.setParameter(++index, obj);
        }
        List<StockTransAction> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void insertStockTransActionNative(StockTransActionDTO stockTransActionDTO) throws Exception {
        StringBuilder sqlInsert = new StringBuilder("");
        if (DataUtil.isNullOrZero(stockTransActionDTO.getStockTransActionId())) {
            stockTransActionDTO.setStockTransActionId(getSequence());
        }
        sqlInsert.append(" INSERT INTO stock_trans_action (stock_trans_action_id");
        sqlInsert.append("                                 ,stock_trans_id");
        sqlInsert.append("                                 ,action_code");
        sqlInsert.append("                                 ,status");
        sqlInsert.append("                                 ,create_datetime");
        sqlInsert.append("                                 ,note");
        sqlInsert.append("                                 ,create_user");
        sqlInsert.append("                                 ,action_staff_id");
        if (!DataUtil.isNullObject(stockTransActionDTO.getRegionOwnerId())) {
            sqlInsert.append("                                 ,region_owner_id");
        }
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCodeShop())) {
            sqlInsert.append("                                 ,action_code_shop");
        }
        sqlInsert.append("                                 )");
        sqlInsert.append("   VALUES   (#stock_trans_action_id");
        sqlInsert.append("             ,#stock_trans_id");
        sqlInsert.append("             ,#action_code");
        sqlInsert.append("             ,#status");
        sqlInsert.append("             ,#create_datetime");
        sqlInsert.append("             ,#note");
        sqlInsert.append("             ,#create_user");
        sqlInsert.append("             ,#action_staff_id");
        if (!DataUtil.isNullObject(stockTransActionDTO.getRegionOwnerId())) {
            sqlInsert.append("             ,#region_owner_id");
        }
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCodeShop())) {
            sqlInsert.append("             ,#action_code_shop");
        }
        sqlInsert.append("             )");
        Query query = em.createNativeQuery(sqlInsert.toString());
        query.setParameter("stock_trans_action_id", stockTransActionDTO.getStockTransActionId());
        query.setParameter("stock_trans_id", stockTransActionDTO.getStockTransId());
        query.setParameter("action_code", stockTransActionDTO.getActionCode());
        query.setParameter("status", stockTransActionDTO.getStatus());
        if (DataUtil.isNullObject(stockTransActionDTO.getCreateDatetime())) {
            stockTransActionDTO.setCreateDatetime(new Date());
        }
        query.setParameter("create_datetime", new java.sql.Date(stockTransActionDTO.getCreateDatetime().getTime()));
        query.setParameter("note", stockTransActionDTO.getNote());
        query.setParameter("create_user", stockTransActionDTO.getCreateUser());
        query.setParameter("action_staff_id", stockTransActionDTO.getActionStaffId());
        if (!DataUtil.isNullObject(stockTransActionDTO.getRegionOwnerId())) {
            query.setParameter("region_owner_id", stockTransActionDTO.getRegionOwnerId());
        }
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCodeShop())) {
            query.setParameter("action_code_shop", stockTransActionDTO.getActionCodeShop());
        }
        int i = query.executeUpdate();
        if (i != 1) {
            throw new Exception("mn.stock.branch.insert.stock.trans.action.err");
        }
    }


    @Override
    public List<StockTransAction> getStockTransActionByToOwnerId(Long toOnwerId, Long toOwnerType) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT   sta.*");
        builder.append("    FROM   stock_trans_action sta, stock_trans st");
        builder.append("   WHERE       sta.stock_trans_id = st.stock_trans_id");
        builder.append("           AND sta.status = 2");
        builder.append("           AND st.to_owner_id = #to_owner_id");
        builder.append("           AND st.to_owner_type = #to_owner_type");
        builder.append("           AND st.stock_trans_type = 1");
        builder.append("           AND st.status = 3");
        builder.append(" ORDER BY   sta.create_datetime");

        Query query = em.createNativeQuery(builder.toString(), StockTransAction.class);
        query.setParameter("to_owner_id", toOnwerId);
        query.setParameter("to_owner_type", toOwnerType);

        List<StockTransAction> list = query.getResultList();
        return list;
    }

    @Override
    public void updateActionCodeShop(Connection conn, String actionCodeShop, Long stockTransActionId) throws Exception {
        PreparedStatement ps = null;
        try {
            StringBuilder builder = new StringBuilder(" update stock_trans_action set action_code_shop=? where stock_trans_action_id=? ");
            ps = conn.prepareCall(builder.toString());
            ps.setString(1, actionCodeShop);
            ps.setLong(1, stockTransActionId);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public int unlockUserInfo(Long stockTransActionId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  delete from lock_user_info where trans_id = #transId");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("transId", stockTransActionId);
        return query.executeUpdate();
    }

    public StockTransAction getStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT * FROM   stock_trans_action");
        builder.append("   WHERE       stock_trans_id = #stockTransId AND status " + DbUtil.createInQuery("status", lstStatus));
        builder.append(" ORDER BY   create_datetime desc");

        Query query = em.createNativeQuery(builder.toString(), StockTransAction.class);
        query.setParameter("stockTransId", stockTransId);
        DbUtil.setParamInQuery(query, "status", lstStatus);
        List<StockTransAction> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public int deleteStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  DELETE FROM   stock_trans_action");
        builder.append("   WHERE       stock_trans_id = #stockTransId AND status " + DbUtil.createInQuery("status", lstStatus));

        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stockTransId", stockTransId);
        DbUtil.setParamInQuery(query, "status", lstStatus);
        return query.executeUpdate();
    }

    public List<StockDeliveringBillDetailDTO> getStockDeliveringResult(Date startTime, Date endTime) throws LogicException, Exception {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append("SELECT   st.stock_trans_id stocktransid,");
        strQuery.append("         (SELECT   stm.action_code");
        strQuery.append("            FROM   stock_trans_action stm");
        strQuery.append("           WHERE       stm.status =1 ");
        strQuery.append("             AND stm.stock_trans_id = st.stock_trans_id");
        strQuery.append("             AND ROWNUM < 2)");
        strQuery.append("                   deliverycommand,");
        strQuery.append("          (SELECT  stm.action_code");
        strQuery.append("             FROM  stock_trans_action stm");
        strQuery.append("            WHERE  stm.status = 2");
        strQuery.append("              AND stm.stock_trans_id = st.stock_trans_id");
        strQuery.append("              AND ROWNUM < 2)");
        strQuery.append("                   deliverynote,");
        strQuery.append("       (SELECT   TO_CHAR (stm.create_datetime, 'dd/MM/yyyy HH24:mi:ss')");
        strQuery.append("          FROM   ( SELECT   * FROM   stock_trans_action");
        strQuery.append("                             WHERE status = 3 ");
        strQuery.append("                               AND stock_trans_id = st.stock_trans_id ) stm");
        strQuery.append("          WHERE   ROWNUM < 2)");
        strQuery.append("                   deliverydate,");
        strQuery.append("       (CASE st.from_owner_type");
        strQuery.append("           WHEN 1 THEN");
        strQuery.append("           (SELECT sh.shop_code");
        strQuery.append("               FROM shop sh");
        strQuery.append("               WHERE sh.shop_id = st.from_owner_id)");
        strQuery.append("           WHEN 2 THEN ");
        strQuery.append("           (SELECT sf.staff_code");
        strQuery.append("               FROM staff sf");
        strQuery.append("               WHERE sf.staff_id = st.from_owner_id)");
        strQuery.append("           WHEN 4 THEN ");
        strQuery.append("           (SELECT TO_CHAR(pr.partner_code)");
        strQuery.append("               FROM partner pr");
        strQuery.append("               WHERE pr.partner_id = st.from_owner_id)");
        strQuery.append("         END)");
        strQuery.append("                   stockcode,");
        strQuery.append("       (CASE st.from_owner_type");
        strQuery.append("           WHEN 1 THEN ");
        strQuery.append("           (SELECT sh.name");
        strQuery.append("               FROM shop sh");
        strQuery.append("               WHERE sh.shop_id = st.from_owner_id)");
        strQuery.append("           WHEN 2 THEN ");
        strQuery.append("           (SELECT sf.name");
        strQuery.append("                FROM staff sf");
        strQuery.append("               WHERE sf.staff_id = st.from_owner_id)");
        strQuery.append("           WHEN 4 THEN ");
        strQuery.append("       (SELECT pr.partner_name");
        strQuery.append("          FROM partner pr");
        strQuery.append("          WHERE pr.partner_id = st.from_owner_id)");
        strQuery.append("        END)");
        strQuery.append("               stockname,");
        strQuery.append("       (CASE st.to_owner_type");
        strQuery.append("           WHEN 1 THEN ");
        strQuery.append("        (SELECT sh.shop_code");
        strQuery.append("               FROM shop sh");
        strQuery.append("               WHERE sh.shop_id = st.to_owner_id)");
        strQuery.append("           WHEN 2 THEN ");
        strQuery.append("        (SELECT sf.staff_code");
        strQuery.append("               FROM staff sf");
        strQuery.append("               WHERE sf.staff_id = st.to_owner_id)");
        strQuery.append("           WHEN 4 THEN ");
        strQuery.append("         (SELECT TO_CHAR(pr.partner_code)");
        strQuery.append("                FROM partner pr");
        strQuery.append("                WHERE pr.partner_id = st.to_owner_id)");
        strQuery.append("       END)");
        strQuery.append("               receiptcode,");
        strQuery.append("      (CASE st.to_owner_type");
        strQuery.append("           WHEN 1 THEN ");
        strQuery.append("         (SELECT sh.name");
        strQuery.append("                 FROM shop sh");
        strQuery.append("                 WHERE sh.shop_id = st.to_owner_id)");
        strQuery.append("           WHEN 2 THEN ");
        strQuery.append("         (SELECT sf.name");
        strQuery.append("                 FROM staff sf");
        strQuery.append("                WHERE sf.staff_id = st.to_owner_id)");
        strQuery.append("           WHEN 4 THEN ");
        strQuery.append("        (SELECT pr.partner_name");
        strQuery.append("           FROM partner pr");
        strQuery.append("           WHERE pr.partner_id = st.to_owner_id)");
        strQuery.append(        "END)");
        strQuery.append("               receiptname,");
        strQuery.append("        (SELECT  (SELECT ttns_code");
        strQuery.append("                    FROM staff");
        strQuery.append("                   WHERE staff_id = a.action_staff_id)");
        strQuery.append("           FROM stock_trans_action a");
        strQuery.append("          WHERE a.stock_trans_id = st.stock_trans_id AND a.status in (1,2) order by a.status fetch first 1 rows only)");
        strQuery.append("               staff_export,");
        strQuery.append("        (SELECT  (SELECT ttns_code");
        strQuery.append("                    FROM staff");
        strQuery.append("                   WHERE staff_id = a.action_staff_id)");
        strQuery.append("           FROM stock_trans_action a");
        strQuery.append("          WHERE a.stock_trans_id = st.stock_trans_id AND a.status in (4,5) order by a.status fetch first 1 rows only)");
        strQuery.append("               staff_import,");
        strQuery.append("       st.transport transport_type,");
        strQuery.append("       NVL(transport_source, 1) transport_source  ");
        strQuery.append(" FROM stock_trans st, stock_trans_action sta  ");
        strQuery.append(" WHERE 1 = 1");
        strQuery.append("  AND st.stock_trans_id = sta.stock_trans_id");
        strQuery.append("  AND st.create_datetime >= #startTime - 90");
        strQuery.append("  AND st.create_datetime <= #endTime");
        strQuery.append("  AND sta.create_datetime >= #startTime");
        strQuery.append("  AND sta.create_datetime <= #endTime");
        strQuery.append("  AND st.transport >= 1");
        strQuery.append("  AND sta.status IN (3, 4, 6)");
        strQuery.append("  AND NVL (st.is_auto_gen, 0) <> 1 ");
        strQuery.append(" ORDER BY sta.create_datetime");

        List<StockDeliveringBillDetailDTO> lstStockDeliveringBill = Lists.newArrayList();
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                StockDeliveringBillDetailDTO stockDeliveringBillDetailDTO = new StockDeliveringBillDetailDTO();
                stockDeliveringBillDetailDTO.setStockTransId(DataUtil.safeToLong(object[index++]));
                stockDeliveringBillDetailDTO.setDeliveryCommand(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setDeliveryNote(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setDeliveryDate(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setStockCode(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setStockName(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setReceiptCode(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setReceiptName(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setStaffExport(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setStaffImport(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setTransportType(DataUtil.safeToString(object[index++]));
                stockDeliveringBillDetailDTO.setTransportSource(DataUtil.safeToString(object[index++]));
                lstStockDeliveringBill.add(stockDeliveringBillDetailDTO);
            }
        }
        return lstStockDeliveringBill;
    }

    @Override
    public List<VActionGoodsStatisticDTO> getListVActionStatistic(String actionCode, Long ownerId, Long ownerType, Long ownerLoginId,
                                                                  Long ownerLoginType, Date fromDate, Date toDate, String transType, String typeOrderNote) throws LogicException, Exception {

        StringBuilder builder = new StringBuilder(" ");
        String statusAction = "";
        if (!DataUtil.isNullOrEmpty(actionCode)) {
            builder.append("	SELECT status FROM stock_trans_action where  action_code = #actionCode ");
            Query query1 = em.createNativeQuery(builder.toString());
            query1.setParameter("actionCode", actionCode.trim());
            List<String> lsResult = query1.getResultList();
            if (!DataUtil.isNullOrEmpty(lsResult)) {
                statusAction = lsResult.get(0);
                if ("1".equals(statusAction)) {
                    statusAction = "1,2";
                } else if ("4".equals(statusAction)) {
                    statusAction = "4,5";
                }
            }
        }

        builder = new StringBuilder(" ");
        builder.append("	SELECT action_code,prod_offer_code,prod_offer_name,to_owner_code,to_owner_name,quantity,weight,volume, from_owner_id, to_owner_id, status,status_name, action_status  ");
        builder.append("	  FROM v_action_goods_statistic sts ");
        builder.append("  WHERE 1=1 ");
        builder.append("  AND action_datetime >= trunc(#from_date) ");
        builder.append("  AND action_datetime < trunc(#to_date) + 1  ");
        builder.append("  AND create_datetime >= trunc(#from_date) - 60 ");
        builder.append("  AND create_datetime < trunc(#to_date) + 1 ");

        if (!DataUtil.isNullOrEmpty(actionCode)) {
            if (!DataUtil.isNullOrEmpty(statusAction)) {
                builder.append("		   AND action_status IN(" + statusAction + ") ");
                builder.append("		   AND exists (select status from stock_trans_action sta1 where sta1.stock_trans_id = sts.stock_trans_id and sta1.action_code = #action_code) ");
            } else {
                builder.append("		   AND action_code = #action_code ");
            }
        }

        if (ownerId != null) {
            builder.append("		   AND ( from_owner_id = #owner_id  OR  to_owner_id = #owner_id )");
        }

        if (ownerType != null) {
            builder.append("		   AND (from_owner_type = #owner_type OR to_owner_type = #owner_type) ");
        }

        if (DataUtil.isNullOrEmpty(transType) && DataUtil.isNullOrEmpty(typeOrderNote)) {
            builder.append("		   AND action_status IN (1,2,4,5) ");
        } else if (!DataUtil.isNullOrEmpty(transType) && DataUtil.isNullOrEmpty(typeOrderNote)) {
            if (Const.STOCK_TRANS_TYPE.EXPORT.equals(transType)) {
                builder.append("		   AND action_status IN (1,2) ");
            } else if (Const.STOCK_TRANS_TYPE.IMPORT.equals(transType)) {
                builder.append("		   AND action_status IN (4,5) ");
            }
        } else if (DataUtil.isNullOrEmpty(transType) && !DataUtil.isNullOrEmpty(typeOrderNote)) {
            if (Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(typeOrderNote)) {
                builder.append("		   AND action_status IN (1,4) ");
            } else if (Const.STOCK_TRANS_STATUS.EXPORT_NOTE.equals(typeOrderNote)) {
                builder.append("		   AND action_status IN (2,5) ");
            }
        } else if (!DataUtil.isNullOrEmpty(transType) && !DataUtil.isNullOrEmpty(typeOrderNote)) {
            if (Const.STOCK_TRANS_TYPE.EXPORT.equals(transType) && Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(typeOrderNote)) {
                builder.append("		   AND action_status = 1 ");
            } else if (Const.STOCK_TRANS_TYPE.EXPORT.equals(transType) && Const.STOCK_TRANS_STATUS.EXPORT_NOTE.equals(typeOrderNote)) {
                builder.append("		   AND action_status = 2 ");
            } else if (Const.STOCK_TRANS_TYPE.IMPORT.equals(transType) && Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(typeOrderNote)) {
                builder.append("		   AND action_status = 4 ");
            } else if (Const.STOCK_TRANS_TYPE.IMPORT.equals(transType) && Const.STOCK_TRANS_STATUS.EXPORT_NOTE.equals(typeOrderNote)) {
                builder.append("		   AND action_status = 5 ");
            }
        }
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("from_date", fromDate);
        query.setParameter("to_date", toDate);

        if (!DataUtil.isNullOrEmpty(actionCode)) {
            query.setParameter("action_code", actionCode.trim());
        }
        if (ownerId != null) {
            query.setParameter("owner_id", ownerId);
        }
        if (ownerType != null) {
            query.setParameter("owner_type", ownerType);
        }

        List<Object[]> results = query.getResultList();

        List<VActionGoodsStatisticDTO> lsData = Lists.newArrayList();

        if (!DataUtil.isNullOrEmpty(results)) {
            int index;
            VActionGoodsStatisticDTO action;
            for (Object[] result : results) {
                index = 0;
                action = new VActionGoodsStatisticDTO();
                action.setActionCode(DataUtil.safeToString(result[index++]));
                action.setProdOfferCode(DataUtil.safeToString(result[index++]));
                action.setProdOfferName(DataUtil.safeToString(result[index++]));
                action.setToOwnerCode(DataUtil.safeToString(result[index++]));
                action.setToOwnerName(DataUtil.safeToString(result[index++]));
                action.setQuantity(DataUtil.safeToLong(result[index++]));
                action.setWeigth(DataUtil.safeToDouble(result[index++]));
                action.setVolume(DataUtil.safeToDouble(result[index++]));
                action.setFromOwnerId(DataUtil.safeToLong(result[index++]));
                action.setToOwnerId(DataUtil.safeToLong(result[index++]));
                action.setStatus(DataUtil.safeToString(result[index++]));
                action.setStatusName(DataUtil.safeToString(result[index++]));
                action.setActionStatus(DataUtil.safeToString(result[index]));
                if (ownerLoginId.equals(action.getFromOwnerId()) && (Const.STOCK_TRANS_STATUS.EXPORT_ORDER.equals(action.getActionStatus()) || Const.STOCK_TRANS_STATUS.EXPORT_NOTE.equals(action.getActionStatus()))
                        || ownerLoginId.equals(action.getToOwnerId()) && (Const.STOCK_TRANS_STATUS.IMPORT_ORDER.equals(action.getActionStatus()) || Const.STOCK_TRANS_STATUS.IMPORT_NOTE.equals(action.getActionStatus()))) {

                    lsData.add(action);
                }
            }
        }
        return lsData;
    }

    public int deleteStockTransAction(Long stockTransActionId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  DELETE FROM   stock_trans_action");
        builder.append("   WHERE       stock_trans_action_id = #stockTransActionId ");

        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stockTransActionId", stockTransActionId);
        return query.executeUpdate();
    }

    @Override
    public List<String> findListStockTransAction(VStockTransDTO vStockTransDTO) {
        List params = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");

        try {

            builder.append("select a.* from stock_trans_action a ,stock_trans_action b");
            builder.append(" where a.stock_trans_id= b.stock_trans_id");
            builder.append("   and b.status in (1,4) ");
            builder.append("   and a.status in (2,5) ");

            if (!DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode())) {
                builder.append("  and b.action_code = ? ");
                params.add(vStockTransDTO.getActionCode());
            }

            if (DataUtil.safeEqual(vStockTransDTO.getActionType(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
                builder.append("   and b.status = ? ");
                builder.append("   and a.status = ? ");
                params.add(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                params.add(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            }

            if (DataUtil.safeEqual(vStockTransDTO.getActionType(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)) {
                builder.append("   and b.status = ? ");
                builder.append("   and a.status = ? ");
                params.add(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
                params.add(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            }

            if (!DataUtil.isNullObject(vStockTransDTO.getStartDate())) {
                builder.append("   and b.create_datetime >= trunc (?) - 30");//Ngay lap lenh lui 30 ngay
                builder.append("   and a.create_datetime >= trunc (?)");
                params.add(vStockTransDTO.getStartDate());
                params.add(vStockTransDTO.getStartDate());
            }
            if (!DataUtil.isNullObject(vStockTransDTO.getEndDate())) {
                builder.append("   and b.create_datetime <= trunc (?) + 1");
                builder.append("   and a.create_datetime <= trunc (?) + 1");
                params.add(vStockTransDTO.getEndDate());
                params.add(vStockTransDTO.getEndDate());
            }

            Query query = em.createNativeQuery(builder.toString(), StockTransAction.class);
            int index = 0;
            for (Object obj : params) {
                query.setParameter(++index, obj);
            }
            List<StockTransAction> list = query.getResultList();

            if (!DataUtil.isNullOrEmpty(list)) {
                List<String> lstResult = new ArrayList<>();
                list.stream().forEach(x -> lstResult.add(x.getActionCode()));
                return lstResult;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
}
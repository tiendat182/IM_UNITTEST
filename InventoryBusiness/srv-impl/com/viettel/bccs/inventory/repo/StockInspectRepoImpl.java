package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockInspect;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockInspect.COLUMNS;
import com.viettel.bccs.inventory.model.QStockInspect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.dto.BaseMessage;
import org.apache.log4j.Logger;
import org.eclipse.persistence.config.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockInspectRepoImpl implements StockInspectRepoCustom {

    public static final Logger logger = Logger.getLogger(StockInspectRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockInspect stockInspect = QStockInspect.stockInspect;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APPROVEDATE:
                        expression = DbUtil.createDateExpression(stockInspect.approveDate, filter);
                        break;
                    case APPROVENOTE:
                        expression = DbUtil.createStringExpression(stockInspect.approveNote, filter);
                        break;
                    case APPROVESTATUS:
                        expression = DbUtil.createLongExpression(stockInspect.approveStatus, filter);
                        break;
                    case APPROVEUSER:
                        expression = DbUtil.createStringExpression(stockInspect.approveUser, filter);
                        break;
                    case APPROVEUSERID:
                        expression = DbUtil.createLongExpression(stockInspect.approveUserId, filter);
                        break;
                    case APPROVEUSERNAME:
                        expression = DbUtil.createStringExpression(stockInspect.approveUserName, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockInspect.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockInspect.createUser, filter);
                        break;
                    case CREATEUSERID:
                        expression = DbUtil.createLongExpression(stockInspect.createUserId, filter);
                        break;
                    case CREATEUSERNAME:
                        expression = DbUtil.createStringExpression(stockInspect.createUserName, filter);
                        break;
                    case ISFINISHED:
                        expression = DbUtil.createLongExpression(stockInspect.isFinished, filter);
                        break;
                    case ISFINISHEDALL:
                        expression = DbUtil.createLongExpression(stockInspect.isFinishedAll, filter);
                        break;
                    case ISSCAN:
                        expression = DbUtil.createLongExpression(stockInspect.isScan, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockInspect.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockInspect.ownerType, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockInspect.prodOfferId, filter);
                        break;
                    case PRODOFFERTYPEID:
                        expression = DbUtil.createLongExpression(stockInspect.prodOfferTypeId, filter);
                        break;
                    case QUANTITYREAL:
                        expression = DbUtil.createLongExpression(stockInspect.quantityReal, filter);
                        break;
                    case QUANTITYSYS:
                        expression = DbUtil.createLongExpression(stockInspect.quantitySys, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockInspect.stateId, filter);
                        break;
                    case STOCKINSPECTID:
                        expression = DbUtil.createLongExpression(stockInspect.stockInspectId, filter);
                        break;
                    case UPDATEDATE:
                        expression = DbUtil.createDateExpression(stockInspect.updateDate, filter);
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
    public List<ApproveStockInspectDTO> searchApproveInspect(Long approveUserId, Long ownerId, Long ownerType, Long approveStatus, Long prodOfferTypeId, String code, Date fromDate, Date toDate) throws Exception {
        List<ApproveStockInspectDTO> result = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT si.stock_inspect_id AS stockInspectId,");
        strQuery.append(" pr.prod_offer_id,");
        strQuery.append(" pr.product_offer_type_id, ");
        strQuery.append(" pr.code AS productCode, ");
        strQuery.append("                    pr.name AS productName, ");
        strQuery.append(" (SELECT owner_code || ' - ' || owner_name");
        strQuery.append(" FROM v_shop_staff ");
        strQuery.append(" WHERE owner_id = si.owner_id AND owner_type = si.owner_type) AS ownerName,");
        strQuery.append(" to_char(si.update_date, 'dd/MM/yyyy HH24:mi:ss') AS issueDateTime,");
        strQuery.append(" si.create_user || '-' || si.create_user_name AS requestUser,");
        strQuery.append(" si.approve_user || '-' || si.approve_user_name AS approveUser, ");
        strQuery.append(" (SELECT   sv.name FROM   option_set s, option_set_value sv ");
        strQuery.append(" WHERE       s.id = sv.option_set_id");
        strQuery.append(" AND s.status = 1 AND sv.status = 1  AND s.code = 'STOCK_INSPECT_STATUS' ");
        strQuery.append(" AND sv.VALUE = to_char(si.approve_status)) as approveStatusString,");
        strQuery.append(" nvl(si.quantity_sys, 0) AS quantitySystem, ");
        strQuery.append(" nvl(si.quantity_real, 0) AS quantityReal,");
        strQuery.append(" (nvl(si.quantity_sys, 0) - nvl(si.quantity_real, 0)) AS diff,");
        strQuery.append(" si.owner_id ownerId,");
        strQuery.append(" si.owner_type ownerType,");
        strQuery.append(" si.approve_user_id AS approveUserId, ");
        strQuery.append(" si.create_user_id userCreateId, ");
        strQuery.append(" si.approve_status approveStatus, ");
        strQuery.append(" si.state_id stateId, ");
        strQuery.append(" (SELECT   sv.name FROM   option_set s, option_set_value sv ");
        strQuery.append(" WHERE       s.id = sv.option_set_id");
        strQuery.append(" AND s.status = 1 AND sv.status = 1  AND s.code = 'GOOD_STATE' ");
        strQuery.append(" AND sv.VALUE = to_char(si.state_id)) as stateName,");
        strQuery.append(" si.approve_note as approveNote,");
        strQuery.append(" si.quantity_finance ");
        strQuery.append("              FROM stock_inspect si, product_offering pr");
        strQuery.append("               WHERE si.prod_offer_id = pr.prod_offer_id ");
        strQuery.append("                           AND (si.is_finished = 1 OR (si.is_finished = 0 AND ");
        strQuery.append("              si.approve_status = 2))");
        strQuery.append("              AND si.create_date >= #from_date ");
        strQuery.append("              AND si.create_date <= #to_date + 1");
        if (!DataUtil.isNullOrZero(approveUserId)) {
            strQuery.append("              AND si.approve_user_id =#approveUserId");
        }
        if (!DataUtil.isNullOrZero(ownerId)) {
            strQuery.append("             AND si.owner_id =#ownerId");
        }
        if (!DataUtil.isNullOrZero(ownerType)) {
            strQuery.append("             AND si.owner_type =#ownerType");
        }
        if (!DataUtil.isNullObject(approveStatus)) {
            strQuery.append("              AND si.approve_status =#approveStatus");
        }
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            strQuery.append("              AND si.prod_offer_type_id =#prodOfferTypeId ");
        }
        if (!DataUtil.isNullOrEmpty(code)) {
            strQuery.append("              AND pr.code =#code");
        }
        strQuery.append("              and rownum <= #rownum");
        strQuery.append("              order by pr.code");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("from_date", fromDate);
        query.setParameter("to_date", toDate);
        if (!DataUtil.isNullOrZero(approveUserId)) {
            query.setParameter("approveUserId", approveUserId);
        }
        if (!DataUtil.isNullOrZero(ownerId)) {
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullOrZero(ownerType)) {
            query.setParameter("ownerType", ownerType);
        }
        if (!DataUtil.isNullObject(approveStatus)) {
            query.setParameter("approveStatus", approveStatus);
        }
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            query.setParameter("prodOfferTypeId", prodOfferTypeId);
        }
        if (!DataUtil.isNullOrEmpty(code)) {
            query.setParameter("code", code);
        }
        query.setParameter("rownum", Const.NUMBER_SEARCH.LIMIT_ALL);

        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                ApproveStockInspectDTO approveStockInspectDTO = new ApproveStockInspectDTO();
                approveStockInspectDTO.setStockInspectId(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setProdOfferId(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setProductOfferTypeId(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setProductCode(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setProductName(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setOwnerName(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setIssueDateTime(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setRequestUser(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setApproveUser(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setApproveStatusString(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setQuantitySystem(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setQuantityReal(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setDiff(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setOwnerId(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setOwnerType(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setApproveUserId(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setUserCreateId(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setApproveStatus(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setStateId(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setStateName(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setApproveNote(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setQuantityFinance(approveStockInspectDTO.getQuantitySystem());
                if (!DataUtil.safeEqual(approveStockInspectDTO.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.NOT_HAVE_APPROVE)) {
                    approveStockInspectDTO.setQuantityFinance(DataUtil.safeToLong(object[index]));
                }
                result.add(approveStockInspectDTO);
            }
            return result;
        }
        return null;
    }

    public boolean checkExistSpecNotFinish(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws Exception {

        StringBuilder builder = new StringBuilder("");

        builder.append(" SELECT   1");
        builder.append("   FROM   stock_inspect");
        builder.append("  WHERE       owner_id = #ownerId");
        builder.append("          AND owner_type = #ownerType");
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            builder.append("      AND prod_offer_type_id = #productOfferTypeId");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("      AND state_id = #stateId");
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("      AND prod_offer_id = #prodOfferId");
        }
        builder.append("          AND is_finished <> 1");
        builder.append("          AND create_date >= #fromDate");
        builder.append("          AND create_date <= #toDate + 1");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("statusProductOffer", Const.STATUS.ACTIVE);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            query.setParameter("productOfferTypeId", productOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        List list = query.getResultList();
        return list != null && list.size() > 0;
    }

    @Override
    public List<Long> getStockTotalMinusStockInspect(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate, boolean isCheckStockTotal) throws Exception {
        StringBuilder builder = new StringBuilder("");
        List<Long> lst;
        builder.append(" SELECT   pr.prod_offer_id, state_id ");
        if (isCheckStockTotal) {
            builder.append("   FROM   stock_total st, product_offering pr ");
        } else {
            builder.append("   FROM   STOCK_DAILY_REMAIN_GENERAL st, product_offering pr ");
        }
        builder.append("  WHERE       st.prod_offer_id = pr.prod_offer_id");
        if (isCheckStockTotal) {
            builder.append("          AND st.status = #status");
        } else {
            builder.append("          AND st.remain_date = last_day(add_months(trunc(sysdate), -1))");
        }
        builder.append("          AND pr.status = #statusProductOffer");
        builder.append("          AND st.owner_id = #ownerId");
        builder.append("          AND st.owner_type = #ownerType");
        builder.append("          AND st.current_quantity > 0");
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            builder.append("          AND pr.product_offer_type_id = #productOfferTypeId");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("          AND st.state_id = #stateId");
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("          AND pr.prod_offer_id = #prodOfferId");
        }
        builder.append(" MINUS ");
        builder.append(" SELECT   prod_offer_id, state_id");
        builder.append("   FROM   stock_inspect");
        builder.append("  WHERE       owner_id = #ownerId");
        builder.append("          AND owner_type = #ownerType");
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            builder.append("          AND prod_offer_type_id = #productOfferTypeId");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("          AND state_id = #stateId");
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("          AND prod_offer_id = #prodOfferId");
        }
        builder.append("          AND is_finished = 1");
        builder.append("          AND create_date >= #fromDate");
        builder.append("          AND create_date <= #toDate + 1");

        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("statusProductOffer", Const.STATUS.ACTIVE);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            query.setParameter("productOfferTypeId", productOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        lst = query.getResultList();
        return lst;
    }

    @Override
    public List<StockInspect> getStockInspect(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId,
                                              Long prodOfferId, Date fromDate, Date toDate, Long isFinsh, Long approveStatus) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append(" SELECT * ");
        builder.append("    FROM stock_inspect ");
        builder.append("  WHERE     owner_id = #ownerId ");
        builder.append("        AND owner_type = #ownerType");
        builder.append("        AND prod_offer_type_id = #prodOfferTypeId");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("        AND prod_offer_id = #prodOfferId");
        }
        builder.append("        AND state_id = #stateId ");
        if (!DataUtil.isNullObject(isFinsh)) {
            builder.append("        AND is_finished = #isFinished ");
        }
        if (!DataUtil.isNullObject(approveStatus)) {
            builder.append("        AND approve_status = #approveStatus ");
        }
        builder.append("        AND create_date >= #fromDate ");
        builder.append("        AND create_date <= #toDate + 1");
        Query query = em.createNativeQuery(builder.toString(), StockInspect.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("prodOfferTypeId", productOfferTypeId);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        query.setParameter("stateId", stateId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (!DataUtil.isNullObject(isFinsh)) {
            query.setParameter("isFinished", isFinsh);
        }
        if (!DataUtil.isNullObject(approveStatus)) {
            query.setParameter("approveStatus", approveStatus);
        }
        List<StockInspect> lst = query.getResultList();
        return lst;
    }

    @Override
    public List<StockInspectCheckDTO> getStockInspectForNoSerial(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws Exception {
        StringBuilder builder = new StringBuilder("");
        List<StockInspectCheckDTO> result = Lists.newArrayList();
        builder.append("  SELECT  pr.prod_offer_id, pr.product_offer_type_id, ");
        builder.append("           pr.code,pr.name,");
        builder.append("           NVL(st.current_quantity,0) AS current_quantity, ");
        builder.append("           NVL(st.available_quantity,0) AS available_quantity, ");
        builder.append("           NVL((SELECT quantity_real ");
        builder.append("             FROM stock_inspect ");
        builder.append("            WHERE     owner_id = #ownerId");
        builder.append("                  AND owner_type = #ownerType ");
        builder.append("                  AND prod_offer_type_id = #prodOfferTypeId");
        builder.append("                  AND state_id = #stateId");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("              AND prod_offer_id = #prodOfferId");
        }
        builder.append("                  AND create_date >= #fromDate ");
        builder.append("                  AND create_date < #toDate +1");
//        builder.append("                  AND is_finished = 0 ");
        builder.append("                  AND prod_offer_id = pr.prod_offer_id),0) AS quantity_Stock_Check,");
        builder.append("             (SELECT   sv.name");
        builder.append("              FROM   option_set s, option_set_value sv");
        builder.append("             WHERE       s.id = sv.option_set_id");
        builder.append("                     AND s.status = 1");
        builder.append("                     AND sv.status = 1");
        builder.append("                     AND s.code = 'GOOD_STATE'");
        builder.append("                     AND trim (sv.VALUE)= to_char(#stateId)) AS state_Name ");
        builder.append("    FROM   product_offering pr, stock_total st ");
        builder.append("   WHERE   1 = 1 ");
        builder.append("           AND st.owner_id(+) = #ownerId ");
        builder.append("           AND st.owner_type(+) = #ownerType");
        builder.append("           AND pr.product_offer_type_id = #prodOfferTypeId ");
        builder.append("           AND pr.status = 1 ");
        builder.append("           AND pr.prod_offer_id = st.prod_offer_id(+) ");
        builder.append("           AND st.state_id(+) = #stateId");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("       AND pr.prod_offer_id = #prodOfferId");
        }
//        builder.append("          AND NOT EXISTS ");
//        builder.append("                (SELECT 1 ");
//        builder.append("                   FROM stock_inspect ");
//        builder.append("                  WHERE     owner_id = #ownerId");
//        builder.append("                        AND owner_type = #ownerType");
//        builder.append("                        AND prod_offer_type_id = #prodOfferTypeId");
//        builder.append("                        AND state_id = #stateId");
//        if (!DataUtil.isNullOrZero(prodOfferId)) {
//            builder.append("                    AND prod_offer_id = #prodOfferId");
//        }
//        builder.append("                        AND create_date >= #fromDate ");
//        builder.append("                        AND create_date < #toDate");
//        builder.append("                        AND is_finished = 1 ");
//        builder.append("                        AND prod_offer_id = pr.prod_offer_id) ");
//        builder.append("                        AND prod_offer_id = pr.prod_offer_id) AND rownum <= :rownum");
        builder.append("           ORDER BY current_quantity DESC, nlssort(name, 'nls_sort=Vietnamese')  ASC");
        Query query = em.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 300);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("prodOfferTypeId", productOfferTypeId);
        query.setParameter("stateId", stateId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
//        query.setParameter("rownum", Const.NUMBER_SEARCH.LIMIT_ALL);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockInspectCheckDTO stockInspectCheckDTO = new StockInspectCheckDTO();
                stockInspectCheckDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setProductCode(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setProductName(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setCurrentQuantity(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setAvailableQuantity(DataUtil.safeToLong(ob[index++]));
                Long quantityStockCheck = DataUtil.safeToLong(ob[index++]);
                stockInspectCheckDTO.setQuantityStockCheck(quantityStockCheck);
                stockInspectCheckDTO.setStateName(DataUtil.safeToString(ob[index]));
                stockInspectCheckDTO.setQuantity(quantityStockCheck);
                stockInspectCheckDTO.setOwnerType(ownerType);
                stockInspectCheckDTO.setOwnerId(ownerId);
                stockInspectCheckDTO.setStateId(stateId);
                result.add(stockInspectCheckDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockInspectCheckDTO> getStockInspectForQuantity(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws Exception {
        StringBuilder builder = new StringBuilder("");
        List<StockInspectCheckDTO> result = Lists.newArrayList();
        builder.append("  SELECT  pr.prod_offer_id, ");
        builder.append("           pr.product_offer_type_id, ");
        builder.append("           pr.code,");
        builder.append("           pr.name,");
        builder.append("           NVL(st.current_quantity,0) AS current_quantity, ");//so luong ton HT
        builder.append("		   NVL((SELECT quantity_real ");
        builder.append("		          FROM bccs_im.stock_inspect si ");
        builder.append("		         WHERE si.create_date >= #fromDate ");
        builder.append("			       AND si.create_date <= #toDate + 1 ");
        builder.append("			       AND owner_type = st.owner_type ");
        builder.append("			       AND owner_id = st.owner_id ");
        builder.append("			       AND prod_offer_type_id = st.prod_offer_type_id ");
        builder.append("			       AND prod_offer_id = st.prod_offer_id ");
        builder.append("			       AND state_id = st.state_id), NVL(st.current_quantity,0)) ");
        builder.append("		        quantity_real, ");//so luong kiem ke
        builder.append("	        NVL((SELECT quantity_poor ");
        builder.append("		           FROM bccs_im.stock_inspect si ");
        builder.append("		          WHERE si.create_date >= #fromDate ");
        builder.append("			        AND si.create_date <= #toDate + 1 ");
        builder.append("			        AND owner_type = st.owner_type ");
        builder.append("			        AND owner_id = st.owner_id ");
        builder.append("			        AND prod_offer_type_id = st.prod_offer_type_id ");
        builder.append("			        AND prod_offer_id = st.prod_offer_id ");
        builder.append("			        AND state_id = st.state_id), 0) ");
        builder.append("		        quantity_poor, ");//so luong kem pham
        builder.append("	         (SELECT note ");
        builder.append("		        FROM bccs_im.stock_inspect si ");
        builder.append("		       WHERE si.create_date >= #fromDate ");
        builder.append("			     AND si.create_date <= #toDate + 1 ");
        builder.append("			     AND owner_type = st.owner_type ");
        builder.append("			     AND owner_id = st.owner_id ");
        builder.append("			     AND prod_offer_type_id = st.prod_offer_type_id ");
        builder.append("			     AND prod_offer_id = st.prod_offer_id ");
        builder.append("			     AND state_id = st.state_id) ");
        builder.append("		        note, ");//ghi chu
        builder.append("             (SELECT   sv.name");
        builder.append("              FROM   option_set s, option_set_value sv");
        builder.append("             WHERE       s.id = sv.option_set_id");
        builder.append("                     AND s.status = 1");
        builder.append("                     AND sv.status = 1");
        builder.append("                     AND s.code = 'GOODS_STATE'");
        builder.append("                     AND trim (sv.VALUE)= to_char(st.state_id)) AS state_Name, ");
        builder.append("                     st.state_id ");
        builder.append("    FROM   product_offering pr, STOCK_DAILY_REMAIN_GENERAL st ");
        builder.append("   WHERE   1 = 1 ");
        builder.append("           AND st.remain_date = last_day(add_months(trunc(sysdate), -1)) ");
        builder.append("           AND st.owner_id = #ownerId ");
        builder.append("           AND st.owner_type = #ownerType");
        builder.append("           AND pr.product_offer_type_id = #prodOfferTypeId ");
        builder.append("           AND pr.status = 1 ");
        builder.append("           AND st.current_quantity > 0");
        builder.append("           AND pr.prod_offer_id = st.prod_offer_id ");
        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("       AND st.state_id = #stateId");
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("       AND pr.prod_offer_id = #prodOfferId");
        }
//        builder.append("          AND NOT EXISTS ");
//        builder.append("                (SELECT 1 ");
//        builder.append("                   FROM stock_inspect ");
//        builder.append("                  WHERE     owner_id = #ownerId");
//        builder.append("                    AND owner_type = #ownerType");
//        builder.append("                    AND prod_offer_type_id = #prodOfferTypeId");
//        if (!DataUtil.isNullOrZero(stateId)) {
//            builder.append("                AND state_id = #stateId");
//        }
//        if (!DataUtil.isNullOrZero(prodOfferId)) {
//            builder.append("                AND prod_offer_id = #prodOfferId");
//        }
//        builder.append("                    AND create_date >= #fromDate ");
//        builder.append("                    AND create_date <= #toDate + 1");
//        builder.append("                    AND is_finished = 1 ");
//        builder.append("                    AND prod_offer_id = pr.prod_offer_id) ");
//        builder.append("                        AND prod_offer_id = pr.prod_offer_id) AND rownum <= :rownum");
        builder.append("           ORDER BY current_quantity DESC, nlssort(name, 'nls_sort=Vietnamese')  ASC");
        Query query = em.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 300);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("prodOfferTypeId", productOfferTypeId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
//        query.setParameter("rownum", Const.NUMBER_SEARCH.LIMIT_ALL);
        //danh sach trang thai hang hong
        List<Long> lsDamageStateId = Lists.newArrayList(Const.GOODS_STATE.BROKEN, Const.GOODS_STATE.DOA, Const.GOODS_STATE.BROKEN_15_DAY_CUSTOMER, Const.GOODS_STATE.BROKEN_WARRANTY);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            convertStockInspect(ownerType, ownerId, stateId, result, lsDamageStateId, queryResult);
        }

        builder = new StringBuilder("");
        builder.append("	SELECT   a.prod_offer_id, ");
        builder.append("			 a.prod_offer_type_id, ");
        builder.append("			 (SELECT   code FROM   product_offering WHERE   prod_offer_id = a.prod_offer_id) code, ");
        builder.append("			 (SELECT   name FROM   product_offering WHERE   prod_offer_id = a.prod_offer_id) name, ");
        builder.append("			 0 current_quantity, ");
        builder.append("			 (SELECT   quantity_real ");
        builder.append("				FROM   bccs_im.stock_inspect si ");
        builder.append("			   WHERE       si.create_date >= #fromDate ");
        builder.append("					   AND si.create_date <= #toDate + 1 ");
        builder.append("					   AND owner_type = a.owner_type ");
        builder.append("					   AND owner_id = a.owner_id ");
        builder.append("					   AND prod_offer_type_id = a.prod_offer_type_id ");
        builder.append("					   AND prod_offer_id = a.prod_offer_id ");
        builder.append("					   AND state_id = a.state_id) ");
        builder.append("				 quantity_check, ");
        builder.append("			 (SELECT   quantity_poor ");
        builder.append("				FROM   bccs_im.stock_inspect si ");
        builder.append("			   WHERE       si.create_date >= #fromDate ");
        builder.append("					   AND si.create_date <= #toDate + 1 ");
        builder.append("					   AND owner_type = a.owner_type ");
        builder.append("					   AND owner_id = a.owner_id ");
        builder.append("					   AND prod_offer_type_id = a.prod_offer_type_id ");
        builder.append("					   AND prod_offer_id = a.prod_offer_id ");
        builder.append("					   AND state_id = a.state_id) ");
        builder.append("				 quantity_poor, ");
        builder.append("			 (SELECT   note ");
        builder.append("				FROM   bccs_im.stock_inspect si ");
        builder.append("			   WHERE       si.create_date >= #fromDate ");
        builder.append("					   AND si.create_date <= #toDate + 1 ");
        builder.append("					   AND owner_type = a.owner_type ");
        builder.append("					   AND owner_id = a.owner_id ");
        builder.append("					   AND prod_offer_type_id = a.prod_offer_type_id ");
        builder.append("					   AND prod_offer_id = a.prod_offer_id ");
        builder.append("					   AND state_id = a.state_id) ");
        builder.append("				 note, ");
        builder.append("             (SELECT   sv.name");
        builder.append("              FROM   option_set s, option_set_value sv");
        builder.append("             WHERE       s.id = sv.option_set_id");
        builder.append("                     AND s.status = 1");
        builder.append("                     AND sv.status = 1");
        builder.append("                     AND s.code = 'GOODS_STATE'");
        builder.append("                     AND trim (sv.VALUE)= to_char(a.state_id)) AS state_Name, ");
        builder.append("                     a.state_id ");
        builder.append("	  FROM   (SELECT   si.owner_id, ");
        builder.append("					   si.owner_type, ");
        builder.append("					   si.prod_offer_type_id, ");
        builder.append("					   si.prod_offer_id, ");
        builder.append("					   si.state_id ");
        builder.append("				FROM   bccs_im.stock_inspect si ");
        builder.append("			   WHERE       si.create_date >= #fromDate ");
        builder.append("					   AND si.create_date <= #toDate + 1 ");
        builder.append("					   AND si.owner_id = #ownerId ");
//        builder.append("					   AND si.is_finished <> 1 ");
        builder.append("					   AND si.owner_type = #ownerType ");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("                   AND si.prod_offer_id = #prodOfferId");
        }
        builder.append("					   AND si.prod_offer_type_id = #prodOfferTypeId ");
        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("				   AND si.state_id = #stateId ");
        }
        builder.append("			  MINUS ");
        builder.append("			  SELECT   st.owner_id, ");
        builder.append("					   st.owner_type, ");
        builder.append("					   st.prod_offer_type_id, ");
        builder.append("					   st.prod_offer_id, ");
        builder.append("					   st.state_id ");
        builder.append("				FROM   bccs_im.product_offering pr, ");
        builder.append("					   bccs_im.stock_daily_remain_general st ");
        builder.append("			   WHERE   1 = 1 ");
        builder.append("					   AND st.remain_date = ");
        builder.append("							  LAST_DAY (ADD_MONTHS (TRUNC (SYSDATE), -1)) ");
        builder.append("					   AND st.owner_id = #ownerId ");
        builder.append("					   AND st.owner_type = #ownerType ");
        builder.append("					   AND pr.product_offer_type_id = #prodOfferTypeId ");
        builder.append("					   AND pr.status = 1 ");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("                   AND pr.prod_offer_id = #prodOfferId");
        }
        builder.append("					   AND pr.prod_offer_id = st.prod_offer_id ");
        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("					   AND st.state_id = #stateId ");
        }
        builder.append("					   AND st.current_quantity > 0) a ");

        Query query2 = em.createNativeQuery(builder.toString());
        query2.setHint(QueryHints.JDBC_TIMEOUT, 300);
        query2.setParameter("ownerId", ownerId);
        query2.setParameter("ownerType", ownerType);
        query2.setParameter("prodOfferTypeId", productOfferTypeId);
        query2.setParameter("fromDate", fromDate);
        query2.setParameter("toDate", toDate);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query2.setParameter("prodOfferId", prodOfferId);
        }

        if (!DataUtil.isNullOrZero(stateId)) {
            query2.setParameter("stateId", stateId);
        }

        List<Object[]> queryResult2 = query2.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult2)) {
            convertStockInspect(ownerType, ownerId, stateId, result, lsDamageStateId, queryResult2);
        }


        return result;
    }

    private void convertStockInspect(Long ownerType, Long ownerId, Long stateId, List<StockInspectCheckDTO> result, List<Long> lsDamageStateId, List<Object[]> queryResult) {
        int index;
        for (Object[] ob : queryResult) {
            index = 0;
            StockInspectCheckDTO stockInspectCheckDTO = new StockInspectCheckDTO();
            stockInspectCheckDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
            stockInspectCheckDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
            stockInspectCheckDTO.setProductCode(DataUtil.safeToString(ob[index++]));
            stockInspectCheckDTO.setProductName(DataUtil.safeToString(ob[index++]));
            stockInspectCheckDTO.setCurrentQuantity(DataUtil.safeToLong(ob[index++]));
            stockInspectCheckDTO.setQuanittyReal(DataUtil.safeToLong(ob[index++]));
            stockInspectCheckDTO.setQuantityPoor(DataUtil.safeToLong(ob[index++]));
            stockInspectCheckDTO.setNote(DataUtil.safeToString(ob[index++]));
            stockInspectCheckDTO.setStateName(DataUtil.safeToString(ob[index++]));
            stockInspectCheckDTO.setStateId(DataUtil.safeToLong(ob[index]));
            stockInspectCheckDTO.setOwnerType(ownerType);
            stockInspectCheckDTO.setOwnerId(ownerId);
            //trang thai disable hang kem pham
            stockInspectCheckDTO.setDisableQuantityPoor(disAbleQuantityPoor(stockInspectCheckDTO.getStateId(), lsDamageStateId));
            if (stockInspectCheckDTO.isDisableQuantityPoor() && DataUtil.safeEqual(stockInspectCheckDTO.getQuantityPoor(), 0L)) {
                stockInspectCheckDTO.setQuantityPoor(stockInspectCheckDTO.getCurrentQuantity());
            }
            result.add(stockInspectCheckDTO);
        }
    }

    private boolean disAbleQuantityPoor(Long stateId, List<Long> lsDamageStateId) {
        return stateId != null && lsDamageStateId != null && lsDamageStateId.contains(stateId);
    }

    @Override
    public List<StockInspectCheckDTO> getStockInspectForSerial(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws Exception {
        StringBuilder builder = new StringBuilder("");
        List<StockInspectCheckDTO> result = Lists.newArrayList();
        builder.append(" SELECT pr.prod_offer_id, pr.product_offer_type_id, pr.code, pr.name, si.state_id AS state_Id, ");
        builder.append("             (SELECT   sv.name");
        builder.append("              FROM   option_set s, option_set_value sv");
        builder.append("             WHERE       s.id = sv.option_set_id");
        builder.append("                     AND s.status = 1");
        builder.append("                     AND sv.status = 1");
        builder.append("                     AND s.code = 'GOOD_STATE'");
        builder.append("                     AND trim (sv.VALUE)= to_char(si.state_id)) AS state_Name, ");
        builder.append("         sir.stock_inspect_real_id AS id, ");
        builder.append("         sir.from_serial AS from_Serial, ");
        builder.append("         sir.to_serial AS to_Serial, ");
        builder.append("         sir.quantity AS quantity ");
        builder.append("    FROM stock_inspect si, ");
        builder.append("         stock_inspect_real sir, ");
        builder.append("         product_offering pr");
        builder.append("   WHERE     1 = 1 ");
        builder.append("         AND si.stock_inspect_id = sir.stock_inspect_id ");
        builder.append("         AND si.prod_offer_id = pr.prod_offer_id ");
        builder.append("         AND si.owner_id = #ownerId");
        builder.append("         AND si.owner_type = #ownerType");
        builder.append("         AND si.prod_offer_type_id = #prodOfferTypeId");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("     AND si.prod_offer_id = #prodOfferId");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("     AND si.state_id = #stateId");
        }
        builder.append("         AND si.create_date >= #fromDate");
        builder.append("         AND si.create_date < #toDate +1 ");
//        builder.append("         AND NVL(si.is_finished,0) = 0");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("prodOfferTypeId", productOfferTypeId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockInspectCheckDTO stockInspectCheckDTO = new StockInspectCheckDTO();
                stockInspectCheckDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setProductCode(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setProductName(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setStateName(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setStockInspectRealId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setFromSerial(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setToSerial(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setQuantity(DataUtil.safeToLong(ob[index]));
                stockInspectCheckDTO.setOwnerType(ownerType);
                stockInspectCheckDTO.setOwnerId(ownerId);
                result.add(stockInspectCheckDTO);
            }
        }
        return result;
    }

    @Override
    public Long getQuantityStockTotal(Long ownerType, Long ownerId, Long productOfferTypeId, Long prodOfferId, Long stateId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT   SUM (current_quantity) AS quantity");
        builder.append("    FROM   stock_total st, product_offering pr");
        builder.append("   WHERE       pr.prod_offer_id = st.prod_offer_id");
        builder.append("           AND st.owner_id = #ownerId");
        builder.append("           AND st.owner_type = #ownerType");
        builder.append("           AND pr.product_offer_type_id = #productOfferTypeId");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("           AND pr.prod_offer_id = #prodOfferId");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("           AND st.state_id = #stateId");
        }
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("productOfferTypeId", productOfferTypeId);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        List lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return DataUtil.safeToLong(lst.get(0));
        }
        return 0L;
    }

    @Override
    public void updateStockInspectAllFinish(Long ownerType, Long ownerId, Date fromDate, Date toDate, Long finishStatus) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  UPDATE   stock_inspect");
        builder.append("     SET   is_finished_all = #finishStatus ");
        builder.append("   WHERE       owner_id = #ownerId");
        builder.append("           AND owner_type = #ownerType");
        builder.append("           AND create_date >= #fromDate");
        builder.append("           AND create_date < #toDate + 1");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("finishStatus", finishStatus);
        query.executeUpdate();
    }

    @Override
    public StockInspectExportDTO exportStockInspect(Date fromDate, Date toDate, Long ownerType, Long ownerId, Long prodOfferTypeId, Long stateId, boolean isSpecConfirmQuantity) throws Exception {
        StockInspectExportDTO stockInspectExportDTO = new StockInspectExportDTO();
        stockInspectExportDTO.setLstRealSerial(exportRealSerial(fromDate, toDate, ownerType, ownerId, prodOfferTypeId, stateId));
        stockInspectExportDTO.setLstSysSerial(exportSysSerial(fromDate, toDate, ownerType, ownerId, prodOfferTypeId, stateId));
        stockInspectExportDTO.setLstProductNotApprove(exportProductNotApprove(fromDate, toDate, ownerType, ownerId, isSpecConfirmQuantity));
        stockInspectExportDTO.setLstTotal(exportTotal(fromDate, toDate, ownerType, ownerId));
        return stockInspectExportDTO;

    }

    public List<ApproveStockInspectDTO> exportRealSerial(Date fromDate, Date toDate, Long ownerType, Long ownerId, Long prodOfferTypeId, Long stateId) throws Exception {
        List<ApproveStockInspectDTO> result = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT pr.code AS productCode, ");
        strQuery.append(" pr.name AS productName,");
        strQuery.append(" sir.from_serial AS fromSerial, ");
        strQuery.append(" sir.to_serial AS toSerial, ");
        strQuery.append(" sir.quantity AS quantity, ");


        strQuery.append(" (SELECT   sv.name ");
        strQuery.append(" FROM   option_set s, option_set_value sv");
        strQuery.append(" WHERE       s.id = sv.option_set_id ");
        strQuery.append(" AND s.status = 1");
        strQuery.append(" AND sv.status = 1");
        strQuery.append(" AND s.code = 'GOODS_STATE'");
        strQuery.append(" AND sv.VALUE = TO_CHAR(si.state_id)) AS status ");

        strQuery.append(" FROM stock_inspect si, ");
        strQuery.append(" stock_inspect_real sir,");
        strQuery.append(" product_offering pr");
        strQuery.append(" WHERE si.stock_inspect_id = sir.stock_inspect_id  ");
        strQuery.append(" AND si.prod_offer_id = pr.prod_offer_id ");
        strQuery.append(" AND si.create_date >= #from_date");
        strQuery.append(" AND si.create_date < #to_date");
        strQuery.append(" AND si.owner_id = #owner_id");
        strQuery.append(" AND si.owner_type = #owner_type");

        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            strQuery.append(" AND si.prod_offer_type_id = #prod_offer_type_id ");
        } else {
            //mat hang no-serial
            strQuery.append(" AND si.prod_offer_type_id <> 11");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            strQuery.append(" AND si.state_id = #state_id");
        }
        strQuery.append("              order by pr.code");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("from_date", fromDate);
        query.setParameter("to_date", toDate);
        query.setParameter("owner_id", ownerId);
        query.setParameter("owner_type", ownerType);

        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            query.setParameter("prod_offer_type_id", prodOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("state_id", stateId);
        }
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                ApproveStockInspectDTO approveStockInspectDTO = new ApproveStockInspectDTO();
                approveStockInspectDTO.setProductCode(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setProductName(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setFromSerial(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setToSearial(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setQuantity(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setProductStatus(DataUtil.safeToString(object[index]));
                result.add(approveStockInspectDTO);
            }
            return result;
        }
        return null;
    }

    public List<ApproveStockInspectDTO> exportSysSerial(Date fromDate, Date toDate, Long ownerType, Long ownerId, Long prodOfferTypeId, Long stateId) throws Exception {
        List<ApproveStockInspectDTO> result = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT pr.code AS productCode, ");
        strQuery.append(" pr.name AS productName,");
        strQuery.append(" sir.from_serial AS fromSerial, ");
        strQuery.append(" sir.to_serial AS toSerial, ");
        strQuery.append(" sir.quantity AS quantity, ");


        strQuery.append(" (SELECT   sv.name ");
        strQuery.append(" FROM   option_set s, option_set_value sv");
        strQuery.append(" WHERE       s.id = sv.option_set_id ");
        strQuery.append(" AND s.status = 1");
        strQuery.append(" AND sv.status = 1");
        strQuery.append(" AND s.code = 'GOODS_STATE'");
        strQuery.append(" AND sv.VALUE = TO_CHAR(si.state_id)) AS status ");


        strQuery.append(" FROM stock_inspect si, ");
        strQuery.append(" stock_inspect_sys sir,");
        strQuery.append(" product_offering pr");
        strQuery.append(" WHERE si.stock_inspect_id = sir.stock_inspect_id  ");
        strQuery.append(" AND si.prod_offer_id = pr.prod_offer_id ");
        strQuery.append(" AND si.create_date >= #from_date");
        strQuery.append(" AND si.create_date < #to_date");
        strQuery.append(" AND si.owner_id = #owner_id");
        strQuery.append(" AND si.owner_type = #owner_type");

        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            strQuery.append(" AND si.prod_offer_type_id = #prod_offer_type_id ");
        } else {
            //mat hang no-serial
            strQuery.append(" AND si.prod_offer_type_id <> 11");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            strQuery.append(" AND si.state_id = #state_id");
        }
        strQuery.append("              order by pr.code");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("from_date", fromDate);
        query.setParameter("to_date", toDate);
        query.setParameter("owner_id", ownerId);
        query.setParameter("owner_type", ownerType);

        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            query.setParameter("prod_offer_type_id", prodOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("state_id", stateId);
        }
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                ApproveStockInspectDTO approveStockInspectDTO = new ApproveStockInspectDTO();
                approveStockInspectDTO.setProductCode(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setProductName(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setFromSerial(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setToSearial(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setQuantity(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setProductStatus(DataUtil.safeToString(object[index]));
                result.add(approveStockInspectDTO);
            }
            return result;
        }
        return null;
    }

    public List<ApproveStockInspectDTO> exportProductNotApprove(Date fromDate, Date toDate, Long ownerType, Long ownerId, boolean isSpecConfirmQuantity) throws Exception {
        List<ApproveStockInspectDTO> result = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        if (isSpecConfirmQuantity) {
            strQuery.append(" ( ");
        }
        strQuery.append(" SELECT   st.prod_offer_id,");
        strQuery.append(" pr.code AS productcode,");
        strQuery.append(" pr.name AS productname,");
        strQuery.append(" st.current_quantity AS quantity,");
        strQuery.append(" (SELECT   sv.name ");
        strQuery.append(" FROM   option_set s, option_set_value sv");
        strQuery.append(" WHERE       s.id = sv.option_set_id ");
        strQuery.append(" AND s.status = 1");
        strQuery.append(" AND sv.status = 1");
        strQuery.append(" AND s.code = 'GOODS_STATE'");
        strQuery.append(" AND sv.VALUE = TO_CHAR(st.state_id)) AS statename ");
        if (isSpecConfirmQuantity) {
            strQuery.append(" FROM   STOCK_DAILY_REMAIN_GENERAL st, product_offering pr");
        } else {
            strQuery.append(" FROM   stock_total st, product_offering pr");
        }
        strQuery.append(" WHERE       st.owner_id = #owner_id");
        if (isSpecConfirmQuantity) {
            strQuery.append("  AND st.remain_date = last_day(add_months(trunc(sysdate), -1)) ");
        }
        strQuery.append(" AND st.owner_type = #owner_type");
        strQuery.append(" AND st.current_quantity > 0");
        strQuery.append(" AND st.prod_offer_id = pr.prod_offer_id ");
        strQuery.append(" AND pr.product_offer_type_id NOT IN (1, 2, 3)");
        strQuery.append(" AND NOT EXISTS ");
        strQuery.append(" (SELECT   1 ");
        strQuery.append(" FROM   stock_inspect si");
        strQuery.append(" WHERE       si.owner_id = st.owner_id ");
        strQuery.append(" AND si.owner_type = st.owner_type ");
        strQuery.append(" AND si.create_date >= #from_date");
        strQuery.append(" AND si.create_date < #to_date + 1 ");
        strQuery.append(" AND si.is_finished_all = 1 ");
        strQuery.append(" UNION ");
        strQuery.append(" SELECT   1 ");
        strQuery.append(" FROM   stock_inspect si");
        strQuery.append(" WHERE       si.owner_id = st.owner_id ");
        strQuery.append(" AND si.owner_type = st.owner_type ");
        strQuery.append(" AND si.prod_offer_id = st.prod_offer_id ");
        strQuery.append(" AND si.state_id = st.state_id ");
        strQuery.append(" AND si.create_date >= #from_date");
        strQuery.append(" AND si.create_date < #to_date + 1 ");
        strQuery.append(" AND si.is_finished = 1)");

        if (isSpecConfirmQuantity) {
            strQuery.append("	) union all ( ");
            strQuery.append("	SELECT   a.prod_offer_id, ");
            strQuery.append("			 (SELECT   code FROM   product_offering WHERE   prod_offer_id = a.prod_offer_id) code, ");
            strQuery.append("			 (SELECT   name FROM   product_offering WHERE   prod_offer_id = a.prod_offer_id) name, ");
            strQuery.append("			 0 current_quantity, ");
            strQuery.append("             (SELECT   sv.name");
            strQuery.append("              FROM   option_set s, option_set_value sv");
            strQuery.append("             WHERE       s.id = sv.option_set_id");
            strQuery.append("                     AND s.status = 1");
            strQuery.append("                     AND sv.status = 1");
            strQuery.append("                     AND s.code = 'GOODS_STATE'");
            strQuery.append("                     AND trim (sv.VALUE)= to_char(a.state_id)) AS state_Name ");
            strQuery.append("	  FROM   (SELECT   si.owner_id, ");
            strQuery.append("					   si.owner_type, ");
            strQuery.append("					   si.prod_offer_type_id, ");
            strQuery.append("					   si.prod_offer_id, ");
            strQuery.append("					   si.state_id ");
            strQuery.append("				FROM   bccs_im.stock_inspect si ");
            strQuery.append("			   WHERE       si.create_date >= #from_date ");
            strQuery.append("					   AND si.create_date <= #to_date + 1 ");
            strQuery.append("                      AND si.prod_offer_type_id NOT IN (1, 2, 3) ");
            strQuery.append("					   AND si.owner_id = #owner_id ");
            strQuery.append("					   AND si.is_finished <> 1 ");
            strQuery.append("					   AND si.owner_type = #owner_type ");
            strQuery.append("			  MINUS ");
            strQuery.append("			  SELECT   st.owner_id, ");
            strQuery.append("					   st.owner_type, ");
            strQuery.append("					   st.prod_offer_type_id, ");
            strQuery.append("					   st.prod_offer_id, ");
            strQuery.append("					   st.state_id ");
            strQuery.append("				FROM    ");
            strQuery.append("					   bccs_im.stock_daily_remain_general st ");
            strQuery.append("			   WHERE   1 = 1 ");
            strQuery.append("					   AND st.remain_date = ");
            strQuery.append("							  LAST_DAY (ADD_MONTHS (TRUNC (SYSDATE), -1)) ");
            strQuery.append("					   AND st.owner_id = #owner_id ");
            strQuery.append("					   AND st.prod_offer_type_id NOT IN (1, 2, 3) ");
            strQuery.append("					   AND st.owner_type = #owner_type ");
            strQuery.append("					   AND st.current_quantity > 0) a ");
            strQuery.append("	) ");
        }

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("owner_id", ownerId);
        query.setParameter("owner_type", ownerType);
        query.setParameter("from_date", fromDate);
        query.setParameter("to_date", toDate);


        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                ApproveStockInspectDTO approveStockInspectDTO = new ApproveStockInspectDTO();
                approveStockInspectDTO.setProdOfferId(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setProductCode(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setProductName(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setQuantity(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setProductStatus(DataUtil.safeToString(object[index]));
                result.add(approveStockInspectDTO);
            }
        }

        return result;
    }

    public List<ApproveStockInspectDTO> exportTotal(Date fromDate, Date toDate, Long ownerType, Long ownerId) throws Exception {
        List<ApproveStockInspectDTO> result = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT pr.code AS productcode,");
        strQuery.append(" pr.name AS productname,");
        strQuery.append(" (SELECT   sv.name ");
        strQuery.append(" FROM   option_set s, option_set_value sv");
        strQuery.append(" WHERE       s.id = sv.option_set_id ");
        strQuery.append(" AND s.status = 1");
        strQuery.append(" AND sv.status = 1");
        strQuery.append(" AND s.code = 'GOOD_STATE'");
        strQuery.append(" AND sv.VALUE = TO_CHAR(si.state_id)) AS statename, ");
        strQuery.append(" si.quantity_sys as quantitySys,");
        strQuery.append(" si.quantity_real as quantityReal,");
        strQuery.append(" (si.quantity_sys - si.quantity_real) AS diff,");
        strQuery.append(" si.create_user as createUser,");
        strQuery.append(" si.approve_user as approveUser,");
        strQuery.append(" TO_CHAR (si.create_date, 'dd/mm/yyyy HH24:mi:ss') AS createDateStr,");
        strQuery.append(" (SELECT   sv.name FROM   option_set s, option_set_value sv ");
        strQuery.append(" WHERE       s.id = sv.option_set_id");
        strQuery.append(" AND s.status = 1 AND sv.status = 1  AND s.code = 'STOCK_INSPECT_STATUS' ");
        strQuery.append(" AND sv.VALUE = to_char(si.approve_status)) as approveStatusString ");
        strQuery.append(" FROM   stock_inspect si, product_offering pr ");
        strQuery.append(" WHERE   si.prod_offer_id = pr.prod_offer_id");
        strQuery.append(" AND si.is_finished = 1 ");
        strQuery.append(" AND si.create_date >= #from_date");
        strQuery.append(" AND si.create_date < #to_date + 1 ");
        strQuery.append(" AND si.owner_id = #owner_id");
        strQuery.append(" AND si.owner_type = #owner_type");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("from_date", fromDate);
        query.setParameter("to_date", toDate);
        query.setParameter("owner_id", ownerId);
        query.setParameter("owner_type", ownerType);

        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                ApproveStockInspectDTO approveStockInspectDTO = new ApproveStockInspectDTO();
                approveStockInspectDTO.setProductCode(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setProductName(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setProductStatus(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setQuantitySystem(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setQuantityReal(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setDiff(DataUtil.safeToLong(object[index++]));
                approveStockInspectDTO.setRequestUser(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setApproveUser(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setIssueDateTime(DataUtil.safeToString(object[index++]));
                approveStockInspectDTO.setApproveStatusString(DataUtil.safeToString(object[index]));
                result.add(approveStockInspectDTO);
            }
            return result;
        }
        return null;
    }

    @Override
    public int updateApproveStaffNew(Long ownerType, Long ownerId, Long productOfferTypeId, Long prodOfferId, Long stateId, Date fromDate, Date toDate, StaffDTO staffDTO) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  UPDATE   stock_inspect");
        builder.append("     SET   approve_user_id = #approveUserId, approve_user = #approveUser, approve_user_name = #approveUserName, update_date = sysdate");
        builder.append("   WHERE       owner_id = #ownerId");
        builder.append("           AND owner_type = #ownerType");
        if (!DataUtil.isNullObject(productOfferTypeId)) {
            builder.append("           AND prod_offer_type_id = #prodOfferTypeId");
        }
        if (!DataUtil.isNullObject(prodOfferId)) {
            builder.append("           AND prod_offer_id = #prodOfferId");
        }
        if (!DataUtil.isNullObject(stateId)) {
            builder.append("           AND state_id = #stateId");
        }
        builder.append("           AND create_date >= #fromDate");
        builder.append("           AND create_date < #toDate");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("approveUserId", staffDTO.getStaffId());
        query.setParameter("approveUser", staffDTO.getStaffCode());
        query.setParameter("approveUserName", staffDTO.getName());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (!DataUtil.isNullObject(productOfferTypeId)) {
            query.setParameter("prodOfferTypeId", productOfferTypeId);
        }
        if (!DataUtil.isNullObject(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullObject(stateId)) {
            query.setParameter("stateId", stateId);
        }
        return query.executeUpdate();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage updateApprove(List<StockInspectDTO> lsStockInspectDTO) throws LogicException, Exception {
        BaseMessage result = new BaseMessage(true);
        try {
            Connection conn = IMCommonUtils.getDBIMConnection();
            conn.setAutoCommit(false);
            PreparedStatement stmUpdate = null;
            PreparedStatement stmDelete = null;
            PreparedStatement stmIsFinishAll = null;
            StringBuilder queryString = new StringBuilder(" ");
            queryString.append(" update bccs_im.stock_inspect ");
            queryString.append(" set approve_status = ?, approve_note = ?, is_finished = ?, approve_date = sysdate, quantity_finance = ? ");
            queryString.append(" where stock_inspect_id = ? and approve_status = 0 ");
            stmUpdate = conn.prepareStatement(queryString.toString());
            // Xoa so lieu he thong di de thuc hien kiem ke lai
            stmDelete = conn.prepareStatement("delete bccs_im.stock_inspect_sys where stock_inspect_id = ? ");
            stmIsFinishAll = conn.prepareStatement("update bccs_im.stock_inspect set is_finished_all = 0 where stock_inspect_id = ? ");
            Long totalRecord = 0L;
            for (StockInspectDTO dto : lsStockInspectDTO) {
                if (!DataUtil.isNullObject(dto)) {
                    checkValidate(dto);

                    stmUpdate.setLong(1, dto.getApproveStatus());
                    stmUpdate.setString(2, dto.getApproveNote());
                    stmUpdate.setLong(3, dto.getIsFinished());
                    stmUpdate.setLong(4, dto.getQuantityFinance());
                    stmUpdate.setLong(5, dto.getStockInspectId());
                    stmUpdate.addBatch();
                    totalRecord = totalRecord + 1L;
                    // Truong hop tu choi thi xoa du lieu he thong di de kiem ke lai
                    if (!DataUtil.isNullOrZero(dto.getApproveStatus()) && DataUtil.safeEqual(dto.getApproveStatus(), Const.INSPECT_APPROVE_STATUS.CANCEL_APPROVE)) {
                        stmDelete.setLong(1, dto.getStockInspectId());
                        stmDelete.addBatch();
                        stmIsFinishAll.setLong(1, dto.getStockInspectId());
                        stmIsFinishAll.addBatch();
                    }
                }
            }
            try {
                int[] updateCount = stmUpdate.executeBatch();
                if (updateCount.length < totalRecord) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "mn.stock.track.number.approve.fail");
                }
                stmDelete.executeBatch();
                stmIsFinishAll.executeBatch();
                conn.commit();

            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                conn.rollback();
            } finally {
                stmUpdate.close();
                stmDelete.close();
                stmIsFinishAll.close();
                conn.close();
            }

        } catch (LogicException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("StockInspectServiceImpl.updateApprove", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happen");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    private void checkValidate(StockInspectDTO dto) throws LogicException, Exception {

        if (!DataUtil.isNullOrEmpty(dto.getApproveNote()) && dto.getApproveNote().getBytes("UTF-8").length > 200) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_INNER_VALIDATE, "export.order.view.approve.stock.inspect.reason.maxlength");
        }
    }
}
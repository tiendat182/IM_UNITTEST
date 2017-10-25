package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ApproveChangeProductDTO;
import com.viettel.bccs.inventory.model.ChangeModelTrans;
import com.viettel.bccs.inventory.dto.ChangeModelTransDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ChangeModelTrans.COLUMNS;
import com.viettel.bccs.inventory.model.QChangeModelTrans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ChangeModelTransRepoImpl implements ChangeModelTransRepoCustom {

    public static final Logger logger = Logger.getLogger(ChangeModelTransRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QChangeModelTrans changeModelTrans = QChangeModelTrans.changeModelTrans;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APPROVEDATE:
                        expression = DbUtil.createDateExpression(changeModelTrans.approveDate, filter);
                        break;
                    case APPROVEUSERID:
                        expression = DbUtil.createLongExpression(changeModelTrans.approveUserId, filter);
                        break;
                    case CHANGEMODELTRANSID:
                        expression = DbUtil.createLongExpression(changeModelTrans.changeModelTransId, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(changeModelTrans.createDate, filter);
                        break;
                    case CREATEUSERID:
                        expression = DbUtil.createLongExpression(changeModelTrans.createUserId, filter);
                        break;
                    case FROMOWNERID:
                        expression = DbUtil.createLongExpression(changeModelTrans.fromOwnerId, filter);
                        break;
                    case FROMOWNERTYPE:
                        expression = DbUtil.createLongExpression(changeModelTrans.fromOwnerType, filter);
                        break;
                    case LISTSTOCKTRANSID:
                        expression = DbUtil.createStringExpression(changeModelTrans.listStockTransId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(changeModelTrans.status, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(changeModelTrans.stockTransId, filter);
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(changeModelTrans.toOwnerId, filter);
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(changeModelTrans.toOwnerType, filter);
                        break;
                    case REQUESTTYPE:
                        expression = DbUtil.createLongExpression(changeModelTrans.requestType, filter);
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

    public List<ApproveChangeProductDTO> getLstChangeStockModel(ApproveChangeProductDTO searchInputDTO) throws Exception {
        List<ApproveChangeProductDTO> lstApproveChangeProductDTOs = Lists.newArrayList();
        StringBuilder queryString = new StringBuilder("");
        queryString.append(" SELECT cmt.from_owner_id, "); // 0
        queryString.append("        cmt.from_owner_type, "); // 1
        queryString.append("        (SELECT owner_code || '-' || owner_name ");
        queryString.append("           FROM v_shop_staff ");
        queryString.append("          WHERE owner_id = cmt.from_owner_id ");
        queryString.append("                AND owner_type = cmt.from_owner_type), ");// 2
        queryString.append("        cmt.create_user_id, ");// 3
        queryString.append("        (SELECT owner_name ");
        queryString.append("           FROM v_shop_staff ");
        queryString.append("          WHERE owner_id = cmt.create_user_id AND owner_type = 2), ");//4
        queryString.append("        trunc(cmt.create_date), ");// 5
        queryString.append("        (SELECT action_code ");
        queryString.append("           FROM stock_trans_action ");
        queryString.append("          WHERE stock_trans_id = cmt.stock_trans_id ");
        queryString.append("           AND status  = 2 and rownum = 1), ");// 6
        queryString.append("        cmt.to_owner_id, ");// 7
        queryString.append("        cmt.to_owner_type, ");// 8
        queryString.append("        (SELECT owner_code || '-' || owner_name ");
        queryString.append("           FROM v_shop_staff ");
        queryString.append("          WHERE owner_id = cmt.to_owner_id AND owner_type = cmt.to_owner_type), ");// 9
        queryString.append("        (SELECT   sv.name ");
        queryString.append("           FROM   option_set s, option_set_value sv ");
        queryString.append("            WHERE       s.id = sv.option_set_id ");
        queryString.append("                AND s.status = 1 ");
        queryString.append("                AND sv.status = 1 ");
        queryString.append("                AND s.code = 'APPROVE_CHANGE_PRODUCT_STATUS' ");
        queryString.append("                AND sv.VALUE = to_char(cmt.status)), ");//10
        queryString.append("        cmt.approve_user_id, ");// 11
        queryString.append("        (SELECT owner_name ");
        queryString.append("           FROM v_shop_staff ");
        queryString.append("          WHERE owner_id = cmt.approve_user_id AND owner_type = 2), ");// 12
        queryString.append("        trunc(cmt.approve_date), ");// 13
        queryString.append("        cmt.status, ");// 14
        queryString.append("        cmt.change_model_trans_id ");// 15
        queryString.append("   FROM change_model_trans cmt ");
        queryString.append("    WHERE 1=1 ");
        queryString.append("   AND cmt.create_date >= #startDate  ");
        queryString.append("   AND cmt.create_date < #endDate + 1 ");
        if (!DataUtil.isNullObject(searchInputDTO.getStatus())) {
            queryString.append(" AND cmt.status = #status ");
        }
        if (!DataUtil.isNullOrZero(searchInputDTO.getShopId())) {
            queryString.append(" AND cmt.from_owner_id = #fromOwnerId ");
        }
        if (!DataUtil.isNullOrZero(searchInputDTO.getRequestType())) {
            queryString.append(" AND cmt.request_type = #requestType ");
        }

        queryString.append("   ORDER BY cmt.create_date desc ");
        Query query = em.createNativeQuery(queryString.toString());
        query.setParameter("startDate", searchInputDTO.getStartDate());
        query.setParameter("endDate", searchInputDTO.getEndDate());
        if (!DataUtil.isNullObject(searchInputDTO.getStatus())) {
            query.setParameter("status", searchInputDTO.getStatus());
        }
        if (!DataUtil.isNullOrZero(searchInputDTO.getShopId())) {
            query.setParameter("fromOwnerId", searchInputDTO.getShopId());
        }
        if (!DataUtil.isNullOrZero(searchInputDTO.getRequestType())) {
            query.setParameter("requestType", searchInputDTO.getRequestType());
        }
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] row : lst) {
                ApproveChangeProductDTO obj = new ApproveChangeProductDTO();
                obj.setFromOwnerId(DataUtil.safeToLong(row[0]));
                obj.setFromOwnerName(DataUtil.safeToString(row[2]));
                obj.setCreateUser(DataUtil.safeToString(row[4]));
                Date createDate = (Date) row[5];
                obj.setCreateDateStr(DateUtil.date2ddMMyyyyString(createDate));
                obj.setActionCode(DataUtil.safeToString(row[6]));
                obj.setToOwnerId(DataUtil.safeToLong(row[7]));
                obj.setToOwnerName(DataUtil.safeToString(row[9]));
                obj.setStatusStr(DataUtil.safeToString(row[10]));
                obj.setApproveUser(DataUtil.safeToString(row[12]));
                Date lastUpdate = (Date) row[13];
                obj.setLastUpdateTimeStr(DateUtil.date2ddMMyyyyString(lastUpdate));
                obj.setStatus(DataUtil.safeToLong(row[14]));
                obj.setChangeModelTransId(DataUtil.safeToLong(row[15]));
                lstApproveChangeProductDTOs.add(obj);
            }
        }
        return lstApproveChangeProductDTOs;
    }

    public List<ChangeModelTrans> getLstCancelRequestThread(Long maxDay) throws LogicException, Exception {
        StringBuilder queryString = new StringBuilder("");
        queryString.append(" SELECT * ");
        queryString.append(" FROM change_model_trans ");
        queryString.append(" WHERE     create_date >= SYSDATE - #maxDay ");
        queryString.append(" AND create_date < SYSDATE ");
        queryString.append(" AND request_type = 2 ");
        queryString.append(" AND status = 6 ");
        queryString.append(" AND (retry is null or retry < 5) ");
        Query query = em.createNativeQuery(queryString.toString(), ChangeModelTrans.class);
        query.setParameter("maxDay", maxDay);
        return query.getResultList();
    }

    public List<ChangeModelTrans> getLstApproveRequestThread(Long maxDay) throws LogicException, Exception {
        StringBuilder queryString = new StringBuilder("");
        queryString.append(" SELECT * ");
        queryString.append(" FROM change_model_trans ");
        queryString.append(" WHERE     create_date >= SYSDATE - #maxDay ");
        queryString.append(" AND create_date < SYSDATE ");
        queryString.append(" AND request_type = 2 ");
        queryString.append(" AND status = 5 ");
        queryString.append(" AND (retry is null or retry < 5) ");
        Query query = em.createNativeQuery(queryString.toString(), ChangeModelTrans.class);
        query.setParameter("maxDay", maxDay);
        return query.getResultList();
    }
    
    @Override
    public List<ChangeModelTransDTO> findApprovedRequestProductExchangeKit(Long shopId, Date fromDate, Date toDate) throws Exception, LogicException {
        StringBuilder jQuery = new StringBuilder();
        jQuery.append(" SELECT   cmt.change_model_trans_id, " +
                "  cmt.stock_trans_id, " +
                "  cmt.from_owner_id, " +
                "  cmt.from_owner_type, " +
                "  cmt.to_owner_id, " +
                "  cmt.to_owner_type, " +
                "  cmt.create_user_id, " +
                "  cmt.create_date, " +
                "  cmt.approve_user_id, " +
                "  cmt.approve_date, " +
                "  cmt.status, " +
                "  cmt.list_stock_trans_id, " +
                "  cmt.request_type , " +
                "           (SELECT   owner_code || '-' || owner_name " +
                "              FROM   v_shop_staff " +
                "             WHERE   owner_id = cmt.from_owner_id " +
                "                     AND owner_type = cmt.from_owner_type) " +
                "               shop_codename, " +
                "           (SELECT   owner_name " +
                "              FROM   v_shop_staff " +
                "             WHERE   owner_id = cmt.create_user_id AND owner_type = 2) Create_owner_name, " +
                "           (SELECT   action_code " +
                "              FROM   stock_trans_action " +
                "             WHERE       stock_trans_id = cmt.stock_trans_id " +
                "                     AND status = 2 " +
                "                     AND ROWNUM = 1) action_code, " +
                "           DECODE (cmt.to_owner_type, 1," +
                "               (SELECT   owner_name " +
                "                  FROM   v_shop_staff " +
                "                 WHERE   owner_id = cmt.to_owner_id " +
                "                         AND owner_type = cmt.to_owner_type), " +
                "               (SELECT   partner_name " +
                "                  FROM   bccs_im.partner " +
                "                 WHERE   partner_id = cmt.to_owner_id)) to_owner_name, " +
                "           (SELECT   sv.name " +
                "              FROM   option_set s, option_set_value sv " +
                "             WHERE       s.id = sv.option_set_id " +
                "                     AND s.status = 1 " +
                "                     AND sv.status = 1 " +
                "                     AND s.code = 'APPROVE_CHANGE_PRODUCT_STATUS' " +
                "                     AND sv.VALUE = TO_CHAR (cmt.status)) " +
                "               request_status, " +
                "           (SELECT   owner_name " +
                "              FROM   v_shop_staff " +
                "             WHERE   owner_id = cmt.approve_user_id AND owner_type = 2) approve_owner_name, " +
                "           TRUNC (cmt.approve_date) approve_date " +
                "    FROM   change_model_trans cmt " +
                "   WHERE       1 = 1 ");
        if (!DataUtil.isNullObject(fromDate)
                && !DataUtil.isNullObject(toDate)) {
            jQuery.append(" AND cmt.create_date >= #fromDate" +
                    "       AND cmt.create_date <  #toDate");
        }
        if (DataUtil.isNullObject(fromDate)
                && !DataUtil.isNullObject(toDate)) {
            jQuery.append(" AND cmt.create_date <  #toDate");
        }
        if (!DataUtil.isNullObject(fromDate)
                && DataUtil.isNullObject(toDate)) {
            jQuery.append(" AND cmt.create_date >= #fromDate");
        }
        jQuery.append("     AND cmt.request_type = 2 " +
                "           AND cmt.status = 3 " +
                "           AND cmt.from_owner_id = #shopId " +
                "ORDER BY   cmt.create_date DESC");
        ;
        Query query = em.createNativeQuery(jQuery.toString());
        if (!DataUtil.isNullObject(fromDate)) {
            query.setParameter("fromDate", fromDate);
        }
        if (!DataUtil.isNullObject(toDate)) {
            query.setParameter("toDate", toDate);
        }
        query.setParameter("shopId", shopId);
        List<ChangeModelTransDTO> results = Lists.newArrayList();
        List<Object[]> resultList = query.getResultList();

        if (!DataUtil.isNullOrEmpty(resultList)) {
            for (Object[] o : resultList) {
                int j = 0;
                ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();

                changeModelTransDTO.setChangeModelTransId(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setStockTransId(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setFromOwnerId(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setFromOwnerType(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setToOwnerId(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setToOwnerType(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setCreateUserId(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setCreateDate(DataUtil.defaultIfNull((Date) o[j++], new Date()));
                changeModelTransDTO.setApproveUserId(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setApproveDate(DataUtil.defaultIfNull((Date) o[j++], new Date()));
                changeModelTransDTO.setStatus(DataUtil.safeToLong(o[j++]));
                changeModelTransDTO.setListStockTransId(DataUtil.safeToString(o[j++]));
                changeModelTransDTO.setRequestType(DataUtil.safeToLong(o[j++]));

                changeModelTransDTO.setShopCodeName(DataUtil.safeToString(o[j++]));
                changeModelTransDTO.setCreateOwnerName(DataUtil.safeToString(o[j++]));
                changeModelTransDTO.setActionCode(DataUtil.safeToString(o[j++]));
                changeModelTransDTO.setToOwnerName(DataUtil.safeToString(o[j++]));
                changeModelTransDTO.setRequestStatus(DataUtil.safeToString(o[j++]));
                changeModelTransDTO.setApproveOwnerName(DataUtil.safeToString(o[j++]));

                results.add(changeModelTransDTO);
            }
        }

        return DataUtil.defaultIfNull(results, new ArrayList<>());
    }

}
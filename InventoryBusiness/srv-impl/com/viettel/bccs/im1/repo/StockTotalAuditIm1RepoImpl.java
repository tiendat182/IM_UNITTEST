package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.dto.StockTotalAuditIm1DTO;
import com.viettel.bccs.im1.model.QStockTotalAuditIm1;
import com.viettel.bccs.im1.model.StockTotalAuditIm1.COLUMNS;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockTotalAuditIm1RepoImpl implements StockTotalAuditIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(StockTotalAuditIm1RepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTotalAuditIm1 stockTotalAudit = QStockTotalAuditIm1.stockTotalAudit;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.ownerId, filter);
                        break;
                    case OWNERCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.ownerCode, filter);
                        break;
                    case OWNERNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.ownerName, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTotalAudit.ownerType, filter);
                        break;
                    case USERID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.userId, filter);
                        break;
                    case USERCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.userCode, filter);
                        break;
                    case USERNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.userName, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.stateId, filter);
                        break;
                    case STOCKMODELID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.stockModelId, filter);
                        break;
                    case STOCKMODELNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.stockModelName, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockTotalAudit.createDate, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockTotalAudit.status, filter);
                        break;
                    case QTY:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qty, filter);
                        break;
                    case QTYISSUE:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qtyIssue, filter);
                        break;
                    case QTYHANG:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qtyHang, filter);
                        break;
                    case QTYBF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qtyBf, filter);
                        break;
                    case QTYISSUEBF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qtyIssueBf, filter);
                        break;
                    case QTYHANGBF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qtyHangBf, filter);
                        break;
                    case QTYAF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qtyAf, filter);
                        break;
                    case QTYISSUEAF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qtyIssueAf, filter);
                        break;
                    case QTYHANGAF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.qtyHangAf, filter);
                        break;
                    case TRANSID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.transId, filter);
                        break;
                    case TRANSCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.transCode, filter);
                        break;
                    case TRANSTYPE:
                        expression = DbUtil.createLongExpression(stockTotalAudit.transType, filter);
                        break;
                    case SOURCETYPE:
                        expression = DbUtil.createLongExpression(stockTotalAudit.sourceType, filter);
                        break;
                    case STICKCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.stickCode, filter);
                        break;
                    case REASONNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.reasonName, filter);
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.reasonId, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(stockTotalAudit.description, filter);
                        break;
                    case SHOPIDLV1:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv1, filter);
                        break;
                    case SHOPCODELV1:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv1, filter);
                        break;
                    case SHOPNAMELV1:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv1, filter);
                        break;
                    case SHOPIDLV2:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv2, filter);
                        break;
                    case SHOPCODELV2:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv2, filter);
                        break;
                    case SHOPNAMELV2:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv2, filter);
                        break;
                    case SHOPIDLV3:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv3, filter);
                        break;
                    case SHOPCODELV3:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv3, filter);
                        break;
                    case SHOPNAMELV3:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv3, filter);
                        break;
                    case SHOPIDLV4:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv4, filter);
                        break;
                    case SHOPCODELV4:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv4, filter);
                        break;
                    case SHOPNAMELV4:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv4, filter);
                        break;
                    case SHOPIDLV5:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv5, filter);
                        break;
                    case SHOPCODELV5:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv5, filter);
                        break;
                    case SHOPNAMELV5:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv5, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopId, filter);
                        break;
                    case SHOPCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCode, filter);
                        break;
                    case SHOPNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopName, filter);
                        break;
                    case SHOPIDLV6:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv6, filter);
                        break;
                    case SHOPCODELV6:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv6, filter);
                        break;
                    case SHOPNAMELV6:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv6, filter);
                        break;
                    case SYNSTATUS:
                        expression = DbUtil.createLongExpression(stockTotalAudit.synStatus, filter);
                        break;
                    case STOCKMODELCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.stockModelCode, filter);
                        break;
                    case STOCKTYPEID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.stockTypeId, filter);
                        break;
                    case VALID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.valid, filter);
                        break;
                    case EXCLUSE_ID_LIST:
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
    public int createStockTotalAudit(StockTotalAuditIm1DTO audit) throws LogicException, Exception {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("INSERT INTO BCCS_IM.STOCK_TOTAL_AUDIT(create_date, owner_id,owner_code,owner_name,owner_type,user_id,user_code,user_name,");
        sqlQuery.append("state_id,stock_model_id,stock_model_name,status, ");
        sqlQuery.append("qty,qty_issue,qty_bf,qty_issue_bf,qty_af,qty_issue_af, ");
        sqlQuery.append("trans_id,trans_code,trans_type,source_type,reason_id,description)");
        sqlQuery.append(" VALUES (#p_create_date,#p_owner_id,#p_owner_code,#p_owner_name,#p_owner_type,#p_user_id,#p_user_code,#p_user_name,");
        sqlQuery.append("#p_state_id,#p_stock_model_id,#p_stock_model_name,#p_status, ");
        sqlQuery.append("#p_qty,#p_qty_issue,#p_qty_bf,#p_qty_issue_bf,#p_qty_af,#p_qty_issue_af,");
        sqlQuery.append("#p_trans_id,#p_trans_code,#p_trans_type,#p_source_type,#p_reason_id,#p_description)");

        Query query = emIM.createNativeQuery(sqlQuery.toString());
        query.setParameter("p_create_date", audit.getCreateDate());
        query.setParameter("p_owner_id", audit.getOwnerId());
        query.setParameter("p_owner_code", DataUtil.safeToString(audit.getOwnerCode()));//
        query.setParameter("p_owner_name", DataUtil.safeToString(audit.getOwnerName()));//
        query.setParameter("p_owner_type", audit.getOwnerType());
        query.setParameter("p_user_id", audit.getUserId());
        query.setParameter("p_user_code", DataUtil.safeToString(audit.getUserCode()));//
        query.setParameter("p_user_name", DataUtil.safeToString(audit.getUserName()));//
        query.setParameter("p_state_id", audit.getStateId());
        query.setParameter("p_stock_model_id", audit.getStockModelId());
        query.setParameter("p_stock_model_name", DataUtil.safeToString(audit.getStockModelName()));
        query.setParameter("p_status", Const.STATUS.ACTIVE);
        query.setParameter("p_qty", audit.getQty());
        query.setParameter("p_qty_issue", audit.getQtyIssue());
//        query.setParameter("p_qty_hang", audit.getQtyHang());//
        query.setParameter("p_qty_bf", audit.getQtyBf());
        query.setParameter("p_qty_issue_bf", audit.getQtyIssueBf());
//        query.setParameter("p_qty_hang_bf", audit.getQtyHangBf());
        query.setParameter("p_qty_af", audit.getQtyAf());
        query.setParameter("p_qty_issue_af", audit.getQtyIssueAf());
//        query.setParameter("p_qty_hang_af", audit.getQtyHangAf());
        query.setParameter("p_trans_id", audit.getTransId());
        query.setParameter("p_trans_code", DataUtil.safeToString(audit.getTransCode()));
        query.setParameter("p_trans_type", audit.getTransType());
        query.setParameter("p_source_type", audit.getSourceType());
        query.setParameter("p_reason_id", DataUtil.safeToLong(audit.getReasonId()));
        query.setParameter("p_description", DataUtil.safeToString(audit.getDescription()));

        return query.executeUpdate();

    }
}
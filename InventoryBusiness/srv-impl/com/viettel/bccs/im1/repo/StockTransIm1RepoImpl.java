package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.model.QStockTransIm1;
import com.viettel.bccs.im1.model.StockTransIm1.COLUMNS;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockTransIm1RepoImpl implements StockTransIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransIm1RepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransIm1 stockTransIm1 = QStockTransIm1.stockTransIm1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCTYPE:
                        expression = DbUtil.createLongExpression(stockTransIm1.accType, filter);
                        break;
                    case ACCOUNT:
                        expression = DbUtil.createStringExpression(stockTransIm1.account, filter);
                        break;
                    case APPROVEDATE:
                        expression = DbUtil.createDateExpression(stockTransIm1.approveDate, filter);
                        break;
                    case APPROVEREASONID:
                        expression = DbUtil.createLongExpression(stockTransIm1.approveReasonId, filter);
                        break;
                    case APPROVEUSERID:
                        expression = DbUtil.createLongExpression(stockTransIm1.approveUserId, filter);
                        break;
                    case BANKPLUSSTATUS:
                        expression = DbUtil.createLongExpression(stockTransIm1.bankplusStatus, filter);
                        break;
                    case CHECKERP:
                        expression = DbUtil.createLongExpression(stockTransIm1.checkErp, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockTransIm1.createDatetime, filter);
                        break;
                    case DEPOSITSTATUS:
                        expression = DbUtil.createLongExpression(stockTransIm1.depositStatus, filter);
                        break;
                    case DRAWSTATUS:
                        expression = DbUtil.createLongExpression(stockTransIm1.drawStatus, filter);
                        break;
                    case FROMOWNERID:
                        expression = DbUtil.createLongExpression(stockTransIm1.fromOwnerId, filter);
                        break;
                    case FROMOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTransIm1.fromOwnerType, filter);
                        break;
                    case FROMSTOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransIm1.fromStockTransId, filter);
                        break;
                    case GOODSWEIGHT:
                        expression = DbUtil.createLongExpression(stockTransIm1.goodsWeight, filter);
                        break;
                    case ISAUTOGEN:
                        expression = DbUtil.createLongExpression(stockTransIm1.isAutoGen, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockTransIm1.note, filter);
                        break;
                    case PARTNERCONTRACTNO:
                        expression = DbUtil.createStringExpression(stockTransIm1.partnerContractNo, filter);
                        break;
                    case PAYSTATUS:
                        expression = DbUtil.createLongExpression(stockTransIm1.payStatus, filter);
                        break;
                    case PROCESSEND:
                        expression = DbUtil.createDateExpression(stockTransIm1.processEnd, filter);
                        break;
                    case PROCESSOFFLINE:
                        expression = DbUtil.createLongExpression(stockTransIm1.processOffline, filter);
                        break;
                    case PROCESSRESULT:
                        expression = DbUtil.createStringExpression(stockTransIm1.processResult, filter);
                        break;
                    case PROCESSSTART:
                        expression = DbUtil.createDateExpression(stockTransIm1.processStart, filter);
                        break;
                    case REALTRANSDATE:
                        expression = DbUtil.createDateExpression(stockTransIm1.realTransDate, filter);
                        break;
                    case REALTRANSUSERID:
                        expression = DbUtil.createLongExpression(stockTransIm1.realTransUserId, filter);
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(stockTransIm1.reasonId, filter);
                        break;
                    case REGIONSTOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransIm1.regionStockTransId, filter);
                        break;
                    case REJECTDATE:
                        expression = DbUtil.createDateExpression(stockTransIm1.rejectDate, filter);
                        break;
                    case REJECTNOTE:
                        expression = DbUtil.createStringExpression(stockTransIm1.rejectNote, filter);
                        break;
                    case REJECTREASONID:
                        expression = DbUtil.createLongExpression(stockTransIm1.rejectReasonId, filter);
                        break;
                    case REJECTUSERID:
                        expression = DbUtil.createLongExpression(stockTransIm1.rejectUserId, filter);
                        break;
                    case REQUESTID:
                        expression = DbUtil.createStringExpression(stockTransIm1.requestId, filter);
                        break;
                    case STOCKBASE:
                        expression = DbUtil.createStringExpression(stockTransIm1.stockBase, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransIm1.stockTransId, filter);
                        break;
                    case STOCKTRANSSTATUS:
                        expression = DbUtil.createLongExpression(stockTransIm1.stockTransStatus, filter);
                        break;
                    case STOCKTRANSTYPE:
                        expression = DbUtil.createLongExpression(stockTransIm1.stockTransType, filter);
                        break;
                    case SYNSTATUS:
                        expression = DbUtil.createStringExpression(stockTransIm1.synStatus, filter);
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(stockTransIm1.toOwnerId, filter);
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTransIm1.toOwnerType, filter);
                        break;
                    case TOTALDEBIT:
                        expression = DbUtil.createLongExpression(stockTransIm1.totalDebit, filter);
                        break;
                    case TRANSPORT:
                        expression = DbUtil.createLongExpression(stockTransIm1.transport, filter);
                        break;
                    case TROUBLETYPE:
                        expression = DbUtil.createStringExpression(stockTransIm1.troubleType, filter);
                        break;
                    case VALID:
                        expression = DbUtil.createLongExpression(stockTransIm1.valid, filter);
                        break;
                    case WARRANTYSTOCK:
                        expression = DbUtil.createLongExpression(stockTransIm1.warrantyStock, filter);
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

    public int updateStatusStockTrans(Long stockTransID, Long status) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE bccs_im.stock_trans ");
        sql.append("         SET stock_trans_status = #status");
        sql.append("         WHERE stock_trans_id = #stockTransID ");
        Query query = emIM.createNativeQuery(sql.toString());
        query.setParameter("status", status);
        query.setParameter("stockTransID", stockTransID);
        return query.executeUpdate();
    }
}
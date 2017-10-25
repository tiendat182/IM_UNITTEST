package com.viettel.bccs.inventory.repo;

import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockRequestOrderTrans.COLUMNS;
import com.viettel.bccs.inventory.model.QStockRequestOrderTrans;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

public class StockRequestOrderTransRepoImpl implements StockRequestOrderTransRepoCustom {

    public static final Logger logger = Logger.getLogger(StockRequestOrderTransRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockRequestOrderTrans stockRequestOrderTrans = QStockRequestOrderTrans.stockRequestOrderTrans;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockRequestOrderTrans.createDatetime, filter);
                        break;
                    case ERRORCODE:
                        expression = DbUtil.createStringExpression(stockRequestOrderTrans.errorCode, filter);
                        break;
                    case ERRORCODEDESCRIPTION:
                        expression = DbUtil.createStringExpression(stockRequestOrderTrans.errorCodeDescription, filter);
                        break;
                    case FROMOWNERID:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.fromOwnerId, filter);
                        break;
                    case FROMOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.fromOwnerType, filter);
                        break;
                    case RETRY:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.retry, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.type, filter);
                        break;
                    case STOCKREQUESTORDERID:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.stockRequestOrderId, filter);
                        break;
                    case STOCKREQUESTORDERTRANSID:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.stockRequestOrderTransId, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.stockTransId, filter);
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.toOwnerId, filter);
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.toOwnerType, filter);
                        break;
                    case STOCKTRANSTYPE:
                        expression = DbUtil.createLongExpression(stockRequestOrderTrans.stockTransType, filter);
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

}
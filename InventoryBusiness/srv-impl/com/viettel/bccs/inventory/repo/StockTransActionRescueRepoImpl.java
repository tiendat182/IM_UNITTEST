package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockTransActionRescue.COLUMNS;
import com.viettel.bccs.inventory.model.QStockTransActionRescue;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class StockTransActionRescueRepoImpl implements StockTransActionRescueRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransActionRescueRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransActionRescue stockTransActionRescue = QStockTransActionRescue.stockTransActionRescue;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONID:
                        expression = DbUtil.createLongExpression(stockTransActionRescue.actionId, filter); 
                        break;
                    case ACTIONSTAFFID:
                        expression = DbUtil.createLongExpression(stockTransActionRescue.actionStaffId, filter); 
                        break;
                    case ACTIONTYPE:
                        expression = DbUtil.createLongExpression(stockTransActionRescue.actionType, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockTransActionRescue.createDate, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(stockTransActionRescue.description, filter); 
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransActionRescue.stockTransId, filter); 
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
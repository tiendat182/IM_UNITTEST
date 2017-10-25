package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockTransDetailKcs.COLUMNS;
import com.viettel.bccs.inventory.model.QStockTransDetailKcs;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class StockTransDetailKcsRepoImpl implements StockTransDetailKcsRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransDetailKcsRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransDetailKcs stockTransDetailKcs = QStockTransDetailKcs.stockTransDetailKcs;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockTransDetailKcs.createDatetime, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockTransDetailKcs.quantity, filter); 
                        break;
                    case STOCKTRANSDETAILKCSID:
                        expression = DbUtil.createLongExpression(stockTransDetailKcs.stockTransDetailKcsId, filter); 
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
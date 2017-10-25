package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockIsdnTransDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QStockIsdnTransDetail;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class StockIsdnTransDetailRepoImpl implements StockIsdnTransDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(StockIsdnTransDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockIsdnTransDetail stockIsdnTransDetail = QStockIsdnTransDetail.stockIsdnTransDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDTIME:
                        expression = DbUtil.createDateExpression(stockIsdnTransDetail.createdTime, filter); 
                        break;
                    case FROMISDN:
                        expression = DbUtil.createStringExpression(stockIsdnTransDetail.fromIsdn, filter); 
                        break;
                    case LENGTHISDN:
                        expression = DbUtil.createLongExpression(stockIsdnTransDetail.lengthIsdn, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockIsdnTransDetail.quantity, filter); 
                        break;
                    case STOCKISDNTRANSDETAILID:
                        expression = DbUtil.createLongExpression(stockIsdnTransDetail.stockIsdnTransDetailId, filter); 
                        break;
                    case STOCKISDNTRANSID:
                        expression = DbUtil.createLongExpression(stockIsdnTransDetail.stockIsdnTransId, filter); 
                        break;
                    case TOISDN:
                        expression = DbUtil.createStringExpression(stockIsdnTransDetail.toIsdn, filter); 
                        break;
                    case TYPEISDN:
                        expression = DbUtil.createLongExpression(stockIsdnTransDetail.typeIsdn, filter); 
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
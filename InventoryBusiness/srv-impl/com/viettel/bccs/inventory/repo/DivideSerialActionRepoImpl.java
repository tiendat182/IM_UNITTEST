package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.DivideSerialAction.COLUMNS;
import com.viettel.bccs.inventory.model.QDivideSerialAction;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class DivideSerialActionRepoImpl implements DivideSerialActionRepoCustom {

    public static final Logger logger = Logger.getLogger(DivideSerialActionRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDivideSerialAction divideSerialAction = QDivideSerialAction.divideSerialAction;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(divideSerialAction.createDatetime, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(divideSerialAction.createUser, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(divideSerialAction.id, filter); 
                        break;
                    case SERIALCONTENT:
                        expression = DbUtil.createStringExpression(divideSerialAction.serialContent, filter); 
                        break;
                    case STOCKTRANSACTIONID:
                        expression = DbUtil.createStringExpression(divideSerialAction.stockTransActionId, filter);
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
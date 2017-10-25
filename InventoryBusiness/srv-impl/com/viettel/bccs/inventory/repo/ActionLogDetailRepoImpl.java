package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ActionLogDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QActionLogDetail;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ActionLogDetailRepoImpl implements ActionLogDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(ActionLogDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QActionLogDetail actionLogDetail = QActionLogDetail.actionLogDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONDATE:
                        expression = DbUtil.createDateExpression(actionLogDetail.actionDate, filter); 
                        break;
                    case ACTIONDETAILID:
                        expression = DbUtil.createLongExpression(actionLogDetail.actionDetailId, filter); 
                        break;
                    case ACTIONID:
                        expression = DbUtil.createLongExpression(actionLogDetail.actionId, filter); 
                        break;
                    case COLUMNNAME:
                        expression = DbUtil.createStringExpression(actionLogDetail.columnName, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(actionLogDetail.description, filter); 
                        break;
                    case NEWVALUE:
                        expression = DbUtil.createStringExpression(actionLogDetail.newValue, filter); 
                        break;
                    case OLDVALUE:
                        expression = DbUtil.createStringExpression(actionLogDetail.oldValue, filter); 
                        break;
                    case TABLENAME:
                        expression = DbUtil.createStringExpression(actionLogDetail.tableName, filter); 
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
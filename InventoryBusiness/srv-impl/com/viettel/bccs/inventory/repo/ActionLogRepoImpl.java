package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ActionLog.COLUMNS;
import com.viettel.bccs.inventory.model.QActionLog;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ActionLogRepoImpl implements ActionLogRepoCustom {

    public static final Logger logger = Logger.getLogger(ActionLogRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QActionLog actionLog = QActionLog.actionLog;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONDATE:
                        expression = DbUtil.createDateExpression(actionLog.actionDate, filter); 
                        break;
                    case ACTIONID:
                        expression = DbUtil.createLongExpression(actionLog.actionId, filter); 
                        break;
                    case ACTIONIP:
                        expression = DbUtil.createStringExpression(actionLog.actionIp, filter); 
                        break;
                    case ACTIONTYPE:
                        expression = DbUtil.createStringExpression(actionLog.actionType, filter); 
                        break;
                    case ACTIONUSER:
                        expression = DbUtil.createStringExpression(actionLog.actionUser, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(actionLog.description, filter); 
                        break;
                    case OBJECTID:
                        expression = DbUtil.createLongExpression(actionLog.objectId, filter); 
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
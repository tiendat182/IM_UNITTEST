package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.SignFlow.COLUMNS;
import com.viettel.bccs.inventory.model.QSignFlow;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class SignFlowRepoImpl implements SignFlowRepoCustom {

    public static final Logger logger = Logger.getLogger(SignFlowRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QSignFlow signFlow = QSignFlow.signFlow;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(signFlow.createDate, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(signFlow.createUser, filter); 
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(signFlow.lastUpdateTime, filter); 
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(signFlow.lastUpdateUser, filter); 
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(signFlow.name, filter); 
                        break;
                    case SHOPCODE:
                        expression = DbUtil.createStringExpression(signFlow.shopCode, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(signFlow.shopId, filter); 
                        break;
                    case SIGNFLOWID:
                        expression = DbUtil.createLongExpression(signFlow.signFlowId, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(signFlow.status, filter); 
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
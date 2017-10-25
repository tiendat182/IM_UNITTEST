package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ApLockTransDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QApLockTransDetail;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ApLockTransDetailRepoImpl implements ApLockTransDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(ApLockTransDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QApLockTransDetail apLockTransDetail = QApLockTransDetail.apLockTransDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(apLockTransDetail.createDate, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(apLockTransDetail.id, filter); 
                        break;
                    case LOCKTYPE:
                        expression = DbUtil.createShortExpression(apLockTransDetail.lockType, filter); 
                        break;
                    case POSID:
                        expression = DbUtil.createLongExpression(apLockTransDetail.posId, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(apLockTransDetail.prodOfferId, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(apLockTransDetail.quantity, filter); 
                        break;
                    case REQUESTID:
                        expression = DbUtil.createStringExpression(apLockTransDetail.requestId, filter); 
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(apLockTransDetail.serial, filter); 
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
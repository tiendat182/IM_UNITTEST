package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.Reason.COLUMNS;
import com.viettel.bccs.inventory.model.QReason;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ReasonRepoImpl implements ReasonRepoCustom {

    public static final Logger logger = Logger.getLogger(ReasonRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QReason reason = QReason.reason;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case HAVEMONEY:
                        expression = DbUtil.createShortExpression(reason.haveMoney, filter); 
                        break;
                    case REASONCODE:
                        expression = DbUtil.createStringExpression(reason.reasonCode, filter); 
                        break;
                    case REASONDESCRIPTION:
                        expression = DbUtil.createStringExpression(reason.reasonDescription, filter); 
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(reason.reasonId, filter); 
                        break;
                    case REASONNAME:
                        expression = DbUtil.createStringExpression(reason.reasonName, filter); 
                        break;
                    case REASONSTATUS:
                        expression = DbUtil.createStringExpression(reason.reasonStatus, filter); 
                        break;
                    case REASONTYPE:
                        expression = DbUtil.createStringExpression(reason.reasonType, filter); 
                        break;
                    case SERVICE:
                        expression = DbUtil.createStringExpression(reason.service, filter); 
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
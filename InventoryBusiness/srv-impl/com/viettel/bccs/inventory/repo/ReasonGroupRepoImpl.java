package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ReasonGroup.COLUMNS;
import com.viettel.bccs.inventory.model.QReasonGroup;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ReasonGroupRepoImpl implements ReasonGroupRepoCustom {

    public static final Logger logger = Logger.getLogger(ReasonGroupRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QReasonGroup reasonGroup = QReasonGroup.reasonGroup;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(reasonGroup.description, filter); 
                        break;
                    case REASONGROUPCODE:
                        expression = DbUtil.createStringExpression(reasonGroup.reasonGroupCode, filter); 
                        break;
                    case REASONGROUPID:
                        expression = DbUtil.createLongExpression(reasonGroup.reasonGroupId, filter); 
                        break;
                    case REASONGROUPNAME:
                        expression = DbUtil.createStringExpression(reasonGroup.reasonGroupName, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(reasonGroup.status, filter); 
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
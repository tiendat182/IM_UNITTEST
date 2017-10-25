package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.NumberActionDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QNumberActionDetail;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class NumberActionDetailRepoImpl implements NumberActionDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(NumberActionDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QNumberActionDetail numberActionDetail = QNumberActionDetail.numberActionDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case COLUMNNAME:
                        expression = DbUtil.createStringExpression(numberActionDetail.columnName, filter); 
                        break;
                    case NEWDETAILVALUE:
                        expression = DbUtil.createStringExpression(numberActionDetail.newDetailValue, filter); 
                        break;
                    case NEWVALUE:
                        expression = DbUtil.createStringExpression(numberActionDetail.newValue, filter); 
                        break;
                    case NUMBERACTIONDETAILID:
                        expression = DbUtil.createLongExpression(numberActionDetail.numberActionDetailId, filter); 
                        break;
                    case NUMBERACTIONID:
                        expression = DbUtil.createLongExpression(numberActionDetail.numberActionId, filter); 
                        break;
                    case OLDDETAILVALUE:
                        expression = DbUtil.createStringExpression(numberActionDetail.oldDetailValue, filter); 
                        break;
                    case OLDVALUE:
                        expression = DbUtil.createStringExpression(numberActionDetail.oldValue, filter); 
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
package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.GroupFilterRule.COLUMNS;
import com.viettel.bccs.inventory.model.QGroupFilterRule;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class GroupFilterRuleRepoImpl implements GroupFilterRuleRepoCustom {

    public static final Logger logger = Logger.getLogger(GroupFilterRuleRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QGroupFilterRule groupFilterRule = QGroupFilterRule.groupFilterRule;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case GROUPTYPE:
                        expression = DbUtil.createStringExpression(groupFilterRule.groupType, filter);
                        break;
                    case GROUPFILTERRULECODE:
                        expression = DbUtil.createStringExpression(groupFilterRule.groupFilterRuleCode, filter);
                        break;
                    case GROUPFILTERRULEID:
                        expression = DbUtil.createLongExpression(groupFilterRule.groupFilterRuleId, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(groupFilterRule.name, filter);
                        break;
                    case NOTES:
                        expression = DbUtil.createStringExpression(groupFilterRule.notes, filter);
                        break;
                    case PARENTID:
                        expression = DbUtil.createLongExpression(groupFilterRule.parentId, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(groupFilterRule.telecomServiceId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createCharacterExpression(groupFilterRule.status, filter);
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
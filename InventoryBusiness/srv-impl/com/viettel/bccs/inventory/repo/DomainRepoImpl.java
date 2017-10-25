package com.viettel.bccs.inventory.repo;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.Domain.COLUMNS;
import com.viettel.bccs.inventory.model.QDomain;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import java.util.List;
public class DomainRepoImpl implements DomainRepoCustom {

    public static final Logger logger = Logger.getLogger(DomainRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDomain domain = QDomain.domain1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(domain.description, filter); 
                        break;
                    case DOMAIN:
                        expression = DbUtil.createStringExpression(domain.domain, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(domain.id, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(domain.status, filter); 
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
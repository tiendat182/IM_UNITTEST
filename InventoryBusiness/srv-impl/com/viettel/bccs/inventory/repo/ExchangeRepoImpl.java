package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.Exchange.COLUMNS;
import com.viettel.bccs.inventory.model.QExchange;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ExchangeRepoImpl implements ExchangeRepoCustom {

    public static final Logger logger = Logger.getLogger(ExchangeRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QExchange exchange = QExchange.exchange;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(exchange.description, filter); 
                        break;
                    case EXCHANGEID:
                        expression = DbUtil.createLongExpression(exchange.exchangeId, filter); 
                        break;
                    case MAXGROUP:
                        expression = DbUtil.createLongExpression(exchange.maxGroup, filter); 
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(exchange.name, filter); 
                        break;
                    case SERVERID:
                        expression = DbUtil.createLongExpression(exchange.serverId, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(exchange.status, filter); 
                        break;
                    case TYPEID:
                        expression = DbUtil.createLongExpression(exchange.typeId, filter); 
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
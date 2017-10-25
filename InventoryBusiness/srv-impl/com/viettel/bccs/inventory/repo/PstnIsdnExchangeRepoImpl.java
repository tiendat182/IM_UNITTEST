package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.PstnIsdnExchange.COLUMNS;
import com.viettel.bccs.inventory.model.QPstnIsdnExchange;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class PstnIsdnExchangeRepoImpl implements PstnIsdnExchangeRepoCustom {

    public static final Logger logger = Logger.getLogger(PstnIsdnExchangeRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPstnIsdnExchange pstnIsdnExchange = QPstnIsdnExchange.pstnIsdnExchange;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(pstnIsdnExchange.createdate, filter); 
                        break;
                    case EXCHANGEID:
                        expression = DbUtil.createLongExpression(pstnIsdnExchange.exchangeId, filter); 
                        break;
                    case POCODE:
                        expression = DbUtil.createStringExpression(pstnIsdnExchange.poCode, filter); 
                        break;
                    case PREFIX:
                        expression = DbUtil.createStringExpression(pstnIsdnExchange.prefix, filter); 
                        break;
                    case PSTNISDNEXCHANGEID:
                        expression = DbUtil.createLongExpression(pstnIsdnExchange.pstnIsdnExchangeId, filter); 
                        break;
                    case TYPE:
                        expression = DbUtil.createStringExpression(pstnIsdnExchange.type, filter); 
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
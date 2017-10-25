package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockSimInfo.COLUMNS;
import com.viettel.bccs.inventory.model.QStockSimInfo;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class StockSimInfoRepoImpl implements StockSimInfoRepoCustom {

    public static final Logger logger = Logger.getLogger(StockSimInfoRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockSimInfo stockSimInfo = QStockSimInfo.stockSimInfo;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case A3A8:
                        expression = DbUtil.createStringExpression(stockSimInfo.a3a8, filter); 
                        break;
                    case ADM1:
                        expression = DbUtil.createStringExpression(stockSimInfo.adm1, filter); 
                        break;
                    case AUCREGDATE:
                        expression = DbUtil.createDateExpression(stockSimInfo.aucRegDate, filter); 
                        break;
                    case AUCREMOVEDATE:
                        expression = DbUtil.createDateExpression(stockSimInfo.aucRemoveDate, filter); 
                        break;
                    case AUCSTATUS:
                        expression = DbUtil.createLongExpression(stockSimInfo.aucStatus, filter); 
                        break;
                    case CONNECTIONDATE:
                        expression = DbUtil.createDateExpression(stockSimInfo.connectionDate, filter); 
                        break;
                    case CONNECTIONSTATUS:
                        expression = DbUtil.createLongExpression(stockSimInfo.connectionStatus, filter); 
                        break;
                    case CONNECTIONTYPE:
                        expression = DbUtil.createStringExpression(stockSimInfo.connectionType, filter); 
                        break;
                    case EKI:
                        expression = DbUtil.createStringExpression(stockSimInfo.eki, filter); 
                        break;
                    case HLRID:
                        expression = DbUtil.createStringExpression(stockSimInfo.hlrId, filter); 
                        break;
                    case HLRREGDATE:
                        expression = DbUtil.createDateExpression(stockSimInfo.hlrRegDate, filter); 
                        break;
                    case HLRREMOVEDATE:
                        expression = DbUtil.createDateExpression(stockSimInfo.hlrRemoveDate, filter); 
                        break;
                    case HLRSTATUS:
                        expression = DbUtil.createLongExpression(stockSimInfo.hlrStatus, filter); 
                        break;
                    case ICCID:
                        expression = DbUtil.createStringExpression(stockSimInfo.iccid, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockSimInfo.id, filter); 
                        break;
                    case IMSI:
                        expression = DbUtil.createStringExpression(stockSimInfo.imsi, filter); 
                        break;
                    case KIND:
                        expression = DbUtil.createStringExpression(stockSimInfo.kind, filter); 
                        break;
                    case PIN:
                        expression = DbUtil.createStringExpression(stockSimInfo.pin, filter); 
                        break;
                    case PIN2:
                        expression = DbUtil.createStringExpression(stockSimInfo.pin2, filter); 
                        break;
                    case PUK:
                        expression = DbUtil.createStringExpression(stockSimInfo.puk, filter); 
                        break;
                    case PUK2:
                        expression = DbUtil.createStringExpression(stockSimInfo.puk2, filter); 
                        break;
                    case SIMTYPE:
                        expression = DbUtil.createStringExpression(stockSimInfo.simType, filter); 
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
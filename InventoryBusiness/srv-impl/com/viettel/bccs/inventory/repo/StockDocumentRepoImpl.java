package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockDocument.COLUMNS;
import com.viettel.bccs.inventory.model.QStockDocument;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class StockDocumentRepoImpl implements StockDocumentRepoCustom {

    public static final Logger logger = Logger.getLogger(StockDocumentRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockDocument stockDocument = QStockDocument.stockDocument;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONCODE:
                        expression = DbUtil.createStringExpression(stockDocument.actionCode, filter); 
                        break;
                    case ACTIONID:
                        expression = DbUtil.createLongExpression(stockDocument.actionId, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockDocument.createDate, filter); 
                        break;
//                    case DELIVERYRECORDS:
//                        expression = DbUtil.createStringExpression(stockDocument.deliveryRecords, filter);
//                        break;
                    case DELIVERYRECORDSNAME:
                        expression = DbUtil.createStringExpression(stockDocument.deliveryRecordsName, filter); 
                        break;
                    case DESTROYDATE:
                        expression = DbUtil.createDateExpression(stockDocument.destroyDate, filter); 
                        break;
                    case DESTROYUSER:
                        expression = DbUtil.createStringExpression(stockDocument.destroyUser, filter); 
                        break;
//                    case EXPORTNOTE:
//                        expression = DbUtil.createStringExpression(stockDocument.exportNote, filter);
//                        break;
                    case EXPORTNOTENAME:
                        expression = DbUtil.createStringExpression(stockDocument.exportNoteName, filter); 
                        break;
                    case FROMSHOPCODE:
                        expression = DbUtil.createStringExpression(stockDocument.fromShopCode, filter); 
                        break;
                    case REASON:
                        expression = DbUtil.createStringExpression(stockDocument.reason, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createShortExpression(stockDocument.status, filter); 
                        break;
                    case STOCKDOCUMENTID:
                        expression = DbUtil.createLongExpression(stockDocument.stockDocumentId, filter); 
                        break;
                    case TOSHOPCODE:
                        expression = DbUtil.createStringExpression(stockDocument.toShopCode, filter); 
                        break;
                    case USERCREATE:
                        expression = DbUtil.createStringExpression(stockDocument.userCreate, filter); 
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
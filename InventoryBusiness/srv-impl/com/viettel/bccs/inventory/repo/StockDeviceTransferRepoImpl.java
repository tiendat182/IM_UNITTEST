package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.QStockDeviceTransfer;
import com.viettel.bccs.inventory.model.StockDeviceTransfer.COLUMNS;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import java.util.List;
public class StockDeviceTransferRepoImpl implements StockDeviceTransferRepoCustom {

    public static final Logger logger = Logger.getLogger(StockDeviceTransferRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockDeviceTransfer stockDeviceTransfer = QStockDeviceTransfer.stockDeviceTransfer;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockDeviceTransfer.createDate, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockDeviceTransfer.createUser, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockDeviceTransfer.id, filter); 
                        break;
                    case NEWPRODOFFERID:
                        expression = DbUtil.createLongExpression(stockDeviceTransfer.newProdOfferId, filter); 
                        break;
                    case NEWSTATEID:
                        expression = DbUtil.createShortExpression(stockDeviceTransfer.newStateId, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockDeviceTransfer.prodOfferId, filter); 
                        break;
                    case STATEID:
                        expression = DbUtil.createShortExpression(stockDeviceTransfer.stateId, filter); 
                        break;
                    case STOCKREQUESTID:
                        expression = DbUtil.createLongExpression(stockDeviceTransfer.stockRequestId, filter); 
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
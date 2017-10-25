package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.InvoiceActionLog.COLUMNS;
import com.viettel.bccs.inventory.model.QInvoiceActionLog;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class InvoiceActionLogRepoImpl implements InvoiceActionLogRepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceActionLogRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceActionLog invoiceActionLog = QInvoiceActionLog.invoiceActionLog;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONTYPE:
                        expression = DbUtil.createStringExpression(invoiceActionLog.actionType, filter); 
                        break;
                    case APPROVEDATE:
                        expression = DbUtil.createDateExpression(invoiceActionLog.approveDate, filter); 
                        break;
                    case APPROVEUSER:
                        expression = DbUtil.createStringExpression(invoiceActionLog.approveUser, filter); 
                        break;
                    case BLOCKNO:
                        expression = DbUtil.createStringExpression(invoiceActionLog.blockNo, filter); 
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceActionLog.createDatetime, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceActionLog.createUser, filter); 
                        break;
                    case CURINVOICE:
                        expression = DbUtil.createLongExpression(invoiceActionLog.curInvoice, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(invoiceActionLog.description, filter); 
                        break;
                    case FROMINVOICE:
                        expression = DbUtil.createLongExpression(invoiceActionLog.fromInvoice, filter); 
                        break;
                    case FROMSHOPID:
                        expression = DbUtil.createLongExpression(invoiceActionLog.fromShopId, filter); 
                        break;
                    case FROMSTAFFID:
                        expression = DbUtil.createLongExpression(invoiceActionLog.fromStaffId, filter); 
                        break;
                    case INVOICEACTIONID:
                        expression = DbUtil.createLongExpression(invoiceActionLog.invoiceActionId, filter); 
                        break;
                    case INVOICELISTID:
                        expression = DbUtil.createLongExpression(invoiceActionLog.invoiceListId, filter); 
                        break;
                    case SERIALNO:
                        expression = DbUtil.createStringExpression(invoiceActionLog.serialNo, filter); 
                        break;
                    case TOINVOICE:
                        expression = DbUtil.createLongExpression(invoiceActionLog.toInvoice, filter); 
                        break;
                    case TOSHOPID:
                        expression = DbUtil.createLongExpression(invoiceActionLog.toShopId, filter); 
                        break;
                    case TOSTAFFID:
                        expression = DbUtil.createLongExpression(invoiceActionLog.toStaffId, filter); 
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
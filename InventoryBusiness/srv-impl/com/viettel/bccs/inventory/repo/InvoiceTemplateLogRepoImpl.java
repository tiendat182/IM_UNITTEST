package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.InvoiceTemplateLog.COLUMNS;
import com.viettel.bccs.inventory.model.QInvoiceTemplateLog;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class InvoiceTemplateLogRepoImpl implements InvoiceTemplateLogRepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceTemplateLogRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceTemplateLog invoiceTemplateLog = QInvoiceTemplateLog.invoiceTemplateLog;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AFTERAMOUNT:
                        expression = DbUtil.createStringExpression(invoiceTemplateLog.afterAmount, filter); 
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceTemplateLog.createDatetime, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceTemplateLog.createUser, filter); 
                        break;
                    case FROMSHOPID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLog.fromShopId, filter); 
                        break;
                    case INVOICETEMPLATEID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLog.invoiceTemplateId, filter); 
                        break;
                    case INVOICETEMPLATELOGID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLog.invoiceTemplateLogId, filter); 
                        break;
                    case LOGTYPE:
                        expression = DbUtil.createStringExpression(invoiceTemplateLog.logType, filter); 
                        break;
                    case ORGAMOUNT:
                        expression = DbUtil.createLongExpression(invoiceTemplateLog.orgAmount, filter); 
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLog.ownerId, filter); 
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createStringExpression(invoiceTemplateLog.ownerType, filter); 
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLog.reasonId, filter); 
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
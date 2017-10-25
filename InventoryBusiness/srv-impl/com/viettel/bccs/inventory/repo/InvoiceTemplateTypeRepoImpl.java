package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.InvoiceTemplateType.COLUMNS;
import com.viettel.bccs.inventory.model.QInvoiceTemplateType;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class InvoiceTemplateTypeRepoImpl implements InvoiceTemplateTypeRepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceTemplateTypeRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceTemplateType invoiceTemplateType = QInvoiceTemplateType.invoiceTemplateType;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceTemplateType.createDatetime, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceTemplateType.createUser, filter); 
                        break;
                    case INVOICETEMPLATETYPEID:
                        expression = DbUtil.createLongExpression(invoiceTemplateType.invoiceTemplateTypeId, filter); 
                        break;
                    case INVOICETYPE:
                        expression = DbUtil.createStringExpression(invoiceTemplateType.invoiceType, filter); 
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(invoiceTemplateType.name, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(invoiceTemplateType.status, filter); 
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceTemplateType.updateDatetime, filter); 
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(invoiceTemplateType.updateUser, filter); 
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
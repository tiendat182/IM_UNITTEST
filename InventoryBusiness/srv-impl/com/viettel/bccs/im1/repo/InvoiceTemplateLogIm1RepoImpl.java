package com.viettel.bccs.im1.repo;

import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.model.InvoiceTemplateLogIm1.COLUMNS;
import com.viettel.bccs.im1.model.QInvoiceTemplateLogIm1;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

public class InvoiceTemplateLogIm1RepoImpl implements InvoiceTemplateLogIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceTemplateLogIm1RepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceTemplateLogIm1 invoiceTemplateLogIm1 = QInvoiceTemplateLogIm1.invoiceTemplateLogIm1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AMOUNTAPPLY:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.amountApply, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(invoiceTemplateLogIm1.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceTemplateLogIm1.createUser, filter);
                        break;
                    case FROMSHOPID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.fromShopId, filter);
                        break;
                    case INVOICETEMPLATEID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.invoiceTemplateId, filter);
                        break;
                    case INVOICETEMPLATELOGID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.invoiceTemplateLogId, filter);
                        break;
                    case LOGTYPE:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.logType, filter);
                        break;
                    case ORGAMOUNT:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.orgAmount, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.shopId, filter);
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.staffId, filter);
                        break;
                    case VALID:
                        expression = DbUtil.createLongExpression(invoiceTemplateLogIm1.valid, filter);
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
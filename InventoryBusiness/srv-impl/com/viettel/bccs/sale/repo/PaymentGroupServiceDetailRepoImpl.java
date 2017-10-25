package com.viettel.bccs.sale.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.sale.model.PaymentGroupServiceDetail.COLUMNS;
import com.viettel.bccs.sale.model.QPaymentGroupServiceDetail;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class PaymentGroupServiceDetailRepoImpl implements PaymentGroupServiceDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(PaymentGroupServiceDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPaymentGroupServiceDetail paymentGroupServiceDetail = QPaymentGroupServiceDetail.paymentGroupServiceDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ID:
                        expression = DbUtil.createLongExpression(paymentGroupServiceDetail.id, filter); 
                        break;
                    case MAXDAYDEBIT:
                        expression = DbUtil.createLongExpression(paymentGroupServiceDetail.maxDayDebit, filter); 
                        break;
                    case PAYMENTGROUPSERVICEID:
                        expression = DbUtil.createLongExpression(paymentGroupServiceDetail.paymentGroupServiceId, filter); 
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(paymentGroupServiceDetail.telecomServiceId, filter); 
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
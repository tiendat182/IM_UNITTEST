package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.model.QPaymentDebitIm1;
import com.viettel.bccs.sale.model.PaymentDebit.COLUMNS;
import com.viettel.bccs.sale.model.QPaymentDebit;
import com.viettel.bccs.sale.repo.PaymentDebitRepoCustom;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import java.util.List;

public class PaymentDebitIm1RepoImpl implements PaymentDebitIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(PaymentDebitRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPaymentDebitIm1 paymentDebit = QPaymentDebitIm1.paymentDebit;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(paymentDebit.createDate, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(paymentDebit.createUser, filter); 
                        break;
                    case LASTUPDATEDATE:
                        expression = DbUtil.createDateExpression(paymentDebit.lastUpdateDate, filter); 
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(paymentDebit.lastUpdateUser, filter); 
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(paymentDebit.ownerId, filter); 
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(paymentDebit.ownerType, filter); 
                        break;
                    case PAYMENTDEBITID:
                        expression = DbUtil.createLongExpression(paymentDebit.paymentDebitId, filter); 
                        break;
                    case PAYMENTGROUPID:
                        expression = DbUtil.createLongExpression(paymentDebit.paymentGroupId, filter); 
                        break;
                    case PAYMENTGROUPSERVICEID:
                        expression = DbUtil.createLongExpression(paymentDebit.paymentGroupServiceId, filter); 
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
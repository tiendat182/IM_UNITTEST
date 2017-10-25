package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.DepositDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QDepositDetail;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class DepositDetailRepoImpl implements DepositDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(DepositDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDepositDetail depositDetail = QDepositDetail.depositDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AMOUNT:
                        expression = DbUtil.createBigDecimalExpression(depositDetail.amount, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(depositDetail.createDate, filter); 
                        break;
                    case DEPOSITDETAILID:
                        expression = DbUtil.createLongExpression(depositDetail.depositDetailId, filter); 
                        break;
                    case DEPOSITID:
                        expression = DbUtil.createLongExpression(depositDetail.depositId, filter); 
                        break;
                    case DEPOSITTYPE:
                        expression = DbUtil.createStringExpression(depositDetail.depositType, filter); 
                        break;
                    case EXPIREDDATE:
                        expression = DbUtil.createDateExpression(depositDetail.expiredDate, filter); 
                        break;
                    case POSID:
                        expression = DbUtil.createLongExpression(depositDetail.posId, filter); 
                        break;
                    case PRICE:
                        expression = DbUtil.createBigDecimalExpression(depositDetail.price, filter); 
                        break;
                    case PRICEID:
                        expression = DbUtil.createLongExpression(depositDetail.priceId, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(depositDetail.quantity, filter); 
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(depositDetail.serial, filter); 
                        break;
                    case STOCKMODELID:
                        expression = DbUtil.createLongExpression(depositDetail.stockModelId, filter); 
                        break;
                    case SUPPLYMONTH:
                        expression = DbUtil.createLongExpression(depositDetail.supplyMonth, filter); 
                        break;
                    case SUPPLYPROGRAM:
                        expression = DbUtil.createStringExpression(depositDetail.supplyProgram, filter); 
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
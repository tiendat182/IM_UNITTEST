package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.Deposit.COLUMNS;
import com.viettel.bccs.inventory.model.QDeposit;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class DepositRepoImpl implements DepositRepoCustom {

    public static final Logger logger = Logger.getLogger(DepositRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDeposit deposit = QDeposit.deposit;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ADDRESS:
                        expression = DbUtil.createStringExpression(deposit.address, filter); 
                        break;
                    case AMOUNT:
                        expression = DbUtil.createLongExpression(deposit.amount, filter); 
                        break;
                    case BRANCHID:
                        expression = DbUtil.createStringExpression(deposit.branchId, filter); 
                        break;
                    case CONTRACTNO:
                        expression = DbUtil.createStringExpression(deposit.contractNo, filter); 
                        break;
                    case CREATEBY:
                        expression = DbUtil.createStringExpression(deposit.createBy, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(deposit.createDate, filter); 
                        break;
                    case CUSTNAME:
                        expression = DbUtil.createStringExpression(deposit.custName, filter); 
                        break;
                    case DELIVERID:
                        expression = DbUtil.createLongExpression(deposit.deliverId, filter); 
                        break;
                    case DEPOSITID:
                        expression = DbUtil.createLongExpression(deposit.depositId, filter); 
                        break;
                    case DEPOSITTYPEID:
                        expression = DbUtil.createLongExpression(deposit.depositTypeId, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(deposit.description, filter); 
                        break;
                    case IDNO:
                        expression = DbUtil.createStringExpression(deposit.idNo, filter); 
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(deposit.isdn, filter); 
                        break;
                    case PRIMARYACCOUNT:
                        expression = DbUtil.createStringExpression(deposit.primaryAccount, filter); 
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(deposit.reasonId, filter); 
                        break;
                    case RECEIPTID:
                        expression = DbUtil.createLongExpression(deposit.receiptId, filter); 
                        break;
                    case REQUESTID:
                        expression = DbUtil.createStringExpression(deposit.requestId, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(deposit.shopId, filter); 
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(deposit.staffId, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(deposit.status, filter); 
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(deposit.stockTransId, filter); 
                        break;
                    case SUBID:
                        expression = DbUtil.createLongExpression(deposit.subId, filter); 
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(deposit.telecomServiceId, filter); 
                        break;
                    case TIN:
                        expression = DbUtil.createStringExpression(deposit.tin, filter); 
                        break;
                    case TYPE:
                        expression = DbUtil.createStringExpression(deposit.type, filter); 
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
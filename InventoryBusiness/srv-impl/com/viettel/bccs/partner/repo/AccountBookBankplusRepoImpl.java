package com.viettel.bccs.partner.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.partner.model.AccountBookBankplus.COLUMNS;
import com.viettel.bccs.partner.model.QAccountBookBankplus;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class AccountBookBankplusRepoImpl implements AccountBookBankplusRepoCustom {

    public static final Logger logger = Logger.getLogger(AccountBookBankplusRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QAccountBookBankplus accountBookBankplus = QAccountBookBankplus.accountBookBankplus;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNTID:
                        expression = DbUtil.createLongExpression(accountBookBankplus.accountId, filter); 
                        break;
                    case AMOUNTREQUEST:
                        expression = DbUtil.createLongExpression(accountBookBankplus.amountRequest, filter);
                        break;
                    case APPNAME:
                        expression = DbUtil.createStringExpression(accountBookBankplus.appName, filter); 
                        break;
                    case BANKCODE:
                        expression = DbUtil.createStringExpression(accountBookBankplus.bankCode, filter); 
                        break;
                    case CLOSINGBALANCE:
                        expression = DbUtil.createLongExpression(accountBookBankplus.closingBalance, filter);
                        break;
                    case CMREQUEST:
                        expression = DbUtil.createStringExpression(accountBookBankplus.cmRequest, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(accountBookBankplus.createDate, filter); 
                        break;
                    case DEBITREQUEST:
                        expression = DbUtil.createLongExpression(accountBookBankplus.debitRequest, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(accountBookBankplus.description, filter); 
                        break;
                    case DESCRIPTIONBANKPLUS:
                        expression = DbUtil.createStringExpression(accountBookBankplus.descriptionBankplus, filter); 
                        break;
                    case IPADDRESS:
                        expression = DbUtil.createStringExpression(accountBookBankplus.ipAddress, filter); 
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(accountBookBankplus.isdn, filter); 
                        break;
                    case ISDNBANKPLUS:
                        expression = DbUtil.createStringExpression(accountBookBankplus.isdnBankPlus, filter); 
                        break;
                    case OPENINGBALANCE:
                        expression = DbUtil.createLongExpression(accountBookBankplus.openingBalance, filter);
                        break;
                    case REALTRANSDATE:
                        expression = DbUtil.createDateExpression(accountBookBankplus.realTransDate, filter); 
                        break;
                    case RECEIPTID:
                        expression = DbUtil.createLongExpression(accountBookBankplus.receiptId, filter); 
                        break;
                    case REQUESTID:
                        expression = DbUtil.createLongExpression(accountBookBankplus.requestId, filter);
                        break;
                    case REQUESTTYPE:
                        expression = DbUtil.createLongExpression(accountBookBankplus.requestType, filter);
                        break;
                    case RESPONSECODE:
                        expression = DbUtil.createStringExpression(accountBookBankplus.responseCode, filter); 
                        break;
                    case RETURNDATE:
                        expression = DbUtil.createDateExpression(accountBookBankplus.returnDate, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(accountBookBankplus.status, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(accountBookBankplus.stockTransId, filter); 
                        break;
                    case TRANSACTIONCP:
                        expression = DbUtil.createStringExpression(accountBookBankplus.transactionCp, filter); 
                        break;
                    case USERREQUEST:
                        expression = DbUtil.createStringExpression(accountBookBankplus.userRequest, filter); 
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
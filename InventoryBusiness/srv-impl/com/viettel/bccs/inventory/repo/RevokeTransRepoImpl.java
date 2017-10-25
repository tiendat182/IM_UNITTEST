package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.QRevokeTrans;
import com.viettel.bccs.inventory.model.RevokeTrans.COLUMNS;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import java.util.List;
public class RevokeTransRepoImpl implements RevokeTransRepoCustom {

    public static final Logger logger = Logger.getLogger(RevokeTransRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QRevokeTrans revokeTrans = QRevokeTrans.revokeTrans;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNTID:
                        expression = DbUtil.createStringExpression(revokeTrans.accountId, filter); 
                        break;
                    case AMOUNTNEEDREVOKE:
                        expression = DbUtil.createLongExpression(revokeTrans.amountNeedRevoke, filter); 
                        break;
                    case AMOUNTREVOKED:
                        expression = DbUtil.createLongExpression(revokeTrans.amountRevoked, filter); 
                        break;
                    case CMREQUEST:
                        expression = DbUtil.createStringExpression(revokeTrans.cmRequest, filter); 
                        break;
                    case CONTRACTNO:
                        expression = DbUtil.createStringExpression(revokeTrans.contractNo, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(revokeTrans.createDate, filter); 
                        break;
                    case POSID:
                        expression = DbUtil.createLongExpression(revokeTrans.posId, filter); 
                        break;
                    case REVOKETRANSID:
                        expression = DbUtil.createLongExpression(revokeTrans.revokeTransId, filter); 
                        break;
                    case SALETRANSDATE:
                        expression = DbUtil.createDateExpression(revokeTrans.saleTransDate, filter); 
                        break;
                    case SALETRANSID:
                        expression = DbUtil.createLongExpression(revokeTrans.saleTransId, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(revokeTrans.shopId, filter); 
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(revokeTrans.staffId, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(revokeTrans.status, filter); 
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(revokeTrans.stockTransId, filter); 
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(revokeTrans.telecomServiceId, filter); 
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
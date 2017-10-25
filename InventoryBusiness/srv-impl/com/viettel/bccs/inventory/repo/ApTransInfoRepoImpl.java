package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ApTransInfo.COLUMNS;
import com.viettel.bccs.inventory.model.QApTransInfo;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ApTransInfoRepoImpl implements ApTransInfoRepoCustom {

    public static final Logger logger = Logger.getLogger(ApTransInfoRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QApTransInfo apTransInfo = QApTransInfo.apTransInfo;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATESHOPID:
                        expression = DbUtil.createLongExpression(apTransInfo.createShopId, filter); 
                        break;
                    case CREATESTAFFID:
                        expression = DbUtil.createLongExpression(apTransInfo.createStaffId, filter); 
                        break;
                    case EXTENDLIST:
                        expression = DbUtil.createStringExpression(apTransInfo.extendList, filter); 
                        break;
                    case FEELIST:
                        expression = DbUtil.createStringExpression(apTransInfo.feeList, filter); 
                        break;
                    case GOODLOCKED:
                        expression = DbUtil.createShortExpression(apTransInfo.goodLocked, filter); 
                        break;
                    case IPLIST:
                        expression = DbUtil.createStringExpression(apTransInfo.ipList, filter); 
                        break;
                    case LOGDATE:
                        expression = DbUtil.createDateExpression(apTransInfo.logDate, filter); 
                        break;
                    case POSID:
                        expression = DbUtil.createStringExpression(apTransInfo.posId, filter); 
                        break;
                    case PSTNLIST:
                        expression = DbUtil.createStringExpression(apTransInfo.pstnList, filter); 
                        break;
                    case QTYISSUEDECREASE:
                        expression = DbUtil.createShortExpression(apTransInfo.qtyIssueDecrease, filter); 
                        break;
                    case QTYSUPPLYISSUEDECREASE:
                        expression = DbUtil.createShortExpression(apTransInfo.qtySupplyIssueDecrease, filter); 
                        break;
                    case REQUESTID:
                        expression = DbUtil.createStringExpression(apTransInfo.requestId, filter); 
                        break;
                    case RESPONSE:
                        expression = DbUtil.createStringExpression(apTransInfo.response, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(apTransInfo.shopId, filter); 
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(apTransInfo.staffId, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createShortExpression(apTransInfo.status, filter); 
                        break;
                    case STOCKLIST:
                        expression = DbUtil.createStringExpression(apTransInfo.stockList, filter); 
                        break;
                    case TOSTRING:
                        expression = DbUtil.createStringExpression(apTransInfo.toString, filter); 
                        break;
                    case TRANSTYPE:
                        expression = DbUtil.createShortExpression(apTransInfo.transType, filter); 
                        break;
                    case VERSION:
                        expression = DbUtil.createStringExpression(apTransInfo.version, filter); 
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
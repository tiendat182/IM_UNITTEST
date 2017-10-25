package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ReportChangeGoods.COLUMNS;
import com.viettel.bccs.inventory.model.QReportChangeGoods;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ReportChangeGoodsRepoImpl implements ReportChangeGoodsRepoCustom {

    public static final Logger logger = Logger.getLogger(ReportChangeGoodsRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QReportChangeGoods reportChangeGoods = QReportChangeGoods.reportChangeGoods;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(reportChangeGoods.createDate, filter); 
                        break;
                    case DATESENDSMS:
                        expression = DbUtil.createDateExpression(reportChangeGoods.dateSendSms, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(reportChangeGoods.id, filter); 
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(reportChangeGoods.isdn, filter); 
                        break;
                    case PRODOFFERCODE:
                        expression = DbUtil.createStringExpression(reportChangeGoods.prodOfferCode, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(reportChangeGoods.prodOfferId, filter); 
                        break;
                    case PRODOFFERNAME:
                        expression = DbUtil.createStringExpression(reportChangeGoods.prodOfferName, filter); 
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(reportChangeGoods.reasonId, filter); 
                        break;
                    case SERIALNEW:
                        expression = DbUtil.createStringExpression(reportChangeGoods.serialNew, filter); 
                        break;
                    case SERIALOLD:
                        expression = DbUtil.createStringExpression(reportChangeGoods.serialOld, filter); 
                        break;
                    case SHOPCODE:
                        expression = DbUtil.createStringExpression(reportChangeGoods.shopCode, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(reportChangeGoods.shopId, filter); 
                        break;
                    case SHOPNAME:
                        expression = DbUtil.createStringExpression(reportChangeGoods.shopName, filter); 
                        break;
                    case STAFFCODE:
                        expression = DbUtil.createStringExpression(reportChangeGoods.staffCode, filter); 
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(reportChangeGoods.staffId, filter); 
                        break;
                    case STAFFNAME:
                        expression = DbUtil.createStringExpression(reportChangeGoods.staffName, filter); 
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(reportChangeGoods.stockTransId, filter); 
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(reportChangeGoods.telecomServiceId, filter); 
                        break;
                    case TOTALSENDSMS:
                        expression = DbUtil.createLongExpression(reportChangeGoods.totalSendSms, filter); 
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
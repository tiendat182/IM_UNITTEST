package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockTotalAudit.COLUMNS;
import com.viettel.bccs.inventory.model.QStockTotalAudit;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class StockTotalAuditRepoImpl implements StockTotalAuditRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTotalAuditRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTotalAudit stockTotalAudit = QStockTotalAudit.stockTotalAudit;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AVAILABLEQUANTITY:
                        expression = DbUtil.createLongExpression(stockTotalAudit.availableQuantity, filter); 
                        break;
                    case AVAILABLEQUANTITYAF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.availableQuantityAf, filter); 
                        break;
                    case AVAILABLEQUANTITYBF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.availableQuantityBf, filter); 
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockTotalAudit.createDate, filter); 
                        break;
                    case CURRENTQUANTITY:
                        expression = DbUtil.createLongExpression(stockTotalAudit.currentQuantity, filter); 
                        break;
                    case CURRENTQUANTITYAF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.currentQuantityAf, filter); 
                        break;
                    case CURRENTQUANTITYBF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.currentQuantityBf, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(stockTotalAudit.description, filter); 
                        break;
                    case HANGQUANTITY:
                        expression = DbUtil.createLongExpression(stockTotalAudit.hangQuantity, filter); 
                        break;
                    case HANGQUANTITYAF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.hangQuantityAf, filter); 
                        break;
                    case HANGQUANTITYBF:
                        expression = DbUtil.createLongExpression(stockTotalAudit.hangQuantityBf, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.id, filter); 
                        break;
                    case OWNERCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.ownerCode, filter); 
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.ownerId, filter); 
                        break;
                    case OWNERNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.ownerName, filter); 
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTotalAudit.ownerType, filter); 
                        break;
                    case PRODOFFERCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.prodOfferCode, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.prodOfferId, filter); 
                        break;
                    case PRODOFFERNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.prodOfferName, filter); 
                        break;
                    case PRODOFFERTYPEID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.prodOfferTypeId, filter); 
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.reasonId, filter); 
                        break;
                    case REASONNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.reasonName, filter); 
                        break;
                    case SHOPCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCode, filter); 
                        break;
                    case SHOPCODELV1:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv1, filter); 
                        break;
                    case SHOPCODELV2:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv2, filter); 
                        break;
                    case SHOPCODELV3:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv3, filter); 
                        break;
                    case SHOPCODELV4:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv4, filter); 
                        break;
                    case SHOPCODELV5:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv5, filter); 
                        break;
                    case SHOPCODELV6:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopCodeLv6, filter); 
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopId, filter); 
                        break;
                    case SHOPIDLV1:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv1, filter); 
                        break;
                    case SHOPIDLV2:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv2, filter); 
                        break;
                    case SHOPIDLV3:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv3, filter); 
                        break;
                    case SHOPIDLV4:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv4, filter); 
                        break;
                    case SHOPIDLV5:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv5, filter); 
                        break;
                    case SHOPIDLV6:
                        expression = DbUtil.createLongExpression(stockTotalAudit.shopIdLv6, filter); 
                        break;
                    case SHOPNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopName, filter); 
                        break;
                    case SHOPNAMELV1:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv1, filter); 
                        break;
                    case SHOPNAMELV2:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv2, filter); 
                        break;
                    case SHOPNAMELV3:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv3, filter); 
                        break;
                    case SHOPNAMELV4:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv4, filter); 
                        break;
                    case SHOPNAMELV5:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv5, filter); 
                        break;
                    case SHOPNAMELV6:
                        expression = DbUtil.createStringExpression(stockTotalAudit.shopNameLv6, filter); 
                        break;
                    case SOURCETYPE:
                        expression = DbUtil.createLongExpression(stockTotalAudit.sourceType, filter); 
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.stateId, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockTotalAudit.status, filter); 
                        break;
                    case STICKCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.stickCode, filter); 
                        break;
                    case SYNSTATUS:
                        expression = DbUtil.createLongExpression(stockTotalAudit.synStatus, filter); 
                        break;
                    case TRANSCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.transCode, filter); 
                        break;
                    case TRANSID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.transId, filter); 
                        break;
                    case TRANSTYPE:
                        expression = DbUtil.createLongExpression(stockTotalAudit.transType, filter); 
                        break;
                    case USERCODE:
                        expression = DbUtil.createStringExpression(stockTotalAudit.userCode, filter); 
                        break;
                    case USERID:
                        expression = DbUtil.createLongExpression(stockTotalAudit.userId, filter); 
                        break;
                    case USERNAME:
                        expression = DbUtil.createStringExpression(stockTotalAudit.userName, filter); 
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
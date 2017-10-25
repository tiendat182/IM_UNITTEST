package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.QRevokeTransDetail;
import com.viettel.bccs.inventory.model.RevokeTransDetail.COLUMNS;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import java.util.List;
public class RevokeTransDetailRepoImpl implements RevokeTransDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(RevokeTransDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QRevokeTransDetail revokeTransDetail = QRevokeTransDetail.revokeTransDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(revokeTransDetail.createDate, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(revokeTransDetail.description, filter); 
                        break;
                    case ERRCODE:
                        expression = DbUtil.createStringExpression(revokeTransDetail.errCode, filter); 
                        break;
                    case OLDOWNERID:
                        expression = DbUtil.createLongExpression(revokeTransDetail.oldOwnerId, filter); 
                        break;
                    case OLDOWNERTYPE:
                        expression = DbUtil.createShortExpression(revokeTransDetail.oldOwnerType, filter); 
                        break;
                    case OLDSERIAL:
                        expression = DbUtil.createStringExpression(revokeTransDetail.oldSerial, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(revokeTransDetail.prodOfferId, filter); 
                        break;
                    case PRODOFFERTYPEID:
                        expression = DbUtil.createLongExpression(revokeTransDetail.prodOfferTypeId, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(revokeTransDetail.quantity, filter); 
                        break;
                    case REVOKEPRICE:
                        expression = DbUtil.createLongExpression(revokeTransDetail.revokePrice, filter); 
                        break;
                    case REVOKETRANSDETAILID:
                        expression = DbUtil.createLongExpression(revokeTransDetail.revokeTransDetailId, filter); 
                        break;
                    case REVOKETRANSID:
                        expression = DbUtil.createLongExpression(revokeTransDetail.revokeTransId, filter); 
                        break;
                    case REVOKEDSERIAL:
                        expression = DbUtil.createStringExpression(revokeTransDetail.revokedSerial, filter); 
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
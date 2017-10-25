package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.PartnerContractDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QPartnerContractDetail;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class PartnerContractDetailRepoImpl implements PartnerContractDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(PartnerContractDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPartnerContractDetail partnerContractDetail = QPartnerContractDetail.partnerContractDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(partnerContractDetail.createDate, filter); 
                        break;
                    case CURRENCYTYPE:
                        expression = DbUtil.createStringExpression(partnerContractDetail.currencyType, filter); 
                        break;
                    case ORDERCODE:
                        expression = DbUtil.createStringExpression(partnerContractDetail.orderCode, filter); 
                        break;
                    case PARTNERCONTRACTDETAILID:
                        expression = DbUtil.createLongExpression(partnerContractDetail.partnerContractDetailId, filter); 
                        break;
                    case PARTNERCONTRACTID:
                        expression = DbUtil.createLongExpression(partnerContractDetail.partnerContractId, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(partnerContractDetail.prodOfferId, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(partnerContractDetail.quantity, filter); 
                        break;
                    case STARTDATEWARRANTY:
                        expression = DbUtil.createDateExpression(partnerContractDetail.startDateWarranty, filter); 
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(partnerContractDetail.stateId, filter); 
                        break;
                    case UNITPRICE:
                        expression = DbUtil.createLongExpression(partnerContractDetail.unitPrice, filter); 
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
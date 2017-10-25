package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ProductErp.COLUMNS;
import com.viettel.bccs.inventory.model.QProductErp;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class ProductErpRepoImpl implements ProductErpRepoCustom {

    public static final Logger logger = Logger.getLogger(ProductErpRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QProductErp productErp = QProductErp.productErp;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(productErp.createDate, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(productErp.createUser, filter); 
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(productErp.ownerId, filter); 
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(productErp.ownerType, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(productErp.prodOfferId, filter); 
                        break;
                    case PRODUCTERPID:
                        expression = DbUtil.createLongExpression(productErp.productErpId, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(productErp.quantity, filter); 
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(productErp.updateDatetime, filter); 
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(productErp.updateUser, filter); 
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
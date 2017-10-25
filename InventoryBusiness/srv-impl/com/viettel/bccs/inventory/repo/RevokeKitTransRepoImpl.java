package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.RevokeKitTrans.COLUMNS;
import com.viettel.bccs.inventory.model.QRevokeKitTrans;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class RevokeKitTransRepoImpl implements RevokeKitTransRepoCustom {

    public static final Logger logger = Logger.getLogger(RevokeKitTransRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QRevokeKitTrans revokeKitTrans = QRevokeKitTrans.revokeKitTrans;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ADDMONEYDATE:
                        expression = DbUtil.createDateExpression(revokeKitTrans.addMoneyDate, filter);
                        break;
                    case ADDMONEYSTATUS:
                        expression = DbUtil.createLongExpression(revokeKitTrans.addMoneyStatus, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(revokeKitTrans.createDate, filter);
                        break;
                    case CREATESTAFFID:
                        expression = DbUtil.createLongExpression(revokeKitTrans.createStaffId, filter);
                        break;
                    case CREATESHOPID:
                        expression = DbUtil.createLongExpression(revokeKitTrans.createShopId, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(revokeKitTrans.id, filter);
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
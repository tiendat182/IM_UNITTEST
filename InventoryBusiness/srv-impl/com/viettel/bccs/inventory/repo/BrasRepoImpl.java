package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.Bras.COLUMNS;
import com.viettel.bccs.inventory.model.QBras;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class BrasRepoImpl implements BrasRepoCustom {

    public static final Logger logger = Logger.getLogger(BrasRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QBras bras = QBras.bras;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AAAIP:
                        expression = DbUtil.createStringExpression(bras.aaaIp, filter); 
                        break;
                    case BRASID:
                        expression = DbUtil.createLongExpression(bras.brasId, filter); 
                        break;
                    case CODE:
                        expression = DbUtil.createStringExpression(bras.code, filter); 
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(bras.description, filter); 
                        break;
                    case EQUIPMENTID:
                        expression = DbUtil.createLongExpression(bras.equipmentId, filter); 
                        break;
                    case IP:
                        expression = DbUtil.createStringExpression(bras.ip, filter); 
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(bras.name, filter); 
                        break;
                    case PORT:
                        expression = DbUtil.createStringExpression(bras.port, filter); 
                        break;
                    case SLOT:
                        expression = DbUtil.createStringExpression(bras.slot, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(bras.status, filter); 
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(bras.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(bras.createUser, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(bras.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(bras.updateUser, filter);
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
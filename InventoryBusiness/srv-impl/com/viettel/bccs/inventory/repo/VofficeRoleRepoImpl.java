package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.VofficeRole.COLUMNS;
import com.viettel.bccs.inventory.model.QVofficeRole;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
public class VofficeRoleRepoImpl implements VofficeRoleRepoCustom {

    public static final Logger logger = Logger.getLogger(VofficeRoleRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QVofficeRole vofficeRole = QVofficeRole.vofficeRole;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(vofficeRole.createDate, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(vofficeRole.createUser, filter); 
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(vofficeRole.lastUpdateTime, filter); 
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(vofficeRole.lastUpdateUser, filter); 
                        break;
                    case ROLECODE:
                        expression = DbUtil.createStringExpression(vofficeRole.roleCode, filter); 
                        break;
                    case ROLENAME:
                        expression = DbUtil.createStringExpression(vofficeRole.roleName, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(vofficeRole.status, filter); 
                        break;
                    case VOFFICEROLEID:
                        expression = DbUtil.createLongExpression(vofficeRole.vofficeRoleId, filter); 
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
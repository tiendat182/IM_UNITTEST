package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.QSignFlowDetail;
import com.viettel.bccs.inventory.model.SignFlowDetail.COLUMNS;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class SignFlowDetailRepoImpl implements SignFlowDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(SignFlowDetailRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QSignFlowDetail signFlowDetail = QSignFlowDetail.signFlowDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case EMAIL:
                        expression = DbUtil.createStringExpression(signFlowDetail.email, filter);
                        break;
                    case SIGNFLOWDETAILID:
                        expression = DbUtil.createLongExpression(signFlowDetail.signFlowDetailId, filter);
                        break;
                    case SIGNFLOWID:
                        expression = DbUtil.createLongExpression(signFlowDetail.signFlowId, filter);
                        break;
                    case SIGNORDER:
                        expression = DbUtil.createLongExpression(signFlowDetail.signOrder, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(signFlowDetail.status, filter);
                        break;
                    case VOFFICEROLEID:
                        expression = DbUtil.createLongExpression(signFlowDetail.vofficeRoleId, filter);
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
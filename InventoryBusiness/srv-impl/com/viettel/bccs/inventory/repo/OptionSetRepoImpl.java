package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.OptionSetDTO;
import com.viettel.bccs.inventory.model.OptionSet;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.QOptionSet;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class OptionSetRepoImpl implements OptionSetRepoCustom {

    public final static Logger logger = Logger.getLogger(OptionSetRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QOptionSet optionSet = QOptionSet.optionSet;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                OptionSet.COLUMNS column = OptionSet.COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CODE:
                        expression = DbUtil.createStringExpression(optionSet.code, filter);
                        break;
                    case CREATEDBY:
                        expression = DbUtil.createStringExpression(optionSet.createdBy, filter);
                        break;
                    case CREATIONDATE:
                        expression = DbUtil.createDateExpression(optionSet.creationDate, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(optionSet.description, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(optionSet.id, filter);
                        break;
                    case LASTUPDATEDATE:
                        expression = DbUtil.createDateExpression(optionSet.lastUpdateDate, filter);
                        break;
                    case LASTUPDATEDBY:
                        expression = DbUtil.createStringExpression(optionSet.lastUpdatedBy, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(optionSet.name, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(optionSet.status, filter);
                        break;
                    case EXCLUSE_ID_LIST:
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

    @Override
    public List<OptionSetDTO> getLsTypeLimit() throws Exception, LogicException {
        List<OptionSetDTO> optionSetDTOs;
        String strQuery= "SELECT   b.*" +
                "  FROM   option_set a, option_set_value b" +
                " WHERE   a.id = b.option_set_id AND a.code = #debitDayType";
        Query query = emIM.createNativeQuery(strQuery,OptionSetValue.class);
        query.setParameter("debitDayType", Const.OPTION_SET.DEBIT_DAY_TYPE);
        return query.getResultList();
    }

}

package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.FinanceType.COLUMNS;
import com.viettel.bccs.inventory.model.QFinanceType;

import java.math.BigDecimal;
import java.util.List;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class FinanceTypeRepoImpl implements FinanceTypeRepoCustom {

    public static final Logger logger = Logger.getLogger(FinanceTypeRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;


    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QFinanceType financeType = QFinanceType.financeType1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(financeType.createDate, filter); 
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(financeType.createUser, filter); 
                        break;
                    case DAYNUM:
                        expression = DbUtil.createLongExpression(financeType.dayNum, filter); 
                        break;
                    case FINANCETYPE:
                        expression = DbUtil.createStringExpression(financeType.financeType, filter); 
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(financeType.id, filter); 
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(financeType.lastUpdateTime, filter); 
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(financeType.lastUpdateUser, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(financeType.status, filter); 
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
    public Long getFinanceType(Long staffId) throws Exception {
        BigDecimal dayNum = BigDecimal.ONE;
        if (DataUtil.isNullObject(staffId)) {
            return dayNum.longValue();
        }
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT  day_num  ");
        strQuery.append("   FROM   finance_type ");
        strQuery.append("  WHERE   finance_type IN (SELECT   finance_type ");
        strQuery.append("                             FROM   stock_debit ");
        strQuery.append("                            WHERE   owner_id = #ownerId AND owner_type = #ownerType) ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("ownerId", staffId);
        query.setParameter("ownerType", Const.OWNER_TYPE.STAFF_LONG);
        List lst = query.getResultList();
        if (DataUtil.isNullOrEmpty(lst)) {
            strQuery = new StringBuilder();
            strQuery.append(" SELECT   day_num ");
            strQuery.append("   FROM   finance_type ");
            strQuery.append("  WHERE   finance_type IN ");
            strQuery.append("                  (SELECT   b.VALUE ");
            strQuery.append("                     FROM   option_set a, option_set_value b ");
            strQuery.append("                    WHERE       a.id = b.option_set_id ");
            strQuery.append("                            AND a.status = #optionSetStatus ");
            strQuery.append("                            AND b.status = #optionSetValueStatus ");
            strQuery.append("                            AND a.code = #optionSetCode) ");
            Query queryDefault = em.createNativeQuery(strQuery.toString());
            queryDefault.setParameter("optionSetStatus", Const.STATUS_ACTIVE);
            queryDefault.setParameter("optionSetValueStatus", Const.STATUS_ACTIVE);
            queryDefault.setParameter("optionSetCode", Const.OPTION_SET.FINANCE_TYPE_DEFAULT);
            List lstDefault = queryDefault.getResultList();
            if (!DataUtil.isNullOrEmpty(lstDefault)) {
                dayNum = (BigDecimal) lstDefault.get(0);
            }
        } else {
            dayNum = (BigDecimal) lst.get(0);
        }
        return dayNum.longValue();
    }

}
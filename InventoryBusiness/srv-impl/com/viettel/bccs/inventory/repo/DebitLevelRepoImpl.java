package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitLevelDTO;
import com.viettel.bccs.inventory.model.DebitLevel.COLUMNS;
import com.viettel.bccs.inventory.model.QDebitLevel;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DebitLevelRepoImpl implements DebitLevelRepoCustom {

    public static final Logger logger = Logger.getLogger(DebitLevelRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDebitLevel debitLevel = QDebitLevel.debitLevel1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(debitLevel.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(debitLevel.createUser, filter);
                        break;
                    case DEBITAMOUNT:
                        expression = DbUtil.createLongExpression(debitLevel.debitAmount, filter);
                        break;
                    case DEBITDAYTYPE:
                        expression = DbUtil.createStringExpression(debitLevel.debitDayType, filter);
                        break;
                    case DEBITLEVEL:
                        expression = DbUtil.createStringExpression(debitLevel.debitLevel, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(debitLevel.id, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(debitLevel.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(debitLevel.lastUpdateUser, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(debitLevel.status, filter);
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
    public List<DebitLevelDTO> searchDebitlevel(DebitLevelDTO debitLevelDTO) {
        try {

            StringBuilder builder = new StringBuilder("select a.ID , debit_level.name debit_level, debit_type.name debit_day_type, a.status, a.debit_amount ,a.create_date, a.create_user, a.last_update_time,a.last_update_user\n" +
                    "from debit_level a\n" +
                    "left join\n" +
                    "(SELECT   b.VALUE, b.name\n" +
                    "  FROM   option_set a, option_set_value b\n" +
                    " WHERE   a.id = b.option_set_id AND a.code ='DEBIT_LEVEL') debit_level on debit_level.value = a.debit_level\n" +
                    "left join\n" +
                    "(SELECT   b.VALUE, b.name\n" +
                    "  FROM   option_set a, option_set_value b\n" +
                    " WHERE   a.id = b.option_set_id AND a.code ='DEBIT_DAY_TYPE')debit_type on a.debit_day_type = debit_type.value \n" +
                    " where  a.status = '1'  ");
            if(!DataUtil.isNullOrEmpty(debitLevelDTO.getDebitDayType()))
                builder.append(" and a.debit_day_type =#debit_day_type");
            if(!DataUtil.isNullOrEmpty(debitLevelDTO.getDebitLevel()))
                builder.append(" and a.debit_level =#debit_level");
            if(debitLevelDTO.getDebitAmount()!=null)
                builder.append(" and a.debit_amount =#debit_amount ");
            builder.append(" order by a.last_update_time DESC ");
            Query query = emIM.createNativeQuery(builder.toString());
            if(!DataUtil.isNullOrEmpty(debitLevelDTO.getDebitDayType()))
                query.setParameter("debit_day_type", debitLevelDTO.getDebitDayType());
            if(!DataUtil.isNullOrEmpty(debitLevelDTO.getDebitLevel()))
                query.setParameter("debit_level", debitLevelDTO.getDebitLevel());
            if(debitLevelDTO.getDebitAmount()!=null)
            query.setParameter("debit_amount", debitLevelDTO.getDebitAmount());
            List<Object[]> objects = query.getResultList();
            List<DebitLevelDTO> result = new ArrayList<>();
            if (!DataUtil.isNullOrEmpty(objects)) {
                for (Object[] object : objects) {
                    DebitLevelDTO a = new DebitLevelDTO();
                    a.setId(DataUtil.safeToLong(object[0]));
                    a.setDebitLevel(DataUtil.safeToString(object[1]));
                    a.setDebitDayType(DataUtil.safeToString(object[2]));
                    a.setStatus(DataUtil.safeToString(object[3]));
                    a.setDebitAmount(DataUtil.safeToLong(object[4]));
                    a.setCreateDate((Date) object[5]);
                    a.setCreateUser(DataUtil.safeToString(object[6]));
                    a.setLastUpdateTime((Date) object[7]);
                    a.setLastUpdateUser(DataUtil.safeToString(object[8]));
                    result.add(a);
                }
                    return result;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error("searchDebitlevel", ex);
            return null;
        }
    }
}
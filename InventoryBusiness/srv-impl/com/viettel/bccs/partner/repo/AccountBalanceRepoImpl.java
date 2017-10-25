package com.viettel.bccs.partner.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.partner.model.AccountBalance;
import com.viettel.bccs.partner.model.AccountBalance.COLUMNS;
import com.viettel.bccs.partner.model.QAccountBalance;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class AccountBalanceRepoImpl implements AccountBalanceRepoCustom {

    public static final Logger logger = Logger.getLogger(AccountBalanceRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QAccountBalance accountBalance = QAccountBalance.accountBalance;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNTID:
                        expression = DbUtil.createLongExpression(accountBalance.accountId, filter);
                        break;
                    case BALANCEID:
                        expression = DbUtil.createLongExpression(accountBalance.balanceId, filter);
                        break;
                    case BALANCETYPE:
                        expression = DbUtil.createLongExpression(accountBalance.balanceType, filter);
                        break;
                    case DATECREATED:
                        expression = DbUtil.createDateExpression(accountBalance.dateCreated, filter);
                        break;
                    case ENDDATE:
                        expression = DbUtil.createDateExpression(accountBalance.endDate, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(accountBalance.lastUpdateTime, filter);
                        break;
                    case REALBALANCE:
                        expression = DbUtil.createLongExpression(accountBalance.realBalance, filter);
                        break;
                    case REALDEBIT:
                        expression = DbUtil.createLongExpression(accountBalance.realDebit, filter);
                        break;
                    case STARTDATE:
                        expression = DbUtil.createDateExpression(accountBalance.startDate, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(accountBalance.status, filter);
                        break;
                    case USERCREATED:
                        expression = DbUtil.createStringExpression(accountBalance.userCreated, filter);
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
    public List<AccountBalance> getAccountBalance(Long ownerType, Long ownerId, Long balanceType) throws Exception {
        StringBuilder builder = new StringBuilder(" SELECT   ab.* ");
        builder.append(" FROM   bccs_im.account_agent ag, bccs_im.account_balance ab ");
        builder.append("  WHERE       ag.account_id = ab.account_id ");
        builder.append("   AND ag.status = 1  ");
        builder.append("   AND ab.status = 1  ");
        builder.append("   AND ab.balance_type = #balance_type  ");
        builder.append("   AND ag.owner_id = #owner_id  ");
        builder.append("   AND ag.owner_type =  #owner_type ");
        Query query = em.createNativeQuery(builder.toString(), AccountBalance.class);
        query.setParameter("balance_type", balanceType);
        query.setParameter("owner_id", ownerId);
        query.setParameter("owner_type", ownerType);
        return query.getResultList();
    }

    @Override
    public AccountBalance findLock(Long accountBalanceId) throws Exception {
        AccountBalance accountBalance = em.find(AccountBalance.class, accountBalanceId, LockModeType.PESSIMISTIC_READ);
        return accountBalance;
    }
}
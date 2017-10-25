package com.viettel.bccs.partner.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.im1.model.AccountAgent;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.model.AccountAgent.COLUMNS;
import com.viettel.bccs.im1.model.QAccountAgent;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class AccountAgentRepoImpl implements AccountAgentRepoCustom {

    public static final Logger logger = Logger.getLogger(AccountAgentRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QAccountAgent accountAgent = QAccountAgent.accountAgent;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNTID:
                        expression = DbUtil.createLongExpression(accountAgent.accountId, filter);
                        break;
                    case ACCOUNTTYPE:
                        expression = DbUtil.createStringExpression(accountAgent.accountType, filter);
                        break;
                    case AGENTID:
                        expression = DbUtil.createLongExpression(accountAgent.agentId, filter);
                        break;
                    case BIRTHDATE:
                        expression = DbUtil.createDateExpression(accountAgent.birthDate, filter);
                        break;
                    case CENTREID:
                        expression = DbUtil.createLongExpression(accountAgent.centreId, filter);
                        break;
                    case CHECKISDN:
                        expression = DbUtil.createLongExpression(accountAgent.checkIsdn, filter);
                        break;
                    case CHECKVAT:
                        expression = DbUtil.createLongExpression(accountAgent.checkVat, filter);
                        break;
                    case COMMBALANCE:
                        expression = DbUtil.createLongExpression(accountAgent.commBalance, filter);
                        break;
                    case COMMBALANCEDATE:
                        expression = DbUtil.createDateExpression(accountAgent.commBalanceDate, filter);
                        break;
                    case CONTACTNO:
                        expression = DbUtil.createStringExpression(accountAgent.contactNo, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(accountAgent.createDate, filter);
                        break;
                    case CREDITEXPIRETIME:
                        expression = DbUtil.createDateExpression(accountAgent.creditExpireTime, filter);
                        break;
                    case CREDITTIMELIMIT:
                        expression = DbUtil.createLongExpression(accountAgent.creditTimeLimit, filter);
                        break;
                    case CURRENTDEBTPAYMENT:
                        expression = DbUtil.createLongExpression(accountAgent.currentDebtPayment, filter);
                        break;
                    case DATECREATED:
                        expression = DbUtil.createDateExpression(accountAgent.dateCreated, filter);
                        break;
                    case DISTRICT:
                        expression = DbUtil.createStringExpression(accountAgent.district, filter);
                        break;
                    case EMAIL:
                        expression = DbUtil.createStringExpression(accountAgent.email, filter);
                        break;
                    case FAX:
                        expression = DbUtil.createStringExpression(accountAgent.fax, filter);
                        break;
                    case ICCID:
                        expression = DbUtil.createStringExpression(accountAgent.iccid, filter);
                        break;
                    case IDNO:
                        expression = DbUtil.createStringExpression(accountAgent.idNo, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(accountAgent.isdn, filter);
                        break;
                    case ISSUEDATE:
                        expression = DbUtil.createDateExpression(accountAgent.issueDate, filter);
                        break;
                    case ISSUEPLACE:
                        expression = DbUtil.createStringExpression(accountAgent.issuePlace, filter);
                        break;
                    case LASTMODIFIED:
                        expression = DbUtil.createDateExpression(accountAgent.lastModified, filter);
                        break;
                    case LASTTIMERECOVER:
                        expression = DbUtil.createDateExpression(accountAgent.lastTimeRecover, filter);
                        break;
                    case LASTUPDATEIPADDRESS:
                        expression = DbUtil.createStringExpression(accountAgent.lastUpdateIpAddress, filter);
                        break;
                    case LASTUPDATEKEY:
                        expression = DbUtil.createStringExpression(accountAgent.lastUpdateKey, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(accountAgent.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(accountAgent.lastUpdateUser, filter);
                        break;
                    case LIMITDEBTPAYMENT:
                        expression = DbUtil.createLongExpression(accountAgent.limitDebtPayment, filter);
                        break;
                    case LOGINFAILURECOUNT:
                        expression = DbUtil.createLongExpression(accountAgent.loginFailureCount, filter);
                        break;
                    case MAXCREDITNUMBER:
                        expression = DbUtil.createLongExpression(accountAgent.maxCreditNumber, filter);
                        break;
                    case MAXCREDITVALUE:
                        expression = DbUtil.createLongExpression(accountAgent.maxCreditValue, filter);
                        break;
                    case MINPAYPERMONTH:
                        expression = DbUtil.createLongExpression(accountAgent.minPayPerMonth, filter);
                        break;
                    case MPIN:
                        expression = DbUtil.createStringExpression(accountAgent.mpin, filter);
                        break;
                    case MPINEXPIREDATE:
                        expression = DbUtil.createDateExpression(accountAgent.mpinExpireDate, filter);
                        break;
                    case MPINSTATUS:
                        expression = DbUtil.createStringExpression(accountAgent.mpinStatus, filter);
                        break;
                    case MSISDN:
                        expression = DbUtil.createStringExpression(accountAgent.msisdn, filter);
                        break;
                    case NUMADDMONEY:
                        expression = DbUtil.createLongExpression(accountAgent.numAddMoney, filter);
                        break;
                    case NUMPOSHPN:
                        expression = DbUtil.createLongExpression(accountAgent.numPosHpn, filter);
                        break;
                    case NUMPOSMOB:
                        expression = DbUtil.createLongExpression(accountAgent.numPosMob, filter);
                        break;
                    case NUMPREHPN:
                        expression = DbUtil.createLongExpression(accountAgent.numPreHpn, filter);
                        break;
                    case NUMPREMOB:
                        expression = DbUtil.createLongExpression(accountAgent.numPreMob, filter);
                        break;
                    case OBJECTDESTROY:
                        expression = DbUtil.createLongExpression(accountAgent.objectDestroy, filter);
                        break;
                    case OUTLETADDRESS:
                        expression = DbUtil.createStringExpression(accountAgent.outletAddress, filter);
                        break;
                    case OWNERCODE:
                        expression = DbUtil.createStringExpression(accountAgent.ownerCode, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(accountAgent.ownerId, filter);
                        break;
                    case OWNERNAME:
                        expression = DbUtil.createStringExpression(accountAgent.ownerName, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(accountAgent.ownerType, filter);
                        break;
                    case PARENTID:
                        expression = DbUtil.createLongExpression(accountAgent.parentId, filter);
                        break;
                    case PASSWORD:
                        expression = DbUtil.createStringExpression(accountAgent.password, filter);
                        break;
                    case PRECINCT:
                        expression = DbUtil.createStringExpression(accountAgent.precinct, filter);
                        break;
                    case PROVINCE:
                        expression = DbUtil.createStringExpression(accountAgent.province, filter);
                        break;
                    case SAPCODE:
                        expression = DbUtil.createStringExpression(accountAgent.sapCode, filter);
                        break;
                    case SECUREQUESTION:
                        expression = DbUtil.createStringExpression(accountAgent.secureQuestion, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(accountAgent.serial, filter);
                        break;
                    case SEX:
                        expression = DbUtil.createStringExpression(accountAgent.sex, filter);
                        break;
                    case STAFFCODE:
                        expression = DbUtil.createStringExpression(accountAgent.staffCode, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(accountAgent.status, filter);
                        break;
                    case STATUSANYPAY:
                        expression = DbUtil.createLongExpression(accountAgent.statusAnypay, filter);
                        break;
                    case STATUSANYPAY2:
                        expression = DbUtil.createLongExpression(accountAgent.statusAnypay2, filter);
                        break;
                    case TIN:
                        expression = DbUtil.createStringExpression(accountAgent.tin, filter);
                        break;
                    case TRADENAME:
                        expression = DbUtil.createStringExpression(accountAgent.tradeName, filter);
                        break;
                    case TRANSTYPE:
                        expression = DbUtil.createStringExpression(accountAgent.transType, filter);
                        break;
                    case USESALT:
                        expression = DbUtil.createStringExpression(accountAgent.useSalt, filter);
                        break;
                    case USERCREATED:
                        expression = DbUtil.createStringExpression(accountAgent.userCreated, filter);
                        break;
                    case USERRECOVER:
                        expression = DbUtil.createLongExpression(accountAgent.userRecover, filter);
                        break;
                    case WORKINGTIME:
                        expression = DbUtil.createDateExpression(accountAgent.workingTime, filter);
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
    public List<AccountAgent> getAccountAgentFromOwnerId(Long ownerId, Long ownerType) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   * ");
        builder.append("    FROM   bccs_im.account_agent");
        builder.append("   WHERE   owner_id = #ownerId AND owner_type = #ownerType and status = 1");
        Query query = em.createNativeQuery(builder.toString(), AccountAgent.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        return query.getResultList();
    }
}
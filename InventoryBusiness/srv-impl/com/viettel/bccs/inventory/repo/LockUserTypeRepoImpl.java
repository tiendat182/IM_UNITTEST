package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.LockUserInfoMsgDTO;
import com.viettel.bccs.inventory.dto.LockUserTypeDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.LockUserType.COLUMNS;
import com.viettel.bccs.inventory.model.QLockUserType;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class LockUserTypeRepoImpl implements LockUserTypeRepoCustom {

    public static final Logger logger = Logger.getLogger(LockUserTypeRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QLockUserType lockUserType = QLockUserType.lockUserType;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONURL:
                        expression = DbUtil.createStringExpression(lockUserType.actionUrl, filter);
                        break;
                    case AUTO:
                        expression = DbUtil.createCharacterExpression(lockUserType.auto, filter);
                        break;
                    case CHECKRANGE:
                        expression = DbUtil.createLongExpression(lockUserType.checkRange, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(lockUserType.description, filter);
                        break;
                    case EFFECTROLE:
                        expression = DbUtil.createStringExpression(lockUserType.effectRole, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(lockUserType.id, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(lockUserType.name, filter);
                        break;
                    case PARAMS:
                        expression = DbUtil.createStringExpression(lockUserType.params, filter);
                        break;
                    case REDIRECTURL:
                        expression = DbUtil.createStringExpression(lockUserType.redirectUrl, filter);
                        break;
                    case SQLCHECKTRANS:
                        expression = DbUtil.createStringExpression(lockUserType.sqlCheckTrans, filter);
                        break;
                    case SQLCMD:
                        expression = DbUtil.createStringExpression(lockUserType.sqlCmd, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createCharacterExpression(lockUserType.status, filter);
                        break;
                    case VALIDRANGE:
                        expression = DbUtil.createLongExpression(lockUserType.validRange, filter);
                        break;
                    case WARNINGCONTENT:
                        expression = DbUtil.createStringExpression(lockUserType.warningContent, filter);
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

    public List<LockUserTypeDTO> getAllUserLockType() throws Exception {
        List<LockUserTypeDTO> lstUserTypeDTO = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select sql_cmd, id, valid_range, check_range from lock_user_type where status = 1 and auto = 1 ");
        Query query = em.createNativeQuery(sql.toString());
        List listResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(listResult)) {
            for (Object o : listResult) {
                Object[] obj = (Object[]) o;
                LockUserTypeDTO lockUserTypeDTO = new LockUserTypeDTO();
                lockUserTypeDTO.setSqlCmd(DataUtil.safeToString(obj[0]));
                lockUserTypeDTO.setId(DataUtil.safeToLong(obj[1]));
                lockUserTypeDTO.setValidRange(DataUtil.safeToLong(obj[2]));
                lockUserTypeDTO.setCheckRange(DataUtil.safeToLong(obj[3]));
                lstUserTypeDTO.add(lockUserTypeDTO);
            }
        }
        return lstUserTypeDTO;
    }


    public List<LockUserInfoMsgDTO> getLockUserInfo(Long staffId) throws Exception {
        List<LockUserInfoMsgDTO> lsLockMsg = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("	SELECT   a.num AS num,  ");
        sql.append("			 a.trans_lst AS translst, ");
        sql.append("			 b.warning_content AS warningcontent, ");
        sql.append("			 b.redirect_url AS redirecturl, ");
        sql.append("			 b.action_url AS actionurl, ");
        sql.append("			 a.trans_code_lst AS transcodelst, ");
        sql.append("			 a.trans_mess_list AS transmesslist, ");
        sql.append("			 a.lock_type_id AS locktypeid ");
        sql.append("	  FROM   (  SELECT   lock_type_id, ");
        sql.append("						 COUNT (1) num, ");
        sql.append("						 LISTAGG(trans_id, ',') WITHIN GROUP (ORDER BY trans_id) AS trans_lst, ");
        sql.append("						 LISTAGG(trans_code, ',') WITHIN GROUP (ORDER BY trans_code) AS trans_code_lst, ");
        sql.append("						 LISTAGG(trans_code || '(' || TO_CHAR (trans_date, 'dd/mm/yyyy') || ')', ',') WITHIN GROUP (ORDER BY trans_code, trans_date) AS trans_mess_list ");
        sql.append("				  FROM   bccs_im.lock_user_info ");
        sql.append("				 WHERE   staff_id = #staffId ");
        sql.append("			  GROUP BY   lock_type_id) a, ");
        sql.append("			 lock_user_type b ");
        sql.append("	 WHERE   a.lock_type_id = b.id AND b.status = '1' ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("staffId", staffId);
        List<Object[]> listResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(listResult)) {
            int index;
            for (Object[] obj : listResult) {
                index = 0;
                LockUserInfoMsgDTO lockMsg = new LockUserInfoMsgDTO();
                lockMsg.setNum(DataUtil.safeToLong(obj[index++]));
                lockMsg.setTransLst(DataUtil.safeToString(obj[index++]));
                lockMsg.setWarningContent(DataUtil.safeToString(obj[index++]));
                lockMsg.setRedirectUrl(DataUtil.safeToString(obj[index++]));
                lockMsg.setActionUrl(DataUtil.safeToString(obj[index++]));
                lockMsg.setTransCodeLst(DataUtil.safeToString(obj[index++]));
                lockMsg.setTransMessList(DataUtil.safeToString(obj[index++]));
                lockMsg.setLockTypeId(DataUtil.safeToLong(obj[index]));
                lsLockMsg.add(lockMsg);
            }
        }
        return lsLockMsg;
    }
}
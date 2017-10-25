package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ApnIpDTO;
import com.viettel.bccs.inventory.model.ApnIp;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ApnIp.COLUMNS;
import com.viettel.bccs.inventory.model.QApnIp;

import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ApnIpRepoImpl implements ApnIpRepoCustom {

    public static final Logger logger = Logger.getLogger(ApnIpRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QApnIp apnIp = QApnIp.apnIp;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APNID:
                        expression = DbUtil.createLongExpression(apnIp.apnId, filter);
                        break;
                    case APNIPID:
                        expression = DbUtil.createLongExpression(apnIp.apnIpId, filter);
                        break;
                    case CENTERCODE:
                        expression = DbUtil.createStringExpression(apnIp.centerCode, filter);
                        break;
                    case CREATETIME:
                        expression = DbUtil.createDateExpression(apnIp.createTime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(apnIp.createUser, filter);
                        break;
                    case IP:
                        expression = DbUtil.createStringExpression(apnIp.ip, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(apnIp.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(apnIp.lastUpdateUser, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(apnIp.status, filter);
                        break;
                    case SUBNET:
                        expression = DbUtil.createStringExpression(apnIp.subNet, filter);
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
    public List<ApnIp> checkDuplicateApnIp(List<ApnIpDTO> listApnIp) {
        if (!DataUtil.isNullOrEmpty(listApnIp)) {
            StringBuilder sql = new StringBuilder();
            List params = Lists.newArrayList();
            sql.append("SELECT   a.* ");
            sql.append("  FROM   Apn_Ip a");
            sql.append(" WHERE   (a.apn_Id, a.ip) IN ");
            sql.append("(");
            for (ApnIpDTO apnIp : listApnIp) {
                sql.append("(?");
                params.add(apnIp.getApnId());
                sql.append(",");
                sql.append("?),");
                params.add(apnIp.getIp());
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
            sql.append(" AND a.status = 1");
            Query query = em.createNativeQuery(sql.toString(), ApnIp.class);
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
            return query.getResultList();
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public List<ApnIp> search(ApnIp apnIp) throws Exception {
        StringBuilder sql = new StringBuilder();
        List params = Lists.newArrayList();
        sql.append("SELECT   a.apn_ip_id,a.apn_id,a.ip,a.center_code,a.status,a.create_user,a.create_time,a.last_update_user,a.last_update_time,a.sub_net,b.apn_Code||' - '||b.apn_Name as apnStr");
        sql.append("  FROM   Apn_Ip a, Apn b");
        sql.append(" WHERE   a.apn_Id = b.apn_Id and a.status <> 2 ");
        if (!DataUtil.isNullObject(apnIp.getApnId())) {
            sql.append(" AND a.apn_Id = ? ");
            params.add(apnIp.getApnId());
        }
        if (!DataUtil.isNullObject(apnIp.getIp())) {
            sql.append(" AND lower(a.ip) like ? ");
            params.add("%" + apnIp.getIp().toLowerCase() + "%");
        }
        if (!DataUtil.isNullObject(apnIp.getStatus())) {
            sql.append(" AND a.status= ? ");
            params.add(apnIp.getStatus());
        }
        if (!DataUtil.isNullObject(apnIp.getCenterCode())) {
            sql.append(" AND lower(a.center_Code) like ? ");
            params.add("%" + apnIp.getCenterCode().toLowerCase() + "%");
        }
        if (!DataUtil.isNullObject(apnIp.getSubNet())) {
            sql.append(" AND lower(a.sub_Net) like ? ");
            params.add("%" + apnIp.getSubNet().toLowerCase() + "%");
        }
        sql.append(" AND ROWNUM <= 1000 ORDER BY a.create_Time desc ");
        Query query = em.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        List<ApnIp> lst = Lists.newArrayList();
        List<Object[]> lstOb = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lstOb)) {
            for (Object[] ob : lstOb) {
                ApnIp apnIpRs = new ApnIp();
                apnIpRs.setApnIpId(DataUtil.safeToLong(ob[0]));
                apnIpRs.setApnId(DataUtil.safeToLong(ob[1]));
                apnIpRs.setIp(DataUtil.safeToString(ob[2]));
                apnIpRs.setCenterCode(DataUtil.safeToString(ob[3]));
                apnIpRs.setStatus(DataUtil.safeToLong(ob[4]));
                apnIpRs.setCreateUser(DataUtil.safeToString(ob[5]));
                apnIpRs.setCreateTime(ob[6] != null ? (Date) (ob[6]) : null);
                apnIpRs.setLastUpdateUser(DataUtil.safeToString(ob[7]));
                apnIpRs.setLastUpdateTime(ob[8] != null ? (Date) (ob[8]) : null);
                apnIpRs.setSubNet(DataUtil.safeToString(ob[9]));
                apnIpRs.setApnStr(ob[10].toString());
                lst.add(apnIpRs);
            }
        }
        return lst;
    }

}
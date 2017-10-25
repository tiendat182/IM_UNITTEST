package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ApnDTO;
import com.viettel.bccs.inventory.model.Apn;
import com.viettel.bccs.inventory.model.Apn.COLUMNS;
import com.viettel.bccs.inventory.model.QApn;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class ApnRepoImpl implements ApnRepoCustom {

    public static final Logger logger = Logger.getLogger(ApnRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QApn apn = QApn.apn;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APNCODE:
                        expression = DbUtil.createStringExpression(apn.apnCode, filter);
                        break;
                    case APNID:
                        expression = DbUtil.createLongExpression(apn.apnId, filter);
                        break;
                    case APNNAME:
                        expression = DbUtil.createStringExpression(apn.apnName, filter);
                        break;
                    case CENTERCODE:
                        expression = DbUtil.createStringExpression(apn.centerCode, filter);
                        break;
                    case CREATETIME:
                        expression = DbUtil.createDateExpression(apn.createTime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(apn.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(apn.description, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(apn.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(apn.lastUpdateUser, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(apn.status, filter);
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
    public List<Apn> searchApnAutoComplete(String input) throws Exception {
        StringBuilder sql = new StringBuilder();
        List params = Lists.newArrayList();

        sql.append("SELECT   a.*");
        sql.append("\n FROM   Apn a");
        sql.append("\n WHERE   1=1 ");

        if (!DataUtil.isNullObject(input)) {
            sql.append(" AND (lower(a.apn_Code) like ? escape '\\' OR lower(a.apn_Name) like ? escape '\\')  ");
            params.add("%" + input.trim().toLowerCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\!") + "%");
            params.add("%" + input.trim().toLowerCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\!") + "%");
        }
        params.add(Const.LONG_OBJECT_TWO);
        sql.append("\n AND a.status != ?");

        sql.append("\n AND ROWNUM <= 5 ORDER BY a.apn_Code desc ");
        Query query = em.createNativeQuery(sql.toString(), Apn.class);
//        query.setParameter("status", Const.LONG_OBJECT_TWO);

        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }

    @Override
    public List<Apn> searchApnCorrect(ApnDTO apnDTO) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT a.* FROM BCCS_IM.APN a \n" +
                "WHERE 1=1 \n" +
                "AND A.STATUS != #statusNe \n" +
                "AND ROWNUM <= 1000 \n");
        if (!DataUtil.isNullOrEmpty(apnDTO.getApnCode())) {
            queryString.append("AND lower(A.APN_CODE) = #apnCode \n");
        }
//        if (!DataUtil.isNullOrEmpty(apnDTO.getApnName())) {
//            queryString.append("AND lower(A.APN_NAME) like :apnName \n");
//        }
//        if (apnDTO.getStatus() != null) {
//            queryString.append("AND A.STATUS = :status \n");
//        }
//        if (!DataUtil.isNullOrEmpty(apnDTO.getCenterCode())) {
//            queryString.append("AND lower(A.CENTER_CODE) like :centerCode \n");
//        }
//        if (!DataUtil.isNullOrEmpty(apnDTO.getDescription())) {
//            queryString.append("AND lower(A.DESCRIPTION) like :description \n");
//        }
//        queryString.append("order by LAST_UPDATE_TIME DESC");
        queryString.append("order by CREATE_TIME DESC");
        Query query = em.createNativeQuery(queryString.toString(), Apn.class);
        query.setParameter("statusNe", Const.LONG_OBJECT_TWO);
        if (!DataUtil.isNullOrEmpty(apnDTO.getApnCode())) {
            query.setParameter("apnCode", apnDTO.getApnCode().toLowerCase());
        }
//        if (!DataUtil.isNullOrEmpty(apnDTO.getApnName())) {
//            query.setParameter("apnName", "%" + apnDTO.getApnName().toLowerCase() + "%");
//        }
//        if (apnDTO.getStatus() != null) {
//            query.setParameter("status", Long.valueOf(apnDTO.getStatus()));
//        }
//        if (!DataUtil.isNullOrEmpty(apnDTO.getCenterCode())) {
//            query.setParameter("centerCode", "%" + apnDTO.getCenterCode().toLowerCase() + "%");
//        }
//        if (!DataUtil.isNullOrEmpty(apnDTO.getDescription())) {
//            query.setParameter("description", "%" + apnDTO.getDescription().toLowerCase() + "%");
//        }
        return query.getResultList();
    }

    @Override
    public List<Apn> searchApn(ApnDTO apnDTO) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT a.* FROM BCCS_IM.APN a \n" +
                "WHERE 1=1 \n" +
                "AND a.STATUS != #statusNe \n");
        if (!DataUtil.isNullOrEmpty(apnDTO.getApnCode())) {
            queryString.append("AND lower(A.APN_CODE) like #apnCode \n");
        }
        if (!DataUtil.isNullOrEmpty(apnDTO.getApnName())) {
            queryString.append("AND lower(A.APN_NAME) like #apnName \n");
        }
        if (apnDTO.getStatus() != null) {
            queryString.append("AND A.STATUS = #status \n");
        }
        if (!DataUtil.isNullOrEmpty(apnDTO.getCenterCode())) {
            queryString.append("AND lower(A.CENTER_CODE) like #centerCode \n");
        }
        if (!DataUtil.isNullOrEmpty(apnDTO.getDescription())) {
            queryString.append("AND lower(A.DESCRIPTION) like #description \n");
        }
//        queryString.append("order by LAST_UPDATE_TIME DESC");
        queryString.append(" order by CREATE_TIME DESC fetch first 1000 rows only ");
        Query query = em.createNativeQuery(queryString.toString(), Apn.class);
        query.setParameter("statusNe", Const.LONG_OBJECT_TWO);
        if (!DataUtil.isNullOrEmpty(apnDTO.getApnCode())) {
            query.setParameter("apnCode", "%" + apnDTO.getApnCode().toLowerCase() + "%");
        }
        if (!DataUtil.isNullOrEmpty(apnDTO.getApnName())) {
            query.setParameter("apnName", "%" + apnDTO.getApnName().toLowerCase() + "%");
        }
        if (apnDTO.getStatus() != null) {
            query.setParameter("status", Long.valueOf(apnDTO.getStatus()));
        }
        if (!DataUtil.isNullOrEmpty(apnDTO.getCenterCode())) {
            query.setParameter("centerCode", "%" + apnDTO.getCenterCode().toLowerCase() + "%");
        }
        if (!DataUtil.isNullOrEmpty(apnDTO.getDescription())) {
            query.setParameter("description", "%" + apnDTO.getDescription().toLowerCase() + "%");
        }
        return query.getResultList();
    }
}
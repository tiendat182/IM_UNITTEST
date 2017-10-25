package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.FilterSpecialNumberDTO;
import com.viettel.bccs.inventory.model.FilterSpecialNumber.COLUMNS;
import com.viettel.bccs.inventory.model.QFilterSpecialNumber;
import com.viettel.bccs.inventory.model.StockNumber;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

public class FilterSpecialNumberRepoImpl implements FilterSpecialNumberRepoCustom {

    public static final Logger logger = Logger.getLogger(FilterSpecialNumberRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QFilterSpecialNumber filterSpecialNumber = QFilterSpecialNumber.filterSpecialNumber;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(filterSpecialNumber.createDatetime, filter);
                        break;
                    case FILTERRULEID:
                        expression = DbUtil.createLongExpression(filterSpecialNumber.filterRuleId, filter);
                        break;
                    case FILTERSPECIALNUMBERID:
                        expression = DbUtil.createLongExpression(filterSpecialNumber.filterSpecialNumberId, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(filterSpecialNumber.isdn, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createCharacterExpression(filterSpecialNumber.status, filter);
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
    public List<Object[]> getResultFilter() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   f.filter_rule_id numberFilterRuleId,");
        sql.append("           n.name numberFilterRuleName,");
        sql.append("           n.group_filter_rule_id groupFilterRuleId,");
        sql.append("           g.name groupFilterRuleName,");
        sql.append("           g.parent_id ruleRootId,");
        sql.append("           (SELECT   name");
        sql.append("              FROM   group_filter_rule");
        sql.append("             WHERE   group_filter_rule_id = g.parent_id)");
        sql.append("               ruleRootName,");
        sql.append("           COUNT ( * ) quantitySpecialNumber,");
        sql.append("           f.status,");
        sql.append("           n.rule_order");
        sql.append("    FROM   filter_special_number f, number_filter_rule n, group_filter_rule g");
        sql.append("   WHERE   f.filter_rule_id = n.filter_rule_id");
        sql.append("           AND  f.status = 1");
        sql.append("           AND g.group_filter_rule_id = n.group_filter_rule_id");
        sql.append(" GROUP BY   f.filter_rule_id,");
        sql.append("           f.status,");
        sql.append("           n.name,");
        sql.append("           n.group_filter_rule_id,");
        sql.append("           g.name,");
        sql.append("           g.parent_id,");
        sql.append("           n.rule_order");
        sql.append(" ORDER BY  n.rule_order");
        Query query = em.createNativeQuery(sql.toString());
        return query.getResultList();
    }

    @Override
    public BaseMessage updateFiltered(final List<FilterSpecialNumberDTO> listNumberFiltered, final List<FilterSpecialNumberDTO> listNumberOK, final String userUpdateCode, final String userIp) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(listNumberFiltered)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "list.filtered.number.null");
        }
        if (!DataUtil.isNullOrEmpty(listNumberOK) && !listNumberFiltered.containsAll(listNumberOK)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "list.filtered.number.ok.null");
        }
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
        try {
            Connection con = IMCommonUtils.getDBIMConnection();
            PreparedStatement updateStock = con.prepareStatement("update stock_number set filter_rule_id=?,last_update_time=sysdate,last_update_user=?,last_update_ip_address=? where isdn=? and status='1'");
            PreparedStatement updateTemp = con.prepareStatement("update filter_special_number set status=? where filter_special_number_id=?");
            List<FilterSpecialNumberDTO> listNumberNotOk = Lists.newArrayList();
            listNumberNotOk.addAll(listNumberFiltered);
            if (!DataUtil.isNullObject(listNumberOK)) {
                listNumberNotOk.removeAll(listNumberOK);
            }
            int i = 0;
            for (FilterSpecialNumberDTO specialNumber : listNumberOK) {
                i++;
                updateStock.setLong(1, specialNumber.getFilterRuleId());
                updateStock.setString(2, userUpdateCode);
                updateStock.setString(3, userIp);
                updateStock.setLong(4, Long.valueOf(specialNumber.getIsdn()));
                updateStock.addBatch();
                updateTemp.setString(1, "1");
                updateTemp.setLong(2, specialNumber.getFilterSpecialNumberId());
                updateTemp.addBatch();
                if (i % 1000 == 0) {
                    try {
                        updateStock.executeBatch();
                        updateTemp.executeBatch();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            try {
                updateStock.executeBatch();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            for (FilterSpecialNumberDTO specialNumber : listNumberNotOk) {
                i++;
                updateStock.setLong(1, specialNumber.getFilterRuleId());
                updateStock.setString(2, userUpdateCode);
                updateStock.setString(3, userIp);
                updateStock.setLong(4, Long.valueOf(specialNumber.getIsdn()));
                updateStock.addBatch();
                updateTemp.setString(1, "2");
                updateTemp.setLong(2, specialNumber.getFilterSpecialNumberId());
                updateTemp.addBatch();
                if (i % 1000 == 0) {
                    try {
                        updateStock.executeBatch();
                        updateTemp.executeBatch();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            try {
                updateTemp.executeBatch();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            updateStock.close();
            updateTemp.close();
            con.close();
            //notify here
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
//            }
//        });
//        thread.start();
        BaseMessage msg = new BaseMessage();
        msg.setSuccess(true);
        return msg;
    }

    @Override
    public BaseMessage updateAllFiltered(String userUpdateCode, String userIp, List<Long> listFilterId) throws LogicException, Exception {
        StringBuilder updateTemp = new StringBuilder("UPDATE filter_special_number SET status='1' WHERE filter_rule_id IN (");
        int count = 0;
        for (Long filterId : listFilterId) {
            updateTemp.append("?");
            count += 1;
            if (count < listFilterId.size())
                updateTemp.append(",");
        }
        updateTemp.append(")");
        Query queryTemp = em.createNativeQuery(updateTemp.toString());
        count = 1;
        for (Long filterId : listFilterId) {
            queryTemp.setParameter(count, filterId);
            count += 1;
        }
        queryTemp.executeUpdate();
        StringBuilder updateStock = new StringBuilder();
        updateStock.append(" UPDATE   stock_number s");
        updateStock.append("   SET   s.last_update_user=?,s.last_update_time=sysdate,s.last_update_ip_address=?, s.filter_rule_id =");
        updateStock.append("             (SELECT   filter_rule_id");
        updateStock.append("                FROM   filter_special_number f");
        updateStock.append("               WHERE   s.isdn = f.isdn)");

        //add Prod_Offer_Id
//        updateStock.append(", s.prod_offer_id = ");
//        updateStock.append("             (SELECT   prod_offer_id");
//        updateStock.append("                FROM   filter_special_number f");
//        updateStock.append("               WHERE   s.isdn = f.isdn)");

        updateStock.append(" WHERE   isdn IN (SELECT   TO_NUMBER (isdn)");
        updateStock.append("                    FROM   filter_special_number m");
        updateStock.append("                   WHERE   filter_rule_id IN (");
        count = 0;
        for (Long filterId : listFilterId) {
            updateTemp.append("?");
            count += 1;
            if (count < listFilterId.size())
                updateStock.append(",");
        }
        updateTemp.append("))");
        Query queryStock = em.createNativeQuery(updateStock.toString());
        queryStock.setParameter(1, userUpdateCode);
        queryStock.setParameter(2, userIp);
        count = 3;
        for (Long filterId : listFilterId) {
            queryStock.setParameter(count, filterId);
            count += 1;
        }
        queryStock.executeUpdate();
        BaseMessage msg = new BaseMessage();
        msg.setSuccess(true);
        return msg;
    }

    @Override
    public List<FilterSpecialNumberDTO> getListSprecialNumberByRule(Long ruleId) throws LogicException, Exception {
        StringBuilder query = new StringBuilder("SELECT  a.filter_special_number_id, a.isdn, a.filter_rule_id, " +
                "a.status, a.create_datetime, b.code, b.name " +
                "FROM filter_special_number a, product_offering b " +
                "WHERE a.prod_offer_id = b.prod_offer_id(+) " +
                "AND a.filter_rule_id = ?");
        Query queryStr = em.createNativeQuery(query.toString());
        queryStr.setParameter(1, ruleId);
        List resultSet = queryStr.getResultList();
        List<FilterSpecialNumberDTO> result = Lists.newArrayList();
        for (Object o : resultSet) {
            Object[] arrSpecialNumber = (Object[]) o;
            FilterSpecialNumberDTO tmp = new FilterSpecialNumberDTO();
            tmp.setFilterSpecialNumberId(DataUtil.safeToLong(arrSpecialNumber[0]));
            tmp.setIsdn(DataUtil.safeToString(arrSpecialNumber[1]));
            tmp.setFilterRuleId(DataUtil.safeToLong(arrSpecialNumber[2]));
            tmp.setStatus(DataUtil.safeToString(arrSpecialNumber[3]));
            tmp.setCreateDatetime((Date) arrSpecialNumber[4]);
            tmp.setProductCode(DataUtil.safeToString(arrSpecialNumber[5]));
            tmp.setProductName(DataUtil.safeToString(arrSpecialNumber[6]));
            result.add(tmp);
        }
        return result;
    }
}
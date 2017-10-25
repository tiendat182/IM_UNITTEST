package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.NumberFilterRule.COLUMNS;
import com.viettel.bccs.inventory.model.QNumberFilterRule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class NumberFilterRuleRepoImpl implements NumberFilterRuleRepoCustom {

    public static final Logger logger = Logger.getLogger(NumberFilterRuleRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QNumberFilterRule numberFilterRule = QNumberFilterRule.numberFilterRule;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CONDITION:
                        expression = DbUtil.createStringExpression(numberFilterRule.condition, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(numberFilterRule.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(numberFilterRule.createUser, filter);
                        break;
                    case FILTERRULEID:
                        expression = DbUtil.createLongExpression(numberFilterRule.filterRuleId, filter);
                        break;
                    case GROUPFILTERRULEID:
                        expression = DbUtil.createLongExpression(numberFilterRule.groupFilterRuleId, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(numberFilterRule.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(numberFilterRule.lastUpdateUser, filter);
                        break;
                    case MASKMAPPING:
                        expression = DbUtil.createStringExpression(numberFilterRule.maskMapping, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(numberFilterRule.name, filter);
                        break;
                    case NOTES:
                        expression = DbUtil.createStringExpression(numberFilterRule.notes, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(numberFilterRule.prodOfferId, filter);
                        break;
                    case RULEORDER:
                        expression = DbUtil.createLongExpression(numberFilterRule.ruleOrder, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(numberFilterRule.status, filter);
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
    public List<NumberFilterRuleDTO> findRuleAggregate(NumberFilterRuleDTO numberFilterRule, boolean extract) throws Exception {
        List<NumberFilterRuleDTO> result = new ArrayList<NumberFilterRuleDTO>();
        List params = new ArrayList();
        StringBuilder sql = new StringBuilder("select nf.filter_rule_id, nf.group_filter_rule_id, nf.name, nf.mask_mapping, nf.status, " +
                " nf.notes, nf.rule_order, nf.condition, nf.prod_offer_id, " +
                " nf.create_date, nf.create_user, nf.last_update_time, nf.last_update_user, " +
                " gf.parent_id " +
                " from number_filter_rule nf, group_filter_rule gf " +
                " where nf.group_filter_rule_id = gf.group_filter_rule_id " +
                " and nf.status = 1 ");

        if (!DataUtil.isNullOrEmpty(numberFilterRule.getName())) {
            if (!extract) {
                sql.append(" and lower(nf.name) like ?");
                params.add("%" + numberFilterRule.getName().toLowerCase().trim() + "%");
            } else {
                sql.append(" and lower(nf.name) = ?");
                params.add(numberFilterRule.getName().toLowerCase().trim());
            }

        }

        if (!DataUtil.isNullOrEmpty(numberFilterRule.getLsFilterRuleId())) {
            sql.append(" and nf.group_filter_rule_id  in ( ");
            String strId = "";
            for (Long filerId : numberFilterRule.getLsFilterRuleId()) {
                if (!DataUtil.isNullOrEmpty(strId)) {
                    strId += ",";
                    sql.append(",");
                }
                strId += filerId;
                sql.append("?");
                params.add(filerId);
            }
            sql.append(" )");
        }

        if ((numberFilterRule.getProdOfferId() != null) && numberFilterRule.getProdOfferId() > 0) {
            sql.append(" and nf.prod_offer_id = ?");
            params.add(numberFilterRule.getProdOfferId());
        }

        if (!DataUtil.isNullOrEmpty(numberFilterRule.getMaskMapping())) {
            if (!extract) {
                sql.append(" and lower(nf.mask_mapping) like ?");
                params.add("%" + numberFilterRule.getMaskMapping().toLowerCase().trim() + "%");
            } else {
                sql.append(" and lower(nf.mask_mapping) = ?");
                params.add(numberFilterRule.getMaskMapping().toLowerCase().trim());
            }
        }

        if (!DataUtil.isNullObject(numberFilterRule.getRuleOrder())) {
            sql.append(" and nf.rule_order = ?");
            params.add(numberFilterRule.getRuleOrder());
        }

        if (!DataUtil.isNullOrEmpty(numberFilterRule.getCondition())) {
            if (!extract) {
                sql.append(" and lower(nf.condition) like ?");
                params.add("%" + numberFilterRule.getCondition().toLowerCase().trim() + "%");
            } else {
                sql.append(" and lower(nf.condition) = ?");
                params.add(numberFilterRule.getCondition().toLowerCase().trim());
            }
        }

        if (!DataUtil.isNullOrEmpty(numberFilterRule.getNotes())) {
            if (!extract) {
                sql.append(" and lower(nf.notes) like ?");
                params.add("%" + numberFilterRule.getNotes().toLowerCase().trim() + "%");
            } else {
                sql.append(" and lower(nf.notes) = ?");
                params.add(numberFilterRule.getNotes().toLowerCase().trim());
            }
        }

        sql.append(" order by nf.last_update_time desc");

        Query query = em.createNativeQuery(sql.toString());
        for (int idx = 0; idx < params.size(); idx++) {
            query.setParameter(idx + 1, params.get(idx));
        }

        List listRuleAggregate = query.getResultList();

        for (Object o : listRuleAggregate) {
            Object[] arrNumberRule = (Object[]) o;
            NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
            numberFilterRuleDTO.setFilterRuleId(DataUtil.safeToLong(arrNumberRule[0]));
            numberFilterRuleDTO.setGroupFilterRuleId(DataUtil.safeToLong(arrNumberRule[1]));
            numberFilterRuleDTO.setName(DataUtil.safeToString(arrNumberRule[2]));
            numberFilterRuleDTO.setMaskMapping(DataUtil.safeToString(arrNumberRule[3]));
            numberFilterRuleDTO.setStatus(DataUtil.safeToString(arrNumberRule[4]));
            numberFilterRuleDTO.setNotes(DataUtil.safeToString(arrNumberRule[5]));
            numberFilterRuleDTO.setRuleOrder(!DataUtil.isNullObject(arrNumberRule[6]) ? DataUtil.safeToLong(arrNumberRule[6]) : null);
            numberFilterRuleDTO.setCondition(DataUtil.safeToString(arrNumberRule[7]));
            numberFilterRuleDTO.setProdOfferId(!DataUtil.isNullObject(arrNumberRule[8]) ? DataUtil.safeToLong(arrNumberRule[8]) : null);
            numberFilterRuleDTO.setCreateDate((Date) arrNumberRule[9]);
            numberFilterRuleDTO.setCreateUser(DataUtil.safeToString(arrNumberRule[10]));
            numberFilterRuleDTO.setLastUpdateTime((Date) arrNumberRule[11]);
            numberFilterRuleDTO.setLastUpdateUser(DataUtil.safeToString(arrNumberRule[12]));
            numberFilterRuleDTO.setAggregateFilterRuleId(DataUtil.safeToLong(arrNumberRule[13]));
            result.add(numberFilterRuleDTO);
        }
        return result;
    }


    @WebMethod
    public Long checkInsertConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception {
        List params = new ArrayList();
        StringBuilder sql = new StringBuilder(" select count(*) total ");
        sql.append(" from number_Filter_rule nf, group_filter_rule gf1, group_filter_rule gf2 ");
        sql.append(" where nf.group_filter_rule_id = gf1.group_filter_rule_id ");
        sql.append(" and gf1.parent_id = gf2.group_filter_rule_id ");
        sql.append(" and nf.status = ? ");
        params.add(Const.STATUS.ACTIVE);
        if (!DataUtil.isNullOrZero(numberFilterRuleDTO.getRuleOrder())) {
            sql.append(" and nf.rule_order = ? ");
            params.add(numberFilterRuleDTO.getRuleOrder());
        }
        if (!DataUtil.isNullOrZero(numberFilterRuleDTO.getAggregateFilterRuleId())) {
            sql.append(" and gf2.telecom_service_id in ( " +
                    " select gf2.telecom_service_id from number_Filter_rule nf, group_filter_rule gf1, group_filter_rule gf2 " +
                    " where " +
                    " nf.group_filter_rule_id = gf1.group_filter_rule_id " +
                    " and gf1.group_filter_rule_id = gf2.group_filter_rule_id" +
                    " and gf1.group_filter_rule_id = ?)");
            params.add(numberFilterRuleDTO.getGroupFilterRuleId());
        }
        Query query = em.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if (result != null) {
            return result.longValue();
        }
        return -1L;
    }

    @WebMethod
    public Long checkEditConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception {
        List params = new ArrayList();
        StringBuilder sql = new StringBuilder(" select count(*) total ");
        sql.append(" from number_Filter_rule nf, group_filter_rule gf1, group_filter_rule gf2 ");
        sql.append(" where nf.group_filter_rule_id = gf1.group_filter_rule_id ");
        sql.append(" and gf1.parent_id = gf2.group_filter_rule_id ");
        sql.append(" and nf.status = ? ");
        params.add(Const.STATUS.ACTIVE);
        if (!DataUtil.isNullOrZero(numberFilterRuleDTO.getRuleOrder())) {
            sql.append(" and nf.rule_order = ? ");
            params.add(numberFilterRuleDTO.getRuleOrder());
        }
        if (!DataUtil.isNullOrZero(numberFilterRuleDTO.getAggregateFilterRuleId())) {
            sql.append(" and gf2.telecom_service_id in ( " +
                    " select gf2.telecom_service_id from number_Filter_rule nf, group_filter_rule gf1, group_filter_rule gf2 " +
                    " where nf.group_filter_rule_id = gf1.group_filter_rule_id " +
                    " and gf1.group_filter_rule_id = gf2.group_filter_rule_id " +
                    " and nf.filter_rule_id = ?) ");
            params.add(numberFilterRuleDTO.getFilterRuleId());
        }

        sql.append(" and nf.filter_rule_id != ? ");
        params.add(numberFilterRuleDTO.getFilterRuleId());

        Query query = em.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if (result != null) {
            return result.longValue();
        }
        return -1L;
    }

    @WebMethod
    public List<NumberFilterRuleDTO> searchHightOrderRule(Long telecomServiceId, Long minOrder) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT   b.filter_rule_id, b.group_filter_rule_id, b.name, b.mask_mapping, b.status, b.notes, b.rule_order, " +
                "b.condition, b.prod_offer_id, b.create_date, b.create_user, b.last_update_time, b.last_update_user ");
        strQuery.append("  FROM   group_filter_rule a, number_filter_rule b ");
        strQuery.append("WHERE       a.group_Filter_Rule_Id = b.group_Filter_Rule_Id ");
        strQuery.append("AND a.status = 1 ");
        strQuery.append("AND b.status = 1 ");
        strQuery.append("AND a.telecom_service_id = ? ");
        strQuery.append("AND b.rule_Order >= ? ");
        strQuery.append("ORDER BY b.rule_Order ASC");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter(1, telecomServiceId);
        query.setParameter(2, minOrder);
        List<NumberFilterRuleDTO> result = Lists.newArrayList();
        List listResult = query.getResultList();
        for (Object o : listResult) {
            Object[] arrNumberRule = (Object[]) o;
            NumberFilterRuleDTO numberFilterRuleDTO = new NumberFilterRuleDTO();
            numberFilterRuleDTO.setFilterRuleId(DataUtil.safeToLong(arrNumberRule[0]));
            numberFilterRuleDTO.setGroupFilterRuleId(DataUtil.safeToLong(arrNumberRule[1]));
            numberFilterRuleDTO.setName(DataUtil.safeToString(arrNumberRule[2]));
            numberFilterRuleDTO.setMaskMapping(DataUtil.safeToString(arrNumberRule[3]));
            numberFilterRuleDTO.setStatus(DataUtil.safeToString(arrNumberRule[4]));
            numberFilterRuleDTO.setNotes(DataUtil.safeToString(arrNumberRule[5]));
            numberFilterRuleDTO.setRuleOrder(DataUtil.safeToLong(arrNumberRule[6]));
            numberFilterRuleDTO.setCondition(DataUtil.safeToString(arrNumberRule[7]));
            numberFilterRuleDTO.setProdOfferId(!DataUtil.isNullObject(arrNumberRule[8]) ? DataUtil.safeToLong(arrNumberRule[8]) : null);
            numberFilterRuleDTO.setCreateDate((Date) arrNumberRule[9]);
            numberFilterRuleDTO.setCreateUser(DataUtil.safeToString(arrNumberRule[10]));
            numberFilterRuleDTO.setLastUpdateTime((Date) arrNumberRule[11]);
            numberFilterRuleDTO.setLastUpdateUser(DataUtil.safeToString(arrNumberRule[12]));
            result.add(numberFilterRuleDTO);
        }
        return result;
    }
}
package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.OptionSetValue.COLUMNS;
import com.viettel.bccs.inventory.model.QOptionSetValue;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class OptionSetValueRepoImpl implements OptionSetValueRepoCustom {

    private final BaseMapper<OptionSetValue, OptionSetValueDTO> mapper = new BaseMapper(OptionSetValue.class, OptionSetValueDTO.class);
    public static final Logger logger = Logger.getLogger(OptionSetValueRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QOptionSetValue optionSetValue = QOptionSetValue.optionSetValue;
        BooleanExpression result = BooleanTemplate.create("1 = 1");

        try {
            for (FilterRequest filter : filters) {
                if (filter.getValue() == null || "".equals(filter.getValue().toString().trim())) {
                    continue;
                }
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDBY:
                        expression = DbUtil.createStringExpression(optionSetValue.createdBy, filter);
                        break;
                    case CREATIONDATE:
                        expression = DbUtil.createDateExpression(optionSetValue.creationDate, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(optionSetValue.description, filter);
                        break;
                    case DISPLAYORDER:
                        expression = DbUtil.createLongExpression(optionSetValue.displayOrder, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(optionSetValue.id, filter);
                        break;
                    case LASTUPDATEDATE:
                        expression = DbUtil.createDateExpression(optionSetValue.lastUpdateDate, filter);
                        break;
                    case LASTUPDATEDBY:
                        expression = DbUtil.createStringExpression(optionSetValue.lastUpdatedBy, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(optionSetValue.name, filter);
                        break;
                    case OPTIONSETID:
                        expression = DbUtil.createLongExpression(optionSetValue.optionSetId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(optionSetValue.status, filter);
                        break;
                    case VALUE:
                        expression = optionSetValue.value.containsIgnoreCase((String) filter.getValue());
                        break;
                    case EXCLUSE_ID_LIST:
                        List idLst = (List) filter.getValue();
                        List<Long> idLongLst = Lists.newArrayList();
                        for (Object id : idLst) {
                            idLongLst.add(DataUtil.safeToLong(id));
                        }
                        if (!idLst.isEmpty()) {
                            expression = optionSetValue.id.notIn(idLongLst);
                        }
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
    public List<OptionSetValue> getLsOptionSetValueById(Long optionSetId) {
        //query
        String strQuery = "SELECT a FROM OptionSetValue as a  " +
                "WHERE 1=1 " +
                "AND a.optionSetId = :optionSetId " +
                "ORDER BY a.displayOrder ASC";
        //execute query
        Query query = em.createQuery(strQuery);
        query.setParameter("optionSetId", optionSetId);
        return query.getResultList();
    }


    @Override
    public List<OptionSetValue> getLsOptionSetValueByCode(String strCode) {
        //query
        String strQuery = " SELECT a.* " +
                " FROM option_set_value a, option_set b  " +
                " WHERE 1=1 " +
                "   AND a.option_set_id = b.id " +
                "   AND b.code = #strCode " +
                "   AND a.status = #aStt " +
                "   AND b.status = #bStt " +
                " ORDER BY a.value, a.name ASC ";
        //execute query
        Query query = em.createNativeQuery(strQuery, OptionSetValue.class);
        query.setParameter("strCode", strCode);
        query.setParameter("aStt", Const.STATUS.ACTIVE);
        query.setParameter("bStt", Const.STATUS.ACTIVE);
        return query.getResultList();
    }

    @Override
    public OptionSetValue getOptionSetValue(String optionSetType, String optionSetValueCode) throws Exception {
        String strQuery = "SELECT a FROM OptionSetValue a, OptionSet b  " +
                "WHERE 1=1 " +
                "AND a.optionSetId = b.id " +
                "AND a.status = " + Const.STATUS.ACTIVE +
                "AND b.status = " + Const.STATUS.ACTIVE +
                "AND b.code = :optionSetType " +
                "AND a.value = :optionSetValueCode ";

        //execute query
        Query query = em.createQuery(strQuery);
        query.setParameter("optionSetType", optionSetType);
        query.setParameter("optionSetValueCode", optionSetValueCode);
        List<OptionSetValue> lsOption = query.getResultList();
        if (lsOption != null && !lsOption.isEmpty()) {
            return lsOption.get(0);
        }
        return null;
    }

    @Override
    public Long getValueByNameFromOptionSetValue(String name) throws Exception {
        String sql = "SELECT osv.* FROM option_set_value osv WHERE 1=1 AND osv.name = #nameopv AND osv.status = #status ";
        Query query = em.createNativeQuery(sql, OptionSetValue.class);
        query.setParameter("nameopv", name);
        query.setParameter("status", Const.STATUS.ACTIVE);
        List<OptionSetValue> lsOptionSetValue = query.getResultList();
        if (lsOptionSetValue != null && !lsOptionSetValue.isEmpty()) {
            return DataUtil.safeToLong(lsOptionSetValue.get(0).getValue());
        }
        return 0L;
    }

    /**
     * Get OptionSetValue name theo OptionSetId va code
     *
     * @param optSetCode
     * @param code
     * @return
     * @throws Exception
     * @author KhuongDV
     */
    @Override
    public String getValueByOptionSetIdAndCode(String optSetCode, String code) throws Exception {
        String sql = " SELECT osv.value FROM option_set o, option_set_value osv WHERE o.id=osv.option_set_id "
                + " AND o.status=#p1 AND osv.status=#p2 "
                + " AND o.code=#p3 AND osv.name=#p4 ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("p1", Const.STATUS.ACTIVE);
        query.setParameter("p2", Const.STATUS.ACTIVE);
        query.setParameter("p3", optSetCode);
        query.setParameter("p4", code);
        List<String> lst = query.getResultList();
        if (!lst.isEmpty()) {
            return lst.get(0);
        }
        return "";
    }

    /**
     * @param code
     * @return
     * @throws Exception
     * @author KhuongDV Get List OptionSetValueDTO theo optionSetID
     */
    @Override
    public List<OptionSetValue> getByOptionSetCode(String code) throws Exception {
        String sql = "SELECT a.* FROM OPTION_SET o, OPTION_SET_VALUE a "
                + "WHERE a.option_set_id = o.ID "
                + " AND a.status = #opSetValStt AND o.STATUS = #opSetStt "
                + " AND o.CODE = #code ORDER BY a.display_order ";
        Query query = em.createNativeQuery(sql, OptionSetValue.class);
        query.setParameter("opSetValStt", Const.STATUS.ACTIVE);
        query.setParameter("opSetStt", Const.STATUS.ACTIVE);
        query.setParameter("code", code);
        List<OptionSetValue> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<OptionSetValue> getByOptionSetValueByNameCode(String code, String name) throws Exception {
        String sql = "SELECT a.* FROM OPTION_SET o, OPTION_SET_VALUE a "
                + "WHERE a.option_set_id = o.ID "
                + " AND a.status = ? AND o.STATUS = ? "
                + " AND o.CODE = ? AND a.name LIKE ? ORDER BY a.name ";
        Query query = em.createNativeQuery(sql, OptionSetValue.class);
        query.setParameter(1, Const.STATUS.ACTIVE);
        query.setParameter(2, Const.STATUS.ACTIVE);
        query.setParameter(3, code);
        query.setParameter(4, "%" + name + "%");
        List<OptionSetValue> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<OptionSetValue> getByOptionsetCodeAndValue(String code, String value) {
        StringBuilder builder = new StringBuilder(" SELECT a.* from OPTION_SET_VALUE a, OPTION_SET c where a.OPTION_SET_ID=c.ID");
        builder.append(" AND c.CODE=#code");
        builder.append(" AND a.VALUE=#value");
        builder.append(" AND a.status=1 and c.status=1");
        builder.append(" ORDER BY a.name ");
        Query query = em.createNativeQuery(builder.toString(), OptionSetValue.class);
        query.setParameter("code", code);
        query.setParameter("value", value);
        return query.getResultList();
    }

    @Override
    public List<OptionSetValue> getDebitDayTypeByDebitLevel(String debitLevel) throws Exception {
        StringBuilder builder = new StringBuilder(" SELECT * FROM option_set_value ")
                .append(" WHERE option_set_id IN (SELECT id FROM option_set WHERE code = 'DEBIT_DAY_TYPE')");
        if (!DataUtil.isNullOrEmpty(debitLevel)) {
            builder.append(" AND value IN (SELECT distinct debit_day_type FROM debit_level WHERE debit_level= #debitLevel)");
        }
        Query query = em.createNativeQuery(builder.toString(), OptionSetValue.class);
        if (!DataUtil.isNullOrEmpty(debitLevel)) {
            query.setParameter("debitLevel", debitLevel);
        }
        return query.getResultList();
    }

    @Override
    public List<OptionSetValue> getStatusOptionSetValueByStockState(String code, List<String> listValue) throws Exception {
        StringBuilder builder = new StringBuilder("SELECT   sv FROM   OptionSet s, OptionSetValue sv "
                + "  WHERE       s.id = sv.optionSetId "
                + "  AND s.status = :status AND sv.status = :status "
                + "  AND s.code = :code ");
        if (!DataUtil.isNullOrEmpty(listValue)) {
            builder.append(" AND sv.value IN :listValue");

        }
        Query query = em.createQuery(builder.toString());
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("code", code);
        if (!DataUtil.isNullOrEmpty(listValue)) {
            query.setParameter("listValue", listValue);
        }
        List<OptionSetValue> result = query.getResultList();
//        List<OptionSetValue> result = Lists.newArrayList();
//        if (!DataUtil.isNullOrEmpty(queryResult)) {
//            for (Object obj : queryResult) {
//                Object[] objects = (Object[]) obj;
//                OptionSetValue value = new OptionSetValue();
//                value.setValue(DataUtil.safeToString(objects[0]));
//                value.setName(DataUtil.safeToString(objects[1]));
//                result.add(value);
//            }
//        }
        return result;
    }

    @Override
    public List<OptionSetValueDTO> getDebitLevel(Long debitDayType) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("select ov.value, ov.name||' ('||trim(TO_CHAR(nvl(dl.debit_amount,0),'999G999G999G999','NLS_NUMERIC_CHARACTERS = '',.'''))||')' ")
                .append(" from option_set op, option_set_value ov, debit_level dl")
                .append(" where op.id=ov.option_set_id")
                .append(" and ov.value=dl.debit_level")
                .append(" and dl.status=1 and ov.status=1 and op.status=1")
                .append(" and op.code='DEBIT_LEVEL'")
                .append(" and dl.debit_day_type=#debitDayType");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("debitDayType", debitDayType);
        List queryResult = query.getResultList();
        List<OptionSetValueDTO> result = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object obj : queryResult) {
                Object[] objects = (Object[]) obj;
                OptionSetValueDTO valueDTO = new OptionSetValueDTO();
                valueDTO.setValue(DataUtil.safeToString(objects[0]));
                valueDTO.setName(DataUtil.safeToString(objects[1]));
                result.add(valueDTO);
            }
        }
        return result;
    }

    @Override
    public List<OptionSetValueDTO> getFinanceType() throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append(" select ov.value, ov.name||' ('||ft.day_num||')'")
                .append("  from option_set op, option_set_value ov, finance_type ft")
                .append(" where op.id = ov.option_set_id")
                .append("       and ov.value = ft.finance_type")
                .append("       and op.code = 'FINANCE_TYPE' and op.status = 1 and ov.status = 1 and ft.status = 1")
                .append(" order by ov.value asc");
        Query query = em.createNativeQuery(builder.toString());
        List queryResult = query.getResultList();
        List<OptionSetValueDTO> result = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object obj : queryResult) {
                Object[] objects = (Object[]) obj;
                OptionSetValueDTO valueDTO = new OptionSetValueDTO();
                valueDTO.setValue(DataUtil.safeToString(objects[0]));
                valueDTO.setName(DataUtil.safeToString(objects[1]));
                result.add(valueDTO);
            }
        }
        return result;
    }

    @Override
    public List<OptionSetValueDTO> getLsProvince(String shopCodeUser, String shopCode, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception {
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" SELECT   osv.* ");
        sql.append(" 	FROM   option_set os, option_set_value osv ");
        sql.append("    WHERE       os.id = osv.option_set_id ");
        sql.append(" 		   AND os.code = 'PROVINCE_ORDER' ");
        sql.append(" 		   AND osv.status=1 ");
        sql.append(" 		   AND osv.value <> #shopCodeUser ");
        sql.append(" 		   AND EXISTS ");
        sql.append(" 				  (SELECT   1 ");
        sql.append(" 					 FROM   stock_total st, shop sh, product_offering po ");
        sql.append(" 					WHERE       st.owner_type = 1 ");
        sql.append(" 							AND st.owner_id = sh.shop_id ");
        sql.append(" 							AND st.prod_offer_id = po.prod_offer_id ");
        sql.append(" 							AND sh.shop_code = osv.value ");
        if (!DataUtil.isNullOrEmpty(shopCode)) {
            sql.append(" 							AND sh.shop_code = #shopCode ");
        }
        sql.append(" 							AND po.status = 1 ");
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            sql.append(" 							AND po.product_offer_type_id = #prodOfferTypeId ");
        } else {
            sql.append(" 							AND po.product_offer_type_id = 7 ");
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            sql.append(" 							AND po.prod_offer_id = #prodOfferId ");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            sql.append(" 							AND st.state_id =#stateId ");
        } else {
            sql.append(" 							AND st.state_id IN (4, 12) ");
        }
        sql.append(" 							AND st.available_quantity > 0) ");
        sql.append("   ORDER BY   ABS(TO_NUMBER (osv.display_order) ");
        sql.append(" 			   - (SELECT   TO_NUMBER (osv.display_order) ");
        sql.append(" 					FROM   option_set os, option_set_value osv ");
        sql.append(" 				   WHERE       os.id = osv.option_set_id ");
        sql.append(" 						   AND os.code = 'PROVINCE_ORDER' ");
        sql.append(" 						   AND osv.value = #shopCodeUser)) ASC ");
        Query query = em.createNativeQuery(sql.toString(), OptionSetValue.class);
        query.setParameter("shopCodeUser", shopCodeUser);
        if (!DataUtil.isNullOrEmpty(shopCode)) {
            query.setParameter("shopCode", shopCode);
        }
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            query.setParameter("prodOfferTypeId", prodOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        List<OptionSetValue> lsResult = query.getResultList();
        return mapper.toDtoBean(lsResult);
    }

    public List<OptionSetValueDTO> getDefaultDebitLevel() throws LogicException, Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append(" SELECT    distinct ov.value, ov.name || ' (' ||")
                .append("           listagg((select ov.name")
                .append(" FROM   bccs_im.option_set op,")
                .append("        bccs_im.option_set_value ov")
                .append("        where  op.id = ov.option_set_id")
                .append("        AND ov.status = 1")
                .append("        AND op.status = 1")
                .append("        and ov.value = dl.debit_day_type")
                .append("        AND op.code = 'DEBIT_DAY_TYPE') || ':' || trim(TO_CHAR(nvl(dl.debit_amount,0),'999G999G999G999','NLS_NUMERIC_CHARACTERS = '',.'''))")
                .append("                      , ',') WITHIN GROUP (ORDER BY dl.debit_day_type)  || ')' name, ")
                .append(" listagg (dl.debit_day_type,',') WITHIN GROUP (ORDER BY dl.debit_day_type) debit_day_type ")
                .append(" FROM   bccs_im.option_set op,")
                .append("        bccs_im.option_set_value ov,")
                .append("        bccs_im.debit_level dl")
                .append(" WHERE       op.id = ov.option_set_id")
                .append("        AND dl.status = 1")
                .append("        AND ov.status = 1")
                .append("        AND op.status = 1")
                .append("        AND op.code = 'DEBIT_LEVEL'")
                .append("        AND dl.debit_day_type in (select ov.value")
                .append(" FROM   bccs_im.option_set op,")
                .append("        bccs_im.option_set_value ov")
                .append("        where  op.id = ov.option_set_id")
                .append("        AND ov.status = 1")
                .append("        AND op.status = 1")
                .append("        AND op.code = 'DEBIT_DAY_TYPE')")
                .append("        AND dl.debit_level = ov.VALUE")
                .append(" group by ov.value, ov.name order by to_number(ov.value)");
        Query query = em.createNativeQuery(builder.toString());
        List queryResult = query.getResultList();
        List<OptionSetValueDTO> result = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object obj : queryResult) {
                Object[] objects = (Object[]) obj;
                OptionSetValueDTO valueDTO = new OptionSetValueDTO();
                valueDTO.setValue(DataUtil.safeToString(objects[0]));
                valueDTO.setName(DataUtil.safeToString(objects[1]));
                valueDTO.setDescription(DataUtil.safeToString(objects[2]));
                result.add(valueDTO);
            }
        }
        return result;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.fw.common.util;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.RangeValue;
import org.apache.commons.collections.map.HashedMap;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author thiendn1
 */
public class DbUtil {

    public static void setParamInQuery(Query query, String prefix, List objs) {
        int idx = 1;
        for (Object obj : objs) {
            query.setParameter(prefix + String.valueOf(idx++), obj);
        }
    }
    public static List createListParamInQuery(String prefix, List objs) {
        List lst = new ArrayList<>();
        int idx = 1;
        for (Object obj : objs) {
            lst.add(new Object[]{prefix + String.valueOf(idx++), obj});
        }

        return lst;
    }

    public static Map<String, Object> createMapParamInQuery(String prefix, List objs) {
        Map<String, Object> map = new HashedMap();
        int idx = 1;
        for (Object obj : objs) {
            map.put(prefix + String.valueOf(idx++), obj);
        }
        return map;
    }


    public static String createInQuery(String prefix, List objs) {
        String inQuery = " IN (";
        for (int idx = 1; idx < objs.size() + 1; idx++) {
            inQuery += ",#" + prefix + idx;
        }
        inQuery += ") ";
        inQuery = inQuery.replaceFirst(",", "");

        return inQuery;
    }

    /**
     * get sys date of database
     *
     * @return
     */
    public static Predicate createOrGroup(List<? extends BooleanExpression> lst) {
        if (lst == null || lst.isEmpty()) {
            return BooleanTemplate.create("1 = 1");
        }

        BooleanExpression ex = lst.get(0);
        for (int i = 1; i < lst.size(); i++) {
            ex = ex.or(lst.get(i));
        }

        return ex;
    }

    public static Date getSysDate(EntityManager em) throws Exception {
        String queryString = "SELECT sysdate from dual";
        Query queryObject = em.createNativeQuery(queryString);
        return (Date) queryObject.getSingleResult();
    }

    /*
     * ThinhDD - 20/12/2011
     * Them ham cho da chu ky
     */
    public static Date getSysDate(EntityManager em, String pattern) throws Exception {
        return getSysDate(em);
    }

    public static Date getTruncSysdate(EntityManager em) throws Exception {
        String strDate = DateUtil.date2ddMMyyyyString(getSysDate(em));
        return DateUtil.string2Date(strDate);
    }

    public static Long getSequence(EntityManager em, String sequenceName) {
        String sql = "select " + sequenceName + ".nextval from dual";
        Query query = em.createNativeQuery(sql);
        return ((BigDecimal) query.getSingleResult()).longValue();
    }

    public static void setInsertCareNull(StringBuilder sql, Object value, List parameterList) {
        setInsertCareNull(sql, value, parameterList, false);
    }

    public static void setInsertCareNull(StringBuilder sql, Object value, List parameterList, boolean isEnd) {
        if (value != null) {
            sql.append(isEnd ? "?)" : "?,");
            parameterList.add(value);
        } else {
            sql.append(isEnd ? "null)" : "?)");
        }
    }

    public static BooleanExpression createDateExpression(DateTimePath<Date> dateProperty, FilterRequest filter) throws Exception {
        if (filter.getValue() == null) {
            return dateProperty.isNull();
        }

        FilterRequest.Operator operator = filter.getOperator();
        if (operator == null || filter.isExtract()) {
            operator = FilterRequest.Operator.EQ;
        }


        DateTime dateTime = DateTime.now();

        if (operator != FilterRequest.Operator.BETWEEN) {
            dateTime = DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(filter.getValue().toString());
        }

        switch (operator) {
            case EQ:
                // Need to compare year, month, date instead of all date object because "dd:mm:yyyy hh:mi:ss" != "dd:mm:yyyy"
                return dateProperty.year().eq(dateTime.getYear())
                        .and(dateProperty.month().eq(dateTime.getMonthOfYear()))
                        .and(dateProperty.dayOfMonth().eq(dateTime.getDayOfMonth()));
            case LOE:
                return dateProperty.lt(dateTime.plusDays(1).toDate());
            case LT:
                return dateProperty.lt(dateTime.toDate());
            case GT:
                return dateProperty.gt(dateTime.plusDays(1).toDate());
            case GOE:
                return dateProperty.goe(dateTime.toDate());
            case BETWEEN:
                RangeValue value = (RangeValue) filter.getValue();
                DateTime dateTimeFrom = DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(value.getFromValue().toString());
                DateTime dateTimeTo = DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(value.getToValue().toString());
                BooleanExpression expression = dateProperty.goe(dateTimeFrom.toDate());
                expression = expression.and(dateProperty.loe(dateTimeTo.plusDays(1).toDate()));
                return expression;
            case NE:
                return dateProperty.year().eq(dateTime.getYear())
                        .and(dateProperty.month().eq(dateTime.getMonthOfYear()))
                        .and(dateProperty.dayOfMonth().eq(dateTime.getDayOfMonth())).not();
            case IS_NULL:
                return dateProperty.isNull();
            case IS_NOT_NULL:
                return dateProperty.isNotNull();
        }

        return null;
    }

    public static BooleanExpression createStringExpression(StringPath dataProperty, FilterRequest filter) {
        if (filter.getValue() == null) {
            return dataProperty.isNull();
        }

        FilterRequest.Operator operator = filter.getOperator();
        if ((operator == null) && filter.isExtract()) {
            operator = FilterRequest.Operator.EQ;
        } else if (operator == null || filter.isForLookup()) {
            operator = FilterRequest.Operator.EQ;
        }

        String value = filter.getValue().toString().trim();
        if (DataUtil.isNullOrEmpty(value)) {
            return null;
        }

        switch (operator) {
            case EQ:
                if (filter.isExtract()) {
                    return dataProperty.eq(value);
                } else {
                    return dataProperty.equalsIgnoreCase(value);
                }

            case LOE:
                return dataProperty.loe(value);
            case GOE:
                return dataProperty.goe(value);
            case LIKE://them % khi tim kiem
                if (filter.isExtract()) {
                    return dataProperty.like(value);
                } else {
                    String valuePercent = "%" + value.toLowerCase() + "%";
                    return dataProperty.toLowerCase().like(valuePercent);
                }
            case IN:
                return dataProperty.in((List<String>) filter.getValue());
            case NE:
                return dataProperty.ne(value);
            case NOTIN:
                return dataProperty.notIn((List<String>) filter.getValue());
            case EQALL:
                break;
            case AQANY:
                break;
            case AS:
                return dataProperty.eq(value);
            case LT:
                return dataProperty.lt(value);
            case GT:
                return dataProperty.gt(value);
            case STARTWITH:
                return dataProperty.startsWith(value);
            case BETWEEN:
                break;
            case IS_NULL:
                return dataProperty.isNull();
            case IS_NOT_NULL:
                return dataProperty.isNotNull();
        }

        return null;
    }

    public static BooleanExpression createShortExpression(NumberPath<Short> dataProperty, FilterRequest filter) {
        FilterRequest.Operator operator = filter.getOperator();

        if (filter.getValue() == null) {
            return dataProperty.isNull();
        }

        if (operator == null) {
            operator = FilterRequest.Operator.EQ;
        }

        Short value = DataUtil.safeToShort(filter.getValue());

        switch (operator) {
            case IN:
                return dataProperty.in(DataUtil.objLstToShortLst((List<Object>) filter.getValue()));
            case EQ:
                return dataProperty.eq(value);
            case NE:
                return dataProperty.ne(value);
            case NOTIN:
                return dataProperty.notIn(DataUtil.objLstToShortLst((List<Object>) filter.getValue()));
            case EQALL:
                break;
            case AQANY:
                break;
            case AS:
                return dataProperty.eq(value);
            case LT:
                return dataProperty.lt(value);
            case GT:
                return dataProperty.gt(value);
            case LOE:
                return dataProperty.loe(value);
            case GOE:
                return dataProperty.goe(value);
            case BETWEEN:
                RangeValue rangeValue = (RangeValue) filter.getValue();
                return dataProperty.between(DataUtil.safeToLong(rangeValue.getFromValue()), DataUtil.safeToLong(rangeValue.getToValue()));
            case LIKE:
                break;
            case IS_NULL:
                return dataProperty.isNull();
            case IS_NOT_NULL:
                return dataProperty.isNotNull();
        }

        return null;
    }

    public static BooleanExpression createLongExpression(NumberPath<Long> dataProperty, FilterRequest filter) {
        FilterRequest.Operator operator = filter.getOperator();

        if (filter.getValue() == null) {
            return dataProperty.isNull();
        }

        if (operator == null) {
            operator = FilterRequest.Operator.EQ;
        }

        Long value = DataUtil.safeToLong(filter.getValue());

        switch (operator) {
            case IN:
                return dataProperty.in(DataUtil.objLstToLongLst((List<Object>) filter.getValue()));
            case EQ:
                return dataProperty.eq(value);
            case NE:
                return dataProperty.ne(value);
            case NOTIN:
                return dataProperty.notIn(DataUtil.objLstToLongLst((List<Object>) filter.getValue()));
            case EQALL:
                break;
            case AQANY:
                break;
            case AS:
                return dataProperty.eq(value);
            case LT:
                return dataProperty.lt(value);
            case GT:
                return dataProperty.gt(value);
            case LOE:
                return dataProperty.loe(value);
            case GOE:
                return dataProperty.goe(value);
            case BETWEEN:
                RangeValue rangeValue = (RangeValue) filter.getValue();
                return dataProperty.between(DataUtil.safeToLong(rangeValue.getFromValue()), DataUtil.safeToLong(rangeValue.getToValue()));
            case LIKE:
                break;
            case IS_NULL:
                return dataProperty.isNull();
            case IS_NOT_NULL:
                return dataProperty.isNotNull();
        }

        return null;
    }

    public static BooleanExpression createBigDecimalExpression(NumberPath<BigDecimal> dataProperty, FilterRequest filter) {
        if (filter.getValue() == null) {
            return dataProperty.isNull();
        }

        FilterRequest.Operator operator = filter.getOperator();

        if (operator == null) {
            operator = FilterRequest.Operator.EQ;
        }

        BigDecimal value = DataUtil.safeToBigDecimal(filter.getValue());

        switch (operator) {
            case IN:
                return dataProperty.in(DataUtil.objLstToBigDecimalLst((List<Object>) filter.getValue()));
            case EQ:
                return dataProperty.eq(value);
            case NE:
                return dataProperty.ne(value);
            case NOTIN:
                return dataProperty.notIn(DataUtil.objLstToBigDecimalLst((List<Object>) filter.getValue()));
            case EQALL:
                break;
            case AQANY:
                break;
            case AS:
                return dataProperty.eq(value);
            case LT:
                return dataProperty.lt(value);
            case GT:
                return dataProperty.gt(value);
            case LOE:
                return dataProperty.loe(value);
            case GOE:
                return dataProperty.goe(value);
            case BETWEEN:
                RangeValue rangeValue = (RangeValue) filter.getValue();
                return dataProperty.between(DataUtil.safeToBigDecimal(rangeValue.getFromValue()), DataUtil.safeToBigDecimal(rangeValue.getToValue()));
            case LIKE:
                break;
            case IS_NULL:
                return dataProperty.isNull();
            case IS_NOT_NULL:
                return dataProperty.isNotNull();
        }

        return null;
    }

    public static BooleanExpression createDoubleExpression(NumberPath<Double> dataProperty, FilterRequest filter) {
        if (filter.getValue() == null) {
            return dataProperty.isNull();
        }

        FilterRequest.Operator operator = filter.getOperator();

        if (operator == null) {
            operator = FilterRequest.Operator.EQ;
        }

        Double value = DataUtil.safeToDouble(filter.getValue());

        switch (operator) {
            case IN:
                return dataProperty.in(DataUtil.objLstToDoubleLst((List<Object>) filter.getValue()));
            case EQ:
                return dataProperty.eq(value);
            case NE:
                return dataProperty.ne(value);
            case NOTIN:
                return dataProperty.notIn(DataUtil.objLstToDoubleLst((List<Object>) filter.getValue()));
            case EQALL:
                break;
            case AQANY:
                break;
            case AS:
                return dataProperty.eq(value);
            case LT:
                return dataProperty.lt(value);
            case GT:
                return dataProperty.gt(value);
            case LOE:
                return dataProperty.loe(value);
            case GOE:
                return dataProperty.goe(value);
            case BETWEEN:
                RangeValue rangeValue = (RangeValue) filter.getValue();
                return dataProperty.between(DataUtil.safeToBigDecimal(rangeValue.getFromValue()), DataUtil.safeToBigDecimal(rangeValue.getToValue()));
            case LIKE:
                break;
            case IS_NULL:
                return dataProperty.isNull();
            case IS_NOT_NULL:
                return dataProperty.isNotNull();
        }

        return null;
    }

    public static BooleanExpression createCharacterExpression(ComparablePath<Character> dataProperty, FilterRequest filter) {
        if (filter.getValue() == null) {
            return dataProperty.isNull();
        }

        FilterRequest.Operator operator = filter.getOperator();
        if (operator == null) {
            operator = FilterRequest.Operator.EQ;
        }

        if (operator == FilterRequest.Operator.EQ) {
            Character status = filter.getValue().toString().charAt(0);
            if (!Objects.equals(status, '-')) {
                return dataProperty.eq(status);
            }
        }
        if (operator == FilterRequest.Operator.IN) {
            return dataProperty.in(DataUtil.objLstToCharLst((List<Object>) filter.getValue()));
        }
        if (operator == FilterRequest.Operator.NE) {
            return dataProperty.ne(filter.getValue().toString().charAt(0));
        }

        return null;
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            resultSet = null;
        }
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            conn = null;
        }
    }

    public static void close(PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            stmt = null;
        }
    }


}

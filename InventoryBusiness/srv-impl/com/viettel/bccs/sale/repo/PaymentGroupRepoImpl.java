package com.viettel.bccs.sale.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.sale.dto.PaymentGroupDTO;
import com.viettel.bccs.sale.dto.PaymentGroupDayTypeDTO;
import com.viettel.bccs.sale.model.PaymentGroup;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.sale.model.PaymentGroup.COLUMNS;
import com.viettel.bccs.sale.model.QPaymentGroup;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class PaymentGroupRepoImpl implements PaymentGroupRepoCustom {

    public static final Logger logger = Logger.getLogger(PaymentGroupRepoCustom.class);
    private final BaseMapper<PaymentGroup, PaymentGroupDTO> mapper = new BaseMapper<>(PaymentGroup.class, PaymentGroupDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager sale;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPaymentGroup paymentGroup = QPaymentGroup.paymentGroup;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(paymentGroup.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(paymentGroup.createUser, filter);
                        break;
                    case DEBITDAYTYPE:
                        expression = DbUtil.createLongExpression(paymentGroup.debitDayType, filter);
                        break;
                    case LASTUPDATEDATE:
                        expression = DbUtil.createDateExpression(paymentGroup.lastUpdateDate, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(paymentGroup.lastUpdateUser, filter);
                        break;
                    case MAXDAYDEBIT:
                        expression = DbUtil.createLongExpression(paymentGroup.maxDayDebit, filter);
                        break;
                    case MAXMONEYDEBIT:
                        expression = DbUtil.createLongExpression(paymentGroup.maxMoneyDebit, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(paymentGroup.name, filter);
                        break;
                    case PAYMENTGROUPID:
                        expression = DbUtil.createLongExpression(paymentGroup.paymentGroupId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(paymentGroup.status, filter);
                        break;
                    case TYPE:
                        expression = DbUtil.createStringExpression(paymentGroup.type, filter);
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

    public PaymentGroupDTO getPaymentGroupByName(String name) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT * FROM payment_group where lower(name) = #name and status = 1");
        //execute query
        Query query = sale.createNativeQuery(strQuery.toString(), PaymentGroup.class);
        query.setParameter("name", name.toLowerCase());
        List<PaymentGroup> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst.get(0));
        }
        return null;
    }

    public List<PaymentGroupDTO> getLstPaymentGroup() throws Exception {
        List<PaymentGroupDTO> lstResult = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   a.payment_group_id, a.name");
        strQuery.append(" FROM   payment_group a");
        strQuery.append(" WHERE   a.status = 1");
        strQuery.append(" AND EXISTS");
        strQuery.append(" (SELECT   1");
        strQuery.append(" FROM   payment_group_day_type b");
        strQuery.append(" WHERE   a.payment_group_id = b.payment_group_id");
        strQuery.append(" AND b.status = 1)");
        strQuery.append(" GROUP BY   a.payment_group_id, a.name");
        //execute query
        Query query = sale.createNativeQuery(strQuery.toString());
        List<PaymentGroup> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object o : lst) {
                Object[] object = (Object[]) o;
                PaymentGroupDTO paymentGroupDTO = new PaymentGroupDTO();
                paymentGroupDTO.setPaymentGroupId(DataUtil.safeToLong(object[0]));
                paymentGroupDTO.setName(DataUtil.safeToString(object[1]));
                lstResult.add(paymentGroupDTO);
            }
        }
        return lstResult;
    }

    public List<PaymentGroupDayTypeDTO> getLstDayTypeByPaymentGroupId(Long paymentGroupId) throws Exception {
        List<PaymentGroupDayTypeDTO> lstResult = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   id,");
        strQuery.append("          payment_group_id,");
        strQuery.append("          debit_day_type,");
        strQuery.append("          max_money_debit");
        strQuery.append("   FROM   payment_group_day_type");
        strQuery.append("  WHERE   status = 1 AND payment_group_id = #paymentGroupId");
        //execute query
        Query query = sale.createNativeQuery(strQuery.toString());
        query.setParameter("paymentGroupId", paymentGroupId);
        List<PaymentGroup> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object o : lst) {
                Object[] object = (Object[]) o;
                PaymentGroupDayTypeDTO paymentGroupDayTypeDTO = new PaymentGroupDayTypeDTO();
                paymentGroupDayTypeDTO.setId(DataUtil.safeToLong(object[0]));
                paymentGroupDayTypeDTO.setPaymentGroupId(DataUtil.safeToLong(object[1]));
                paymentGroupDayTypeDTO.setDebitDayType(DataUtil.safeToLong(object[2]));
                paymentGroupDayTypeDTO.setMaxDebitMoney(DataUtil.safeToLong(object[3]));
                lstResult.add(paymentGroupDayTypeDTO);
            }
        }
        return lstResult;
    }
}
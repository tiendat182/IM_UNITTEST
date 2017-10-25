package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ReceiptExpenseDTO;
import com.viettel.bccs.inventory.model.QReceiptExpense;
import com.viettel.bccs.inventory.model.ReceiptExpense;
import com.viettel.bccs.inventory.model.ReceiptExpense.COLUMNS;
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

public class ReceiptExpenseRepoImpl implements ReceiptExpenseRepoCustom {

    public static final Logger logger = Logger.getLogger(ReceiptExpenseRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    private final BaseMapper<ReceiptExpense, ReceiptExpenseDTO> mapper = new BaseMapper(ReceiptExpense.class, ReceiptExpenseDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QReceiptExpense receiptExpense = QReceiptExpense.receiptExpense;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ADDRESS:
                        expression = DbUtil.createStringExpression(receiptExpense.address, filter);
                        break;
                    case AMOUNT:
                        expression = DbUtil.createLongExpression(receiptExpense.amount, filter);
                        break;
                    case APPROVERDATE:
                        expression = DbUtil.createDateExpression(receiptExpense.approverDate, filter);
                        break;
                    case APPROVERSTAFFID:
                        expression = DbUtil.createLongExpression(receiptExpense.approverStaffId, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(receiptExpense.createDatetime, filter);
                        break;
                    case DEBT:
                        expression = DbUtil.createStringExpression(receiptExpense.debt, filter);
                        break;
                    case DELIVERER:
                        expression = DbUtil.createStringExpression(receiptExpense.deliverer, filter);
                        break;
                    case DELIVERERSHOPID:
                        expression = DbUtil.createLongExpression(receiptExpense.delivererShopId, filter);
                        break;
                    case DELIVERERSTAFFID:
                        expression = DbUtil.createLongExpression(receiptExpense.delivererStaffId, filter);
                        break;
                    case DESTROYDATE:
                        expression = DbUtil.createDateExpression(receiptExpense.destroyDate, filter);
                        break;
                    case DESTROYSTAFFID:
                        expression = DbUtil.createLongExpression(receiptExpense.destroyStaffId, filter);
                        break;
                    case NOTES:
                        expression = DbUtil.createStringExpression(receiptExpense.notes, filter);
                        break;
                    case OWN:
                        expression = DbUtil.createStringExpression(receiptExpense.own, filter);
                        break;
                    case PAYMETHOD:
                        expression = DbUtil.createStringExpression(receiptExpense.payMethod, filter);
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(receiptExpense.reasonId, filter);
                        break;
                    case RECEIPTDATE:
                        expression = DbUtil.createDateExpression(receiptExpense.receiptDate, filter);
                        break;
                    case RECEIPTID:
                        expression = DbUtil.createLongExpression(receiptExpense.receiptId, filter);
                        break;
                    case RECEIPTNO:
                        expression = DbUtil.createStringExpression(receiptExpense.receiptNo, filter);
                        break;
                    case RECEIPTTYPEID:
                        expression = DbUtil.createLongExpression(receiptExpense.receiptTypeId, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(receiptExpense.shopId, filter);
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(receiptExpense.staffId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(receiptExpense.status, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(receiptExpense.telecomServiceId, filter);
                        break;
                    case TYPE:
                        expression = DbUtil.createStringExpression(receiptExpense.type, filter);
                        break;
                    case USERNAME:
                        expression = DbUtil.createStringExpression(receiptExpense.username, filter);
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

    public ReceiptExpenseDTO findByStockTransIdAndType(Long stockTransId, String type) throws Exception, LogicException {
        ReceiptExpenseDTO result = new ReceiptExpenseDTO();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select a.receipt_id,a.receipt_no,a.reason_id,a.deliverer_shop_id, ");
        strQuery.append(" a.deliverer,a.amount,a.pay_method, b.deposit_id from receipt_expense a, deposit b   ");
        strQuery.append("WHERE 1 = 1 ");
        strQuery.append("AND a.status = 3 and a.receipt_id = b.receipt_id and a.receipt_id in ( ");
        strQuery.append("select receipt_id from deposit where type =#type and stock_trans_id =#stock_trans_id ) ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("type", type);
        query.setParameter("stock_trans_id", stockTransId);
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                result.setReceiptId(DataUtil.safeToLong(object[index++]));
                result.setReceiptNo(DataUtil.safeToString(object[index++]));
                result.setReasonId(DataUtil.safeToLong(object[index++]));
                result.setDelivererShopId(DataUtil.safeToLong(object[index++]));
                result.setDeliverer(DataUtil.safeToString(object[index++]));
                result.setAmount(DataUtil.safeToLong(object[index++]));
                result.setPayMethod(DataUtil.safeToString(object[index++]));
                result.setDepositId(DataUtil.safeToLong(object[index]));
                break;
            }
            return result;
        }
        return null;
    }

    @Override
    public List<ReceiptExpense> getReceiptExpenseFromReceiptNo(String receiptNo) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from Receipt_Expense where receipt_No like #receipt_No ");
        Query query = em.createNativeQuery(strQuery.toString(), ReceiptExpense.class);
        query.setParameter("receipt_No", receiptNo + "%");
        return query.getResultList();
    }
}
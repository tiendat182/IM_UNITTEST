package com.viettel.bccs.sale.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.repo.FinanceTypeRepo;
import com.viettel.bccs.sale.dto.SaleTransBankplusDTO;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.model.QSaleTrans;
import com.viettel.bccs.sale.model.SaleTrans;
import com.viettel.bccs.sale.model.SaleTrans.COLUMNS;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class SaleTransRepoImpl implements SaleTransRepoCustom {
    private final BaseMapper<SaleTrans, SaleTransDTO> mapper = new BaseMapper<>(SaleTrans.class, SaleTransDTO.class);
    public static final Logger logger = Logger.getLogger(SaleTransRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager em;

    @Autowired
    FinanceTypeRepo financeTypeRepo;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QSaleTrans saleTrans = QSaleTrans.saleTrans;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACTIONCODE:
                        expression = DbUtil.createStringExpression(saleTrans.actionCode, filter);
                        break;
                    case ADDRESS:
                        expression = DbUtil.createStringExpression(saleTrans.address, filter);
                        break;
                    case AMOUNTMODEL:
                        expression = DbUtil.createLongExpression(saleTrans.amountModel, filter);
                        break;
                    case AMOUNTNOTTAX:
                        expression = DbUtil.createLongExpression(saleTrans.amountNotTax, filter);
                        break;
                    case AMOUNTSERVICE:
                        expression = DbUtil.createLongExpression(saleTrans.amountService, filter);
                        break;
                    case AMOUNTTAX:
                        expression = DbUtil.createLongExpression(saleTrans.amountTax, filter);
                        break;
                    case APPROVERDATE:
                        expression = DbUtil.createDateExpression(saleTrans.approverDate, filter);
                        break;
                    case APPROVERUSER:
                        expression = DbUtil.createStringExpression(saleTrans.approverUser, filter);
                        break;
                    case CHECKSTOCK:
                        expression = DbUtil.createStringExpression(saleTrans.checkStock, filter);
                        break;
                    case COMPANY:
                        expression = DbUtil.createStringExpression(saleTrans.company, filter);
                        break;
                    case CONTRACTNO:
                        expression = DbUtil.createStringExpression(saleTrans.contractNo, filter);
                        break;
                    case CREATESTAFFID:
                        expression = DbUtil.createLongExpression(saleTrans.createStaffId, filter);
                        break;
                    case CUSTNAME:
                        expression = DbUtil.createStringExpression(saleTrans.custName, filter);
                        break;
                    case DESTROYDATE:
                        expression = DbUtil.createDateExpression(saleTrans.destroyDate, filter);
                        break;
                    case DESTROYUSER:
                        expression = DbUtil.createStringExpression(saleTrans.destroyUser, filter);
                        break;
                    case DISCOUNT:
                        expression = DbUtil.createLongExpression(saleTrans.discount, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(saleTrans.id, filter);
                        break;
                    case INVOICECREATEDATE:
                        expression = DbUtil.createDateExpression(saleTrans.invoiceCreateDate, filter);
                        break;
                    case INVOICEUSEDID:
                        expression = DbUtil.createLongExpression(saleTrans.invoiceUsedId, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(saleTrans.isdn, filter);
                        break;
                    case LSTSTOCKISDN:
                        // expression = DbUtil.createStringExpression(saleTrans.lstStockIsdn, filter);
                        break;
                    case LSTSTOCKSIM:
                        //expression = DbUtil.createStringExpression(saleTrans.lstStockSim, filter); 
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(saleTrans.note, filter);
                        break;
                    case PAYMETHOD:
                        expression = DbUtil.createStringExpression(saleTrans.payMethod, filter);
                        break;
                    case PROMOTION:
                        expression = DbUtil.createBigDecimalExpression(saleTrans.promotion, filter);
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(saleTrans.reasonId, filter);
                        break;
                    case SALESERVICEID:
                        expression = DbUtil.createLongExpression(saleTrans.saleServiceId, filter);
                        break;
                    case SALESERVICEPRICEID:
                        expression = DbUtil.createLongExpression(saleTrans.saleServicePriceId, filter);
                        break;
                    case SALETRANSCODE:
                        expression = DbUtil.createStringExpression(saleTrans.saleTransCode, filter);
                        break;
                    case SALETRANSDATE:
                        expression = DbUtil.createDateExpression(saleTrans.saleTransDate, filter);
                        break;
                    case SALETRANSID:
                        expression = DbUtil.createLongExpression(saleTrans.saleTransId, filter);
                        break;
                    case SALETRANSTYPE:
                        expression = DbUtil.createStringExpression(saleTrans.saleTransType, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(saleTrans.serial, filter);
                        break;
                    case SHOPCODE:
                        expression = DbUtil.createStringExpression(saleTrans.shopCode, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(saleTrans.shopId, filter);
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(saleTrans.staffId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(saleTrans.status, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(saleTrans.stockTransId, filter);
                        break;
                    case SUBID:
                        expression = DbUtil.createLongExpression(saleTrans.subId, filter);
                        break;
                    case TAX:
                        expression = DbUtil.createLongExpression(saleTrans.tax, filter);
                        break;
                    case TELNUMBER:
                        expression = DbUtil.createStringExpression(saleTrans.telNumber, filter);
                        break;
                    case EMAIL:
                        expression = DbUtil.createStringExpression(saleTrans.email, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(saleTrans.telecomServiceId, filter);
                        break;
                    case TIN:
                        expression = DbUtil.createStringExpression(saleTrans.tin, filter);
                        break;
                    case TRANSRESULT:
                        expression = DbUtil.createLongExpression(saleTrans.transResult, filter);
                        break;
                    case TRANSFERGOODS:
                        expression = DbUtil.createStringExpression(saleTrans.transferGoods, filter);
                        break;
                    case VAT:
                        expression = DbUtil.createLongExpression(saleTrans.vat, filter);
                        break;
                    case EXCLUSE_ID_LIST:
                        break;
                    default:
                        result = result.and(BooleanTemplate.create("1 = 0"));
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
    public List<SaleTrans> findSaleTransByStockTransId(Long stockTransId, Date saleTransDate) {
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT * FROM bccs_sale.sale_trans where stock_trans_id = #stockTransId and status <> #status and is_adjust is null");
        if (!DataUtil.isNullObject(saleTransDate)) {
            builder.append(" and sale_trans_date >= trunc(#saleTransDate) ");
        }
        Query query = em.createNativeQuery(builder.toString(), SaleTrans.class);
        query.setParameter("stockTransId", stockTransId);
        query.setParameter("status", Const.SALE_TRANS.SALE_TRANS_STATUS_CANCEL);
        if (!DataUtil.isNullObject(saleTransDate)) {
            query.setParameter("saleTransDate", saleTransDate);
        }
        return query.getResultList();
    }

    @Override
    public List<SaleTransDTO> findTransExpiredPay(Long staffId) throws Exception {
        Long numDay;
        numDay = financeTypeRepo.getFinanceType(staffId);
        if (numDay < 0) {
            return new ArrayList();
        }
        Map<String, Object> params = new HashMap();

        String sql = "SELECT * FROM BCCS_SALE.SALE_TRANS"
                + " WHERE SALE_TRANS_DATE >= TRUNC (SYSDATE) -"
                + " (SELECT TO_NUMBER (osv.VALUE)"
                + " FROM BCCS_SALE.option_set_value osv, BCCS_SALE.option_set os"
                + " WHERE os.CODE = #p_option_set_code"
                + " AND osv.OPTION_SET_ID = os.ID"
                + " AND osv.status = #p_status"
                + " AND os.STATUS = #p_status)"
                + " AND status <> #p_ignore_status"
                + " AND STAFF_ID = #p_staff_id AND sale_trans_type " + DbUtil.createInQuery("sale_trans_type", Arrays.asList(Const.SaleTransType.RETAIL.getValue(), Const.SaleTransType.COLL.getValue())) //(1, 3)
                + " AND NVL(REVENUE_PAY_STATUS, 0) = #p_revenue_pay_status"
                + " AND SALE_TRANS_DATE < TRUNC (SYSDATE) - #p_num_day ";
        params.put("p_staff_id", staffId);
        params.put("p_option_set_code", Const.OPTION_SET.FINANCE_STAFF_MAX_DAY);
        params.put("p_status", Const.STATUS.ACTIVE);
        params.put("p_ignore_status", Const.SaleTransStatus.DESTROY.getValue());
        params.put("p_revenue_pay_status", Const.STATUS_INACTIVE);
        params.put("p_num_day", numDay);

        Query query = em.createNativeQuery(sql, SaleTrans.class);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        DbUtil.setParamInQuery(query, "sale_trans_type", Arrays.asList(Const.SaleTransType.RETAIL.getValue(), Const.SaleTransType.COLL.getValue()));
        return mapper.toDtoBean(query.getResultList());
    }

    @Override
    public List<SaleTransDTO> getListSaleTransByStaffId(Long staffId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append(" SELECT   st.sale_trans_id, st.sale_trans_code ");
        strQuery.append("   FROM   sale_trans st ");
        strQuery.append("  WHERE   st.sale_trans_date >= ");
        strQuery.append("              TRUNC (SYSDATE) ");
        strQuery.append("              - (SELECT   VALUE ");
        strQuery.append("                   FROM   option_set_value ");
        strQuery.append("                  WHERE   option_set_id = ");
        strQuery.append("                              (SELECT   id ");
        strQuery.append("                                 FROM   option_set ");
        strQuery.append("                                WHERE   code = 'FINANCE_STAFF_MAX_DAY')) ");
        strQuery.append("          AND st.status IN (2, 3) ");
        strQuery.append("          AND st.amount_tax > 0 ");
        strQuery.append("          AND st.staff_id = #staffId ");
        strQuery.append("          AND st.request_type = 0 ");
        strQuery.append("          AND st.bankplus_status IN (0, 1, 2, 4) ");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("staffId", staffId);
        List<Object[]> objects = query.getResultList();
        List<SaleTransDTO> result = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object[] object : objects) {
                SaleTransDTO a = new SaleTransDTO();
                a.setSaleTransId(DataUtil.safeToLong(object[0]));
                a.setSaleTransCode(DataUtil.safeToString(object[1]));
                result.add(a);
            }
            return result;
        } else {
            return null;
        }
    }

    @Override
    public List<SaleTransBankplusDTO> getListSaleTransBankplusByStaffId(Long staffId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append(" SELECT   a.sale_trans_bankplus_id, staff_code ");
        strQuery.append("   FROM   sale_trans_bankplus a ");
        strQuery.append("  WHERE   a.status = 1 AND a.owner_id = #staffId AND a.owner_type = 2 ");
        strQuery.append("          AND a.create_date >= ");
        strQuery.append("                 TRUNC (SYSDATE) ");
        strQuery.append("                 - (SELECT   VALUE ");
        strQuery.append("                      FROM   option_set_value ");
        strQuery.append("                     WHERE   option_set_id = ");
        strQuery.append("                                 (SELECT   id ");
        strQuery.append("                                    FROM   option_set ");
        strQuery.append("                                   WHERE   code = 'FINANCE_STAFF_MAX_DAY')) ");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("staffId", staffId);
        List<Object[]> objects = query.getResultList();
        List<SaleTransBankplusDTO> result = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object[] object : objects) {
                SaleTransBankplusDTO saleTransBankplus = new SaleTransBankplusDTO();
                saleTransBankplus.setSaleTransBankplusId(DataUtil.safeToLong(object[0]));
                result.add(saleTransBankplus);
            }
            return result;
        } else {
            return null;
        }
    }


}

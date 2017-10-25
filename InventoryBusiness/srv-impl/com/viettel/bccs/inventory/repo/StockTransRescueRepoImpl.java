package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransRescueDTO;
import com.viettel.bccs.inventory.model.QStockTransRescue;
import com.viettel.bccs.inventory.model.StockTransRescue.COLUMNS;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

public class StockTransRescueRepoImpl implements StockTransRescueRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransRescueRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransRescue stockTransRescue = QStockTransRescue.stockTransRescue;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APPROVESTAFFID:
                        expression = DbUtil.createLongExpression(stockTransRescue.approveStaffId, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockTransRescue.createDate, filter);
                        break;
                    case FROMOWNERID:
                        expression = DbUtil.createLongExpression(stockTransRescue.fromOwnerId, filter);
                        break;
                    case FROMOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTransRescue.fromOwnerType, filter);
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(stockTransRescue.reasonId, filter);
                        break;
                    case REQUESTCODE:
                        expression = DbUtil.createStringExpression(stockTransRescue.requestCode, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockTransRescue.status, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransRescue.stockTransId, filter);
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(stockTransRescue.toOwnerId, filter);
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTransRescue.toOwnerType, filter);
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
    public List<StockTransRescueDTO> searchStockRescue(StockTransRescueDTO stockTransRescueDTO) throws LogicException, Exception {
        List<StockTransRescueDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   a.stock_trans_id stocktransid,");
        builder.append(" a.request_code requestcode,");
        builder.append(" TO_CHAR (a.create_date, 'dd/MM/yyyy HH:mm:ss') strcreatedate,");
        builder.append(" sf.staff_code || ' - ' || sf.name fromstock,");
        builder.append(" sh.shop_code || ' - ' || sh.name tostock,");
        builder.append(" rn.reason_name reasonname,");
        builder.append(" a.from_owner_id,");
        builder.append(" a.to_owner_id,");
        builder.append(" a.status,");
        builder.append(" (SELECT   sv.name");
        builder.append(" FROM   option_set s, option_set_value sv");
        builder.append(" WHERE       s.id = sv.option_set_id");
        builder.append(" AND s.status = 1");
        builder.append(" AND sv.status = 1");
        builder.append(" AND s.code = 'WARRANTY_STATUS'");
        builder.append(" AND sv.VALUE = to_char(a.status))");
        builder.append(" AS strstatus");
        builder.append(" FROM   bccs_im.stock_trans_rescue a,");
        builder.append(" bccs_im.staff sf,");
        builder.append(" bccs_im.shop sh,");
        builder.append(" bccs_im.reason rn");
        if (!DataUtil.isNullObject(stockTransRescueDTO.getFromOwnerId())) {
            builder.append(" WHERE  a.from_owner_id = #fromOwnerId");
            builder.append(" AND a.from_owner_type = 2");
        } else {
            builder.append(" WHERE  a.to_owner_id = #toOwnerId");
            builder.append(" AND a.to_owner_type = 1");
        }

        builder.append(" AND a.from_owner_id = sf.staff_id");
        builder.append(" AND a.to_owner_id = sh.shop_id");
        builder.append(" AND a.reason_id = rn.reason_id");
        builder.append(" AND a.create_date >= to_date(#fromDate,'dd/MM/yyyy')");
        builder.append(" AND a.create_date < to_date(#toDate,'dd/MM/yyyy') + 1");
        if (!DataUtil.isNullOrEmpty(stockTransRescueDTO.getRequestCode())) {
            builder.append(" AND LOWER(a.request_code) like #requestCode");
        }
        if (!DataUtil.isNullOrZero(stockTransRescueDTO.getStatus())) {
            builder.append(" AND a.status = #status");
        }
        builder.append(" ORDER BY   NLSSORT (a.request_code, 'nls_sort=Vietnamese') DESC");
        Query query = em.createNativeQuery(builder.toString());
        if (!DataUtil.isNullObject(stockTransRescueDTO.getFromOwnerId())) {
            query.setParameter("fromOwnerId", stockTransRescueDTO.getFromOwnerId());
        } else {
            query.setParameter("toOwnerId", stockTransRescueDTO.getToOwnerId());
        }
        query.setParameter("fromDate", DateUtil.date2ddMMyyyyString(stockTransRescueDTO.getFromDate()));
        query.setParameter("toDate", DateUtil.date2ddMMyyyyString(stockTransRescueDTO.getToDate()));
        if (!DataUtil.isNullOrEmpty(stockTransRescueDTO.getRequestCode())) {
            query.setParameter("requestCode", "%" + stockTransRescueDTO.getRequestCode().toLowerCase() + "%");
        }
        if (!DataUtil.isNullOrZero(stockTransRescueDTO.getStatus())) {
            query.setParameter("status", stockTransRescueDTO.getStatus());
        }
        List<Object[]> list = query.getResultList();
        for (Object[] ob : list) {
            int index = 0;
            StockTransRescueDTO stockTransRescueDTO1 = new StockTransRescueDTO();
            stockTransRescueDTO1.setStockTransId(DataUtil.safeToLong(ob[index++]));
            stockTransRescueDTO1.setRequestCode(DataUtil.safeToString(ob[index++]));
            stockTransRescueDTO1.setStrCreateDate(DataUtil.safeToString(ob[index++]));
            stockTransRescueDTO1.setFromStock(DataUtil.safeToString(ob[index++]));
            stockTransRescueDTO1.setToStock(DataUtil.safeToString(ob[index++]));
            stockTransRescueDTO1.setReasonName(DataUtil.safeToString(ob[index++]));
            stockTransRescueDTO1.setFromOwnerId(DataUtil.safeToLong(ob[index++]));
            stockTransRescueDTO1.setToOwnerId(DataUtil.safeToLong(ob[index++]));
            stockTransRescueDTO1.setStatus(DataUtil.safeToLong(ob[index++]));
            stockTransRescueDTO1.setStrStaus(DataUtil.safeToString(ob[index]));

            result.add(stockTransRescueDTO1);
        }
        return result;
    }

    @Override
    public Long getMaxId() throws LogicException, Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("select MAX (stock_trans_id) stockTransId FROM stock_trans_rescue");
        Query query = em.createNativeQuery(builder.toString());
        BigDecimal maxId = (BigDecimal) query.getSingleResult();
        if (!DataUtil.isNullOrZero(maxId)) {
            return maxId.longValue() + 1L;
        }
        return 1L;
    }

    @Override
    public Long getReasonId() throws LogicException, Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT   reason_id FROM   reason  WHERE   LOWER (reason_code) = LOWER ('o') AND reason_status = 1");
        Query query = em.createNativeQuery(builder.toString());
        List lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return DataUtil.safeToLong(lst.get(0));
        }
        return 3L;
    }

    @Override
    public boolean isDulicateRequestCode(String requestCode) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("select a.*")
                .append("  from stock_trans_rescue a")
                .append("  where a.request_code =?");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, requestCode);
        //
        return DataUtil.isNullOrEmpty(query.getResultList());
    }
}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.StockInspectReal;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockInspectReal.COLUMNS;
import com.viettel.bccs.inventory.model.QStockInspectReal;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockInspectRealRepoImpl implements StockInspectRealRepoCustom {

    public static final Logger logger = Logger.getLogger(StockInspectRealRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockInspectReal stockInspectReal = QStockInspectReal.stockInspectReal;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockInspectReal.createDate, filter);
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(stockInspectReal.fromSerial, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockInspectReal.quantity, filter);
                        break;
                    case STOCKINSPECTID:
                        expression = DbUtil.createLongExpression(stockInspectReal.stockInspectId, filter);
                        break;
                    case STOCKINSPECTREALID:
                        expression = DbUtil.createLongExpression(stockInspectReal.stockInspectRealId, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(stockInspectReal.toSerial, filter);
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
    public List<StockInspectReal> getStockInspectReal(Long stockInspectId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT   * ");
        builder.append("    FROM   stock_inspect_real");
        builder.append("   WHERE   stock_inspect_id  = #stock_inspect_id");
        Query query = em.createNativeQuery(builder.toString(), StockInspectReal.class);
        query.setParameter("stock_inspect_id", stockInspectId);
        List lst = query.getResultList();
        return lst;
    }

    @Override
    public int deleteStockInspectReal(Long stockInspectId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  delete  stock_inspect_real");
        builder.append("   WHERE   stock_inspect_id  = #stock_inspect_id");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stock_inspect_id", stockInspectId);
        int countDelete = query.executeUpdate();
        return countDelete;
    }
}
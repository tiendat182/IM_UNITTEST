package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.StockInspectReal;
import com.viettel.bccs.inventory.model.StockInspectSys;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockInspectSys.COLUMNS;
import com.viettel.bccs.inventory.model.QStockInspectSys;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockInspectSysRepoImpl implements StockInspectSysRepoCustom {

    public static final Logger logger = Logger.getLogger(StockInspectSysRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockInspectSys stockInspectSys = QStockInspectSys.stockInspectSys;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockInspectSys.createDate, filter);
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(stockInspectSys.fromSerial, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockInspectSys.quantity, filter);
                        break;
                    case STOCKINSPECTID:
                        expression = DbUtil.createLongExpression(stockInspectSys.stockInspectId, filter);
                        break;
                    case STOCKINSPECTSYSID:
                        expression = DbUtil.createLongExpression(stockInspectSys.stockInspectSysId, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(stockInspectSys.toSerial, filter);
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
    public int deleteStockInspectSys(Long stockInspectId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  delete  stock_inspect_sys");
        builder.append("   WHERE   stock_inspect_id  = #stock_inspect_id");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stock_inspect_id", stockInspectId);
        int countDelete = query.executeUpdate();
        return countDelete;
    }
}
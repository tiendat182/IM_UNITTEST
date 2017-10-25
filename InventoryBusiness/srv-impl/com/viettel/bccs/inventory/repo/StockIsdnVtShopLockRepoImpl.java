package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockIsdnVtShopLock.COLUMNS;
import com.viettel.bccs.inventory.model.QStockIsdnVtShopLock;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockIsdnVtShopLockRepoImpl implements StockIsdnVtShopLockRepoCustom {

    public static final Logger logger = Logger.getLogger(StockIsdnVtShopLockRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockIsdnVtShopLock stockIsdnVtShopLock = QStockIsdnVtShopLock.stockIsdnVtShopLock;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ISDN:
                        expression = DbUtil.createStringExpression(stockIsdnVtShopLock.isdn, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockIsdnVtShopLock.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockIsdnVtShopLock.ownerType, filter);
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

    public void deleteShopLock(String isdn) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from stock_isdn_vt_shop_lock where isdn = #isdn");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("isdn", isdn);
        query.executeUpdate();
    }

}
package com.viettel.bccs.partner.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.partner.model.StockOwnerTmp;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.partner.model.StockOwnerTmp.COLUMNS;
import com.viettel.bccs.partner.model.QStockOwnerTmp;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockOwnerTmpRepoImpl implements StockOwnerTmpRepoCustom {

    public static final Logger logger = Logger.getLogger(StockOwnerTmpRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockOwnerTmp stockOwnerTmp = QStockOwnerTmp.stockOwnerTmp;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AGENTID:
                        expression = DbUtil.createLongExpression(stockOwnerTmp.agentId, filter);
                        break;
                    case CHANNELTYPEID:
                        expression = DbUtil.createLongExpression(stockOwnerTmp.channelTypeId, filter);
                        break;
                    case CODE:
                        expression = DbUtil.createStringExpression(stockOwnerTmp.code, filter);
                        break;
                    case CURRENTDEBIT:
                        expression = DbUtil.createLongExpression(stockOwnerTmp.currentDebit, filter);
                        break;
                    case DATERESET:
                        expression = DbUtil.createLongExpression(stockOwnerTmp.dateReset, filter);
                        break;
                    case MAXDEBIT:
                        expression = DbUtil.createLongExpression(stockOwnerTmp.maxDebit, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(stockOwnerTmp.name, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockOwnerTmp.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createStringExpression(stockOwnerTmp.ownerType, filter);
                        break;
                    case STOCKID:
                        expression = DbUtil.createLongExpression(stockOwnerTmp.stockId, filter);
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
    public List<StockOwnerTmp> getStockOwnerTmpByOwnerId(Long ownerId, Long ownerType) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   * ");
        builder.append("    FROM   bccs_im.stock_owner_tmp");
        builder.append("   WHERE   owner_id = #ownerId AND owner_type = #ownerType");
        Query query = em.createNativeQuery(builder.toString(), StockOwnerTmp.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        return query.getResultList();
    }

}
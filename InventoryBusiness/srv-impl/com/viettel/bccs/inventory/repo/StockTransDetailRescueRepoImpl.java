package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransDetailRescueDTO;
import com.viettel.bccs.inventory.model.QStockTransDetailRescue;
import com.viettel.bccs.inventory.model.StockTransDetailRescue.COLUMNS;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockTransDetailRescueRepoImpl implements StockTransDetailRescueRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransDetailRescueRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransDetailRescue stockTransDetailRescue = QStockTransDetailRescue.stockTransDetailRescue;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockTransDetailRescue.createDate, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockTransDetailRescue.prodOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockTransDetailRescue.quantity, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTransDetailRescue.stateId, filter);
                        break;
                    case STOCKTRANSDETAILID:
                        expression = DbUtil.createLongExpression(stockTransDetailRescue.stockTransDetailId, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransDetailRescue.stockTransId, filter);
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
    public List<StockTransDetailRescueDTO> viewDetail(Long stockTransId) throws Exception {
        List<StockTransDetailRescueDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   pr.code productCode,");
        builder.append(" pr.name productName,");
        builder.append(" 'Hàng hỏng' strstatus,");
        builder.append(" a.prod_offer_id prodOfferId,");
        builder.append(" a.stock_trans_id stocktransid,");
        builder.append(" a.quantity");
        builder.append(" FROM   stock_trans_detail_rescue a, product_offering pr");
        builder.append(" WHERE   a.stock_trans_id = #stocktransid");
        builder.append(" AND a.prod_offer_id = pr.prod_offer_id");
        builder.append(" ORDER BY   pr.code");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stocktransid", stockTransId);

        List<Object[]> list = query.getResultList();
        for (Object[] ob : list) {
            int index = 0;
            StockTransDetailRescueDTO stockTransDetailRescueDTO = new StockTransDetailRescueDTO();
            stockTransDetailRescueDTO.setProductCode(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setProductName(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setStatus(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
            stockTransDetailRescueDTO.setStockTransId(DataUtil.safeToLong(ob[index++]));
            stockTransDetailRescueDTO.setQuantity(DataUtil.safeToLong(ob[index]));

            result.add(stockTransDetailRescueDTO);
        }
        return result;
    }

    public List<StockTransDetailRescueDTO> getCountLstDetail(Long stockTransId) throws LogicException, Exception {
        List<StockTransDetailRescueDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append(" select prod_offer_id_return, count (1) as quantity");
        builder.append(" from stock_trans_serial_rescue");
        builder.append(" where stock_trans_id = #stocktransid");
        builder.append(" group by prod_offer_id_return");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stocktransid", stockTransId);
        List<Object[]> list = query.getResultList();
        for (Object[] ob : list) {
            int index = 0;
            StockTransDetailRescueDTO stockTransDetailRescueDTO = new StockTransDetailRescueDTO();
            stockTransDetailRescueDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
            stockTransDetailRescueDTO.setQuantity(DataUtil.safeToLong(ob[index]));

            result.add(stockTransDetailRescueDTO);
        }
        return result;
    }
}
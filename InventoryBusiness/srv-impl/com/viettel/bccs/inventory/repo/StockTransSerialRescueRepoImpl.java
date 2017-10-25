package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.bccs.inventory.model.QStockTransSerialRescue;
import com.viettel.bccs.inventory.model.StockTransSerialRescue.COLUMNS;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockTransSerialRescueRepoImpl implements StockTransSerialRescueRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransSerialRescueRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransSerialRescue stockTransSerialRescue = QStockTransSerialRescue.stockTransSerialRescue;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockTransSerialRescue.createDate, filter);
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(stockTransSerialRescue.fromSerial, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockTransSerialRescue.prodOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockTransSerialRescue.quantity, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTransSerialRescue.stateId, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransSerialRescue.stockTransId, filter);
                        break;
                    case STOCKTRANSSERIALID:
                        expression = DbUtil.createLongExpression(stockTransSerialRescue.stockTransSerialId, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(stockTransSerialRescue.toSerial, filter);
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
    public List<StockTransSerialRescueDTO> viewDetailSerail(Long stockTransId, Long prodOfferId, Long prodOfferIdReturn) throws LogicException, Exception {
        List<StockTransSerialRescueDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   a.prod_offer_id,a.from_serial fromserial, a.to_serial toserial, a.quantity, a.serial_return, a.prod_offer_id_return");
        builder.append(" FROM   stock_trans_serial_rescue a");
        builder.append("    WHERE       1 = 1");
        builder.append(" AND a.stock_trans_id = #stocktransid");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append(" AND a.prod_offer_id = #prodofferid");
        }
        if (!DataUtil.isNullOrZero(prodOfferIdReturn)) {
            builder.append(" AND a.prod_offer_id_return = #prodofferidreturn");
        }
        builder.append(" ORDER BY   NLSSORT (a.from_serial, 'nls_sort=Vietnamese') ASC");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stocktransid", stockTransId);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodofferid", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(prodOfferIdReturn)) {
            query.setParameter("prodofferidreturn", prodOfferIdReturn);
        }

        List<Object[]> list = query.getResultList();
        for (Object[] ob : list) {
            int index = 0;
            StockTransSerialRescueDTO stockTransDetailRescueDTO = new StockTransSerialRescueDTO();
            stockTransDetailRescueDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
            stockTransDetailRescueDTO.setFromSerial(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setToSerial(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
            stockTransDetailRescueDTO.setSerialReturn(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setProdOfferIdReturn(DataUtil.safeToLong(ob[index]));

            result.add(stockTransDetailRescueDTO);
        }
        return result;
    }

    public List<StockTransSerialRescueDTO> getListDetailSerial(Long stockTransId) throws LogicException, Exception {
        List<StockTransSerialRescueDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   pr.code productCode,pr.name productName, a.from_serial,");
        builder.append(" a.prod_offer_id prodOfferId,");
        builder.append(" a.stock_trans_id stockTransId,");
        builder.append(" a.stock_trans_serial_id stockTransSerialId,");
        builder.append(" (select prg.code from product_offering prg where prg.prod_offer_id = a.prod_offer_id_return) prodOfferReturn,");
        builder.append(" a.serial_return serialReturn");
        builder.append(" FROM   stock_trans_serial_rescue a, product_offering pr");
        builder.append(" WHERE   a.stock_trans_id = #stocktransid");
        builder.append(" AND a.prod_offer_id = pr.prod_offer_id");
        builder.append(" ORDER BY   pr.code");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("stocktransid", stockTransId);

        List<Object[]> list = query.getResultList();
        for (Object[] ob : list) {
            int index = 0;
            StockTransSerialRescueDTO stockTransDetailRescueDTO = new StockTransSerialRescueDTO();
            stockTransDetailRescueDTO.setProductCode(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setProductName(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setFromSerial(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
            stockTransDetailRescueDTO.setStockTransId(DataUtil.safeToLong(ob[index++]));
            stockTransDetailRescueDTO.setStockTransSerialId(DataUtil.safeToLong(ob[index++]));
            stockTransDetailRescueDTO.setProdCodeReturn(DataUtil.safeToString(ob[index++]));
            stockTransDetailRescueDTO.setSerialReturn(DataUtil.safeToString(ob[index]));
            result.add(stockTransDetailRescueDTO);
        }
        return result;
    }
}
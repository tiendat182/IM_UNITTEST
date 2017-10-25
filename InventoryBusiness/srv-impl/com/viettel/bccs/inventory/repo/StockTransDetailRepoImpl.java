package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.model.QStockTransDetail;
import com.viettel.bccs.inventory.model.StockTransDetail.COLUMNS;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockTransDetailRepoImpl implements StockTransDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransDetailRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransDetail stockTransDetail = QStockTransDetail.stockTransDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AMOUNT:
                        expression = DbUtil.createLongExpression(stockTransDetail.amount, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockTransDetail.createDatetime, filter);
                        break;
                    case PRICE:
                        expression = DbUtil.createLongExpression(stockTransDetail.price, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockTransDetail.prodOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockTransDetail.quantity, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTransDetail.stateId, filter);
                        break;
                    case STOCKTRANSDETAILID:
                        expression = DbUtil.createLongExpression(stockTransDetail.stockTransDetailId, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransDetail.stockTransId, filter);
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
    public List<StockTransDetailDTO> getDetailByStockTransId(Long stockTransId) throws Exception {
        List<StockTransDetailDTO> listStockDetail = Lists.newArrayList();
        String sql = "select a.stock_trans_detail_id, a.stock_trans_id, a.prod_offer_id,\n" +
                "       a.state_id, a.quantity, a.price, a.amount, b.code, b.name\n" +
                "        from stock_trans_detail a, product_offering b\n" +
                " where a.prod_offer_id = b.prod_offer_id" +
                " and a.stock_trans_id = #stockTransId ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("stockTransId", stockTransId);

        List<Object[]> lstResult = query.getResultList();

        StockTransDetailDTO stockTransDetailDTO;
        for (Object[] object : lstResult) {
            int index = 0;
            stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setStockTransDetailId(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setStockTransId(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setProdOfferId(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setStateId(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setQuantity(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setPrice(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setAmount(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setProdOfferCode(DataUtil.safeToString(object[index++]));
            stockTransDetailDTO.setProdOfferName(DataUtil.safeToString(object[index]));
            listStockDetail.add(stockTransDetailDTO);

        }
        return listStockDetail;
    }

    @Override
    public List<StockTransDetailDTO> getStockTransDetailByStockTransId(Long stockTransId, Long toOwnerType) throws Exception {
        List<StockTransDetailDTO> listStockDetail = Lists.newArrayList();
        String sql = "SELECT   po.prod_offer_id,\n" +
                "           po.code product_code,\n" +
                "           po.name product_name,\n" +
                "           sta.action_code,\n" +
                "           std.stock_trans_id,\n" +
                "           std.state_id,\n" +
                "           to_char(std.create_datetime, 'dd/MM/yyyy HH24:mi:ss'),\n" +
                "           (SELECT   name FROM   option_set_value WHERE   VALUE = po.unit AND status = 1 AND option_set_id = (SELECT   id FROM   option_set WHERE   status = 1 AND code = 'STOCK_MODEL_UNIT')) unit,\n" +
                "           std.quantity, po.check_serial \n" +
                "    FROM   stock_trans_detail std, product_offering po, stock_trans_action sta, stock_trans st\n" +
                "   WHERE       std.prod_offer_id = po.prod_offer_id\n" +
                "           AND std.stock_trans_id = sta.stock_trans_id\n" +
                "           AND st.stock_trans_id = sta.stock_trans_id\n" +
                "           AND st.status = 3\n" +
                "           AND sta.status = 2\n" +
                "           AND std.stock_trans_id = #stockTransId\n";
        if (!DataUtil.isNullOrZero(toOwnerType)) {
            sql += " AND st.to_owner_type = #toOwnerType";
        }
        sql += " ORDER BY   std.create_datetime DESC";
        Query query = em.createNativeQuery(sql);
        query.setParameter("stockTransId", stockTransId);
        if (!DataUtil.isNullOrZero(toOwnerType)) {
            query.setParameter("toOwnerType", toOwnerType);
        }

        List<Object[]> lstResult = query.getResultList();

        StockTransDetailDTO stockTransDetailDTO ;
        for (Object[] object : lstResult) {
            int index = 0;
            stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setProdOfferId(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setProdOfferCode(DataUtil.safeToString(object[index++]));
            stockTransDetailDTO.setProdOfferName(DataUtil.safeToString(object[index++]));
            stockTransDetailDTO.setActionCode(DataUtil.safeToString(object[index++]));
            stockTransDetailDTO.setStockTransId(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setStateId(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setCreateDatetimeString(DataUtil.safeToString(object[index++]));
            stockTransDetailDTO.setUnit(DataUtil.safeToString(object[index++]));
            stockTransDetailDTO.setQuantity(DataUtil.safeToLong(object[index++]));
            stockTransDetailDTO.setCheckSerial(DataUtil.safeToLong(object[index]));
            listStockDetail.add(stockTransDetailDTO);
        }
        return listStockDetail;
    }
}
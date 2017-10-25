package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockRequestOrderDetailDTO;
import com.viettel.bccs.inventory.model.QStockRequestOrderDetail;
import com.viettel.bccs.inventory.model.StockOrderDetail;
import com.viettel.bccs.inventory.model.StockRequestOrderDetail.COLUMNS;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class StockRequestOrderDetailRepoImpl implements StockRequestOrderDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(StockRequestOrderDetailRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockRequestOrderDetail stockRequestOrderDetail = QStockRequestOrderDetail.stockRequestOrderDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APPROVEDQUANTITY:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.approvedQuantity, filter);
                        break;
                    case FROMOWNERID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.fromOwnerId, filter);
                        break;
                    case FROMOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.fromOwnerType, filter);
                        break;
                    case FROMSTOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.fromStockTransId, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockRequestOrderDetail.note, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.prodOfferId, filter);
                        break;
                    case REQUESTQUANTITY:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.requestQuantity, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.stateId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.status, filter);
                        break;
                    case STOCKREQUESTORDERDETAILID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.stockRequestOrderDetailId, filter);
                        break;
                    case STOCKREQUESTORDERID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.stockRequestOrderId, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.stockTransId, filter);
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.toOwnerId, filter);
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.toOwnerType, filter);
                        break;
                    case EXPORTTRANSID:
                        expression = DbUtil.createLongExpression(stockRequestOrderDetail.exportTransId, filter);
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
    public List<StockRequestOrderDetailDTO> getListStockRequestOrderDetail(Long stockRequestOrderId, String status) throws Exception {
        List<StockRequestOrderDetailDTO> lsResult = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT   stock_request_order_detail_id, ");
        sql.append("         (SELECT   shop_code || '-' || name FROM   shop WHERE shop_id = a.from_owner_id) from_owner_name, ");
        sql.append("         (SELECT   pot.name FROM   product_offering po, product_offer_type pot WHERE po.product_offer_type_id = pot.product_offer_type_id AND po.prod_offer_id=a.prod_offer_id) prod_offer_type_name, ");
        sql.append("         (SELECT   code || '-' || name FROM   product_offering WHERE prod_offer_id = a.prod_offer_id) prod_offer_name, ");
        sql.append("         (SELECT   osv.name FROM option_set os, option_set_value osv  ");
        sql.append("          WHERE os.id = osv.option_set_id AND os.code = 'GOODS_STATE'  AND osv.VALUE = TO_CHAR (a.state_id)) state_name, ");
        sql.append("         a.request_quantity, ");
        sql.append("         a.approved_quantity, ");
        sql.append("         a.create_datetime ");
        sql.append(" FROM   stock_request_order_detail a ");
        sql.append(" WHERE       1 = 1 ");
        sql.append("         AND stock_request_order_id = #stockRequestOrderId ");
        if (!DataUtil.isNullOrEmpty(status)) {
            sql.append("         AND status = #status ");
        }
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("stockRequestOrderId", stockRequestOrderId);
        if (!DataUtil.isNullOrEmpty(status)) {
            query.setParameter("status", status);
        }
        List<Object[]> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            int index;
            for (Object[] obj : list) {
                index = 0;
                StockRequestOrderDetailDTO orderDetailDTO = new StockRequestOrderDetailDTO();
                orderDetailDTO.setStockRequestOrderDetailId(DataUtil.safeToLong(obj[index++]));
                orderDetailDTO.setFromOwnerName(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setProdOfferTypeName(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setProdOfferName(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setStateName(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setRequestQuantity(DataUtil.safeToLong(obj[index++]));
                orderDetailDTO.setApprovedQuantity(DataUtil.safeToLong(obj[index++]));
                Object createDate = obj[index];
                orderDetailDTO.setCreateDatetime(createDate != null ? (Date) createDate : null);
                lsResult.add(orderDetailDTO);
            }
        }
        return lsResult;
    }

  @Override
    public int deleteStockRequestOrderDetai(Long stockRequestOrderId, String status) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE bccs_im.stock_request_order_detail ");
        sql.append("         SET status = #status, LAST_UPDATETIME = SYSDATE ");
        sql.append("         WHERE stock_request_order_id = #stockRequestOrderId ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("status", status);
        query.setParameter("stockRequestOrderId", stockRequestOrderId);
        return query.executeUpdate();
    }
    public List<StockRequestOrderDetailDTO> getLstDetailTemplate(Long stockRequestOrderId) throws Exception {
        List<StockRequestOrderDetailDTO> lsResult = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT   stock_request_order_detail_id, ");
        sql.append("         (SELECT   osv.description FROM option_set os, option_set_value osv  ");
        sql.append("          WHERE os.id = osv.option_set_id AND os.code = 'PROVINCE_ORDER' AND  osv.name = to_char(a.from_owner_id)) from_owner_name, ");
        sql.append("         (SELECT   osv.description FROM option_set os, option_set_value osv  ");
        sql.append("          WHERE os.id = osv.option_set_id AND os.code = 'PROVINCE_ORDER' AND  osv.name = to_char(a.to_owner_id)) to_owner_name, ");
        sql.append("         (SELECT   code FROM   product_offering WHERE prod_offer_id = a.prod_offer_id) prod_offer_code, ");
        sql.append("         (SELECT   name FROM   product_offering WHERE prod_offer_id = a.prod_offer_id) prod_offer_name, ");
        sql.append("         (SELECT   osv.name FROM option_set os, option_set_value osv  ");
        sql.append("          WHERE os.id = osv.option_set_id AND os.code = 'PRODUCT_OFFER_UNIT' AND  osv.value = (select unit from    product_offering WHERE prod_offer_id = a.prod_offer_id)) unitName, ");
        sql.append("         (SELECT   osv.name FROM option_set os, option_set_value osv  ");
        sql.append("          WHERE os.id = osv.option_set_id AND os.code = 'GOODS_STATE'  AND osv.VALUE = TO_CHAR (a.state_id)) state_name, ");
        sql.append("         a.request_quantity, ");
        sql.append("         a.create_datetime ");
        sql.append(" FROM   stock_request_order_detail a ");
        sql.append(" WHERE       1 = 1 ");
        sql.append("         AND stock_request_order_id = #stockRequestOrderId ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("stockRequestOrderId", stockRequestOrderId);
        List<Object[]> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            int index;
            for (Object[] obj : list) {
                index = 0;
                StockRequestOrderDetailDTO orderDetailDTO = new StockRequestOrderDetailDTO();
                orderDetailDTO.setStockRequestOrderDetailId(DataUtil.safeToLong(obj[index++]));
                orderDetailDTO.setFromOwnerName(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setToOwnerName(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setProdOfferCode(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setProdOfferName(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setUnit(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setStateName(DataUtil.safeToString(obj[index++]));
                orderDetailDTO.setRequestQuantity(DataUtil.safeToLong(obj[index++]));
                Object createDate = obj[index];
                orderDetailDTO.setCreateDatetime(createDate != null ? (Date) createDate : null);
                lsResult.add(orderDetailDTO);
            }
        }
        return lsResult;
    }

    public List<StockRequestOrderDetailDTO> getLstDetailToExport() throws Exception {
        List<StockRequestOrderDetailDTO> lsResult = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT   x.stock_request_order_id,x.from_owner_id,x.to_owner_id ");
        sql.append("   FROM       (  SELECT   stock_request_order_id, from_owner_id, to_owner_id ");
        sql.append("                    FROM   stock_request_order_detail a");
        sql.append("                   WHERE   status = 0 and approved_quantity > 0");
        sql.append("                   GROUP BY   stock_request_order_id, from_owner_id, to_owner_id) x ");
        sql.append("            LEFT JOIN");
        sql.append("                stock_request_order_trans y ");
        sql.append("            ON x.stock_request_order_id = y.stock_request_order_id ");
        sql.append("               AND y.from_owner_id = x.from_owner_id ");
        sql.append("               AND y.to_owner_id = x.to_owner_id ");
        sql.append("               AND y.stock_trans_type = 1  ");
        sql.append("    WHERE   1 = 1 AND (y.retry IS NULL OR y.retry < 5)          ");
        sql.append("      AND EXISTS        ");
        sql.append("          (SELECT   1   ");
        sql.append("            FROM   stock_request_order a, stock_trans_voffice b  ");
        sql.append("          WHERE   a.stock_request_order_id = b.stock_trans_action_id    ");
        sql.append("                  AND a.stock_request_order_id = x.stock_request_order_id    ");
        sql.append("                  AND a.status = 1    ");
        sql.append("                  AND b.status IN (1, 2)    ");
        sql.append("                  AND b.prefix_template = 'GOODS_REVOKE')    ");
        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            int index;
            for (Object[] obj : list) {
                index = 0;
                StockRequestOrderDetailDTO orderDetailDTO = new StockRequestOrderDetailDTO();
                orderDetailDTO.setStockRequestOrderId(DataUtil.safeToLong(obj[index++]));
                orderDetailDTO.setFromOwnerId(DataUtil.safeToLong(obj[index++]));
                orderDetailDTO.setToOwnerId(DataUtil.safeToLong(obj[index]));
                lsResult.add(orderDetailDTO);
            }
        }
        return lsResult;
    }

    public int updateCancelNote(Long stockRequestOrderId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE stock_request_order_detail  ");
        sql.append("  SET status = 2, last_updatetime = sysdate  ");
        sql.append("  WHERE stock_request_order_id = #stockRequestOrderId ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("stockRequestOrderId", stockRequestOrderId);
        return query.executeUpdate();
    }
}
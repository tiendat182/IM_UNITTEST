package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.bccs.inventory.model.StockDeliverDetail;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockDeliverDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QStockDeliverDetail;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockDeliverDetailRepoImpl implements StockDeliverDetailRepoCustom {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<StockDeliverDetail, StockDeliverDetailDTO> mapper = new BaseMapper(StockDeliverDetail.class, StockDeliverDetailDTO.class);

    public static final Logger logger = Logger.getLogger(StockDeliverDetailRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockDeliverDetail stockDeliverDetail = QStockDeliverDetail.stockDeliverDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockDeliverDetail.createDate, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockDeliverDetail.note, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockDeliverDetail.prodOfferId, filter);
                        break;
                    case PRODOFFERTYPEID:
                        expression = DbUtil.createLongExpression(stockDeliverDetail.prodOfferTypeId, filter);
                        break;
                    case QUANTITYREAL:
                        expression = DbUtil.createLongExpression(stockDeliverDetail.quantityReal, filter);
                        break;
                    case QUANTITYSYS:
                        expression = DbUtil.createLongExpression(stockDeliverDetail.quantitySys, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockDeliverDetail.stateId, filter);
                        break;
                    case STOCKDELIVERDETAILID:
                        expression = DbUtil.createLongExpression(stockDeliverDetail.stockDeliverDetailId, filter);
                        break;
                    case STOCKDELIVERID:
                        expression = DbUtil.createLongExpression(stockDeliverDetail.stockDeliverId, filter);
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

    public List<StockDeliverDetailDTO> viewStockTotalFull(Long ownerId, Long ownerType) throws Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT prod_offer_id,product_offer_type_id,state_id,prod_offer_code,prod_offer_name,state_name,current_quantity FROM stock_total_full");
        builder.append(" where current_quantity > 0 and owner_id = #ownerId and owner_type = #ownerType");
        builder.append("           ORDER BY current_quantity DESC, nlssort(prod_offer_name, 'nls_sort=Vietnamese')  ASC");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List<Object[]> result = query.getResultList();
        List<StockDeliverDetailDTO> lst = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object[] item : result) {
                int index = 0;
                StockDeliverDetailDTO stockDeliverDetailDTO = new StockDeliverDetailDTO();
                stockDeliverDetailDTO.setProdOfferId(DataUtil.safeToLong(item[index++]));
                stockDeliverDetailDTO.setProdOfferTypeId(DataUtil.safeToLong(item[index++]));
                stockDeliverDetailDTO.setStateId(DataUtil.safeToLong(item[index++]));
                stockDeliverDetailDTO.setProductCode(DataUtil.safeToString(item[index++]));
                stockDeliverDetailDTO.setProductName(DataUtil.safeToString(item[index++]));
                stockDeliverDetailDTO.setStateName(DataUtil.safeToString(item[index++]));
                stockDeliverDetailDTO.setQuantitySys(DataUtil.safeToLong(item[index]));
                stockDeliverDetailDTO.setQuantityReal(stockDeliverDetailDTO.getQuantitySys());
                lst.add(stockDeliverDetailDTO);
            }
        }
        return lst;
    }

    public List<StockDeliverDetailDTO> getLstDetailByStockDeliverId(Long stockDeliverId) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder(" ");
        sqlBuilder.append(" SELECT   stock_deliver_detail_id, ");
        sqlBuilder.append("          (SELECT   code ");
        sqlBuilder.append("             FROM   product_offering ");
        sqlBuilder.append("            WHERE   prod_offer_id = a.prod_offer_id) ");
        sqlBuilder.append("              productcode, ");
        sqlBuilder.append("          (SELECT   name ");
        sqlBuilder.append("             FROM   product_offering ");
        sqlBuilder.append("            WHERE   prod_offer_id = a.prod_offer_id) ");
        sqlBuilder.append("              productname, ");
        sqlBuilder.append("          (SELECT   sv.name ");
        sqlBuilder.append("             FROM   option_set s, option_set_value sv ");
        sqlBuilder.append("            WHERE       s.id = sv.option_set_id ");
        sqlBuilder.append("                    AND s.status = 1 ");
        sqlBuilder.append("                    AND sv.status = 1 ");
        sqlBuilder.append("                    AND s.code = 'GOODS_STATE' ");
        sqlBuilder.append("                    AND TRIM (sv.VALUE) = TO_CHAR (a.state_id)) ");
        sqlBuilder.append("              AS statename, ");
        sqlBuilder.append("          a.quantity_sys, ");
        sqlBuilder.append("          a.quantity_real ");
        sqlBuilder.append("   FROM   stock_deliver_detail a where a.stock_Deliver_id = #stockDeliverId");
        Query query = em.createNativeQuery(sqlBuilder.toString());
        query.setParameter("stockDeliverId", stockDeliverId);
        List<Object[]> result = query.getResultList();
        List<StockDeliverDetailDTO> lstDetail = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object[] item : result) {
                int index = 0;
                StockDeliverDetailDTO stockDeliverDetailDTO = new StockDeliverDetailDTO();
                stockDeliverDetailDTO.setStockDeliverDetailId(DataUtil.safeToLong(item[index++]));
                stockDeliverDetailDTO.setProductCode(DataUtil.safeToString(item[index++]));
                stockDeliverDetailDTO.setProductName(DataUtil.safeToString(item[index++]));
                stockDeliverDetailDTO.setStateName(DataUtil.safeToString(item[index++]));
                stockDeliverDetailDTO.setQuantitySys(DataUtil.safeToLong(item[index]));
                stockDeliverDetailDTO.setQuantityReal(stockDeliverDetailDTO.getQuantitySys());
                lstDetail.add(stockDeliverDetailDTO);
            }
        }
        return lstDetail;
    }
}
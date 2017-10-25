package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.StockCycleDTO;
import com.viettel.bccs.inventory.dto.StockTotalCycleDTO;
import com.viettel.bccs.inventory.dto.StockTotalCycleReportDTO;
import com.viettel.bccs.inventory.model.QStockTotalCycle;
import com.viettel.bccs.inventory.model.StockTotalCycle.COLUMNS;
import com.viettel.common.util.DateTimeUtils;
import com.viettel.fw.common.util.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.ws.common.utils.Locate;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.eclipse.persistence.config.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StockTotalCycleRepoImpl implements StockTotalCycleRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTotalCycleRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private OptionSetValueRepo optionSetValueRepo;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTotalCycle stockTotalCycle = QStockTotalCycle.stockTotalCycle;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockTotalCycle.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockTotalCycle.createUser, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockTotalCycle.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTotalCycle.ownerType, filter);
                        break;
                    case PRICECOST:
                        expression = DbUtil.createLongExpression(stockTotalCycle.priceCost, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockTotalCycle.prodOfferId, filter);
                        break;
                    case PRODUCTOFFERTYPEID:
                        expression = DbUtil.createLongExpression(stockTotalCycle.productOfferTypeId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockTotalCycle.quantity, filter);
                        break;
                    case QUANTITYCYCLE1:
                        expression = DbUtil.createLongExpression(stockTotalCycle.quantityCycle1, filter);
                        break;
                    case QUANTITYCYCLE2:
                        expression = DbUtil.createLongExpression(stockTotalCycle.quantityCycle2, filter);
                        break;
                    case QUANTITYCYCLE3:
                        expression = DbUtil.createLongExpression(stockTotalCycle.quantityCycle3, filter);
                        break;
                    case QUANTITYCYCLE4:
                        expression = DbUtil.createLongExpression(stockTotalCycle.quantityCycle4, filter);
                        break;
                    case RETAILPRICE:
                        expression = DbUtil.createLongExpression(stockTotalCycle.retailPrice, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTotalCycle.stateId, filter);
                        break;
                    case STOCKTOTALCYCLEID:
                        expression = DbUtil.createLongExpression(stockTotalCycle.stockTotalCycleId, filter);
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
    public List<StockTotalCycleReportDTO> getStockCycle(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception {
        List<StockTotalCycleReportDTO> result = Lists.newArrayList();
        List params = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        builder.append(" select * from (  select to_char(create_datetime, 'dd/MM/yyyy') create_datetime, sum(quantity_cycle) , sum(quantity_cycle_1), sum(quantity_cycle_2), sum(quantity_cycle_3), sum(quantity_cycle_4), sum(quantity_over), sum(quantity),");
        builder.append(" sum(nvl(quantity_cycle, 0) * nvl(price_cost, 0)) priceCostQuantityCycle, sum(nvl(quantity_cycle_1, 0) * nvl(price_cost, 0)) priceCostQuantityCycle1, sum(nvl(quantity_cycle_2, 0) * nvl(price_cost, 0)) priceCostQuantityCycle2, sum(nvl(quantity_cycle_3, 0) * nvl(price_cost, 0)) priceCostQuantityCycle3, sum(nvl(quantity_cycle_4, 0) * nvl(price_cost, 0)) priceCostQuantityCycle4,  ");
        builder.append(" sum(nvl(quantity_cycle, 0) * nvl(retail_price, 0)) retailPriceQuantityCycle, sum(nvl(quantity_cycle_1, 0) * nvl(retail_price, 0)) retailPriceQuantityCycle1, sum(nvl(quantity_cycle_2, 0) * nvl(retail_price, 0)) retailPriceQuantityCycle2, sum(nvl(quantity_cycle_3, 0) * nvl(retail_price, 0)) retailPriceQuantityCycle3, sum(nvl(quantity_cycle_4, 0) * nvl(retail_price, 0)) retailPriceQuantityCycle4  ");
        builder.append(" from stock_total_cycle a where 1 = 1 ");
        builder.append(" and a.create_datetime >= trunc(?) ");
        builder.append(" and a.create_datetime <= trunc(?) + 1 ");
        params.add(fromDate);
        params.add(toDate);

        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append(" and a.state_id=?");
            params.add(stateId);
        }
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            builder.append(" and a.product_offer_type_id=?");
            params.add(productOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append(" and a.prod_offer_id=?");
            params.add(prodOfferId);
        }
        if (!DataUtil.isNullOrZero(ownerType) && !DataUtil.isNullOrZero(ownerId)) {
            builder.append(" and a.owner_type=?");
            builder.append(" and a.owner_id=?");
            params.add(ownerType);
            params.add(ownerId);
        }
        builder.append(" group by to_char(create_datetime, 'dd/MM/yyyy') ) order by create_datetime ");
        Query query = em.createNativeQuery(builder.toString());
        int indexx = 0;
        for (Object obj : params) {
            query.setParameter(++indexx, obj);
        }
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTotalCycleReportDTO stockTotalCycle = new StockTotalCycleReportDTO();
                stockTotalCycle.setCreateDatetime(DataUtil.safeToString(ob[index++]));
                stockTotalCycle.setQuantityCycle(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setQuantityCycle1(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setQuantityCycle2(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setQuantityCycle3(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setQuantityCycle4(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setQuantityOver(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setQuantity(DataUtil.safeToLong(ob[index++]));

                stockTotalCycle.setPriceCostQuantityCycle(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setPriceCostQuantityCycle1(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setPriceCostQuantityCycle2(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setPriceCostQuantityCycle3(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setPriceCostQuantityCycle4(DataUtil.safeToLong(ob[index++]));

                stockTotalCycle.setRetailPriceQuantityCycle(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setRetailPriceQuantityCycle1(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setRetailPriceQuantityCycle2(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setRetailPriceQuantityCycle3(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setRetailPriceQuantityCycle4(DataUtil.safeToLong(ob[index]));
                result.add(stockTotalCycle);
            }
        }
        return result;
    }

    @Override
    public List<StockTotalCycleDTO> getStockCycleExport(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId,
                                                        Long prodOfferId, Long ownerType, Long ownerId) throws Exception {
        List<StockTotalCycleDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        builder.append("	SELECT   s.shop_code, s.name shopName,po.code prodOfferCode, po.name prodOfferName, stc.quantity, stc.price_cost,stc.retail_price ");
        builder.append("	FROM   stock_total_cycle stc, shop s, product_offering po ");
        builder.append("	WHERE       1 = 1 ");
        builder.append("		AND stc.owner_id = s.shop_id ");
        builder.append("		AND stc.prod_offer_id = po.prod_offer_id ");
        builder.append("		AND stc.create_datetime >= trunc(#fromDate) ");
        builder.append("		AND stc.create_datetime <= trunc(#toDate) + 1 ");

        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("		AND stc.state_id=#stateId ");
        }
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            builder.append("		AND stc.product_offer_type_id=#prodOfferTypeId ");
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("          AND stc.prod_offer_id=#prodOfferId ");
        }
        if (!DataUtil.isNullOrZero(ownerType) && !DataUtil.isNullOrZero(ownerId)) {
            builder.append("		AND stc.owner_type=#ownerType ");
            builder.append("		AND stc.owner_id=#ownerId ");
        }
        builder.append(" ORDER BY s.shop_code,s.name, po.code ");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            query.setParameter("prodOfferTypeId", productOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(ownerType) && !DataUtil.isNullOrZero(ownerId)) {
            query.setParameter("ownerType", ownerType);
            query.setParameter("ownerId", ownerId);
        }

        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTotalCycleDTO stockTotalCycle = new StockTotalCycleDTO();
                stockTotalCycle.setShopCode(DataUtil.safeToString(ob[index++]));
                stockTotalCycle.setShopName(DataUtil.safeToString(ob[index++]));
                stockTotalCycle.setProdOfferCode(DataUtil.safeToString(ob[index++]));
                stockTotalCycle.setProdOfferName(DataUtil.safeToString(ob[index++]));
                stockTotalCycle.setQuantity(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setPriceCost(DataUtil.safeToLong(ob[index++]));
                stockTotalCycle.setRetailPrice(DataUtil.safeToLong(ob[index]));
                result.add(stockTotalCycle);
            }
        }
        return result;
    }

    @Override
    public List<String> getListSerialStockXByDayRemain(Long prodOfferId, Long ownerId, Long ownerType, Long typeCycle, String tableName, Long dayStockRemain, Long stateId) throws Exception {

        boolean isNotNullProdOfferId = (prodOfferId != null);

        StringBuilder builder = new StringBuilder("");

        if (isNotNullProdOfferId) {
            builder.append("	SELECT   serial, to_char(create_date,'dd/MM/yyyy') create_date ");
            builder.append("	FROM   " + tableName);
            builder.append("	WHERE       1 = 1 ");
            builder.append("		AND owner_id = #ownerId ");
            builder.append("		AND owner_type = #ownerType ");
            builder.append("		AND prod_offer_id = #prodOfferId ");
            builder.append("		AND state_id = #stateId ");
            builder.append("		AND status in (1,3) ");

            if (!Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE_TOTAL.equals(typeCycle)) {//tong ton kho
                if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE.equals(typeCycle)) {
                    builder.append("		and create_date >= TRUNC (SYSDATE) - #dayStockRemain ");//ton trong ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE1.equals(typeCycle)) {
                    builder.append("		and create_date < TRUNC (SYSDATE) - #dayStockRemain and create_date >= TRUNC (SYSDATE) - #dayStockRemain - #dayStockCycle ");//ton kho 1 chu ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE2.equals(typeCycle)) {
                    builder.append("		and create_date < TRUNC (SYSDATE) - #dayStockRemain - #dayStockCycle and create_date >= TRUNC (SYSDATE) - #dayStockRemain - 2 * #dayStockCycle ");//ton kho 2 chu ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE3.equals(typeCycle)) {
                    builder.append("		and create_date < TRUNC (SYSDATE) - #dayStockRemain - 2 * #dayStockCycle and create_date >= TRUNC (SYSDATE) - #dayStockRemain - 3 * #dayStockCycle    ");//ton kho 3 chu ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE4.equals(typeCycle)) {
                    builder.append("		and create_date < TRUNC (SYSDATE) - #dayStockRemain - 3 * #dayStockCycle and create_date >= TRUNC (SYSDATE) - #dayStockRemain - 4 * #dayStockCycle     ");//ton kho 4 chu ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE4_OVER.equals(typeCycle)) {
                    builder.append("		and create_date < TRUNC (SYSDATE) - #dayStockRemain - 4 * #dayStockCycle  ");//ton kho qua 4 chu ky
                }
            }
        } else {
            builder.append("	SELECT   a.serial, to_char(a.create_date,'dd/MM/yyyy') create_date, b.code, b.name ");
            builder.append("	FROM bccs_im." + tableName + " a ");
            builder.append("		,bccs_im.product_offering b ");
            builder.append("	WHERE       1 = 1 ");
            builder.append("	AND a.prod_offer_id=b.prod_offer_id ");

            builder.append("		AND a.owner_id = #ownerId ");
            builder.append("		AND a.owner_type = #ownerType ");
            builder.append("		AND a.state_id = #stateId ");
            builder.append("		AND a.status in (1,3) ");

            String strDayStockRemain = isNotNullProdOfferId ? " #dayStockRemain " : " nvl(b.day_stock_remain, 0) ";

            if (!Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE_TOTAL.equals(typeCycle)) {//tong ton kho
                if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE.equals(typeCycle)) {
                    builder.append("		and a.create_date >= TRUNC (SYSDATE) - " + strDayStockRemain);//ton trong ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE1.equals(typeCycle)) {
                    builder.append("		and a.create_date < TRUNC (SYSDATE) - " + strDayStockRemain + " and a.create_date >= TRUNC (SYSDATE) - " + strDayStockRemain + " - #dayStockCycle ");//ton kho 1 chu ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE2.equals(typeCycle)) {
                    builder.append("		and a.create_date < TRUNC (SYSDATE) - " + strDayStockRemain + " - #dayStockCycle and a.create_date >= TRUNC (SYSDATE) - " + strDayStockRemain + " - 2 * #dayStockCycle ");//ton kho 2 chu ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE3.equals(typeCycle)) {
                    builder.append("		and a.create_date < TRUNC (SYSDATE) - " + strDayStockRemain + " - 2 * #dayStockCycle and a.create_date >= TRUNC (SYSDATE) - " + strDayStockRemain + " - 3 * #dayStockCycle    ");//ton kho 3 chu ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE4.equals(typeCycle)) {
                    builder.append("		and a.create_date < TRUNC (SYSDATE) - " + strDayStockRemain + " - 3 * #dayStockCycle and a.create_date >= TRUNC (SYSDATE) - " + strDayStockRemain + " - 4 * #dayStockCycle     ");//ton kho 4 chu ky
                } else if (Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE4_OVER.equals(typeCycle)) {
                    builder.append("		and a.create_date < TRUNC (SYSDATE) - " + strDayStockRemain + " - 4 * #dayStockCycle  ");//ton kho qua 4 chu ky
                }
            }
        }

        builder.append(" ORDER BY serial asc, create_date asc ");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        if (isNotNullProdOfferId) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        query.setParameter("stateId", stateId);
        if (!Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE_TOTAL.equals(typeCycle)) {
            if (isNotNullProdOfferId) {
                query.setParameter("dayStockRemain", DataUtil.safeToLong(dayStockRemain));
            }
            if (!Const.STOCK_CYCLE_TYPE.EXIST_IN_CYCLE.equals(typeCycle)) {
                Long dayStockCycle = DataUtil.safeToLong(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.DAY_STOCK_CYCLE, Const.OPTION_SET.DAY_STOCK_CYCLE));
                query.setParameter("dayStockCycle", dayStockCycle);
            }
        }
        List<String> lsResult = Lists.newArrayList();
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] obj : queryResult) {
                lsResult.add(isNotNullProdOfferId ? (DataUtil.safeToString(obj[0]) + "\t" + DataUtil.safeToString(obj[1]) + "\n")
                        : (DataUtil.safeToString(obj[0]) + "\t" + DataUtil.safeToString(obj[1]) + "\t" + DataUtil.safeToString(obj[2]) + "\t" + DataUtil.safeToString(obj[3]) + "\n"));
            }
        }
        return lsResult;
    }

    @Override
    public List<StockTotalCycleDTO> getStockTotalCycleHandset(Long ownerType, Long prodOfferId, Long dayStockRemain) throws Exception {
        List<StockTotalCycleDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT prod_offer_id, owner_type, owner_id, state_id, SUM (total), SUM (total_cycleOver)");
        builder.append("     FROM (SELECT prod_offer_id, owner_type, owner_id, state_id, 1 total, ");
        builder.append("                  (CASE WHEN (create_date is null or create_date < TRUNC (SYSDATE) - #dayStockRemain) THEN 1 ELSE 0 END) total_cycleOver ");
        builder.append("             FROM bccs_im.stock_handset");
        builder.append("            WHERE prod_offer_id = #prodOfferId");
        builder.append("              AND status in (1, 3, 4, 6) ");
        builder.append("              AND owner_type = #ownerType ) ");
        builder.append(" GROUP BY prod_offer_id, owner_type, owner_id, state_id");

        Query query = em.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 900);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("dayStockRemain", dayStockRemain);

        List<Object[]> queryResult = query.getResultList();
        int index;
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                index = 0;
                StockTotalCycleDTO stockTotalCycleDTO = new StockTotalCycleDTO();
                stockTotalCycleDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantityOver(DataUtil.safeToLong(ob[index]));

                result.add(stockTotalCycleDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockTotalCycleDTO> getStockTotalCycleHandset(Long productOfferTypeId) throws Exception {
        List<StockTotalCycleDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT   prod_offer_id, owner_type, owner_id, state_id, min (price_cost), min (day_stock_remain), SUM (total), SUM (total_cycleover)");
        builder.append("     FROM   (SELECT   a.prod_offer_id, a.owner_type, a.owner_id, a.state_id, NVL (b.price_cost, 0) price_cost, 1 total, NVL (b.day_stock_remain, 210) day_stock_remain, ");
        builder.append("                      (CASE WHEN (a.create_date IS NULL OR a.create_date < TRUNC (SYSDATE) - NVL (b.day_stock_remain, 210)) ");
        builder.append("                           THEN 1 ELSE 0 ");
        builder.append("                       END) total_cycleover ");
        builder.append("               FROM   stock_handset a, product_offering b, v_shop_staff c,");
        builder.append("                      channel_type d, shop e, channel_type f");
        builder.append("              WHERE   a.prod_offer_id = b.prod_offer_id ");
        builder.append("               AND a.owner_id = c.owner_id ");
        builder.append("               AND a.owner_type = c.owner_type ");
        builder.append("               AND c.channel_type_id = d.channel_type_id ");
        builder.append("               AND c.parent_shop_id = e.shop_id ");
        builder.append("               AND e.channel_type_id = f.channel_type_id ");
        builder.append("               AND d.is_vt_unit = 1 ");
        builder.append("               AND f.is_vt_unit = 1 ");
        builder.append("               AND a.status IN (1, 3, 4, 6) ");
        builder.append("               AND b.product_offer_type_id = #productOfferTypeId) ");
        builder.append(" GROUP BY   prod_offer_id, owner_type, owner_id, state_id ");

        Query query = em.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 900);
        query.setParameter("productOfferTypeId", productOfferTypeId);

        List<Object[]> queryResult = query.getResultList();
        int index;
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                index = 0;
                StockTotalCycleDTO stockTotalCycleDTO = new StockTotalCycleDTO();
                stockTotalCycleDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setPriceCost(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setDayStockRemain(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantityOver(DataUtil.safeToLong(ob[index]));

                result.add(stockTotalCycleDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockTotalCycleDTO> getStockTotalCycle(Long ownerType, Long ownerId, Long prodOfferId, Long dayStockRemain, String tableName, Long dayStockCycle) throws Exception {
        List<StockTotalCycleDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT prod_offer_id, owner_type, owner_id, state_id," +
                "       SUM (total), SUM (total_cycle), SUM (total_cycle1), SUM (total_cycle2), SUM (total_cycle3), SUM (total_cycle4), SUM (total_cycleOver)" +
                "  FROM (SELECT prod_offer_id, owner_type, owner_id, state_id, 1 total, " +
                "        (CASE WHEN create_date >= TRUNC (SYSDATE) - #dayStockRemain THEN 1 ELSE 0 END) total_cycle," +
                "        (CASE WHEN create_date < TRUNC (SYSDATE) - #dayStockRemain and create_date >= TRUNC (SYSDATE) - #dayStockRemain - #dayStockCycle  THEN 1 ELSE 0 END) total_cycle1," +
                "        (CASE WHEN create_date < TRUNC (SYSDATE) - #dayStockRemain - #dayStockCycle and create_date >= TRUNC (SYSDATE) - #dayStockRemain - 2 * #dayStockCycle  THEN 1 ELSE 0 END) total_cycle2," +
                "        (CASE WHEN create_date < TRUNC (SYSDATE) - #dayStockRemain - 2 * #dayStockCycle and create_date >= TRUNC (SYSDATE) - #dayStockRemain - 3 * #dayStockCycle  THEN 1 ELSE 0 END) total_cycle3," +
                "        (CASE WHEN create_date < TRUNC (SYSDATE) - #dayStockRemain - 3 * #dayStockCycle and create_date >= TRUNC (SYSDATE) - #dayStockRemain - 4 * #dayStockCycle  THEN 1 ELSE 0 END) total_cycle4," +
                "        (CASE WHEN create_date < TRUNC (SYSDATE) - #dayStockRemain - 4 * #dayStockCycle  THEN 1 ELSE 0 END) total_cycleOver " +
                "          FROM bccs_im." + tableName +
                "         WHERE     prod_offer_id = #prodOfferId" +
                "           AND status in (1, 3) " +
                "           AND owner_type = #ownerType     " +
                "           AND owner_id = #ownerId         " +
                "       )" +
                "GROUP BY prod_offer_id," +
                "         owner_type," +
                "         owner_id," +
                "         state_id ");

        Query query = em.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 900);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        query.setParameter("dayStockRemain", dayStockRemain);
        query.setParameter("dayStockCycle", dayStockCycle);

        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTotalCycleDTO stockTotalCycleDTO = new StockTotalCycleDTO();
                stockTotalCycleDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantityCycle(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantityCycle1(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantityCycle2(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantityCycle3(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantityCycle4(DataUtil.safeToLong(ob[index++]));
                stockTotalCycleDTO.setQuantityOver(DataUtil.safeToLong(ob[index]));
                result.add(stockTotalCycleDTO);
            }
        }
        return result;
    }

    @Override
    public int insertStockTotalCycle(Long productOfferTypeId, Date currentDate, List<StockTotalCycleDTO> stockTotalCycleDTOs, Connection conn) throws Exception {
        int result = 0;
        PreparedStatement stmt = null;
        try {
            if (conn == null || conn.isClosed()) {
                return result;
            }
            StringBuilder sqlInsert = new StringBuilder(" ");
            sqlInsert.append("INSERT INTO stock_total_cycle ( stock_total_cycle_id, owner_type, owner_id,");
            sqlInsert.append("        product_offer_type_id, prod_offer_id, state_id, quantity,");
            sqlInsert.append("        quantity_cycle, quantity_cycle_1, quantity_cycle_2,");
            sqlInsert.append("        quantity_cycle_3, quantity_cycle_4, quantity_over, price_cost,");
            sqlInsert.append("        retail_price, create_datetime, create_user )");
            sqlInsert.append(" VALUES (stock_total_cycle_seq.nextval, ?, ?, ?, ?, ?, ");
            sqlInsert.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
            sqlInsert.append(" trunc(?), ?");
            sqlInsert.append(" )");
            if (!DataUtil.isNullOrEmpty(stockTotalCycleDTOs)) {
                stmt = conn.prepareStatement(sqlInsert.toString());
                int count = 0;
                int commitBatch = 0;
                int index = 1;
                for (StockTotalCycleDTO stockTotalCycleDTO : stockTotalCycleDTOs) {
                    stmt.setLong(index++, stockTotalCycleDTO.getOwnerType());
                    stmt.setLong(index++, stockTotalCycleDTO.getOwnerId());
                    stmt.setLong(index++, productOfferTypeId);
                    stmt.setLong(index++, stockTotalCycleDTO.getProdOfferId());
                    stmt.setLong(index++, stockTotalCycleDTO.getStateId());
                    stmt.setLong(index++, stockTotalCycleDTO.getQuantity());
                    stmt.setLong(index++, stockTotalCycleDTO.getQuantityCycle());
                    stmt.setLong(index++, stockTotalCycleDTO.getQuantityCycle1());
                    stmt.setLong(index++, stockTotalCycleDTO.getQuantityCycle2());
                    stmt.setLong(index++, stockTotalCycleDTO.getQuantityCycle3());
                    stmt.setLong(index++, stockTotalCycleDTO.getQuantityCycle4());
                    stmt.setLong(index++, stockTotalCycleDTO.getQuantityOver());
                    stmt.setLong(index++, stockTotalCycleDTO.getPriceCost());
                    stmt.setLong(index++, 0L);
                    stmt.setDate(index++, DateTimeUtils.convertToSqlDate(currentDate));
                    stmt.setString(index, "SYSTEM");
                    stmt.addBatch();
                    count++;
                    commitBatch++;
                    if (commitBatch >= Const.BATCH_SIZE_1000) {
                        stmt.executeBatch();
                        conn.commit();
                        commitBatch = 0;
                    }
                    index = 1;
                }
                if (commitBatch > 0) {
                    stmt.executeBatch();
                    conn.commit();
                }
                result = count;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return result;
    }

    public List<StockTotalCycleDTO> findStockOver(Long ownerId, Long ownerType) throws Exception {

        List<StockTotalCycleDTO> result = Lists.newArrayList();
        try {
            StringBuilder sqlQuery = new StringBuilder(" ");
            sqlQuery.append("SELECT  SUM (quantity*price_cost) - SUM (quantity_over*price_cost) quantityCycle, SUM (quantity_over*price_cost) quantity_over");
            sqlQuery.append("  FROM  stock_total_cycle");
            sqlQuery.append(" WHERE  create_datetime >= SYSDATE - 6");
            sqlQuery.append("  AND  owner_id = #ownerId ");
            sqlQuery.append("  AND  owner_type = #ownerType ");

            Query query = em.createNativeQuery(sqlQuery.toString());
            query.setHint(QueryHints.JDBC_TIMEOUT, 3);
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
            Locate new_locate = GetTextFromBundleHelper.getLocate();
            new_locate.setCountry("VN");
            new_locate.setLanguage("vi");
            List<Object[]> queryResult = query.getResultList();
            if (!DataUtil.isNullOrEmpty(queryResult)) {
                for (Object[] ob : queryResult) {
                    StockTotalCycleDTO stockTotalCycleDTO = new StockTotalCycleDTO();
                    stockTotalCycleDTO.setProdOfferName(BundleUtil.getText(new_locate, "dashboard.bar.barMode1.inStock"));
                    stockTotalCycleDTO.setQuantity(DataUtil.safeToLong(ob[0]));
                    result.add(stockTotalCycleDTO);

                    StockTotalCycleDTO stockTotalCycleDTO1 = new StockTotalCycleDTO();
                    stockTotalCycleDTO1.setProdOfferName(BundleUtil.getText(new_locate, "dashboard.bar.barMode1.outStock"));
                    stockTotalCycleDTO1.setQuantity(DataUtil.safeToLong(ob[1]));
                    result.add(stockTotalCycleDTO1);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    public List<StockTotalCycleDTO> findStockOverByStateId(Long ownerId, Long ownerType) throws Exception {

        List<StockTotalCycleDTO> result = Lists.newArrayList();
        try {
            StringBuilder sqlQuery = new StringBuilder(" ");
            sqlQuery.append("SELECT  c.name, a.quantity");
            sqlQuery.append("  FROM  (SELECT  state_id, SUM (quantity*price_cost) quantity");
            sqlQuery.append("           FROM  stock_total_cycle");
            sqlQuery.append("          WHERE  create_datetime >= SYSDATE - 6");
            sqlQuery.append("            AND  owner_id = #ownerId");
            sqlQuery.append("            AND  owner_type = #ownerType");
            sqlQuery.append("       GROUP BY   state_id) a,");
            sqlQuery.append(" option_set b, option_set_value c");
            sqlQuery.append(" WHERE   b.id = c.option_set_id");
            sqlQuery.append("  AND TO_CHAR (a.state_id) = c.VALUE");
            sqlQuery.append("  AND b.code = 'GOODS_STATE'");

            Query query = em.createNativeQuery(sqlQuery.toString());
            query.setHint(QueryHints.JDBC_TIMEOUT, 3);
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);

            List<Object[]> queryResult = query.getResultList();
            if (!DataUtil.isNullOrEmpty(queryResult)) {
                int index;
                for (Object[] ob : queryResult) {
                    index = 0;
                    StockTotalCycleDTO stockTotalCycleDTO = new StockTotalCycleDTO();
                    stockTotalCycleDTO.setProdOfferName(DataUtil.safeToString(ob[index++]));
                    stockTotalCycleDTO.setQuantity(DataUtil.safeToLong(ob[index]));
                    result.add(stockTotalCycleDTO);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    public List<StockTotalCycleDTO> findStockOverByTypeId(Long ownerId, Long ownerType) throws Exception {

        List<StockTotalCycleDTO> result = Lists.newArrayList();
        try {
            StringBuilder sqlQuery = new StringBuilder(" ");

            sqlQuery.append("SELECT   b.name, SUM (a.quantity*price_cost)");
            sqlQuery.append("  FROM   stock_total_cycle a, product_offer_type b");
            sqlQuery.append(" WHERE   1 = 1");
            sqlQuery.append("  AND a.product_offer_type_id = b.product_offer_type_id");
            sqlQuery.append("  AND a.create_datetime >= SYSDATE - 6");
            sqlQuery.append("  AND a.owner_id = #ownerId");
            sqlQuery.append("  AND a.owner_type = #ownerType ");
            sqlQuery.append("GROUP BY   b.name");

            Query query = em.createNativeQuery(sqlQuery.toString());
            query.setHint(QueryHints.JDBC_TIMEOUT, 3);
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);

            List<Object[]> queryResult = query.getResultList();
            if (!DataUtil.isNullOrEmpty(queryResult)) {
                int index;
                for (Object[] ob : queryResult) {
                    index = 0;
                    StockTotalCycleDTO stockTotalCycleDTO = new StockTotalCycleDTO();
                    stockTotalCycleDTO.setProdOfferName(DataUtil.safeToString(ob[index++]));
                    stockTotalCycleDTO.setQuantity(DataUtil.safeToLong(ob[index]));
                    result.add(stockTotalCycleDTO);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public List<StockTotalCycleDTO> getStockTotalCycleSolr(Long prodOfferId, Long dayStockRemain, Const.SOLR_CORE core) throws Exception {

        HashMap<String, StockTotalCycleDTO> lst = new HashMap<>();

        getStockTotalCycleSolr(lst, prodOfferId, dayStockRemain, core, false);
        getStockTotalCycleSolr(lst, prodOfferId, dayStockRemain, core, true);

        return new ArrayList<StockTotalCycleDTO>(lst.values());
    }

    private void getStockTotalCycleSolr(HashMap<String, StockTotalCycleDTO> lst, Long prodOfferId, Long dayStockRemain, Const.SOLR_CORE core, boolean checkCreateDate) throws Exception {
        String[] facetList;

        StringBuilder query = new StringBuilder("*:*");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.append(" AND prod_offer_id:").append(prodOfferId);
        }

        if (checkCreateDate) {
            query.append(" AND create_date:").append("[* TO 20160801]");
        }

        QueryResponse queryResponse = SolrController.getResponse(core, query.toString(), true, "key_search", 1L);
        List<FacetField.Count> counts = queryResponse.getFacetFields().get(0).getValues();
        StockTotalCycleDTO stockTotalCycleDTO;

        for (FacetField.Count c : counts) {
            facetList = c.getName().split("_");
            if (DataUtil.isNullOrZero(prodOfferId) || DataUtil.safeEqual(DataUtil.safeToString(prodOfferId), facetList[2])) {

                if (lst.containsKey(c.getName())) {
                    stockTotalCycleDTO = lst.get(c.getName());
                } else {
                    stockTotalCycleDTO = new StockTotalCycleDTO();
                    stockTotalCycleDTO.setOwnerType(DataUtil.safeToLong(facetList[0]));
                    stockTotalCycleDTO.setOwnerId(DataUtil.safeToLong(facetList[1]));
                    stockTotalCycleDTO.setProdOfferId(DataUtil.safeToLong(facetList[2]));
                    stockTotalCycleDTO.setStateId(DataUtil.safeToLong(facetList[3]));
                }

                if (checkCreateDate) {
                    stockTotalCycleDTO.setQuantityOver(DataUtil.safeToLong(c.getCount()));
                } else {
                    stockTotalCycleDTO.setQuantity(DataUtil.safeToLong(c.getCount()));
                }

                lst.put(c.getName(), stockTotalCycleDTO);
            }
        }
    }

    public void insertSerialCycle(List<StockTotalCycleDTO> lstStockTotalCycle, String tableName, Date currentDate, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        if (conn == null || conn.isClosed()) {
            return;
        }
        try {
            StringBuilder sqlInsert = new StringBuilder(" ");
            sqlInsert.append("INSERT INTO ");
            sqlInsert.append(tableName + "_cycle");
            sqlInsert.append("  (" + tableName + "_cycle_id,");
            sqlInsert.append("  owner_type, owner_id, prod_offer_id, state_id, serial, price_cost,");
            sqlInsert.append("  retail_price, stock_trans_id, create_datetime, import_datetime, day_stock_remain, num_day)");
            sqlInsert.append(" VALUES (" + tableName + "_cycle_seq.nextval, ?, ?, ?, ?, ?, ?, ");
            sqlInsert.append(" ?, ?, trunc(?), ?, ?, ?");
            sqlInsert.append(" )");

            stmt = conn.prepareStatement(sqlInsert.toString());

            int commitBatch = 0;
            int index = 1;
            Long numDay;

            for (StockTotalCycleDTO stockTotalCycle : lstStockTotalCycle) {
                List<StockCycleDTO> lstStockCycleDTO;
                if ("STOCK_HANDSET".equals(tableName)) {
                    lstStockCycleDTO = getSerialDB(stockTotalCycle.getOwnerType(), stockTotalCycle.getOwnerId(),
                            stockTotalCycle.getProdOfferId(), stockTotalCycle.getStateId(), tableName, stockTotalCycle.getDayStockRemain());
                } else {
                    lstStockCycleDTO = getSerialSolr(stockTotalCycle.getOwnerType(), stockTotalCycle.getOwnerId(),
                            stockTotalCycle.getProdOfferId(), stockTotalCycle.getStateId(), tableName, stockTotalCycle.getDayStockRemain());
                }
                if (!DataUtil.isNullOrEmpty(lstStockCycleDTO)) {
                    for (StockCycleDTO stockCycleDTO : lstStockCycleDTO) {
                        stmt.setLong(index++, stockTotalCycle.getOwnerType());
                        stmt.setLong(index++, stockTotalCycle.getOwnerId());
                        stmt.setLong(index++, stockTotalCycle.getProdOfferId());
                        stmt.setLong(index++, stockTotalCycle.getStateId());
                        stmt.setString(index++, stockCycleDTO.getSerial());
                        stmt.setLong(index++, DataUtil.safeToLong(stockTotalCycle.getPriceCost()));
                        stmt.setLong(index++, DataUtil.safeToLong(stockTotalCycle.getRetailPrice()));
                        stmt.setLong(index++, DataUtil.safeToLong(stockCycleDTO.getStockTransId()));
                        stmt.setDate(index++, DateTimeUtils.convertToSqlDate(currentDate));
                        stmt.setDate(index++, DateTimeUtils.convertToSqlDate(DateTimeUtils.convertStringToTime(stockCycleDTO.getCreateDate(), "yyyyMMdd")));
                        stmt.setLong(index++, stockTotalCycle.getDayStockRemain());
                        numDay = DateUtil.daysBetween2Dates(stockTotalCycle.getCreateDatetime(), DateTimeUtils.convertStringToTime(stockCycleDTO.getCreateDate(), "yyyyMMdd"));
                        stmt.setLong(index++, numDay >= stockTotalCycle.getDayStockRemain() ? numDay - stockTotalCycle.getDayStockRemain() : 0L);
                        stmt.addBatch();
                        commitBatch++;
                        if (commitBatch >= Const.DEFAULT_BATCH_SIZE) {
                            stmt.executeBatch();
                            conn.commit();
                            commitBatch = 0;
                        }
                        index = 1;
                    }
                    if (commitBatch > 0) {
                        stmt.executeBatch();
                        conn.commit();
                        commitBatch = 0;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public List<StockCycleDTO> getSerialDB(Long ownerType, Long ownerId, Long prodOfferId, Long stateId, String tableName, Long dayStockRemain) throws Exception {
        List<StockCycleDTO> result = Lists.newArrayList();

        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT serial,to_char (nvl (create_date,to_date('01012012','ddmmyyyy')),'yyyymmdd'), stock_trans_id ");
        builder.append("     FROM bccs_im.");
        builder.append(tableName);
        builder.append("    WHERE  prod_offer_id = #prodOfferId");
        builder.append("      AND status in (1, 3, 4, 6) ");
        builder.append("      AND owner_type = #ownerType     ");
        builder.append("      AND owner_id = #ownerId         ");
        builder.append("      AND state_id = #stateId         ");
        builder.append("      AND not (owner_type=1 and owner_id =224388 and prod_offer_id=20692)  ");//Loai bo kho xu ly du lieu

        Query query = em.createNativeQuery(builder.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 900);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        query.setParameter("stateId", stateId);

        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockCycleDTO stockCycleDTO = new StockCycleDTO();
                stockCycleDTO.setSerial(DataUtil.safeToString(ob[index++]));
                stockCycleDTO.setCreateDate(DataUtil.safeToString(ob[index++]));
                stockCycleDTO.setStockTransId(DataUtil.safeToString(ob[index]));
                result.add(stockCycleDTO);
            }
        }
        return result;
    }

    public List<StockCycleDTO> getSerialSolr(Long ownerType, Long ownerId, Long prodOfferId, Long stateId, String tableName, Long dayStockRemain) throws Exception {

        StringBuilder query = new StringBuilder("");

        query.append(" owner_type:").append(ownerType);
        query.append(" AND owner_id:").append(ownerId);
        query.append(" AND prod_offer_id:").append(prodOfferId);
        query.append(" AND state_id:").append(stateId);

        List<StockCycleDTO> result = SolrController.doSearch(Const.SOLR_CORE.DB_SIM, query.toString(), StockCycleDTO.class, null, null, null);

        return result;
    }

}
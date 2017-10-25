package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockIsdnVtShopFeeDTO;
import com.viettel.bccs.inventory.dto.StockTotalFullDTO;
import com.viettel.bccs.inventory.model.QStockIsdnVtShopFee;
import com.viettel.bccs.inventory.model.StockIsdnVtShopFee;
import com.viettel.bccs.inventory.model.StockIsdnVtShopFee.COLUMNS;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class StockIsdnVtShopFeeRepoImpl implements StockIsdnVtShopFeeRepoCustom {

    public static final Logger logger = Logger.getLogger(StockIsdnVtShopFeeRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockIsdnVtShopFee stockIsdnVtShopFee = QStockIsdnVtShopFee.stockIsdnVtShopFee;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AMOUNT:
                        expression = DbUtil.createLongExpression(stockIsdnVtShopFee.amount, filter);
                        break;
                    case FEETYPE:
                        expression = DbUtil.createStringExpression(stockIsdnVtShopFee.feeType, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(stockIsdnVtShopFee.isdn, filter);
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

    public List<StockIsdnVtShopFeeDTO> findVtShopFeeByIsdn(String isdn) throws Exception {

        List<StockIsdnVtShopFeeDTO> lstResult = new ArrayList<>();
        StringBuilder builder = new StringBuilder("");
        builder.append("SELECT a.isdn, a.fee_type, a.amount, b.pay_status ");
        builder.append("  FROM bccs_im.stock_isdn_vt_shop_fee a, bccs_im.stock_isdn_vt_shop b ");
        builder.append(" WHERE a.isdn = b.isdn ");
        builder.append("   AND a.isdn = #isdn ");

        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("isdn", isdn);

        List<Object[]> list = query.getResultList();

        if (list != null && !list.isEmpty()) {
            for (Object[] item : list) {

                int i = 0;
                StockIsdnVtShopFeeDTO stockIsdnVtShopFeeDTO = new StockIsdnVtShopFeeDTO();
                stockIsdnVtShopFeeDTO.getStockIsdnVtShopFeeId().setIsdn(DataUtil.safeToString(item[i++]));
                stockIsdnVtShopFeeDTO.getStockIsdnVtShopFeeId().setFeeType(DataUtil.safeToString(item[i++]));
                stockIsdnVtShopFeeDTO.setAmount(DataUtil.safeToLong(item[i++]));
                stockIsdnVtShopFeeDTO.setPayStatus(DataUtil.safeToString(item[i]));

                lstResult.add(stockIsdnVtShopFeeDTO);
            }
        }

        return lstResult;
    }

    public void deleteFee(String isdn) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from stock_isdn_vt_shop_fee where isdn = #isdn");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("isdn", isdn);
        query.executeUpdate();
    }

    public List<StockTotalFullDTO> findStockByArea(Long shopId, Long parentShopId, Long prodOfferId) throws Exception {
        List<StockTotalFullDTO> lstResult = new ArrayList<>();
        //Kho quan/huyen bao gom ca kho nhan vien thuoc quan huyen do
        StringBuilder builder = new StringBuilder("");
        builder.append("SELECT province, district, shop_code, shop_name, staff_code, staff_name, prod_offer_code, prod_offer_name, quantity");
        builder.append("  FROM (SELECT b.province, b.district, b.shop_code, b.name shop_name, NULL staff_code, NULL staff_name,");
        builder.append("               c.code prod_offer_code, c.name prod_offer_name, a.available_quantity quantity");
        builder.append("          FROM stock_total a, shop b, product_offering c");
        builder.append("         WHERE a.owner_id = b.shop_id");
        builder.append("           AND a.owner_type = 1");
        builder.append("           AND b.status = 1");
        builder.append("           AND a.prod_offer_id = c.prod_offer_id");
        builder.append("           AND b.shop_id = #shopId");
        builder.append("           AND c.prod_offer_id = #prodOfferId");
        builder.append("           AND a.state_id = #stateId");
        builder.append("           AND a.available_quantity > 0");
        builder.append("        UNION ALL");
        builder.append("       SELECT b.province, b.district, b.shop_code, b.name shop_name, c.staff_code staff_code, c.name staff_name,");
        builder.append("              d.code prod_offer_code, d.name prod_offer_name, a.available_quantity quantity");
        builder.append("         FROM stock_total a, shop b, staff c, product_offering d");
        builder.append("        WHERE b.shop_id = c.shop_id");
        builder.append("          AND a.owner_id = c.staff_id");
        builder.append("          AND b.status = 1");
        builder.append("          AND c.status = 1");
        builder.append("          AND a.prod_offer_id = d.prod_offer_id");
        builder.append("          AND a.owner_type = 2");
        builder.append("          AND d.prod_offer_id = #prodOfferId");
        builder.append("          AND a.state_id = #stateId");
        builder.append("          AND b.shop_id = #shopId");
        builder.append("          AND a.available_quantity > 0) ");
        builder.append("  ORDER BY shop_code, staff_code desc");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("shopId", shopId);
        query.setParameter("stateId", Const.NEW);
        query.setParameter("prodOfferId", prodOfferId);

        List<Object[]> list = query.getResultList();
        lstResult = convertToListStockTotal(list);
        // kho cua hang thuoc quan/huyen
        if (DataUtil.isNullOrEmpty(lstResult)) {
            builder = new StringBuilder("");
            builder.append("SELECT b.province, b.district, b.shop_code, b.name shop_name, NULL staff_code, NULL staff_name,");
            builder.append("               c.code prod_offer_code, c.name prod_offer_name, a.available_quantity quantity");
            builder.append("          FROM stock_total a, shop b, product_offering c");
            builder.append("         WHERE a.owner_id = b.shop_id");
            builder.append("           AND a.owner_type = 1");
            builder.append("           AND b.status = 1");
            builder.append("           AND a.prod_offer_id = c.prod_offer_id");
            builder.append("           AND b.parent_shop_id = #shopId");
            builder.append("           AND c.prod_offer_id = #prodOfferId");
            builder.append("           AND a.state_id = #stateId");
            builder.append("           AND a.available_quantity > 0");
            Query query2 = em.createNativeQuery(builder.toString());
            query2.setParameter("shopId", shopId);
            query2.setParameter("stateId", Const.NEW);
            query2.setParameter("prodOfferId", prodOfferId);

            List<Object[]> list2 = query2.getResultList();
            lstResult = convertToListStockTotal(list2);
            //kho tinh thanh pho
            if (DataUtil.isNullOrEmpty(lstResult)) {
                builder = new StringBuilder("");
                builder.append("SELECT b.province, b.district, b.shop_code, b.name shop_name, NULL staff_code, NULL staff_name,");
                builder.append("               c.code prod_offer_code, c.name prod_offer_name, a.available_quantity quantity");
                builder.append("          FROM stock_total a, shop b, product_offering c");
                builder.append("         WHERE a.owner_id = b.shop_id");
                builder.append("           AND a.owner_type = 1");
                builder.append("           AND b.status = 1");
                builder.append("           AND a.prod_offer_id = c.prod_offer_id");
                builder.append("           AND b.shop_id = #parentShopId");
                builder.append("           AND c.prod_offer_id = #prodOfferId");
                builder.append("           AND a.state_id = #stateId");
                builder.append("           AND a.available_quantity > 0");
                Query query3 = em.createNativeQuery(builder.toString());
                query3.setParameter("parentShopId", parentShopId);
                query3.setParameter("stateId", Const.NEW);
                query3.setParameter("prodOfferId", prodOfferId);

                lstResult = convertToListStockTotal(query3.getResultList());
            }
        }

        return lstResult;
    }

    public List<StockTotalFullDTO> findStockDigital(Long shopId, Long prodOfferId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("SELECT b.province, b.district, b.shop_code, b.name shop_name, NULL staff_code, NULL staff_name,");
        builder.append("               c.code prod_offer_code, c.name prod_offer_name, a.available_quantity quantity");
        builder.append("          FROM stock_total a, shop b, product_offering c");
        builder.append("         WHERE a.owner_id = b.shop_id");
        builder.append("           AND a.owner_type = 1");
        builder.append("           AND a.prod_offer_id = c.prod_offer_id");
        builder.append("           AND b.shop_id = #shopId");
        builder.append("           AND c.prod_offer_id = #prodOfferId");
        builder.append("           AND a.state_id = #stateId");
        builder.append("           AND a.available_quantity > 0");
        Query query2 = em.createNativeQuery(builder.toString());
        query2.setParameter("shopId", shopId);
        query2.setParameter("stateId", Const.NEW);
        query2.setParameter("prodOfferId", prodOfferId);
        return convertToListStockTotal(query2.getResultList());
    }

    private List<StockTotalFullDTO> convertToListStockTotal(List<Object[]> list) {

        List<StockTotalFullDTO> lstResult = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(list)) {
            return lstResult;
        }
        int i;
        for (Object[] item : list) {
            i = 0;
            StockTotalFullDTO stockTotalFullDTO = new StockTotalFullDTO();
            stockTotalFullDTO.setProvince(DataUtil.safeToString(item[i++]));
            stockTotalFullDTO.setDistrict(DataUtil.safeToString(item[i++]));
            stockTotalFullDTO.setShopCode(DataUtil.safeToString(item[i++]));
            stockTotalFullDTO.setShopName(DataUtil.safeToString(item[i++]));
            stockTotalFullDTO.setStaffCode(DataUtil.safeToString(item[i++]));
            stockTotalFullDTO.setStaffName(DataUtil.safeToString(item[i++]));
            stockTotalFullDTO.setProdOfferCode(DataUtil.safeToString(item[i++]));
            stockTotalFullDTO.setProdOfferName(DataUtil.safeToString(item[i++]));
            stockTotalFullDTO.setAvailableQuantity(DataUtil.safeToLong(item[i]));

            lstResult.add(stockTotalFullDTO);
        }
        return lstResult;
    }

}
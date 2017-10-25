package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.StockManagementForWarranty;
import com.viettel.bccs.inventory.dto.StockTotalDTO;
import com.viettel.bccs.inventory.dto.StockTotalFullDTO;
import com.viettel.bccs.inventory.model.ProductOfferType;
import com.viettel.bccs.inventory.model.StockTotal;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockTotal.COLUMNS;
import com.viettel.bccs.inventory.model.QStockTotal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockTotalRepoImpl implements StockTotalRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTotalRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;
    @Autowired
    private ShopService shopService;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTotal stockTotal = QStockTotal.stockTotal;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AVAILABLEQUANTITY:
                        expression = DbUtil.createLongExpression(stockTotal.availableQuantity, filter);
                        break;
                    case CURRENTQUANTITY:
                        expression = DbUtil.createLongExpression(stockTotal.currentQuantity, filter);
                        break;
                    case HANGQUANTITY:
                        expression = DbUtil.createLongExpression(stockTotal.hangQuantity, filter);
                        break;
                    case MODIFIEDDATE:
                        expression = DbUtil.createDateExpression(stockTotal.modifiedDate, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockTotal.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockTotal.ownerType, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockTotal.prodOfferId, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockTotal.stateId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockTotal.status, filter);
                        break;
                    case STOCKTOTALID:
                        expression = DbUtil.createLongExpression(stockTotal.stockTotalId, filter);
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
    public Long getTotalValueStock(Long ownerId, String ownerType) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT nvl(SUM (st.current_quantity * pop.price), 0) ");
        builder.append(" FROM stock_total st, product_offer_price pop, product_offering po ");
        builder.append(" WHERE st.prod_offer_id = pop.prod_offer_id ");
        builder.append("  AND st.prod_offer_id = po.prod_offer_id ");
        builder.append("  AND st.owner_type = #ownerType ");
        builder.append("  AND st.owner_id  = #ownerId ");
        builder.append("  AND st.state_id = #stateId ");
        builder.append("  AND pop.status = #status ");
        builder.append("  AND pop.price_type_id = #priceTypeId ");
        builder.append("  AND pop.price_policy_id = #pricePolicyId ");
        builder.append("  AND pop.effect_datetime <= SYSDATE ");
        builder.append("  AND (pop.expire_datetime IS NULL OR pop.expire_datetime >= TRUNC (SYSDATE))");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        query.setParameter("stateId", Const.STOCK_TOTAL.PRODUCT_NEW);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("priceTypeId", Const.PRODUCT_OFFERING.PRICE_TYPE_ROOT);
        query.setParameter("pricePolicyId", Const.PRODUCT_OFFERING.PRICE_POLICY_VT);
        BigDecimal bi = (BigDecimal) query.getSingleResult();
        if (bi != null) {
            return bi.longValue();
        }
        return 0L;
    }

    @Override
    public Long checkAction(StockTotalDTO stockTotalDTO) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT * FROM stock_total where  ");
        builder.append(" owner_Id =#ownerId and owner_type =#ownerType and prod_offer_id  =#prodOfferId  ");
        builder.append(" and state_id =#stateId FOR UPDATE SKIP LOCKED ");
        Query query = emIM.createNativeQuery(builder.toString(), StockTotal.class);
        query.setParameter("ownerId", stockTotalDTO.getOwnerId());
        query.setParameter("ownerType", stockTotalDTO.getOwnerType());
        query.setParameter("prodOfferId", stockTotalDTO.getProdOfferId());
        query.setParameter("stateId", stockTotalDTO.getStateId());
        int bi = query.getResultList().size();
        return DataUtil.safeToLong(bi);
    }

    @Override
    public List<ProductOfferingDTO> getProductInStockTotal(Long ownerType, Long ownerId, Long prodOfferId, Long productOfferTypeId) throws Exception, LogicException {
        List<ProductOfferingDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT   po.product_offer_type_id, ");
        builder.append("            po.prod_offer_id, ");
        builder.append("            po.code productCode, ");
        builder.append("            po.name productName, ");
        builder.append("            po.check_serial, ");
        builder.append("            st.state_id, ");
        builder.append("            (SELECT   name ");
        builder.append("               FROM   option_set_value ");
        builder.append("              WHERE   VALUE = to_char(st.state_id) AND status = 1 ");
        builder.append("                      AND option_set_id = ");
        builder.append("                             (SELECT   id ");
        builder.append("                                FROM   option_set ");
        builder.append("                               WHERE   status = 1 AND code = 'GOOD_STATE')) ");
        builder.append("                stateName, ");
        builder.append("            st.current_quantity, ");
        builder.append("            st.available_quantity ");
        builder.append("     FROM   stock_total st, product_offering po ");
        builder.append("    WHERE       st.prod_offer_id = po.prod_offer_id ");
        builder.append("            AND po.status = 1 ");
        builder.append("            AND st.status = 1 ");
        builder.append("            AND st.owner_type = #owner_type ");
        builder.append("            AND st.owner_id = #owner_id ");
        builder.append("            AND st.current_quantity > 0 ");
        builder.append("            AND st.available_quantity > 0 ");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("            AND st.prod_offer_id = #prod_offer_id ");
        }
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            builder.append("            AND po.product_offer_type_id = #product_offer_type_id ");
        }

        builder.append(" ORDER BY   po.product_offer_type_id, po.prod_offer_id, st.state_id ");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("owner_type", ownerType);
        query.setParameter("owner_id", ownerId);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prod_offer_id", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(productOfferTypeId)) {
            query.setParameter("product_offer_type_id", productOfferTypeId);
        }
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
                productOfferingDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                productOfferingDTO.setProductOfferingId(DataUtil.safeToLong(ob[index++]));
                productOfferingDTO.setCode(DataUtil.safeToString(ob[index++]));
                productOfferingDTO.setName(DataUtil.safeToString(ob[index++]));
                productOfferingDTO.setCheckSerial(DataUtil.safeToLong(ob[index++]));
                productOfferingDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                productOfferingDTO.setStateIdName(DataUtil.safeToString(ob[index++]));
                productOfferingDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
                productOfferingDTO.setAvailableQuantity(DataUtil.safeToLong(ob[index]));
                result.add(productOfferingDTO);
            }
        }
        return result;
    }

    public Long getStockTotal(StockTotalDTO stockTotalDTO) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT stock_total_id FROM stock_total  ");
        builder.append("  where owner_Id =#ownerId   ");
        builder.append("    and owner_type =#ownerType ");
        builder.append("    and prod_offer_id  =#prodOfferId  ");
        builder.append("    and state_id =#stateId  ");
        Query query = emIM.createNativeQuery(builder.toString(), StockTotal.class);
        query.setParameter("ownerId", stockTotalDTO.getOwnerId());
        query.setParameter("ownerType", stockTotalDTO.getOwnerType());
        query.setParameter("prodOfferId", stockTotalDTO.getProdOfferId());
        query.setParameter("stateId", stockTotalDTO.getStateId());
        int bi = query.getResultList().size();
        return DataUtil.safeToLong(bi);
    }

    @Override
    public List<StockTotalFullDTO> getListStockStockTotalFullForWarranty(Long ownerId, Long ownerType, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT  ");
        builder.append("     st.OWNER_ID                       , ");
        builder.append("     st.OWNER_TYPE                     , ");
        builder.append("     st.PROD_OFFER_ID                  , ");
        builder.append("     st.STATE_ID                       , ");
        builder.append("     st.CURRENT_QUANTITY               , ");
        builder.append("     st.AVAILABLE_QUANTITY             , ");
        builder.append("     st.MODIFIED_DATE                  , ");
        builder.append("     st.STATUS                         , ");
        builder.append("     st.PROD_OFFER_CODE                , ");
        builder.append("     st.PROD_OFFER_NAME                , ");
        builder.append("     st.ACCOUNTING_MODEL_CODE          , ");
        builder.append("     st.ACCOUNTING_MODEL_NAME          , ");
        builder.append("     st.PRODUCT_OFFER_TYPE_ID          , ");
        builder.append("     st.UNIT                           , ");
        builder.append("     st.CHECK_SERIAL                   , ");
        builder.append("     st.STATE_NAME                     , ");
        builder.append("     st.STAFF_CODE1                    , ");
        builder.append("     st.SHOP                           , ");
        builder.append("     st.STAFF_OWNER_ID                 , ");
        builder.append("     st.IS_STAFF                       , ");
        builder.append("     st.STAFF_CODE                       ");
        builder.append(" FROM bccs_im.stock_total_full st ");
        builder.append(" WHERE     st.owner_id = #ownerId ");
        builder.append("		   AND st.owner_type = #ownerType ");
        if (prodOfferTypeId != null) {
            builder.append("		   AND st.product_offer_type_id = #prodOfferTypeId ");
        }
        if (prodOfferId != null) {
            builder.append("		   AND st.prod_offer_id = #prodOfferId ");
        }
        builder.append("		   AND st.status = #status ");
        if (stateId != null) {
            builder.append("		   AND st.state_id = #stateId ");
        }
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("status", Const.STATUS.ACTIVE);
        if (prodOfferTypeId != null) {
            query.setParameter("prodOfferTypeId", prodOfferTypeId);
        }
        if (prodOfferId != null) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (stateId != null) {
            query.setParameter("stateId", stateId);
        }
        List<Object[]> queryResult = query.getResultList();
        List<StockTotalFullDTO> lsResult = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTotalFullDTO stockTotalFullDTO = new StockTotalFullDTO();
                stockTotalFullDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setCurrentQuantity(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setAvailableQuantity(DataUtil.safeToLong(ob[index++]));
                Object modifiedDate = ob[index++];
                stockTotalFullDTO.setModifiedDate(modifiedDate != null ? (Date) modifiedDate : null);
                stockTotalFullDTO.setStatus(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setProdOfferCode(DataUtil.safeToString(ob[index++]));
                stockTotalFullDTO.setProdOfferName(DataUtil.safeToString(ob[index++]));
                stockTotalFullDTO.setAccountModelCode(DataUtil.safeToString(ob[index++]));
                stockTotalFullDTO.setAccountModelName(DataUtil.safeToString(ob[index++]));
                stockTotalFullDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setUnit(DataUtil.safeToString(ob[index++]));
                stockTotalFullDTO.setCheckSerial(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setStateName(DataUtil.safeToString(ob[index++]));
                stockTotalFullDTO.setStaffCode1(DataUtil.safeToString(ob[index++]));
                stockTotalFullDTO.setShop(DataUtil.safeToString(ob[index++]));
                stockTotalFullDTO.setStaffOwnerId(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setIsStaff(DataUtil.safeToLong(ob[index++]));
                stockTotalFullDTO.setStaffCode(DataUtil.safeToString(ob[index]));
                lsResult.add(stockTotalFullDTO);
            }
        }
        return lsResult;
    }

    @Override
    public List<StockTotalDTO> findOneStockTotal(StockTotalDTO stockTotalDTO) throws LogicException, Exception {
        List<StockTotalDTO> stockTotalDTOList = Lists.newArrayList();
        StockTotalDTO stockTotal;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.stock_total_id, a.owner_id, a.owner_type, a.prod_offer_id, a.state_id, ");
        sql.append("       a.current_quantity, a.available_quantity, a.status, a.hang_quantity ");
        sql.append("  FROM bccs_im.stock_total a WHERE a.prod_offer_id = #prodOfferId ");
        sql.append("  and a.owner_id = #ownerId and a.state_id = #stateId and a.owner_type = #ownerType ");
        if (!DataUtil.isNullObject(stockTotalDTO.getStatus())) {
            sql.append(" and a.status = #status");
        }

        sql.append(" for update nowait ");

        Query query = emIM.createNativeQuery(sql.toString());
        query.setParameter("prodOfferId", stockTotalDTO.getProdOfferId());
        query.setParameter("ownerId", stockTotalDTO.getOwnerId());
        query.setParameter("ownerType", stockTotalDTO.getOwnerType());
        query.setParameter("stateId", stockTotalDTO.getStateId());
        if (!DataUtil.isNullObject(stockTotalDTO.getStatus())) {
            query.setParameter("status", stockTotalDTO.getStatus());
        }
        List<Object[]> lstResult = Lists.newArrayList();
        try {
            lstResult = query.getResultList();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "basestock.validate.invalidate.stocktotal.updating");
        }

        for (Object[] object : lstResult) {
            int index = 0;
            stockTotal = new StockTotalDTO();
            stockTotal.setStockTotalId(DataUtil.safeToLong(object[index++]));
            stockTotal.setOwnerId(DataUtil.safeToLong(object[index++]));
            stockTotal.setOwnerType(DataUtil.safeToLong(object[index++]));
            stockTotal.setProdOfferId(DataUtil.safeToLong(object[index++]));
            stockTotal.setStateId(DataUtil.safeToLong(object[index++]));
            stockTotal.setCurrentQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setAvailableQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setStatus(DataUtil.safeToLong(object[index++]));
            stockTotal.setHangQuantity(DataUtil.safeToLong(object[index]));

            stockTotalDTOList.add(stockTotal);
        }
        return stockTotalDTOList;
    }


    @Override
    public List<StockManagementForWarranty> viewStockKV(Long shopId, Long prodOfferId, Long stateId) throws Exception {

        StringBuilder builder = new StringBuilder(" ");
        builder.append("SELECT a.prod_offer_id, a.prod_offer_code, a.prod_offer_name, a.state_id, a.state_name, SUM (a.current_quantity) current_quantity ");
        builder.append("  FROM stock_total_full a, v_shop_staff b ");
        builder.append(" WHERE a.owner_id = b.owner_id AND a.owner_type = b.owner_type ");
        builder.append("   AND (  (b.owner_id = #shopId AND b.owner_type = 1) ");
        builder.append("       OR (b.parent_shop_id = #shopId AND b.owner_type = 2)) ");
        builder.append("   AND a.current_quantity > 0  ");

        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("   AND a.prod_offer_id = #prodOfferId ");
        }

        if (!DataUtil.isNullOrZero(stateId)) {
            builder.append("   AND a.state_id = #stateId ");
        }

        builder.append("GROUP BY a.prod_offer_id,");
        builder.append("        a.prod_offer_code,");
        builder.append("        a.prod_offer_name,");
        builder.append("        a.state_id,");
        builder.append("        a.state_name ");
        builder.append("ORDER BY prod_offer_name");

        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("shopId", shopId);

        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        List<Object[]> queryResult = query.getResultList();
        List<StockManagementForWarranty> lsResult = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockManagementForWarranty stockWarranty = new StockManagementForWarranty();
                stockWarranty.setStockModelId(DataUtil.safeToLong(ob[index++]));
                stockWarranty.setStockModelCode(DataUtil.safeToString(ob[index++]));
                stockWarranty.setStockModelName(DataUtil.safeToString(ob[index++]));
                stockWarranty.setStateId(DataUtil.safeToLong(ob[index++]));
                stockWarranty.setStateName(DataUtil.safeToString(ob[index++]));
                stockWarranty.setQuantity(DataUtil.safeToLong(ob[index]));

                lsResult.add(stockWarranty);
            }
        }
        return lsResult;
    }

    @Override
    public List<StockTotalDTO> getStockTotalAvailableStaff(Long productOfferTypeId) throws Exception, LogicException {
        List<StockTotalDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT   distinct po.product_offer_type_id, ");
        builder.append("            po.prod_offer_id, ");
//        builder.append("            st.owner_id, ");
//        builder.append("            st.owner_type, ");
        builder.append("            nvl(po.day_stock_remain, 0), nvl(price_cost, 0) price_cost, ");
        builder.append("            nvl( (SELECT price FROM  product_offer_price WHERE 1=1 AND status = #status");
        builder.append("                AND prod_offer_id = po.prod_offer_id ");
        builder.append("                AND price_type_id = #priceTypeId ");
        builder.append("                AND effect_datetime <= SYSDATE ");
        builder.append("                AND (expire_datetime IS NULL OR expire_datetime >= TRUNC (SYSDATE)) ");
        builder.append("                AND rownum = 1 ), 0) retail");
        builder.append("     FROM   stock_total st, product_offering po ");
        builder.append("    WHERE       st.prod_offer_id = po.prod_offer_id ");
        builder.append("            AND po.status = 1 ");
        builder.append("            AND st.status = 1 ");
        builder.append("            AND st.owner_type = #owner_type ");
        builder.append("            AND (st.current_quantity > 0 ");
        builder.append("            OR st.available_quantity > 0) ");
        builder.append("            AND po.product_offer_type_id = #productOfferTypeId ");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("owner_type", Const.OWNER_TYPE.STAFF_LONG);
        query.setParameter("productOfferTypeId", productOfferTypeId);
        query.setParameter("status", Const.STATUS_ACTIVE);
        query.setParameter("priceTypeId", Const.CODE_SERVICE.PRICE_TYPE_RETAIL);

        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTotalDTO stockTotalDTO = new StockTotalDTO();
                stockTotalDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
//                stockTotalDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
//                stockTotalDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setDayStockRemain(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setPriceCost(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setPriceRetail(DataUtil.safeToLong(ob[index]));
                result.add(stockTotalDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockTotalDTO> getStockTotalAvailableShop(Long productOfferTypeId) throws Exception, LogicException {
        List<StockTotalDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT   distinct po.product_offer_type_id, ");
        builder.append("            po.prod_offer_id, ");
//        builder.append("            st.owner_id, ");
//        builder.append("            st.owner_type, ");
        builder.append("            nvl(po.day_stock_remain, 210), nvl(price_cost, 0) price_cost, ");
        builder.append("            nvl( (SELECT price FROM  product_offer_price WHERE 1=1 AND status = #status");
        builder.append("                AND prod_offer_id = po.prod_offer_id ");
        builder.append("                AND price_type_id = #priceTypeId ");
        builder.append("                AND effect_datetime <= SYSDATE ");
        builder.append("                AND (expire_datetime IS NULL OR expire_datetime >= TRUNC (SYSDATE)) ");
        builder.append("                AND rownum = 1 ), 0) retail");
        builder.append("     FROM   stock_total st, product_offering po, shop s ");
        builder.append("    WHERE       st.prod_offer_id = po.prod_offer_id ");
        builder.append("            AND po.status = 1 ");
        builder.append("            AND st.status = 1 ");
        builder.append("            AND s.status = 1 ");
        builder.append("            AND s.shop_id = st.owner_id ");
        builder.append("            AND st.owner_type = #owner_type ");
        builder.append("            AND (st.current_quantity > 0 ");
        builder.append("            OR st.available_quantity > 0) ");
        builder.append("            AND po.product_offer_type_id = #productOfferTypeId ");
//        builder.append("            AND s.shop_code not in (select osv.value  ");
//        builder.append("              from option_set os, option_set_value osv    ");
//        builder.append("              where osv.option_set_id = os.id  and os.code = #optionSetCode and os.status = #status and osv.status = #status) ");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("owner_type", Const.OWNER_TYPE.SHOP_LONG);
        query.setParameter("productOfferTypeId", productOfferTypeId);
        query.setParameter("status", Const.STATUS_ACTIVE);
        query.setParameter("priceTypeId", Const.CODE_SERVICE.PRICE_TYPE_RETAIL);
        query.setParameter("optionSetCode", Const.OPTION_SET.STOCK_OWNER_NOT_PROCESS);

        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTotalDTO stockTotalDTO = new StockTotalDTO();
                stockTotalDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
//                stockTotalDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
//                stockTotalDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setDayStockRemain(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setPriceCost(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setPriceRetail(DataUtil.safeToLong(ob[index]));
                result.add(stockTotalDTO);
            }
        }
        return result;
    }

    @Override
    public List<Long> getTotalQuantityStockTotal(Long prodOfferId, Long ownerId) throws Exception {

        StringBuilder builder = new StringBuilder(" ");
        builder.append("	SELECT   SUM (current_quantity) current_quantity, ");
        builder.append("			 SUM (available_quantity) available_quantity ");
        builder.append("	  FROM   stock_total a, v_shop_staff b ");
        builder.append("	 WHERE       a.owner_id = b.owner_id ");
        builder.append("			 AND a.owner_type = b.owner_type ");
        builder.append("			 AND a.prod_offer_id = #prodOfferId ");
        builder.append("			 AND a.state_id = 1 ");
        builder.append("			 AND ( (b.owner_id = #ownerId AND b.owner_type = 1) ");
        builder.append("				  OR (b.owner_type = 2 AND b.parent_shop_id = #ownerId)) ");

        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerId", ownerId);

        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            return Lists.newArrayList(DataUtil.safeToLong(queryResult.get(0)[0]), DataUtil.safeToLong(queryResult.get(0)[1]));
        }
        return Lists.newArrayList();
    }

    public boolean checkHaveProductInStockTotal(Long ownerId, Long ownerType) throws Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append("	select owner_Id, owner_type from bccs_im.stock_total  ");
        builder.append("	where owner_id = #ownerId and owner_type = #ownerType and current_quantity > 0 and status = 1");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            return true;
        }
        return false;
    }

    @Override
    public List<StockTotalDTO> getStockTotalAvailable(Long productOfferTypeId) throws Exception, LogicException {
        List<StockTotalDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder(" ");
        builder.append("   SELECT   distinct po.product_offer_type_id, po.prod_offer_id,");
        builder.append("            nvl(po.day_stock_remain, 0), nvl(price_cost, 0) price_cost, ");
        builder.append("            nvl( (SELECT price FROM  product_offer_price WHERE 1=1 AND status = #status");
        builder.append("                AND prod_offer_id = po.prod_offer_id ");
        builder.append("                AND price_type_id = #priceTypeId ");
        builder.append("                AND effect_datetime <= SYSDATE ");
        builder.append("                AND (expire_datetime IS NULL OR expire_datetime >= TRUNC (SYSDATE)) ");
        builder.append("                AND rownum = 1 ), 0) retail");
        builder.append("     FROM   stock_total st, product_offering po ");
        builder.append("    WHERE       st.prod_offer_id = po.prod_offer_id ");
        builder.append("            AND po.status = 1 ");
        builder.append("            AND st.status = 1 ");
        builder.append("            AND (st.current_quantity > 0  OR st.available_quantity > 0)");
        builder.append("            AND po.product_offer_type_id = #productOfferTypeId ");
        Query query = emIM.createNativeQuery(builder.toString());
        query.setParameter("productOfferTypeId", productOfferTypeId);
        query.setParameter("status", Const.STATUS_ACTIVE);
        query.setParameter("priceTypeId", Const.CODE_SERVICE.PRICE_TYPE_RETAIL);

        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTotalDTO stockTotalDTO = new StockTotalDTO();
                stockTotalDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setDayStockRemain(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setPriceCost(DataUtil.safeToLong(ob[index++]));
                stockTotalDTO.setPriceRetail(DataUtil.safeToLong(ob[index]));
                result.add(stockTotalDTO);
            }
        }
        return result;
    }
}
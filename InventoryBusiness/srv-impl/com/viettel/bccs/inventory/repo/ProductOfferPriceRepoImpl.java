package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.bccs.inventory.model.ProductOfferPrice;
import com.viettel.bccs.inventory.model.ProductOfferPrice.COLUMNS;
import com.viettel.bccs.inventory.model.QProductOfferPrice;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class ProductOfferPriceRepoImpl implements ProductOfferPriceRepoCustom {

    public static final Logger logger = Logger.getLogger(ProductOfferPriceRepoCustom.class);
    private final BaseMapper<ProductOfferPrice, ProductOfferPriceDTO> mapper = new BaseMapper(ProductOfferPrice.class, ProductOfferPriceDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QProductOfferPrice productOfferPrice = QProductOfferPrice.productOfferPrice;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(productOfferPrice.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(productOfferPrice.createUser, filter);
                        break;
                    case CRONEXPRESSION:
                        expression = DbUtil.createStringExpression(productOfferPrice.cronExpression, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(productOfferPrice.description, filter);
                        break;
                    case EFFECTDATETIME:
                        expression = DbUtil.createDateExpression(productOfferPrice.effectDatetime, filter);
                        break;
                    case EFFECTTYPE:
                        expression = DbUtil.createStringExpression(productOfferPrice.effectType, filter);
                        break;
                    case EXPIREDATETIME:
                        expression = DbUtil.createDateExpression(productOfferPrice.expireDatetime, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(productOfferPrice.name, filter);
                        break;
                    case PLEDGEAMOUNT:
                        expression = DbUtil.createLongExpression(productOfferPrice.pledgeAmount, filter);
                        break;
                    case PLEDGETIME:
                        expression = DbUtil.createLongExpression(productOfferPrice.pledgeTime, filter);
                        break;
                    case PRICE:
                        expression = DbUtil.createLongExpression(productOfferPrice.price, filter);
                        break;
                    case PRICEPOLICYID:
                        expression = DbUtil.createLongExpression(productOfferPrice.pricePolicyId, filter);
                        break;
                    case PRICETYPEID:
                        expression = DbUtil.createLongExpression(productOfferPrice.priceTypeId, filter);
                        break;
                    case PRIORPAY:
                        expression = DbUtil.createLongExpression(productOfferPrice.priorPay, filter);
                        break;
                    case PRIORITY:
                        expression = DbUtil.createShortExpression(productOfferPrice.priority, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(productOfferPrice.prodOfferId, filter);
                        break;
                    case PRODUCTOFFERPRICEID:
                        expression = DbUtil.createLongExpression(productOfferPrice.productOfferPriceId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(productOfferPrice.status, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(productOfferPrice.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(productOfferPrice.updateUser, filter);
                        break;
                    case VAT:
                        expression = DbUtil.createLongExpression(productOfferPrice.vat, filter);
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

    public List<ProductOfferPriceDTO> getPriceAmount(Long prodOfferId, Long priceTypeId, Long pricePolicyId) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" SELECT * ");
        builder.append(" FROM  product_offer_price ");
        builder.append(" WHERE 1=1 ");
        builder.append("  AND status = #status ");
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            builder.append("  AND prod_offer_id = #prodOfferId ");
        }
        if (!DataUtil.isNullOrZero(priceTypeId)) {
            builder.append("  AND price_type_id = #priceTypeId ");
        }

        if (!DataUtil.isNullOrZero(pricePolicyId)) {
            builder.append("  AND price_policy_id = #pricePolicyId ");
        }
        builder.append("  AND effect_datetime <= SYSDATE ");
        builder.append("  AND (expire_datetime IS NULL OR expire_datetime >= TRUNC (SYSDATE))");
        Query query = emIM.createNativeQuery(builder.toString(), ProductOfferPrice.class);
        query.setParameter("status", Const.STATUS.ACTIVE);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(priceTypeId)) {
            query.setParameter("priceTypeId", priceTypeId);
        }
        if (!DataUtil.isNullOrZero(pricePolicyId)) {
            query.setParameter("pricePolicyId", pricePolicyId);
        }
        List<ProductOfferPrice> result = query.getResultList();
        return mapper.toDtoBean(result);
    }

    public List<ProductOfferPriceDTO> getProductOfferPrice(Long prodOfferId, Long branchId, Long pricePolicyId, Long channelTypeId, Long priceTypeId, Long ownerType, Long ownerId) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder(" ");
        builder.append(" select pop.* from product_offer_price pop ");
        builder.append(" where 1 = 1 ");
        if (!DataUtil.isNullObject(pricePolicyId)) {
            builder.append(" and price_policy_id =#price_policy_id ");
        }
        if (!DataUtil.isNullObject(priceTypeId)) {
            builder.append(" and PRICE_TYPE_ID = #price_type_id ");
        }
        if (!DataUtil.isNullObject(prodOfferId)) {
            builder.append(" and prod_offer_id =#prod_offer_id  ");
        }
        builder.append("  and status  = 1 ");
        builder.append("  and   trunc(effect_datetime) <= trunc(sysdate) and  (expire_datetime is null or trunc(expire_datetime) >= trunc(sysdate) )");
        if (!DataUtil.isNullOrZero(branchId)) {
            builder.append("  and  EXISTS (select 'X' from map_price_shop psm ");
            builder.append("        where pop.product_offer_price_id = psm.product_offer_price_id and psm.shop_id =#shop_id and status=1) ");
        }
        if (!DataUtil.isNullOrZero(channelTypeId)) {
            builder.append("  and  EXISTS (select 'X' from map_price_channel pcm ");
            builder.append("            where pop.product_offer_price_id= pcm.product_offer_price_id and status=1");
            builder.append("  and pcm.channel_type_id =#channel_type_id) ");
        }
        if (!DataUtil.isNullOrZero(ownerId) && !DataUtil.isNullOrZero(ownerType)) {
            builder.append("  and  EXISTS (select 1 from stock_total st ");
            builder.append("            where st.prod_offer_id= pop.prod_offer_id ");
            builder.append("            and st.status=1 and st.owner_id = #owner_id");
            builder.append("            and st.owner_type = #owner_type and st.available_quantity > 0) ");
        }
        if (!DataUtil.isNullOrZero(channelTypeId)) {
            builder.append("  and  EXISTS (select 1 from stock_deposit sd ");
            builder.append("            where sd.status = 1 and sd.chanel_type_id = #channel_type_id ");
            if (!DataUtil.isNullObject(priceTypeId)) {
                builder.append(" and sd.trans_type = #transType");
            }
            builder.append("  and sd.prod_offer_id = pop.prod_offer_id and (trunc(sd.date_from) <= trunc(sysdate) and (sd.date_to is null or trunc(sd.date_to) >= trunc(sysdate)))) ");
        }
        Query query = emIM.createNativeQuery(builder.toString(), ProductOfferPrice.class);
        if (!DataUtil.isNullObject(pricePolicyId)) {
            query.setParameter("price_policy_id", pricePolicyId);
        }
        if (!DataUtil.isNullObject(priceTypeId)) {
            query.setParameter("price_type_id", priceTypeId);
        }
        if (!DataUtil.isNullObject(prodOfferId)) {
            query.setParameter("prod_offer_id", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(branchId)) {
            query.setParameter("shop_id", branchId);
        }
        if (!DataUtil.isNullOrZero(channelTypeId)) {
            query.setParameter("channel_type_id", channelTypeId);
            if (!DataUtil.isNullObject(priceTypeId) && DataUtil.safeEqual(priceTypeId, Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_DEPOSIT)) {
                query.setParameter("transType", Const.DEPOSIT_TRANS_TYPE.TYPE_DEPOSIT);
            } else if (!DataUtil.isNullObject(priceTypeId) && DataUtil.safeEqual(priceTypeId, Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_PAY)) {
                query.setParameter("transType", Const.DEPOSIT_TRANS_TYPE.TYPE_PAY);
            }
        }
        if (!DataUtil.isNullOrZero(ownerId) && !DataUtil.isNullOrZero(ownerType)) {
            query.setParameter("owner_id", ownerId);
            query.setParameter("owner_type", ownerType);
        }
        List<ProductOfferPrice> result = query.getResultList();
        return mapper.toDtoBean(result);
    }

    public List<ProductOfferPrice> getProductOfferPriceByProdOfferId(Long prodOfferId) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   pop.* ");
        sql.append("  FROM   Product_Offer_Price pop");
        sql.append(" WHERE   pop.status = 1 AND pop.price_Type_Id = 2");
        sql.append("         AND ( (pop.effect_Datetime IS NULL OR pop.effect_Datetime < SYSDATE)");
        sql.append("              AND (pop.expire_Datetime IS NULL");
        sql.append("                   OR pop.expire_Datetime > SYSDATE))");
        sql.append("         AND pop.prod_Offer_Id = #prodOfferId");
        Query query = emIM.createNativeQuery(sql.toString(), ProductOfferPrice.class);
        query.setParameter("prodOfferId", prodOfferId);
        return query.getResultList();
    }

}
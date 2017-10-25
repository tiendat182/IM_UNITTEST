package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductOfferTypeDTO;
import com.viettel.bccs.inventory.model.ProductOfferType;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ProductOfferType.COLUMNS;
import com.viettel.bccs.inventory.model.QProductOfferType;

import java.util.ArrayList;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ProductOfferTypeRepoImpl implements ProductOfferTypeRepoCustom {

    public static final Logger logger = Logger.getLogger(ProductOfferTypeRepoCustom.class);
    private final BaseMapper<ProductOfferType, ProductOfferTypeDTO> mapper = new BaseMapper(ProductOfferType.class, ProductOfferTypeDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QProductOfferType productOfferType = QProductOfferType.productOfferType;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(productOfferType.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(productOfferType.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(productOfferType.description, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(productOfferType.name, filter);
                        break;
                    case PARENTID:
                        expression = DbUtil.createLongExpression(productOfferType.parentId, filter);
                        break;
                    case PRODUCTOFFERTYPEID:
                        expression = DbUtil.createLongExpression(productOfferType.productOfferTypeId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(productOfferType.status, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(productOfferType.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(productOfferType.updateUser, filter);
                        break;
                    case TABLENAME:
                        expression = DbUtil.createStringExpression(productOfferType.tableName, filter);
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
    public List<ProductOfferType> getListProductOfferType() throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append(" SELECT   * FROM   product_offer_type ");
        strQuery.append(" WHERE   1 = 1 ");
        strQuery.append(" AND status = #stt AND check_exp =#checkExp ");
        strQuery.append(" ORDER BY   NLSSORT (name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("stt", Const.STATUS.ACTIVE);
        query.setParameter("checkExp", Const.PRODUCT_OFFER_TYPE.CHECK_EXP);
        List<ProductOfferType> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<ProductOfferTypeDTO> getAllProductOfferTypePhone(Long ownerId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE prot.parent_id = #parentId ");
        strQuery.append("   AND prot.status = #status ");
        strQuery.append("   AND prot.check_exp = #checkExp ");
        strQuery.append("   AND prot.product_offer_type_id in ");
        strQuery.append("   ( ");
        strQuery.append("       SELECT distinct(a.product_offer_type_id) ");
        strQuery.append("       FROM product_offering a, stock_total b ");
        strQuery.append("       WHERE  a.prod_offer_id = b.prod_offer_id ");
        strQuery.append("           AND a.status = #status AND b.status = #status ");
        strQuery.append("           AND b.available_quantity >= #quantity AND b.current_quantity >= #quantity ");
        strQuery.append("           AND b.owner_id = #ownerId ");
        strQuery.append("   ) ");
        strQuery.append(" ORDER BY prot.name DESC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("parentId", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("checkExp", Const.PRODUCT_OFFER_TYPE.CHECK_EXP_ISDN);
        query.setParameter("quantity", Const.STOCK_TOTAL.PLUS_QUANTITY);
        query.setParameter("ownerId", ownerId);
        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerId(Long ownerId, String ownerType, Long typeNoSerial) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE prot.parent_id = #parentId ");
        strQuery.append("   AND prot.status = #status ");
        strQuery.append("   AND prot.check_exp = #checkExp ");
        if (!DataUtil.isNullOrZero(typeNoSerial)) {
            strQuery.append("   AND prot.product_offer_type_id <> #typeNoSerial ");
        }
        strQuery.append("   AND prot.product_offer_type_id in ");
        strQuery.append("   ( ");
        strQuery.append("       SELECT distinct(a.product_offer_type_id) ");
        strQuery.append("       FROM product_offering a, stock_total b ");
        strQuery.append("       WHERE  a.prod_offer_id = b.prod_offer_id ");
        strQuery.append("           AND a.status = #status AND b.status = #status ");
        strQuery.append("           AND b.available_quantity >= #quantity AND b.current_quantity >= #quantity ");
        strQuery.append("           AND b.owner_id = #ownerId ");
        strQuery.append("           AND b.owner_type = #ownerType ");
        strQuery.append("   ) ");
        strQuery.append(" ORDER BY   NLSSORT (prot.name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("parentId", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("checkExp", Const.PRODUCT_OFFER_TYPE.CHECK_EXP);
        if (!DataUtil.isNullOrZero(typeNoSerial)) {
            query.setParameter("typeNoSerial", Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL);
        }
        query.setParameter("quantity", Const.STOCK_TOTAL.PLUS_QUANTITY);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdMaterial(Long ownerId, String ownerType, Long prodOfferTypeId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE prot.parent_id = #parentId ");
        strQuery.append("   AND prot.status = #status ");
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            strQuery.append("   AND prot.product_offer_type_id = #prodOfferTypeId ");
        }
        strQuery.append("   AND prot.product_offer_type_id in ");
        strQuery.append("   ( ");
        strQuery.append("       SELECT distinct(a.product_offer_type_id) ");
        strQuery.append("       FROM product_offering a, stock_total b ");
        strQuery.append("       WHERE  a.prod_offer_id = b.prod_offer_id ");
        strQuery.append("           AND a.status = #status AND b.status = #status ");
        strQuery.append("           AND b.available_quantity >= #quantity AND b.current_quantity >= #quantity ");
        strQuery.append("           AND b.owner_id = #ownerId ");
        strQuery.append("           AND b.owner_type = #ownerType ");
        strQuery.append("   ) ");
        strQuery.append(" ORDER BY   NLSSORT (prot.name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("parentId", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("status", Const.STATUS.ACTIVE);
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            query.setParameter("prodOfferTypeId", prodOfferTypeId);
        }
        query.setParameter("quantity", Const.STOCK_TOTAL.PLUS_QUANTITY);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdAndTableName(Long ownerId, String ownerType, String tableName) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE prot.parent_id = #parentId ");
        strQuery.append("   AND prot.status = #status ");
        strQuery.append("   AND prot.table_name = #tableName ");
        strQuery.append("   AND prot.product_offer_type_id in ");
        strQuery.append("   ( ");
        strQuery.append("       SELECT distinct(a.product_offer_type_id) ");
        strQuery.append("       FROM product_offering a ");
        strQuery.append("       WHERE  a.status = #status ");
        strQuery.append("   ) ");
        strQuery.append("  ORDER BY   NLSSORT (prot.name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("parentId", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("tableName", tableName);
        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    @Override
    public List<ProductOfferTypeDTO> getListByOwnerIdOwnerTypeState(Long ownerId, String ownerType, Long stateId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE prot.parent_id = #parentId ");
        strQuery.append("   AND prot.status = #status ");
        strQuery.append("   AND prot.check_exp = #checkExp ");
        strQuery.append("   AND prot.product_offer_type_id in ");
        strQuery.append("   ( ");
        strQuery.append("       SELECT distinct(a.product_offer_type_id) ");
        strQuery.append("       FROM product_offering a, stock_total b ");
        strQuery.append("       WHERE  a.prod_offer_id = b.prod_offer_id ");
        strQuery.append("           AND a.status = #status AND b.status = #status ");
        strQuery.append("           AND b.available_quantity >= #quantity AND b.current_quantity >= #quantity ");
        strQuery.append("           AND b.owner_id = #ownerId ");
        strQuery.append("           AND b.owner_type = #ownerType ");
//        strQuery.append("           AND b.state_id = :stateId ");
        strQuery.append("   ) ");
        strQuery.append("  ORDER BY   NLSSORT (prot.name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("parentId", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("checkExp", Const.PRODUCT_OFFER_TYPE.CHECK_EXP);
        query.setParameter("quantity", Const.STOCK_TOTAL.QUANTITY);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
//        query.setParameter("stateId", stateId);

        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeChannelAndShop(Long ownerId, String ownerType, Long priceType, Long channelTypeId, Long branchId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE prot.parent_id = #parentId ");
        strQuery.append("   AND prot.status = #status ");
        strQuery.append("   AND prot.check_exp = #checkExp ");
        strQuery.append("   AND prot.product_offer_type_id in ");
        strQuery.append("   ( ");
        strQuery.append("       SELECT distinct(a.product_offer_type_id) ");
        strQuery.append("       FROM product_offering a");

        if (!DataUtil.isNullOrZero(ownerId) && !DataUtil.isNullObject(ownerType)) {
            strQuery.append("       ,stock_total b ");
            strQuery.append("       WHERE  a.prod_offer_id = b.prod_offer_id ");
            strQuery.append("           AND a.status = #status AND b.status = #status ");
            strQuery.append("           AND b.available_quantity >= #quantity AND b.current_quantity >= #quantity ");
            strQuery.append("           AND b.owner_id = #ownerId ");
            strQuery.append("           AND b.owner_type = #ownerType ");
        } else {
            strQuery.append("     WHERE a.status = #status ");
        }
        strQuery.append("     AND  EXISTS (select 1 from product_offer_price p ");
        strQuery.append("                where p.PRICE_TYPE_ID = #price_type and p.prod_offer_id = a.prod_offer_id and p.status  = 1 ");
        strQuery.append("                  AND p.effect_datetime <= SYSDATE AND (p.expire_datetime IS NULL OR expire_datetime >= TRUNC (SYSDATE)) ");
        if (!DataUtil.isNullOrZero(channelTypeId)) {
            strQuery.append("              AND EXISTS (select 'X' from map_price_channel pcm ");
            strQuery.append("                           where p.product_offer_price_id= pcm.product_offer_price_id and status=1");
            strQuery.append("                             and pcm.channel_type_id =#channel_type_id) ");
        }
        if (!DataUtil.isNullOrZero(branchId)) {
            strQuery.append("              AND  EXISTS (select 'X' from map_price_shop psm ");
            strQuery.append("                            where p.product_offer_price_id = psm.product_offer_price_id and psm.shop_id =#shop_id and status=1) ");
        }
        strQuery.append("               )");
        strQuery.append("   ) ");
        strQuery.append(" ORDER BY   NLSSORT (prot.name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("parentId", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("checkExp", Const.PRODUCT_OFFER_TYPE.CHECK_EXP);
        if (!DataUtil.isNullOrZero(ownerId) && !DataUtil.isNullObject(ownerType)) {
            query.setParameter("quantity", Const.STOCK_TOTAL.PLUS_QUANTITY);
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
        }
        query.setParameter("price_type", priceType);
        if (!DataUtil.isNullOrZero(channelTypeId)) {
            query.setParameter("channel_type_id", channelTypeId);
        }
        if (!DataUtil.isNullOrZero(branchId)) {
            query.setParameter("shop_id", branchId);
        }
        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProduct() throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE prot.parent_id = #parentId ");
        strQuery.append("   AND prot.status = #status ");
        strQuery.append("   AND prot.check_exp = #checkExp ");
        strQuery.append("  ORDER BY   NLSSORT (prot.name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("parentId", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("checkExp", Const.PRODUCT_OFFER_TYPE.CHECK_EXP);

        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeById(List<Long> productOfferTypeId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE product_offer_type_id " + DbUtil.createInQuery("product_offer_type_id", productOfferTypeId));
        strQuery.append("   AND prot.status = #status ");
        strQuery.append("  ORDER BY   NLSSORT (prot.name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        DbUtil.setParamInQuery(query, "product_offer_type_id", productOfferTypeId);
        query.setParameter("status", Const.STATUS.ACTIVE);

        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProdOffeTypeForWarranty(Long ownerId, Long ownerType, Long prodOfferId) throws Exception {
        StringBuilder sqlQuery = new StringBuilder();

        if (prodOfferId == null) {
            sqlQuery.append("  SELECT pot.* ");
            sqlQuery.append("  FROM bccs_im.product_offer_type pot ");
            sqlQuery.append("  WHERE pot.status = #status AND pot.product_offer_type_id IN (7, 10, 11) ");
            sqlQuery.append("       AND pot.product_offer_type_id IN ");
            sqlQuery.append("               (SELECT product_offer_type_id ");
            sqlQuery.append("                  FROM bccs_im.product_offering ");
            sqlQuery.append("                 WHERE status=#status AND prod_offer_id IN ");
            sqlQuery.append("                           (SELECT prod_offer_id ");
            sqlQuery.append("                              FROM bccs_im.stock_total ");
            sqlQuery.append("                             WHERE     owner_id = #ownerId ");
            sqlQuery.append("                                   AND owner_type = #ownerType ");
            sqlQuery.append("                                   AND status = #status))");
        } else {
            sqlQuery.append(" SELECT pot.* FROM bccs_im.product_offer_type pot");
            sqlQuery.append(" WHERE status = #status AND product_offer_type_id IN (7, 10, 11)  ");
            sqlQuery.append("          AND product_offer_type_id IN ");
            sqlQuery.append("  (SELECT product_offer_type_id FROM bccs_im.product_offering WHERE prod_offer_id = #prodOfferId and status=#status) ");
        }
        Query query = em.createNativeQuery(sqlQuery.toString(), ProductOfferType.class);
        if (prodOfferId == null) {
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
            query.setParameter("status", Const.STATUS.ACTIVE);
        } else {
            query.setParameter("prodOfferId", prodOfferId);
            query.setParameter("status", Const.STATUS.ACTIVE);
        }

        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }

    public List<ProductOfferTypeDTO> getListOfferTypeSerial() throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT prot.* FROM product_offer_type prot ");
        strQuery.append(" WHERE table_name is not null ");
        strQuery.append("   AND prot.status = #status ");
        strQuery.append("   AND prot.check_exp = #checkExp ");
        strQuery.append("  ORDER BY   NLSSORT (prot.name, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), ProductOfferType.class);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("checkExp", Const.PRODUCT_OFFER_TYPE.CHECK_EXP);

        List<ProductOfferType> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return new ArrayList<ProductOfferTypeDTO>();
    }
}
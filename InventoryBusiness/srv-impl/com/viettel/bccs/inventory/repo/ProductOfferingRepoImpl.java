package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.bccs.inventory.model.ProductOffering.COLUMNS;
import com.viettel.bccs.inventory.model.QProductOffering;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductOfferingRepoImpl implements ProductOfferingRepoCustom {

    public static final Logger logger = Logger.getLogger(ProductOfferingRepoCustom.class);
    private final BaseMapper<ProductOffering, ProductOfferingDTO> mapper = new BaseMapper(ProductOffering.class, ProductOfferingDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM1;
    @Autowired
    private OptionSetValueRepo optionSetValueRepo;


    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QProductOffering productOffering = QProductOffering.productOffering;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CODE:
                        expression = DbUtil.createStringExpression(productOffering.code, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(productOffering.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(productOffering.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(productOffering.description, filter);
                        break;
                    case EFFECTDATETIME:
                        expression = DbUtil.createDateExpression(productOffering.effectDatetime, filter);
                        break;
                    case EXPIREDATETIME:
                        expression = DbUtil.createDateExpression(productOffering.expireDatetime, filter);
                        break;
                    case NAME:
                        expression = DbUtil.createStringExpression(productOffering.name, filter);
                        break;
                    case PRODUCTOFFERTYPEID:
                        expression = DbUtil.createLongExpression(productOffering.productOfferTypeId, filter);
                        break;
                    case PRODUCTOFFERINGID:
                        expression = DbUtil.createLongExpression(productOffering.productOfferingId, filter);
                        break;
                    case PRODUCTSPECID:
                        expression = DbUtil.createLongExpression(productOffering.productSpecId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(productOffering.status, filter);
                        break;
                    case SUBTYPE:
                        expression = DbUtil.createStringExpression(productOffering.subType, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(productOffering.telecomServiceId, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(productOffering.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(productOffering.updateUser, filter);
                        break;
                    case CHECKSERIAL:
                        expression = DbUtil.createLongExpression(productOffering.checkSerial, filter);
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
    public List<ProductOffering> getListProductOffering() throws Exception {
        String sql = "SELECT   * FROM   product_offering a "
                + "WHERE   1 = 1 AND product_offer_type_id = #productOfferTypeId "
                + " AND a.status = #stt AND ROWNUM <= #maxlength  "
                + " ORDER BY   NLSSORT (a.code, 'nls_sort=Vietnamese') ";
        Query query = em.createNativeQuery(sql, ProductOffering.class);
        query.setParameter("productOfferTypeId", Const.PRODUCT_OFFERING.PRODUCT_OFFER_TYPE);
        query.setParameter("stt", Const.STATUS.ACTIVE);
        query.setParameter("maxlength", Const.NUMBER_SEARCH.NUMBER_ROW);
        List<ProductOffering> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<ProductOfferingDTO> getListProductOfferTypeViewer(String ownerType, String ownerId, Long prodOfferId, Long stateId) throws Exception {
        List<ProductOfferingDTO> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT   name, product_offer_type_id  ");
        sql.append("   FROM   product_offer_type");
        sql.append("  WHERE   status = 1 ");
        sql.append("    AND   parent_id = 100 ");
        sql.append("    AND   product_offer_type_id IN (SELECT  product_offer_type_id ");
        sql.append("                                      FROM  product_offering a, stock_total b");
        sql.append("                                     WHERE  a.prod_offer_id = b.prod_offer_id  ");
        sql.append("                                       AND  a.status = 1 ");
        sql.append("                                       AND  b.status = 1 ");
        sql.append("                                       AND (b.available_quantity > 0 OR b.current_quantity > 0) ");
        sql.append("                                       AND  b.owner_type = #ownerType ");

        if (ownerId != null) {
            sql.append("     AND b.owner_id = #ownerId ");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            sql.append("     AND b.state_id = #stateId  ");
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            sql.append(" AND a.prod_offer_id = #prod_offer_id  ");
        }

        sql.append(" ) ");

        sql.append(" AND ROWNUM <= #maxlength  ");
        sql.append(" ORDER BY   NLSSORT (name, 'nls_sort=Vietnamese') ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("ownerType", ownerType);
        if (ownerId != null) {
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prod_offer_id", prodOfferId);
        }

        query.setParameter("maxlength", Const.NUMBER_SEARCH.NUMBER_ROW);

        List<Object[]> lstResult = query.getResultList();
        for (Object[] object : lstResult) {
            int index = 0;
            ProductOfferingDTO product = new ProductOfferingDTO();
            product.setName(DataUtil.safeToString(object[index++]));
            product.setProductOfferTypeId(DataUtil.safeToLong(object[index]));
            results.add(product);
        }

        return results;
    }

    public List<ProductOfferingDTO> getListProductOfferTypeForProcessStock(String ownerType, String ownerId, Long productOfferId, Long stateId) throws Exception {
        List<ProductOfferingDTO> results = new ArrayList<ProductOfferingDTO>();
        String sql = " SELECT name,product_offer_type_id  " +
                "  FROM   product_offer_type" +
                " WHERE   status = 1 AND check_exp = 1 ";
        sql += "         AND product_offer_type_id IN(SELECT   product_offer_type_id FROM   product_offering "
                + " WHERE    status = 1 ";
        if (!DataUtil.isNullOrZero(productOfferId)) {
            sql += " AND prod_offer_id = #productOfferId";
        } else {
            sql += " AND prod_offer_id IN (SELECT   prod_offer_id " +
                    "     FROM   stock_total WHERE  status = 1 " +
                    "     AND owner_type = #ownerType ";
            if (ownerId != null) {
                sql += "     AND owner_id = #ownerId ";
            }
            if (!DataUtil.isNullOrZero(stateId)) {
                sql += " AND state_id = #stateId  ";
            }
            sql += " ) ";
        }
        sql += " ) ";
        sql += " AND ROWNUM <= #maxlength  "
                + " ORDER BY   NLSSORT (name, 'nls_sort=Vietnamese') ";

        Query query = em.createNativeQuery(sql);
        if (!DataUtil.isNullOrZero(productOfferId)) {
            query.setParameter("productOfferId", productOfferId);
        } else {
            query.setParameter("ownerType", ownerType);
            if (ownerId != null) {
                query.setParameter("ownerId", ownerId);
            }
            if (!DataUtil.isNullOrZero(stateId)) {
                query.setParameter("stateId", stateId);
            }
        }

        query.setParameter("maxlength", Const.NUMBER_SEARCH.NUMBER_ROW);

        List<Object[]> lstResult = query.getResultList();
        for (Object[] object : lstResult) {
            int index = 0;
            ProductOfferingDTO product = new ProductOfferingDTO();
            product.setName(DataUtil.safeToString(object[index++]));
            product.setProductOfferTypeId(DataUtil.safeToLong(object[index]));
            results.add(product);
        }

        return results;
    }

    @Override
    public List<ProductOffering> getListProductOfferingByProOfTypeId(Long productOfferTypeId, Long ownerId, String ownerType) throws Exception {
        String sql = "SELECT   a.prod_offer_id, a.code, a.code || '-'||a.name, a.product_offer_type_id  FROM   product_offering a "
                + "WHERE   1 = 1 AND a.product_offer_type_id = #productOfferTypeId "
                + " AND a.status = #stt AND EXISTS  (SELECT   prod_offer_id FROM   stock_total b  "
                + "  WHERE       b.prod_offer_id = a.prod_offer_id AND b.status = #stt  ";
        if (!DataUtil.isNullObject(ownerId)) {
            sql += "  AND b.owner_id = #owner_id  ";
        }
        if (!DataUtil.isNullObject(ownerType)) {
            sql += "  AND b.owner_type = #owner_type  ";
        }
        sql += "   AND b.available_quantity > 0) order by a.name ";
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("productOfferTypeId", productOfferTypeId);
        query.setParameter("stt", Const.STATUS.ACTIVE);
        if (!DataUtil.isNullObject(ownerId)) {
            query.setParameter("owner_id", ownerId);
        }
        if (!DataUtil.isNullObject(ownerType)) {
            query.setParameter("owner_type", ownerType);
        }
        List queryResult = query.getResultList();
        List<ProductOffering> result = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object obj : queryResult) {
                Object[] objects = (Object[]) obj;
                ProductOffering value = new ProductOffering();
                value.setProductOfferingId(DataUtil.safeToLong(objects[0]));
                value.setCode(DataUtil.safeToString(objects[1]));
                value.setName(DataUtil.safeToString(objects[2]));
                value.setProductOfferTypeId(DataUtil.safeToLong(objects[3]));
                result.add(value);
            }
        }
        return result;
    }

    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingViewer(String ownerType, String ownerId, Long prodOfferId) throws Exception {
        List<OptionSetValue> lsOptionState = optionSetValueRepo.getLsOptionSetValueByCode(Const.OPTION_SET.GOODS_STATE);
        Map<String, String> mapState = lsOptionState.stream().collect(Collectors.toMap(OptionSetValue::getValue, OptionSetValue::getName));

        List<ProductOfferingTotalDTO> listStock = new ArrayList<ProductOfferingTotalDTO>();
        String sql = "select s.prod_offer_id,a.name,a.code,(SELECT osv.name" +
                "                FROM bccs_im.option_set os, bccs_im.option_set_value osv" +
                "                WHERE os.id = osv.option_set_id" +
                "                AND os.code = 'PRODUCT_OFFER_UNIT' " +
                "                and osv.value = a.unit" +
                "                AND ROWNUM < 2)" +
                "                 AS unit,s.state_id, s.available_quantity," +
                " s.current_quantity,a.product_offer_type_id, s.owner_type, s.owner_id,a.check_serial, b.name productTypeName   " +
                " ,a.accounting_model_code   " +
                " from stock_total s, product_offering a, product_offer_type b  " +
                " where s.prod_offer_id = a.prod_offer_id " +
                " and  a.product_offer_type_id = b.product_offer_type_id " +
                " and (s.available_quantity > 0 OR s.current_quantity > 0) " +
                " and s.status =1  and s.owner_Type = #ownerType ";

        if (ownerId != null) {
            sql += "     AND s.owner_id = #ownerId ";
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            sql += " AND a.prod_offer_id = #prod_offer_id ";
        }
        sql += " order by a.name ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("ownerType", ownerType);
        if (ownerId != null) {
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prod_offer_id", prodOfferId);
        }

        List<Object[]> lstResult = query.getResultList();
        return getLstStockConvert(lstResult);
    }

    @Override
    public String getStockModelPrefixById(Long prodOfferId) throws Exception {

        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT NVL(substr (code, 1, instr (code, '_', -1) - 1), code) FROM bccs_im.product_offering ");
        strQuery.append(" where prod_offer_id = #prodOfferId ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("prodOfferId", prodOfferId);

        List lstResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lstResult)) {
            return DataUtil.safeToString(lstResult.get(0));
        } else {
            return null;
        }
    }

    private List<ProductOfferingTotalDTO> getLstStockConvert(List<Object[]> lstResult) throws Exception {
        List<ProductOfferingTotalDTO> listStock = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(lstResult)) {
            List<OptionSetValue> lsState = optionSetValueRepo.getByOptionSetCode(Const.STOCK_INSPECT.STATE);
            Map<String, String> mapState = lsState.stream().collect(Collectors.toMap(OptionSetValue::getValue, OptionSetValue::getName));
            for (Object[] object : lstResult) {
                int index = 0;
                ProductOfferingTotalDTO stockTotal = new ProductOfferingTotalDTO();
                stockTotal.setProductOfferingId(DataUtil.safeToLong(object[index++]));
                stockTotal.setName(DataUtil.safeToString(object[index++]));
                stockTotal.setCode(DataUtil.safeToString(object[index++]));
                stockTotal.setUnit(DataUtil.safeToString(object[index++]));
                String state = DataUtil.safeToString(object[index++]);
                stockTotal.setStateId(DataUtil.safeToLong(state));
                stockTotal.setStateName(mapState.get(state));
                stockTotal.setAvailableQuantity(DataUtil.safeToLong(object[index++]));
                stockTotal.setCurrentQuantity(DataUtil.safeToLong(object[index++]));
                stockTotal.setProductOfferTypeId(DataUtil.safeToLong(object[index++]));
                stockTotal.setOwnerType(DataUtil.safeToString(object[index++]));
                stockTotal.setOwnerId(DataUtil.safeToLong(object[index++]));
                stockTotal.setCheckSerial(DataUtil.safeToString(object[index++]));
                stockTotal.setProductOfferTypeName(DataUtil.safeToString(object[index++]));
                stockTotal.setAccountingModelCode(DataUtil.safeToString(object[index]));
                listStock.add(stockTotal);

            }
        }
        return listStock;
    }


    @Override
    public List<ProductOfferingTotalDTO> getLsRequestProductByShop(Long ownerType, Long ownerId, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception {
        List<OptionSetValue> lsOptionState = optionSetValueRepo.getLsOptionSetValueByCode(Const.OPTION_SET.GOODS_STATE);
        Map<String, String> mapState = lsOptionState.stream().collect(Collectors.toMap(OptionSetValue::getValue, OptionSetValue::getName));

        List<ProductOfferingTotalDTO> listStock = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("");
        sql.append("	SELECT   s.prod_offer_id, ");
        sql.append("			 a.name, ");
        sql.append("			 a.code, ");
        sql.append("			 (SELECT   osv.name ");
        sql.append("				FROM   bccs_im.option_set os, bccs_im.option_set_value osv ");
        sql.append("			   WHERE       os.id = osv.option_set_id ");
        sql.append("					   AND os.code = 'PRODUCT_OFFER_UNIT' ");
        sql.append("					   AND osv.VALUE = a.unit ");
        sql.append("					   AND ROWNUM < 2) ");
        sql.append("				 AS unit, ");
        sql.append("			 s.state_id, ");
        sql.append("			 s.available_quantity, ");
        sql.append("			 s.current_quantity, ");
        sql.append("			 a.product_offer_type_id, ");
        sql.append("			 s.owner_type, ");
        sql.append("			 s.owner_id, ");
        sql.append("			 a.check_serial, ");
        sql.append("			 b.name producttypename, ");
        sql.append("			 (SELECT   SUM (request_quantity) ");
        sql.append("				FROM   stock_request_order_detail ");
        sql.append("			   WHERE       from_owner_id = s.owner_id ");
        sql.append("					   AND from_owner_type = s.owner_type ");
        sql.append("					   AND status=0 ");
        sql.append("					   AND prod_offer_id = a.prod_offer_id ");
        sql.append("					   AND state_id = s.state_id) ");
        sql.append("				 request_quantity ");

        sql.append("	  FROM   stock_total s, product_offering a, product_offer_type b ");
        sql.append("	 WHERE       s.prod_offer_id = a.prod_offer_id ");
        sql.append("			 AND a.product_offer_type_id = b.product_offer_type_id ");
        sql.append("			 AND (s.available_quantity > 0 OR s.current_quantity > 0) ");
        sql.append("			 AND s.owner_type = #ownerType ");
        sql.append("			 AND s.owner_id = #ownerId ");
        sql.append("			 AND a.status = 1 ");
        //TH ko chuyen loai mat hang thi lay mac dinh = 7
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            sql.append("			 AND a.product_offer_type_id = #prodOfferTypeId ");
        } else {
            sql.append("			 AND a.product_offer_type_id = 7 ");
        }
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            sql.append("			 AND a.prod_offer_id = #prodOfferId ");
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            sql.append("			 AND s.state_id =#stateId ");
        } else {
            sql.append("			 AND s.state_id IN (4, 12) ");
        }
        sql.append("			 order by a.name ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        if (!DataUtil.isNullOrZero(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullOrZero(prodOfferTypeId)) {
            query.setParameter("prodOfferTypeId", prodOfferTypeId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }

        List<Object[]> lstResult = query.getResultList();
        int index;
        for (Object[] object : lstResult) {
            index = 0;
            ProductOfferingTotalDTO stockTotal = new ProductOfferingTotalDTO();
            stockTotal.setProductOfferingId(DataUtil.safeToLong(object[index++]));
            stockTotal.setName(DataUtil.safeToString(object[index++]));
            stockTotal.setCode(DataUtil.safeToString(object[index++]));
            stockTotal.setUnit(DataUtil.safeToString(object[index++]));
            stockTotal.setStateId(DataUtil.safeToLong(object[index++]));
            stockTotal.setStateName(mapState.get(DataUtil.safeToString(stockTotal.getStateId())));
            stockTotal.setAvailableQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setCurrentQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setProductOfferTypeId(DataUtil.safeToLong(object[index++]));
            stockTotal.setOwnerType(DataUtil.safeToString(object[index++]));
            stockTotal.setOwnerId(DataUtil.safeToLong(object[index++]));
            stockTotal.setCheckSerial(DataUtil.safeToString(object[index++]));
            stockTotal.setProductOfferTypeName(DataUtil.safeToString(object[index++]));
            stockTotal.setRequestQuantity(DataUtil.safeToLong(object[index]));
            listStock.add(stockTotal);
        }
        return listStock;
    }

    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingTotalDistinct(String ownerType, String ownerId, String stockCode) throws Exception {
        List<ProductOfferingTotalDTO> listStock = new ArrayList<ProductOfferingTotalDTO>();
        String sql = "select distinct s.prod_offer_id,a.name,a.code " +
                " from stock_total s, product_offering a " +
                " where s.prod_offer_id = a.prod_offer_id " +
                " and s.status =1  and s.owner_Type = #ownerType ";

        if (ownerId != null) {
            sql += "     AND s.owner_id = #ownerId ";
        }
        if (stockCode != null) {
            sql += " AND LOWER (a.code) LIKE #stockCode ";
        }
        sql += " order by a.name ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("ownerType", ownerType);
        if (ownerId != null) {
            query.setParameter("ownerId", ownerId);
        }
        if (stockCode != null) {
            query.setParameter("stockCode", "%" + stockCode.toLowerCase() + "%");
        }

        List<Object[]> lstResult = query.getResultList();


        for (Object[] object : lstResult) {
            int index = 0;
            ProductOfferingTotalDTO stockTotal = new ProductOfferingTotalDTO();
            stockTotal.setProductOfferingId(DataUtil.safeToLong(object[index++]));
            stockTotal.setName(DataUtil.safeToString(object[index++]));
            stockTotal.setCode(DataUtil.safeToString(object[index]));

            listStock.add(stockTotal);

        }
        return listStock;
    }

    public List<ProductOfferingTotalDTO> getListProductOfferingForProcessStock(Long ownerType, Long ownerId, Long proOfferId, Long stateId) throws Exception {
        List<ProductOfferingTotalDTO> listStock = new ArrayList<ProductOfferingTotalDTO>();
        String sql = "select s.prod_offer_id,a.name,a.code,(SELECT osv.name" +
                "                FROM bccs_im.option_set os, bccs_im.option_set_value osv" +
                "                WHERE os.id = osv.option_set_id" +
                "                AND os.code = 'PRODUCT_OFFER_UNIT' " +
                "                and osv.value = a.unit" +
                "                AND ROWNUM < 2)" +
                "                 AS unit,s.state_id, s.available_quantity," +
                " s.current_quantity,a.product_offer_type_id, s.owner_type, s.owner_id,a.check_serial, b.name productTypeName   " +
                " ,a.accounting_model_code   " +
                " from stock_total s, product_offering a, product_offer_type b  " +
                " where s.prod_offer_id = a.prod_offer_id " +
                " and  a.product_offer_type_id = b.product_offer_type_id " +
                " and s.status =1 and a.status = 1  ";
        if (!DataUtil.isNullOrZero(ownerType)) {
            sql += "     and s.owner_Type = #ownerType ";
        }

        if (!DataUtil.isNullOrZero(ownerId)) {
            sql += "     AND s.owner_id = #ownerId ";
        }
        if (!DataUtil.isNullOrZero(proOfferId)) {
            sql += " AND a.prod_offer_id = #proOfferId ";
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            sql += " AND s.state_id  = #stateId ";
        }
        sql += " order by a.name ";

        Query query = em.createNativeQuery(sql);
        if (!DataUtil.isNullOrZero(ownerType)) {
            query.setParameter("ownerType", ownerType);
        }

        if (!DataUtil.isNullOrZero(ownerId)) {
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullOrZero(proOfferId)) {
            query.setParameter("proOfferId", proOfferId);
        }
        if (!DataUtil.isNullOrZero(stateId)) {
            query.setParameter("stateId", stateId);
        }
        List<Object[]> lstResult = query.getResultList();

        return getLstStockConvert(lstResult);
    }

    @Override
    public ProductOfferingDTO getProdOfferDtoByCodeAndStock(String prodOfferCode, Long ownerId, Long stateId) {
        return getProdOfferDtoByCodeAndStockNew(prodOfferCode, ownerId, Const.OWNER_TYPE.SHOP_LONG, stateId);
    }

    public ProductOfferingDTO getProdOfferDtoByCodeAndStockNew(String prodOfferCode, Long ownerId, Long ownerType, Long stateId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("  SELECT   * ");
        sql.append("  FROM   product_offering ");
        sql.append(" WHERE   prod_offer_id in ");
        sql.append("                 ( SELECT   DISTINCT a.prod_offer_id");
        sql.append("                    FROM   product_offering a, stock_total b");
        sql.append("                   WHERE       1 = 1");
        sql.append("                           AND a.prod_offer_id = b.prod_offer_id");
        sql.append("                           AND a.code = #prodOfferCode");
        sql.append("                           AND b.state_id = #stateId");
        sql.append("                           AND b.owner_type = #ownerType ");
        sql.append("                           AND b.owner_id = #ownerId");
        sql.append("                           AND a.status = #status) ");

        Query query = em.createNativeQuery(sql.toString(), ProductOffering.class);
        query.setParameter("prodOfferCode", prodOfferCode);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("stateId", stateId);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List<ProductOffering> ret = query.getResultList();
        if (!DataUtil.isNullOrEmpty(ret)) {
            return mapper.toDtoBean(ret.get(0));
        }
        return null;
    }

    @Override
    public List<Long> getProdOfferIdForRequestBalanceExport(Long ownerId, Long ownerType, Long prodOfferId) {
        List<Long> result = Lists.newArrayList();
        String sql = "  SELECT   prod_offer_id, owner_id " +
                "      FROM   (SELECT   b.prod_offer_id, " +
                "                     b.owner_id, " +
                "                     NVL (" +
                "                         (SELECT   quantity " +
                "                            FROM   product_erp a " +
                "                           WHERE       a.prod_offer_id = b.prod_offer_id " +
                "                                   AND a.owner_id = b.owner_id " +
                "                                   AND a.owner_type = b.owner_type " +
                "                                   AND a.create_date >= TRUNC (SYSDATE, 'mm') " +
                "                                   AND ROWNUM = 1), " +
                "                         0) " +
                "                          AS quantity_erp, " +
                "                        b.quantity_system_check " +
                "                FROM   rp_check_good_month b " +
                "               WHERE   b.check_date >= TRUNC (SYSDATE, 'mm') " +
                "               AND b.owner_id = #ownerId " +
                "               AND b.owner_type = #ownerType ";
        if (!DataUtil.isNullObject(prodOfferId)) {
            sql += "               AND b.prod_offer_id = #prodOfferId ";
        }
        sql += "               ) " +
                "       WHERE   quantity_erp < quantity_system_check ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        if (!DataUtil.isNullObject(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        List<Object[]> lstResult = query.getResultList();

        for (Object[] object : lstResult) {
            result.add(DataUtil.safeToLong(object[0]));
        }
        return result;
    }

    @Override
    public List<Long> getProdOfferIdForRequestBalanceImport(Long ownerId, Long ownerType, Long prodOfferId) {
        List<Long> result = Lists.newArrayList();
        String sql = "  SELECT   prod_offer_id, owner_id " +
                "      FROM   (SELECT   b.prod_offer_id, " +
                "                     b.owner_id, " +
                "                     NVL (" +
                "                         (SELECT   quantity_system_check " +
                "                            FROM   rp_check_good_month a " +
                "                           WHERE       a.prod_offer_id = b.prod_offer_id " +
                "                                   AND a.owner_id = b.owner_id " +
                "                                   AND a.owner_type = b.owner_type " +
                "                                   AND a.check_date >= TRUNC (SYSDATE, 'mm') " +
                "                                   AND ROWNUM = 1), " +
                "                         0) " +
                "                          AS quantity_system_check, " +
                "                        b.quantity as quantity_erp " +
                "                FROM   product_erp b " +
                "               WHERE   b.create_date >= TRUNC (SYSDATE, 'mm') " +
                "               AND b.owner_id = #ownerId " +
                "               AND b.owner_type = #ownerType ";
        if (!DataUtil.isNullObject(prodOfferId)) {
            sql += "               AND b.prod_offer_id = #prodOfferId ";
        }
        sql += "               ) " +
                "       WHERE   quantity_erp > quantity_system_check ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        if (!DataUtil.isNullObject(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        List<Object[]> lstResult = query.getResultList();

        for (Object[] object : lstResult) {
            result.add(DataUtil.safeToLong(object[0]));
        }
        return result;
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingByListId(List<Long> prodOfferIds) throws Exception {
        String sql = "  SELECT   * " +
                "  FROM   product_offering " +
                " WHERE   prod_offer_id " + DbUtil.createInQuery("prod_offer_id", prodOfferIds) + " AND status = #status";
        List<ProductOfferingTotalDTO> result = Lists.newArrayList();
        Query query = em.createNativeQuery(sql, ProductOffering.class);
        DbUtil.setParamInQuery(query, "prod_offer_id", prodOfferIds);
        query.setParameter("status", Const.STATUS.ACTIVE);
        List<ProductOffering> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            List<ProductOfferingDTO> lstProdOffering = mapper.toDtoBean(lst);
            for (ProductOfferingDTO productOfferingDTO : lstProdOffering) {
                ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
                productOfferingTotalDTO.setProductOfferingId(productOfferingDTO.getProductOfferingId());
                productOfferingTotalDTO.setProductOfferTypeId(productOfferingDTO.getProductOfferTypeId());
                productOfferingTotalDTO.setName(productOfferingDTO.getName());
                productOfferingTotalDTO.setCode(productOfferingDTO.getCode());
                result.add(productOfferingTotalDTO);
            }
        }
        return result;
    }

    @Override
    public List<ProductOfferingDTO> getLsProductOfferDtoByTypeId(Long productOfferTypeId) throws Exception {
        String sql = "  SELECT  * FROM   product_offering  WHERE   product_offer_type_id = #productOfferTypeId AND status = #status ORDER BY   NLSSORT (name, 'nls_sort=Vietnamese') ASC";
        Query query = em.createNativeQuery(sql, ProductOffering.class);
        query.setParameter("productOfferTypeId", productOfferTypeId);
        query.setParameter("status", Const.STATUS.ACTIVE);
        List<ProductOffering> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return Lists.newArrayList();
    }

    @Override
    public ProductOfferingDTO getProductOfferingStockById(Long prodOfferId) throws Exception {
        String sql = "SELECT   * FROM   product_offering a "
                + "WHERE   1 = 1 AND prod_offer_id = #prodOfferId "
                + " AND a.status = #status AND product_offer_type_id IN " +
                " (select product_offer_type_id from product_offer_type prot WHERE prot.parent_id = #parentId " +
                " AND prot.status = #status AND prot.check_exp = #checkExp) ";
        Query query = em.createNativeQuery(sql, ProductOffering.class);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("parentId", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("checkExp", Const.PRODUCT_OFFER_TYPE.CHECK_EXP);
        List<ProductOffering> ret = query.getResultList();
        if (!DataUtil.isNullOrEmpty(ret)) {
            return mapper.toDtoBean(ret).get(0);
        }
        return null;
    }

    public List<ProductInfoDTO> getLstProductInfo(Long stockTransId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT pr.code productCode,");
        strQuery.append("        pr.name name,");
        strQuery.append("        sv.name productUnit,");
        strQuery.append("        std.quantity productNum,");
        strQuery.append("        pr.price_cost productPrice,");
        strQuery.append("        pr.weight weight,");
        strQuery.append("        pr.volume volume");
        strQuery.append("  FROM  stock_trans_detail std,");
        strQuery.append("         product_offering pr,");
        strQuery.append("        option_set s,option_set_value sv");
        strQuery.append(" WHERE  std.stock_trans_id = #stockTransId");
        strQuery.append("       AND std.prod_offer_id = pr.prod_offer_id");
        strQuery.append("       AND pr.unit = sv.value");
        strQuery.append("       AND s.id = sv.option_set_id");
        strQuery.append("       AND s.status = 1");
        strQuery.append("       AND sv.status = 1");
        strQuery.append("       AND s.code = 'STOCK_MODEL_UNIT'");

        List<ProductInfoDTO> lstProductInfoDTOs = Lists.newArrayList();
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("stockTransId", stockTransId);
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] object : lst) {
                int index = 0;
                ProductInfoDTO productInfoDTO = new ProductInfoDTO();
                productInfoDTO.setProductCode(DataUtil.safeToString(object[index++]));
                productInfoDTO.setProductName(DataUtil.safeToString(object[index++]));
                productInfoDTO.setProductUnit(DataUtil.safeToString(object[index++]));
                productInfoDTO.setProductNum(DataUtil.safeToLong(object[index++]));
                productInfoDTO.setProductPrice(DataUtil.safeToLong(object[index++]));
                productInfoDTO.setWeight(DataUtil.safeToLong(object[index++]));
                productInfoDTO.setVolume(DataUtil.safeToLong(object[index]));
                lstProductInfoDTOs.add(productInfoDTO);
            }
        }
        return lstProductInfoDTOs;
    }

    public List<ProductOfferingLogisticDTO> getProductOfferingLogistic(Long stockTransId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        List<ProductOfferingLogisticDTO> lst = Lists.newArrayList();
        strQuery.append(" SELECT   std.prod_offer_id stockModelId, ");
        strQuery.append(" pr.code goodCode, ");
        strQuery.append(" pr.name goodName,  ");
        strQuery.append(" std.quantity amount,  ");
        strQuery.append(" std.state_id goodState, ");
        strQuery.append(" sv.name goodUnitName, ");
        strQuery.append(" (SELECT   a.code FROM   product_offering a  ");
        strQuery.append("       WHERE   a.prod_offer_id = std.prod_offer_id_swap AND ROWNUM < 2) transferGoodCode ");
        strQuery.append(" FROM   stock_trans_detail std, ");
        strQuery.append(" product_offering pr,  ");
        strQuery.append(" option_set s, option_set_value sv ");
        strQuery.append(" WHERE       std.stock_trans_id = #stockTransId ");
        strQuery.append(" AND std.prod_offer_id = pr.prod_offer_id ");
        strQuery.append(" AND pr.unit = sv.value  ");
        strQuery.append(" AND s.id = sv.option_set_id ");
        strQuery.append(" AND s.status = 1 ");
        strQuery.append(" AND sv.status = 1 ");
        strQuery.append(" AND s.code  = 'STOCK_MODEL_UNIT' ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("stockTransId", stockTransId);
        List queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList)) {
            for (Object queryResult : queryResultList) {
                Object[] objects = (Object[]) queryResult;
                int index = 0;
                ProductOfferingLogisticDTO productOfferingLogisticDTO = new ProductOfferingLogisticDTO();
                productOfferingLogisticDTO.setStockModelId(DataUtil.safeToLong(objects[index++]));
                productOfferingLogisticDTO.setGoodCode(DataUtil.safeToString(objects[index++]));
                productOfferingLogisticDTO.setGoodName(DataUtil.safeToString(objects[index++]));
                productOfferingLogisticDTO.setAmount(DataUtil.safeToLong(objects[index++]));
                productOfferingLogisticDTO.setGoodState(DataUtil.safeToLong(objects[index++]));
                productOfferingLogisticDTO.setGoodUnitName(DataUtil.safeToString(objects[index++]));
                productOfferingLogisticDTO.setTransferGoodCode(DataUtil.safeToString(objects[index]));
                lst.add(productOfferingLogisticDTO);
            }
        }
        return lst;
    }

    @Override
    public List<StockTotalFullDTO> getListSearchProductByArea(String province, String district, String productType, String inputSearch) throws Exception {

        String input = DataUtil.safeToString(inputSearch).trim().toLowerCase();

        StringBuilder strQuery = new StringBuilder();
        strQuery.append("	SELECT         prod_offer_code, ");
        strQuery.append("				   prod_offer_name, ");
        strQuery.append("				   available_quantity, ");
        strQuery.append("				   id_no,  ");
        strQuery.append("				   staff_code,  ");
        strQuery.append("				   staff_name, ");
        strQuery.append("				   tel, ");
        strQuery.append("				   owner_id, ");
        strQuery.append("				   owner_type ");
        strQuery.append("	  FROM (SELECT  ");
        strQuery.append("				   c.code prod_offer_code, ");
        strQuery.append("				   c.name prod_offer_name, ");
        strQuery.append("				   a.available_quantity, ");
        strQuery.append("				   NULL id_no,  ");
        strQuery.append("				   NULL staff_code,  ");
        strQuery.append("				   NULL staff_name, ");
        strQuery.append("				   NULL tel, ");
        strQuery.append("				   a.owner_id, ");
        strQuery.append("				   1 owner_type ");
        strQuery.append("			  FROM stock_total a, product_offering c ");
        strQuery.append("			 WHERE 1=1 ");
        strQuery.append("			   AND a.owner_type = 1 ");
        strQuery.append("			   AND c.status = '1' ");
        strQuery.append("			   AND a.prod_offer_id = c.prod_offer_id ");
        strQuery.append("			   AND EXISTS (SELECT 1  FROM shop b  WHERE 1=1 AND b.status='1' AND b.shop_id = a.owner_id  AND b.province = #province AND b.district = #district ) ");

        if ("1".equals(productType)) { // neu la vat tu
            strQuery.append("			  AND c.stock_model_type = 2 AND c.product_offer_type_id=11 ");
        } else if ("2".equals(productType)) {//neu la hang hoa
            strQuery.append("			   AND c.stock_model_type is null and c.product_offer_type_id=7 ");
        }

        strQuery.append("			   AND a.state_id = #stateId ");
        strQuery.append("			   AND a.available_quantity > 0 ");

        if (!DataUtil.isNullOrEmpty(input)) {
            strQuery.append("			   AND LOWER(c.name) LIKE #input ");
        }

        strQuery.append("			UNION ALL ");
        strQuery.append("		   SELECT  ");
        strQuery.append("				   d.code prod_offer_code, ");
        strQuery.append("				   d.name prod_offer_name, ");
        strQuery.append("				   a.available_quantity, ");
        strQuery.append("				   c.id_no id_no,  ");
        strQuery.append("				   c.staff_code,  ");
        strQuery.append("				   c.name staff_name, ");
        strQuery.append("				   c.tel,  ");
        strQuery.append("				   c.staff_id owner_id, ");
        strQuery.append("				   2 owner_type");
        strQuery.append("			 FROM stock_total a,  staff c, product_offering d ");
        strQuery.append("			WHERE 1=1 ");
        strQuery.append("			  AND a.owner_id = c.staff_id ");
        strQuery.append("			  AND a.prod_offer_id = d.prod_offer_id ");
        strQuery.append("			  AND a.owner_type = 2 ");
        strQuery.append("			  AND c.status = '1' ");
        strQuery.append("			  AND d.status = '1' ");
        if ("1".equals(productType)) { // neu la vat tu
            strQuery.append("			  AND d.stock_model_type = 2 AND d.product_offer_type_id=11 ");
        } else if ("2".equals(productType)) {//neu la hang hoa
            strQuery.append("			  AND d.stock_model_type is null and d.product_offer_type_id=7 ");
        }

        strQuery.append("			  AND a.state_id = #stateId ");
        strQuery.append("			  AND EXISTS (SELECT 1 FROM shop b WHERE  1 = 1 AND b.status = '1' AND c.shop_id = b.shop_id AND b.province = #province AND b.district = #district) ");
        strQuery.append("			  AND a.available_quantity > 0 ");

        if (!DataUtil.isNullOrEmpty(input)) {
            strQuery.append("			   AND LOWER(d.name) LIKE #input ");
        }

        strQuery.append("			  )  ");
        strQuery.append("	  ORDER BY prod_offer_code, prod_offer_name desc ");

        List<StockTotalFullDTO> lstProductInfoDTOs = Lists.newArrayList();
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("stateId", Const.GOODS_STATE.NEW);
        query.setParameter("province", province);
        query.setParameter("district", district);
        if (!DataUtil.isNullOrEmpty(input)) {
            query.setParameter("input", "%" + input + "%");
        }

        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            int index;
            StockTotalFullDTO productInfoDTO;
            for (Object[] object : lst) {
                index = 0;
                productInfoDTO = new StockTotalFullDTO();
                productInfoDTO.setProdOfferCode(DataUtil.safeToString(object[index++]));
                productInfoDTO.setProdOfferName(DataUtil.safeToString(object[index++]));
                productInfoDTO.setAvailableQuantity(DataUtil.safeToLong(object[index++]));
                productInfoDTO.setIdNo(DataUtil.safeToString(object[index++]));
                productInfoDTO.setStaffCode(DataUtil.safeToString(object[index++]));
                productInfoDTO.setStaffName(DataUtil.safeToString(object[index++]));
                productInfoDTO.setTel(DataUtil.safeToString(object[index++]));
                productInfoDTO.setOwnerId(DataUtil.safeToLong(object[index++]));
                productInfoDTO.setOwnerType(DataUtil.safeToLong(object[index]));
                lstProductInfoDTOs.add(productInfoDTO);
            }
        }
        return lstProductInfoDTOs;
    }

    @Override
    public ProductOfferingDTO getProductByCodeIm1(String prodOfferCode) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT stock_model_id, status ");
        strQuery.append("        FROM bccs_im.stock_model sm ");
        strQuery.append("       WHERE     sm.stock_model_code like #prodOfferCode ");

        List<ProductOfferingDTO> productOfferingDTOs = new ArrayList<ProductOfferingDTO>();
        Query query = emIM1.createNativeQuery(strQuery.toString());
        query.setParameter("prodOfferCode", prodOfferCode);
        List<Object[]> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            for (Object[] object : lst) {
                ProductOfferingDTO productInfoDTO = new ProductOfferingDTO();
                productInfoDTO.setProductOfferingId(DataUtil.safeToLong(object[0]));
                productInfoDTO.setStatus(DataUtil.safeToString(object[1]));
                productOfferingDTOs.add(productInfoDTO);
            }
        }
        if (!DataUtil.isNullOrEmpty(productOfferingDTOs)) {
            return productOfferingDTOs.get(0);
        }
        return null;
    }

    @Override
    public List<ProductOfferingDTO> getInventoryInfoWarranty(List<Long> listProdOfferId) throws Exception {

        if (DataUtil.isNullOrEmpty(listProdOfferId)) {
            return Lists.newArrayList();
        }

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   c.code, ");
        strQuery.append("          c.name, ");
        strQuery.append("          a.current_quantity, ");
        strQuery.append("          a.prod_offer_id, ");
        strQuery.append("          b.shop_code code, ");
        strQuery.append("          1 owner_type ");
        strQuery.append("   FROM   bccs_im.stock_total a, bccs_im.shop b, bccs_im.product_offering c ");
        strQuery.append("  WHERE       1 = 1 ");
        strQuery.append("          AND a.prod_offer_id = c.prod_offer_id ");
        strQuery.append("          AND c.status = 1 ");
        strQuery.append("          AND a.prod_offer_id ");
        strQuery.append(DbUtil.createInQuery("prodOfferIdShop", listProdOfferId));
        strQuery.append("          AND a.owner_id = b.shop_id ");
        strQuery.append("          AND a.owner_type = 1 ");
        strQuery.append("          and a.state_id =1 ");
        strQuery.append("          AND b.status = 1 ");
        strQuery.append("          AND b.shop_code LIKE 'KV$_%' ESCAPE '$' ");
        strQuery.append("          AND a.current_quantity > 0 ");
        strQuery.append(" UNION ALL ");
        strQuery.append(" SELECT   c.code, ");
        strQuery.append("          c.name, ");
        strQuery.append("          a.current_quantity, ");
        strQuery.append("          a.prod_offer_id, ");
        strQuery.append("          b.staff_code code, ");
        strQuery.append("          2 owner_type ");
        strQuery.append("   FROM   bccs_im.stock_total a, bccs_im.staff b, bccs_im.product_offering c ");
        strQuery.append("  WHERE       1 = 1 ");
        strQuery.append("          AND a.prod_offer_id = c.prod_offer_id ");
        strQuery.append("          AND c.status = 1 ");
        strQuery.append("          AND a.prod_offer_id ");
        strQuery.append(DbUtil.createInQuery("prodOfferIdStaff", listProdOfferId));
        strQuery.append("          AND a.owner_id = b.staff_id ");
        strQuery.append("          AND a.owner_type = 2 ");
        strQuery.append("          and a.state_id =1 ");
        strQuery.append("          AND b.status = 1 ");
        strQuery.append("          AND b.staff_code LIKE 'VT_TTBH_KV$_%' ESCAPE '$' ");
        strQuery.append("          AND a.current_quantity > 0 ");

        List<ProductOfferingDTO> lstProductOfferingDTO = Lists.newArrayList();
        Query query = em.createNativeQuery(strQuery.toString());
        DbUtil.setParamInQuery(query, "prodOfferIdShop", listProdOfferId);
        DbUtil.setParamInQuery(query, "prodOfferIdStaff", listProdOfferId);

        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            int index;
            ProductOfferingDTO productOfferingDTO;
            for (Object[] object : lst) {
                index = 0;
                productOfferingDTO = new ProductOfferingDTO();
                productOfferingDTO.setCode(DataUtil.safeToString(object[index++]));
                productOfferingDTO.setName(DataUtil.safeToString(object[index++]));
                productOfferingDTO.setQuantity(DataUtil.safeToLong(object[index++]));
                productOfferingDTO.setProductOfferingId(DataUtil.safeToLong(object[index++]));
                productOfferingDTO.setOwnerCode(DataUtil.safeToString(object[index++]));
                productOfferingDTO.setOwnerType(DataUtil.safeToLong(object[index]));
                lstProductOfferingDTO.add(productOfferingDTO);
            }
        }
        return lstProductOfferingDTO;
    }

    @Override
    public ProductOffering getProductByCode(String prodOfferCode) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT * from product_offering a ");
        strQuery.append(" WHERE  1 = 1 and upper(a.code) = #prodOfferCode ");

        Query query = em.createNativeQuery(strQuery.toString(), ProductOffering.class);
        query.setParameter("prodOfferCode", prodOfferCode.toUpperCase());
        List<ProductOffering> ret = query.getResultList();
        if (!DataUtil.isNullOrEmpty(ret)) {
            return ret.get(0);
        }
        return null;
    }
}
package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductExchangeDTO;
import com.viettel.bccs.inventory.model.ProductExchange;
import com.viettel.bccs.inventory.model.ProductExchange.COLUMNS;
import com.viettel.bccs.inventory.model.QProductExchange;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductExchangeRepoImpl implements ProductExchangeRepoCustom {

    public static final Logger logger = Logger.getLogger(ProductExchangeRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QProductExchange productExchange = QProductExchange.productExchange;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(productExchange.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(productExchange.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(productExchange.description, filter);
                        break;
                    case ENDDATETIME:
                        expression = DbUtil.createDateExpression(productExchange.endDatetime, filter);
                        break;
                    case NEWPRODOFFERID:
                        expression = DbUtil.createLongExpression(productExchange.newProdOfferId, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(productExchange.prodOfferId, filter);
                        break;
                    case PRODOFFERTYPEID:
                        expression = DbUtil.createLongExpression(productExchange.prodOfferTypeId, filter);
                        break;
                    case PRODUCTEXCHANGEID:
                        expression = DbUtil.createLongExpression(productExchange.productExchangeId, filter);
                        break;
                    case STARTDATETIME:
                        expression = DbUtil.createDateExpression(productExchange.startDatetime, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(productExchange.status, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(productExchange.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(productExchange.updateUser, filter);
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
    public List<ProductExchangeDTO> searchProductExchange(ProductExchangeDTO productExchangeDTO) throws Exception, LogicException {
        StringBuilder query = new StringBuilder(" ");
        query.append("  SELECT   product_exchange_id," +
                "           (SELECT   code" +
                "              FROM   product_offering a " +
                "             WHERE   a.prod_offer_id = pe.prod_offer_id) " +
                "               prod_offer_code, " +
                "           (SELECT   name " +
                "              FROM   product_offering a " +
                "             WHERE   a.prod_offer_id = pe.prod_offer_id) " +
                "               prod_offer_name, " +
                "           (SELECT   code " +
                "              FROM   product_offering a " +
                "             WHERE   a.prod_offer_id = pe.new_prod_offer_id) " +
                "               new_prod_offer_code, " +
                "           (SELECT   name " +
                "              FROM   product_offering a " +
                "             WHERE   a.prod_offer_id = pe.new_prod_offer_id) " +
                "               new_prod_offer_name,   " +
                "               start_datetime, " +
                "               end_datetime" +
                "    FROM   product_exchange pe " +
                "   WHERE       1 = 1 ");
        if (!DataUtil.isNullObject(productExchangeDTO.getProdOfferTypeId())) {
            query.append("AND prod_offer_type_id = #productOfferType ");
        }
        if (!DataUtil.isNullObject(productExchangeDTO.getProdOfferId())) {
            query.append("AND prod_offer_id = #productOfferId ");
        }
        if (!DataUtil.isNullObject(productExchangeDTO.getNewProdOfferId())) {
            query.append(" AND new_prod_offer_id = #productOfferIdNew ");
        }
        if (!DataUtil.isNullObject(productExchangeDTO.getStartDatetime())
                && !DataUtil.isNullObject(productExchangeDTO.getEndDatetime())) {
            query.append(" AND (( trunc(#startDate) <= start_datetime AND start_datetime <= trunc(#endDate)) " +
                    " AND ( trunc(#startDate) <= end_datetime AND end_datetime <= trunc(#endDate) ) " +
                    ")");
        }
        if (!DataUtil.isNullObject(productExchangeDTO.getStartDatetime())
                && DataUtil.isNullObject(productExchangeDTO.getEndDatetime())) {
            query.append(" AND start_datetime >= trunc(#startDate) ");
        }
        if (DataUtil.isNullObject(productExchangeDTO.getStartDatetime())
                && !DataUtil.isNullObject(productExchangeDTO.getEndDatetime())) {
            query.append(" AND end_datetime < trunc(#endDate) + 1 ");
        }
//        if (!DataUtil.isNullObject(productExchangeDTO.getStatus())) {
        query.append(" AND status = #status ");
//        }
        query.append(" ORDER BY prod_offer_id, start_datetime DESC ");
        Query jQuery = em.createNativeQuery(query.toString());

        if (!DataUtil.isNullObject(productExchangeDTO.getProdOfferTypeId())) {
            jQuery.setParameter("productOfferType", productExchangeDTO.getProdOfferTypeId());
        }
        if (!DataUtil.isNullObject(productExchangeDTO.getProdOfferId())) {
            jQuery.setParameter("productOfferId", productExchangeDTO.getProdOfferId());
        }
        if (!DataUtil.isNullObject(productExchangeDTO.getNewProdOfferId())) {
            jQuery.setParameter("productOfferIdNew", productExchangeDTO.getNewProdOfferId());
        }
        if (!DataUtil.isNullObject(productExchangeDTO.getStartDatetime())) {
            jQuery.setParameter("startDate", productExchangeDTO.getStartDatetime());
        }
        if (!DataUtil.isNullObject(productExchangeDTO.getEndDatetime())) {
            jQuery.setParameter("endDate", productExchangeDTO.getEndDatetime());
        }
//        if (!DataUtil.isNullObject(productExchangeDTO.getStatus())) {
        jQuery.setParameter("status", Const.LONG_OBJECT_ONE);
//        }

        List<ProductExchangeDTO> results = Lists.newArrayList();
        List<Object[]> resultList = jQuery.getResultList();

        if (!DataUtil.isNullOrEmpty(resultList)) {
            for (Object[] o : resultList) {
                ProductExchangeDTO productExchangeResult = new ProductExchangeDTO();
                productExchangeResult.setProductExchangeId(DataUtil.safeToLong(o[0]));
                productExchangeResult.setProdExchangeCode(DataUtil.safeToString(o[1]));

                productExchangeResult.setProdExchangeName(DataUtil.safeToString(o[2]));
                productExchangeResult.setProdExchangeCodeNew(DataUtil.safeToString(o[3]));
                productExchangeResult.setProdExchangeNameNew(DataUtil.safeToString(o[4]));
                if (!DataUtil.isNullObject(o[5])) {
                    productExchangeResult.setStartDatetime((Date) o[5]);
                }
                if (!DataUtil.isNullObject(o[6])) {
                    productExchangeResult.setEndDatetime((Date) o[6]);
                }
                results.add(productExchangeResult);
            }
        }

        return DataUtil.defaultIfNull(results, new ArrayList<>());
    }


    @Override
    public List<ProductExchange> checkProductExchange(Long prodOfferId, Long prodOfferIdNew, Date fromDate, Date toDate) throws Exception, LogicException {
        StringBuilder builder = new StringBuilder();
        builder.append("  SELECT   * " +
                "  FROM   product_exchange a " +
                " WHERE  1=1 " +
                " AND status = 1 " +
                " AND prod_offer_id = #prodOfferId AND new_prod_offer_id = #prodOfferIdNew " +
                "         AND ( (a.start_datetime <= trunc(#fromDate) " +
                "                AND trunc(#fromDate) < a.end_datetime + 1) " +
                "              OR ( trunc(#toDate) >= a.start_datetime " +
                "                  AND trunc(#toDate) < a.end_datetime + 1))  ");
        Query jQuery = em.createNativeQuery(builder.toString(), ProductExchange.class);
        jQuery.setParameter("prodOfferId", String.valueOf(prodOfferId));
        jQuery.setParameter("prodOfferIdNew", String.valueOf(prodOfferIdNew));
        jQuery.setParameter("fromDate", fromDate);
        jQuery.setParameter("toDate", toDate);
        List<ProductExchange> results = jQuery.getResultList();

        return results;
    }


}
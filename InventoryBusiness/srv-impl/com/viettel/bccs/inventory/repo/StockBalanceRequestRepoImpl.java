package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.StockBalanceRequestDTO;
import com.viettel.bccs.inventory.model.StockBalanceRequest;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockBalanceRequest.COLUMNS;
import com.viettel.bccs.inventory.model.QStockBalanceRequest;

import java.math.BigDecimal;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockBalanceRequestRepoImpl implements StockBalanceRequestRepoCustom {

    public static final Logger logger = Logger.getLogger(StockBalanceRequestRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockBalanceRequest stockBalanceRequest = QStockBalanceRequest.stockBalanceRequest;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockBalanceRequest.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockBalanceRequest.createUser, filter);
                        break;
                    case LISTEMAILSIGN:
                        expression = DbUtil.createStringExpression(stockBalanceRequest.listEmailSign, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockBalanceRequest.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockBalanceRequest.ownerType, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockBalanceRequest.status, filter);
                        break;
                    case STOCKREQUESTID:
                        expression = DbUtil.createLongExpression(stockBalanceRequest.stockRequestId, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(stockBalanceRequest.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(stockBalanceRequest.updateUser, filter);
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
    public List<StockBalanceRequest> searchStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws Exception {
        StringBuilder builder = new StringBuilder("select * from stock_balance_request")
                .append(" where create_datetime >= trunc(?)")
                .append(" and create_datetime <= trunc(?) +1 ")
                .append(" and  owner_id in(select sh.shop_id ")
                .append(" from shop sh, shop s")
                .append(" where sh.status=1 and s.status=1 and sh.shop_id=? or sh.shop_path like s.shop_path||'_%' and s.shop_id=?)");
        List params = Lists.newArrayList();
        params.add(stockBalanceRequestDTO.getFromDate());
        params.add(stockBalanceRequestDTO.getToDate());
        params.add(stockBalanceRequestDTO.getShopId());
        params.add(stockBalanceRequestDTO.getShopId());
        if (stockBalanceRequestDTO.getStatus() != null) {
            builder.append(" and status=?");
            params.add(stockBalanceRequestDTO.getStatus());
        }
        if (stockBalanceRequestDTO.getOwnerId() != null) {
            builder.append(" and owner_id=?");
            params.add(stockBalanceRequestDTO.getOwnerId());
        }
        if (stockBalanceRequestDTO.getType() != null) {
            builder.append(" and type=?");
            params.add(stockBalanceRequestDTO.getType());
        }
        builder.append(" order by create_datetime desc fetch first ").append(Const.NUMBER_SEARCH.NUMBER_ALL).append(" rows only");
        Query query = em.createNativeQuery(builder.toString(), StockBalanceRequest.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingExport(Long ownerType, Long ownerId, Long prodOfferId) throws Exception {
        List<ProductOfferingTotalDTO> result = Lists.newArrayList();
        String sql = "  SELECT   prod_offer_id, owner_id,quantity_erp,quantity_system_check,quantity_real_check " +
                "      FROM   (SELECT   b.prod_offer_id, " +
                "                     b.owner_id," +
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
                "                        b.quantity_system_check ," +
                "                       b.quantity_real_check" +
                "                FROM   rp_check_good_month b " +
                "               WHERE   b.check_date >= TRUNC (SYSDATE, 'mm') " +
                "               AND b.owner_id = #ownerId " +
                "               AND b.owner_type = #ownerType ";
        sql += "               AND b.prod_offer_id = #prodOfferId ";
        sql += "               ) " +
                "       WHERE   quantity_erp < quantity_system_check ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
                productOfferingTotalDTO.setProductOfferingId(DataUtil.safeToLong(ob[index++]));
                productOfferingTotalDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                productOfferingTotalDTO.setQuantityFinance(DataUtil.safeToLong(ob[index++]));
                productOfferingTotalDTO.setQuantityBccs(DataUtil.safeToLong(ob[index++]));
                productOfferingTotalDTO.setQuantityInspect(DataUtil.safeToLong(ob[index]));
                result.add(productOfferingTotalDTO);
            }
        }
        return result;
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingImport(Long ownerType, Long ownerId, Long prodOfferId) throws Exception {
        List<ProductOfferingTotalDTO> result = Lists.newArrayList();
        String sql = "  SELECT   prod_offer_id, owner_id,quantity_system_check,quantity_real_check,quantity_erp " +
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
                "                     NVL (" +
                "                         (SELECT   quantity_real_check " +
                "                            FROM   rp_check_good_month a " +
                "                           WHERE       a.prod_offer_id = b.prod_offer_id " +
                "                                   AND a.owner_id = b.owner_id " +
                "                                   AND a.owner_type = b.owner_type " +
                "                                   AND a.check_date >= TRUNC (SYSDATE, 'mm') " +
                "                                   AND ROWNUM = 1), " +
                "                         0) " +
                "                          AS quantity_real_check, " +
                "                        b.quantity as quantity_erp " +
                "                FROM   product_erp b " +
                "               WHERE   b.create_date >= TRUNC (SYSDATE, 'mm') " +
                "               AND b.owner_id = #ownerId " +
                "               AND b.owner_type = #ownerType ";

        sql += "               AND b.prod_offer_id = #prodOfferId ";

        sql += "               ) " +
                "       WHERE   quantity_erp > quantity_system_check ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
                productOfferingTotalDTO.setProductOfferingId(DataUtil.safeToLong(ob[index++]));
                productOfferingTotalDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                productOfferingTotalDTO.setQuantityBccs(DataUtil.safeToLong(ob[index++]));
                productOfferingTotalDTO.setQuantityInspect(DataUtil.safeToLong(ob[index++]));
                productOfferingTotalDTO.setQuantityFinance(DataUtil.safeToLong(ob[index]));
                result.add(productOfferingTotalDTO);
            }
        }
        return result;
    }

    public Long getMaxId() throws LogicException, Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("select MAX (stock_request_id) stockRequestId FROM stock_balance_request");
        Query query = em.createNativeQuery(builder.toString());
        BigDecimal maxId = (BigDecimal) query.getSingleResult();
        if (!DataUtil.isNullOrZero(maxId)) {
            return maxId.longValue() + 1L;
        }
        return 1L;
    }

    @Override
    public boolean checkStockBalanceDetail(Long ownerType, Long ownerId, List<Long> prodOfferId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("   SELECT   sbd.stock_balance_detail_id");
        builder.append("     FROM   stock_balance_request sbr, stock_balance_detail sbd");
        builder.append("    WHERE       sbr.stock_request_id = sbd.stock_request_id");
        builder.append("            AND sbr.owner_type = #ownerType");
        builder.append("            AND sbr.owner_id = #ownerId");
        builder.append("            AND sbd.prod_offer_id in(#prodOfferId)");
        builder.append("            AND sbr.status IN (0, 3)");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("ownerId", ownerId);
        List queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean doValidateContraints(Long ownerID, Long staffId) {
        StringBuilder builder = new StringBuilder("");
        builder.append("select sh.*")
                .append("  from shop sh, shop s, staff st")
                .append("  where sh.shop_id=?")
                .append("  and sh.status=1 and s.status=1 and st.status=1 and st.shop_id=s.shop_id")
                .append("  and (sh.shop_id=s.shop_id or sh.shop_path like s.shop_path||'_%')")
                .append("  and st.staff_id=?");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, ownerID);
        query.setParameter(2, staffId);
        //
        return DataUtil.isNullOrEmpty(query.getResultList());
    }

    @Override
    public boolean isDulicateRequestCode(String requestCode) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("select a.*")
                .append("  from stock_balance_request a")
                .append("  where a.request_code =?");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, requestCode);
        //
        return DataUtil.isNullOrEmpty(query.getResultList());
    }
}
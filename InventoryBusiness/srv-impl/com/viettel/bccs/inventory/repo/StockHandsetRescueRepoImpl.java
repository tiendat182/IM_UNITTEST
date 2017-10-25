package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockHandsetRescueDTO;
import com.viettel.bccs.inventory.model.QStockHandsetRescue;
import com.viettel.bccs.inventory.model.StockHandsetRescue.COLUMNS;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockHandsetRescueRepoImpl implements StockHandsetRescueRepoCustom {

    public static final Logger logger = Logger.getLogger(StockHandsetRescueRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockHandsetRescue stockHandsetRescue = QStockHandsetRescue.stockHandsetRescue;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockHandsetRescue.createDate, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockHandsetRescue.id, filter);
                        break;
                    case LASTMODIFY:
                        expression = DbUtil.createDateExpression(stockHandsetRescue.lastModify, filter);
                        break;
                    case NEWPRODOFFERLID:
                        expression = DbUtil.createLongExpression(stockHandsetRescue.newProdOfferlId, filter);
                        break;
                    case OLDPRODOFFERID:
                        expression = DbUtil.createLongExpression(stockHandsetRescue.oldProdOfferId, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockHandsetRescue.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockHandsetRescue.ownerType, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(stockHandsetRescue.serial, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockHandsetRescue.status, filter);
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
    public List<StockHandsetRescueDTO> getListHansetRescue(Long ownerId) throws LogicException, Exception {
        List<StockHandsetRescueDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   a.id,");
        builder.append(" a.serial,");
        builder.append(" pr.code productCode,");
        builder.append(" pr.name productName");
        builder.append(" FROM   stock_handset_rescue a, product_offering pr");
        builder.append(" WHERE       a.status = 0");
        builder.append(" AND a.new_prod_offer_id = pr.prod_offer_id");
        builder.append(" AND a.owner_id = #ownerid");
        builder.append(" AND a.owner_type = 2");
        builder.append(" ORDER BY   NLSSORT (pr.name, 'nls_sort=Vietnamese') ASC,");
        builder.append(" NLSSORT (a.serial, 'nls_sort=Vietnamese') ASC");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerid", ownerId);

        List<Object[]> list = query.getResultList();
        for (Object[] ob : list) {
            int index = 0;
            StockHandsetRescueDTO stockHandsetRescueDTO = new StockHandsetRescueDTO();
            stockHandsetRescueDTO.setId(DataUtil.safeToLong(ob[index++]));
            stockHandsetRescueDTO.setSerial(DataUtil.safeToString(ob[index++]));
            stockHandsetRescueDTO.setProductCode(DataUtil.safeToString(ob[index++]));
            stockHandsetRescueDTO.setProductName(DataUtil.safeToString(ob[index]));

            result.add(stockHandsetRescueDTO);
        }
        return result;
    }

    @Override
    public List<StockHandsetRescueDTO> getListProductForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception {
        List<StockHandsetRescueDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT   a.new_prod_offer_id, COUNT (1) quantity");
        builder.append(" FROM   stock_handset_rescue a");
        builder.append(" WHERE       a.status = 0");
        builder.append(" AND a.owner_id = #ownerId");
        builder.append(" AND a.owner_type = 2");
        builder.append(" AND a.serial " + DbUtil.createInQuery("serial", lstSerial));
        builder.append(" GROUP BY   a.new_prod_offer_id");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        DbUtil.setParamInQuery(query, "serial", lstSerial);

        List<Object[]> list = query.getResultList();
        for (Object[] ob : list) {
            int index = 0;
            StockHandsetRescueDTO stockHandsetRescueDTO = new StockHandsetRescueDTO();
            stockHandsetRescueDTO.setNewProdOfferlId(DataUtil.safeToLong(ob[index++]));
            stockHandsetRescueDTO.setQuantity(DataUtil.safeToLong(ob[index]));

            result.add(stockHandsetRescueDTO);
        }
        return result;
    }

    @Override
    public List<StockHandsetRescueDTO> getListSerialForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception {
        List<StockHandsetRescueDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT a.new_prod_offer_id , serial fromSerial, serial toSerial");
        builder.append(" FROM   stock_handset_rescue a");
        builder.append(" WHERE       a.status = 0");
        builder.append(" AND a.owner_id = #ownerId");
        builder.append(" AND a.owner_type = 2");
        builder.append(" AND a.serial " + DbUtil.createInQuery("serial", lstSerial));
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        DbUtil.setParamInQuery(query, "serial", lstSerial);

        List<Object[]> list = query.getResultList();
        for (Object[] ob : list) {
            int index = 0;
            StockHandsetRescueDTO stockHandsetRescueDTO = new StockHandsetRescueDTO();
            stockHandsetRescueDTO.setNewProdOfferlId(DataUtil.safeToLong(ob[index++]));
            stockHandsetRescueDTO.setFromSerial(DataUtil.safeToString(ob[index++]));
            stockHandsetRescueDTO.setToSerial(DataUtil.safeToString(ob[index]));

            result.add(stockHandsetRescueDTO);
        }
        return result;
    }

    public int updateStatusSerialForRescue(Long statusAffer, Long statusBefor, Long ownerId, String serial, Long prodOfferId) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(" update Stock_Handset_Rescue");
        builder.append(" set status = #statusAffer, last_modify = sysdate");
        builder.append(" where owner_id = #ownerId");
        builder.append(" and owner_Type = 2");
        builder.append(" and serial = #serial");
        builder.append(" and new_prod_offer_id = #prodOfferId");
        if (!DataUtil.isNullOrZero(statusBefor)) {
            builder.append(" AND status = #statusBefor");
        }
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("statusAffer", statusAffer);
        query.setParameter("ownerId", ownerId);
        query.setParameter("serial", serial);
        query.setParameter("prodOfferId", prodOfferId);
        if (!DataUtil.isNullOrZero(statusBefor)) {
            query.setParameter("statusBefor", statusBefor);
        }
        return query.executeUpdate();
    }
}
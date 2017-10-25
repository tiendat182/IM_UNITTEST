package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockBaseDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.bccs.inventory.service.ProductOfferTypeServiceImpl;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class InventoryWsRepoImpl implements InventoryWsRepoCustom {

    public static final Logger logger = Logger.getLogger(InventoryWsRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public List<StockTotalWsDTO> getStockTotalShop(Long ownerId, Long ownerType, Long type, String productOfferCode) throws Exception {
        List<StockTotalWsDTO> listStock = new ArrayList<>();
        StringBuilder sql = new StringBuilder("");

        sql.append(" SELECT   a.owner_id ownerId,");
        sql.append("         a.owner_type ownerType,");
        sql.append("         a.prod_offer_id ,");
        sql.append("         a.current_quantity ,");
        sql.append("         a.available_quantity ,");
        sql.append("         b.code ,");
        sql.append("         b.name name,");
        sql.append("         b.product_offer_type_id ,");
        sql.append("         (SELECT osv.name");
        sql.append("                FROM bccs_im.option_set os, bccs_im.option_set_value osv");
        sql.append("                WHERE os.id = osv.option_set_id ");
        sql.append("                AND os.code = 'PRODUCT_OFFER_UNIT' ");
        sql.append("                and osv.value = b.unit");
        sql.append("                AND ROWNUM < 2)");
        sql.append("                 AS unit,");
        sql.append("         a.status status,");
        sql.append("         a.state_id stateId,");
        sql.append("         b.check_serial ");
        sql.append("         ,b.accounting_model_code ");
        sql.append("  FROM  bccs_im.stock_total a, bccs_im.product_offering b");
        sql.append("  WHERE     a.prod_offer_id = b.prod_offer_id ");
        sql.append("  AND  a.status = 1 ");
        sql.append("  AND  a.current_quantity > 0 ");
        //Neu chi lay hang hoa
        if (type != null && type.compareTo(1L) == 0) {
            sql.append("     AND (b.stock_model_type IS NULL OR b.stock_model_type != 2) ");
        } else if (type != null && type.compareTo(2L) == 0) {
            sql.append("     AND b.product_offer_type_id = 11 ");
            sql.append("     AND b.stock_model_type = 2 ");
        }

        if (ownerType != null) {
            sql.append("     AND a.owner_type = #ownerType ");
        }
        if (ownerId != null) {
            sql.append("     AND a.owner_id = #ownerId ");
        }
        if (!DataUtil.isNullObject(productOfferCode)) {
            sql.append(" AND LOWER (b.code) LIKE #productOfferCode ");
        }
        sql.append(" order by b.name ");

        Query query = em.createNativeQuery(sql.toString());
        if (ownerType != null) {
            query.setParameter("ownerType", ownerType);
        }
        if (ownerId != null) {
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullObject(productOfferCode)) {
            query.setParameter("productOfferCode", "%" + productOfferCode.toLowerCase() + "%");
        }
        List<Object[]> lstResult = query.getResultList();

        for (Object[] object : lstResult) {
            int index = 0;
            StockTotalWsDTO stockTotal = new StockTotalWsDTO();
            stockTotal.setOwnerId(DataUtil.safeToLong(object[index++]));
            stockTotal.setOwnerType(DataUtil.safeToLong(object[index++]));
            stockTotal.setProdOfferId(DataUtil.safeToLong(object[index++]));
            stockTotal.setCurrentQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setAvailableQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setCode(DataUtil.safeToString(object[index++]));
            stockTotal.setName(DataUtil.safeToString(object[index++]));
            stockTotal.setProdOfferTypeId(DataUtil.safeToLong(object[index++]));
            stockTotal.setUnit(DataUtil.safeToString(object[index++]));
            stockTotal.setStatus(DataUtil.safeToLong(object[index++]));
            stockTotal.setStateId(DataUtil.safeToLong(object[index++]));
            stockTotal.setCheckSerial(DataUtil.safeToLong(object[index++]));
            stockTotal.setAccountingModelCode(DataUtil.safeToString(object[index]));

            listStock.add(stockTotal);

        }
        return listStock;
    }

    @Override
    public List<StockTotalWsDTO> getLstStockTotalDetail(Long ownerId, List<Long> lstProdOfferId) throws Exception {
        List<StockTotalWsDTO> listStock = new ArrayList<StockTotalWsDTO>();

        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("   SELECT   a.prod_offer_id ,");
        strQuery.append("            SUM (a.current_quantity) current_quantity,");
        strQuery.append("            SUM (a.available_quantity) available_quantity,");
        strQuery.append("            c.code, c.name ");
        strQuery.append("     FROM   bccs_im.stock_total a, bccs_im.staff b, bccs_im.product_offering c ");
        strQuery.append("    WHERE       1 = 1 AND a.prod_offer_id = c.prod_offer_id ");
        strQuery.append("            AND a.owner_type = 2");
        strQuery.append("            AND a.owner_id = b.staff_id");
        strQuery.append("            AND a.status = 1");
        strQuery.append("            AND b.shop_id = #ownerId");
        if (!DataUtil.isNullOrEmpty(lstProdOfferId)) {
            strQuery.append("            AND a.prod_offer_id " + DbUtil.createInQuery("prod_offer_id", lstProdOfferId));
        }
        strQuery.append(" GROUP BY   a.prod_offer_id, c.code, c.name");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("ownerId", ownerId);
        if (!DataUtil.isNullOrEmpty(lstProdOfferId)) {
            DbUtil.setParamInQuery(query, "prod_offer_id", lstProdOfferId);
        }

        List<Object[]> lstResult = query.getResultList();

        for (Object[] object : lstResult) {
            int index = 0;
            StockTotalWsDTO stockTotal = new StockTotalWsDTO();
            stockTotal.setProdOfferId(DataUtil.safeToLong(object[index++]));
            stockTotal.setCurrentQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setAvailableQuantity(DataUtil.safeToLong(object[index++]));
            stockTotal.setCode(DataUtil.safeToString(object[index++]));
            stockTotal.setName(DataUtil.safeToString(object[index]));

            listStock.add(stockTotal);
        }

        return listStock;
    }

    @Override
    public List<StockTransSerialDTO> getStockTransSerial(StockTotalWsDTO stockTotalWsDTO, int maxValue) throws Exception {
        List<StockTransSerialDTO> lstSerial = Lists.newArrayList();
        String tableName = IMServiceUtil.getTableNameByOfferType(stockTotalWsDTO.getProdOfferTypeId());
        StringBuilder sql = new StringBuilder("");

        //Doi voi mat hang so thi bo qua
        if (DataUtil.isNullObject(tableName) || "STOCK_NUMBER".equalsIgnoreCase(tableName)) {
            return lstSerial;
        }

        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTotalWsDTO.getProdOfferTypeId())) {
            sql.append(" select serial as fromSerial, serial as toSerial, 1 as quantity ");
            sql.append(" from stock_handset where owner_type = ? and owner_id = ? ");
            sql.append(" and prod_offer_id = ? and state_id = ? and status = ?  and rownum <= ? ");
        }
//        else if (Const.STOCK_TYPE.STOCK_CARD_NAME.equals(tableName)) {
//            sql.append( " select from_serial as fromSerial, to_serial as toSerial, quantity ");
//            sql.append( " from stock_card where owner_type = ? and owner_id = ? ");
//            sql.append( " and prod_offer_id = ? and state_id = ? and status = ?  and rownum <= ? ");
//        }
        else {
            sql.append(" SELECT   MIN (to_number(serial)) as  fromSerial,");
            sql.append(" MAX (to_number(serial)) as toSerial,");
            sql.append(" MAX(to_number(serial)) -MIN(to_number(serial)) +1 as quantity ");
            sql.append(" FROM (SELECT serial, serial - ROW_NUMBER () OVER (ORDER BY to_number(serial)) rn ");
            sql.append(" FROM " + tableName + " where owner_type = ? and owner_id= ? and prod_offer_id = ? and");
            sql.append(" state_id = ? and status = ?  and rownum <= ? ");
            sql.append(" ) GROUP BY rn ");
            sql.append(" ORDER BY to_number(fromSerial) ");
        }
        Query q = em.createNativeQuery(sql.toString());
        int index = 1;
        q.setParameter(index++, stockTotalWsDTO.getOwnerType());
        q.setParameter(index++, stockTotalWsDTO.getOwnerId());
        q.setParameter(index++, stockTotalWsDTO.getProdOfferId());
        q.setParameter(index++, stockTotalWsDTO.getStateId());
        q.setParameter(index++, stockTotalWsDTO.getStatus());
        q.setParameter(index, maxValue);
        List<Object[]> lstResult = q.getResultList();
        for (Object[] object : lstResult) {
            index = 0;
            StockTransSerialDTO stockTotal = new StockTransSerialDTO();
            stockTotal.setFromSerial(DataUtil.safeToString(object[index++]));
            stockTotal.setToSerial(DataUtil.safeToString(object[index++]));
            stockTotal.setQuantity(DataUtil.safeToLong(object[index]));
            lstSerial.add(stockTotal);
        }
        return lstSerial;
    }

    /**
     * kiem tra xem 1 serial co dung productOffer nhap vao ko
     */
    public StockBaseDTO checkValidOfferOfSerial(Long prodOfferTypeId, Long prodOfferId, String serial) throws Exception {
        String strTableName = IMServiceUtil.getTableNameByOfferType(prodOfferTypeId);
        if (DataUtil.isNullOrEmpty(strTableName)) {
            return null;
        }
        StringBuilder queryString = new StringBuilder();
        queryString.append("select serial, owner_id as ownerId, owner_type as ownerType,");
        queryString.append("       status, prod_offer_id as prodOfferId, deposit_price depositPrice, state_id stateId ");
        queryString.append("  from " + strTableName + " ");

        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodOfferTypeId) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(prodOfferTypeId)) {
            queryString.append(" where serial = ?1 ");
        } else {
            queryString.append(" where TO_NUMBER(serial) = ?1 ");
        }

        queryString.append("   and prod_offer_id = ?2 ");
        Query queryObject = em.createNativeQuery(queryString.toString());
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodOfferTypeId) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(prodOfferTypeId)) {
            queryObject.setParameter(1, serial);
        } else {
            queryObject.setParameter(1, new BigInteger(serial));
        }

        queryObject.setParameter(2, prodOfferId);
        List<StockBaseDTO> list = new ArrayList<>();
        List result = queryObject.getResultList();
        for (Object item : result) {
            Object[] objs = (Object[]) item;
            StockBaseDTO stockBase = new StockBaseDTO();
            int index = 0;
            stockBase.setSerial(DataUtil.safeToString(index++));
            stockBase.setOwnerId(DataUtil.safeToLong(objs[index++]));
            stockBase.setOwnerType(DataUtil.safeToLong(objs[index++]));
            stockBase.setStatus(DataUtil.safeToString(objs[index++]));
            stockBase.setProdOfferId(DataUtil.safeToLong(objs[index++]));
            stockBase.setDepositPrice(DataUtil.safeToLong(objs[index++]));
            stockBase.setStateId(DataUtil.safeToLong(objs[index]));

            list.add(stockBase);
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

}
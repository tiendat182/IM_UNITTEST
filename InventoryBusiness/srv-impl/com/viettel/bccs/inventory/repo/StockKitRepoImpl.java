package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.LookupSerialCardAndKitByFileDTO;
import com.viettel.bccs.inventory.dto.StockKitDTO;
import com.viettel.bccs.inventory.dto.StockKitIm1DTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockKit.COLUMNS;
import com.viettel.bccs.inventory.model.QStockKit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.transform.Result;

public class StockKitRepoImpl implements StockKitRepoCustom {

    public static final Logger logger = Logger.getLogger(StockKitRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM1;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockKit stockKit = QStockKit.stockKit;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AUCREGDATE:
                        expression = DbUtil.createDateExpression(stockKit.aucRegDate, filter);
                        break;
                    case AUCREMOVEDATE:
                        expression = DbUtil.createDateExpression(stockKit.aucRemoveDate, filter);
                        break;
                    case AUCSTATUS:
                        expression = DbUtil.createCharacterExpression(stockKit.aucStatus, filter);
                        break;
                    case BANKPLUSSTATUS:
                        expression = DbUtil.createLongExpression(stockKit.bankplusStatus, filter);
                        break;
                    case CHECKDIAL:
                        expression = DbUtil.createLongExpression(stockKit.checkDial, filter);
                        break;
                    case CONNECTIONDATE:
                        expression = DbUtil.createDateExpression(stockKit.connectionDate, filter);
                        break;
                    case CONNECTIONSTATUS:
                        expression = DbUtil.createLongExpression(stockKit.connectionStatus, filter);
                        break;
                    case CONNECTIONTYPE:
                        expression = DbUtil.createLongExpression(stockKit.connectionType, filter);
                        break;
                    case CONTRACTCODE:
                        expression = DbUtil.createStringExpression(stockKit.contractCode, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockKit.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockKit.createUser, filter);
                        break;
                    case DEPOSITPRICE:
                        expression = DbUtil.createLongExpression(stockKit.depositPrice, filter);
                        break;
                    case DIALSTATUS:
                        expression = DbUtil.createLongExpression(stockKit.dialStatus, filter);
                        break;
                    case HLRID:
                        expression = DbUtil.createStringExpression(stockKit.hlrId, filter);
                        break;
                    case HLRREGDATE:
                        expression = DbUtil.createDateExpression(stockKit.hlrRegDate, filter);
                        break;
                    case HLRREMOVEDATE:
                        expression = DbUtil.createDateExpression(stockKit.hlrRemoveDate, filter);
                        break;
                    case HLRSTATUS:
                        expression = DbUtil.createLongExpression(stockKit.hlrStatus, filter);
                        break;
                    case ICCID:
                        expression = DbUtil.createStringExpression(stockKit.iccid, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockKit.id, filter);
                        break;
                    case IMSI:
                        expression = DbUtil.createStringExpression(stockKit.imsi, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(stockKit.isdn, filter);
                        break;
                    case OLDOWNERID:
                        expression = DbUtil.createLongExpression(stockKit.oldOwnerId, filter);
                        break;
                    case OLDOWNERTYPE:
                        expression = DbUtil.createLongExpression(stockKit.oldOwnerType, filter);
                        break;
                    case ORDERCODE:
                        expression = DbUtil.createStringExpression(stockKit.orderCode, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockKit.ownerId, filter);
                        break;
                    case OWNERRECEIVERID:
                        expression = DbUtil.createLongExpression(stockKit.ownerReceiverId, filter);
                        break;
                    case OWNERRECEIVERTYPE:
                        expression = DbUtil.createLongExpression(stockKit.ownerReceiverType, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockKit.ownerType, filter);
                        break;
                    case PIN:
                        expression = DbUtil.createStringExpression(stockKit.pin, filter);
                        break;
                    case PIN2:
                        expression = DbUtil.createStringExpression(stockKit.pin2, filter);
                        break;
                    case POCODE:
                        expression = DbUtil.createStringExpression(stockKit.poCode, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockKit.prodOfferId, filter);
                        break;
                    case PUK:
                        expression = DbUtil.createStringExpression(stockKit.puk, filter);
                        break;
                    case PUK2:
                        expression = DbUtil.createStringExpression(stockKit.puk2, filter);
                        break;
                    case RECEIVERNAME:
                        expression = DbUtil.createStringExpression(stockKit.receiverName, filter);
                        break;
                    case SALEDATE:
                        expression = DbUtil.createDateExpression(stockKit.saleDate, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(stockKit.serial, filter);
                        break;
                    case SERIALREAL:
                        expression = DbUtil.createLongExpression(stockKit.serialReal, filter);
                        break;
                    case SIMTYPE:
                        expression = DbUtil.createStringExpression(stockKit.simType, filter);
                        break;
                    case STARTDATEWARRANTY:
                        expression = DbUtil.createDateExpression(stockKit.startDateWarranty, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockKit.stateId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockKit.status, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockKit.stockTransId, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(stockKit.telecomServiceId, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(stockKit.updateDatetime, filter);
                        break;
                    case UPDATENUMBER:
                        expression = DbUtil.createLongExpression(stockKit.updateNumber, filter);
                        break;
                    case USERSESSIONID:
                        expression = DbUtil.createStringExpression(stockKit.userSessionId, filter);
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
    public StockKitDTO getStockKitBySerial(String serial) {
        String queryString = " select a.id, a.prod_offer_id, a.owner_id, a.owner_type, a.serial, a.state_id,a.status  from stock_kit a where 1 = 1  and to_number(a.serial) = to_number(#serial)";
        Query query = em.createNativeQuery(queryString);
        query.setParameter("serial", serial);
        List<Object[]> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            Object[] obj = list.get(0);
            int index = 0;
            StockKitDTO stockKitDTO = new StockKitDTO();
            stockKitDTO.setId(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setProdOfferId(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setOwnerId(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setOwnerType(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setSerial(DataUtil.safeToString(obj[index++]));
            stockKitDTO.setStateId(DataUtil.safeToLong(obj[index++]));
            stockKitDTO.setStatus(DataUtil.safeToLong(obj[index]));
            return stockKitDTO;
        }
        return null;
    }

    @Override
    public List<StockKitDTO> getStockKitBySerialAndProdOfferId(Long staffId, String fromSerial, String toSerial, Long prodOfferId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT isdn, " +
                "       serial, " +
                "       pin, " +
                "       puk, " +
                "       pin2, " +
                "       puk2, " +
                "       (SELECT name " +
                "          FROM bccs_im.product_offering " +
                "         WHERE prod_offer_id = a.prod_offer_id) " +
                "           offer_name, " +
                "       to_date(ADD_MONTHS (TRUNC (SYSDATE, 'yyyy'), 12) - 1) expired_datetime " +
                "  FROM bccs_im.stock_kit a " +
                " WHERE     1 = 1 " +
                "       AND TO_NUMBER (serial) >= #fromSerial " +
                "       AND TO_NUMBER (serial) <= #toSerial " +
                "       AND PROD_OFFER_ID = #prodOfferId " +
                "       AND state_id =1 " +
                "       AND owner_type = 1 " +
                "       AND owner_id = #staffId ");

        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("fromSerial", fromSerial);
        query.setParameter("toSerial", toSerial);
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("staffId", staffId);

        List<StockKitDTO> results = Lists.newArrayList();
        List<Object[]> resultList = query.getResultList();

        if (!DataUtil.isNullOrEmpty(resultList)) {
            for (Object[] o : resultList) {
                int j = 0;
                StockKitDTO stockKitDTO = new StockKitDTO();

                stockKitDTO.setIsdn(DataUtil.safeToString(o[j++]));
                stockKitDTO.setSerial(DataUtil.safeToString(o[j++]));
                stockKitDTO.setPin(DataUtil.safeToString(o[j++]));
                stockKitDTO.setPuk(DataUtil.safeToString(o[j++]));
                stockKitDTO.setPin2(DataUtil.safeToString(o[j++]));
                stockKitDTO.setPuk2(DataUtil.safeToString(o[j++]));
                stockKitDTO.setProdOfferName(DataUtil.safeToString(o[j++]));
                Date expireDate = (Date) o[j++];
                if (!DataUtil.isNullObject(expireDate)) {
                    stockKitDTO.setExpireDate(expireDate);
                }
                results.add(stockKitDTO);
            }
        }
        return results;
    }

    @Override
    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serialList) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
                "SELECT sk.serial, \n" +
                        "  (SELECT price \n" +
                        "    FROM product_offer_price pr \n" +
                        "    WHERE pr.PRICE_TYPE_ID = 1 AND pr.STATUS = 1 AND pr.PRICE_POLICY_ID= 1 \n" +
                        "      AND (pr.CREATE_DATETIME IS NULL OR TRUNC (pr.CREATE_DATETIME) <= TRUNC (SYSDATE)) \n" +
                        "      AND (pr.EXPIRE_DATETIME IS NULL OR TRUNC (pr.EXPIRE_DATETIME) >= TRUNC (SYSDATE)) \n" +
                        "      AND pr.prod_offer_id = sk.prod_offer_id \n" +
                        "      AND ROWNUM = 1) AS price, \n" +
                        "  TO_CHAR (sk.sale_date,'yyyy-MM-dd HH24:mi:ss') AS sale_trans_date, \n" +
                        "  receiver_name AS staff_code, \n" +
                        "  DECODE ( owner_receiver_type, 2, \n" +
                        "    (SELECT ar.name \n" +
                        "      FROM staff st, shop sh, area ar \n" +
                        "      WHERE st.shop_id = sh.shop_id AND st.staff_id = sk.owner_receiver_id AND sh.province = ar.area_code AND ROWNUM = 1 ), \n" +
                        "    (SELECT ar.name \n" +
                        "      FROM shop sh, area ar \n" +
                        "      WHERE sh.shop_id = sk.owner_receiver_id AND sh.province = ar.area_code AND ROWNUM = 1 )) AS name, \n" +
                        "  DECODE ( owner_receiver_type, 2, \n" +
                        "    (SELECT sh.province \n" +
                        "      FROM staff st, shop sh \n" +
                        "      WHERE st.shop_id = sh.shop_id AND st.staff_id = sk.owner_receiver_id AND ROWNUM = 1), \n" +
                        "    (SELECT  province \n" +
                        "      FROM shop \n" +
                        "      WHERE shop_id = sk.owner_receiver_id AND ROWNUM = 1)) AS province, \n" +
                        "DECODE (sk.status,0,'0', '1') \n" +
                        "FROM  stock_kit sk \n" +
                        "WHERE  1 = 1 AND  to_number(sk.serial) " + DbUtil.createInQuery("serialList", serialList)
        );
        Query query = em.createNativeQuery(strQuery.toString());
        DbUtil.setParamInQuery(query, "serialList", serialList);
        List queryResult = query.getResultList();
        List<LookupSerialCardAndKitByFileDTO> result = Lists.newArrayList();
        if (!queryResult.isEmpty()) {
            for (Object obj : queryResult) {
                //try {
                result.add(valueDTO(obj));
                //} catch (Exception e) {
                //    logger.error(e.getMessage(), e);
                //}
            }
        }
        return result;
    }

    public LookupSerialCardAndKitByFileDTO valueDTO(Object obj) throws Exception {
        Object[] objects = (Object[]) obj;
        LookupSerialCardAndKitByFileDTO valueDTO = new LookupSerialCardAndKitByFileDTO();
        valueDTO.setSerial(DataUtil.safeToString(objects[0]));
        /*
        if (objects[1]!=null) {
            valueDTO.setPrice(DataUtil.safeToString(objects[1]));
        } else {
            valueDTO.setPrice(null);
        }
        */
        valueDTO.setPrice(safeToString(objects[1]));
        if (objects[2] != null) {
            String dateExpStr = DataUtil.safeToString(objects[2]);
            DateFormat df = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
            Date dateExp = df.parse(dateExpStr);
            valueDTO.setDateExp(dateExp);
        } else {
            valueDTO.setDateExp(null);
        }
        valueDTO.setStaffCode(DataUtil.safeToString(objects[3]));
        valueDTO.setProvinceName(DataUtil.safeToString(objects[4]));
        valueDTO.setProvinceCode(DataUtil.safeToString(objects[5]));
        valueDTO.setNote(DataUtil.safeToString(objects[6]));
        return valueDTO;
    }

    public String safeToString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    @Override
    public List<StockKitIm1DTO> findStockKitAndHisStockKitBySerial(String serial) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   STOCK_MODEL_ID, sysdate  , status " +
                "              FROM   bccs_im.stock_kit sk " +
                "             WHERE   TO_NUMBER (sk.serial) = TO_NUMBER (#serial) " +
                "            UNION ALL " +
                "            SELECT   STOCK_MODEL_ID , sysdate , status " +
                "              FROM   bccs_im.his_stock_kit hsk " +
                "  WHERE   TO_NUMBER (hsk.serial) = TO_NUMBER (#serial)  ");

        Query query = emIM1.createNativeQuery(strQuery.toString());
        query.setParameter("serial", serial);
        List<Object[]> result = query.getResultList();
        List<StockKitIm1DTO> lstStockResult = new ArrayList<>();
        StockKitIm1DTO stockKitResult;
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object[] obj : result) {
                int i = 0;
                stockKitResult = new StockKitIm1DTO();
                stockKitResult.setStockModelId(DataUtil.safeToLong(obj[i++]));
                Object tmpDate = obj[i++];
                stockKitResult.setSaleDate(!DataUtil.isNullObject(tmpDate) ? (Date) tmpDate : null);
                Object status = obj[i++];
                stockKitResult.setStatus(!DataUtil.isNullObject(status) ? DataUtil.safeToLong(status) : null);
                lstStockResult.add(stockKitResult);
            }
        }
        return DataUtil.defaultIfNull(lstStockResult, new ArrayList<>());
    }

    @Override
    public List<StockKitDTO> getStatusStockKit(Long prodOfferId, String serial) {
        if (DataUtil.isNullOrZero(prodOfferId)) {
            return null;
        }
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT  id, prod_offer_id, status " +
                        "  FROM   bccs_im.stock_kit " +
                        " WHERE   prod_offer_id = #prodOfferId  " +
                        "         AND serial = #serial   "
//                + "         AND status = #status   "
        );

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);
//        query.setParameter("status", Const.STATUS.ACTIVE);
        List<Object[]> result = query.getResultList();
        List<StockKitDTO> lstStockResult = new ArrayList<>();
        StockKitDTO stockKitResult;
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object[] obj : result) {
                int i = 0;
                stockKitResult = new StockKitDTO();
                stockKitResult.setId(DataUtil.safeToLong(obj[i++]));
                stockKitResult.setProdOfferId(DataUtil.safeToLong(obj[i++]));
                stockKitResult.setStatus(DataUtil.safeToLong(obj[i++]));
                lstStockResult.add(stockKitResult);
            }
        }
        return DataUtil.defaultIfNull(lstStockResult, new ArrayList<>());
    }
}
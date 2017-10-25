package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.QStockTransSerial;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.model.StockTransSerial.COLUMNS;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ProductOfferTypeService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.eclipse.persistence.config.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StockTransSerialRepoImpl implements StockTransSerialRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransSerialRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;
    @Autowired
    private OptionSetValueRepo optionSetValueRepo;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransSerial stockTransSerial = QStockTransSerial.stockTransSerial;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockTransSerial.createDatetime, filter);
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(stockTransSerial.fromSerial, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockTransSerial.quantity, filter);
                        break;
                    case STOCKTRANSDETAILID:
                        expression = DbUtil.createLongExpression(stockTransSerial.stockTransDetailId, filter);
                        break;
                    case STOCKTRANSSERIALID:
                        expression = DbUtil.createLongExpression(stockTransSerial.stockTransSerialId, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(stockTransSerial.toSerial, filter);
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
    public List<StockTransSerialDTO> getListStockTransSerial(Long stockTransActionId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT   (SELECT   owner_code || '-' || owner_name ")
                .append("           FROM   v_shop_staff ")
                .append("           WHERE   st.from_owner_id = owner_id ")
                .append("                     AND st.from_owner_type = owner_type) ")
                .append("               AS fromOwnerName, ")
                .append("           (SELECT   owner_code || '-' || owner_name ")
                .append("              FROM   v_shop_staff ")
                .append("             WHERE   st.to_owner_id = owner_id ")
                .append("                     AND st.to_owner_type = owner_type) ")
                .append("               AS toOwnerName, ")
                .append("           (prof.code || '-' || name) AS stockModelName, ")
                .append("           (SELECT name ")
                .append("                           from OPTION_SET_VALUE ")
                .append("                           where OPTION_SET_ID IN(SELECT ID FROM OPTION_SET ")
                .append("                           WHERE CODE='GOOD_STATE') and value=to_char(std.state_id)) modelStateName,")
                .append("           sts.from_serial, ")
                .append("           sts.to_serial, ")
                .append("           sts.quantity, ")
                .append("           sta.action_code, ")
                .append("           st.create_datetime")
                .append("     FROM  stock_trans st, ")
                .append("           stock_trans_detail std, ")
                .append("           stock_trans_serial sts, ")
                .append("           stock_trans_action sta, ")
                .append("           product_offering prof ")
                .append("    WHERE  1 = 1 ")
                .append("           AND st.stock_trans_id = std.stock_trans_id ")
                .append("           AND st.stock_trans_id=sta.stock_trans_id")
                .append("           AND std.stock_trans_detail_id = sts.stock_trans_detail_id")
                .append("           AND std.prod_offer_id = prof.prod_offer_id  ")
                .append("           AND sta.stock_trans_action_id = #actionId");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("actionId", stockTransActionId);
        List queryResultList = query.getResultList();
        List<StockTransSerialDTO> serialDTOs = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResultList)) {
            for (Object queryResult : queryResultList) {
                Object[] objects = (Object[]) queryResult;
                StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                serialDTO.setFromOwnerName(DataUtil.safeToString(objects[0]));
                serialDTO.setToOwnerName(DataUtil.safeToString(objects[1]));
                serialDTO.setModelName(DataUtil.safeToString(objects[2]));
                serialDTO.setModelStateName(DataUtil.safeToString(objects[3]));
                serialDTO.setFromSerial(DataUtil.safeToString(objects[4]));
                serialDTO.setToSerial(DataUtil.safeToString(objects[5]));
                serialDTO.setQuantity(DataUtil.safeToLong(objects[6]));
                serialDTO.setActionCode(DataUtil.safeToString(objects[7]));
                serialDTO.setCreateDatetime((Date) objects[8]);
                serialDTOs.add(serialDTO);
            }
        }
        return serialDTOs;
    }

    @Override
    public List<StockTransSerialDTO> findStockTransSerial(StockTransSerialDTO stockTransSerialDTO) throws Exception {
        List params = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        builder.append("SELECT     ")
                .append("                          (prof.code || '-' || name) AS stockModelName, ")
                .append("                          (SELECT name ")
                .append("                                          from OPTION_SET_VALUE ")
                .append("                                          where OPTION_SET_ID IN(SELECT ID FROM OPTION_SET ")
                .append("                                          WHERE CODE='GOOD_STATE') and value=to_char(std.state_id)) modelStateName,")
                .append("                          sts.from_serial, ")
                .append("                          sts.to_serial, ")
                .append("                          sts.quantity")
                .append("                    FROM  stock_trans_detail std, ")
                .append("                          stock_trans_serial sts, ")
                .append("                          product_offering prof ")
                .append("                   WHERE  1 = 1 ")
                .append("                          AND std.stock_trans_detail_id = sts.stock_trans_detail_id")
                .append("                          AND std.prod_offer_id = prof.prod_offer_id ");
        if (!DataUtil.isNullOrZero(stockTransSerialDTO.getStockTransDetailId())) {
            builder.append("   AND std.stock_trans_detail_id=#P").append(params.size());
            params.add(stockTransSerialDTO.getStockTransDetailId());
        }
        Query query = em.createNativeQuery(builder.toString());
        int i = 0;
        for (Object param : params) {
            query.setParameter("P" + i++, param);
        }
        List queryResultList = query.getResultList();
        List<StockTransSerialDTO> serialDTOs = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(queryResultList)) {
            for (Object queryResult : queryResultList) {
                Object[] objects = (Object[]) queryResult;
                StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                serialDTO.setModelName(DataUtil.safeToString(objects[0]));
                serialDTO.setModelStateName(DataUtil.safeToString(objects[1]));
                serialDTO.setFromSerial(DataUtil.safeToString(objects[2]));
                serialDTO.setToSerial(DataUtil.safeToString(objects[3]));
                serialDTO.setQuantity(DataUtil.safeToLong(objects[4]));
                serialDTOs.add(serialDTO);
            }
        }
        return serialDTOs;
    }

    @Override
    public List<StockTransSerialDTO> getRangeSerial(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId, String channelTypeId, Long serialStatus) throws Exception {
        StringBuilder sql = new StringBuilder();
        List<StockTransSerialDTO> serialDTOs = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(tableName)) {
            return serialDTOs;
        }
//        Long rowNum = 0L;

        Long configStockCard = getConfigStockCard();

        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(productOfferId);
        if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            sql.append(" SELECT serial as fromSerial, serial as toSerial, 1 as quantity ");
            sql.append(" FROM stock_handset  ");
            sql.append(" WHERE 1=1 ");
            if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)
                    && !Const.GOODS_STATE.DEMO.equals(stateId)) {
                sql.append("    AND owner_type = #owner_type ");
                sql.append("    AND owner_id = #owner_id ");
            }
            sql.append("    AND prod_offer_id = #prod_offer_id ");
            sql.append("    AND state_id = #state_id  ");
            sql.append("    AND status = #status  ");
            sql.append("    ORDER BY fromSerial  ");
        } else if (Const.STOCK_TYPE.STOCK_CARD_NAME.equalsIgnoreCase(tableName) && Const.STOCK_CARD_STRIP.equals(configStockCard)) {
            sql.append(" SELECT   from_serial, to_serial, (to_serial - from_serial + 1) quantity ");
            sql.append("   FROM  stock_card  ");
            sql.append(" WHERE 1=1 ");
            if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)
                    && !Const.GOODS_STATE.DEMO.equals(stateId)) {
                sql.append("    AND owner_type = #owner_type ");
                sql.append("    AND owner_id = #owner_id ");
            }
            sql.append("       AND prod_offer_id = #prod_offer_id ");
            sql.append("       AND state_id = #state_id ");
            sql.append("       AND status = #status ");
            sql.append("    ORDER BY from_serial,to_serial, quantity   ");
        } else if (Const.STOCK_TYPE.STOCK_NUMBER_NAME.equalsIgnoreCase(tableName)) {
            sql.append(" SELECT MIN (to_number(isdn)) as  fromSerial, ");
            sql.append("        MAX (to_number(isdn)) as toSerial, ");
            sql.append("        MAX(to_number(isdn)) -MIN(to_number(isdn)) +1 as quantity ");
            sql.append(" FROM ( ");
            sql.append("        SELECT isdn, isdn - ROW_NUMBER () OVER (ORDER BY to_number(isdn)) rn ");
            sql.append("        FROM stock_number ");
            sql.append(" WHERE 1=1 ");
            if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)
                    && !Const.GOODS_STATE.DEMO.equals(stateId)) {
                sql.append("    AND owner_type = #owner_type ");
                sql.append("    AND owner_id = #owner_id ");
            }
            sql.append("              AND prod_offer_id  = #prod_offer_id ");
            sql.append("              AND status  = #status ");
            sql.append("              ) ");
            sql.append(" GROUP BY rn");
            sql.append(" ORDER BY to_number (fromSerial), to_number(toSerial), quantity ");
//            rowNum = DataUtil.safeToLong(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.MAX_ROW_SERIAL, Const.OPTION_SET.MAX_ROW_SERIAL));

        } else {
            //phuong an cu
            sql.append(" SELECT MIN (to_number(serial)) as  fromSerial, ");
            sql.append("        MAX (to_number(serial)) as toSerial, ");
            sql.append("        MAX(to_number(serial)) -MIN(to_number(serial)) +1 as quantity ");
            sql.append(" FROM ( ");
            sql.append("        SELECT serial, serial - ROW_NUMBER () OVER (ORDER BY to_number(serial)) rn ");
            sql.append("        FROM ");
            sql.append(tableName);
            sql.append(" WHERE 1=1 ");
            if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)
                    && !Const.GOODS_STATE.DEMO.equals(stateId)) {
                sql.append("    AND owner_type = #owner_type ");
                sql.append("    AND owner_id = #owner_id ");
            }
            sql.append("              AND prod_offer_id  = #prod_offer_id ");
            sql.append("              AND state_id  = #state_id ");
            sql.append("              AND status  = #status ");
            if (!DataUtil.isNullOrEmpty(channelTypeId)) {
                sql.append(" and channel_type_id = #channel_type_id ");
            }
            sql.append("              ) ");
//            sql.append("              AND ROWNUM <= :rowNum ) ");
            sql.append(" GROUP BY rn ");
            sql.append(" ORDER BY to_number (fromSerial), to_number(toSerial), quantity ");
//            rowNum = DataUtil.safeToLong(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.MAX_ROW_SERIAL, Const.OPTION_SET.MAX_ROW_SERIAL));
        }

        Query query = em.createNativeQuery(sql.toString());
        if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)
                && !Const.GOODS_STATE.DEMO.equals(stateId)) {
            query.setParameter("owner_type", ownerType);
            query.setParameter("owner_id", ownerId);
        }
        query.setParameter("prod_offer_id", productOfferId);
        if (!Const.STOCK_TYPE.STOCK_NUMBER_NAME.equalsIgnoreCase(tableName)) {
            query.setParameter("state_id", stateId);
        }
        //tuydv1 neu state_id =5 -- hang thu hoi --> tim status =0
        if (Const.GOODS_STATE.RESCUE.equals(stateId) || DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)
                || Const.GOODS_STATE.DEMO.equals(stateId)) {
            query.setParameter("status", Const.STATUS.NO_ACTIVE);
        } else {
            query.setParameter("status", Const.STATUS.ACTIVE);
        }
       /* if (rowNum != 0L) {
            query.setParameter("rowNum", rowNum);
        }*/
        List<Object[]> queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList) && queryResultList.size() > 0) {
            Long index = 0L;
            for (Object[] objects : queryResultList) {
                StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                serialDTO.setFromSerial(DataUtil.safeToString(objects[0]));
                serialDTO.setToSerial(DataUtil.safeToString(objects[1]));
                serialDTO.setQuantity(DataUtil.safeToLong(objects[2]));
                serialDTO.setIndex(index++);
                serialDTOs.add(serialDTO);
            }
        }
        return serialDTOs;
    }

    private Long getConfigStockCard() throws Exception {
        Long configStockCard = 0L;
        List<OptionSetValue> lstConfigStockCard = optionSetValueRepo.getByOptionSetCode("STOCK_CARD_STRIP");
        if (!DataUtil.isNullOrEmpty(lstConfigStockCard) && !DataUtil.isNullObject(lstConfigStockCard.get(0).getValue())) {
            configStockCard = DataUtil.safeToLong(lstConfigStockCard.get(0).getValue());
        }
        return configStockCard;
    }

    @Override
    public List<StockTransSerialDTO> getRangeSerialFifo(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId, String channelTypeId, Long serialStatus) throws Exception {
        StringBuilder sql = new StringBuilder();
        List<StockTransSerialDTO> serialDTOs = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(tableName)) {
            return serialDTOs;
        }

        Long configStockCard = getConfigStockCard();


        String limitSerial = Const.L_VT_SHOP_ID.equals(ownerId) ? optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.THRESHOLD_EXP_SERIAL, Const.OPTION_SET.THRESHOLD_VT)
                : optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.THRESHOLD_EXP_SERIAL, Const.OPTION_SET.THRESHOLD_BRANCH);
        limitSerial = DataUtil.isNullOrEmpty(limitSerial) ? "1000000" : limitSerial;

        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(productOfferId);
        if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            sql.append(" SELECT serial as fromSerial, serial as toSerial, 1 as quantity ");
            sql.append("          ,trunc(create_date) ");
            sql.append(" FROM stock_handset  ");
            sql.append(" WHERE 1=1 ");
            if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                sql.append("    AND owner_type = #owner_type ");
                sql.append("    AND owner_id = #owner_id ");
            }
            sql.append("    AND prod_offer_id = #prod_offer_id ");
            sql.append("    AND state_id = #state_id  ");
            sql.append("    AND status = #status  ");
            sql.append("    ORDER BY trunc(create_date), fromSerial  ");
        } else if (Const.STOCK_TYPE.STOCK_CARD_NAME.equalsIgnoreCase(tableName) && Const.STOCK_CARD_STRIP.equals(configStockCard)) {
            sql.append(" SELECT   from_serial, to_serial, (to_serial - from_serial + 1) quantity ");
            sql.append("          ,create_date ");
            sql.append("   FROM  stock_card  ");
            sql.append(" WHERE 1=1 ");
            if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                sql.append("    AND owner_type = #owner_type ");
                sql.append("    AND owner_id = #owner_id ");
            }
            sql.append("       AND prod_offer_id = #prod_offer_id ");
            sql.append("       AND state_id = #state_id ");
            sql.append("       AND status = #status ");
            sql.append("    ORDER BY trunc(create_date), from_serial,to_serial, quantity   ");
        } else if (Const.STOCK_TYPE.STOCK_NUMBER_NAME.equalsIgnoreCase(tableName)) {
            sql.append(" SELECT MIN (to_number(isdn)) as  fromSerial, ");
            sql.append("        MAX (to_number(isdn)) as toSerial, ");
            sql.append("        MAX(to_number(isdn)) -MIN(to_number(isdn)) +1 as quantity, ");
            sql.append("        trunc(create_date)  ");
            sql.append(" FROM ( ");
            sql.append("        SELECT isdn, isdn - ROW_NUMBER () OVER (ORDER BY to_number(isdn)) rn ");
            sql.append("        FROM stock_number ");
            sql.append(" WHERE 1=1 ");
            if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                sql.append("    AND owner_type = #owner_type ");
                sql.append("    AND owner_id = #owner_id ");
            }
            sql.append("              AND prod_offer_id  = #prod_offer_id ");
            sql.append("              AND status  = #status ");
            sql.append("              ) ");
            sql.append(" GROUP BY rn, trunc(create_date) ");
            sql.append(" ORDER BY trunc(create_date), to_number (fromSerial), to_number(toSerial), quantity ");
//            rowNum = DataUtil.safeToLong(optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.MAX_ROW_SERIAL, Const.OPTION_SET.MAX_ROW_SERIAL));

        } else {
            //phuong an moi
            sql.append("	SELECT    ");
            sql.append("			 b.from_serial, ");
            sql.append("			 b.to_serial, ");
            sql.append("			 b.quantity, ");
            sql.append("			 b.receive_date AS create_date ");
            sql.append("	  FROM   table(bccs_im.func_gather_serial_v2 ( ");
            sql.append("					   cursor(  SELECT   owner_id stock_id, ");
            sql.append("										 prod_offer_id good_id, ");
            sql.append("										 state_id good_state, ");
            sql.append("										 trunc(nvl (create_date,update_datetime)), ");
            sql.append("										 serial, ");
            sql.append("										 status, ");
            sql.append("										 1 AS channel_type ");
            sql.append("								  FROM   bccs_im.").append(tableName);
            sql.append("								 WHERE       prod_offer_id = #prod_offer_id ");
            if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                sql.append("    AND owner_type = #owner_type ");
                sql.append("    AND owner_id = #owner_id ");
            }
            sql.append("										 AND status = #status  ");
            sql.append("										 AND state_id = #state_id  ");
            sql.append("							  ORDER BY   prod_offer_id, ");
            sql.append("										 trunc(nvl (create_date, update_datetime)), ");
            sql.append("										 TO_NUMBER (serial)), ");
            sql.append("					   " + limitSerial + ")) b ");
        }

        Query query = em.createNativeQuery(sql.toString());
        if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
            query.setParameter("owner_type", ownerType);
            query.setParameter("owner_id", ownerId);
        }
        query.setParameter("prod_offer_id", productOfferId);
        if (!Const.STOCK_TYPE.STOCK_NUMBER_NAME.equalsIgnoreCase(tableName)) {
            query.setParameter("state_id", stateId);
        }
        //tuydv1 neu state_id =5 -- hang thu hoi --> tim status =0
        if (Const.GOODS_STATE.RESCUE.equals(stateId) || DataUtil.safeEqual(serialStatus, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
            query.setParameter("status", Const.STATUS.NO_ACTIVE);
        } else {
            query.setParameter("status", Const.STATUS.ACTIVE);
        }
        List<Object[]> queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList) && queryResultList.size() > 0) {
            Long index = 0L;
            for (Object[] objects : queryResultList) {
                StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                serialDTO.setFromSerial(DataUtil.safeToString(objects[0]));
                serialDTO.setToSerial(DataUtil.safeToString(objects[1]));
                serialDTO.setQuantity(DataUtil.safeToLong(objects[2]));
                serialDTO.setCreateDatetime(objects[3] != null ? (Date) objects[3] : null);
                serialDTO.setIndex(index++);
                serialDTOs.add(serialDTO);
            }
        }
        return serialDTOs;
    }

    @Override
    public List<String> getListSerialValid(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId,
                                           String fromSerial, String toSerial, Long rowNum, Long serialStatus) throws Exception {
        StringBuilder sql = new StringBuilder();
        if (DataUtil.isNullOrEmpty(tableName)) {
            return new ArrayList<String>();
        }

        Long configStockCard = 0L;
        List<OptionSetValueDTO> lstConfigStockCard = optionSetValueService.getByOptionSetCode("STOCK_CARD_STRIP");
        if (!DataUtil.isNullOrEmpty(lstConfigStockCard) && !DataUtil.isNullObject(lstConfigStockCard.get(0).getValue())) {
            configStockCard = DataUtil.safeToLong(lstConfigStockCard.get(0).getValue());
        }

        if (Const.STOCK_TYPE.STOCK_CARD_NAME.equals(tableName) && Const.STOCK_CARD_STRIP.equals(configStockCard)) {
            sql.append(" SELECT DISTINCT from_serial as serial ");
        } else if (!Const.STOCK_TYPE.STOCK_NUMBER_NAME.equals(tableName)) {
            sql.append(" SELECT DISTINCT serial");
        } else {
            sql.append(" SELECT DISTINCT isdn");
        }
        sql.append(" FROM ");
        sql.append(tableName);
        sql.append(" WHERE 1=1 ");
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(productOfferId);
        if (Const.STOCK_TYPE.STOCK_NUMBER_NAME.equals(tableName)) {
            sql.append(" AND to_number(isdn) >= #fromSerial ");
            sql.append(" AND to_number(isdn) <= #toSerial ");
        } else if (Const.STOCK_TYPE.STOCK_HANDSET_NAME.equals(tableName)
                && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            sql.append(" AND serial = #serial ");
        } else if (Const.STOCK_TYPE.STOCK_CARD_NAME.equals(tableName) && Const.STOCK_CARD_STRIP.equals(configStockCard)) {
            sql.append(" AND to_number(to_serial) >= #toSerial ");
            sql.append(" AND to_number(from_serial) <= #fromSerial ");
        } else {
            sql.append(" AND to_number(serial) >= #fromSerial ");
            sql.append(" AND to_number(serial) <= #toSerial ");
        }
        if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, DataUtil.safeToLong(Const.STATUS.NO_ACTIVE))
                && !Const.GOODS_STATE.DEMO.equals(stateId)) {
            sql.append(" AND owner_type = #owner_type ");
            sql.append(" AND owner_id = #owner_id ");
        }
        sql.append(" AND prod_offer_id = #prod_offer_id ");
        if (!Const.STOCK_TYPE.STOCK_NUMBER_NAME.equals(tableName)) {
            sql.append(" AND state_id = #state_id ");
        }
        sql.append(" AND status = #status ");
        if (Const.STOCK_TYPE.STOCK_NUMBER_NAME.equals(tableName)) {
            sql.append(" ORDER BY isdn ");
        } else if (Const.STOCK_TYPE.STOCK_HANDSET_NAME.equals(tableName)
                && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            sql.append(" ORDER BY serial ");
        } else {
            sql.append(" ORDER BY to_number(serial) ");
        }
        Query query = em.createNativeQuery(sql.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 60);
        if (Const.STOCK_TYPE.STOCK_HANDSET_NAME.equals(tableName)
                && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            query.setParameter("serial", fromSerial);
        } else {
            query.setParameter("fromSerial", new BigInteger(fromSerial));
            query.setParameter("toSerial", new BigInteger(toSerial));
        }
        if (!Const.GOODS_STATE.RESCUE.equals(stateId) && !DataUtil.safeEqual(serialStatus, DataUtil.safeToLong(Const.STATUS.NO_ACTIVE))
                && !Const.GOODS_STATE.DEMO.equals(stateId)) {
            query.setParameter("owner_type", ownerType);
            query.setParameter("owner_id", ownerId);
        }

        query.setParameter("prod_offer_id", productOfferId);
        if (!Const.STOCK_TYPE.STOCK_NUMBER_NAME.equals(tableName)) {
            query.setParameter("state_id", stateId);
        }
        if (Const.GOODS_STATE.RESCUE.equals(stateId) || DataUtil.safeEqual(serialStatus, DataUtil.safeToLong(Const.STATUS.NO_ACTIVE))
                || Const.GOODS_STATE.DEMO.equals(stateId)) {
            query.setParameter("status", Const.STATUS.NO_ACTIVE);
        } else {
            query.setParameter("status", Const.STATUS.ACTIVE);
        }
        List<String> result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            if (rowNum != null && rowNum.intValue() < result.size()) {
                return result.subList(0, rowNum.intValue());
            }
            return result;
        }
        return new ArrayList<String>();
    }


    /**
     * thaont19
     *
     * @param
     * @return
     * @throws Exception
     */
    public List<StockTransSerialDTO> findStockTransSerialByProductOfferType(WareHouseInfoDTO infoDTO) throws Exception {
        List<StockTransSerialDTO> result = Lists.newArrayList();
        //lay ten luu serial
        String tableName = getTableNameOfStockSerial(infoDTO.getProductOfferTypeId());

        //Doi voi mat hang so thi bo qua
        if (DataUtil.isNullObject(tableName) || "STOCK_NUMBER".equalsIgnoreCase(tableName)) {
            return result;
        }

        //lay du lieu serial
        if ("stock_handset".equalsIgnoreCase(tableName)) {
            result = findStockTransSerialAtStockHandset(infoDTO);
        } else {
            result = findStockTransSerialAtStockNumber(infoDTO, tableName);
        }

        return result;
    }

    private List<StockTransSerialDTO> findStockTransSerialAtStockCard(WareHouseInfoDTO infoDTO) {
        List<StockTransSerialDTO> results = Lists.newArrayList();

        String sql = "select from_serial, to_serial, (to_serial-from_serial + 1) quantity  " +
                "from stock_card  " +
                "where owner_type = ? and owner_id = ?  " +
                "and prod_offer_id = ? and state_id = ? ";
        if (!infoDTO.isExport()) {
            sql += " AND ROWNUM <= ? ";
        }
        sql += " ORDER BY   from_serial ";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, infoDTO.getOwnerType());
        query.setParameter(2, infoDTO.getOwnerId());
        query.setParameter(3, infoDTO.getProductOfferingId());
        query.setParameter(4, infoDTO.getStateId());
        if (!infoDTO.isExport()) {
            query.setParameter(5, Const.NUMBER_SEARCH.NUMBER_ALL);
        }

        List<Object[]> lstResult = query.getResultList();
        for (Object[] object : lstResult) {
            int index = 0;
            StockTransSerialDTO stockSerial = new StockTransSerialDTO();
            stockSerial.setFromSerial(DataUtil.safeToString(object[index++]));
            stockSerial.setToSerial(DataUtil.safeToString(object[index++]));
            stockSerial.setQuantity(DataUtil.safeToLong(object[index]));
            results.add(stockSerial);
        }
        return results;
    }

    private List<StockTransSerialDTO> findStockTransSerialAtStockHandset(WareHouseInfoDTO infoDTO) {
        List<StockTransSerialDTO> results = new ArrayList<StockTransSerialDTO>();

        String sql = "select serial as fromSerial, serial as toSerial, 1 as quantity  " +
                "from stock_handset where owner_type = ? and owner_id = ?  " +
                "and prod_offer_id = ?  and state_id = ? and status = ? ";
        if (!infoDTO.isExport()) {
            sql += " AND ROWNUM <= ? ";
        }
        sql += " ORDER BY   serial ";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, infoDTO.getOwnerType());
        query.setParameter(2, infoDTO.getOwnerId());
        query.setParameter(3, infoDTO.getProductOfferingId());
        query.setParameter(4, infoDTO.getStateId());
        query.setParameter(5, Const.STATUS.ACTIVE);
        if (!infoDTO.isExport()) {
            query.setParameter(6, Const.NUMBER_SEARCH.NUMBER_ALL);
        }

        List<Object[]> lstResult = query.getResultList();
        for (Object[] object : lstResult) {
            int index = 0;
            StockTransSerialDTO stockSerial = new StockTransSerialDTO();
            stockSerial.setFromSerial(DataUtil.safeToString(object[index++]));
            stockSerial.setToSerial(DataUtil.safeToString(object[index++]));
            stockSerial.setQuantity(DataUtil.safeToLong(object[index]));
            results.add(stockSerial);
        }
        return results;
    }

    private List<StockTransSerialDTO> findStockTransSerialAtStockNumber(WareHouseInfoDTO infoDTO, String tableName) {
        List<StockTransSerialDTO> results = Lists.newArrayList();

        String sql = "SELECT   MIN (to_number(serial)) as  fromSerial, " +
                "           MAX (to_number(serial)) as toSerial, " +
                "           MAX(to_number(serial)) -MIN(to_number(serial)) + 1 as quantity " +
                "    FROM   (SELECT   serial,  serial - ROW_NUMBER () OVER (ORDER BY TO_NUMBER (serial)) rn " +
                "              FROM   " + tableName + " where   prod_offer_id = ? " +
                "              and owner_type = ? and owner_id = ?  and state_id = ? and status = ?) GROUP BY  rn " +
                " ORDER BY   TO_NUMBER (fromserial) ";


        Query query = em.createNativeQuery(sql);
        query.setParameter((int) 1, infoDTO.getProductOfferingId());
        query.setParameter((int) 2, infoDTO.getOwnerType());
        query.setParameter((int) 3, infoDTO.getOwnerId());
        query.setParameter((int) 4, infoDTO.getStateId());
        query.setParameter((int) 5, Const.STATUS.ACTIVE);


        List<Object[]> lstResult = query.getResultList();
        for (Object[] object : lstResult) {
            int index = 0;
            StockTransSerialDTO stockSerial = new StockTransSerialDTO();
            stockSerial.setFromSerial(DataUtil.safeToString(object[index++]));
            stockSerial.setToSerial(DataUtil.safeToString(object[index++]));
            stockSerial.setQuantity(DataUtil.safeToLong(object[index]));
            results.add(stockSerial);
        }
        return results;
    }

    private String getTableNameOfStockSerial(String productOfferType) {
        String tableName = null;
        StringBuilder sql = new StringBuilder("");
        sql.append(" select table_name from product_offer_type ");
        sql.append(" where parent_id = #parentProduct and product_offer_type_id = #productOfferType ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("parentProduct", Const.PRODUCT_OFFERING.PARRENT_ID);
        query.setParameter("productOfferType", productOfferType);

        List<Object[]> lstResult = query.getResultList();
        if (!lstResult.isEmpty()) {
            tableName = DataUtil.safeToString(lstResult.get(0));
        }
        return tableName;
    }

    @Override
    public List<String> getRangeSerialStockCardValid(Long ownerType, Long ownerId, Long productOfferId, Long stateId,
                                                     String fromSerial, String toSerial, Long quantity, String status) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT serial ");
        sql.append(" FROM   stock_card ");
        sql.append(" WHERE  1 = 1 ");
        sql.append("    AND  TO_NUMBER (serial) >= #fromSerial ");
        sql.append("    AND  TO_NUMBER (serial) <= #toSerial ");
        sql.append("    AND owner_type = #ownerType ");
        sql.append("    AND owner_id = #ownerId ");
        sql.append("    AND prod_offer_id = #prodOfferId ");
        sql.append("    AND state_id = #stateId ");
        sql.append("    AND status = #status ");
        sql.append(" ORDER BY TO_NUMBER(serial) ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", DataUtil.safeToString(ownerType));
        query.setParameter("fromSerial", DataUtil.safeToLong(fromSerial));
        query.setParameter("toSerial", DataUtil.safeToLong(toSerial));
        query.setParameter("prodOfferId", productOfferId);
        query.setParameter("stateId", stateId);
        query.setParameter("status", status);
        List<String> resultSerial = query.getResultList();
        return resultSerial;
    }

    @Override
    public List<String> getRangeSerialStockCardValidWithoutOwner(Long productOfferId, Long stateId,
                                                                 String fromSerial, String toSerial, Long quantity, String status) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT   serial ");
        sql.append(" FROM   stock_card ");
        sql.append(" WHERE       1 = 1 ");
        sql.append("    AND prod_offer_id = #prodOfferId ");
        sql.append("    AND status = #status ");
        sql.append("    AND state_id = #stateId ");
        sql.append("    AND (TO_NUMBER (serial) <= #toSerial AND TO_NUMBER (serial) >= #fromSerial) ");
        sql.append(" ORDER BY serial ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("fromSerial", DataUtil.safeToLong(fromSerial));
        query.setParameter("toSerial", DataUtil.safeToLong(toSerial));
        query.setParameter("prodOfferId", productOfferId);
        query.setParameter("stateId", stateId);
        query.setParameter("status", status);
        List<String> resultSerial = query.getResultList();
        return resultSerial;
    }


    @Override

    public List<StockTransSerialDTO> getListSerialFromTable(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName) throws Exception {
        List<StockTransSerialDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        String getSerialIm1 = optionSetValueService.getValueByTwoCodeOption("GET_SERIAL_IM1", "GET_SERIAL_IM1");

        EntityManager emQuery = em;
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
        if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            builder.append("  SELECT   serial as from_serial, serial as to_serial, 1 as quantity");
            builder.append("    FROM   bccs_im.stock_handset");
            builder.append("   WHERE       prod_offer_id = #prodOfferId");
            builder.append("           AND owner_id = #ownerId");
            builder.append("           AND owner_type = #ownerType");
            builder.append("           AND status = 1");
            builder.append("           AND state_id = #stateId");
        } else {
            if (DataUtil.safeEqual(getSerialIm1, "1")) {
                if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_ACCESSORIES)) {
                    tableName = Const.STOCK_TYPE.STOCK_ACCESSORIES_NAME;
                }
                emQuery = emIm1;
                builder.append(" SELECT   b.from_serial,b.to_serial,b.quantity "
                        + "  FROM   table(bccs_im.show_serial_trip_new ( "
                        + "                   cursor(  SELECT   owner_id stock_id, "
                        + "                                     stock_model_id good_id, "
                        + "                                     state_id good_state, "
                        + "                                     serial, "
                        + "                                     status, "
                        + "                                     1 AS channel_type "
                        + "                              FROM  bccs_im." + tableName
                        + "                             WHERE   stock_model_id = #prodOfferId "
                        + "                                     AND owner_id = #ownerId "
                        + "                                     AND owner_type = #ownerType "
                        + "                                     AND status = 1 "
                        + "                                     AND state_id = #stateId "
                        + "                          ORDER BY   stock_model_id, TO_NUMBER (serial)),10000000)) b ");
            } else {
                builder.append("  SELECT   b.from_serial,b.to_serial,b.quantity");
                builder.append("    FROM   table(func_gather_serial ( ");
                builder.append("                     cursor(  SELECT   owner_id stock_id, ");
                builder.append("                                       prod_offer_id good_id, ");
                builder.append("                                       state_id good_state, ");
                builder.append("                                       serial, ");
                builder.append("                                       status, ");
                builder.append("                                       1 AS channel_type ");
                builder.append("                               FROM ");
                builder.append(tableName);
                builder.append("                               WHERE   prod_offer_id = #prodOfferId ");
                builder.append("                                       AND owner_id = #ownerId");
                builder.append("                                       AND owner_type = #ownerType ");
                builder.append("                                       AND status = 1 ");
                builder.append("                                       AND state_id = #stateId");
                builder.append("                            ORDER BY   prod_offer_id, TO_NUMBER (serial)),10000000)) b");
            }
        }

        Query query = emQuery.createNativeQuery(builder.toString());
        //set timeout
        query.setHint(QueryHints.JDBC_TIMEOUT, 180);
        //
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("stateId", stateId);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
                stockTransSerialDTO.setFromSerial(DataUtil.safeToString(ob[index++]));
                stockTransSerialDTO.setToSerial(DataUtil.safeToString(ob[index++]));
                stockTransSerialDTO.setQuantity(DataUtil.safeToLong(ob[index]));
                result.add(stockTransSerialDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockInspectCheckDTO> getListGatherSerial(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName, String fromSerial, String toSerial) throws Exception {
        List<StockInspectCheckDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        String getSerialIm1 = optionSetValueService.getValueByTwoCodeOption("GET_SERIAL_IM1", "GET_SERIAL_IM1");
        //
//        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
        EntityManager emQuery = em;
        if (DataUtil.safeEqual(getSerialIm1, "1")) {
            if (DataUtil.safeEqual(productOfferTypeId, Const.STOCK_TYPE.STOCK_ACCESSORIES)) {
                tableName = Const.STOCK_TYPE.STOCK_ACCESSORIES_NAME;
            }
            emQuery = emIm1;
            builder.append("  SELECT COUNT (1) AS quantity, ");
            builder.append("                      pr.stock_model_id, ");
            builder.append("                      pr.stock_model_code AS productCode, ");
            builder.append("                      pr.name AS productName, ");
            builder.append("                      model.state_id AS stateId ");
            builder.append("                 FROM ");
            builder.append(" bccs_im.").append(tableName);
            builder.append("                    model, ");
            builder.append("                 bccs_im.stock_model pr");
            builder.append("                WHERE     1 = 1 ");
            builder.append("                      AND model.stock_model_id = pr.stock_model_id ");
            builder.append("                      AND model.owner_id = #ownerId");
            builder.append("                      AND model.owner_type = #ownerType");
            if (!DataUtil.isNullObject(productOfferTypeId)) {
                builder.append("                  AND pr.stock_type_id = #productOfferTypeId");
            }
            if (!DataUtil.isNullObject(prodOfferId)) {
                builder.append("                  AND pr.stock_model_id = #prodOfferId");
            }
            if (!DataUtil.isNullObject(stateId)) {
                builder.append("                  AND model.state_id = #stateId");
            }
            builder.append("                      AND model.status = 1 ");
            if (DataUtil.safeEqual(productOfferTypeId, Const.STOCK_TYPE.STOCK_HANDSET)) {
                builder.append("                   AND model.serial >= #fromSerial");
                builder.append("                   AND model.serial <= #toSerial");
                builder.append("                   AND length(model.serial) = length(#fromSerial)");
            } else {
                builder.append("                   AND TO_NUMBER (model.serial) >= TO_NUMBER (#fromSerial)");
                builder.append("                   AND TO_NUMBER (model.serial) <= TO_NUMBER (#toSerial)");
            }

            builder.append("  GROUP BY pr.stock_model_id, pr.stock_model_code, pr.name, model.state_id");
        } else {
            builder.append("  SELECT COUNT (1) AS quantity, ");
            builder.append("                      pr.prod_offer_id, ");
            builder.append("                      pr.code AS productCode, ");
            builder.append("                      pr.name AS productName, ");
            builder.append("                      model.state_id AS stateId ");
            builder.append("                 FROM ");
            builder.append(tableName);
            builder.append("                    model, ");
            builder.append("                 product_offering pr");
            builder.append("                WHERE     1 = 1 ");
            builder.append("                      AND model.prod_offer_id = pr.prod_offer_id ");
            builder.append("                      AND model.owner_id = #ownerId");
            builder.append("                      AND model.owner_type = #ownerType");
            if (!DataUtil.isNullObject(productOfferTypeId)) {
                builder.append("                      AND pr.product_offer_type_id = #productOfferTypeId");
            }
            if (!DataUtil.isNullObject(prodOfferId)) {
                builder.append("                      AND pr.prod_offer_id = #prodOfferId");
            }
            if (!DataUtil.isNullObject(stateId)) {
                builder.append("                  AND model.state_id = #stateId");
            }
            builder.append("                      AND model.status = 1 ");
            if (DataUtil.safeEqual(productOfferTypeId, Const.STOCK_TYPE.STOCK_HANDSET)) {
                builder.append("                   AND model.serial >= #fromSerial");
                builder.append("                   AND model.serial <= #toSerial");
                builder.append("                   AND length(model.serial) = length(#fromSerial)");
            } else {
                builder.append("                   AND TO_NUMBER (model.serial) >= TO_NUMBER (#fromSerial)");
                builder.append("                   AND TO_NUMBER (model.serial) <= TO_NUMBER (#toSerial)");
            }

            builder.append("  GROUP BY pr.prod_offer_id, pr.code, pr.name, model.state_id");
        }

        Query query = emQuery.createNativeQuery(builder.toString());
        if (!DataUtil.isNullObject(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullObject(productOfferTypeId)) {
            query.setParameter("productOfferTypeId", productOfferTypeId);
        }
        if (!DataUtil.isNullObject(stateId)) {
            query.setParameter("stateId", stateId);
        }
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("fromSerial", fromSerial);
        query.setParameter("toSerial", toSerial);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockInspectCheckDTO stockInspectCheckDTO = new StockInspectCheckDTO();
                stockInspectCheckDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setProductCode(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setProductName(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setStateId(DataUtil.safeToLong(ob[index]));
                stockInspectCheckDTO.setFromSerial(fromSerial);
                stockInspectCheckDTO.setToSerial(toSerial);
                result.add(stockInspectCheckDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockInspectCheckDTO> getListSerialFromList(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName, List<String> lstSerial) throws Exception {
        List<StockInspectCheckDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        String getSerialIm1 = optionSetValueService.getValueByTwoCodeOption("GET_SERIAL_IM1", "GET_SERIAL_IM1");
        //
        EntityManager emQuery = em;
        if (DataUtil.safeEqual(getSerialIm1, "1")) {
            if (DataUtil.safeEqual(productOfferTypeId, Const.STOCK_TYPE.STOCK_ACCESSORIES)) {
                tableName = Const.STOCK_TYPE.STOCK_ACCESSORIES_NAME;
            }
            emQuery = emIm1;
            builder.append("  SELECT 1 AS quantity, ");
            builder.append("                      pr.stock_model_id, ");
            builder.append("                      pr.stock_model_code AS productCode, ");
            builder.append("                      pr.name AS productName, ");
            builder.append("                      model.state_id AS stateId, ");
            builder.append("                      model.serial AS fromSerial, ");
            builder.append("                      model.serial AS toSerial ");
            builder.append("                 FROM ");
            builder.append(" bccs_im.").append(tableName);
            builder.append("                    model, ");
            builder.append("                 bccs_im.stock_model pr");
            builder.append("                WHERE     1 = 1 ");
            builder.append("                      AND model.stock_model_id = pr.stock_model_id ");
            builder.append("                      AND model.owner_id = #ownerId");
            builder.append("                      AND model.owner_type = #ownerType");
            if (!DataUtil.isNullObject(productOfferTypeId)) {
                builder.append("                      AND pr.stock_type_id = #productOfferTypeId");
            }
            if (!DataUtil.isNullObject(prodOfferId)) {
                builder.append("                      AND pr.stock_model_id = #prodOfferId");
            }
            if (!DataUtil.isNullObject(stateId)) {
                builder.append("                      AND model.state_id = #stateId");
            }
            builder.append("                      AND model.status = 1 ");

            if (!DataUtil.isNullOrEmpty(lstSerial)) {
                if (DataUtil.safeEqual(productOfferTypeId, Const.STOCK_TYPE.STOCK_HANDSET)) {
                    builder.append("                   AND model.serial " + DbUtil.createInQuery("serial", lstSerial));
                } else {
                    builder.append("                   AND TO_NUMBER (model.serial)" + DbUtil.createInQuery("serial", lstSerial));
                }
            }
        } else {
            builder.append("  SELECT 1 AS quantity, ");
            builder.append("                      pr.prod_offer_id, ");
            builder.append("                      pr.code AS productCode, ");
            builder.append("                      pr.name AS productName, ");
            builder.append("                      model.state_id AS stateId, ");
            builder.append("                      model.serial AS fromSerial, ");
            builder.append("                      model.serial AS toSerial ");
            builder.append("                 FROM ");
            builder.append(tableName);
            builder.append("                    model, ");
            builder.append("                 product_offering pr");
            builder.append("                WHERE     1 = 1 ");
            builder.append("                      AND model.prod_offer_id = pr.prod_offer_id ");
            builder.append("                      AND model.owner_id = #ownerId");
            builder.append("                      AND model.owner_type = #ownerType");
            if (!DataUtil.isNullObject(productOfferTypeId)) {
                builder.append("                      AND pr.product_offer_type_id = #productOfferTypeId");
            }
            if (!DataUtil.isNullObject(prodOfferId)) {
                builder.append("                      AND pr.prod_offer_id = #prodOfferId");
            }
            if (!DataUtil.isNullObject(stateId)) {
                builder.append("                      AND model.state_id = #stateId");
            }
            builder.append("                      AND model.status = 1 ");

            if (!DataUtil.isNullOrEmpty(lstSerial)) {
                if (DataUtil.safeEqual(productOfferTypeId, Const.STOCK_TYPE.STOCK_HANDSET)) {
                    builder.append("                   AND model.serial " + DbUtil.createInQuery("serial", lstSerial));
                } else {
                    builder.append("                   AND TO_NUMBER (model.serial)" + DbUtil.createInQuery("serial", lstSerial));
                }
            }
        }
        Query query = emQuery.createNativeQuery(builder.toString());
        if (!DataUtil.isNullObject(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullObject(productOfferTypeId)) {
            query.setParameter("productOfferTypeId", productOfferTypeId);
        }
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        if (!DataUtil.isNullObject(stateId)) {
            query.setParameter("stateId", stateId);
        }
        if (!DataUtil.isNullOrEmpty(lstSerial)) {
            DbUtil.setParamInQuery(query, "serial", lstSerial);
        }
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockInspectCheckDTO stockInspectCheckDTO = new StockInspectCheckDTO();
                stockInspectCheckDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setProductCode(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setProductName(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                stockInspectCheckDTO.setFromSerial(DataUtil.safeToString(ob[index++]));
                stockInspectCheckDTO.setToSerial(DataUtil.safeToString(ob[index]));
                result.add(stockInspectCheckDTO);
            }
        }
        return result;
    }

    @Override
    public int updateStatusStockSerial(Long ownerId, Long ownerType, Long prodOfferId, String fromSerial, String toSerial, Long newStatus, Long oldStatus) throws Exception {
        try {
            ProductOfferingDTO productOffering = productOfferingService.findOne(prodOfferId);
            String tableName = IMServiceUtil.getTableNameByOfferType(productOffering.getProductOfferTypeId());
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(" UPDATE ").append(tableName);
            strQuery.append(" SET status = #newStatus, update_datetime = sysdate ");
            strQuery.append(" WHERE owner_id = #ownerId AND owner_type = #ownerType AND prod_offer_id = #prodOfferId ");
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOffering.getProductOfferTypeId())) {
                strQuery.append("       AND serial = #fromSerial ");
            } else {
                strQuery.append("       AND to_number(serial) >= to_number(#fromSerial) ");
                strQuery.append("       AND to_number(serial) <= to_number(#toSerial) ");
            }
            strQuery.append("       AND status = #oldStatus ");
            Query query = em.createNativeQuery(strQuery.toString());
            query.setParameter("newStatus", newStatus);
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
            query.setParameter("prodOfferId", prodOfferId);
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOffering.getProductOfferTypeId())) {
                query.setParameter("fromSerial", fromSerial);
            } else {
                query.setParameter("fromSerial", fromSerial);
                query.setParameter("toSerial", toSerial);
            }
            query.setParameter("oldStatus", oldStatus);
            return query.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }

    }

    @Override
    public Long getDepostiPriceFromStockX(Long ownerId, Long ownerType, Long prodOfferId, String serial) throws Exception {
        try {
            ProductOfferingDTO productOffering = productOfferingService.findOne(prodOfferId);
            String tableName = IMServiceUtil.getTableNameByOfferType(productOffering.getProductOfferTypeId());
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(" SELECT deposit_price FROM ").append(tableName);
            strQuery.append(" WHERE owner_id = #ownerId AND owner_type = #ownerType AND prod_offer_id = #prodOfferId ");
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOffering.getProductOfferTypeId())) {
                strQuery.append("       AND serial = #serial ");
            } else {
                strQuery.append("       AND to_number(serial) = to_number(#serial) ");
            }
            Query query = em.createNativeQuery(strQuery.toString());
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
            query.setParameter("prodOfferId", prodOfferId);
            query.setParameter("serial", serial);
            List result = query.getResultList();
            if (!DataUtil.isNullOrEmpty(result)) {
                return DataUtil.safeToLong(result.get(0));
            }
            return 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }

    }

    @Override
    public Long getDepostiPriceForRetrieve(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String serial) throws Exception {
        try {
            ProductOfferingDTO productOffering = productOfferingService.findOne(prodOfferId);
            String tableName = IMServiceUtil.getTableNameByOfferType(productOffering.getProductOfferTypeId());
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(" SELECT deposit_price FROM ").append(tableName);
            strQuery.append(" WHERE owner_id = #ownerId AND owner_type = #ownerType AND prod_offer_id = #prodOfferId AND state_id= #stateId ");
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOffering.getProductOfferTypeId())) {
                strQuery.append("       AND serial = #serial ");
            } else {
                strQuery.append("       AND to_number(serial) = to_number(#serial) ");
            }
            Query query = em.createNativeQuery(strQuery.toString());
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
            query.setParameter("prodOfferId", prodOfferId);
            query.setParameter("stateId", stateId);
            query.setParameter("serial", serial);
            List result = query.getResultList();
            if (!DataUtil.isNullOrEmpty(result)) {
                return DataUtil.safeToLong(result.get(0));
            }
            return 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }

    }

    @Override
    public Long getDepostiPriceByRangeSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String fromSerial, String toSerial) throws Exception {
        try {
            ProductOfferingDTO productOffering = productOfferingService.findOne(prodOfferId);
            String tableName = IMServiceUtil.getTableNameByOfferType(productOffering.getProductOfferTypeId());
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(" SELECT SUM(deposit_price) FROM ").append(tableName);
            strQuery.append(" WHERE owner_id = #ownerId AND owner_type = #ownerType AND prod_offer_id = #prodOfferId AND state_id= #stateId ");
            strQuery.append("       AND to_number(serial) >= to_number(#fromSerial) ");
            strQuery.append("       AND to_number(serial) <= to_number(#toSerial) ");
            Query query = em.createNativeQuery(strQuery.toString());
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
            query.setParameter("prodOfferId", prodOfferId);
            query.setParameter("stateId", stateId);
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
            List result = query.getResultList();
            if (!DataUtil.isNullOrEmpty(result)) {
                return DataUtil.safeToLong(result.get(0));
            }
            return 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }

    }


    @Override
    public List<StockTransSerialDTO> getListSerialQuantity(Long prodOfferId, Long ownerId, Long ownerType,
                                                           Long stateId, String status, String tableName, String fromSerial, String toSerial) throws Exception {
        List<StockTransSerialDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT COUNT (1) AS quantity, ");
        builder.append("                      model.prod_offer_id, ");
        builder.append("                      model.state_id AS stateId, model.owner_type, model.owner_id ");
        builder.append("                 FROM ");
        builder.append(tableName);
        builder.append("                    model ");
        builder.append("                WHERE     1 = 1 ");
        if (!DataUtil.isNullObject(ownerId)) {
            builder.append("                      AND model.owner_id = #ownerId");
        }
        if (!DataUtil.isNullObject(ownerType)) {
            builder.append("                      AND model.owner_type = #ownerType");
        }
        if (!DataUtil.isNullObject(prodOfferId)) {
            builder.append("                      AND model.prod_offer_id = #prodOfferId");
        }
        if (!DataUtil.isNullObject(stateId)) {
            builder.append("                      AND model.state_id = #stateId");
        }
        builder.append("                      AND model.status = #status ");
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
        if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            builder.append("                   AND model.serial >= #fromSerial");
            builder.append("                   AND model.serial <= #toSerial");
            builder.append("                   AND length(model.serial) = length(#fromSerial)");
        } else {
            builder.append("                   AND TO_NUMBER (model.serial) >= TO_NUMBER (#fromSerial)");
            builder.append("                   AND TO_NUMBER (model.serial) <= TO_NUMBER (#toSerial)");
        }

        builder.append("  GROUP BY model.prod_offer_id, model.state_id, model.owner_type, model.owner_id ");
        Query query = em.createNativeQuery(builder.toString());
        if (!DataUtil.isNullObject(prodOfferId)) {
            query.setParameter("prodOfferId", prodOfferId);
        }
        if (!DataUtil.isNullObject(ownerId)) {
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullObject(ownerType)) {
            query.setParameter("ownerType", ownerType);
        }
        if (!DataUtil.isNullObject(stateId)) {
            query.setParameter("stateId", stateId);
        }
        query.setParameter("fromSerial", fromSerial);
        query.setParameter("toSerial", toSerial);
        query.setParameter("status", status);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
                stockTransSerialDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setOwnerId(DataUtil.safeToLong(ob[index]));
                result.add(stockTransSerialDTO);
            }
        }
        return result;
    }

    @Override
    public List<LookupSerialByFileDTO> searchSerialExact(Long prodductOfferTypeId, List<String> lsSerial, String serialGpon, boolean isSearchNew) throws Exception {
        List<LookupSerialByFileDTO> result = Lists.newArrayList();
        String tableName = IMServiceUtil.getTableNameByOfferType(prodductOfferTypeId);
        if (!isSearchNew) {
            tableName = "HIS_" + tableName;
        }

        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("SELECT   a.serial AS serial, ");
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodductOfferTypeId)) {
            strQuery.append("      a.serial_gpon as serialGpon, ");
        } else {
            strQuery.append("      NULL as serialGpon,");
        }
        strQuery.append("         b.code AS prodOfferCode, ");
        strQuery.append("         b.name AS prodOfferName, ");
        strQuery.append("          b.prod_offer_id, ");
        strQuery.append("         a.state_id AS stateid, ");
        strQuery.append("         a.owner_type AS ownertype, ");
        strQuery.append("         a.owner_id AS ownerid, ");
        strQuery.append("         d.owner_code AS ownercode, ");
        strQuery.append("         d.owner_name AS ownername, ");
        strQuery.append("         a.status AS status, ");
        // a.receiver_Name as shopImportName,
        strQuery.append("         a.connection_status AS connectionstatus, ");
        strQuery.append("         a.connection_type AS connectiontype, ");
        strQuery.append("         TO_CHAR (a.connection_date ,'dd/MM/yyyy') AS connectiondatestr ");
        if (isSearchNew) {
            strQuery.append("         ,pc.contract_code AS contractcode, ");
            strQuery.append("         TO_CHAR (pc.contract_date, 'dd/MM/yyyy') AS contractdatestr, ");
            strQuery.append("         pc.po_code AS pocode, ");
            strQuery.append("         TO_CHAR (pc.po_date, 'dd/MM/yyyy') AS podatestr, ");
            strQuery.append("         TO_CHAR (pc.request_date, 'dd/MM/yyyy') AS requestimportdatestr, ");
            strQuery.append("         TO_CHAR (pc.import_date, 'dd/MM/yyyy') AS importstockdatestr ");
        }
        strQuery.append(" FROM   bccs_im." + tableName + " a, ");
        strQuery.append("         bccs_im.product_offering b, ");
        strQuery.append("         bccs_im.v_shop_staff d ");
        if (isSearchNew) {
            strQuery.append("         ,bccs_im.partner_contract pc ");
        }
        strQuery.append(" WHERE    a.prod_offer_id = b.prod_offer_id ");
        strQuery.append("         AND a.owner_type = d.owner_type ");
        strQuery.append("         AND a.owner_id = d.owner_id ");
        strQuery.append("         AND a.prod_offer_id IN (SELECT   prod_offer_id ");
        strQuery.append("                                   FROM   product_offering ");
        strQuery.append("                                  WHERE   product_offer_type_id = #product_offer_type_id) ");
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodductOfferTypeId) && !DataUtil.isNullOrEmpty(serialGpon)) {
            strQuery.append("         AND a.serial_gpon = #serialGpon ");
        }
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodductOfferTypeId) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(prodductOfferTypeId)) {
            if (lsSerial != null && !DataUtil.isNullOrEmpty(lsSerial.get(0))) {
                strQuery.append("     AND a.serial " + DbUtil.createInQuery("serial", lsSerial));
            }
        } else {
            strQuery.append("         AND to_number(a.serial) " + DbUtil.createInQuery("serial", lsSerial));
        }
        if (isSearchNew) {
            strQuery.append("         AND a.stock_trans_id = pc.stock_trans_id(+) ");
        }
        strQuery.append("         ORDER BY b.code, b.name, a.state_id ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("product_offer_type_id", prodductOfferTypeId);

        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodductOfferTypeId) && !DataUtil.isNullOrEmpty(serialGpon)) {
            query.setParameter("serialGpon", serialGpon.trim());
        }
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(prodductOfferTypeId) || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(prodductOfferTypeId)) {
            if (lsSerial != null && !DataUtil.isNullOrEmpty(lsSerial.get(0))) {
                DbUtil.setParamInQuery(query, "serial", lsSerial);
            }
        } else {
            DbUtil.setParamInQuery(query, "serial", lsSerial);
        }


        List queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            List<OptionSetValue> lsOptionState = optionSetValueRepo.getLsOptionSetValueByCode(Const.OPTION_SET.GOODS_STATE);
            Map<String, String> mapState = lsOptionState.stream().collect(Collectors.toMap(OptionSetValue::getValue, OptionSetValue::getName));
            for (Object o : queryResult) {
                Object[] obj = (Object[]) o;
                LookupSerialByFileDTO dto = new LookupSerialByFileDTO();
                dto.setSerial(DataUtil.safeToString(obj[0]));//so serial
                dto.setSerialGpon(DataUtil.safeToString(obj[1]));//serial GPON
                dto.setProdOfferCode(DataUtil.safeToString(obj[2]));//ma mat hang
                dto.setProdOfferName(DataUtil.safeToString(obj[3]));//ten mat hang
                dto.setProdOfferId(DataUtil.safeToLong(obj[4]));
                dto.setStateId(DataUtil.safeToLong(obj[5]));//tinh trang hang
                dto.setStateName(mapState.get(DataUtil.safeToString(dto.getStateId())));
                dto.setOwnerType(DataUtil.safeToLong(obj[6]));//loai kho
                dto.setOwnerTypeName(IMServiceUtil.getOwnerTypeName(DataUtil.safeToString(dto.getOwnerType())));
                dto.setOwnerId(DataUtil.safeToLong(obj[7]));
                dto.setOwnerCode(DataUtil.safeToString(obj[8]));//ma kho
                dto.setOwnerName(DataUtil.safeToString(obj[9]));//ten kho
                dto.setStatus(DataUtil.safeToLong(obj[10]));//trang thai
                dto.setStrStatus(IMServiceUtil.getProdStatusName(dto.getStatus()));
                dto.setConnectionStatus(DataUtil.safeToLong(obj[11]));//trang thai dau noi
                dto.setConnectionStatusName(IMServiceUtil.getConnectionStatus(DataUtil.safeToString(dto.getConnectionStatus())));
                dto.setConnectionType(DataUtil.safeToLong(obj[12]));//
                dto.setConnectionDateStr(DataUtil.safeToString(obj[13]));//ngay dau noi
                if (isSearchNew) {
                    dto.setContractCode(DataUtil.safeToString(obj[14]));//ngay ky HD
                    dto.setContractDateStr(DataUtil.safeToString(obj[15]));//ngay ky HD
                    dto.setPoCode(DataUtil.safeToString(obj[16]));//ma PO
                    dto.setPoDateStr(DataUtil.safeToString(obj[17]));//Ngay ky PO
                    dto.setRequestImportDateStr(DataUtil.safeToString(obj[18]));//ngay yeu cau nhap kho
                    dto.setImportStockDateStr(DataUtil.safeToString(obj[19]));//ngay hang ve
                }
                result.add(dto);
            }
        }
        return result;
    }


    @Override
    public List<ViewSerialHistoryDTO> listLookUpSerialHistory(String serial, Long prodOfferId, Date fromDate, Date toDate, boolean isSearchNew) throws Exception {
        List<OptionSetValueDTO> lsOption = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
        Map<String, String> mapStatus = Maps.newHashMap();
        if (lsOption != null) {
            lsOption.forEach(obj -> mapStatus.put(obj.getValue(), obj.getName()));
        }
        int serialLength = serial.trim().length();
        boolean hasTurningCode = serialLength > 3;
        String strPrefixSerial = "";
        //chi turning voi cac serial co do dai > 3
        if (serialLength > 6) {
            strPrefixSerial = serial.substring(0, serialLength - 6);
        } else if (serialLength > 3) {
            strPrefixSerial = serial.substring(0, serialLength - 3);
        }
        String sFromDate = DateUtil.dateyyyyMMdd(fromDate) + " 00:00:00";
        String sToDate = DateUtil.dateyyyyMMdd(toDate) + " 23:59:59";

        String sql = "SELECT  a.stock_trans_id AS stockTransId, " +
                "  decode (c.status,3,1,6,2) stock_trans_type," +
                "  CASE WHEN a.from_owner_type = 1 THEN (SELECT shop_code || ' - ' || NAME FROM bccs_im.shop WHERE shop_id = a.from_owner_id) WHEN a.from_owner_type = 2 THEN (SELECT staff_code || ' - ' || NAME FROM bccs_im.staff WHERE staff_id = a.from_owner_id) ELSE NULL END AS exportStore, " +
                "  CASE WHEN a.to_owner_type = 1 THEN (SELECT shop_code || ' - ' || NAME FROM bccs_im.shop WHERE shop_id = a.to_owner_id) WHEN a.to_owner_type = 2 THEN (SELECT staff_code || ' - ' || NAME FROM bccs_im.staff WHERE staff_id = a.to_owner_id) ELSE NULL END AS importStore, " +
                "  a.status,  " +
                "  (SELECT staff.staff_code ||' - ' || staff.name FROM staff WHERE staff_id = c.action_staff_id) AS userSerial, " +
                "  c.create_datetime AS createDate ";
        if (isSearchNew) {
            sql += " FROM    bccs_im.stock_trans a, bccs_im.stock_trans_serial b , bccs_im.stock_trans_action c ";
        } else {
            sql += " FROM    bccs_im.his_stock_trans a, bccs_im.his_stock_trans_serial b, bccs_im.his_stock_trans_action c ";
        }
        sql += "WHERE   a.stock_trans_id = b.stock_trans_id " +
                "  AND a.CREATE_DATETIME >= to_date(#fromDate, 'yyyy-MM-dd HH24:mi:ss') " +
                "  AND a.CREATE_DATETIME < to_date(#toDate, 'yyyy-MM-dd HH24:mi:ss') " +
                "  AND b.CREATE_DATETIME >= to_date(#fromDate, 'yyyy-MM-dd HH24:mi:ss')  " +
                "  AND b.CREATE_DATETIME < to_date(#toDate, 'yyyy-MM-dd HH24:mi:ss') " +
                "  AND a.stock_trans_id = c.stock_trans_id ";
        //xu ly turning code serial
        if (hasTurningCode) {
            sql += "  AND b.from_serial like #prefixSerial AND b.to_serial like #prefixSerial ";
        } else {
            sql += "  AND b.CREATE_DATETIME >= #fromDate  AND b.CREATE_DATETIME < #toDate ";
        }
        sql += "  AND a.pay_status IS NULL " +
                "  AND b.prod_offer_id= #prodOfferId " +
                "  AND b.from_serial <= #serial " +
                "  AND b.to_serial >= #serial  " +
                "  AND length(b.from_serial) = length(#serial) " +
                "  and c.status in (3,6)  ORDER BY a.CREATE_DATETIME DESC";
        Query query = em.createNativeQuery(sql);
        query.setHint(QueryHints.JDBC_TIMEOUT, 60);
        if (hasTurningCode) {
            query.setParameter("fromDate", sFromDate);
            query.setParameter("toDate", sToDate);
        } else {
            query.setParameter("fromDate", new java.sql.Date(fromDate.getTime()));
            query.setParameter("toDate", new java.sql.Date(toDate.getTime()));
        }
        if (hasTurningCode) {
            query.setParameter("prefixSerial", strPrefixSerial + "%");
        }
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);

        List<ViewSerialHistoryDTO> lsResult = Lists.newArrayList();
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] obj : queryResult) {
                int index = 0;
                ViewSerialHistoryDTO dto = new ViewSerialHistoryDTO();
                dto.setStockTransId(DataUtil.safeToLong(obj[index++]));
                dto.setStockTransType(DataUtil.safeToString(obj[index++]));
                dto.setStockTransTypeName(IMServiceUtil.getStockTransTypeName(dto.getStockTransType()));
                dto.setExportStore(DataUtil.safeToString(obj[index++]));
                dto.setImportStore(DataUtil.safeToString(obj[index++]));
                dto.setStatus(DataUtil.safeToString(obj[index++]));
                dto.setStatusName(mapStatus.get(dto.getStatus()));
                dto.setUserSerial(DataUtil.safeToString(obj[index++]));
                Object dat = obj[index];
                dto.setCreateDate(dat != null ? (Date) dat : null);
                lsResult.add(dto);
            }
        }
        return lsResult;
    }

    @Override
    public List<ChangeDamagedProductDTO> getListSerialProduct(String serial, Long ownerType, Long ownerId) throws Exception {
        List<ChangeDamagedProductDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        builder.append("   SELECT   prod_offer_id,");
        builder.append("            prod_offer_code,");
        builder.append("            prod_offer_name,");
        builder.append("            prod_offer_type_id");
        builder.append("     FROM   bccs_im.v_serial_handset");
        builder.append("    WHERE   from_serial >= #serial AND to_serial <= #serial");
        builder.append("    AND   length(from_serial) = length(#serial) ");
        builder.append("    AND owner_id = #ownerId AND owner_type = #ownerType AND status = #status AND state_id = #stateId");
        if (DataUtil.isNumber(serial)) {
            builder.append("   UNION");
            builder.append("   SELECT   prod_offer_id,");
            builder.append("            prod_offer_code,");
            builder.append("            prod_offer_name,");
            builder.append("            prod_offer_type_id");
            builder.append("     FROM   bccs_im.v_serial_full");
            builder.append("    WHERE   TO_NUMBER (from_serial) >= #serial AND TO_NUMBER (to_serial) <= #serial");
            builder.append("     AND owner_id = #ownerId AND owner_type = #ownerType AND status = #status AND state_id = #stateId");
        }
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("serial", serial);
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        query.setParameter("status", Const.STATUS.ACTIVE);
        query.setParameter("stateId", Const.GOODS_STATE.NEW);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
                changeDamagedProductDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                changeDamagedProductDTO.setProductCode(DataUtil.safeToString(ob[index++]));
                changeDamagedProductDTO.setProductName(DataUtil.safeToString(ob[index++]));
                changeDamagedProductDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index]));
                result.add(changeDamagedProductDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockTransSerialDTO> getListBySerial(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, String tableName) throws Exception {
        List<StockTransSerialDTO> result = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(tableName) || DataUtil.isNullOrEmpty(serial) || DataUtil.isNullObject(prodOfferId)) {
            return result;
        }
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT   serial as from_serial, serial as to_serial, status, owner_id, owner_type, state_id");
        builder.append("    FROM   ");
        builder.append(tableName);
        builder.append("   WHERE       prod_offer_id = #prodOfferId");
        if (!DataUtil.isNullObject(ownerType)) {
            builder.append("           AND owner_type = #ownerType");
        }
        if (!DataUtil.isNullObject(ownerId)) {
            builder.append("           AND owner_id = #ownerId");
        }
        if (!DataUtil.isNullObject(status)) {
            builder.append("           AND status = #status");
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
        if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            builder.append("        AND serial = #serial");
        } else {
            builder.append("        AND TO_NUMBER (serial) = TO_NUMBER (#serial)");
        }
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("serial", serial);
        if (!DataUtil.isNullObject(ownerType)) {
            query.setParameter("ownerType", ownerType);
        }
        if (!DataUtil.isNullObject(ownerId)) {
            query.setParameter("ownerId", ownerId);
        }
        if (!DataUtil.isNullObject(status)) {
            query.setParameter("status", status);
        }
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
                stockTransSerialDTO.setFromSerial(DataUtil.safeToString(ob[index++]));
                stockTransSerialDTO.setToSerial(DataUtil.safeToString(ob[index++]));
                stockTransSerialDTO.setStatus(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setStateId(DataUtil.safeToLong(ob[index]));
                result.add(stockTransSerialDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockTransSerialDTO> getListStockCardSaledBySerial(Long prodOfferId, String serial) throws Exception {
        List<StockTransSerialDTO> result = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT   serial from_serial, serial to_serial, status, owner_id, owner_type, state_id");
        builder.append("    FROM   bccs_im.his_stock_card ");
        builder.append("   WHERE       stock_model_id = #prodOfferId");
//        builder.append("           AND state_id = :stateId");
        builder.append("           AND TO_NUMBER (serial) = TO_NUMBER (#serial)");
        Query query = emIm1.createNativeQuery(builder.toString());
        query.setParameter("prodOfferId", prodOfferId);
//        query.setParameter("stateId", Const.GOODS_STATE.NEW);
        query.setParameter("serial", serial);
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
                stockTransSerialDTO.setFromSerial(DataUtil.safeToString(ob[index++]));
                stockTransSerialDTO.setToSerial(DataUtil.safeToString(ob[index++]));
                stockTransSerialDTO.setStatus(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                stockTransSerialDTO.setStateId(DataUtil.safeToLong(ob[index]));
                result.add(stockTransSerialDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockTransSerial> getStockTransSerialByStockTransId(Long stockTransId, Long prodOfferId, Long toOwnerType) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT   sts.* ");
        builder.append("    FROM   stock_trans_serial sts, stock_trans st ");
        builder.append("   WHERE       st.stock_trans_id = sts.stock_trans_id");
        builder.append("           AND st.stock_trans_id = #stockTransId");
        builder.append("           AND sts.prod_offer_id = #prodOfferId");
        if (!DataUtil.isNullOrZero(toOwnerType)) {
            builder.append("           AND st.to_owner_type = #toOwnerType");
        }
        Query query = em.createNativeQuery(builder.toString(), StockTransSerial.class);
        query.setParameter("stockTransId", stockTransId);
        query.setParameter("prodOfferId", prodOfferId);
        if (!DataUtil.isNullOrZero(toOwnerType)) {
            query.setParameter("toOwnerType", toOwnerType);
        }
        List<StockTransSerial> result = query.getResultList();
        return result;
    }

    public List<StockTransSerial> getSerialByStockTransId(Long stockTransId) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("  SELECT   sts.* ");
        builder.append("    FROM   stock_trans_serial sts ");
        builder.append("   WHERE       sts.stock_trans_id = #stockTransId");

        Query query = em.createNativeQuery(builder.toString(), StockTransSerial.class);
        query.setParameter("stockTransId", stockTransId);
        List<StockTransSerial> result = query.getResultList();
        return result;
    }

    @Override
    public List<SmartPhoneDTO> getListIsdnMbccsByRangeIsdnOwnerId(Long prodOfferTypeId, String fromIsdn, String toIsdn,
                                                                  Long ownerId, Long ownerType, Long status, boolean isCTV, StaffDTO staffDTO) throws Exception {
        List<SmartPhoneDTO> result = Lists.newArrayList();
        List listParams = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   pdt.product_offer_type_id,");
        sql.append("         pdt.name productoffertypename,");
        sql.append("         a.isdn isdn,");
        sql.append("         a.owner_id ownerid,");
        sql.append("         a.owner_type ownertype,");
        sql.append("         (CASE");
        sql.append("              WHEN a.owner_type = 2");
        sql.append("              THEN");
        sql.append("                  (SELECT   staff_code");
        sql.append("                     FROM   bccs_im.staff");
        sql.append("                    WHERE   staff_id = a.owner_id)");
        sql.append("              ELSE");
        sql.append("                  (SELECT   shop_code");
        sql.append("                     FROM   bccs_im.shop");
        sql.append("                    WHERE   shop_id = a.owner_id)");
        sql.append("          END)");
        sql.append("             ownercode,");
        sql.append("         (CASE");
        sql.append("              WHEN a.owner_type = 2");
        sql.append("              THEN");
        sql.append("                  (SELECT   name");
        sql.append("                     FROM   bccs_im.staff");
        sql.append("                    WHERE   staff_id = a.owner_id)");
        sql.append("              ELSE");
        sql.append("                  (SELECT   name");
        sql.append("                     FROM   bccs_im.shop");
        sql.append("                    WHERE   shop_id = a.owner_id)");
        sql.append("          END)");
        sql.append("             ownername,");
        sql.append("         a.status");
        sql.append("  FROM   bccs_im.stock_number a,");
        sql.append("         bccs_im.product_offering p,");
        sql.append("         bccs_im.product_offer_type pdt");
        sql.append(" WHERE       1 = 1");
        sql.append("         AND a.prod_offer_id = p.prod_offer_id");
        sql.append("         AND p.product_offer_type_id = pdt.product_offer_type_id");
        sql.append("         AND pdt.product_offer_type_id = ?");
        sql.append("         AND to_number(a.isdn) >= to_number(?)");
        listParams.add(prodOfferTypeId);
        listParams.add(fromIsdn);
        sql.append(" 		   AND to_number(a.isdn) <= to_number(?) ");
        listParams.add(toIsdn);
        if (ownerId != null && ownerType != null) {
            sql.append(" 		   AND a.owner_id = ? ");
            listParams.add(ownerId);
            sql.append(" 		   AND a.owner_type = ? ");
            listParams.add(ownerType);
        } else {
            //Neu la CTV thi lay tat ca cac kho duoc cau hinh trong App_params voi type = 'CTV_STOCK_ISDN_MBCCS'
            if (isCTV) {
                sql.append(" 		   AND a.owner_id IN (SELECT value FROM   option_set_value WHERE   status = 1 AND option_set_id = (SELECT   id FROM   option_set WHERE   status = 1 AND code = 'CTV_STOCK_ISDN_MBCCS')) ");
                sql.append(" 		   AND a.owner_type = 1 ");
            } else {//Neu la NV thi lay tat ca cac kho duoc dau noi cua NV
                sql.append(" 		   AND ((a.owner_id = ? and owner_type = 2) ");
                listParams.add(staffDTO.getStaffId());
                sql.append("                          OR (a.owner_id = ? and owner_type = 1) ");
                listParams.add(staffDTO.getShopId());
                sql.append(" 		          OR (a.owner_id IN (select shop_id from bccs_im.shop where status = 1 and channel_type_id = 8) and owner_type = 1)) ");
            }
        }
        if (status != null) {
            sql.append(" 		   AND a.status = ? ");
            listParams.add(status);
        }
        sql.append(" 		   AND ROWNUM <= 20 ");
        sql.append(" ORDER BY   a.isdn ");
        Query query = em.createNativeQuery(sql.toString());
        int i = 0;
        for (Object object : listParams) {
            query.setParameter(++i, object);
        }
        List<Object[]> queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            for (Object[] ob : queryResult) {
                int index = 0;
                SmartPhoneDTO smartPhoneDTO = new SmartPhoneDTO();
                smartPhoneDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                smartPhoneDTO.setProductOfferTypeName(DataUtil.safeToString(ob[index++]));
                smartPhoneDTO.setIsdn(DataUtil.safeToLong(ob[index++]));
                smartPhoneDTO.setOwnerId(DataUtil.safeToLong(ob[index++]));
                smartPhoneDTO.setOwnerType(DataUtil.safeToLong(ob[index++]));
                smartPhoneDTO.setOwnerCode(DataUtil.safeToString(ob[index++]));
                smartPhoneDTO.setOwnerName(DataUtil.safeToString(ob[index++]));
                smartPhoneDTO.setStatus(DataUtil.safeToLong(ob[index]));
                result.add(smartPhoneDTO);
            }
        }
        return result;
    }

    @Override
    public List<StockTransSerialDTO> getGatherSerial(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId) throws Exception {
        StringBuilder sql = new StringBuilder();
        List<StockTransSerialDTO> serialDTOs = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(tableName)) {
            return serialDTOs;
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(productOfferId);
        if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            sql.append(" SELECT serial as fromSerial, serial as toSerial, 1 as quantity, prod_offer_id, state_id ");
            sql.append(" FROM stock_handset  ");
            sql.append(" WHERE 1=1 ");
            sql.append("    AND prod_offer_id = #prod_offer_id ");
            sql.append("    AND state_id = #state_id  ");
            sql.append("    AND status = #status  ");
            if (!DataUtil.isNullOrZero(ownerType)) {
                sql.append("    AND owner_type = #ownerType  ");
            }
            if (!DataUtil.isNullOrZero(ownerId)) {
                sql.append("    AND owner_id = #ownerId  ");
            }
            sql.append("    ORDER BY fromSerial  ");
        } else {
            sql.append(" SELECT MIN (to_number(serial)) as  fromSerial, ");
            sql.append("        MAX (to_number(serial)) as toSerial, ");
            sql.append("        MAX(to_number(serial)) -MIN(to_number(serial)) +1 as quantity, ");
            sql.append("        prod_offer_id, state_id");
            sql.append(" FROM ( ");
            sql.append("        SELECT serial, prod_offer_id, state_id, serial - ROW_NUMBER () OVER (ORDER BY to_number(serial)) rn ");
            sql.append("        FROM ");
            sql.append(tableName);
            sql.append(" WHERE 1=1 ");
            sql.append("              AND prod_offer_id  = #prod_offer_id ");
            sql.append("              AND state_id  = #state_id ");
            sql.append("              AND status  = #status ");
            if (!DataUtil.isNullOrZero(ownerType)) {
                sql.append("    AND owner_type = #ownerType  ");
            }
            if (!DataUtil.isNullOrZero(ownerId)) {
                sql.append("    AND owner_id = #ownerId  ");
            }
            sql.append("              ) ");
            sql.append(" GROUP BY rn,prod_offer_id, state_id ");
            sql.append(" ORDER BY prod_offer_id, state_id, to_number (fromSerial) ");
        }

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("prod_offer_id", productOfferId);
        query.setParameter("state_id", stateId);
        query.setParameter("status", Const.STATUS.ACTIVE);
        if (!DataUtil.isNullOrZero(ownerType)) {
            query.setParameter("ownerType", ownerType);
        }
        if (!DataUtil.isNullOrZero(ownerId)) {
            query.setParameter("ownerId", ownerId);
        }
        List<Object[]> queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList) && queryResultList.size() > 0) {
            for (Object[] objects : queryResultList) {
                int index = 0;
                StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                serialDTO.setFromSerial(DataUtil.safeToString(objects[index++]));
                serialDTO.setToSerial(DataUtil.safeToString(objects[index++]));
                serialDTO.setQuantity(DataUtil.safeToLong(objects[index++]));
                serialDTO.setProdOfferId(DataUtil.safeToLong(objects[index++]));
                serialDTO.setStateId(DataUtil.safeToLong(objects[index++]));
                serialDTO.setIndex(DataUtil.safeToLong(index));
                serialDTOs.add(serialDTO);
            }
        }
        return serialDTOs;
    }

    @Override
    public List<StockSerialDTO> getStockSerialInfomation(Long productOfferTypeId, String tableName, String serial) throws Exception {
        StringBuilder sql = new StringBuilder();
        List<StockSerialDTO> serialDTOs = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(tableName)) {
            return serialDTOs;
        }
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(productOfferTypeId)) {
            sql.append(" SELECT b.product_offer_type_id, a.serial, a.prod_offer_id, b.code, b.name, a.owner_id, a.owner_type, a.status, c.partner_id, c.partner_name ");
            sql.append(" FROM stock_handset a, product_offering b, partner c ");
            sql.append(" WHERE 1=1 ");
            sql.append("    AND a.prod_offer_id = b.prod_offer_id ");
            sql.append("    AND a.partner_id = c.partner_id(+) ");
            sql.append("    AND a.serial = #serial  ");
            sql.append("    AND b.product_offer_type_id = #productOfferTypeId  ");
        } else {
            sql.append(" SELECT b.product_offer_type_id, a.serial, a.prod_offer_id, b.code, b.name, a.owner_id, a.owner_type, a.status ");
            sql.append(" FROM  ");
            sql.append(tableName);
            sql.append(" a, product_offering b ");
            sql.append(" WHERE 1=1 ");
            sql.append("    AND a.prod_offer_id = b.prod_offer_id ");
            sql.append("    AND to_number(a.serial) = to_number(#serial) ");
            sql.append("    AND b.product_offer_type_id = #productOfferTypeId  ");
        }

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("serial", serial);
        query.setParameter("productOfferTypeId", productOfferTypeId);
        List<Object[]> queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList) && queryResultList.size() > 0) {
            for (Object[] objects : queryResultList) {
                int index = 0;
                StockSerialDTO serialDTO = new StockSerialDTO();
                serialDTO.setProductOfferTypeId(DataUtil.safeToLong(objects[index++]));
                serialDTO.setSerial(DataUtil.safeToString(objects[index++]));
                serialDTO.setProdOfferId(DataUtil.safeToLong(objects[index++]));
                serialDTO.setProductCode(DataUtil.safeToString(objects[index++]));
                serialDTO.setProductName(DataUtil.safeToString(objects[index++]));
                serialDTO.setOwnerId(DataUtil.safeToLong(index++));
                serialDTO.setOwnerType(DataUtil.safeToLong(index++));
                serialDTO.setStatus(DataUtil.safeToLong(index++));
                if (Const.STOCK_TYPE.STOCK_HANDSET.equals(productOfferTypeId)) {
                    serialDTO.setPartnerId(DataUtil.safeToLong(index++));
                    serialDTO.setPartnerName(DataUtil.safeToString(index));
                }
                serialDTOs.add(serialDTO);
            }
        }
        return serialDTOs;
    }

    @Override
    public List<String> getListSerialExist(String fromSerial, String toSerial, Long prodOfferId, Long ownerId, Long ownerType, String tableName, Long status) throws Exception {
        StringBuilder sqlBuider = new StringBuilder("");
        sqlBuider.append("select serial from " + tableName + " where prod_offer_id= #prodOfferId ");
        if (ownerId != null) {
            sqlBuider.append(" and owner_id= #ownerId ");
        }
        if (ownerType != null) {
            sqlBuider.append(" and owner_type=#ownerType ");
        }
        if (status != null) {
            sqlBuider.append(" and status = #status ");
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
        if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
            sqlBuider.append(" and serial >= #fromSerial and serial <= #toSerial ");
        } else {
            sqlBuider.append(" and to_number(serial) >= #fromSerial and to_number(serial) <= #toSerial ");
        }

        Query query = em.createNativeQuery(sqlBuider.toString());
        query.setParameter("fromSerial", fromSerial);
        query.setParameter("toSerial", toSerial);
        query.setParameter("prodOfferId", prodOfferId);
        if (ownerId != null) {
            query.setParameter("ownerId", ownerId);
        }
        if (ownerType != null) {
            query.setParameter("ownerType", ownerType);
        }
        if (status != null) {
            query.setParameter("status", status);
        }
        List<String> bi = (List<String>) query.getResultList();
        return bi;
    }

    @Override
    public int updateStockBalance(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String strFromSerial,
                                  String strToSerial, Long stockTransId, Long newStatus, Long oldStatus) throws Exception {
        ProductOfferingDTO productOffering = productOfferingService.findOne(prodOfferId);
        ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOffering.getProductOfferTypeId());
        String tableName = productOfferTypeDTO.getTableName();
        StringBuilder strUpdate = new StringBuilder();
        strUpdate.append(" update ").append(tableName);
        strUpdate.append(" set status = #status, state_id = #stateId, owner_id = #ownerId, owner_type = #ownerType, update_datetime = sysdate, stock_trans_id = #stockTransId ");
        strUpdate.append(" where status = #oldStatus and prod_offer_id = #prodOfferId ");
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOffering.getProductOfferTypeId())) {
            strUpdate.append("       and serial = #fromSerial ");
        } else {
            strUpdate.append("       and to_number(serial) >= to_number(#fromSerial) ");
            strUpdate.append("       and to_number(serial) <= to_number(#toSerial) ");
        }
        Query queryUpdate = em.createNativeQuery(strUpdate.toString());
        queryUpdate.setParameter("status", newStatus);
        queryUpdate.setParameter("stateId", stateId);
        queryUpdate.setParameter("ownerType", ownerType);
        queryUpdate.setParameter("ownerId", ownerId);
        queryUpdate.setParameter("stockTransId", stockTransId);
        queryUpdate.setParameter("oldStatus", oldStatus);
        queryUpdate.setParameter("prodOfferId", prodOfferId);
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOffering.getProductOfferTypeId())) {
            queryUpdate.setParameter("fromSerial", strFromSerial);
        } else {
            queryUpdate.setParameter("fromSerial", strFromSerial);
            queryUpdate.setParameter("toSerial", strToSerial);
        }
        return queryUpdate.executeUpdate();
    }

    private String formatSerial(String serial, boolean isCard) {
        if (DataUtil.isNullObject(serial)) {
            return serial;
        }
        String newSerial = serial.trim();
        if (isCard) {
            while (newSerial.length() < 11) {
                newSerial = "0" + newSerial;
            }
        }
        return newSerial;
    }

    public List<StockDeviceDTO> getLstStockDevice(Long stockTransActionId) throws LogicException, Exception {
        List<StockDeviceDTO> lstStockDevice = Lists.newArrayList();
        StringBuilder sqlBuilder = new StringBuilder(" ");
        sqlBuilder.append(" SELECT   b.action_code, ");
        sqlBuilder.append("            c.from_serial, ");
        sqlBuilder.append("            (SELECT   code ");
        sqlBuilder.append("               FROM   product_offering ");
        sqlBuilder.append("              WHERE   prod_offer_id = c.prod_offer_id) ");
        sqlBuilder.append("                AS prodoffercode, ");
        sqlBuilder.append("            (SELECT   code ");
        sqlBuilder.append("               FROM   product_offering ");
        sqlBuilder.append("              WHERE   prod_offer_id = d.new_prod_offer_id) ");
        sqlBuilder.append("                AS newprodoffercode, ");
        sqlBuilder.append("            (SELECT   name ");
        sqlBuilder.append("               FROM   product_offering ");
        sqlBuilder.append("              WHERE   prod_offer_id = d.new_prod_offer_id) ");
        sqlBuilder.append("                AS newprodoffername, ");
        sqlBuilder.append("            1 AS quantity, ");
        sqlBuilder.append("            (SELECT   sv.name ");
        sqlBuilder.append("               FROM   option_set s, option_set_value sv ");
        sqlBuilder.append("              WHERE       s.id = sv.option_set_id ");
        sqlBuilder.append("                      AND s.status = 1 ");
        sqlBuilder.append("                      AND sv.status = 1 ");
        sqlBuilder.append("                      AND s.code = 'GOODS_STATE' ");
        sqlBuilder.append("                      AND TRIM (sv.VALUE) = TO_CHAR (d.new_state_id)) ");
        sqlBuilder.append("                AS statename, ");
        sqlBuilder.append("            (SELECT   price_cost ");
        sqlBuilder.append("               FROM   product_offering ");
        sqlBuilder.append("              WHERE   prod_offer_id = d.new_prod_offer_id) ");
        sqlBuilder.append("                AS price, ");
        sqlBuilder.append("            (SELECT   name ");
        sqlBuilder.append("               FROM   shop ");
        sqlBuilder.append("              WHERE   shop_id = a.from_owner_id) ");
        sqlBuilder.append("                AS requestshopname, ");
        sqlBuilder.append("            a.from_owner_id AS requestshopid ");
        sqlBuilder.append("     FROM   stock_trans a, ");
        sqlBuilder.append("            stock_trans_action b, ");
        sqlBuilder.append("            stock_trans_serial c, ");
        sqlBuilder.append("            stock_device_transfer d, ");
        sqlBuilder.append("            stock_request e ");
        sqlBuilder.append("    WHERE       1 = 1 ");
        sqlBuilder.append("            AND a.stock_trans_id = b.stock_trans_id ");
        sqlBuilder.append("            AND b.stock_trans_action_id = #stocktransactionid ");
        sqlBuilder.append("            AND b.status = 2 ");
        sqlBuilder.append("            AND c.stock_trans_id = a.stock_trans_id ");
        sqlBuilder.append("            AND c.prod_offer_id = d.prod_offer_id ");
        sqlBuilder.append("            AND d.stock_request_id = e.stock_request_id ");
        sqlBuilder.append("            AND e.stock_trans_id = a.stock_trans_id ");
        sqlBuilder.append("            AND a.create_datetime >= TRUNC (SYSDATE) - 30 ");
        sqlBuilder.append("            AND a.create_datetime < TRUNC (SYSDATE) + 1 ");
        sqlBuilder.append(" ORDER BY   c.prod_offer_id,c.from_serial DESC ");

        Query query = em.createNativeQuery(sqlBuilder.toString());
        query.setParameter("stocktransactionid", stockTransActionId);
        List<Object[]> queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList) && queryResultList.size() > 0) {
            for (Object[] objects : queryResultList) {
                int index = 0;
                StockDeviceDTO stockDeviceDTO = new StockDeviceDTO();
                stockDeviceDTO.setActionCode(DataUtil.safeToString(objects[index++]));
                stockDeviceDTO.setSerial(DataUtil.safeToString(objects[index++]));
                stockDeviceDTO.setProductOfferCode(DataUtil.safeToString(objects[index++]));
                stockDeviceDTO.setNewProductOfferCode(DataUtil.safeToString(objects[index++]));
                stockDeviceDTO.setNewProductOfferName(DataUtil.safeToString(objects[index++]));
                stockDeviceDTO.setQuantity(DataUtil.safeToLong(objects[index++]));
                stockDeviceDTO.setStatusName(DataUtil.safeToString(objects[index++]));
                stockDeviceDTO.setPrice(DataUtil.safeToLong(objects[index++]));
                stockDeviceDTO.setRequestShopName(DataUtil.safeToString(objects[index++]));
                stockDeviceDTO.setRequestShopId(DataUtil.safeToLong(objects[index]));
                lstStockDevice.add(stockDeviceDTO);
            }
        }
        return lstStockDevice;
    }

    public List<StockDeviceTransferDTO> getLstDeviceTransfer(Long prodOfferId, Long stateId, Long stockRequestId) throws LogicException, Exception {
        List<StockDeviceTransferDTO> lstDeviceTransfer = Lists.newArrayList();
        StringBuilder sqlBuilder = new StringBuilder(" ");
        sqlBuilder.append(" SELECT prod_offer_id, state_id, new_prod_offer_id, new_state_id, stock_request_id");
        sqlBuilder.append(" FROM stock_device_transfer");
        sqlBuilder.append(" WHERE prod_offer_id = #prodOfferId");
        sqlBuilder.append(" AND state_id = #stateId");
        sqlBuilder.append(" AND stock_request_id = #stockRequestId");
        Query query = em.createNativeQuery(sqlBuilder.toString());
        query.setParameter("prodOfferId", prodOfferId);
        query.setParameter("stateId", stateId);
        query.setParameter("stockRequestId", stockRequestId);
        List<Object[]> queryResultList = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResultList) && queryResultList.size() > 0) {
            for (Object[] objects : queryResultList) {
                int index = 0;
                StockDeviceTransferDTO stockDeviceTransferDTO = new StockDeviceTransferDTO();
                stockDeviceTransferDTO.setProdOfferId(DataUtil.safeToLong(objects[index++]));
                stockDeviceTransferDTO.setStateId(DataUtil.safeToLong(objects[index++]));
                stockDeviceTransferDTO.setNewProdOfferId(DataUtil.safeToLong(objects[index++]));
                stockDeviceTransferDTO.setNewStateId(DataUtil.safeToLong(objects[index++]));
                stockDeviceTransferDTO.setStockRequestId(DataUtil.safeToLong(objects[index]));
                lstDeviceTransfer.add(stockDeviceTransferDTO);
            }
        }
        return lstDeviceTransfer;
    }
}
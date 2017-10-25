package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockSimDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockSim.COLUMNS;
import com.viettel.bccs.inventory.model.QStockSim;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockSimRepoImpl implements StockSimRepoCustom {

    public static final Logger logger = Logger.getLogger(StockSimRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager emSale;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockSim stockSim = QStockSim.stockSim;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CONTRACTCODE:
                        expression = DbUtil.createStringExpression(stockSim.contractCode, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockSim.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockSim.createUser, filter);
                        break;
                    case DEPOSITPRICE:
                        expression = DbUtil.createStringExpression(stockSim.depositPrice, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockSim.id, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockSim.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockSim.ownerType, filter);
                        break;
                    case POCODE:
                        expression = DbUtil.createStringExpression(stockSim.poCode, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockSim.prodOfferId, filter);
                        break;
                    case SALEDATE:
                        expression = DbUtil.createDateExpression(stockSim.saleDate, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(stockSim.serial, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockSim.stateId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockSim.status, filter);
                        break;
                    case STOCKSIMINFOID:
                        expression = DbUtil.createLongExpression(stockSim.stockSimInfoId, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(stockSim.telecomServiceId, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(stockSim.updateDatetime, filter);
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

    public String findSerialSimBySub(String msisdn) throws Exception {

        StringBuilder builder = new StringBuilder("");

        builder.append("SELECT serial ");
        builder.append("  FROM bccs_sale.subscriber ");
        builder.append(" WHERE isdn=#isdn AND status = 2 ");

        Query query = emSale.createNativeQuery(builder.toString());
        query.setParameter("isdn", msisdn);

        List lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return DataUtil.safeToString(lst.get(0));
        }
        return "";
    }


    public StockSimDTO findStockSim(String serial) throws Exception {

        StringBuilder builder = new StringBuilder("");

        builder.append("SELECT PROD_OFFER_ID, IMSI, ICCID, PIN, PUK, PIN2, PUK2,  ");
        builder.append("       SIM_TYPE, STATUS, SERIAL, STATE_ID, KIND, A3A8 ");
        builder.append("  FROM bccs_im.v_stock_sim ");
        builder.append(" WHERE to_number(serial) = #serial ");

        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("serial", serial);

        List<Object[]> list = query.getResultList();

        if (list != null && !list.isEmpty()) {
            Object[] item = list.get(0);
            StockSimDTO stockSimDTO = new StockSimDTO();

            int i = 0;
            stockSimDTO.setProdOfferId(DataUtil.safeToLong(item[i++]));
            stockSimDTO.setImsi(DataUtil.safeToString(item[i++]));
            stockSimDTO.setIccid(DataUtil.safeToString(item[i++]));
            stockSimDTO.setPin(DataUtil.safeToString(item[i++]));
            stockSimDTO.setPuk(DataUtil.safeToString(item[i++]));
            stockSimDTO.setPin2(DataUtil.safeToString(item[i++]));
            stockSimDTO.setPuk2(DataUtil.safeToString(item[i++]));
            stockSimDTO.setSimType(DataUtil.safeToString(item[i++]));
            stockSimDTO.setStatus(DataUtil.safeToLong(item[i++]));
            stockSimDTO.setSerial(DataUtil.safeToString(item[i++]));
            stockSimDTO.setStateId(DataUtil.safeToLong(item[i++]));
            stockSimDTO.setKind(DataUtil.safeToString(item[i++]));
            stockSimDTO.setA3a8(DataUtil.safeToString(item[i]));

            return stockSimDTO;
        }

        return null;
    }

    public boolean isCaSim(String serial) {

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT   a.* ");
        sb.append("  FROM   bccs_im.stock_sim a, bccs_im.product_offering b");
        sb.append(" WHERE   a.prod_offer_id  = b.prod_offer_id ");
        sb.append("   AND   TO_NUMBER (a.serial) = #serial ");
        sb.append("   AND   upper(b.code) LIKE 'SIM_CA%' AND b.status = 1 ");

        if (DataUtil.isNullOrEmpty(serial)) {
            return false;
        }

        if (!DataUtil.isNumber(serial)) {
            return false;
        }

        try {
            Query query = em.createNativeQuery(sb.toString());
            query.setParameter("serial", serial);
            List list = query.getResultList();
            if (list != null && list.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }


    //check sim ton tai cac dieu kien hop le cho KIT
    @Override
    public boolean checkSimElegibleExists(String fromSerial, String toSerial) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("selecT 1 from stock_sim where to_number(serial) >= to_number(#fromSerial)  " +
                "and to_number(serial) <= to_number(#toSerial) " +
                "and (IMSI is  NULL " +
                "or SERIAL is  NULL " +
                "or PIN is  null " +
                "or PUK is  null " +
                "or PIN2 is  null " +
                "or PUK2 is  null " +
                "or ADM1 is  null " +
                "or EKI is  null) ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("fromSerial", fromSerial);
        query.setParameter("toSerial", toSerial);
        List<Object[]> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean checkSimElegibleExistsIm1(String fromSerial, String toSerial) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("selecT 1 from stock_sim where to_number(serial) >= to_number(#fromSerial)  " +
                "and to_number(serial) <= to_number(#toSerial) " +
                "and (IMSI is  NULL " +
                "or SERIAL is  NULL " +
                "or PIN is  null " +
                "or PUK is  null " +
                "or PIN2 is  null " +
                "or PUK2 is  null " +
                "or ADM1 is  null " +
                "or EKI is  null) ");
        Query query = emIm1.createNativeQuery(strQuery.toString());
        query.setParameter("fromSerial", fromSerial);
        query.setParameter("toSerial", toSerial);
        List<Object[]> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

}
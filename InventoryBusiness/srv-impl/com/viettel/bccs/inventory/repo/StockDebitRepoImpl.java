package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockDebitDTO;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.bccs.inventory.model.StockDebit;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.QStockDebit;
import com.viettel.bccs.inventory.model.StockDebit.COLUMNS;
import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class StockDebitRepoImpl implements StockDebitRepoCustom {

    public static final Logger logger = Logger.getLogger(StockDebitRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockDebit stockDebit = QStockDebit.stockDebit;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockDebit.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockDebit.createUser, filter);
                        break;
                    case DEBITDAYTYPE:
                        expression = DbUtil.createStringExpression(stockDebit.debitDayType, filter);
                        break;
                    case DEBITVALUE:
                        expression = DbUtil.createLongExpression(stockDebit.debitValue, filter);
                        break;
                    case FINANCETYPE:
                        expression = DbUtil.createStringExpression(stockDebit.financeType, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(stockDebit.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(stockDebit.lastUpdateUser, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockDebit.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createStringExpression(stockDebit.ownerType, filter);
                        break;
                    case STOCKDEBITID:
                        expression = DbUtil.createLongExpression(stockDebit.stockDebitId, filter);
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
    public StockDebit buildStockDebitFromDebitRequestDetail(DebitRequestDetail debitDetail, String staff) throws Exception {
        StockDebit stockDebit = new StockDebit();
        stockDebit.setOwnerId(debitDetail.getOwnerId());
        stockDebit.setOwnerType(debitDetail.getOwnerType());
        StringBuilder builder = new StringBuilder("select * from stock_debit a where 1=1");
        List params = Lists.newArrayList();
        if (!DataUtil.isNullObject(debitDetail.getOwnerId())) {
            builder.append(" and owner_id= #ownerId");
            params.add(debitDetail.getOwnerId());
        }
        if (!DataUtil.isNullOrEmpty(debitDetail.getOwnerType())) {
            builder.append(" and owner_type = #ownerType");
            params.add(debitDetail.getOwnerType());
        }
        if (!DataUtil.isNullOrEmpty(debitDetail.getDebitDayType())) {
            builder.append(" and debit_day_type = #dayType");
            params.add(debitDetail.getDebitDayType());
        }
        Query query = em.createNativeQuery(builder.toString(), StockDebit.class);
        if (!DataUtil.isNullObject(debitDetail.getOwnerId())) {
            query.setParameter("ownerId", debitDetail.getOwnerId());
        }
        if (!DataUtil.isNullOrEmpty(debitDetail.getOwnerType())) {
            query.setParameter("ownerType", debitDetail.getOwnerType());
        }
        if (!DataUtil.isNullOrEmpty(debitDetail.getDebitDayType())) {
            query.setParameter("dayType", debitDetail.getDebitDayType());
        }
        Date currentDate = DbUtil.getSysDate(em);
        List<StockDebit> result = query.getResultList();
        if (result != null && !result.isEmpty()) {
            stockDebit = result.get(0);
            stockDebit.setDebitValue(debitDetail.getDebitValue());
            stockDebit.setFinanceType(debitDetail.getFinanceType());
            stockDebit.setLastUpdateTime(currentDate);
            stockDebit.setLastUpdateUser(staff);
        } else {
            stockDebit.setOwnerId(debitDetail.getOwnerId());
            stockDebit.setOwnerType(debitDetail.getOwnerType());
            stockDebit.setDebitDayType(debitDetail.getDebitDayType());
            stockDebit.setCreateUser(staff);
            stockDebit.setCreateDate(currentDate);
            stockDebit.setDebitValue(debitDetail.getDebitValue());
            stockDebit.setFinanceType(debitDetail.getFinanceType());
            stockDebit.setLastUpdateTime(currentDate);
            stockDebit.setLastUpdateUser(staff);
        }
        return stockDebit;
    }

    @Override
    public void updateFinanceType(Long staffId, String financeType) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" update stock_debit ");
        strQuery.append(" set finance_type = #financeType ");
        strQuery.append(" where owner_id = #ownerId ");
        strQuery.append(" and owner_type = #ownerType ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("financeType", financeType);
        query.setParameter("ownerId", staffId);
        query.setParameter("ownerType", Const.OWNER_TYPE.STAFF);
        query.executeUpdate();
    }

    @Override
    public Long totalPriceStock(Long ownerId, String ownerType) throws Exception {
        Long totalPrice = null;
        StringBuilder builder = new StringBuilder("SELECT sum(total.currentamount) FROM (" +
                "  SELECT   (" +
                "        SELECT   shop_code FROM  shop WHERE   shop_id = sd.owner_id) staffcode, sd.debit_value debitvalue," +
                "         (SELECT   b.name FROM   option_set a, option_set_value b WHERE a.id = b.option_set_id AND a.code = 'DEBIT_DAY_TYPE' " +
                "         AND a.status = 1 AND b.status = 1 AND VALUE = sd.debit_day_type) debitdayname,'--' financename, sd.owner_type ownertype," +
                "         (SELECT   NVL (SUM (st.current_quantity * p.price), 0)FROM   stock_total st, product_offer_price p, shop sh" +
                "           WHERE st.prod_offer_id = p.prod_offer_id" +
                "                   AND st.owner_type = 1 AND st.owner_id = sh.shop_id AND st.owner_id = sd.owner_id AND st.state_id = 1" +
                "                   AND p.status = 1 AND p.price_type_id = 23 AND p.price_policy_id = sh.price_policy AND p.effect_datetime <= SYSDATE" +
                "                   AND (p.expire_datetime IS NULL OR p.expire_datetime >= TRUNC (SYSDATE))) currentamount" +
                "        FROM   stock_debit sd WHERE   sd.owner_id = #ownerId AND sd.owner_type = #ownerType) total");

        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);

        List<Object[]> objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            totalPrice = DataUtil.safeToLong(objects);
        }
        return totalPrice;
    }

    @Override
    public StockDebitDTO findLimitStock(Long ownerId, String ownerType, Date createDate) throws Exception {
        if (Const.OWNER_TYPE.SHOP.equals(ownerType)) {
            return findLimitStockUnit(ownerId, createDate);
        } else {
            return findLimitStockStaff(ownerId, createDate);
        }
    }

    private StockDebitDTO findLimitStockUnit(Long shopId, Date createDate) throws Exception {
        StringBuilder queryString = new StringBuilder("");
        queryString.append(" SELECT   (SELECT   to_char(shop_code)||'-'||to_char(name)")
                .append("            FROM   shop")
                .append("           WHERE   shop_id = sd.owner_id AND ROWNUM < 2)")
                .append("             staffcode,")
                .append("         sd.debit_value debitvalue,")
                .append("         (SELECT   b.name")
                .append("            FROM   option_set a, option_set_value b")
                .append("           WHERE       a.id = b.option_set_id")
                .append("                   AND a.code = 'DEBIT_DAY_TYPE'")
                .append("                   AND a.status = 1")
                .append("                   AND b.status = 1")
                .append("                   AND b.VALUE = sd.debit_day_type)")
                .append("             debitdayname,")
                .append("         '' financename,")
                .append("         sd.owner_type ownertype,")
                .append("         (SELECT   NVL (SUM (st.current_quantity * p.price), 0)")
                .append("            FROM   stock_total st, product_offer_price p, product_offering sm")
                .append("           WHERE       st.prod_offer_id = p.prod_offer_id")
                .append("                   AND st.prod_offer_id = sm.prod_offer_id")
                .append("                   AND p.status = 1")
                .append("                   AND p.price_type_id = 23")
                .append("                   AND p.price_policy_id =")
                .append("                          (SELECT   price_policy")
                .append("                             FROM   shop")
                .append("                            WHERE   shop_id = sd.owner_id AND ROWNUM < 2)")
                .append("                   AND st.owner_type = 1")
                .append("                   AND st.owner_id = sd.owner_id")
                .append("                   AND p.effect_datetime <= SYSDATE")
                .append("                   AND (p.expire_datetime IS NULL")
                .append("                        OR p.expire_datetime >= TRUNC (SYSDATE))")
                .append("                   AND st.state_id = 1)")
                .append("             currentamount")
                .append("  FROM   stock_debit sd")
                .append(" WHERE   sd.owner_id = #P0 AND sd.owner_type = ").append(Const.OWNER_TYPE.SHOP)
                .append("         AND debit_day_type IN")
                .append("                    (NVL (")
                .append("                         (SELECT   debit_day_type")
                .append("                            FROM   debit_day_type")
                .append("                           WHERE   start_date <= TRUNC (#P1)")
                .append("                                         AND (end_date IS NULL")
                .append("                                              OR end_date >= TRUNC (#P2))")
                .append("                                   AND status = 1 AND ROWNUM < 2),")
                .append("                         (SELECT   VALUE")
                .append("                            FROM   option_set a, option_set_value b")
                .append("                           WHERE       a.id = b.option_set_id")
                .append("                                   AND a.code = 'DEBIT_DAY_TYPE_DEFAULT'")
                .append("                                   AND a.status = 1")
                .append("                                   AND b.status = 1))) ");
        Query query = em.createNativeQuery(queryString.toString());
        query.setParameter("P0", shopId);
        query.setParameter("P1", createDate);
        query.setParameter("P2", createDate);
        List result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return mapper((Object[]) result.get(0));
//            List<StockDebitDTO> stockDebitDTOs = Lists.newArrayList();
//            for (Object object : result) {
//                stockDebitDTOs.add(mapper((Object[]) object));
//            }
//            return stockDebitDTOs;
        }
        return findDefaultLimitStockShop(shopId, createDate);
    }

    private StockDebitDTO findDefaultLimitStockShop(Long shopId, Date createDate) {
        StringBuilder queryString = new StringBuilder("");
        String optionSetValueCode;
        String isVtUnit = getIsVtUnitChannelType(Const.OWNER_TYPE.SHOP_LONG, shopId);
        if (DataUtil.safeEqual(Const.VT_UNIT.VT, isVtUnit)) {
            optionSetValueCode = Const.OPTION_SET.DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_SHOP;
        } else {
            optionSetValueCode = Const.OPTION_SET.DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_AGENT;
        }
        queryString.append("SELECT (SELECT to_char(shop_code)||'-'||to_char(name)   ")
                .append(" FROM shop  ")
                .append(" WHERE shop_id = #P0 AND ROWNUM < 2) staffCode,")
                .append(" b.value AS debitValue,")
                .append(" 'stock.trans.debitValue.default' AS debitDayName,")
                .append(" '' AS financeName,        1 AS ownerType,")
                .append(" (SELECT NVL (SUM (st.current_quantity * p.price), 0)   ")
                .append(" FROM stock_total st, product_offer_price p, product_offering sm ")
                .append(" WHERE     st.prod_offer_id = p.prod_offer_id")
                .append(" AND st.prod_offer_id = sm.prod_offer_id")
                .append(" AND p.status = 1 and st.status=1 and sm.status=1")
                .append(" AND p.price_type_id = 23")
                .append(" AND p.price_policy_id = (SELECT price_policy")
                .append(" FROM shop")
                .append(" WHERE shop_id = #P1 AND ROWNUM < 2)")
                .append(" AND st.owner_type =  ").append(Const.OWNER_TYPE.SHOP)
                .append(" AND st.owner_id = #P2 ")
                .append(" AND p.effect_datetime <= SYSDATE")
                .append(" AND (p.expire_datetime IS NULL OR p.expire_datetime >= TRUNC (SYSDATE))")
                .append(" AND st.state_id = 1)           currentAmount   ")
                .append(" FROM   option_set a, option_set_value b ")
                .append(" WHERE       a.id = b.option_set_id")
                .append(" AND a.code = #optionSetValueCode")
                .append(" AND a.status = 1      ")
                .append(" AND b.status = 1");
        Query qDef = em.createNativeQuery(queryString.toString());
        qDef.setParameter("P0", shopId);
        qDef.setParameter("P1", shopId);
        qDef.setParameter("P2", shopId);
        qDef.setParameter("optionSetValueCode", optionSetValueCode);
        List result = qDef.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return mapper((Object[]) result.get(0));
        }
        return null;
    }

    private StockDebitDTO findLimitStockStaff(Long staffId, Date createDate) throws Exception {
        StringBuilder builder = new StringBuilder("SELECT")
                .append("         (SELECT   to_char(sf.staff_code)||'-'||to_char(sf.name)")
                .append("            FROM   staff sf")
                .append("           WHERE   sf.staff_id = NVL (sd.owner_id, sf.staff_id))")
                .append("             staff_code,")
                .append("         (select dl.debit_amount from debit_level dl where dl.debit_level=sd.debit_value and dl.status=1 and dl.debit_day_type=sd.debit_day_type) debit_value,")
                .append("         (SELECT   b.name")
                .append("            FROM   option_set a, option_set_value b")
                .append("           WHERE       a.id = b.option_set_id")
                .append("                   AND a.code = 'DEBIT_DAY_TYPE'")
                .append("                   AND a.status = 1")
                .append("                   AND b.status = 1")
                .append("                   AND VALUE = sd.debit_day_type)")
                .append("             debit_day_name,")
                .append("         (SELECT   b.name")
                .append("            FROM   option_set a, option_set_value b")
                .append("           WHERE   a.id = b.option_set_id")
                .append("                   AND a.code = 'FINANCE_TYPE'")
                .append("                   AND a.status = 1")
                .append("                   AND b.status = 1")
                .append("                   AND VALUE = sd.finance_type)")
                .append("             finance_name,")
                .append("         sd.owner_type ownertype,")
                .append("         (SELECT   NVL (SUM (st.current_quantity * p.price), 0)")
                .append("            FROM   stock_total st,")
                .append("                   product_offer_price p,")
                .append("                   staff s,")
                .append("                   shop sh")
                .append("           WHERE   st.prod_offer_id = p.prod_offer_id")
                .append("                   AND st.owner_type = 2")
                .append("                   AND st.owner_id = s.staff_id")
                .append("                   AND st.owner_id = sd.owner_id")
                .append("                   AND s.shop_id = sh.shop_id")
                .append("                   AND st.state_id = 1")
                .append("                   AND p.status = 1")
                .append("                   AND p.price_type_id = 23")
                .append("                   AND p.price_policy_id = sh.price_policy")
                .append("                   AND p.effect_datetime <= SYSDATE")
                .append("                   AND (p.expire_datetime IS NULL")
                .append("                        OR p.expire_datetime >= TRUNC (SYSDATE)))")
                .append("             currentamount, sd.owner_id")
                .append("  FROM   stock_debit sd")
                .append(" WHERE   sd.owner_type = ").append(Const.OWNER_TYPE.STAFF).append(" and sd.owner_id= #p3")
                .append("         AND sd.debit_day_type =")
                .append("                (NVL (")
                .append("                     (SELECT   debit_day_type")
                .append("                        FROM   debit_day_type")
                .append("                       WHERE   start_date <= TRUNC (#p1)")
                .append("                               AND (end_date IS NULL")
                .append("                                    OR end_date >= TRUNC (#p2))")
                .append("                               AND status = 1")
                .append("                               AND ROWNUM < 2),")
                .append("                     (SELECT   VALUE")
                .append("                        FROM   option_set a, option_set_value b")
                .append("                       WHERE   a.id = b.option_set_id")
                .append("                               AND a.code = 'DEBIT_DAY_TYPE_DEFAULT'")
                .append("                               AND a.status = 1")
                .append("                               AND b.status = 1)))");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("p1", createDate);
        query.setParameter("p2", createDate);
        query.setParameter("p3", staffId);
        List result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return mapper((Object[]) result.get(0));
        }
        return findDefaultLimitStockStaff(staffId, createDate);
    }

    private StockDebitDTO findDefaultLimitStockStaff(Long staffId, Date createDate) {
        String optionSetValueCode;
        String isVtUnit = getIsVtUnitChannelType(Const.OWNER_TYPE.STAFF_LONG, staffId);
        if (DataUtil.safeEqual(Const.VT_UNIT.VT, isVtUnit)) {
            optionSetValueCode = Const.OPTION_SET.DEFAULT_DAY_TYPE_FOR_MAX_DEBIT;
        } else {
            optionSetValueCode = Const.OPTION_SET.DEFAULT_DAY_TYPE_FOR_MAX_DEBIT_COLLABORATOR;
        }
        StringBuilder builder = new StringBuilder("SELECT")
                .append("        (SELECT   to_char(sf.staff_code)||'-'||to_char(sf.name)")
                .append("           FROM   staff sf")
                .append("          WHERE   sf.staff_id = #P0)")
                .append("            staffCode,")
                .append("        debit_amount debitValue,")
                .append("        'stock.trans.debitValue.default' debitdayname,")
                .append("        '' financeName,")
                .append("        2 ownerType,")
                .append("        (SELECT   NVL (SUM (st.current_quantity * p.price), 0)")
                .append("           FROM   stock_total st,")
                .append("                  product_offer_price p,")
                .append("                  staff s,")
                .append("                  shop sh")
                .append("          WHERE   st.prod_offer_id = p.prod_offer_id")
                .append("                  AND st.owner_type = 2")
                .append("                  AND st.owner_id = s.staff_id")
                .append("                  AND st.owner_id = #P1")
                .append("                  AND s.shop_id = sh.shop_id")
                .append("                  AND st.state_id = 1")
                .append("                  AND p.status = 1")
                .append("                  AND p.price_type_id = 23")
                .append("                  AND p.price_policy_id = sh.price_policy")
                .append("                  AND p.effect_datetime <= SYSDATE")
                .append("                  AND (p.expire_datetime IS NULL")
                .append("                       OR p.expire_datetime >= TRUNC (SYSDATE)))")
                .append("            currentAmount")
                .append(" FROM   debit_level")
                .append(" WHERE   status=1 and debit_day_type =")
                .append("            (NVL (")
                .append("                 (SELECT   debit_day_type")
                .append("                    FROM   debit_day_type")
                .append("                   WHERE   start_date <= TRUNC (#P2)")
                .append("                           AND (end_date IS NULL OR end_date >= TRUNC (#P3))")
                .append("                           AND status = 1")
                .append("                           AND ROWNUM < 2),")
                .append("                 (SELECT   VALUE")
                .append("                    FROM   option_set a, option_set_value b")
                .append("                   WHERE   a.id = b.option_set_id")
                .append("                           AND a.code = 'DEBIT_DAY_TYPE_DEFAULT'")
                .append("                           AND a.status = 1")
                .append("                           AND b.status = 1)))")
                .append(" AND debit_level =")
                .append("               (SELECT   VALUE")
                .append("                  FROM   option_set a, option_set_value b")
                .append("                 WHERE   a.id = b.option_set_id")
                .append("                         AND a.code = #optionSetValueCode")
                .append("                         AND a.status = 1")
                .append("                         AND b.status = 1)");
        Query qDefault = em.createNativeQuery(builder.toString());
        qDefault.setParameter("P0", staffId);
        qDefault.setParameter("P1", staffId);
        qDefault.setParameter("P2", createDate);
        qDefault.setParameter("P3", createDate);
        qDefault.setParameter("optionSetValueCode", optionSetValueCode);
        List result = qDefault.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return mapper((Object[]) result.get(0));
        }
        return null;
    }

    private StockDebitDTO mapper(Object[] objects) {
        StockDebitDTO result = new StockDebitDTO();
        result.setOwnerCode(DataUtil.safeToString(objects[0]));
        result.setDebitValue(DataUtil.safeToLong(objects[1]));
        result.setDebitDayTypeName(GetTextFromBundleHelper.getText(DataUtil.safeToString(objects[2])));
        result.setFinanceTypeName((GetTextFromBundleHelper.getText(DataUtil.safeToString(objects[3]))));
        result.setOwnerType(DataUtil.safeToString(objects[4]));
        result.setTotalPrice(DataUtil.safeToLong(objects[5]));
        if (objects.length > 6) {
            result.setOwnerId(DataUtil.safeToLong(objects[6]));
        }
        return result;
    }

    @Override
    public List<StockDebitDTO> searchStockDebitForUnit(Long shopId) throws Exception {
        StringBuilder builder = new StringBuilder("SELECT   (SELECT   sh.shop_code||'-'||sh.name ")
                .append("           FROM   shop sh")
                .append("          WHERE   sh.shop_id = sd.owner_id)")
                .append("            staffcode,")
                .append("        sd.debit_value debittype,")
                .append("        (SELECT   b.name")
                .append("           FROM   option_set a, option_set_value b")
                .append("          WHERE       a.id = b.option_set_id")
                .append("                  AND a.code = 'DEBIT_DAY_TYPE'")
                .append("                  AND a.status = 1")
                .append("                  AND b.status = 1")
                .append("                  AND VALUE = sd.debit_day_type)")
                .append("            debitdayname,")
                .append("        '--' financename,")
                .append("        sd.owner_type ownertype,")
                .append("        (SELECT   NVL (SUM (st.current_quantity * p.price), 0)")
                .append("           FROM   stock_total st, product_offer_price p, shop sh")
                .append("          WHERE       st.prod_offer_id = p.prod_offer_id")
                .append("                  AND st.owner_type = 1")
                .append("                  AND st.owner_id = sh.shop_id")
                .append("                  AND st.owner_id = sd.owner_id")
                .append("                  AND st.state_id = 1")
                .append("                  AND p.status = 1")
                .append("                  AND p.price_type_id = 23")
                .append(" AND p.price_policy_id = sh.price_policy")
                .append("                  AND p.effect_datetime <= SYSDATE")
                .append("                  AND (p.expire_datetime IS NULL")
                .append("                       OR p.expire_datetime >= TRUNC (SYSDATE)))")
                .append("            currentamount, sd.owner_id")
                .append(" ,(select day_num from finance_type ft where ft.status=1 and ft.finance_type=sd.finance_type and rownum<2) display_finance_type")
                .append(" FROM  stock_debit sd")
                .append(" WHERE   sd.owner_id =#shopId AND sd.owner_type = 1");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("shopId", shopId);
        List queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            List<StockDebitDTO> stockDebitDTOs = Lists.newArrayList();
            for (Object object : queryResult) {
                stockDebitDTOs.add(mapper((Object[]) object));
            }
            return stockDebitDTOs;
        }
        return null;
    }

    @Override
    public List<StockDebitDTO> searchStockDebitForStaff(Long staffId) throws Exception {
        StringBuilder builder = new StringBuilder("SELECT   ")
                .append("        (SELECT   sf.staff_code ||'-'|| sf.name")
                .append("           FROM   staff sf")
                .append("          WHERE   sf.staff_id = NVL (sd.owner_id, sf.staff_id))")
                .append("            staff_code,")
                .append("            sd.debit_value,")
                .append("        (SELECT   b.name")
                .append("           FROM   option_set a, option_set_value b")
                .append("          WHERE       a.id = b.option_set_id")
                .append("                  AND a.code = 'DEBIT_DAY_TYPE'")
                .append("                  AND a.status = 1")
                .append("                  AND b.status = 1")
                .append("                  AND VALUE = sd.debit_day_type)")
                .append("            debit_day_name,")
                .append("        (SELECT   b.name")
                .append("           FROM   option_set a, option_set_value b")
                .append("          WHERE       a.id = b.option_set_id")
                .append("                  AND a.code = 'FINANCE_TYPE'")
                .append("                  AND a.status = 1")
                .append("                  AND b.status = 1")
                .append("                  AND VALUE = sd.finance_type)")
                .append("            finance_name,")
                .append("        sd.owner_type ownertype,")
                .append("        (SELECT   NVL (SUM (st.current_quantity * p.price), 0)")
                .append("           FROM   stock_total st,")
                .append("                  product_offer_price p,")
                .append("                  staff s,")
                .append("                  shop sh")
                .append("          WHERE       st.prod_offer_id = p.prod_offer_id")
                .append("                  AND st.owner_type = 2")
                .append("                  AND st.owner_id = s.staff_id")
                .append("                  AND st.owner_id = sd.owner_id")
                .append("                  AND s.shop_id = sh.shop_id")
                .append("                  AND st.state_id = 1")
                .append("                  AND p.status = 1")
                .append("                  AND p.price_type_id = 23")
                .append("                  AND p.price_policy_id = sh.price_policy")
                .append("                  AND p.effect_datetime <= SYSDATE")
                .append("                  AND (p.expire_datetime IS NULL")
                .append("                       OR p.expire_datetime >= TRUNC (SYSDATE)))")
                .append("            currentamount, sd.owner_id, nvl((select debit_amount from debit_level dlv where dlv.debit_day_type=sd.debit_day_type and dlv.status=1 and dlv.debit_level=sd.debit_value and rownum<2),0) display_debit_value,")
                .append(" (select day_num from finance_type ft where ft.status=1 and ft.finance_type=sd.finance_type and rownum<2) display_finance_type")
                .append(" FROM   stock_debit sd")
                .append(" WHERE   sd.owner_type = 2 AND sd.owner_id = #staffId");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("staffId", staffId);
        List queryResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(queryResult)) {
            List<StockDebitDTO> stockDebitDTOs = Lists.newArrayList();
            for (Object object : queryResult) {
                Object[] map = (Object[]) object;
                StockDebitDTO stockDebitDTO = mapper(map);
                stockDebitDTO.setDisplayDebitValue(DataUtil.safeToLong(map[7]));
                stockDebitDTO.setDisplayFinance(DataUtil.safeToLong(map[8]));
                stockDebitDTOs.add(stockDebitDTO);
            }
            return stockDebitDTOs;
        }
        return null;
    }

    private String getIsVtUnitChannelType(Long ownerType, Long ownerId) {
        StringBuilder builder = new StringBuilder("");
        builder.append("SELECT   ct.is_vt_unit ");
        if (DataUtil.safeEqual(Const.OWNER_TYPE.STAFF_LONG, ownerType)) {
            builder.append("    FROM   staff a, channel_type ct ");
            builder.append("    WHERE   a.channel_type_id = ct.channel_type_id");
            builder.append("            AND a.staff_id = #ownerId");
            builder.append("            AND ct.object_type = #ownerType");
        } else if (DataUtil.safeEqual(Const.OWNER_TYPE.SHOP_LONG, ownerType)) {
            builder.append("    FROM   shop a, channel_type ct ");
            builder.append("    WHERE   a.channel_type_id = ct.channel_type_id");
            builder.append("            AND a.shop_id= #ownerId");
            builder.append("            AND ct.object_type = #ownerType");
        } else {
            return null;
        }
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List queryResult = query.getResultList();
        if (queryResult != null && !queryResult.isEmpty()) {
            return DataUtil.safeToString(queryResult.get(0));
        }
        return null;
    }
}
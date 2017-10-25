package com.viettel.bccs.inventory.common;

import com.google.common.collect.Lists;
import com.viettel.bccs.sale.model.SaleTransDetail;
import com.viettel.bccs.sale.repo.SaleTransDetailRepo;
import com.viettel.common.util.DateTimeUtils;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

    public static final Logger logger = Logger.getLogger(Utils.class);

    public static String findValueOptionSet(EntityManager em, String code, String name) {
        String res = "";
        String sql = "select osv.value from option_set os, option_set_value osv where os.code=:code and osv.name=:name and os.id=osv.option_set_id";
        Query query = em.createNativeQuery(sql);
        query.setParameter("code", code);
        query.setParameter("name", name);
        List list = query.getResultList();
        if (!list.isEmpty()) {
            Object value = list.get(0);
            if (value != null) {
                res = value.toString();
            }
        }
        return res;
    }

    public static String getValueByCodeOptionSet(EntityManager em, String type) {
        String code = null;
        String sql = "select osv.name from option_set_value osv, option_set os where osv.option_set_id=os.id and os.status=1 and os.code=:code and rownum <2";
        try {
            Query query = em.createNativeQuery(sql);
            query.setParameter("code", type);
            List lst = query.getResultList();
            if (!lst.isEmpty()) {
                code = (String) lst.get(0);
            }
            if (code == null) {
                code = "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String buildMessage(EntityManager emSale, Long saleTransId, SaleTransDetailRepo repo) throws Exception {
        try {
            List lstSaleTransDetail = findSaleTransDetailByProperty(emSale, SaleTransDetail.COLUMNS.SALETRANSID.name(), saleTransId, repo);
            String sendMess = "Viettel tran trong thong bao: ngay %s, Quy khach mua";
            sendMess = String.format(sendMess, DateTimeUtils.convertDateToString(DbUtil.getSysDate(emSale)));
            for (int i = 0; i < lstSaleTransDetail.size(); i++) {
                SaleTransDetail saleTransDetail = (SaleTransDetail) lstSaleTransDetail.get(i);
                if (i != 0) {
                    sendMess += ", " + saleTransDetail.getQuantity() + " " + saleTransDetail.getStockModelName();
                } else {
                    sendMess += " " + saleTransDetail.getQuantity() + " " + saleTransDetail.getStockModelName();
                }
            }
            return sendMess;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static List findSaleTransDetailByProperty(EntityManager emSale, String columnName, Object value, SaleTransDetailRepo repo) {
        logger.info("finding SaleTransDetail instance with columnName: " + columnName + ", value: " + value);
        try {
            List<SaleTransDetail> listSaleTransDetail = Lists.newArrayList(repo.findAll(repo.toPredicate(Lists.newArrayList(
                    new FilterRequest(columnName, FilterRequest.Operator.EQ, value)
            ))));
            return listSaleTransDetail;
        } catch (RuntimeException re) {
            logger.error("find by columnName failed", re);
            throw re;
        }
    }

    public static String getTableNameByStockType(Long stockType, String nameType) {
        /**
         * END LOG
         */
        try {
            String stockModelNameNormal = "";
            String stockModelNameHibernate = "";
            if (stockType.equals(Const.STOCK_TYPE.STOCK_CARD)) {
                stockModelNameHibernate = "StockCard";
                stockModelNameNormal = "STOCK_CARD";
            }

            if (stockType.equals(Const.STOCK_TYPE.STOCK_HANDSET)) {
                stockModelNameHibernate = "StockHandset";
                stockModelNameNormal = "STOCK_HANDSET";
            }

            if (stockType.equals(Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE)) {
                stockModelNameHibernate = "StockIsdnHomephone";
                stockModelNameNormal = "STOCK_ISDN_HOMEPHONE";
            }

            if (stockType.equals(Const.STOCK_TYPE.STOCK_ISDN_MOBILE)) {
                stockModelNameHibernate = "StockIsdnMobile";
                stockModelNameNormal = "STOCK_ISDN_MOBILE";
            }

            if (stockType.equals(Const.STOCK_TYPE.STOCK_ISDN_PSTN)) {
                stockModelNameHibernate = "StockIsdnPstn";
                stockModelNameNormal = "STOCK_ISDN_PSTN";
            }

            if (stockType.equals(Const.STOCK_TYPE.STOCK_KIT)) {
                stockModelNameHibernate = "StockKit";
                stockModelNameNormal = "STOCK_KIT";
            }

            if (stockType.equals(Const.STOCK_TYPE.STOCK_SIM_POST_PAID)) {
                stockModelNameHibernate = "StockSimPostPaid";
                stockModelNameNormal = "STOCK_SIM";
            }

            if (stockType.equals(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID)) {
                stockModelNameHibernate = "StockSimPrePaid";
                stockModelNameNormal = "STOCK_SIM";
            }
            if ("normal".equals(nameType)) {
                return stockModelNameNormal;
            }

            return stockModelNameHibernate;

        } catch (RuntimeException re) {
            throw re;
        }
    }

}

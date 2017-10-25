package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockIsdnVtShopDTO;
import com.viettel.bccs.inventory.model.StockIsdnVtShop;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockIsdnVtShop.COLUMNS;
import com.viettel.bccs.inventory.model.QStockIsdnVtShop;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockIsdnVtShopRepoImpl implements StockIsdnVtShopRepoCustom {

    public static final Logger logger = Logger.getLogger(StockIsdnVtShopRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockIsdnVtShop stockIsdnVtShop = QStockIsdnVtShop.stockIsdnVtShop;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ADDRESS:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.address, filter);
                        break;
                    case AREACODE:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.areaCode, filter);
                        break;
                    case CONTACTISDN:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.contactIsdn, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockIsdnVtShop.createDate, filter);
                        break;
                    case CUSTOMERNAME:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.customerName, filter);
                        break;
                    case IDNO:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.idNo, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.isdn, filter);
                        break;
                    case LASTMODIFY:
                        expression = DbUtil.createDateExpression(stockIsdnVtShop.lastModify, filter);
                        break;
                    case OTP:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.otp, filter);
                        break;
                    case STAFFUPDATE:
                        expression = DbUtil.createLongExpression(stockIsdnVtShop.staffUpdate, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.status, filter);
                        break;
                    case VIETTELSHOPID:
                        expression = DbUtil.createStringExpression(stockIsdnVtShop.viettelshopId, filter);
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

    public List<StockIsdnVtShop> findRequestList(List<Long> requestList) {
        StringBuilder builder = new StringBuilder("");
        builder.append(" SELECT ab.* ");
        builder.append("   FROM stock_isdn_vt_shop ab ");
        builder.append("  WHERE ab.request_id " + DbUtil.createInQuery("request_id", requestList));
        Query query = em.createNativeQuery(builder.toString(), StockIsdnVtShop.class);
        DbUtil.setParamInQuery(query, "request_id", requestList);
        return query.getResultList();
    }

}
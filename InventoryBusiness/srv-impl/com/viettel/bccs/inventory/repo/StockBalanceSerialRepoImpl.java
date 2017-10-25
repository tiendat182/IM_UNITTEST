package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.StockBalanceSerial;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockBalanceSerial.COLUMNS;
import com.viettel.bccs.inventory.model.QStockBalanceSerial;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockBalanceSerialRepoImpl implements StockBalanceSerialRepoCustom {


    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    public static final Logger logger = Logger.getLogger(StockBalanceSerialRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockBalanceSerial stockBalanceSerial = QStockBalanceSerial.stockBalanceSerial;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockBalanceSerial.createDatetime, filter);
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(stockBalanceSerial.fromSerial, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockBalanceSerial.prodOfferId, filter);
                        break;
                    case STOCKBALANCEDETAIL:
                        expression = DbUtil.createLongExpression(stockBalanceSerial.stockBalanceDetail, filter);
                        break;
                    case STOCKBALANCESERIALID:
                        expression = DbUtil.createLongExpression(stockBalanceSerial.stockBalanceSerialId, filter);
                        break;
                    case STOCKREQUESTID:
                        expression = DbUtil.createLongExpression(stockBalanceSerial.stockRequestId, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(stockBalanceSerial.toSerial, filter);
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
    public List<StockBalanceSerial> getListStockBalanceSerial(Long detailID) throws Exception {
        StringBuilder builder = new StringBuilder("select * from stock_balance_serial\n" +
                "where stock_balance_detail=?");
        Query query = em.createNativeQuery(builder.toString(), StockBalanceSerial.class);
        query.setParameter(1, detailID);

        return DataUtil.defaultIfNull(query.getResultList(), Lists.newArrayList());
    }
}
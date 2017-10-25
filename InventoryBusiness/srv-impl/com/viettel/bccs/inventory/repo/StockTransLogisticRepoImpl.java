package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransLogisticDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockTransLogistic.COLUMNS;
import com.viettel.bccs.inventory.model.QStockTransLogistic;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockTransLogisticRepoImpl implements StockTransLogisticRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransLogisticRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransLogistic stockTransLogistic = QStockTransLogistic.stockTransLogistic;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockTransLogistic.createDate, filter);
                        break;
                    case LOGISTICDESCRIPTION:
                        expression = DbUtil.createStringExpression(stockTransLogistic.logisticDescription, filter);
                        break;
                    case LOGISTICRESPONSECODE:
                        expression = DbUtil.createStringExpression(stockTransLogistic.logisticResponseCode, filter);
                        break;
                    case LOGISTICRETRY:
                        expression = DbUtil.createLongExpression(stockTransLogistic.logisticRetry, filter);
                        break;
                    case PRINTUSERLIST:
                        expression = DbUtil.createStringExpression(stockTransLogistic.printUserList, filter);
                        break;
                    case REJECTNOTEDESCRIPTION:
                        expression = DbUtil.createStringExpression(stockTransLogistic.rejectNoteDescription, filter);
                        break;
                    case REJECTNOTERESPONSE:
                        expression = DbUtil.createStringExpression(stockTransLogistic.rejectNoteResponse, filter);
                        break;
                    case REQUESTTYPELOGISTIC:
                        expression = DbUtil.createLongExpression(stockTransLogistic.requestTypeLogistic, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockTransLogistic.status, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransLogistic.stockTransId, filter);
                        break;
                    case STOCKTRANSLOGISTICID:
                        expression = DbUtil.createLongExpression(stockTransLogistic.stockTransLogisticId, filter);
                        break;
                    case STOCKTRANSTYPE:
                        expression = DbUtil.createLongExpression(stockTransLogistic.stockTransType, filter);
                        break;
                    case UPDATELOGISTIC:
                        expression = DbUtil.createLongExpression(stockTransLogistic.updateLogistic, filter);
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

    public List<StockTransLogisticDTO> getLstOrderLogistics(Long id, Long maxRetry, Long maxDay, Long numberThread) throws Exception {
        StringBuilder sql = new StringBuilder();
        List<StockTransLogisticDTO> lstStockTransLogisticDTOs = Lists.newArrayList();
        sql.append(" SELECT   stock_trans_logistic_id, stock_trans_id,logistic_retry ");
        sql.append("  FROM   stock_trans_logistic");
        sql.append("   WHERE       create_date >= TRUNC (SYSDATE - #maxDay)  ");
        sql.append("  AND status = 0");
        sql.append("  AND (logistic_retry is null or logistic_retry < #maxRetry)");
        sql.append("  AND mod(stock_trans_logistic_id, #numThread) = #id");
        sql.append("  ORDER BY   create_date ASC");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("maxDay", maxDay);
        query.setParameter("maxRetry", maxRetry);
        query.setParameter("numThread", numberThread);
        query.setParameter("id", id);
        List listResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(listResult)) {
            for (Object o : listResult) {
                Object[] obj = (Object[]) o;
                StockTransLogisticDTO stockTransLogisticDTO = new StockTransLogisticDTO();
                stockTransLogisticDTO.setStockTransLogisticId(DataUtil.safeToLong(obj[0]));
                stockTransLogisticDTO.setStockTransId(DataUtil.safeToLong(obj[1]));
                stockTransLogisticDTO.setLogisticRetry(DataUtil.safeToLong(obj[2]));
                lstStockTransLogisticDTOs.add(stockTransLogisticDTO);
            }
        }
        return lstStockTransLogisticDTOs;
    }

}
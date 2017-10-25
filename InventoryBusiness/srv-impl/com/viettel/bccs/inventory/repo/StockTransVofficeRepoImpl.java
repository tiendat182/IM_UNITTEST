package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.model.QStockTransVoffice;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.bccs.inventory.model.StockTransVoffice.COLUMNS;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class StockTransVofficeRepoImpl implements StockTransVofficeRepoCustom {


    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    public static final Logger logger = Logger.getLogger(StockTransVofficeRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransVoffice stockTransVoffice = QStockTransVoffice.stockTransVoffice;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ACCOUNTNAME:
                        expression = DbUtil.createStringExpression(stockTransVoffice.accountName, filter);
                        break;
                    case ACCOUNTPASS:
                        expression = DbUtil.createStringExpression(stockTransVoffice.accountPass, filter);
                        break;
                    case ACTIONCODE:
                        expression = DbUtil.createStringExpression(stockTransVoffice.actionCode, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockTransVoffice.createDate, filter);
                        break;
                    case CREATEUSERID:
                        expression = DbUtil.createLongExpression(stockTransVoffice.createUserId, filter);
                        break;
                    case DENIEDDATE:
                        expression = DbUtil.createDateExpression(stockTransVoffice.deniedDate, filter);
                        break;
                    case DENIEDUSER:
                        expression = DbUtil.createStringExpression(stockTransVoffice.deniedUser, filter);
                        break;
                    case ERRORCODE:
                        expression = DbUtil.createStringExpression(stockTransVoffice.errorCode, filter);
                        break;
                    case ERRORCODEDESCRIPTION:
                        expression = DbUtil.createStringExpression(stockTransVoffice.errorCodeDescription, filter);
                        break;
                    case FINDSERIAL:
                        expression = DbUtil.createLongExpression(stockTransVoffice.findSerial, filter);
                        break;
                    case LASTMODIFY:
                        expression = DbUtil.createDateExpression(stockTransVoffice.lastModify, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockTransVoffice.note, filter);
                        break;
                    case PREFIXTEMPLATE:
                        expression = DbUtil.createStringExpression(stockTransVoffice.prefixTemplate, filter);
                        break;
                    case RECEIPTNO:
                        expression = DbUtil.createStringExpression(stockTransVoffice.receiptNo, filter);
                        break;
                    case RESPONSECODE:
                        expression = DbUtil.createStringExpression(stockTransVoffice.responseCode, filter);
                        break;
                    case RESPONSECODEDESCRIPTION:
                        expression = DbUtil.createStringExpression(stockTransVoffice.responseCodeDescription, filter);
                        break;
                    case RETRY:
                        expression = DbUtil.createLongExpression(stockTransVoffice.retry, filter);
                        break;
                    case SIGNUSERLIST:
                        expression = DbUtil.createStringExpression(stockTransVoffice.signUserList, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(stockTransVoffice.status, filter);
                        break;
                    case STOCKTRANSACTIONID:
                        expression = DbUtil.createLongExpression(stockTransVoffice.stockTransActionId, filter);
                        break;
                    case STOCKTRANSVOFFICEID:
                        expression = DbUtil.createStringExpression(stockTransVoffice.stockTransVofficeId, filter);
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
    public StockTransVoffice findStockTransVofficeByActionId(Long actionId) {
        StringBuilder builder = new StringBuilder("select * from stock_trans_voffice where stock_trans_action_id=?");
        Query query = em.createNativeQuery(builder.toString(), StockTransVoffice.class);
        query.setParameter(1, actionId);
        List<StockTransVoffice> lsStockTransVoffices = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lsStockTransVoffices)) {
            return lsStockTransVoffices.get(0);
        }
        return null;
    }

    @Override
    public StockTransVoffice findStockTransVofficeByRequestId(String requestId) {
        StringBuilder builder = new StringBuilder("select * from stock_trans_voffice where stock_trans_voffice_id=?");
        Query query = em.createNativeQuery(builder.toString(), StockTransVoffice.class);
        query.setParameter(1, requestId);
        List<StockTransVoffice> lsStockTransVoffices = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lsStockTransVoffices)) {
            return lsStockTransVoffices.get(0);
        }
        return null;
    }

    @Override
    public List<StockTransVoffice> searchStockTransVoffice(StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        List params = Lists.newArrayList();//and stock_trans_voffice_id = '1606170000016733'
        StringBuilder builder = new StringBuilder("select * from stock_trans_voffice where 1=1 ");
//        //simple param
//        if (stockTransVofficeDTO.getNumberThread() != null) {
//            builder.append(" and mod(stock_trans_voffice_id, #P").append(params.size()).append(")");
//            params.add(stockTransVofficeDTO.getNumberThread());
//            builder.append(" = #P").append(params.size());
//            params.add(stockTransVofficeDTO.getId());
//        }
        if (stockTransVofficeDTO.getMaxRetry() != null) {
            builder.append(" and (retry  is null or retry < #P").append(params.size()).append(")");
            params.add(stockTransVofficeDTO.getMaxRetry());
        }
        if (stockTransVofficeDTO.getMaxDay() != null) {
            builder.append(" and create_date > trunc(sysdate) - #P").append(params.size());
            params.add(stockTransVofficeDTO.getMaxDay());
        }
        if (!DataUtil.isNullOrEmpty(stockTransVofficeDTO.getPrefixTemplate())) {
            builder.append(" and prefix_template = #P").append(params.size());
            params.add(stockTransVofficeDTO.getPrefixTemplate());
        }
        if (!DataUtil.isNullOrEmpty(stockTransVofficeDTO.getStatus())) {
            builder.append(" and status = #P").append(params.size());
            params.add(stockTransVofficeDTO.getStatus());
        }

        builder.append(" AND ROWNUM <= #P").append(params.size());
        params.add(stockTransVofficeDTO.getNumberThread());
        //param as list obj
        if (!DataUtil.isNullOrEmpty(stockTransVofficeDTO.getListStatus())) {
            builder.append(" and status " + DbUtil.createInQuery("status", stockTransVofficeDTO.getListStatus()));
        }
        builder.append(" FOR UPDATE NOWAIT");
        Query query = em.createNativeQuery(builder.toString(), StockTransVoffice.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter("P" + i, params.get(i));
        }
        if (!DataUtil.isNullOrEmpty(stockTransVofficeDTO.getListStatus())) {
            DbUtil.setParamInQuery(query, "status", stockTransVofficeDTO.getListStatus());
        }
        return query.getResultList();
    }

    public List<StockTransVoffice> getLstVofficeSigned(Long maxDay, String prefixTemplate) throws Exception {
        StringBuilder builder = new StringBuilder("select * from stock_trans_voffice where 1=1 ");
        builder.append(" AND status in (1,2)");
        builder.append(" AND prefix_template = #prefixTemplate");
        builder.append(" AND (retry is null or retry <= 4)");
        builder.append(" AND create_date >= trunc(sysdate) - #maxDay");
        Query query = em.createNativeQuery(builder.toString(), StockTransVoffice.class);
        query.setParameter("prefixTemplate", prefixTemplate);
        query.setParameter("maxDay", maxDay);
        return query.getResultList();
    }

    public StockTransVoffice findOneById(String stockTransVofficeId) throws Exception {
        StringBuilder builder = new StringBuilder("select * from stock_trans_voffice where stock_trans_voffice_id= ?");
        Query query = em.createNativeQuery(builder.toString(), StockTransVoffice.class);
        query.setParameter(1, stockTransVofficeId);
        List<StockTransVoffice> lsStockTransVoffices = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lsStockTransVoffices)) {
            return lsStockTransVoffices.get(0);
        }
        return null;
    }
}
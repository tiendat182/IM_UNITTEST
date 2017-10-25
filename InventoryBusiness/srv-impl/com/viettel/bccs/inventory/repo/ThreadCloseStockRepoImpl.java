package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ThreadCloseStockDTO;
import com.viettel.bccs.inventory.model.QThreadCloseStock;
import com.viettel.bccs.inventory.model.ThreadCloseStock;
import com.viettel.bccs.inventory.model.ThreadCloseStock.COLUMNS;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class ThreadCloseStockRepoImpl implements ThreadCloseStockRepoCustom {

    public static final Logger logger = Logger.getLogger(ThreadCloseStockRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<ThreadCloseStock, ThreadCloseStockDTO> mapper = new BaseMapper(ThreadCloseStock.class, ThreadCloseStockDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QThreadCloseStock threadCloseStock = QThreadCloseStock.threadCloseStock;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(threadCloseStock.description, filter);
                        break;
                    case ENDACTIONTIME:
                        expression = DbUtil.createDateExpression(threadCloseStock.endActionTime, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(threadCloseStock.id, filter);
                        break;
                    case PROCESSDATE:
                        expression = DbUtil.createDateExpression(threadCloseStock.processDate, filter);
                        break;
                    case STARTACTIONTIME:
                        expression = DbUtil.createDateExpression(threadCloseStock.startActionTime, filter);
                        break;
                    case STARTHOUR:
                        expression = DbUtil.createStringExpression(threadCloseStock.startHour, filter);
                        break;
                    case STARTMINUTE:
                        expression = DbUtil.createStringExpression(threadCloseStock.startMinute, filter);
                        break;
                    case STARTSECOND:
                        expression = DbUtil.createStringExpression(threadCloseStock.startSecond, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(threadCloseStock.status, filter);
                        break;
                    case THREADNAME:
                        expression = DbUtil.createStringExpression(threadCloseStock.threadName, filter);
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
    public ThreadCloseStockDTO getThreadCloseStockByName(String threadName) throws Exception {
        List<ThreadCloseStock> lst ;
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from thread_close_stock where thread_name = #thread_name ");
        Query query = em.createNativeQuery(strQuery.toString(), ThreadCloseStock.class);
        query.setParameter("thread_name", threadName);
        lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst.get(0));
        }
        return null;
    }

    @Override
    public ThreadCloseStockDTO getThreadAvailable(String threadName) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from thread_close_stock where thread_name = #thread_name " +
                " AND (process_date is null OR (status = 0 AND trunc(process_date) < trunc(sysdate))) ");
        Query query = em.createNativeQuery(strQuery.toString(), ThreadCloseStock.class);
        query.setParameter("thread_name", threadName);
        List<ThreadCloseStock> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst.get(0));
        }
        return null;
    }
}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransExtDTO;
import com.viettel.bccs.inventory.model.StockTransExt;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockTransExt.COLUMNS;
import com.viettel.bccs.inventory.model.QStockTransExt;

import java.math.BigDecimal;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockTransExtRepoImpl implements StockTransExtRepoCustom {

    public static final Logger logger = Logger.getLogger(StockTransExtRepoCustom.class);

    private final BaseMapper<StockTransExt, StockTransExtDTO> extMapper = new BaseMapper<>(StockTransExt.class, StockTransExtDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockTransExt stockTransExt = QStockTransExt.stockTransExt;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case EXTKEY:
                        expression = DbUtil.createStringExpression(stockTransExt.extKey, filter);
                        break;
                    case EXTVALUE:
                        expression = DbUtil.createStringExpression(stockTransExt.extValue, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockTransExt.id, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockTransExt.stockTransId, filter);
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

    public boolean checkDuplicateTrans(String key, String value) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT COUNT (*) ");
        strQuery.append("  FROM stock_trans a");
        strQuery.append(" WHERE a.create_datetime >= SYSDATE - 10");
        strQuery.append("   AND EXISTS (SELECT 1 FROM stock_trans_ext b ");
        strQuery.append("                       WHERE a.stock_trans_id = b.stock_trans_id");
        strQuery.append("                         AND b.ext_key = #key ");
        strQuery.append("                         AND b.ext_value = #value ");
        strQuery.append("                         AND b.status = 1 )");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("key", key);
        query.setParameter("value", value);
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if (result != null) {
            return result.longValue() > 0;
        }
        return false;
    }

    public StockTransExtDTO getStockTransId(String extKey, String extValue) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" select *");
        strQuery.append(" from bccs_im.stock_trans_ext");
        strQuery.append(" where ext_key = #extKey");
        strQuery.append(" and ext_value = #extValue");
        strQuery.append(" and status = 1");
        Query query = em.createNativeQuery(strQuery.toString(), StockTransExt.class);
        query.setParameter("extKey", extKey);
        query.setParameter("extValue", extValue);
        List<StockTransExt> result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            return extMapper.toDtoBean(result.get(0));
        }
        return null;
    }
}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockOrderDetailDTO;
import com.viettel.bccs.inventory.model.StockOrderDetail;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockOrderDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QStockOrderDetail;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockOrderDetailRepoImpl implements StockOrderDetailRepoCustom {
    private final BaseMapper<StockOrderDetail, StockOrderDetailDTO> mapper = new BaseMapper<>(StockOrderDetail.class, StockOrderDetailDTO.class);
    public static final Logger logger = Logger.getLogger(StockOrderDetailRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockOrderDetail stockOrderDetail = QStockOrderDetail.stockOrderDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case QUANTITYORDER:
                        expression = DbUtil.createLongExpression(stockOrderDetail.quantityOrder, filter);
                        break;
                    case QUANTITYREAL:
                        expression = DbUtil.createLongExpression(stockOrderDetail.quantityReal, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockOrderDetail.prodOfferId, filter);
                        break;
                    case STOCKORDERDETAILID:
                        expression = DbUtil.createLongExpression(stockOrderDetail.stockOrderDetailId, filter);
                        break;
                    case STOCKORDERID:
                        expression = DbUtil.createLongExpression(stockOrderDetail.stockOrderId, filter);
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
    public List<StockOrderDetailDTO> getListStockOrderDetail(Long stockOrderId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from  stock_order_detail where 1=1  ");
        strQuery.append(" and stock_order_id = #stockOrderId ");
        Query query = em.createNativeQuery(strQuery.toString(), StockOrderDetail.class);
        query.setParameter("stockOrderId", stockOrderId);
        List<StockOrderDetail> list = query.getResultList();
        if (!DataUtil.isNullOrEmpty(list)) {
            return mapper.toDtoBean(list);
        }
        return null;
    }

}
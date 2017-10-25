package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockDepositDTO;
import com.viettel.bccs.inventory.model.StockDeposit;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockDeposit.COLUMNS;
import com.viettel.bccs.inventory.model.QStockDeposit;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockDepositRepoImpl implements StockDepositRepoCustom {

    public static final Logger logger = Logger.getLogger(StockDepositRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<StockDeposit, StockDepositDTO> mapper = new BaseMapper(StockDeposit.class, StockDepositDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockDeposit stockDeposit = QStockDeposit.stockDeposit;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CHANELTYPEID:
                        expression = DbUtil.createLongExpression(stockDeposit.chanelTypeId, filter);
                        break;
                    case DATEFROM:
                        expression = DbUtil.createDateExpression(stockDeposit.dateFrom, filter);
                        break;
                    case DATETO:
                        expression = DbUtil.createDateExpression(stockDeposit.dateTo, filter);
                        break;
                    case LASTMODIFY:
                        expression = DbUtil.createDateExpression(stockDeposit.lastModify, filter);
                        break;
                    case MAXSTOCK:
                        expression = DbUtil.createLongExpression(stockDeposit.maxStock, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockDeposit.prodOfferId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(stockDeposit.status, filter);
                        break;
                    case STOCKDEPOSITID:
                        expression = DbUtil.createLongExpression(stockDeposit.stockDepositId, filter);
                        break;
                    case TRANSTYPE:
                        expression = DbUtil.createLongExpression(stockDeposit.transType, filter);
                        break;
                    case USERMODIFY:
                        expression = DbUtil.createStringExpression(stockDeposit.userModify, filter);
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

    public StockDepositDTO getStockDeposit(Long prodOfferId, Long channelTypeId, Long transType) throws Exception {
        try {
            List<StockDeposit> lst ;
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("SELECT * FROM STOCK_DEPOSIT ");
            strQuery.append(" where status = 1 and chanel_type_id = #channelTypeId and trans_type = #transType and prod_offer_id = #prodOfferId ");
            strQuery.append(" and (trunc(date_from) <= trunc(sysdate) and (date_to is null or trunc(date_to) >= trunc(sysdate)))");
            //execute query
            Query query = em.createNativeQuery(strQuery.toString(), StockDeposit.class);
            query.setParameter("channelTypeId", channelTypeId);
            query.setParameter("transType", transType);
            query.setParameter("prodOfferId", prodOfferId);
            lst = query.getResultList();
            if (lst != null && !lst.isEmpty()) {
                return mapper.toDtoBean(lst.get(0));
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
        }
        return null;
    }


}
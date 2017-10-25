package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockOrderAgentDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QStockOrderAgentDetail;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockOrderAgentDetailRepoImpl implements StockOrderAgentDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(StockOrderAgentDetailRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockOrderAgentDetail stockOrderAgentDetail = QStockOrderAgentDetail.stockOrderAgentDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockOrderAgentDetail.createDate, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockOrderAgentDetail.note, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockOrderAgentDetail.prodOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(stockOrderAgentDetail.quantity, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(stockOrderAgentDetail.stateId, filter);
                        break;
                    case STOCKORDERAGENTDETAILID:
                        expression = DbUtil.createLongExpression(stockOrderAgentDetail.stockOrderAgentDetailId, filter);
                        break;
                    case STOCKORDERAGENTID:
                        expression = DbUtil.createLongExpression(stockOrderAgentDetail.stockOrderAgentId, filter);
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

    public List<StockTransFullDTO> getListGood(Long orderAgentId) {
        List<StockTransFullDTO> lstResult = Lists.newArrayList();
        try {
            StringBuilder queryString = new StringBuilder("");
            queryString.append(" SELECT   st.name as stockTypeName, ");
            queryString.append("          soad.prod_offer_id proOfferId, ");
            queryString.append("          sm.name as productName, ");
            queryString.append("          sm.product_offer_type_id proOfferTypeId, ");
            queryString.append("          sm.name as proOfferTypeName, ");
            queryString.append("          (select name from option_set_value where value = sm.unit and status=1 and option_set_id = (select id from option_set where status=1 and code = 'STOCK_MODEL_UNIT')) unit,");
            queryString.append("          soad.state_id as stateId, ");
            queryString.append("          (select name from option_set_value where value = to_char(soad.state_id) and status=1 and option_set_id = (select id from option_set where status=1 and code = 'GOOD_STATE')) stateName,");
            queryString.append("          soad.quantity AS quantity, ");
            queryString.append("          soad.quantity AS quantityReal ");
            queryString.append("   FROM   stock_order_agent_detail soad, ");
            queryString.append("          product_offering sm, ");
            queryString.append("          product_offer_type st ");
            queryString.append("  WHERE       soad.stock_order_agent_id = ? ");
            queryString.append("          AND soad.prod_offer_id = sm.prod_offer_id ");
            queryString.append("          AND st.product_offer_type_id = sm.product_offer_type_id ");
            Query query = em.createNativeQuery(queryString.toString());
            query.setParameter(1, orderAgentId);
            List<Object[]> tmp = query.getResultList();
            for(Object[] ob:tmp){
                StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
                int index = 0;
                stockTransFullDTO.setStockTypeName(DataUtil.safeToString(ob[index++]));
                stockTransFullDTO.setProdOfferId(DataUtil.safeToLong(ob[index++]));
                stockTransFullDTO.setProdOfferName(DataUtil.safeToString(ob[index++]));
                stockTransFullDTO.setProductOfferTypeId(DataUtil.safeToLong(ob[index++]));
                stockTransFullDTO.setProductOfferTypeName(DataUtil.safeToString(ob[index++]));
                stockTransFullDTO.setUnit(DataUtil.safeToString(ob[index++]));
                stockTransFullDTO.setStateId(DataUtil.safeToLong(ob[index++]));
                stockTransFullDTO.setStateName(DataUtil.safeToString(ob[index++]));
                stockTransFullDTO.setQuantity(DataUtil.safeToLong(ob[index++]));
                stockTransFullDTO.setQuantityRemain(DataUtil.safeToLong(ob[index]));
                lstResult.add(stockTransFullDTO);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lstResult;
    }

}
package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.RequestLiquidateDetailDTO;
import com.viettel.bccs.inventory.model.QRequestLiquidateDetail;
import com.viettel.bccs.inventory.model.RequestLiquidateDetail.COLUMNS;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class RequestLiquidateDetailRepoImpl implements RequestLiquidateDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(RequestLiquidateDetailRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    
    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QRequestLiquidateDetail requestLiquidateDetail = QRequestLiquidateDetail.requestLiquidateDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(requestLiquidateDetail.createDatetime, filter); 
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(requestLiquidateDetail.prodOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(requestLiquidateDetail.quantity, filter); 
                        break;
                    case REQUESTLIQUIDATEDETAILID:
                        expression = DbUtil.createLongExpression(requestLiquidateDetail.requestLiquidateDetailId, filter); 
                        break;
                    case REQUESTLIQUIDATEID:
                        expression = DbUtil.createLongExpression(requestLiquidateDetail.requestLiquidateId, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(requestLiquidateDetail.stateId, filter); 
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
    public List<RequestLiquidateDetailDTO> getLstRequestLiquidateDetailDTO(Long requestLiquidateId) throws Exception {
        List params = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   a.request_liquidate_detail_id, a.request_liquidate_id, a.prod_offer_id, a.quantity, a.state_id, ");
        strQuery.append("          (select check_serial from product_offering where prod_offer_id = a.prod_offer_id) as check_serial ");
        strQuery.append("   FROM   request_liquidate_detail a ");
        strQuery.append("  WHERE       1 = 1 ");
        if (requestLiquidateId != null) {
            strQuery.append("          AND request_liquidate_id = ? ");
            params.add(requestLiquidateId);
        }
        Query query = em.createNativeQuery(strQuery.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        List<Object[]> lsResult = query.getResultList();
        List<RequestLiquidateDetailDTO> lsData = Lists.newArrayList();
        if (!DataUtil.isNullObject(lsResult)) {
            RequestLiquidateDetailDTO dto;
            for (Object[] object : lsResult) {
                int index = 0;
                dto = new RequestLiquidateDetailDTO();
                dto.setRequestLiquidateDetailId(DataUtil.safeToLong(object[index++]));
                dto.setRequestLiquidateId(DataUtil.safeToLong(object[index++]));
                dto.setProdOfferId(DataUtil.safeToLong(object[index++]));
                dto.setQuantity(DataUtil.safeToLong(object[index++]));
                dto.setStateId(DataUtil.safeToLong(object[index++]));
                dto.setCheckSerial(DataUtil.safeToString(object[index]));
                lsData.add(dto);
            }
        }
        return lsData;
    }

}
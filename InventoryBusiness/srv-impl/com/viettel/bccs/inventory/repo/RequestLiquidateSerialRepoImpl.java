package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.QRequestLiquidateSerial;
import com.viettel.bccs.inventory.model.RequestLiquidateSerial;
import com.viettel.bccs.inventory.model.RequestLiquidateSerial.COLUMNS;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class RequestLiquidateSerialRepoImpl implements RequestLiquidateSerialRepoCustom {

    public static final Logger logger = Logger.getLogger(RequestLiquidateSerialRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QRequestLiquidateSerial requestLiquidateSerial = QRequestLiquidateSerial.requestLiquidateSerial;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(requestLiquidateSerial.createDatetime, filter); 
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(requestLiquidateSerial.fromSerial, filter); 
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(requestLiquidateSerial.quantity, filter); 
                        break;
                    case REQUESTLIQUIDATEID:
                        expression = DbUtil.createLongExpression(requestLiquidateSerial.requestLiquidateId, filter); 
                        break;
                    case REQUESTLIQUIDATESERIALID:
                        expression = DbUtil.createLongExpression(requestLiquidateSerial.requestLiquidateSerialId, filter); 
                        break;
                    case REQUESTLIQUIDATEDETAILID:
                        expression = DbUtil.createLongExpression(requestLiquidateSerial.requestLiquidateDetailId, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(requestLiquidateSerial.toSerial, filter); 
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
    public List<RequestLiquidateSerial> getLstRequestLiquidateSerialDTO(Long requestLiquidateDetailId) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   request_liquidate_serial_id, request_liquidate_detail_id, request_liquidate_id, ");
        strQuery.append("          from_serial, to_serial, quantity ");
        strQuery.append("   FROM   request_liquidate_serial ");
        strQuery.append("  WHERE   request_liquidate_detail_id = #requestLiquidateDetailId ");
        Query query = em.createNativeQuery(strQuery.toString(), RequestLiquidateSerial.class);
        query.setParameter("requestLiquidateDetailId", requestLiquidateDetailId);
        return query.getResultList();
    }

}
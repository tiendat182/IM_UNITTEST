package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ChangeModelTransSerialDTO;
import com.viettel.bccs.inventory.model.ChangeModelTransSerial;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ChangeModelTransSerial.COLUMNS;
import com.viettel.bccs.inventory.model.QChangeModelTransSerial;

import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ChangeModelTransSerialRepoImpl implements ChangeModelTransSerialRepoCustom {

    public static final Logger logger = Logger.getLogger(ChangeModelTransSerialRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QChangeModelTransSerial changeModelTransSerial = QChangeModelTransSerial.changeModelTransSerial;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CHANGEMODELTRANSDETAILID:
                        expression = DbUtil.createLongExpression(changeModelTransSerial.changeModelTransDetailId, filter);
                        break;
                    case CHANGEMODELTRANSSERIALID:
                        expression = DbUtil.createLongExpression(changeModelTransSerial.changeModelTransSerialId, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(changeModelTransSerial.createDate, filter);
                        break;
                    case FROMSERIAL:
                        expression = DbUtil.createStringExpression(changeModelTransSerial.fromSerial, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(changeModelTransSerial.quantity, filter);
                        break;
                    case TOSERIAL:
                        expression = DbUtil.createStringExpression(changeModelTransSerial.toSerial, filter);
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
    public List<ChangeModelTransSerialDTO> findModelTransSerialByChangeModelTransDetailId(Long changeModelTransDetailId) throws Exception {
        List<ChangeModelTransSerialDTO> lstChangeModelTransSerials = Lists.newArrayList();
        StringBuilder queryString = new StringBuilder("");
        queryString.append("SELECT CHANGE_MODEL_TRANS_SERIAL_ID, " +
                "CHANGE_MODEL_TRANS_DETAIL_ID, " +
                "FROM_SERIAL, " +
                "TO_SERIAL, " +
                "QUANTITY, " +
                "CREATE_DATE" +
                "  FROM bccs_im.change_model_trans_serial a " +
                " WHERE change_model_trans_detail_id = #changeModelTransDetailId ");
        Query query = em.createNativeQuery(queryString.toString());
        query.setParameter("changeModelTransDetailId", changeModelTransDetailId);
        List<Object[]> lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            for (Object[] objects : lst) {
                ChangeModelTransSerialDTO changeModelTransSerialDTO = new ChangeModelTransSerialDTO();
                int index = 0;
                changeModelTransSerialDTO.setChangeModelTransSerialId(DataUtil.safeToLong(objects[index++]));
                changeModelTransSerialDTO.setChangeModelTransDetailId(DataUtil.safeToLong(objects[index++]));
                changeModelTransSerialDTO.setFromSerial(DataUtil.safeToString(objects[index++]));
                changeModelTransSerialDTO.setToSerial(DataUtil.safeToString(objects[index++]));
                changeModelTransSerialDTO.setQuantity(DataUtil.safeToLong(objects[index++]));
                changeModelTransSerialDTO.setCreateDate(DataUtil.defaultIfNull((Date) objects[index++], new Date()));
                lstChangeModelTransSerials.add(changeModelTransSerialDTO);
            }
        }
        return lstChangeModelTransSerials;
    }
}
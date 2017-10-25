package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.NumberActionDTO;
import com.viettel.bccs.inventory.dto.SearchNumberRangeDTO;
import com.viettel.bccs.inventory.model.NumberAction.COLUMNS;
import com.viettel.bccs.inventory.model.QNumberAction;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.eclipse.persistence.config.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NumberActionRepoImpl implements NumberActionRepoCustom {

    public static final Logger logger = Logger.getLogger(NumberActionRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QNumberAction numberAction = QNumberAction.numberAction;
        BooleanExpression result = BooleanTemplate.create("1 = 1 AND numberAction.fromIsdn IS NOT NULL AND numberAction.toIsdn IS NOT NULL ");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case FROMTOISDN:
                        Long[] fromToIsdn = (Long[]) filter.getValue();
                        StringBuilder builderFromTo = new StringBuilder("(numberAction.fromIsdn <= ");
                        builderFromTo.append(fromToIsdn[1]);
                        builderFromTo.append(" OR numberAction.fromIsdn IS NULL) AND (numberAction.toIsdn >=  ");
                        builderFromTo.append(fromToIsdn[0]);
                        builderFromTo.append(" OR numberAction.toIsdn IS NULL)");
                        expression = BooleanTemplate.create(builderFromTo.toString());
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(numberAction.status, filter);
                        break;
                    case SERVICETYPE:
                        expression = DbUtil.createStringExpression(numberAction.serviceType, filter);
                        break;
                    case ACTIONTYPE:
                        expression = DbUtil.createStringExpression(numberAction.actionType, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(numberAction.createDate, filter);
                        break;
                    case FROMISDN:
                        if (filter.getOperator().equals(FilterRequest.Operator.GOE) && filter.getValue() instanceof Long) {
                            StringBuilder builderFrom = new StringBuilder(" numberAction.fromIsdn >= ");
                            builderFrom.append(filter.getValue());
                            expression = BooleanTemplate.create(builderFrom.toString());
                        } else {
                            expression = DbUtil.createStringExpression(numberAction.fromIsdn, filter);
                        }
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(numberAction.note, filter);
                        break;
                    case NUMBERACTIONID:
                        expression = DbUtil.createLongExpression(numberAction.numberActionId, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(numberAction.telecomServiceId, filter);
                        break;
                    case REASONID:
                        expression = DbUtil.createLongExpression(numberAction.reasonId, filter);
                        break;
                    case TOISDN:
                        if (filter.getOperator().equals(FilterRequest.Operator.LOE) && filter.getValue() instanceof Long) {
                            StringBuilder builderTo = new StringBuilder(" numberAction.toIsdn <= ");
                            builderTo.append(filter.getValue());
                            expression = BooleanTemplate.create(builderTo.toString());
                        } else {
                            expression = DbUtil.createStringExpression(numberAction.toIsdn, filter);
                        }
                        break;
                    case USERCREATE:
                        expression = DbUtil.createStringExpression(numberAction.userCreate, filter);
                        break;
                    case USERIPADDRESS:
                        expression = DbUtil.createStringExpression(numberAction.userIpAddress, filter);
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
    public Boolean checkOverlap(Long fromIsdn, Long toIsdn, String telecomServiceId) throws Exception {
//        StringBuilder sql = new StringBuilder("SELECT   1  FROM   stock_number  a WHERE a.telecom_service_id=:telecomServiceId AND (a.isdn >= :fromIsdn AND a.isdn <= :toIsdn) AND a.status='1'");
//        a.telecom_service_id=:telecomServiceId AND
        StringBuilder sql = new StringBuilder("SELECT   1  FROM   Number_Action  a WHERE action_type=1 AND (a.from_isdn <= #toIsdn OR a.from_isdn IS NULL) AND (a.to_isdn >= #fromIsdn OR a.to_isdn IS NULL) AND a.from_isdn IS NOT NULL AND a.to_isdn IS NOT NULL ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("fromIsdn", fromIsdn);
        query.setParameter("toIsdn", toIsdn);
//        query.setParameter("telecomServiceId", telecomServiceId);
        List lst = query.getResultList();
        return DataUtil.isNullOrEmpty(lst) ? true : false;
    }

    public List<NumberActionDTO> search(SearchNumberRangeDTO searchDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        List lstParameter = new ArrayList();
        sql.append(" select number_action_id,telecom_service_id,action_type,from_isdn,to_isdn,note,");
        sql.append(" reason_id,user_create,user_ip_address,create_date,service_type,status");
        sql.append(" from number_action where 1 = 1 and action_type = 1 ");
        if (!DataUtil.isNullObject(searchDTO.getStartRange()) && !DataUtil.isNullObject(searchDTO.getEndRange())) {
            Long[] fromToIsdn = new Long[2];
            fromToIsdn[0] = Long.valueOf((!DataUtil.isNullObject(searchDTO.getPstnCode()) ? searchDTO.getPstnCode() : "") + searchDTO.getStartRange());
            fromToIsdn[1] = Long.valueOf((!DataUtil.isNullObject(searchDTO.getPstnCode()) ? searchDTO.getPstnCode() : "") + searchDTO.getEndRange());
            sql.append(" AND (from_Isdn <= #P").append(lstParameter.size());
            lstParameter.add(fromToIsdn[1]);
            sql.append(" OR from_Isdn IS NULL) AND (to_Isdn >= #P").append(lstParameter.size());
            lstParameter.add(fromToIsdn[0]);
            sql.append(" OR to_Isdn IS NULL)");
        } else if (!DataUtil.isNullObject(searchDTO.getStartRange())) {
            sql.append(" AND TO_NUMBER(from_Isdn) >= #P").append(lstParameter.size());
            lstParameter.add(searchDTO.getStartRange());
        } else if (!DataUtil.isNullObject(searchDTO.getEndRange())) {
            sql.append(" AND TO_NUMBER(to_Isdn) <= #P").append(lstParameter.size());
            lstParameter.add(searchDTO.getEndRange());
        }
        if (!DataUtil.isNullObject(searchDTO.getServiceType())) {
            sql.append(" AND telecom_service_id = #P").append(lstParameter.size());
            lstParameter.add(DataUtil.safeToLong(searchDTO.getServiceType()));
        }
        if (!DataUtil.isNullObject(searchDTO.getPstnCode())) {
            sql.append(" AND from_isdn like #P").append(lstParameter.size());
            lstParameter.add(searchDTO.getPstnCode() + "%");
        }
        if (!DataUtil.isNullObject(searchDTO.getPlanningType())) {
            sql.append(" AND service_type = #P").append(lstParameter.size());
            lstParameter.add(searchDTO.getPlanningType());
        }
        if (!DataUtil.isNullObject(searchDTO.getStatus())) {
            sql.append(" AND status = #P").append(lstParameter.size());
            lstParameter.add(searchDTO.getStatus());
        }
        Query query = em.createNativeQuery(sql.toString());
        for (int i = 0; i < lstParameter.size(); i++) {
            query.setParameter("P" + i, lstParameter.get(i));
        }
        List<Object[]> objects = query.getResultList();
        List<NumberActionDTO> result = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(objects)) {
            for (Object[] object : objects) {
                NumberActionDTO numberActionDTO = new NumberActionDTO();
                int index = 0;
                numberActionDTO.setNumberActionId(DataUtil.safeToLong(object[index++]));
                numberActionDTO.setTelecomServiceId(DataUtil.safeToLong(object[index++]));
                numberActionDTO.setActionType(DataUtil.safeToString(object[index++]));
                numberActionDTO.setFromIsdn(DataUtil.safeToString(object[index++]));
                numberActionDTO.setToIsdn(DataUtil.safeToString(object[index++]));
                numberActionDTO.setNote(DataUtil.safeToString(object[index++]));
                numberActionDTO.setReasonId(DataUtil.safeToLong(object[index++]));
                numberActionDTO.setUserCreate(DataUtil.safeToString(object[index++]));
                numberActionDTO.setUserIpAddress(DataUtil.safeToString(object[index++]));
                Object createDate = object[index++];
                numberActionDTO.setCreateDate(createDate != null ? (Date) createDate : null);
                numberActionDTO.setServiceType(DataUtil.safeToString(object[index++]));
                numberActionDTO.setStatus(DataUtil.safeToString(object[index]));
                result.add(numberActionDTO);
            }
        }
        return result;
    }
}
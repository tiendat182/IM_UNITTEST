package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.dto.DeviceConfigDTO;
import com.viettel.bccs.inventory.model.DeviceConfig;
import com.viettel.bccs.inventory.model.QDeviceConfig;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


public class DeviceConfigRepoImpl implements DeviceConfigRepoCustom {

    @PersistenceContext(unitName = "BCCS_IM")
    private EntityManager em;
    public static final Logger logger = Logger.getLogger(DeviceConfigRepoCustom.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDeviceConfig deviceConfig = QDeviceConfig.deviceConfig;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                DeviceConfig.COLUMNS column = DeviceConfig.COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(deviceConfig.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(deviceConfig.createUser, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(deviceConfig.id, filter);
                        break;
                    case NEWPRODOFFERID:
                        expression = DbUtil.createLongExpression(deviceConfig.newProdOfferId, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(deviceConfig.prodOfferId, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(deviceConfig.stateId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(deviceConfig.status, filter);
                        break;
                    case UPDATEDATE:
                        expression = DbUtil.createDateExpression(deviceConfig.updateDate, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(deviceConfig.updateUser, filter);
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
    public List<DeviceConfigDTO> searchDeviceByProductOfferIdAndStateId(Long productOfferId, Long stateId) throws Exception {
        ArrayList params = new ArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select c.name AS offer_type_name,b.code,b.name offer_name,");
        strQuery.append("(SELECT osv.name FROM option_set os, option_set_value osv WHERE  os.code = 'GOODS_STATE' AND os.id = osv.option_set_id AND osv.VALUE = TO_CHAR (a.state_id)) AS state");
        strQuery.append(", b.prod_offer_id, a.state_id FROM (SELECT   DISTINCT prod_offer_id, state_id FROM device_config where status <> 2) a, ");
        strQuery.append(" product_offering b, product_offer_type c WHERE a.prod_offer_id = b.prod_offer_id ");
        strQuery.append(" AND b.product_offer_type_id = c.product_offer_type_id ");
        strQuery.append(" AND c.product_offer_type_id = 7 ");
        if (!DataUtil.isNullObject(productOfferId)) {
            strQuery.append(" and b.prod_offer_id = ? ");
            params.add(productOfferId);
        }
        if (!DataUtil.isNullObject(stateId)) {
            strQuery.append(" and a.state_id= ? ");
            params.add(stateId);
        }
        strQuery.append(" and b.status <> 2 ");

        Query query = em.createNativeQuery(strQuery.toString());
        for (int list = 0; list < params.size(); list++) {
            query.setParameter(list + 1, params.get(list));
        }
        List<DeviceConfigDTO> deviceConfigDTOS = new ArrayList<>();
        List<Object[]> results = DataUtil.defaultIfNull(query.getResultList(), new ArrayList());
        for (Object[] obj : results) {
            int index = 0;
            DeviceConfigDTO dto = new DeviceConfigDTO();
            dto.setOfferTypeName(DataUtil.safeToString(obj[index++]));
            dto.setProductOfferCode(DataUtil.safeToString(obj[index++]));
            dto.setProductOfferName(DataUtil.safeToString(obj[index++]));
            dto.setStateName(DataUtil.safeToString(obj[index++]));
            dto.setProdOfferId(DataUtil.safeToLong(obj[index++]));
            dto.setStateId(DataUtil.safeToLong(obj[index]));
            deviceConfigDTOS.add(dto);
        }

        return deviceConfigDTOS;
    }

    @Override
    public boolean checkProductIdConfig(Long id, Long probOfferId, Long stateId, Long newProbOfferId) throws Exception {
        String sql = "SELECT   count (*)\n" +
                "  FROM   device_config\n" +
                " WHERE   prod_offer_id = #probOfferId AND state_id = #stateId and new_prod_offer_id = #newProbOfferId\n" +
                "and status <> 2\n";
        if(id != null)
            sql += " and id <> #id";
        Query query = em.createNativeQuery(sql);
        query.setParameter("probOfferId", probOfferId);
        query.setParameter("stateId", stateId);
        query.setParameter("newProbOfferId", newProbOfferId);
        if(id != null)
            query.setParameter("id", id);
        Long count = DataUtil.safeToLong(query.getSingleResult());
        return count > 0 ? true : false;
    }

    @Override
    public boolean checkProductIsConfigWithState(Long probOfferId, Long stateId) throws Exception {
        return checkProductIsConfigWithState(probOfferId, stateId, false);
    }

    @Override
    public boolean checkProductIsConfigWithState(Long probOfferId, Long stateId, boolean isUpdate) throws Exception {
        String sql = "SELECT   count (*)\n" +
                "  FROM   device_config\n" +
                " WHERE   prod_offer_id = #probOfferId AND state_id = #stateId " +
                "and status <> 2\n";
        Query query = em.createNativeQuery(sql);
        query.setParameter("probOfferId", probOfferId);
        query.setParameter("stateId", stateId);

        Long count = DataUtil.safeToLong(query.getSingleResult());
        if(isUpdate)
            return count > 1 ? true : false;
        return count > 0 ? true : false;
    }

    @Override
    public boolean checkCanDelete(Long probOfferId, Long stateId) throws Exception {
        String sql = "SELECT   count (*)\n" +
                "  FROM   stock_request a,  stock_trans_detail b\n" +
                " WHERE   a.stock_trans_id=b.stock_trans_id\n" +
                " and a.request_type=3\n" +
                " and a.status in (0,1)\n" +
                " and b.prod_offer_id= #probOfferId and b.state_id= #stateId\n";
        Query query = em.createNativeQuery(sql);
        query.setParameter("probOfferId", probOfferId);
        query.setParameter("stateId", stateId);

        Long count = DataUtil.safeToLong(query.getSingleResult());
        return count > 0 ? false : true;
    }


    @Override
    public String getStateStr(Long stateId) {
        String sql = "SELECT   osv.name\n" +
                "            FROM   option_set os, option_set_value osv\n" +
                "           WHERE       os.code = 'GOODS_STATE'\n" +
                "                   AND os.id = osv.option_set_id\n" +
                "                   AND osv.VALUE = TO_CHAR (#stateId)\n";

        Query query = em.createNativeQuery(sql);
        query.setParameter("stateId", stateId);
        return DataUtil.safeToString(query.getSingleResult());
    }

    @Override
    public List<DeviceConfigDTO> getDeviceConfigInfo() {
        List<DeviceConfigDTO> rs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("");
/*        sql.append("SELECT (SELECT pd.CODE FROM PRODUCT_OFFERING pd WHERE pd.PROD_OFFER_ID = dc.PROD_OFFER_ID AND pd.STATUS = 1) productCode,");
        sql.append(" dc.STATE_ID as state,");
        sql.append("(SELECT pd2.CODE FROM PRODUCT_OFFERING pd2 WHERE pd2.PROD_OFFER_ID = dc.NEW_PROD_OFFER_ID AND pd2.STATUS = 1)");
        sql.append("FROM DEVICE_CONFIG dc WHERE STATUS <> 2 ORDER BY productCode, state");*/


        sql.append("SELECT ");
        sql.append("po1.NAME, ");
        sql.append("po1.CODE, ");
        sql.append("dc.STATE_ID, ");
        sql.append("(SELECT osv.NAME from OPTION_SET_VALUE osv, OPTION_SET os WHERE os.CODE = 'GOODS_STATE' AND os.STATUS = 1 AND osv.OPTION_SET_ID = os.ID AND dc.STATE_ID = to_number(osv.VALUE)) as stateName, ");
        sql.append("po2.NAME, ");
        sql.append("po2.CODE ");
        sql.append("FROM DEVICE_CONFIG dc, PRODUCT_OFFERING po1, PRODUCT_OFFERING po2 ");
        sql.append("WHERE dc.STATUS <> 2 AND dc.PROD_OFFER_ID = po1.PROD_OFFER_ID AND dc.NEW_PROD_OFFER_ID = po2.PROD_OFFER_ID  AND ");
        sql.append("po1.STATUS = 1 AND po2.STATUS = 1 ORDER BY po1.NAME, stateName ");



        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> results = DataUtil.defaultIfNull(query.getResultList(), new ArrayList());

        for(Object [] obj : results){
            DeviceConfigDTO deviceConfigDTO = new DeviceConfigDTO();
            deviceConfigDTO.setProductOfferName(DataUtil.safeToString(obj[0]));
            deviceConfigDTO.setProductOfferCode(DataUtil.safeToString(obj[1]));
            deviceConfigDTO.setStateId(DataUtil.safeToLong(obj[2]));
            deviceConfigDTO.setStateName(DataUtil.safeToString(obj[3]));
            deviceConfigDTO.setNewProdOfferName(DataUtil.safeToString(obj[4]));
            deviceConfigDTO.setNewProdOfferCode(DataUtil.safeToString(obj[5]));
            /*deviceConfigDTO.setProductOfferCode(DataUtil.safeToString(obj[0]));
            deviceConfigDTO.setStateId(DataUtil.safeToLong(obj[1]));
            deviceConfigDTO.setItemOfferName(DataUtil.safeToString(obj[2]));*/
            rs.add(deviceConfigDTO);
        }

        return rs;
    }


/*    @Override
    public List<DeviceConfig> getDeviceConfigByProductAndState(Long probOfferId, Long stateId) {
        String sql = "SELECT   a.* " +
                "  FROM   device_config a, product_offering b " +
                "  WHERE  a.new_prod_offer_id = b.prod_offer_id " +
                "         AND a.prod_offer_id = #prodOfferId " +
                "         AND a.state_id = #stateId " +
                "         and b.status = 1" +
                "         and a.status<> 2";

        Query query = em.createNativeQuery(sql, DeviceConfig.class);
        query.setParameter("prodOfferId", probOfferId);
        query.setParameter("stateId", stateId);

        return query.getResultList();
    }*/


}
package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.StockSim;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
import com.viettel.bccs.inventory.repo.StockSimRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class StockSimServiceImpl extends BaseServiceImpl implements StockSimService {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM1;

    private final BaseMapper<StockSim, StockSimDTO> mapper = new BaseMapper(StockSim.class, StockSimDTO.class);

    @Autowired
    private StockSimRepo repository;

    @Autowired
    private ProductOfferingService productOfferingService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OptionSetValueService optionSetValueService;

    public static final Logger logger = Logger.getLogger(StockSimService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockSimDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockSimDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockSimDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Override
    public SimKITDTO findStockSim(String imsi, String serial) throws Exception {
        try {
            SimKITDTO simKITDTO = null;
            if (DataUtil.isNullOrEmpty(imsi) && DataUtil.isNullOrEmpty(serial)) {
                return null;
            }
            List<StringBuilder> lstSql = Lists.newArrayList();

            StringBuilder sql = new StringBuilder(" ");
            StringBuilder sqlStockSim = new StringBuilder(" ");
            StringBuilder sqlHisStockSim = new StringBuilder(" ");
            StringBuilder sqlStockSimCusAll = new StringBuilder(" ");
            StringBuilder sqlCondition = new StringBuilder(" ");

            sql.append(" SELECT A.STOCK_MODEL_ID, A.OWNER_TYPE, A.OWNER_ID, A.STATE_ID, ");
            sql.append(" A.IMSI,");
            sql.append(" A.SERIAL,");
            sql.append(" A.PIN,");
            sql.append(" A.PUK,");
            sql.append(" A.PIN2,");
            sql.append(" A.PUK2,");
            sql.append(" TO_CHAR (A.AUC_REG_DATE, 'dd/mm/yyyy HH24:MI:SS')    AS STARTAUC,");
            sql.append(" TO_CHAR (A.AUC_REMOVE_DATE, 'dd/mm/yyyy HH24:MI:SS') AS ENDAUC,");
            sql.append(" TO_CHAR (A.HLR_REG_DATE, 'dd/mm/yyyy HH24:MI:SS')    AS STARTHLR,");
            sql.append(" TO_CHAR (A.HLR_REMOVE_DATE, 'dd/mm/yyyy HH24:MI:SS') ENDHLR,");
            sql.append(" A.STATUS,    ");
            sql.append(" A.OWNER_TYPE AS TYPEWAREHOUSE,");
            //stock_sim
            sqlStockSim.append(" A.CONNECTION_STATUS AS STATUSPOINTTOPOINT,");
            sqlStockSim.append(" A.sim_model_type as SIMMODELTYPE");
            sqlStockSim.append(" FROM BCCS_IM.STOCK_SIM A where 1=1 ");
            lstSql.add(sqlStockSim);
            //his_stock_sim
            sqlHisStockSim.append(" A.CONNECTION_STATUS AS STATUSPOINTTOPOINT,");
            sqlHisStockSim.append(" NULL as SIMMODELTYPE");
            sqlHisStockSim.append(" FROM BCCS_IM.HIS_STOCK_SIM A where 1=1 ");
            lstSql.add(sqlHisStockSim);
            //stock_sim_cus_all
            sqlStockSimCusAll.append(" NULL AS STATUSPOINTTOPOINT,");
            sqlStockSimCusAll.append(" NULL as SIMMODELTYPE");
            sqlStockSimCusAll.append(" FROM BCCS_IM.STOCK_SIM_CUS_ALL A where 1=1 ");
            lstSql.add(sqlStockSimCusAll);

            if (!DataUtil.isNullOrEmpty(imsi)) {
                sqlCondition.append(" and A.imsi = #imsi ");
            }
            if (!DataUtil.isNullObject(serial)) {
                sqlCondition.append(" and to_number(A.serial) = to_number(#serial) ");
            }


            for (int i = 0; i <= 2; i++) {
                Query query = emIM1.createNativeQuery(sql.toString() + lstSql.get(i).toString() + sqlCondition.toString());
                if (!DataUtil.isNullObject(imsi)) {
                    query.setParameter("imsi", imsi);
                }
                if (!DataUtil.isNullObject(serial)) {
                    query.setParameter("serial", serial);
                }
                query.setMaxResults(1);

                List<Object[]> list = query.getResultList();
                if (!DataUtil.isNullOrEmpty(list)) {
                    Object[] item = list.get(0);
                    simKITDTO = new SimKITDTO();
                    simKITDTO.setErrorCode("0");
                    simKITDTO.setResult(true);
                    int index = 0;
                    simKITDTO.setStockModelId(DataUtil.safeToLong(item[index++]));
                    simKITDTO.setOwnerType(DataUtil.safeToLong(item[index++]));
                    simKITDTO.setOwnerId(DataUtil.safeToLong(item[index++]));
                    simKITDTO.setStateId(DataUtil.safeToLong(item[index++]));
                    simKITDTO.setNumberIMSI(DataUtil.safeToString(item[index++]));
                    simKITDTO.setNumberSerial(DataUtil.safeToString(item[index++]));
                    simKITDTO.setNumberPIN1(DataUtil.safeToString(item[index++]));
                    simKITDTO.setNumberPUK1(DataUtil.safeToString(item[index++]));
                    simKITDTO.setNumberPIN2(DataUtil.safeToString(item[index++]));
                    simKITDTO.setNumberPUK2(DataUtil.safeToString(item[index++]));
                    simKITDTO.setStartAuC(DataUtil.safeToString(item[index++]));
                    simKITDTO.setEndAuC(DataUtil.safeToString(item[index++]));
                    simKITDTO.setStartHLR(DataUtil.safeToString(item[index++]));
                    simKITDTO.setEndHLR(DataUtil.safeToString(item[index++]));
                    simKITDTO.setStatus(DataUtil.safeToString(item[index++]));
                    simKITDTO.setTypeWarehouse(DataUtil.safeToString(item[index++]));
                    simKITDTO.setStatusPointToPoint(DataUtil.safeToString(item[index++]));
                    simKITDTO.setSimModelType(DataUtil.safeToString(item[index]));
                    break;
                }
            }

            if (!DataUtil.isNullObject(simKITDTO)) {
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(simKITDTO.getStockModelId());
                simKITDTO.setIdOrder(productOfferingDTO.getCode());
                simKITDTO.setNameOrder(productOfferingDTO.getName());
                MvShopStaffDTO mvShopStaffDTO = shopService.getMViewShopStaff(simKITDTO.getOwnerType(), simKITDTO.getOwnerId());
                if (!DataUtil.isNullObject(mvShopStaffDTO)) {
                    simKITDTO.setIdWarehouse(mvShopStaffDTO.getOwnerCode());
                    simKITDTO.setNameWarehouse(mvShopStaffDTO.getOwnerName());
                    simKITDTO.setStaffName(mvShopStaffDTO.getContactName());
                    simKITDTO.setStaffMobile(mvShopStaffDTO.getContactMobile());
                    simKITDTO.setProvinceName(mvShopStaffDTO.getProvinceName());
                    simKITDTO.setDistrictName(mvShopStaffDTO.getDistrictName());
                }
                List<OptionSetValueDTO> lstOptionSetValueDTOs = optionSetValueService.getByOptionsetCodeAndValue("GOOD_STATE", DataUtil.safeToString(simKITDTO.getStateId()));
                if (!DataUtil.isNullOrEmpty(lstOptionSetValueDTOs)) {
                    simKITDTO.setStatusOrder(lstOptionSetValueDTOs.get(0).getName());
                }
            }

            return simKITDTO;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happen");
        }
    }

    @WebMethod
    @Override
    public SimKITDTO findStockKit(@WebParam(name = "serial") String serial) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  A.IMSI,\n" +
                "         A.SERIAL,\n" +
                "         A.PIN,\n" +
                "         A.PUK,\n" +
                "         A.PIN2,\n" +
                "         A.PUK2,\n" +
                "         TO_CHAR (A.AUC_REG_DATE, 'dd/mm/yyyy HH24:MI:SS') AS STARTAUC,\n" +
                "         TO_CHAR (A.AUC_REMOVE_DATE, 'dd/mm/yyyy HH24:MI:SS') AS ENDAUC,\n" +
                "         TO_CHAR (A.HLR_REG_DATE, 'dd/mm/yyyy HH24:MI:SS') AS STARTHLR,\n" +
                "         TO_CHAR (A.HLR_REMOVE_DATE, 'dd/mm/yyyy HH24:MI:SS') ENDHLR,\n" +
                "         A.STATUS,\n" +
                "         A.CONNECTION_STATUS AS STATUSPOINTTOPOINT,\n" +
                "         (SELECT   C.NAME\n" +
                "            FROM   BCCS_IM.OPTION_SET_VALUE C\n" +
                "           WHERE   C.OPTION_SET_ID = (SELECT   D.ID\n" +
                "                                        FROM   BCCS_IM.OPTION_SET D\n" +
                "                                       WHERE   D.CODE = 'GOOD_STATE')\n" +
                "                   AND C.VALUE = TO_CHAR (A.STATE_ID))\n" +
                "             AS STATUSORDER,\n" +
                "         (SELECT   E.CODE\n" +
                "            FROM   BCCS_IM.PRODUCT_OFFERING E\n" +
                "           WHERE   E.PROD_OFFER_ID = A.PROD_OFFER_ID)\n" +
                "             AS IDORDER,\n" +
                "         (SELECT   F.NAME\n" +
                "            FROM   BCCS_IM.PRODUCT_OFFERING F\n" +
                "           WHERE   F.PROD_OFFER_ID = A.PROD_OFFER_ID)\n" +
                "             AS NAMEORDER,\n" +
                "         A.OWNER_TYPE AS TYPEWAREHOUSE,\n" +
                "         DECODE (A.OWNER_TYPE,\n" +
                "                 1,\n" +
                "                 (SELECT   SHOP_CODE\n" +
                "                    FROM   BCCS_IM.SHOP\n" +
                "                   WHERE   SHOP_ID = A.OWNER_ID),\n" +
                "                 2,\n" +
                "                 (SELECT   STAFF_CODE\n" +
                "                    FROM   BCCS_IM.STAFF\n" +
                "                   WHERE   STAFF_ID = A.OWNER_ID))\n" +
                "             AS IDWAREHOUSE,\n" +
                "         DECODE (A.OWNER_TYPE,\n" +
                "                 1,\n" +
                "                 (SELECT   NAME\n" +
                "                    FROM   BCCS_IM.SHOP\n" +
                "                   WHERE   SHOP_ID = A.OWNER_ID),\n" +
                "                 2,\n" +
                "                 (SELECT   NAME\n" +
                "                    FROM   BCCS_IM.STAFF\n" +
                "                   WHERE   STAFF_ID = A.OWNER_ID))\n" +
                "             AS NAMEWAREHOUSE,\n" +
                "         DECODE (A.OWNER_TYPE,\n" +
                "                 1,\n" +
                "                 (SELECT   CONTACT_NAME\n" +
                "                    FROM   BCCS_IM.SHOP\n" +
                "                   WHERE   SHOP_ID = A.OWNER_ID),\n" +
                "                 2,\n" +
                "                 (SELECT   NAME\n" +
                "                    FROM   BCCS_IM.STAFF\n" +
                "                   WHERE   STAFF_ID = A.OWNER_ID))\n" +
                "             AS CONTACT_NAME,\n" +
                "         DECODE (A.OWNER_TYPE,\n" +
                "                 1,\n" +
                "                 (SELECT   TEL\n" +
                "                    FROM   BCCS_IM.SHOP\n" +
                "                   WHERE   SHOP_ID = A.OWNER_ID),\n" +
                "                 2,\n" +
                "                 (SELECT   TEL\n" +
                "                    FROM   BCCS_IM.STAFF\n" +
                "                   WHERE   STAFF_ID = A.OWNER_ID))\n" +
                "             AS CONTACT_MOBLIE,\n" +
                "(select name from bccs_im.area where area_code = (select province from bccs_im.shop where shop_id = (decode(a.owner_type,1,a.owner_id,(select shop_id from bccs_im.staff where staff_id = a.owner_id))))) as provinceName," +
                "(select area_code from bccs_im.area where area_code = (select province from bccs_im.shop where shop_id = (decode(a.owner_type,1,a.owner_id,(select shop_id from bccs_im.staff where staff_id = a.owner_id))))) as provinceCode," +
                "(select name from bccs_im.area where  area_code = \n" +
                "(select province from bccs_im.shop where shop_id = \n" +
                "(decode(a.owner_type,1,a.owner_id,(select shop_id from bccs_im.staff where staff_id = a.owner_id)))) || \n" +
                "(select district from bccs_im.shop where shop_id = \n" +
                "(decode(a.owner_type,1,a.owner_id,(select shop_id from bccs_im.staff where staff_id = a.owner_id))))) \n" +
                "as districtName,\n " +
                "(select area_code from bccs_im.area where  area_code = \n" +
                "(select province from bccs_im.shop where shop_id = \n" +
                "(decode(a.owner_type,1,a.owner_id,(select shop_id from bccs_im.staff where staff_id = a.owner_id)))) || \n" +
                "(select district from bccs_im.shop where shop_id = \n" +
                "(decode(a.owner_type,1,a.owner_id,(select shop_id from bccs_im.staff where staff_id = a.owner_id))))) \n" +
                "as districtCode\n " +
                "  FROM   BCCS_IM.STOCK_KIT A where 1=1 ");

        if (!DataUtil.isNullObject(serial)) {
            sql.append(" and to_number(serial) = to_number(#serial) ");
        }

        sql.append(" ORDER BY A.owner_type desc ");

        Query query = em.createNativeQuery(sql.toString());

        if (!DataUtil.isNullObject(serial)) {
            query.setParameter("serial", serial);
        }

        query.setMaxResults(1);

        List<Object[]> list = query.getResultList();

        if (list != null && !list.isEmpty()) {
            Object[] item = list.get(0);
            SimKITDTO simKITDTO = new SimKITDTO();
            simKITDTO.setResult(true);

            int i = 0;
            simKITDTO.setErrorCode("0");
            simKITDTO.setNumberIMSI(DataUtil.safeToString(item[i++]));
            simKITDTO.setNumberSerial(DataUtil.safeToString(item[i++]));
            simKITDTO.setNumberPIN1(DataUtil.safeToString(item[i++]));
            simKITDTO.setNumberPUK1(DataUtil.safeToString(item[i++]));
            simKITDTO.setNumberPIN2(DataUtil.safeToString(item[i++]));
            simKITDTO.setNumberPUK2(DataUtil.safeToString(item[i++]));
            simKITDTO.setStartAuC(DataUtil.safeToString(item[i++]));
            simKITDTO.setEndAuC(DataUtil.safeToString(item[i++]));
            simKITDTO.setStartHLR(DataUtil.safeToString(item[i++]));
            simKITDTO.setEndHLR(DataUtil.safeToString(item[i++]));
            simKITDTO.setStatus(DataUtil.safeToString(item[i++]));
            simKITDTO.setStatusPointToPoint(DataUtil.safeToString(item[i++]));
            simKITDTO.setStatusOrder(DataUtil.safeToString(item[i++]));
            simKITDTO.setIdOrder(DataUtil.safeToString(item[i++]));
            simKITDTO.setNameOrder(DataUtil.safeToString(item[i++]));
            simKITDTO.setTypeWarehouse(DataUtil.safeToString(item[i++]));
            simKITDTO.setIdWarehouse(DataUtil.safeToString(item[i++]));
            simKITDTO.setNameWarehouse(DataUtil.safeToString(item[i++]));
            simKITDTO.setStaffName(DataUtil.safeToString(item[i++]));
            simKITDTO.setStaffMobile(DataUtil.safeToString(item[i++]));
            simKITDTO.setProvinceName(DataUtil.safeToString(item[i++]));
            simKITDTO.setProvinceCode(DataUtil.safeToString(item[i++]));
            simKITDTO.setDistrictName(DataUtil.safeToString(item[i++]));
            simKITDTO.setDistrictCode(DataUtil.safeToString(item[i]));
            return simKITDTO;
        }

        return null;

    }

    @WebMethod
    @Override
    public StockSimDTO getSimInfor(String msisdn) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(msisdn)) {
            throw new LogicException("MSISDN_IS_EMPTY", "vtportal.validate.isdn.require");
        }

        msisdn = msisdn.trim();

        //Chuan hoa so thue bao
        if (msisdn.startsWith("0")) {
            msisdn = msisdn.substring(1);
        }

        if (msisdn.startsWith("84")) {
            msisdn = msisdn.substring(2);
        }

        String serial = repository.findSerialSimBySub(msisdn);

        if (DataUtil.isNullOrEmpty(serial)) {
            throw new LogicException("MSISDN_IS_NOTFOUND", "vtportal.validate.subscriber.invalid", msisdn);
        }

        StockSimDTO stockSim = repository.findStockSim(serial);

        if (stockSim == null) {
            throw new LogicException("SERIAL_SIM_IS_NOTFOUND", "vtportal.validate.serial.invalid", serial, msisdn);
        }

        return stockSim;
    }

    public boolean isCaSim(String serial) {
        return repository.isCaSim(serial);
    }

    @Override
    @WebMethod
    public boolean checkSimElegibleExists(String fromSerial, String toSerial) throws Exception, LogicException {
        if (repository.checkSimElegibleExistsIm1(fromSerial, toSerial) && repository.checkSimElegibleExists(fromSerial, toSerial))
            return true;
        return false;
    }
}

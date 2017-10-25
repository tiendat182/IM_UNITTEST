package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.service.StockIsdnIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IsdnConst;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.eclipse.persistence.config.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class StockNumberRepoImpl implements StockNumberRepoCustom {

    public static final Logger logger = Logger.getLogger(StockNumberRepoCustom.class);
    private final BaseMapper<StockNumber, StockNumberDTO> mapper = new BaseMapper(StockNumber.class, StockNumberDTO.class);
    private final BaseMapper<NumberAction, NumberActionDTO> mapperNumberAction = new BaseMapper(NumberAction.class, NumberActionDTO.class);
    private final BaseMapper<NumberActionDetail, NumberActionDetailDTO> mapperNumberActionDetail = new BaseMapper(NumberActionDetail.class, NumberActionDetailDTO.class);
    private final BaseMapper<StockNumber, StockNumberDTO> mapperStockNumber = new BaseMapper(StockNumber.class, StockNumberDTO.class);

    @Autowired
    private NumberActionRepo numberActionRepo;

    @Autowired
    private NumberActionDetailRepo numberActionDetailRepo;

    @Autowired
    private AreaRepo areaRepo;

    @Autowired
    private ShopRepo shopRepo;

    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private StockIsdnTransRepo stockIsdnTransRepo;

    @Autowired
    private StockIsdnTransDetailRepo stockIsdnTransDetailRepo;

    @Autowired
    private ReasonRepo reasonRepo;

    @Autowired
    private StockIsdnIm1Service stockIsdnIm1Service;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em1;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockNumber stockNumber = QStockNumber.stockNumber;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                StockNumber.COLUMNS column = StockNumber.COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AREACODE:
                        expression = DbUtil.createStringExpression(stockNumber.areaCode, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockNumber.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createShortExpression(stockNumber.ownerType, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(stockNumber.createDate, filter);
                        break;
                    case FILTERRULEID:
                        expression = DbUtil.createLongExpression(stockNumber.filterRuleId, filter);
                        break;
                    case FILTERRULEIDLIST:
                        expression = stockNumber.filterRuleId.in(DataUtil.objLstToLongLst((List<Object>) filter.getValue()));
                        break;
                    case FILTERSTATUSLIST:
                        expression = stockNumber.status.in(DataUtil.objLstToStringLst((List<Object>) filter.getValue()));
                        break;
                    case HLRSTATUS:
                        expression = DbUtil.createCharacterExpression(stockNumber.hlrStatus, filter);
                        break;
                    case ID:
                        expression = DbUtil.createLongExpression(stockNumber.id, filter);
                        break;
                    case IMSI:
                        expression = DbUtil.createStringExpression(stockNumber.imsi, filter);
                        break;
                    case ISDN:
                        expression = DbUtil.createStringExpression(stockNumber.isdn, filter);
                        break;
                    case ISDNTYPE:
                        expression = DbUtil.createCharacterExpression(stockNumber.isdnType, filter);
                        break;
                    case LASTUPDATEIPADDRESS:
                        expression = DbUtil.createStringExpression(stockNumber.lastUpdateIpAddress, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(stockNumber.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(stockNumber.lastUpdateUser, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(stockNumber.prodOfferId, filter);
                        break;
                    case SERIAL:
                        expression = DbUtil.createStringExpression(stockNumber.serial, filter);
                        break;
                    case SERVICETYPE:
                        expression = DbUtil.createStringExpression(stockNumber.serviceType, filter);
                        break;
                    case STATIONCODE:
                        expression = DbUtil.createStringExpression(stockNumber.stationCode, filter);
                        break;
                    case STATIONID:
                        expression = DbUtil.createLongExpression(stockNumber.stationId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(stockNumber.status, filter);
                        break;
                    case STOCKID:
                        expression = DbUtil.createLongExpression(stockNumber.stockId, filter);
                        break;
                    case TELECOMSERVICEID:
                        expression = DbUtil.createLongExpression(stockNumber.telecomServiceId, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(stockNumber.updateDatetime, filter);
                        break;
                    case USERCREATE:
                        expression = DbUtil.createStringExpression(stockNumber.userCreate, filter);
                        break;
                    case LOCKBYSTAFF:
                        expression = DbUtil.createLongExpression(stockNumber.lockByStaff, filter);
                        break;
                }
                if (expression != null) {
                    result = result.and(expression);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.info("Result Predicate :: " + (result == null ? "" : result.toString()));
        logger.info("Exiting predicates");
        return result;
    }


    @Override
    public List<StockNumberDTO> getListNumberFilter(Long telecomServiceId, BigDecimal startNumber, BigDecimal endNumber, Long minNumber, Long ownerId, List<String> lstStatus) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT   a.isdn,");
        strQuery.append("         a.filter_rule_id,");
        strQuery.append("         b.rule_order");
        strQuery.append("  FROM   stock_number a, number_filter_rule b");
        strQuery.append(" WHERE   1 = 1");
        strQuery.append("   AND a.filter_rule_id = b.filter_rule_id(+)");
        strQuery.append("   AND to_number(a.isdn) >= ?");
        strQuery.append("   AND to_number(a.isdn) <= ?");
        strQuery.append("   AND a.telecom_service_id= ?");
        strQuery.append("   AND (a.filter_rule_id IS NULL OR b.rule_order >= ?)");
        if (!DataUtil.isNullOrZero(ownerId)) {
            strQuery.append(" AND a.owner_id = ? ");   //neu chon kho
        }
        if (!DataUtil.isNullOrEmpty(lstStatus)) {
            strQuery.append(" AND (a.status = ? ");
            for (int i = 1; i < lstStatus.size(); i++) {
                strQuery.append(" OR a.status = ? ");
            }
            strQuery.append(" ) ");
        }
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter(1, startNumber);
        query.setParameter(2, endNumber);
        query.setParameter(3, telecomServiceId);
        query.setParameter(4, minNumber);
        if ((!DataUtil.isNullOrZero(ownerId)) && (DataUtil.isNullOrEmpty(lstStatus))) {
            query.setParameter(5, ownerId);   //neu chon kho
        }
        if ((DataUtil.isNullOrZero(ownerId)) && (!DataUtil.isNullOrEmpty(lstStatus))) {
            query.setParameter(5, lstStatus.get(0));
            for (int i = 1; i < lstStatus.size(); i++) {
                query.setParameter(i + 5, lstStatus.get(i));
            }
        }
        if ((!DataUtil.isNullOrZero(ownerId)) && (!DataUtil.isNullOrEmpty(lstStatus))) {
            query.setParameter(5, ownerId);   //neu chon kho
            query.setParameter(6, lstStatus.get(0));// neu chon bat ki 1 status
            for (int i = 1; i < lstStatus.size(); i++) {
                query.setParameter(i + 6, lstStatus.get(i));
            }
        }

        List<StockNumberDTO> result = Lists.newArrayList();
        List listResult = query.getResultList();
        for (Object o : listResult) {
            Object[] arrNumberRule = (Object[]) o;
            StockNumberDTO stockNumberDTO = new StockNumberDTO();
            stockNumberDTO.setIsdn(DataUtil.safeToString(arrNumberRule[0]));
            stockNumberDTO.setFilterRuleId(DataUtil.safeToLong(arrNumberRule[1]));
            stockNumberDTO.setRuleOrder(DataUtil.safeToLong(arrNumberRule[2]));
            result.add(stockNumberDTO);
        }

        return result;
    }

    @Override
    public List<TableNumberRangeDTO> getListRangeGrant(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        List<TableNumberRangeDTO> lstTableResult = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        List params = new ArrayList();
        sql.append("SELECT s.shop_code," +
                " s.name," +
                " sn.telecom_service_id," +
                " sn.mn from_isdn," +
                " sn.mx to_isdn," +
                " sn.so_luong," +
                " sn.status," +
                " rownum, " +
                " s.shop_id " +
                " FROM   (  SELECT   MIN (isdn) mn," +
                " MAX (isdn) mx," +
                " status," +
                " telecom_service_id," +
                " owner_id," +
                " COUNT ( * ) AS so_luong" +
                " FROM   ( SELECT   isdn, " +
                " status, " +
                " telecom_service_id, " +
                " owner_id, " +
                " (isdn - ROW_NUMBER () " +
                " OVER ( " +
                " ORDER BY " +
                " s.telecom_service_id, " +
                " s.owner_id, " +
                " s.status, " +
                " s.isdn)) dif from stock_number s ");
        if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getStartRange())) {
            sql.append(" WHERE   to_number(isdn) >= ? ");
            params.add(Long.parseLong(searchNumberRangeDTO.getStartRange()));
        }
        if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getEndRange())) {
            sql.append(" AND   to_number(isdn) <= ? ");
            params.add(Long.parseLong(searchNumberRangeDTO.getEndRange()));
        }
        if (DataUtil.safeToLong(searchNumberRangeDTO.getServiceType()) > 0) {
            sql.append(" AND   telecom_service_id = ? ");
            params.add(DataUtil.safeToLong(searchNumberRangeDTO.getServiceType()));
        }
        if (!DataUtil.isNullOrZero(searchNumberRangeDTO.getShopId())) {
            sql.append(" AND  owner_id = ? AND owner_type = ? ");
            params.add(searchNumberRangeDTO.getShopId());
            params.add(new Long(1));
        }
        if (DataUtil.isNullOrZero(searchNumberRangeDTO.getOldStatus())) {
            sql.append(" AND status in (?,?) ");
            params.add(new Long(1));
            params.add(new Long(3));
        } else {
            sql.append(" AND status in (?) ");
            params.add(searchNumberRangeDTO.getOldStatus());
        }
        sql.append(") GROUP BY status, telecom_service_id, owner_id, dif) sn, shop s where sn.owner_id = s.shop_id order by from_isdn ");

        Query query = em.createNativeQuery(sql.toString());
        for (int idx = 0; idx < params.size(); idx++) {
            query.setParameter(idx + 1, params.get(idx));
        }

        List listResult = query.getResultList();

        for (Object o : listResult) {
            Object[] obj = (Object[]) o;
            TableNumberRangeDTO tableItem = new TableNumberRangeDTO();
            tableItem.setShopCode(DataUtil.safeToString(obj[0]));
            tableItem.setShopName(DataUtil.safeToString(obj[1]));
            tableItem.setTelecomService(DataUtil.safeToString(obj[2]));
            tableItem.setFromISDN(DataUtil.safeToString(obj[3]));
            tableItem.setToISDN(DataUtil.safeToString(obj[4]));
            tableItem.setTotal(DataUtil.safeToLong(obj[5]));
//            tableItem.setStatus(searchNumberRangeDTO.getStatus());
            tableItem.setOldStatus(DataUtil.safeToString(obj[6]));
            tableItem.setRowKey(DataUtil.safeToString(obj[7]));
            tableItem.setShopId(DataUtil.safeToLong(obj[8]));
            lstTableResult.add(tableItem);
        }
        return lstTableResult;
    }

    @Override
    public List<TableNumberRangeDTO> getListRangeGrantFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        List<TableNumberRangeDTO> lstTableResult = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(searchNumberRangeDTO.getMapISDN())) {
            return lstTableResult;
        }
        StringBuilder sql = new StringBuilder();
        List params = new ArrayList();
        sql.append("SELECT s.shop_code," +
                " s.name," +
                " sn.telecom_service_id," +
                " sn.mn from_isdn," +
                " sn.mx to_isdn ," +
                " sn.so_luong," +
                " sn.status," +
                " rownum, " +
                " s.shop_id " +
                " FROM   (  SELECT   isdn mn," +
                " isdn mx," +
                " status," +
                " telecom_service_id," +
                " owner_id," +
                " 1 AS so_luong" +
                " FROM   (SELECT   isdn, " +
                " status, " +
                " telecom_service_id, " +
                " owner_id, " +
                " (isdn - ROWNUM) dif from stock_number ");
        if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getMapISDN())) {
            sql.append(" WHERE   to_number(isdn) in (");
            Iterator<String> ite = searchNumberRangeDTO.getMapISDN().keySet().iterator();
            StringBuilder paramStr = new StringBuilder("");
            while (ite.hasNext()) {
                String isdn = ite.next();
                paramStr.append("?,");
                params.add(Long.parseLong(isdn));
            }
            sql.append(paramStr.substring(0, (paramStr.length() - 1))).append(")");
            //params.add(searchNumberRangeDTO.getIsdnParam());
        }

        if (DataUtil.safeToLong(searchNumberRangeDTO.getServiceType()) > 0) {
            sql.append(" AND   telecom_service_id = ? ");
            params.add(DataUtil.safeToLong(searchNumberRangeDTO.getServiceType()));
        }
        if (!DataUtil.isNullOrZero(searchNumberRangeDTO.getShopId())) {
            sql.append(" AND  owner_id = ? AND owner_type = ? ");
            params.add(searchNumberRangeDTO.getShopId());
            params.add(new Long(1));
        }
        sql.append(" AND status in (?,?) ");
        params.add(new Long(1));
        params.add(new Long(3));
        sql.append(" ) tmp) sn, shop s where sn.owner_id = s.shop_id order by from_isdn ");
//        sql.append(" ) tmp GROUP BY status, telecom_service_id, owner_id, dif) sn, shop s where sn.owner_id = s.shop_id order by name ");

        Query query = em.createNativeQuery(sql.toString());
        for (int idx = 0; idx < params.size(); idx++) {
            query.setParameter(idx + 1, params.get(idx));
        }

        List listResult = query.getResultList();

        for (Object o : listResult) {
            Object[] obj = (Object[]) o;
            TableNumberRangeDTO tableItem = new TableNumberRangeDTO();
            tableItem.setShopCode(DataUtil.safeToString(obj[0]));
            tableItem.setShopName(DataUtil.safeToString(obj[1]));
            tableItem.setTelecomService(DataUtil.safeToString(obj[2]));
            tableItem.setFromISDN(DataUtil.safeToString(obj[3]));
            tableItem.setToISDN(DataUtil.safeToString(obj[4]));
            tableItem.setTotal(DataUtil.safeToLong(obj[5]));
            tableItem.setOldStatus(DataUtil.safeToString(obj[6]));
            if (searchNumberRangeDTO.getMapISDN() != null && searchNumberRangeDTO.getMapISDN().get(tableItem.getFromISDN()) != null) {
                tableItem.setStatus(searchNumberRangeDTO.getMapISDN().get(tableItem.getFromISDN()));
            }
            tableItem.setRowKey(DataUtil.safeToString(obj[7]));
            tableItem.setShopId(DataUtil.safeToLong(obj[8]));
            lstTableResult.add(tableItem);
        }
        return lstTableResult;
    }

    @Override
    public long updateRangeGrant(List<TableNumberRangeDTO> listRangeUpdate, StaffDTO staff, String ip, SearchNumberRangeDTO searchNumberRangeDTO) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(listRangeUpdate)) {
            return 0L;
        }

        long totalUpdate = 0;
        long start = 0;
        long end = 0;
        Date sysdate = DbUtil.getSysDate(em);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");

        for (TableNumberRangeDTO range : listRangeUpdate) {
            List params = new ArrayList();
            StringBuilder sqlUpdateStockNumber = new StringBuilder("update Stock_Number set ");
            if (DataUtil.isNullOrEmpty(searchNumberRangeDTO.getStatus())) {
                sqlUpdateStockNumber.append("  status = ? ");
                params.add(range.getStatus());
            } else {
                sqlUpdateStockNumber.append("  status = ? ");
                params.add(searchNumberRangeDTO.getStatus());
            }
            if (staff != null) {
                sqlUpdateStockNumber.append(" ,last_Update_Time = ?, last_Update_User = ? ");
                params.add(sysdate);
                params.add(staff.getStaffCode());
            }
            if (ip != null) {
                sqlUpdateStockNumber.append(" ,last_Update_Ip_Address = ? ");
                params.add(ip);
            }
            if (!DataUtil.isNullOrEmpty(range.getFromISDN())) {
                start = DataUtil.safeToLong(range.getFromISDN());
                sqlUpdateStockNumber.append(" where to_number(isdn) >= ? ");
                params.add(start);
            }
            if (!DataUtil.isNullOrEmpty(range.getToISDN())) {
                end = DataUtil.safeToLong(range.getToISDN());
                sqlUpdateStockNumber.append(" and to_number(isdn) <= ? ");
                params.add(end);
            }
            if (!DataUtil.isNullOrZero(range.getShopId())) {
                sqlUpdateStockNumber.append(" and owner_Id = ? ");
                params.add(DataUtil.safeToLong(range.getShopId()));
            }

            sqlUpdateStockNumber.append(" and owner_Type = ? and status in (?,?) ");
            params.add(new Long(1));
            params.add("1");
            params.add("3");

            String bccs1Query = sqlUpdateStockNumber.toString();
            List bccs1Params = Lists.newArrayList(params);

            if (DataUtil.safeToLong(range.getTelecomService()) > 0) {
                sqlUpdateStockNumber.append(" AND   telecom_service_id = ? ");
                params.add(DataUtil.safeToLong(range.getTelecomService()));
            }

            Query query = em.createNativeQuery(sqlUpdateStockNumber.toString());
            for (int idx = 0; idx < params.size(); idx++) {
                query.setParameter(idx + 1, params.get(idx));
            }
            long updated = query.executeUpdate();
            totalUpdate += updated;

            //Cap nhat BCCS1
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                    && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                    && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                updateStatusStockNumberBCCS1(bccs1Params, bccs1Query, end - start + 1, range.getTelecomService());
            }
            //insert log number action
            if (updated == (end - start + 1)) {
                NumberActionDTO numberActionDTO = new NumberActionDTO();
                numberActionDTO.setActionType(IsdnConst.ISDN_ACTION_TYPE_STATUS);
                numberActionDTO.setTelecomServiceId(Long.parseLong(range.getTelecomService()));
                numberActionDTO.setFromIsdn(range.getFromISDN());
                numberActionDTO.setToIsdn(range.getToISDN());
                numberActionDTO.setCreateDate(DbUtil.getSysDate(em));
                numberActionDTO.setUserCreate(staff.getStaffCode());
                numberActionDTO.setUserIpAddress(ip);
                NumberAction numberAction = numberActionRepo.save(mapperNumberAction.toPersistenceBean(numberActionDTO));
                //insert number action detail
                NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
                numberActionDetailDTO.setNumberActionId(numberAction.getNumberActionId());
                numberActionDetailDTO.setColumnName("STATUS");
                numberActionDetailDTO.setNewValue(range.getStatus());
                numberActionDetailDTO.setOldValue(range.getOldStatus());
                numberActionDetailDTO.setNewDetailValue(range.getStatusName());
                numberActionDetailDTO.setOldDetailValue(range.getOldStatusName());
                numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(numberActionDetailDTO));
            } else {
                throw new LogicException("", "mn.stock.status.isdn.range.change");
            }
        }
        return totalUpdate;
    }

    @Override
    public List<String> checkListStockNumber(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {

        List<String> listISDNNotOK = new ArrayList<String>();
        List<String> listISDNOK = new ArrayList<String>();
        StringBuilder sql = new StringBuilder("select isdn from stock_number ");
        List params = new ArrayList();
        if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getMapISDN())) {
            sql.append(" WHERE   to_number(isdn) in (");
            Iterator<String> ite = searchNumberRangeDTO.getMapISDN().keySet().iterator();
            StringBuilder paramStr = new StringBuilder("");
            while (ite.hasNext()) {
                String isdn = ite.next();
                paramStr.append("?,");
                params.add(DataUtil.safeToLong(isdn));
            }
            sql.append(paramStr.substring(0, (paramStr.length() - 1))).append(") and status in (?, ?) ");
            params.add(new Long(1));
            params.add(new Long(3));
            if (!DataUtil.isNullOrZero(searchNumberRangeDTO.getShopId())) {
                sql.append(" and owner_type = ? and owner_id = ? ");
                params.add(new Long(1));
                params.add(searchNumberRangeDTO.getShopId());
            }
            if (DataUtil.safeToLong(searchNumberRangeDTO.getServiceType()) > 0) {
                sql.append(" and telecom_service_id = ? ");
                params.add(DataUtil.safeToLong(searchNumberRangeDTO.getServiceType()));
            }
            //params.add(searchNumberRangeDTO.getIsdnParam());
            Query query = em.createNativeQuery(sql.toString());
            for (int idx = 0; idx < params.size(); idx++) {
                query.setParameter(idx + 1, params.get(idx));
            }

            List listResult = query.getResultList();

            for (Object o : listResult) {
                String s = (String) o;
                String isdn = DataUtil.safeToString(s);
                listISDNOK.add(isdn);
            }
        }
        if (DataUtil.isNullOrEmpty(listISDNOK)) {
            if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getMapISDN())) {
                listISDNNotOK.addAll(searchNumberRangeDTO.getMapISDN().keySet());
            }
        } else {
            listISDNNotOK = (ArrayList<String>) CollectionUtils.subtract(searchNumberRangeDTO.getMapISDN().keySet(), listISDNOK);
        }
        return listISDNNotOK;
    }

    @Override
    public List<TableNumberRangeDTO> previewFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        try {
            List<TableNumberRangeDTO> lstTableResult = Lists.newArrayList();
            StringBuilder sql = new StringBuilder();
            List params = new ArrayList();
            sql.append("SELECT   ss.from_isdn, ss.to_isdn, ss.total,  ss.telecom_service_id," +
                    "         ss.shop_code, ss.shop_name, ss.prod_offer_id," +
                    "         po.code, po.name , ss.owner_id, rownum" +
                    "  FROM       (SELECT   s.from_isdn, s.to_isdn, s.total," +
                    "                       s.telecom_service_id, sh.shop_code," +
                    "                       sh.name shop_name, s.prod_offer_id, s.owner_id " +
                    "                FROM   (  SELECT   isdn from_isdn," +
                    "                                   isdn to_isdn," +
                    "                                   1 total," +
                    "                                   telecom_service_id," +
                    "                                   owner_id," +
                    "                                   prod_offer_id" +
                    " FROM   (SELECT   isdn, " +
                    " prod_offer_id, " +
                    " telecom_service_id, " +
                    " owner_id, " +
                    " (isdn - ROWNUM) dif from stock_number ");
            sql.append(" WHERE 1=1 and  to_number(isdn) in (0");
            if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getMapISDN())) {
                Iterator<String> ite = searchNumberRangeDTO.getMapISDN().keySet().iterator();
                StringBuilder paramStr = new StringBuilder("");
                while (ite.hasNext()) {
                    String isdn = ite.next();
                    paramStr.append(",?");
                    params.add(Long.parseLong(isdn));
                }
//            sql.append(paramStr.substring(0, (paramStr.length() - 1)));
                //params.add(searchNumberRangeDTO.getIsdnParam());
                sql.append(paramStr);
            }
            sql.append(")");
            if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getServiceType())) {
                sql.append(" AND   telecom_service_id = ? ");
                params.add(DataUtil.safeToLong(searchNumberRangeDTO.getServiceType()));
            }
            if (!DataUtil.isNullOrZero(searchNumberRangeDTO.getShopId())) {
                sql.append(" AND  owner_id = ? AND owner_type = ?");
                params.add(searchNumberRangeDTO.getShopId());
                params.add(new Long(1));
            }
            sql.append(" AND status in (?,?)");
            params.add(new Long(1));
            params.add(new Long(3));

            sql.append(") tmp ) s," +
                    "                       shop sh" +
                    "               WHERE   s.owner_id = sh.shop_id) ss" +
                    " LEFT OUTER JOIN" +
                    "             product_offering po" +
                    "         ON ss.prod_offer_id = po.prod_offer_id ORDER BY ss.from_isdn asc");
            System.out.println(sql.toString());
            System.out.println(params);
            Query query = em.createNativeQuery(sql.toString());
            for (int idx = 0; idx < params.size(); idx++) {
                query.setParameter(idx + 1, params.get(idx));
            }

            List listResult = query.getResultList();

            for (Object o : listResult) {
                Object[] obj = (Object[]) o;
                TableNumberRangeDTO tableItem = new TableNumberRangeDTO();
                tableItem.setFromISDN(DataUtil.safeToString(obj[0]));
                tableItem.setToISDN(DataUtil.safeToString(obj[1]));
                tableItem.setTotal(Long.parseLong(DataUtil.safeToString(obj[2])));
                tableItem.setTelecomService(DataUtil.safeToString(obj[3]));
                tableItem.setShopCode(DataUtil.safeToString(obj[4]));
                tableItem.setShopName(DataUtil.safeToString(obj[5]));
                if (!DataUtil.isNullObject(searchNumberRangeDTO.getMapISDN()) && !DataUtil.isNullObject(tableItem.getFromISDN())) {
                    tableItem.setProductCodeNew(searchNumberRangeDTO.getMapISDN().get(tableItem.getFromISDN()));
                }
                tableItem.setProductOfferId(DataUtil.safeToLong(obj[6]));
                tableItem.setProductCodeOld(DataUtil.safeToString(obj[7]));
                tableItem.setProductNameOld(DataUtil.safeToString(obj[8]));
                tableItem.setShopId(DataUtil.safeToLong(obj[9]));
                tableItem.setRowKey(DataUtil.safeToString(obj[10]));
                lstTableResult.add(tableItem);
            }
            return lstTableResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<TableNumberRangeDTO> previewFromRange(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        List<TableNumberRangeDTO> lstTableResult = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        List params = new ArrayList();
        sql.append("SELECT   ss.from_isdn, ss.to_isdn, ss.total,  ss.telecom_service_id," +
                "         ss.shop_code, ss.shop_name, ss.prod_offer_id," +
                "         po.code, po.name, ss.owner_id, rownum" +
                "  FROM       (SELECT   s.from_isdn, s.to_isdn, s.total," +
                "                       s.telecom_service_id, sh.shop_code," +
                "                       sh.name shop_name, s.prod_offer_id, s.owner_id " +
                "                FROM   (  SELECT   MIN (isdn) from_isdn," +
                "                                   MAX (isdn) to_isdn," +
                "                                   COUNT ( * ) total," +
                "                                   telecom_service_id," +
                "                                   owner_id," +
                "                                   prod_offer_id" +
                " FROM   ( SELECT   isdn, " +
                " prod_offer_id, " +
                " telecom_service_id, " +
                " owner_id, " +
                " (isdn - ROW_NUMBER () " +
                " OVER ( " +
                " ORDER BY " +
                " s.telecom_service_id, " +
                " s.prod_offer_id, " +
                " s.isdn)) dif from stock_number s ");
        if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getStartRange())) {
            sql.append(" WHERE   to_number(isdn) >= ? ");
            params.add(DataUtil.safeToLong(searchNumberRangeDTO.getStartRange()));
        }
        if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getEndRange())) {
            sql.append(" AND   to_number(isdn) <= ? ");
            params.add(DataUtil.safeToLong(searchNumberRangeDTO.getEndRange()));
        }
        if (!DataUtil.isNullOrEmpty(searchNumberRangeDTO.getServiceType())) {
            sql.append(" AND   telecom_service_id = ? ");
            params.add(searchNumberRangeDTO.getServiceType());
        }
        if (!DataUtil.isNullOrZero(searchNumberRangeDTO.getShopId())) {
            sql.append(" AND  owner_id = ? AND owner_type = ? ");
            params.add(searchNumberRangeDTO.getShopId());
            params.add(new Long(1));

        }
        sql.append(" AND status in (?,?)");
        params.add(new Long(1));
        params.add(new Long(3));

        if (!DataUtil.isNullOrZero(searchNumberRangeDTO.getProductOldId())) {
            sql.append(" AND  prod_offer_id = ? ");
            params.add(searchNumberRangeDTO.getProductOldId());
        }
        sql.append(" ) GROUP BY   telecom_service_id," +
                "                                   owner_id," +
                "                                   prod_offer_id, dif) s," +
                "                       shop sh" +
                "               WHERE   s.owner_id = sh.shop_id) ss" +
                " LEFT OUTER JOIN" +
                "             product_offering po" +
                "         ON ss.prod_offer_id = po.prod_offer_id ORDER BY ss.from_isdn asc");

        Query query = em.createNativeQuery(sql.toString());
        for (int idx = 0; idx < params.size(); idx++) {
            query.setParameter(idx + 1, params.get(idx));
        }

        List listResult = query.getResultList();

        for (Object o : listResult) {
            Object[] obj = (Object[]) o;
            TableNumberRangeDTO tableItem = new TableNumberRangeDTO();
            tableItem.setFromISDN(DataUtil.safeToString(obj[0]));
            tableItem.setToISDN(DataUtil.safeToString(obj[1]));
            tableItem.setTotal(Long.parseLong(DataUtil.safeToString(obj[2])));
            tableItem.setTelecomService(DataUtil.safeToString(obj[3]));
            tableItem.setShopCode(DataUtil.safeToString(obj[4]));
            tableItem.setShopName(DataUtil.safeToString(obj[5]));
            tableItem.setProductOfferId(DataUtil.safeToLong(obj[6]));
            tableItem.setProductCodeOld(DataUtil.safeToString(obj[7]));
            tableItem.setProductNameOld(DataUtil.safeToString(obj[8]));
            tableItem.setShopId(DataUtil.safeToLong(obj[9]));
            tableItem.setRowKey(DataUtil.safeToString(obj[10]));
            lstTableResult.add(tableItem);
        }
        return lstTableResult;
    }

    @Override
    public long updateStockNumberByProOfferId(List<TableNumberRangeDTO> listRangeUpdate, String staffCode, String ip, Date currentDate, ProductOfferingTotalDTO productOfferingTotalDTO, boolean isFromFile) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(listRangeUpdate)) {
            return 0L;
        }
        if (DataUtil.isNullObject(productOfferingTotalDTO) && !isFromFile) {
            return 0L;
        }
        long totalUpdate = 0;

        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");

        for (TableNumberRangeDTO range : listRangeUpdate) {
            List params = new ArrayList();
            StringBuilder sqlUpdateStockNumber = new StringBuilder("update Stock_Number ");
            if (isFromFile) {
                if (DataUtil.isNullObject(range.getProductOfferNewId())) {
                    throw new LogicException("", "common.error.happened");
                } else {
                    sqlUpdateStockNumber.append(" set prod_offer_id = ? ");
                    params.add(range.getProductOfferNewId());
                }
            } else if (!DataUtil.isNullOrZero(productOfferingTotalDTO.getProductOfferingId())) {
                sqlUpdateStockNumber.append(" set prod_offer_id = ? ");
                params.add(productOfferingTotalDTO.getProductOfferingId());
            }
            if (!DataUtil.isNullObject(staffCode)) {
                sqlUpdateStockNumber.append(" ,last_Update_Time = ?, last_Update_User = ? ");
                params.add(currentDate);
                params.add(staffCode);
            }
            if (ip != null) {
                sqlUpdateStockNumber.append(" ,last_Update_Ip_Address = ? ");
                params.add(ip);
            }
            if (!DataUtil.isNullOrEmpty(range.getFromISDN())) {
                sqlUpdateStockNumber.append(" where to_number(isdn) >= ? ");
                params.add(DataUtil.safeToLong(range.getFromISDN()));
            }
            if (!DataUtil.isNullOrEmpty(range.getToISDN())) {
                sqlUpdateStockNumber.append(" and to_number(isdn) <= ? ");
                params.add(DataUtil.safeToLong(range.getToISDN()));
            }

            if (!DataUtil.isNullOrZero(range.getShopId())) {
                sqlUpdateStockNumber.append(" and owner_Id = ? ");
                params.add(DataUtil.safeToLong(range.getShopId()));
            }
            sqlUpdateStockNumber.append(" and owner_Type = ? and status in (?,?) ");
            params.add(new Long(1));
            params.add("1");
            params.add("3");

            Query query = null;
            long updateSuccess = 0;
            long start = DataUtil.safeToLong(range.getFromISDN());
            long end = DataUtil.safeToLong(range.getToISDN());
            try {
                query = em.createNativeQuery(sqlUpdateStockNumber.toString());
                for (int idx = 0; idx < params.size(); idx++) {
                    query.setParameter(idx + 1, params.get(idx));
                }
                updateSuccess = query.executeUpdate();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                return 0;
            }
            //Cap nhat BCCS1
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                    && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                    && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                updateStockNumberBCCS1(params, sqlUpdateStockNumber.toString(), end - start + 1, range.getTelecomService());
            }

            //insert log number action
            if (updateSuccess == (end - start + 1)) {
                NumberActionDTO numberActionDTO = new NumberActionDTO();
                numberActionDTO.setActionType(IsdnConst.ISDN_ACTION_TYPE_MODEL);
                numberActionDTO.setTelecomServiceId(Long.parseLong(range.getTelecomService()));
                numberActionDTO.setFromIsdn(range.getFromISDN());
                numberActionDTO.setToIsdn(range.getToISDN());
                numberActionDTO.setCreateDate(currentDate);
                numberActionDTO.setUserCreate(staffCode);
                numberActionDTO.setUserIpAddress(ip);
                NumberAction numberAction = numberActionRepo.save(mapperNumberAction.toPersistenceBean(numberActionDTO));
                //insert number action detail
                NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
                numberActionDetailDTO.setNumberActionId(numberAction.getNumberActionId());
                numberActionDetailDTO.setColumnName("PROD_OFFER_ID");
                if (isFromFile) {
                    numberActionDetailDTO.setNewValue(range.getProductOfferNewId().toString());
                    numberActionDetailDTO.setNewDetailValue(range.getProductCodeNew());
                } else {
                    numberActionDetailDTO.setNewValue(productOfferingTotalDTO.getProductOfferingId().toString());
                    numberActionDetailDTO.setNewDetailValue((!DataUtil.isNullObject(productOfferingTotalDTO.getCode()) && !DataUtil.isNullObject(productOfferingTotalDTO.getName())) ? (productOfferingTotalDTO.getCode() + "-" + productOfferingTotalDTO.getName()) : null);
                }
                numberActionDetailDTO.setOldValue((!DataUtil.isNullObject(range.getProductOfferId()) && !range.getProductOfferId().equals(0L)) ? range.getProductOfferId().toString() : null);
                numberActionDetailDTO.setOldDetailValue(range.getProductCodeOld());
                numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(numberActionDetailDTO));
            } else {
                throw new LogicException("", "mn.stock.status.isdn.range.change");
            }
        }

        return totalUpdate;
    }

    private void updateStockNumberBCCS1(List params, String strQuery, long number, String telecomService) throws LogicException, Exception {

        if (Const.NUMBER_SEARCH.STOCK_TYPE_MOBILE.equals(telecomService)) {
            strQuery = strQuery.replace("Stock_Number", "bccs_im.stock_isdn_mobile");
        } else if (Const.NUMBER_SEARCH.STOCK_TYPE_HP.equals(telecomService)) {
            strQuery = strQuery.replace("Stock_Number", "bccs_im.stock_isdn_homephone");
        } else {
            strQuery = strQuery.replace("Stock_Number", "bccs_im.stock_isdn_pstn");
        }
        strQuery = strQuery.replace("prod_offer_id", "stock_model_id");
        int updateSuccess;
        try {
            Query query = em1.createNativeQuery(strQuery);
            for (int idx = 0; idx < params.size(); idx++) {
                query.setParameter(idx + 1, params.get(idx));
            }
            updateSuccess = query.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return;
        }
        if (updateSuccess != number) {
            throw new LogicException("", "mn.stock.status.isdn.range.change");
        }
    }

    private void updateStatusStockNumberBCCS1(List params, String strQuery, long number, String telecomService) throws LogicException, Exception {

        if (Const.NUMBER_SEARCH.STOCK_TYPE_MOBILE.equals(telecomService)) {
            strQuery = strQuery.replace("Stock_Number", "bccs_im.stock_isdn_mobile");
        } else if (Const.NUMBER_SEARCH.STOCK_TYPE_HP.equals(telecomService)) {
            strQuery = strQuery.replace("Stock_Number", "bccs_im.stock_isdn_homephone");
        } else {
            strQuery = strQuery.replace("Stock_Number", "bccs_im.stock_isdn_pstn");
        }
        int updateSuccess;
        try {
            Query query = em1.createNativeQuery(strQuery);
            for (int idx = 0; idx < params.size(); idx++) {
                query.setParameter(idx + 1, params.get(idx));
            }
            updateSuccess = query.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return;
        }
        if (updateSuccess != number) {
            throw new LogicException("", "mn.stock.status.isdn.range.change");
        }
    }

    @Override
    public List<Object[]> previewDistriButeNumber(InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        if (DataUtil.isNullObject(inputByRange.getTelecomServiceId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.service.type.not.null");
        }
        if (DataUtil.isNullObject(inputByRange.getStartRange())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "from.number.require.msg");
        }
        if (!DataUtil.isNumber(inputByRange.getStartRange())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "from.number.invalid.msg");
        }
        if (inputByRange.getStartRange().length() < 8 || inputByRange.getStartRange().length() > 11) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.from.length");
        }
        if (DataUtil.isNullObject(inputByRange.getEndRange())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "to.number.require.msg");
        }
        if (!DataUtil.isNumber(inputByRange.getEndRange())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "to.number.invalid.msg");
        }
        if (inputByRange.getEndRange().length() < 8 || inputByRange.getEndRange().length() > 11) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.to.length");
        }
        if (inputByRange.getStartRange().length() != inputByRange.getEndRange().length()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "from.to.number.length.msg");
        }

        String roleStr = "";
        try {
            roleStr = getRoleQuery(requiredRoleMap);
        } catch (LogicException le) {
            throw le;
        }

        Long from = DataUtil.safeToLong(inputByRange.getStartRange());
        Long to = DataUtil.safeToLong(inputByRange.getEndRange());
        if (to < from) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "from.to.number.invalid.msg");
        }
        if (to - from > 1000000) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.number.per.range.over");
        }
        StringBuilder sql = new StringBuilder();
        List params = Lists.newArrayList();
        sql.append("SELECT   ss.owner_code,");
        sql.append("           ss.owner_name,");
        sql.append("           ss.telecom_service_id,");
        sql.append("           ss.from_isdn,");
        sql.append("           ss.to_isdn,");
        sql.append("           ss.so_luong,");
        sql.append("           ss.status,");
        sql.append("           ROWNUM,");
        sql.append("           ss.owner_id,");
        sql.append("           po.name prod_name,");
        sql.append("           nvl(nf.name,' ') rule_name,");
        sql.append("           ss.owner_type owner_type");
        sql.append("    FROM           (SELECT   s.owner_code,");
        sql.append("                             s.owner_name,");
        sql.append("                             s.owner_type,");
        sql.append("                             sn.telecom_service_id,");
        sql.append("                             sn.mn from_isdn,");
        sql.append("                             sn.mx to_isdn,");
        sql.append("                             sn.so_luong,");
        sql.append("                             sn.status,");
        sql.append("                             s.owner_id,");
        sql.append("                             sn.prod_offer_id,");
        sql.append("                             sn.filter_rule_id");
        sql.append("                      FROM   (  SELECT   MIN (isdn) mn,");
        sql.append("                                         MAX (isdn) mx,");
        sql.append("                                         status,");
        sql.append("                                         telecom_service_id,");
        sql.append("                                         owner_id,");
        sql.append("                                         owner_type,");
        sql.append("                                         prod_offer_id,");
        sql.append("                                         filter_rule_id,");
        sql.append("                                         COUNT ( * ) AS so_luong");
        sql.append("                                  FROM   (SELECT   isdn,");
        sql.append("                                                   status,");
        sql.append("                                                   telecom_service_id,");
        sql.append("                                                   owner_id,");
        sql.append("                                                   owner_type,");
        sql.append("                                                   prod_offer_id,");
        sql.append("                                                   filter_rule_id,");
        sql.append("                                                   (isdn");
        sql.append("                                                    - ROW_NUMBER ()");
        sql.append("                                                          OVER (");
        sql.append("                                                              ORDER BY");
        sql.append("                                                                  s.status,");
        sql.append("                                                                  s.telecom_service_id,");
        sql.append("                                                                  s.owner_id,");
        sql.append("                                                                  s.owner_type,");
        sql.append("                                                                  s.prod_offer_id,");
        sql.append("                                                                  s.filter_rule_id,");
        sql.append("                                                                  s.isdn))");
        sql.append("                                                       dif");
        sql.append("                                            FROM   stock_number s");
        sql.append("                                           WHERE       to_number(s.isdn) >= ?");
        params.add(from);
        sql.append("                                                   AND to_number(s.isdn) <= ?");
        params.add(to);
        sql.append("                                                   AND s.telecom_service_id = ?");
        params.add(inputByRange.getTelecomServiceId());
        if (DataUtil.isNullObject(inputByRange.getFromOwnerId())) {
            sql.append(" AND ( ( exists (SELECT 'X' FROM SHOP WHERE shop_id = s.owner_id and (shop_id = ? or shop_path like ?)  ) and s.owner_type=1 ) or ( exists  (SELECT 'X' FROM STAFF WHERE staff_id = s.owner_id and status=1  and shop_id in  (select shop_id from shop where shop_id = ? or shop_path like ?) ) and s.owner_type=2 ) )");
            params.add(inputByRange.getCurrentShopid());
            params.add(inputByRange.getCurrentShopPath() + "%");
            params.add(inputByRange.getCurrentShopid());
            params.add(inputByRange.getCurrentShopPath() + "%");
        } else {
            sql.append("                                                   AND s.owner_id = ?");
            params.add(inputByRange.getFromOwnerId());
            sql.append("                                                   AND s.owner_type = ?");
            params.add(inputByRange.getFromOwnerType());
        }
        sql.append("                                                   AND s.status IN (1,3) ");
        sql.append(roleStr + ") ");
        sql.append("                              GROUP BY   status,");
        sql.append("                                         telecom_service_id,");
        sql.append("                                         owner_id,");
        sql.append("                                         owner_type,");
        sql.append("                                         prod_offer_id,");
        sql.append("                                         filter_rule_id,");
        sql.append("                                         dif) sn,");
        sql.append("                             v_shop_staff s");
        sql.append("                     WHERE   sn.owner_id = s.owner_id");
        sql.append("                             AND sn.owner_type = s.owner_type) ss");
        sql.append("               INNER JOIN");
        sql.append("                   product_offering po");
        sql.append("               ON po.prod_offer_id = ss.prod_offer_id");
        sql.append("           LEFT JOIN");
        sql.append("               number_filter_rule nf");
        sql.append("           ON ss.filter_rule_id = nf.filter_rule_id ");
        sql.append(" ORDER BY   from_isdn");
        Query query = em.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }

    @Override
    public String distriButeNumber(List<Object[]> listRangePreviewed, InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(listRangePreviewed)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "distribute.must.preview");
        }
        if (DataUtil.isNullObject(inputByRange.getTelecomServiceId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.service.type.not.null");
        }

        String roleStr = "";
        try {
            roleStr = getRoleQuery(requiredRoleMap);
        } catch (LogicException le) {
            throw le;
        }

        if (!DataUtil.isNullObject(inputByRange.getNote()) && inputByRange.getNote().length() > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }
        if (DataUtil.isNullObject(inputByRange.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "id.receive.stock.not.null");
        }
        if (DataUtil.isNullObject(inputByRange.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "type.receive.stock.not.null");
        }
        if (DataUtil.isNullObject(inputByRange.getReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "reason.not.null.msg");
        }
//        List<OptionSetValueDTO> listService = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
        List<StockIsdnTrans> lstStockIsdnTrans = Lists.newArrayList();
        List<StockIsdnTransDetail> lstStockIsdnTransDetail = Lists.newArrayList();
        Date sysdate = DbUtil.getSysDate(em);
        List<Long> lstStockIsdnTransId = Lists.newArrayList();
        for (Object ob[] : listRangePreviewed) {
            lstStockIsdnTransId.add(DbUtil.getSequence(em, "STOCK_ISDN_TRANS_SEQ"));
        }
        for (int ijk = 0; ijk < listRangePreviewed.size(); ijk++) {
            Object ob[] = listRangePreviewed.get(ijk);
            Long from = DataUtil.safeToLong(ob[3]);
            Long to = DataUtil.safeToLong(ob[4]);
            String fromOwnerCode = DataUtil.safeToString(ob[0]);
            String fromOwnerName = DataUtil.safeToString(ob[1]);
            String fromOwnerId = DataUtil.safeToString(ob[8]);
            String fromOwnerType = DataUtil.safeToString(ob[11]);
            StringBuilder sql = new StringBuilder();
            List params = Lists.newArrayList();
            sql.append("UPDATE   stock_number s ");
            sql.append("   SET   s.owner_id = ?, s.owner_type = ?, s.last_update_user = ?, " +
                    "s.last_update_ip_address = ?, s.last_update_time = ?");
            params.add(inputByRange.getToOwnerId());
            params.add(inputByRange.getToOwnerType());
            params.add(inputByRange.getUserCode());
            params.add(inputByRange.getUserIp());
            params.add(sysdate);
            sql.append(" WHERE   to_number(s.isdn) >= ? AND to_number(s.isdn) <= ? AND s.telecom_service_id = ? AND s.status in (1,3) ");
            params.add(from);
            params.add(to);
            params.add(inputByRange.getTelecomServiceId());
            sql.append(" AND owner_id = ? ");
            params.add(fromOwnerId);
            sql.append(" AND owner_type = ?  ");
            params.add(fromOwnerType);
            sql.append("AND ( ( exists (SELECT 'X' FROM SHOP WHERE shop_id = s.owner_id and (shop_id = ? or shop_path like ?)  ) and s.owner_type=1 ) or ( exists  (SELECT 'X' FROM STAFF WHERE staff_id = s.owner_id and status=1  and shop_id in  (select shop_id from shop where shop_id = ? or shop_path like ?) ) and s.owner_type=2 ) )");
            params.add(inputByRange.getCurrentShopid());
            params.add(inputByRange.getCurrentShopPath() + "%");
            params.add(inputByRange.getCurrentShopid());
            params.add(inputByRange.getCurrentShopPath() + "%");
            sql.append(roleStr);
            Query query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
            long result = query.executeUpdate();
            if (result == (to - from + 1)) {
                //insert number_action

                NumberActionDTO action = new NumberActionDTO();
                action.setFromIsdn(from.toString());
                action.setTelecomServiceId(inputByRange.getTelecomServiceId());
                action.setToIsdn(to.toString());
                action.setActionType(Const.NUMBER_ACTION_TYPE.DISTRIBUTE);
                action.setUserCreate(inputByRange.getUserCode());
                action.setUserIpAddress(inputByRange.getUserIp());
                action.setReasonId(inputByRange.getReasonId());
                action.setNote(inputByRange.getNote());
                action.setCreateDate(sysdate);
                NumberAction numberAction = numberActionRepo.save(mapperNumberAction.toPersistenceBean(action));
                //insert number_action_detail
                NumberActionDetailDTO detailOwnerType = new NumberActionDetailDTO();
                detailOwnerType.setNumberActionId(numberAction.getNumberActionId());
                detailOwnerType.setColumnName("OWNER_TYPE");
                detailOwnerType.setOldValue(fromOwnerType);
                detailOwnerType.setNewValue(inputByRange.getToOwnerType());
                detailOwnerType.setOldDetailValue(inputByRange.getFromOwnerType());
                detailOwnerType.setNewDetailValue(inputByRange.getToOwnerType());
                numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(detailOwnerType));
                //insert number_action_detail
                NumberActionDetailDTO detailOwnerId = new NumberActionDetailDTO();
                detailOwnerId.setNumberActionId(numberAction.getNumberActionId());
                detailOwnerId.setColumnName("OWNER_ID");
                detailOwnerId.setOldValue(fromOwnerId);
                detailOwnerId.setNewValue(inputByRange.getToOwnerId().toString());
                detailOwnerId.setOldDetailValue(fromOwnerCode + "-" + fromOwnerName);
                detailOwnerId.setNewDetailValue(inputByRange.getToOwnerCode() + "-" + inputByRange.getToOwnerName());
                numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(detailOwnerId));
                //cap nhat vao stock_isdn_trans va stock_isdn_trans_detail
                StockIsdnTrans stockIsdnTrans = new StockIsdnTrans();
//                stockIsdnTrans.se
                //get squence
                stockIsdnTrans.setStockIsdnTransId(lstStockIsdnTransId.get(ijk));
                String code = genCode(lstStockIsdnTransId.get(ijk));
                stockIsdnTrans.setStockIsdnTransCode(code);
                stockIsdnTrans.setFromOwnerCode(fromOwnerCode);
                stockIsdnTrans.setFromOwnerId(DataUtil.safeToLong(fromOwnerId));
                stockIsdnTrans.setFromOwnerName(fromOwnerName);
                stockIsdnTrans.setFromOwnerType(DataUtil.safeToLong(fromOwnerType));
                stockIsdnTrans.setToOwnerCode(inputByRange.getToOwnerCode());
                stockIsdnTrans.setToOwnerName(inputByRange.getToOwnerName());
                stockIsdnTrans.setToOwnerId(inputByRange.getToOwnerId());
                stockIsdnTrans.setToOwnerType(DataUtil.safeToLong(inputByRange.getToOwnerType()));
                stockIsdnTrans.setStockTypeId(inputByRange.getTelecomServiceId());
                stockIsdnTrans.setStockTypeName("STOCK_NUMBER");
                stockIsdnTrans.setQuantity(to - from + 1);
                stockIsdnTrans.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
                stockIsdnTrans.setReasonId(inputByRange.getReasonId());
                stockIsdnTrans.setReasonCode(inputByRange.getReasonCode());
                stockIsdnTrans.setReasonName(inputByRange.getReasonName());
                stockIsdnTrans.setNote(inputByRange.getNote());

                stockIsdnTrans.setCreatedTime(sysdate);
                stockIsdnTrans.setCreatedUserId(inputByRange.getUserId());
                stockIsdnTrans.setCreatedUserName(inputByRange.getUserCreate());
                stockIsdnTrans.setCreatedUserCode(inputByRange.getUserCode());
                stockIsdnTrans.setCreatedUserIp(inputByRange.getUserIp());
                stockIsdnTrans.setLastUpdatedUserId(inputByRange.getUserId());
                stockIsdnTrans.setLastUpdatedUserName(inputByRange.getUserCreate());
                stockIsdnTrans.setLastUpdatedUserCode(inputByRange.getUserCode());
                stockIsdnTrans.setLastUpdatedTime(sysdate);
                stockIsdnTrans.setLastUpdatedUserIp(inputByRange.getUserIp());

                StockIsdnTransDetail stockIsdnTransDetail = new StockIsdnTransDetail();
                stockIsdnTransDetail.setFromIsdn(from + "");
                stockIsdnTransDetail.setToIsdn(to + "");
                stockIsdnTransDetail.setQuantity(to - from + 1);
                stockIsdnTransDetail.setLengthIsdn(new Long((from + "").length()));
                stockIsdnTransDetail.setCreatedTime(sysdate);
                saveStockIsdn(stockIsdnTrans, stockIsdnTransDetail);

                //clone object stock_isdn_trans insert to im1
                StockIsdnTrans stockIsdnTrans1 = DataUtil.cloneBean(stockIsdnTrans);
                if (stockIsdnTrans1.getStockTypeId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
                    stockIsdnTrans1.setStockTypeId(Const.PRODUCT_OFFER_TYPE.MOBILE);
                } else if (stockIsdnTrans1.getStockTypeId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
                    stockIsdnTrans1.setStockTypeId(Const.PRODUCT_OFFER_TYPE.HP);
                } else {
                    stockIsdnTrans1.setStockTypeId(Const.PRODUCT_OFFER_TYPE.PSTN);
                }
                lstStockIsdnTrans.add(stockIsdnTrans1);

                //clone object stock_isdn_trans_detail insert to im1
                StockIsdnTransDetail stockIsdnTransDetail1 = DataUtil.cloneBean(stockIsdnTransDetail);
                lstStockIsdnTransDetail.add(stockIsdnTransDetail1);
            } else {
                throw new LogicException("", "isdn.distribute.fail.change");
            }
        }

        //cap nhat giao dich IM1
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            stockIsdnIm1Service.doDistributeIsdn(inputByRange, listRangePreviewed, lstStockIsdnTrans, lstStockIsdnTransDetail);
        }
        return "SUCCESS...";
    }

    private String genCode(Long id) {
        String transCode = String.format("GD%011d", id);
        return transCode;
    }

    public void saveStockIsdn(StockIsdnTrans stockIsdnTrans, StockIsdnTransDetail stockIsdnTransDetail) throws LogicException, Exception {

        StockIsdnTrans transToSave = stockIsdnTransRepo.saveAndFlush(stockIsdnTrans);

        stockIsdnTransDetail.setStockIsdnTransId(transToSave.getStockIsdnTransId());
        StockIsdnTransDetail detailToSave = stockIsdnTransDetailRepo.saveAndFlush(stockIsdnTransDetail);

        stockIsdnTrans.setStockIsdnTransId(transToSave.getStockIsdnTransId());
        stockIsdnTransDetail.setStockIsdnTransDetailId(detailToSave.getStockIsdnTransDetailId());

    }

    public String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue() + "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            return null;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getCellFormula();
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.format("%.0f", cell.getNumericCellValue());
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        }
        return "";
    }

    //xem truoc xoa so
    @Override
    public List<DeleteNumberDTO> previewDeleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO deleteStockNumberDTO) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(lstDelete) || DataUtil.isNullObject(deleteStockNumberDTO)) {
            throw new LogicException("101", "mn.stock.status.isdn.delete.nodata");
        }

        for (DeleteNumberDTO deleteDTO : lstDelete) {
            if (IsdnConst.DEL_DOUBLE.equals(deleteDTO.getResult())) {
                continue;
            }
            List params = new ArrayList<>();
            StringBuilder sql = new StringBuilder("select count(*) from stock_number where 1 = 1 ");
            if (DataUtil.isNullOrZero(deleteDTO.getFromISDN())) {
                deleteDTO.setResult(IsdnConst.DEL_INVALID_FROM);
                continue;
            } else {
                sql.append(" and to_number(isdn) >= ?");
                params.add(deleteDTO.getFromISDN());
            }
            if (DataUtil.isNullOrZero(deleteDTO.getToISDN())) {
                deleteDTO.setResult(IsdnConst.DEL_INVALID_TO);
                continue;
            } else {
                sql.append(" and to_number(isdn) <= ? and status = ?");
                params.add(deleteDTO.getToISDN());
                params.add(Const.NEW);
            }
            if (!DataUtil.isNullOrZero(deleteStockNumberDTO.getServiceType())) {
                sql.append(" and telecom_service_id = ? ");
                params.add(deleteStockNumberDTO.getServiceType());
            }
            Query query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
            List lstResult = query.getResultList();
            long total = DataUtil.safeToLong(lstResult.get(0));
            deleteDTO.setTotal(total);
            if ((deleteDTO.getToISDN() - (deleteDTO.getFromISDN() - 1)) == total) {
                deleteDTO.setResult(IsdnConst.DEL_AVAIL);
            } else {
                deleteDTO.setResult(IsdnConst.DEL_NOT_RANGE);
            }
        }

        return lstDelete;

    }

    //xoa so
    @Override
    public List<DeleteNumberDTO> deleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO dto, String user, String ip) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(lstDelete)) {
            throw new LogicException("101", "list null");
        }

        for (DeleteNumberDTO deleteDTO : lstDelete) {
            List params = new ArrayList<>();

            if (DataUtil.isNullOrEmpty(deleteDTO.getResult())) {
                continue;
            }
            if (!IsdnConst.DEL_AVAIL.equals(deleteDTO.getResult())) {
                continue;
            }

            StringBuilder sql = new StringBuilder("select na.number_action_id, na.from_isdn, na.to_isdn, na.action_type from number_action na ");
            sql.append(" where (((na.from_isdn <= ?) and (? <= na.to_isdn)) ");
            params.add(deleteDTO.getFromISDN());
            params.add(deleteDTO.getFromISDN());
            sql.append(" or ((na.from_isdn <= ?) and (? <= na.to_isdn)) ");
            params.add(deleteDTO.getToISDN());
            params.add(deleteDTO.getToISDN());
            sql.append(" or ((? <= na.from_isdn) and (na.to_isdn <= ?))) and na.action_type = ? ");
            params.add(deleteDTO.getFromISDN());
            params.add(deleteDTO.getToISDN());
            params.add(new Long(1));
            Query query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
            List lstResult = query.getResultList();
            for (Object o : lstResult) {
                Object[] obj = (Object[]) o;
                Long numberActionId = DataUtil.safeToLong(obj[0]);
                Long logFromIsdn = DataUtil.safeToLong(obj[1]);
                Long logToIsdn = DataUtil.safeToLong(obj[2]);
//                Long actionType = DataUtil.safeToLong(obj[3]);

                NumberAction numberAction = numberActionRepo.findOne(numberActionId);
                boolean isUpdate = false;
                boolean isInsert = false;
                //N?u na.from_isdn <= fromISDN AND na.to_isdn <= toISDN
                //update number_action set to_isdn = fromISDN, note = 'T?o l?i x�a d?i s?' where number_action_id = na.number_action_id;
                if (logFromIsdn >= deleteDTO.getFromISDN() && logToIsdn <= deleteDTO.getToISDN()) {
                    numberActionRepo.delete(numberAction);
                    //xoa ban ghi detail
                    StringBuilder sqlDeleteDetail = new StringBuilder("delete from bccs_im.number_action_detail n where n.number_action_id = " + numberAction.getNumberActionId());
                    Query sqlDelete = em.createNativeQuery(sqlDeleteDetail.toString());
                    sqlDelete.executeUpdate();
                    continue;
                } else if (logFromIsdn >= deleteDTO.getFromISDN() && logFromIsdn <= deleteDTO.getToISDN()
                        && logToIsdn >= deleteDTO.getToISDN()) {
                    numberAction.setFromIsdn((deleteDTO.getToISDN() + 1) + "");
                    numberAction.setNote(dto.getNote());
                    isUpdate = true;
                } else if (logFromIsdn <= deleteDTO.getFromISDN() && logToIsdn >= deleteDTO.getFromISDN()
                        && logToIsdn <= deleteDTO.getToISDN()) {
                    numberAction.setToIsdn((deleteDTO.getFromISDN() - 1) + "");
                    numberAction.setNote(dto.getNote());
                    isUpdate = true;
                } else if (logFromIsdn < deleteDTO.getFromISDN() && deleteDTO.getToISDN() < logToIsdn) {
                    numberAction.setToIsdn((deleteDTO.getFromISDN() - 1) + "");
                    numberAction.setNote(dto.getNote());
                    isUpdate = true;
                    isInsert = true;
                }

                if (isUpdate) {
                    numberActionRepo.save(numberAction);
                }
                if (isInsert) {
                    NumberActionDTO action = new NumberActionDTO();
                    action.setFromIsdn((deleteDTO.getToISDN() + 1) + "");
                    action.setTelecomServiceId(numberAction.getTelecomServiceId());
                    action.setToIsdn(logToIsdn + "");
                    action.setUserCreate(user);
                    action.setUserIpAddress(ip);
                    action.setNote(numberAction.getNote());
                    action.setReasonId(numberAction.getReasonId());
                    action.setActionType(IsdnConst.ISDN_ACTION_TYPE_CREATE);
                    action.setCreateDate(DbUtil.getSysDate(em));
                    numberActionRepo.save(mapperNumberAction.toPersistenceBean(action));
                }
            }
            //xoa
            StringBuilder sqlDelete = new StringBuilder("delete from stock_number ");
            List paramDelete = new ArrayList();
            sqlDelete.append(" where to_number(isdn) >= ?");
            paramDelete.add(deleteDTO.getFromISDN());
            sqlDelete.append(" and to_number(isdn) <= ? and status = ?");
            paramDelete.add(deleteDTO.getToISDN());
            paramDelete.add(new Long(1));
            if (!DataUtil.isNullOrZero(dto.getServiceType())) {
                sqlDelete.append(" and telecom_service_id = ? ");
                paramDelete.add(dto.getServiceType());
            }

            Query queryDelete = em.createNativeQuery(sqlDelete.toString());
            for (int i = 0; i < paramDelete.size(); i++) {
                queryDelete.setParameter(i + 1, paramDelete.get(i));
            }

            long result = queryDelete.executeUpdate();
            if (DataUtil.safeEqual(result, deleteDTO.getTotal())) {
                deleteDTO.setResult(IsdnConst.DEL_SUCCESS);
            } else {
                deleteDTO.setResult(IsdnConst.DEL_FAIL);
                throw new LogicException("", "mn.stock.status.isdn.delete.fail");
            }
        }

        return lstDelete;

    }

    //xem truoc gan so
    public List<TransferNGNDTO> previewTransfer(List<TransferNGNDTO> lstTransfer, TransferNGNDTO dto) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lstTransfer)) {
            throw new LogicException("", "mn.stock.status.isdn.update.file.noselect");
        }
        //lay danh sach shopId
        List<ShopDTO> lstShopNGN;
        if (IsdnConst.TYPE_NGN == (dto.getTelecomService())) {
            lstShopNGN = shopRepo.getListShopByCodeOption(IsdnConst.ARANGE_STOCK_ISDN_STOCK_CODE);
            if (DataUtil.isNullOrEmpty(lstShopNGN)) {
                throw new LogicException(IsdnConst.ERR_NGN_CODE, "mn.isdn.manage.code.NGN.noshop");
            }
        } else {
            lstShopNGN = shopRepo.getListShopByCodeOption(IsdnConst.ARANGE_STOCK_ISDN_TK_STOCK_CODE);
            if (DataUtil.isNullOrEmpty(lstShopNGN)) {
                throw new LogicException(IsdnConst.ERR_NGN_CODE, "mn.isdn.manage.code.NGN.noshop");
            }
        }
        //Lay danh sach ma tinh
        List<String> lstAreaCode = new ArrayList<String>();
        StringBuilder sql = new StringBuilder("select upper(area_code) from area ");
        sql.append(" where status = 1 and district is null and precinct is null");
//        String sql = "select area_code from area ";
        Query areaQuery = em.createNativeQuery(sql.toString());
        List lstResult = areaQuery.getResultList();

        if (DataUtil.isNullOrEmpty(lstResult)) {
            throw new LogicException("", "mn.isdn.manage.code.NGN.nocode");
        }

        lstAreaCode.addAll(lstResult);
        List params = new ArrayList<>();
        StringBuilder sqlPreview = new StringBuilder("select isdn, area_code, owner_id from stock_number where status = ? ");
        params.add(new Long(1));

        HashMap<Long, TransferNGNDTO> mapTransfer = new HashMap<Long, TransferNGNDTO>();

        sqlPreview.append(" AND to_number(isdn) in (");
        StringBuilder paramStr = new StringBuilder("");
        for (TransferNGNDTO transfer : lstTransfer) {
            //validate newAreaCode
            mapTransfer.put(transfer.getIsdn(), transfer);
            if (DataUtil.isNullOrEmpty(transfer.getNewAreaCode()) || !lstAreaCode.contains(transfer.getNewAreaCode().toUpperCase())) {
                transfer.setResult(IsdnConst.NGN_INVALID_CODE);
                continue;
            }
            if (DataUtil.isNullOrZero(transfer.getIsdn())) {
                transfer.setResult(IsdnConst.ISDN_NOT_EXIST);
                continue;
            }
            paramStr.append("?,");
            params.add(transfer.getIsdn());
        }
        sqlPreview.append(paramStr.substring(0, (paramStr.length() - 1))).append(") ");
        sqlPreview.append(" AND telecom_service_id = ? ");
        params.add(dto.getTelecomService());
        Query previewQuery = em.createNativeQuery(sqlPreview.toString());

        for (int i = 0; i < params.size(); i++) {
            previewQuery.setParameter(i + 1, params.get(i));
        }

        List lstPreview = previewQuery.getResultList();

        if (DataUtil.isNullOrEmpty(lstPreview)) {
            for (TransferNGNDTO transfer : lstTransfer) {
                if (DataUtil.isNullOrEmpty(transfer.getResult())) {
                    transfer.setResult(IsdnConst.ISDN_NOT_EXIST);
                }
            }
            return lstTransfer;
        }

        List<Long> lstIsdnInDB = new ArrayList<Long>();
        for (Object o : lstPreview) {
            Object[] result = (Object[]) o;
            Long isdn = DataUtil.safeToLong(result[0]);
            lstIsdnInDB.add(isdn);
            String areaCode = DataUtil.safeToString(result[1]);
            Long shopId = DataUtil.safeToLong(result[2]);
            //kiem tra shop co thoa man trong shop duoc gan khong
            TransferNGNDTO transfer = mapTransfer.get(isdn);
            if (!DataUtil.isNullOrEmpty(areaCode) && areaCode.toUpperCase().equals(transfer.getNewAreaCode().toUpperCase())) {
                transfer.setResult(IsdnConst.CODE_DUP);
                continue;
            }
            boolean canGrant = false;
            for (ShopDTO shop : lstShopNGN) {
                if (shop.getShopId().equals(shopId)) {
                    canGrant = true;
                    break;
                }
            }
            if (!canGrant) {
                transfer.setResult(IsdnConst.NGN_WRONG_SHOP);
                continue;
            }
            if (transfer != null) {
                transfer.setCurAreaCode(areaCode);
                transfer.setOwnerId(shopId);
                transfer.setResult(IsdnConst.SUCCESS);
            }
        }
        Iterator<Long> ite = mapTransfer.keySet().iterator();
        while (ite.hasNext()) {
            Long isdn = ite.next();
            TransferNGNDTO transfer = mapTransfer.get(isdn);
            if (DataUtil.isNullOrEmpty(transfer.getResult()) && !lstIsdnInDB.contains(isdn)) {
                transfer.setResult(IsdnConst.ISDN_NOT_EXIST);
            }
        }
        return lstTransfer;
    }

    //gan so
    public List<TransferNGNDTO> transferNGN(List<TransferNGNDTO> lstTransfer, TransferNGNDTO dto, String userCreate, String userIpCreate) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lstTransfer)) {
            throw new LogicException("101", "common.empty.records");
        }
        //lay danh sach shopId
        List<ShopDTO> lstShopNGN;
        if (IsdnConst.TYPE_NGN == (dto.getTelecomService())) {
            lstShopNGN = shopRepo.getListShopByCodeOption(IsdnConst.ARANGE_STOCK_ISDN_STOCK_CODE);
            if (DataUtil.isNullOrEmpty(lstShopNGN)) {
                throw new LogicException(IsdnConst.ERR_NGN_CODE, "mn.isdn.manage.code.NGN.noshop");
            }
        } else {
            lstShopNGN = shopRepo.getListShopByCodeOption(IsdnConst.ARANGE_STOCK_ISDN_TK_STOCK_CODE);
            if (DataUtil.isNullOrEmpty(lstShopNGN)) {
                throw new LogicException(IsdnConst.ERR_NGN_CODE, "mn.isdn.manage.code.NGN.noshop");
            }
        }
        //danh sach shopId
        List<Long> lstShopId = new ArrayList<Long>();
        for (ShopDTO shopDto : lstShopNGN) {
            lstShopId.add(shopDto.getShopId());
        }
        //
        String serviceType;
        if (IsdnConst.TYPE_NGN == (dto.getTelecomService())) {
            serviceType = "1";
        } else if (IsdnConst.TYPE_PSTN == (dto.getTelecomService())) {
            serviceType = "2";
        } else {
            throw new LogicException("101", "mn.stock.status.isdn.NGN.wrong.service");
        }

        //Lay danh sach ma tinh
        List<String> lstAreaCode = new ArrayList<String>();
        StringBuilder sqlArea = new StringBuilder("select upper(area_code) from area ");
        sqlArea.append(" where status = 1 and district is null and precinct is null");
//        String sql = "select area_code from area ";
        Query areaQuery = em.createNativeQuery(sqlArea.toString());
        List lstResult = areaQuery.getResultList();
        lstAreaCode.addAll(lstResult);

        if (DataUtil.isNullOrEmpty(lstResult)) {
            throw new LogicException("", "mn.isdn.manage.code.NGN.nocode");
        }

        for (TransferNGNDTO transfer : lstTransfer) {
            if (!IsdnConst.SUCCESS.equals(transfer.getResult())) {
                continue;
            }
            if ((!DataUtil.isNullOrEmpty(transfer.getCurAreaCode()) && !lstAreaCode.contains(transfer.getCurAreaCode().toUpperCase()))
                    || !lstAreaCode.contains(transfer.getNewAreaCode().toUpperCase())) {
                transfer.setResult(IsdnConst.NGN_AREA_CODE_EXPIRE);
                continue;
            }

            List params = new ArrayList<>();
            StringBuilder sql = new StringBuilder("update stock_number set area_code = #P0, last_update_user = #P1, last_update_ip_address = #P2, last_update_time = #P3 ");
            params.add(transfer.getNewAreaCode().toUpperCase());
            params.add(userCreate);
            params.add(userIpCreate);
            params.add(DbUtil.getSysDate(em));
            sql.append(" where isdn = #P").append(params.size());
            params.add(transfer.getIsdn());
            if (!DataUtil.isNullOrEmpty(transfer.getCurAreaCode())) {
                sql.append(" and lower(area_code) = #P").append(params.size());
                params.add(transfer.getCurAreaCode().toLowerCase());
            }
            sql.append(" and status = #P").append(params.size());
            params.add(new Long(1));
            if (!DataUtil.isNullOrZero(dto.getTelecomService())) {
                sql.append(" and telecom_service_id = #P").append(params.size());
                params.add(dto.getTelecomService());
            }
            if (!DataUtil.isNullOrEmpty(lstShopId)) {
                sql.append(" and owner_id " + DbUtil.createInQuery("owner_id", lstShopId));
            }

            Query query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter("P" + i, params.get(i));
            }
            if (!DataUtil.isNullOrEmpty(lstShopId)) {
                DbUtil.setParamInQuery(query, "owner_id", lstShopId);
            }

            long result = query.executeUpdate();

            if (result == 1) {
                NumberActionDTO action = new NumberActionDTO();
                action.setFromIsdn(transfer.getIsdn() + "");
                action.setTelecomServiceId(dto.getTelecomService());
                action.setToIsdn(transfer.getIsdn() + "");
                action.setUserCreate(userCreate);
                action.setActionType(IsdnConst.ISDN_ACTION_TYPE_NGN);
                action.setUserIpAddress(userIpCreate);
                action.setNote("");
                action.setServiceType(serviceType);
                action.setCreateDate(DbUtil.getSysDate(em));
                NumberAction numberAction = numberActionRepo.save(mapperNumberAction.toPersistenceBean(action));
                NumberActionDetailDTO detailOwnerType = new NumberActionDetailDTO();
                detailOwnerType.setNumberActionId(numberAction.getNumberActionId());
                detailOwnerType.setColumnName("AREA_CODE");
                detailOwnerType.setOldValue(transfer.getCurAreaCode().toUpperCase());
                detailOwnerType.setNewValue(transfer.getNewAreaCode().toUpperCase());
                detailOwnerType.setOldDetailValue(transfer.getCurAreaCode().toUpperCase());
                detailOwnerType.setNewDetailValue(transfer.getNewAreaCode().toUpperCase());
                numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(detailOwnerType));
                transfer.setResult(IsdnConst.GRANT_SUCCESS);
            } else {
                transfer.setResult(IsdnConst.GRANT_FAIL);
                throw new LogicException("", "mn.isdn.manage.code.NGN.grant.fail");
            }
        }
        return lstTransfer;
    }

//    public int checkStockCode(List<String[]> stockCode, String stockType) {
//        if (DataUtil.isNullObject(stockCode)) {
//            return 5;
//        }
//        try {
//            StringBuilder sqlCheckStock = new StringBuilder();
//            sqlCheckStock.append("select owner_id from v_shop_staff where upper(owner_code)=?   and owner_type=?");
//            Query queryCheckStock = em.createNativeQuery(sqlCheckStock.toString());
//            queryCheckStock.setParameter(1, stockCode.toUpperCase());
//            queryCheckStock.setParameter(2, stockType);
//            if (DataUtil.isNullOrEmpty(queryCheckStock.getResultList())) {
//                return 6;//khong ton tai hoac so thue bao khong o trang thai phan phoi
//            }
//        } catch (Exception ex) {
//            return 6;
//        }
//        return 0;
//    }

    public String findStockCode(String stockCode, List<Object[]> listStock, int fromIndex, int toIndex) {
        if (stockCode.compareTo(listStock.get(fromIndex)[0].toString()) < 0 || stockCode.compareTo(listStock.get(toIndex)[0].toString()) > 0) {
            return null;
        }
        int index = (fromIndex + toIndex) / 2;
        if (stockCode.equals(listStock.get(index)[0].toString())) {
            return listStock.get(index)[1].toString();
        } else if (stockCode.compareTo(listStock.get(index)[0].toString()) > 0) {
            return findStockCode(stockCode, listStock, index + 1, toIndex);
        } else {
            return findStockCode(stockCode, listStock, fromIndex, index - 1);
        }
    }

    public int checkIsdn(String isdn, String fromOwnerId, String fromOwnerType, String telecomServiceId) {
        if (DataUtil.isNullObject(isdn)) {
            return 1;//Khong duoc trong
        }
        if (!DataUtil.isNumber(isdn)) {
            return 2;//khong dung dinh dang
        }
        try {
            StringBuilder sqlCheckStatus = new StringBuilder();
            sqlCheckStatus.append("SELECT   1 ");
            sqlCheckStatus.append("  FROM   stock_number s");
            sqlCheckStatus.append(" WHERE   to_number(s.isdn) = ? AND s.status IN (1, 3)");
            sqlCheckStatus.append("AND s.telecom_service_id=? AND s.owner_id=? ");
            sqlCheckStatus.append("AND s.owner_type=? ");
            Query queryCheckStatus = em.createNativeQuery(sqlCheckStatus.toString());
            queryCheckStatus.setParameter(1, DataUtil.safeToLong(isdn));
            queryCheckStatus.setParameter(2, telecomServiceId);
            queryCheckStatus.setParameter(3, fromOwnerId);
            queryCheckStatus.setParameter(4, fromOwnerType);
            if (DataUtil.isNullOrEmpty(queryCheckStatus.getResultList())) {
                return 3;//khong ton tai hoac so thue bao khong o trang thai phan phoi
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return 3;
        }
        try {
            StringBuilder sqlCheckProDuct = new StringBuilder();
            sqlCheckProDuct.append("SELECT   1 ");
            sqlCheckProDuct.append("  FROM   stock_number s, product_offering p");
            sqlCheckProDuct.append(" WHERE       to_number(s.isdn) = ?");
            sqlCheckProDuct.append("         AND s.status IN (1, 3)");
            sqlCheckProDuct.append("         AND p.prod_offer_id = s.prod_offer_id");
            Query queryCheckProduct = em.createNativeQuery(sqlCheckProDuct.toString());
            queryCheckProduct.setParameter(1, DataUtil.safeToLong(isdn));
            if (DataUtil.isNullOrEmpty(queryCheckProduct.getResultList())) {
                return 4;//So chua dc gan loai mat hang hoac laoi mat hang khong ton tai
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return 4;
        }
        return 0;
    }

    public HashMap<String, String> checkIsdnBeforeDistribute(List<String[]> listIsdn, String currentOwnerId, String currentShopPath, String telecomServiceId, RequiredRoleMap requiredRoleMap) {
        HashMap<String, String> map = new HashMap<>();
        Gson gson = new Gson();
        Type typeArrayString = new TypeToken<String[]>() {
        }.getType();
        Type typeVShopStaff = new TypeToken<VShopStaffDTO>() {
        }.getType();
        for (int i = 0; i < listIsdn.size(); i++) {
            String[] ob = new String[4];
            String rowKey = listIsdn.get(i)[0];
            String isdn = listIsdn.get(i)[1];
            String stock = listIsdn.get(i)[2];
            String fromOwner;
            if (DataUtil.isNullObject(isdn)) {
                ob[0] = isdn;
                ob[1] = stock;
                ob[2] = "distribute.error.1";
                map.put(rowKey, gson.toJson(ob, typeArrayString));
                continue;
            }
            if (!DataUtil.isNumber(isdn)) {
                ob[0] = isdn;
                ob[1] = stock;
                ob[2] = "distribute.error.2";
                map.put(rowKey, gson.toJson(ob, typeArrayString));
                continue;
            }
            try {
                StringBuilder sqlCheckIsdn = new StringBuilder();
                sqlCheckIsdn.append("SELECT   s.* ");
                sqlCheckIsdn.append("  FROM   Stock_Number s");
                sqlCheckIsdn.append(" WHERE       to_number(s.isdn) = #isdn ");
                sqlCheckIsdn.append("         AND s.status IN (1,3)");
                sqlCheckIsdn.append("         AND s.telecom_Service_Id =  #telecomServiceId");
                sqlCheckIsdn.append(getRoleQuery(requiredRoleMap));
                Query queryCheckIsdn = em.createNativeQuery(sqlCheckIsdn.toString(), StockNumber.class);
                queryCheckIsdn.setParameter("isdn", DataUtil.safeToLong(isdn));
                queryCheckIsdn.setParameter("telecomServiceId", DataUtil.safeToLong(telecomServiceId));
                List<StockNumber> list = queryCheckIsdn.getResultList();
                if (DataUtil.isNullOrEmpty(list)) {
                    //khong ton tai hoac so thue bao khong o trang thai phan phoi
                    ob[0] = isdn;
                    ob[1] = stock;
                    ob[2] = "distribute.error.3";
                    map.put(rowKey, gson.toJson(ob, typeArrayString));
                    continue;
                } else {
                    StringBuilder sqlCheckShop = new StringBuilder("SELECT v.owner_id, v.owner_type, v.owner_name, v.channel_type_id, v.parent_shop_id, v.owner_code, v.status, v.shop_path, v.staff_owner_id FROM v_shop_staff v WHERE 1=1");
                    sqlCheckShop.append(" AND v.owner_id=#ownerId AND v.owner_type=#ownerType ");
                    sqlCheckShop.append(" AND ( ( exists (SELECT 'X' FROM SHOP WHERE shop_id = #ownerId and (shop_id = #shopId or shop_path like #shopPath)  ) and 1 = #ownerType ) or ( exists  (SELECT 'X' FROM STAFF WHERE staff_id = #ownerId and status=1  and shop_id in  (select shop_id from shop where shop_id = #shopId or shop_path like #shopPath) ) and 2 = #ownerType ) )");
                    Query queryCheckShop = em.createNativeQuery(sqlCheckShop.toString());
                    queryCheckShop.setParameter("ownerId", list.get(0).getOwnerId());
                    queryCheckShop.setParameter("ownerType", list.get(0).getOwnerType());
                    queryCheckShop.setParameter("shopId", currentOwnerId);
                    queryCheckShop.setParameter("shopPath", currentShopPath + "%");
                    List<Object[]> lstShop = queryCheckShop.getResultList();
                    if (DataUtil.isNullOrEmpty(lstShop)) {
                        ob[0] = isdn;
                        ob[1] = stock;
                        ob[2] = "distribute.error.3";
                        map.put(rowKey, gson.toJson(ob, typeArrayString));
                        continue;
                    } else {
                        VShopStaffDTO fromShop = new VShopStaffDTO();
                        int index = 0;
                        fromShop.setOwnerId(DataUtil.safeToString(lstShop.get(0)[index++]));
                        fromShop.setOwnerType(DataUtil.safeToString(lstShop.get(0)[index++]));
                        fromShop.setOwnerName(DataUtil.safeToString(lstShop.get(0)[index++]));
                        fromShop.setChannelTypeId(DataUtil.safeToString(lstShop.get(0)[index++]));
                        fromShop.setParentShopId(DataUtil.safeToString(lstShop.get(0)[index++]));
                        fromShop.setOwnerCode(DataUtil.safeToString(lstShop.get(0)[index++]));
                        fromShop.setStatus(DataUtil.safeToString(lstShop.get(0)[index++]));
                        fromShop.setShopPath(DataUtil.safeToString(lstShop.get(0)[index++]));
                        fromShop.setStaffOwnerId(DataUtil.safeToString(lstShop.get(0)[index]));
                        fromOwner = gson.toJson(fromShop, typeVShopStaff);
                    }
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                //khong ton tai hoac so thue bao khong o trang thai phan phoi
                ob[0] = isdn;
                ob[1] = stock;
                ob[2] = "distribute.error.3";
                map.put(rowKey, gson.toJson(ob, typeArrayString));
                continue;
            }
            try {
                StringBuilder sqlCheckProDuct = new StringBuilder();
                sqlCheckProDuct.append("SELECT   1 ");
                sqlCheckProDuct.append("  FROM   Stock_Number s, Product_Offering p");
                sqlCheckProDuct.append(" WHERE       to_number(s.isdn) = ? ");
                sqlCheckProDuct.append("         AND s.status IN (1,3)");
                sqlCheckProDuct.append("         AND p.prod_Offer_Id = s.prod_Offer_Id");
                Query queryCheckProduct = em.createNativeQuery(sqlCheckProDuct.toString());
                queryCheckProduct.setParameter(1, DataUtil.safeToLong(isdn));
                if (DataUtil.isNullOrEmpty(queryCheckProduct.getResultList())) {
                    //So chua dc gan loai mat hang hoac laoi mat hang khong ton tai
                    ob[0] = isdn;
                    ob[1] = stock;
                    ob[2] = "distribute.error.4";
                    map.put(rowKey, gson.toJson(ob, typeArrayString));
                    continue;
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                //So chua dc gan loai mat hang hoac laoi mat hang khong ton tai
                ob[0] = isdn;
                ob[1] = stock;
                ob[2] = "distribute.error.4";
                map.put(rowKey, gson.toJson(ob, typeArrayString));
                continue;
            }
            ob[0] = isdn;
            ob[1] = stock;
            ob[2] = null;
            ob[3] = fromOwner;
            map.put(rowKey, gson.toJson(ob, typeArrayString));
        }
        return map;
    }

    public List<StockTransSerialDTO> findRangeForExportNote(String fromIsdn, String toIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("        SELECT   MIN (isdn) mn,");
        sql.append("                MAX (isdn) mx,");
        sql.append("                COUNT ( * ) AS so_luong");
        sql.append("        FROM   (SELECT   isdn,");
        sql.append("                status,");
        sql.append("                telecom_service_id,");
        sql.append("                owner_id,");
        sql.append("                (isdn - ROW_NUMBER ()");
        sql.append("                    OVER(  ORDER BY");
        sql.append("                        s.telecom_service_id,");
        sql.append("                        s.status,");
        sql.append("                        s.isdn))");
        sql.append("                 dif");
        sql.append("        FROM   stock_number s");
        sql.append("        WHERE       to_number(isdn) >= ?");
        sql.append("        AND to_number(isdn) <= ?");
        sql.append("        AND prod_offer_id = ?");
        sql.append("        AND owner_id = ?");
        sql.append("        AND owner_type = ?");
        sql.append("        AND status = ?)");
        sql.append("        GROUP BY   dif");
        sql.append("        ORDER BY MN");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, fromIsdn);
        query.setParameter(2, toIsdn);
        query.setParameter(3, prodOfferId);
        query.setParameter(4, ownerId);
        query.setParameter(5, ownerType);
        query.setParameter(6, Const.STOCK_NUMBER_STATUS.NEW);

        List<StockTransSerialDTO> result = Lists.newArrayList();
        List listResult = query.getResultList();
        for (Object o : listResult) {
            Object[] arrNumberRule = (Object[]) o;
            StockTransSerialDTO dto = new StockTransSerialDTO();
            dto.setFromSerial(DataUtil.safeToString(arrNumberRule[0]));
            dto.setToSerial(DataUtil.safeToString(arrNumberRule[1]));
            dto.setQuantity(DataUtil.safeToLong(arrNumberRule[2]));
            result.add(dto);
        }
        return result;
    }

    public List<Object[]> previewDistriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        List<String> listIsdnValidToDistribute = Lists.newArrayList();
        if (DataUtil.isNullObject(inputByFile.getTelecomServiceId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.service.type.not.null");
        }

        String roleStr = "";
        try {
            roleStr = getRoleQuery(requiredRoleMap);
        } catch (LogicException le) {
            throw le;
        }

        StringBuilder sql = new StringBuilder();
        List params = Lists.newArrayList();
        sql.append("SELECT   ss.owner_code,");
        sql.append("           ss.owner_name,");
        sql.append("           to_char(ss.telecom_service_id),");
        sql.append("           ss.from_isdn,");
        sql.append("           ss.to_isdn,");
        sql.append("           to_char(ss.so_luong),");
        sql.append("           ss.status,");
        sql.append("           ROWNUM,");
        sql.append("           ss.owner_id,");
        sql.append("           po.name prod_name,");
        sql.append("           nvl(nf.name,' ') rule_name,");
        sql.append("           ss.owner_type owner_type");
        sql.append("    FROM           (SELECT   s.owner_code,");
        sql.append("                             s.owner_name,");
        sql.append("                             s.owner_type,");
        sql.append("                             sn.telecom_service_id,");
        sql.append("                             sn.mn from_isdn,");
        sql.append("                             sn.mx to_isdn,");
        sql.append("                             sn.so_luong,");
        sql.append("                             sn.status,");
        sql.append("                             s.owner_id,");
        sql.append("                             sn.prod_offer_id,");
        sql.append("                             sn.filter_rule_id");
        sql.append("                      FROM   (  SELECT   MIN (isdn) mn,");
        sql.append("                                         MAX (isdn) mx,");
        sql.append("                                         status,");
        sql.append("                                         telecom_service_id,");
        sql.append("                                         owner_id,");
        sql.append("                                         owner_type,");
        sql.append("                                         prod_offer_id,");
        sql.append("                                         filter_rule_id,");
        sql.append("                                         COUNT ( * ) AS so_luong");
        sql.append("                                  FROM   (SELECT   isdn,");
        sql.append("                                                   status,");
        sql.append("                                                   telecom_service_id,");
        sql.append("                                                   owner_id,");
        sql.append("                                                   owner_type,");
        sql.append("                                                   prod_offer_id,");
        sql.append("                                                   filter_rule_id,");
        sql.append("                                                   isdn");
//        sql.append("                                                    - ROW_NUMBER ()");
//        sql.append("                                                          OVER (");
//        sql.append("                                                              ORDER BY");
//        sql.append("                                                                  s.telecom_service_id,");
//        sql.append("                                                                  s.status,");
//        sql.append("                                                                  s.isdn))");
        sql.append("                                                       dif");
        sql.append("                                            FROM   stock_number s");
        sql.append("                                           WHERE ( to_number(s.isdn) IN (0,");
        for (int index = 1; index <= listIsdn.size(); index++) {
            String isdn = listIsdn.get(index - 1)[0].toString();
            if (index % 1000 == 0) {
                sql.deleteCharAt(sql.length() - 1);
                sql.append(") OR to_number(s.isdn) IN (?,");
                params.add(DataUtil.safeToLong(isdn));
                listIsdnValidToDistribute.add(isdn);
            } else {
                sql.append("?,");
                params.add(DataUtil.safeToLong(isdn));
                listIsdnValidToDistribute.add(isdn);
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append("))");
        sql.append("                                                   AND s.telecom_service_id = ?");
        params.add(inputByFile.getTelecomServiceId());

        sql.append(" AND ( ( exists (SELECT 'X' FROM SHOP WHERE shop_id = s.owner_id and (shop_id = ? or shop_path like ?)  ) and s.owner_type=1 ) or ( exists  (SELECT 'X' FROM STAFF WHERE staff_id = s.owner_id and status=1  and shop_id in  (select shop_id from shop where shop_id = ? or shop_path like ?) ) and s.owner_type=2 ) )");
        params.add(inputByFile.getFromOwnerId());
        params.add(inputByFile.getFromShopPath() + "%");
        params.add(inputByFile.getFromOwnerId());
        params.add(inputByFile.getFromShopPath() + "%");

        sql.append("                                                   AND s.status IN (1,3) ");
        sql.append(roleStr + ") ");
        sql.append("                              GROUP BY   status,");
        sql.append("                                         telecom_service_id,");
        sql.append("                                         owner_id,");
        sql.append("                                         owner_type,");
        sql.append("                                         prod_offer_id,");
        sql.append("                                         filter_rule_id,");
        sql.append("                                         dif) sn,");
        sql.append("                             v_shop_staff s");
        sql.append("                     WHERE   sn.owner_id = s.owner_id");
        sql.append("                             AND sn.owner_type = s.owner_type) ss");
        sql.append("               INNER JOIN");
        sql.append("                   product_offering po");
        sql.append("               ON po.prod_offer_id = ss.prod_offer_id");
        sql.append("           LEFT JOIN");
        sql.append("               number_filter_rule nf");
        sql.append("           ON ss.filter_rule_id = nf.filter_rule_id ");
        sql.append("ORDER BY   to_number(from_isdn)");

        Query query = em.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }

    public HashMap<String, String> distriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        HashMap<String, String> map = new HashMap();
        if (DataUtil.isNullObject(inputByFile.getTelecomServiceId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.service.type.not.null");
        }
        if (DataUtil.isNullObject(inputByFile.getReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "reason.not.null.msg");
        }
        if (!DataUtil.isNullObject(inputByFile.getNote()) && inputByFile.getNote().length() > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }
        String roleStr = "";
        try {
            roleStr = getRoleQuery(requiredRoleMap);
        } catch (LogicException le) {
            throw le;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<VShopStaffDTO>() {
        }.getType();
        Date sysdate;
        try {
            sysdate = DbUtil.getSysDate(em);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
        List<StockIsdnTrans> lstStockIsdnTrans = Lists.newArrayList();
        List<StockIsdnTransDetail> lstStockIsdnTransDetail = Lists.newArrayList();
        for (Object[] ob : listIsdn) {
            String isdn = ob[0].toString();
            List params = Lists.newArrayList();
            VShopStaffDTO toOwner = gson.fromJson(ob[1].toString(), type);
            StringBuilder sql = new StringBuilder();
            VShopStaffDTO fromOwner = gson.fromJson(ob[3].toString(), type);
            sql.append(" update stock_number s  set s.owner_id = ? ,s.owner_type = ? , s.last_update_user = ?, s.last_update_ip_address = ?, s.last_update_time = ? ");
            params.add(toOwner.getOwnerId());
            params.add(toOwner.getOwnerType());
            params.add(inputByFile.getUserCreate());
            params.add(inputByFile.getUserIp());
            params.add(sysdate);
            sql.append(" where to_number(s.isdn)=? AND STATUS IN(1,3)");
            params.add(DataUtil.safeToLong(isdn));
            sql.append(" AND owner_id = ? ");
            params.add(fromOwner.getOwnerId());
            sql.append(" AND owner_type = ?  ");
            params.add(fromOwner.getOwnerType());
            sql.append("AND ( ( exists (SELECT 'X' FROM SHOP WHERE shop_id = s.owner_id and (shop_id = ? or shop_path like ?)  ) and s.owner_type=1 ) or ( exists  (SELECT 'X' FROM STAFF WHERE staff_id = s.owner_id and status=1  and shop_id in  (select shop_id from shop where shop_id = ? or shop_path like ?) ) and s.owner_type=2 ) )");
            params.add(inputByFile.getFromOwnerId());
            params.add(inputByFile.getFromShopPath() + "%");
            params.add(inputByFile.getFromOwnerId());
            params.add(inputByFile.getFromShopPath() + "%");
            sql.append(roleStr);

            Query query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }

            int numRowUpdate = query.executeUpdate();
            if (numRowUpdate == 0) {
                map.put(ob[2] + "", "error.stock.code.isdn");
                continue;
            }
            NumberActionDTO action = new NumberActionDTO();
            action.setFromIsdn(isdn);
            action.setTelecomServiceId(DataUtil.safeToLong(inputByFile.getTelecomServiceId()));
            action.setToIsdn(isdn);
            action.setUserCreate(inputByFile.getUserCode());
            action.setUserIpAddress(inputByFile.getUserIp());
            action.setNote(inputByFile.getNote());
            action.setReasonId(inputByFile.getReasonId());
            action.setActionType(Const.NUMBER_ACTION_TYPE.DISTRIBUTE);
            action.setCreateDate(sysdate);
            NumberAction numberAction = numberActionRepo.save(mapperNumberAction.toPersistenceBean(action));
            NumberActionDetailDTO detailOwnerType = new NumberActionDetailDTO();
            detailOwnerType.setNumberActionId(numberAction.getNumberActionId());

            detailOwnerType.setColumnName("OWNER_TYPE");
            detailOwnerType.setOldValue(fromOwner.getOwnerType());
            detailOwnerType.setNewValue(inputByFile.getToOwnerType());
            detailOwnerType.setOldDetailValue(fromOwner.getOwnerType());
            detailOwnerType.setNewDetailValue(inputByFile.getToOwnerType());
            numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(detailOwnerType));

            NumberActionDetailDTO detailOwnerId = new NumberActionDetailDTO();
            detailOwnerId.setNumberActionId(numberAction.getNumberActionId());
            detailOwnerId.setColumnName("OWNER_ID");
            detailOwnerId.setOldValue(inputByFile.getFromOwnerId().toString());
            detailOwnerId.setNewValue(toOwner.getOwnerId());
            detailOwnerId.setOldDetailValue(fromOwner.getOwnerCode() + "-" + fromOwner.getOwnerName());
            detailOwnerId.setNewDetailValue(toOwner.getOwnerCode() + "-" + toOwner.getOwnerName());
            numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(detailOwnerId));
            //them vao bang stock_isdn_trans
            Long stockIsdnTransId = DbUtil.getSequence(em, "STOCK_ISDN_TRANS_SEQ");
            Long stockIsdnTransId1 = stockIsdnTransId;

            StockIsdnTrans stockIsdnTrans = new StockIsdnTrans();
            stockIsdnTrans.setStockIsdnTransId(stockIsdnTransId);
            String code = genCode(stockIsdnTransId);
            stockIsdnTrans.setStockIsdnTransCode(code);
            stockIsdnTrans.setFromOwnerCode(fromOwner.getOwnerCode());
            stockIsdnTrans.setFromOwnerId(DataUtil.safeToLong(fromOwner.getOwnerId()));
            stockIsdnTrans.setFromOwnerName(fromOwner.getOwnerName());
            stockIsdnTrans.setFromOwnerType(DataUtil.safeToLong(fromOwner.getOwnerType()));
            stockIsdnTrans.setToOwnerCode(toOwner.getOwnerCode());
            stockIsdnTrans.setToOwnerName(toOwner.getOwnerName());
            stockIsdnTrans.setToOwnerId(DataUtil.safeToLong(toOwner.getOwnerId()));
            stockIsdnTrans.setToOwnerType(DataUtil.safeToLong(toOwner.getOwnerType()));
            stockIsdnTrans.setStockTypeId(inputByFile.getTelecomServiceId());
            stockIsdnTrans.setStockTypeName("STOCK_NUMBER");
            stockIsdnTrans.setQuantity(1L);
            stockIsdnTrans.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            stockIsdnTrans.setReasonId(inputByFile.getReasonId());
            stockIsdnTrans.setReasonCode(inputByFile.getReasonCode());
            stockIsdnTrans.setReasonName(inputByFile.getReasonName());
            stockIsdnTrans.setNote(inputByFile.getNote());
            stockIsdnTrans.setCreatedTime(sysdate);
            stockIsdnTrans.setCreatedUserId(inputByFile.getUserId());
            stockIsdnTrans.setCreatedUserName(inputByFile.getUserCreate());
            stockIsdnTrans.setCreatedUserCode(inputByFile.getUserCode());
            stockIsdnTrans.setCreatedUserIp(inputByFile.getUserIp());
            stockIsdnTrans.setLastUpdatedUserId(inputByFile.getUserId());
            stockIsdnTrans.setLastUpdatedUserName(inputByFile.getUserCreate());
            stockIsdnTrans.setLastUpdatedUserCode(inputByFile.getUserCode());
            stockIsdnTrans.setLastUpdatedTime(sysdate);
            stockIsdnTrans.setLastUpdatedUserIp(inputByFile.getUserIp());

            StockIsdnTrans stockIsdnTrans1 = DataUtil.cloneBean(stockIsdnTrans);
            if (stockIsdnTrans1.getStockTypeId().equals(Const.PRODUCT_OFFER_TYPE.MOBILE)) {
                stockIsdnTrans1.setStockTypeId(Const.PRODUCT_OFFER_TYPE.MOBILE);
            } else if (stockIsdnTrans1.getStockTypeId().equals(Const.PRODUCT_OFFER_TYPE.HP)) {
                stockIsdnTrans1.setStockTypeId(Const.PRODUCT_OFFER_TYPE.HP);
            } else {
                stockIsdnTrans1.setStockTypeId(Const.PRODUCT_OFFER_TYPE.PSTN);
            }
            stockIsdnTrans1.setStockIsdnTransId(stockIsdnTransId1);

            lstStockIsdnTrans.add(stockIsdnTrans);

            StockIsdnTransDetail stockIsdnTransDetail = new StockIsdnTransDetail();
            stockIsdnTransDetail.setFromIsdn(isdn);
            stockIsdnTransDetail.setToIsdn(isdn);
            stockIsdnTransDetail.setQuantity(1L);
            stockIsdnTransDetail.setLengthIsdn(new Long((isdn + "").length()));
            stockIsdnTransDetail.setCreatedTime(sysdate);

            StockIsdnTransDetail stockIsdnTransDetail1 = DataUtil.cloneBean(stockIsdnTransDetail);
            stockIsdnTransDetail1.setStockIsdnTransId(stockIsdnTransId1);
            lstStockIsdnTransDetail.add(stockIsdnTransDetail);
            try {
                saveStockIsdn(stockIsdnTrans, stockIsdnTransDetail);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw ex;
            }
        }
        //cap nhat giao dich IM1
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            stockIsdnIm1Service.doDistriButeIsdnByFile(listIsdn, inputByFile, lstStockIsdnTrans, lstStockIsdnTransDetail);
        }
        return map;
    }

    private String getRoleQuery(RequiredRoleMap requiredRoleMap) throws LogicException {
        boolean hasNiceRole = requiredRoleMap.hasRole(Const.PERMISION.ROLE_DISTRIBUTION_NICE_ISDN);
        boolean hasNormalRole = requiredRoleMap.hasRole(Const.PERMISION.ROLE_DISTRIBUTION_NORMAL_ISDN);
        if (!hasNiceRole && !hasNormalRole) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PERMISSION, "isdn.distribute.norole");
        }
        String roleStr = "";
        if (hasNiceRole && hasNormalRole) {
            roleStr = " and s.prod_offer_id is not null ";
        }
        if (hasNiceRole && !hasNormalRole) {
            roleStr = " and (s.prod_offer_id is not null) and (s.filter_rule_id is not null) ";
        }
        if (hasNormalRole && !hasNiceRole) {
            roleStr = " and (s.prod_offer_id is not null) and (s.filter_rule_id is null) ";
        }
        return roleStr;
    }

    public List<StockTransSerialDTO> findByListIsdn(List<String> lstIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("        SELECT   MIN (isdn) mn,");
        sql.append("                MAX (isdn) mx,");
        sql.append("                COUNT ( * ) AS so_luong");
        sql.append("        FROM   (SELECT   isdn,");
        sql.append("                status,");
        sql.append("                telecom_service_id,");
        sql.append("                owner_id,");
        sql.append("                (isdn - ROW_NUMBER ()");
        sql.append("                    OVER(  ORDER BY");
        sql.append("                        s.telecom_service_id,");
        sql.append("                        s.status,");
        sql.append("                        s.isdn))");
        sql.append("                 dif");
        sql.append("        FROM   stock_number s");
        sql.append("        WHERE       prod_offer_id = ?");
        sql.append("        AND owner_id = ?");
        sql.append("        AND owner_type = ?");
        sql.append("        AND status = ?");
        sql.append("        AND to_number(isdn) in (");
        for (int i = 0; i < lstIsdn.size(); i++) {
            sql.append("?");
            if (i == lstIsdn.size() - 1) {
                sql.append(") ");
            } else {
                sql.append(", ");
            }
        }
        sql.append(")");
        sql.append("        GROUP BY   dif");
        sql.append("        ORDER BY MN");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, prodOfferId);
        query.setParameter(2, ownerId);
        query.setParameter(3, ownerType);
        query.setParameter(4, Const.STOCK_NUMBER_STATUS.NEW);
        for (int i = 0; i < lstIsdn.size(); i++) {
            query.setParameter(i + 5, lstIsdn.get(i));
        }

        List<StockTransSerialDTO> result = Lists.newArrayList();
        List listResult = query.getResultList();
        for (Object o : listResult) {
            Object[] arrNumberRule = (Object[]) o;
            StockTransSerialDTO dto = new StockTransSerialDTO();
            dto.setFromSerial(DataUtil.safeToString(arrNumberRule[0]));
            dto.setToSerial(DataUtil.safeToString(arrNumberRule[1]));
            dto.setQuantity(DataUtil.safeToLong(arrNumberRule[2]));
            result.add(dto);
        }
        return result;
    }

    public List<IsdnViewDetailDTO> searchIsdn(InfoSearchIsdnDTO infoSearch, int rowNum, int firstRow) throws LogicException, Exception {
        StringBuilder sql = new StringBuilder();
        List<OptionSetValueDTO> listSpecialShop = Lists.newArrayList();
        try {
            listSpecialShop = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.LOOK_UP_SPECIAL_STOCK), Lists.newArrayList());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        if (DataUtil.isNullObject(infoSearch.getInputFile())) {
            throw new LogicException("", "search.isdn.input.type.require");
        }
        if (infoSearch.getInputFile()) {
            if (DataUtil.isNullObject(infoSearch.getAttachFileName())) {
                throw new LogicException("", "mn.stock.status.isdn.update.file.noselect");
            }
            if (DataUtil.isNullOrEmpty(infoSearch.getListIsdnReadFromFile())) {
                throw new LogicException("", "search.isdn.file.empty");
            }
            if (infoSearch.getListIsdnReadFromFile().size() > 15000) {
                throw new LogicException("", "mn.stock.status.isdn.delete.maxline", 15000);
            }
        } else {
            if (DataUtil.isNullObject(infoSearch.getRequiredRoleMap())) {
                throw new LogicException("", "search.isdn.role.map");
            }
            if (DataUtil.isNullObject(infoSearch.getOwnerType())) {
                throw new LogicException("", "stock.type.not.null");
            }

            if (DataUtil.isNullObject(infoSearch.getOwnerId()) && DataUtil.isNullObject(infoSearch.getRoleTypeKho())) {
                throw new LogicException("", "search.isdn.role.type.kho");
            }
            //Bo bat buoc truyen loai dich vu
//            if (DataUtil.isNullObject(infoSearch.getTelecomServiceId())) {
//                throw new LogicException("", "ws.service.type.not.null");
//            }
            //Bo bat buoc truyen loai dich vu
            if (!checkPermissionViewIsdn(infoSearch.getOwnerCode(), infoSearch.getRequiredRoleMap(), listSpecialShop)) {
                throw new LogicException("", "search.isdn.permision.error");
            }
            if (!DataUtil.isNullObject(infoSearch.getStartRange()) && !DataUtil.isNumber(infoSearch.getStartRange())) {
                throw new LogicException("", "search.isdn.start.range.invalid");
            }
            if (!DataUtil.isNullObject(infoSearch.getEndRange()) && !DataUtil.isNumber(infoSearch.getEndRange())) {
                throw new LogicException("", "search.isdn.end.range.invalid");
            }
            if (!DataUtil.isNullObject(infoSearch.getEndRange()) && !DataUtil.isNullObject(infoSearch.getStartRange())) {
                if (DataUtil.safeToLong(infoSearch.getEndRange()) < DataUtil.safeToLong(infoSearch.getStartRange())) {
                    throw new LogicException("", "search.isdn.start.end.range.invalid");
                }
                /*if (DataUtil.safeToLong(infoSearch.getEndRange()) - DataUtil.safeToLong(infoSearch.getStartRange()) > 100000) {
                    throw new LogicException("", "search.isdn.over.range");
                }*/
            }
            //kiem tra dai gia
            if (!DataUtil.isNullObject(infoSearch.getFromPrice()) && !DataUtil.isNumber(infoSearch.getFromPrice())) {
                throw new LogicException("", "search.isdn.from.price.invalid");
            }
            if (!DataUtil.isNullObject(infoSearch.getToPrice()) && !DataUtil.isNumber(infoSearch.getToPrice())) {
                throw new LogicException("", "search.isdn.to.price.invalid");
            }
            if (!DataUtil.isNullObject(infoSearch.getFromPrice())
                    && !DataUtil.isNullObject(infoSearch.getToPrice())
                    && DataUtil.safeToLong(infoSearch.getToPrice()) < DataUtil.safeToLong(infoSearch.getFromPrice())) {
                throw new LogicException("", "search.isdn.from.to.price.invalid");
            }
        }
        //kiem tra tu ngay den ngay
        if (!DataUtil.isNullObject(infoSearch.getFromDate()) && !DataUtil.isNullObject(infoSearch.getToDate())) {
            if (DateUtil.compareDateToDate(infoSearch.getFromDate(), infoSearch.getToDate()) > 0) {
                throw new LogicException("", "stock.trans.from.to.valid");
            }

            try {
                if (DateUtil.daysBetween2Dates(infoSearch.getToDate(), infoSearch.getFromDate()) > 30) {
                    throw new LogicException("", "stock.trans.from.to.valid");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        List params = Lists.newArrayList();
        sql.append("SELECT sn.isdn,");
        sql.append("       sn.isdn_type AS isdntype,");
        sql.append("       DECODE (sn.owner_type,");
        sql.append("               1, (SELECT shop_code || ' - ' || name");
        sql.append("                     FROM shop s");
        sql.append("                    WHERE s.shop_id = sn.owner_id),");
        sql.append("               2, (SELECT staff_code || ' - ' || name");
        sql.append("                     FROM staff st");
        sql.append("                    WHERE st.staff_id = sn.owner_id))");
        sql.append("           AS code_shop_staff,");
        sql.append("       sn.status,");
        sql.append("       (SELECT po.NAME FROM product_offering po WHERE po.prod_offer_id = sn.prod_offer_id AND ROWNUM < 2) AS prodname,");
        sql.append("       (SELECT nfr.NAME FROM number_filter_rule nfr WHERE nfr.filter_rule_id = sn.filter_rule_id AND ROWNUM < 2)  AS rulename,");
        sql.append("       sn.telecom_service_id,");
        sql.append("       sn.prod_offer_id,");
        sql.append("       sn.owner_id,");
        sql.append("       sn.owner_type,");
        sql.append("       DECODE (sn.owner_type,");
        sql.append("               1, (SELECT shop_code");
        sql.append("                     FROM shop s");
        sql.append("                    WHERE s.shop_id = sn.owner_id),");
        sql.append("               2, (SELECT staff_code");
        sql.append("                     FROM staff st");
        sql.append("                    WHERE st.staff_id = sn.owner_id))");
        sql.append("           AS ownercode,");
        sql.append("       DECODE (sn.owner_type,");
        sql.append("               1, (SELECT name");
        sql.append("                     FROM shop s");
        sql.append("                    WHERE s.shop_id = sn.owner_id),");
        sql.append("               2, (SELECT name");
        sql.append("                     FROM staff st");
        sql.append("                    WHERE st.staff_id = sn.owner_id))");
        sql.append("           AS ownername,");
        sql.append("       CASE sn.telecom_service_id");
        sql.append("           WHEN 1 THEN ''");
        sql.append("           ELSE get_exchange_name (sn.isdn)");
        sql.append("       END");
        sql.append("           AS exchange_name,");
        sql.append("       sn.last_update_time,");
        sql.append("       sn.create_date, ");
        sql.append("       sn.lock_by_staff ");
        sql.append("  FROM stock_number sn ");
        sql.append(" WHERE     1=1");
        if (!DataUtil.isNullOrEmpty(infoSearch.getOwnerIdLock())) {
            sql.append(" and sn.lock_by_staff = ?");
            params.add(DataUtil.safeToLong(infoSearch.getOwnerIdLock()));
        }

        if (!DataUtil.isNullOrZero(infoSearch.getTelecomServiceId())) {
            sql.append(" and sn.telecom_service_id = ?");
            params.add(infoSearch.getTelecomServiceId());
        }

        if (infoSearch.getInputFile()) {
            // nhap theo file
            if (!DataUtil.isNullOrEmpty(infoSearch.getListIsdnReadFromFile())) {
                sql.append(" AND ( ");
                for (int i = 0; i < infoSearch.getListIsdnReadFromFile().size(); i++) {
                    sql.append(" to_number(sn.isdn) = ?");
                    params.add(infoSearch.getListIsdnReadFromFile().get(i));
                    if (i == infoSearch.getListIsdnReadFromFile().size() - 1) {
                        sql.append(") ");
                    } else {
                        sql.append(" OR ");
                    }
                }
            }

//            sql.append(" and ((pop.effect_datetime is null or pop.effect_datetime < sysdate) and (pop.expire_datetime is null or pop.expire_datetime > sysdate)) and (sn.prod_offer_id is null or pop.price_type_id = 2)");
            if (infoSearch.getOwnerType().equals(Const.OWNER_TYPE.SHOP)) {
                sql.append("       AND EXISTS");
                sql.append("               (SELECT sh.shop_code");
                sql.append("                  FROM shop sh");
                sql.append("                 WHERE sh.shop_id = sn.owner_id");
                sql.append("                       AND (sh.shop_code NOT IN");
                sql.append("                               (SELECT osv.VALUE");
                sql.append("                                  FROM option_set_value osv, option_set os");
                sql.append("                                 WHERE osv.status = 1");
                sql.append("                                       AND os.status = 1");
                sql.append("                                       AND osv.option_set_id = os.id");
                sql.append("                                       AND os.code = ?)");
                params.add(Const.OPTION_SET.LOOK_UP_ISND_ROLE_TYPE);

                if (!DataUtil.isNullOrEmpty(infoSearch.getRoleTypeKho().getValues())) {
                    sql.append("                             OR EXISTS");
                    sql.append("                              (SELECT sh.shop_code");
                    sql.append("                                 FROM shop sh");
                    sql.append("                                WHERE sh.shop_id = sn.owner_id");
                    sql.append("                                      AND sh.shop_code IN");
                    sql.append("                                              (SELECT osv.VALUE");
                    sql.append("                                  FROM option_set_value osv, option_set os");
                    sql.append("                                 WHERE osv.status = 1");
                    sql.append("                                       AND os.status = 1");
                    sql.append("                                       AND osv.option_set_id = os.id");
                    sql.append("                                       AND os.code = ?");
                    params.add(Const.OPTION_SET.LOOK_UP_ISND_ROLE_TYPE);
                    sql.append("                                       AND osv.name IN  (");
                    for (int i = 0; i < infoSearch.getRoleTypeKho().getValues().size(); i++) {
                        sql.append("?");
                        params.add(infoSearch.getRoleTypeKho().getValues().get(i));
                        if (i == infoSearch.getRoleTypeKho().getValues().size() - 1) {
                            sql.append(") ");
                        } else {
                            sql.append(", ");
                        }
                    }

                    sql.append("))");
                }
                sql.append("))");
            }
        } else {
            if (!DataUtil.isNullObject(infoSearch.getIsdnType())) {
                sql.append(" and sn.isdn_type = ?");
                params.add(infoSearch.getIsdnType());
            }

            if (DataUtil.isNumber(infoSearch.getStartRange())) {
                sql.append(" and to_number(sn.isdn) >= ? ");
                params.add(DataUtil.safeToLong(infoSearch.getStartRange()));
            }
            if (DataUtil.isNumber(infoSearch.getEndRange())) {
                sql.append(" and to_number(sn.isdn) <= ? ");
                params.add(DataUtil.safeToLong(infoSearch.getEndRange()));
            }

            if (!DataUtil.isNullObject(infoSearch.getStatus())) {
                sql.append(" and sn.status = ?");
                params.add(infoSearch.getStatus());
            }

            //tim kiem theo tap luat
            if (!DataUtil.isNullOrZero(infoSearch.getFilterrRuleId())) {
                sql.append(" and sn.filter_rule_id IN (SELECT   filter_rule_id  FROM   number_filter_rule  WHERE   group_filter_rule_id IN (SELECT group_filter_rule_id FROM group_filter_rule WHERE parent_id=? )) ");
                params.add(infoSearch.getFilterrRuleId());
            }
            // tim kiem theo nhom luat
            if (!DataUtil.isNullOrZero(infoSearch.getGroupFilterRuleId())) {
                sql.append(" and sn.filter_rule_id IN (SELECT   filter_rule_id  FROM   number_filter_rule  WHERE   group_filter_rule_id =? ) ");
                params.add(infoSearch.getGroupFilterRuleId());
            }

            // tim kiem theo luat so dep
            if (!DataUtil.isNullOrZero(infoSearch.getRuleNiceIsdnId())) {
                sql.append(" and sn.filter_rule_id = ?");
                params.add(infoSearch.getRuleNiceIsdnId());
            }

            if (DataUtil.isNumber(infoSearch.getFromPrice()) || DataUtil.isNumber(infoSearch.getToPrice())) {

                sql.append(" and EXISTS (select 'x' from product_offer_price pop where sn.prod_offer_id = pop.prod_offer_id and pop.status=1 ");
                if (DataUtil.isNumber(infoSearch.getFromPrice())) {
                    sql.append(" and pop.price >= ?");
                    params.add(infoSearch.getFromPrice());
                }
                if (DataUtil.isNumber(infoSearch.getToPrice())) {
                    sql.append(" and pop.price <= ?");
                    params.add(infoSearch.getToPrice());
                }
                sql.append(" and ((pop.effect_datetime is null or pop.effect_datetime < sysdate) and (pop.expire_datetime is null or pop.expire_datetime > sysdate)) and pop.price_type_id = 2) ");
            }

            if (!DataUtil.isNullOrZero(infoSearch.getProOfferId())) {
                sql.append(" and sn.prod_offer_id = ?");
                params.add(infoSearch.getProOfferId());
            }
            if (!DataUtil.isNullObject(infoSearch.getOwnerType())) {
                sql.append(" and sn.owner_type = ? ");
                params.add(infoSearch.getOwnerType());
            }
            //neu co nhap kho
            if (infoSearch.getOwnerType().equals(Const.OWNER_TYPE.SHOP)) {
                sql.append("       AND EXISTS");
                sql.append("               (SELECT sh.shop_code");
                sql.append("                  FROM shop sh");
                sql.append("                 WHERE sh.shop_id = sn.owner_id");
                sql.append("                       AND (sh.shop_code NOT IN");
                sql.append("                               (SELECT osv.VALUE");
                sql.append("                                  FROM option_set_value osv, option_set os");
                sql.append("                                 WHERE osv.status = 1");
                sql.append("                                       AND os.status = 1");
                sql.append("                                       AND osv.option_set_id = os.id");
                sql.append("                                       AND os.code = ?)");
                params.add(Const.OPTION_SET.LOOK_UP_ISND_ROLE_TYPE);

                if (!DataUtil.isNullOrEmpty(infoSearch.getRoleTypeKho().getValues())) {
                    sql.append("                             OR EXISTS");
                    sql.append("                              (SELECT sh.shop_code");
                    sql.append("                                 FROM shop sh");
                    sql.append("                                WHERE sh.shop_id = sn.owner_id");
                    sql.append("                                      AND sh.shop_code IN");
                    sql.append("                                              (SELECT osv.VALUE");
                    sql.append("                                  FROM option_set_value osv, option_set os");
                    sql.append("                                 WHERE osv.status = 1");
                    sql.append("                                       AND os.status = 1");
                    sql.append("                                       AND osv.option_set_id = os.id");
                    sql.append("                                       AND os.code = ?");
                    params.add(Const.OPTION_SET.LOOK_UP_ISND_ROLE_TYPE);
                    sql.append("                                       AND osv.name IN  (");
                    for (int i = 0; i < infoSearch.getRoleTypeKho().getValues().size(); i++) {
                        sql.append("?");
                        params.add(infoSearch.getRoleTypeKho().getValues().get(i));
                        if (i == infoSearch.getRoleTypeKho().getValues().size() - 1) {
                            sql.append(") ");
                        } else {
                            sql.append(", ");
                        }
                    }

                    sql.append("))");
                }
                sql.append("))");
            }

            if (!DataUtil.isNullObject(infoSearch.getOwnerId())) {
                sql.append(" and sn.owner_id = ? ");
                params.add(infoSearch.getOwnerId());

                if (infoSearch.getOwnerType().equals(Const.OWNER_TYPE.STAFF)) {

                    sql.append("       AND EXISTS");
                    sql.append("               (SELECT stf.staff_code");
                    sql.append("                  FROM staff stf");
                    sql.append("                 WHERE stf.staff_id = sn.owner_id");
                    sql.append("                       AND (stf.staff_code NOT IN");
                    sql.append("                               (SELECT osv.VALUE");
                    sql.append("                                  FROM option_set_value osv, option_set os");
                    sql.append("                                 WHERE osv.status = 1");
                    sql.append("                                       AND os.status = 1");
                    sql.append("                                       AND osv.option_set_id = os.id");
                    sql.append("                                       AND os.code = ?)))");
                    params.add(Const.OPTION_SET.LOOK_UP_ISND_ROLE_TYPE);
                }
            }

            if (infoSearch.getCheckIsdnLike()
                    && !DataUtil.isNullObject(infoSearch.getIsdnLike())
                    && !DataUtil.isNullObject(infoSearch.getOwnerType())) {
                sql.append(" and sn.isdn like ? ");
                params.add(infoSearch.getIsdnLike());
            }
        }
        if (!DataUtil.isNullObject(infoSearch.getFromDate())) {
            /*sql.append(" and sn.last_update_time >= trunc(?) ");
            params.add(infoSearch.getFromDate());*/

            sql.append(" and sn.update_number >= ? ");
            params.add(DateUtil.date2LongUpdateDateTime(infoSearch.getFromDate()));
        }
        RequiredRoleMap requiredRoleMap = infoSearch.getRequiredRoleMap();
        if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_LOOK_UP_SPECIAL_STOCK) && !infoSearch.getViewSpecialIsdn()) {
            sql.append(" and decode(sn.owner_type, 1, (select shop_code from shop s where s.shop_id = sn.owner_id), ");
            sql.append(" 2, (select  staff_code from staff st where st.staff_id = sn.owner_id)) not in (SELECT  value  FROM   option_set_value WHERE   status=1 and option_set_id IN (SELECT   id FROM   option_set WHERE   status =1 and code =?))");
            params.add(Const.OPTION_SET.LOOK_UP_SPECIAL_STOCK);
        }

        if (!DataUtil.isNullObject(infoSearch.getToDate())) {
            /*sql.append(" and sn.last_update_time < trunc(?)+1 ");
            params.add(infoSearch.getToDate());*/

            sql.append(" and sn.update_number < ? ");
            params.add(DateUtil.date2LongUpdateDateTime(DateUtil.addDay(infoSearch.getToDate(), 1)));
        }
        sql.append(" and rownum <= ? order by sn.isdn");
        params.add(rowNum);
        Query query = em.createNativeQuery(sql.toString());
        query.setHint(QueryHints.JDBC_TIMEOUT, 300);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }

        if (firstRow > 0) {
            query.setMaxResults(firstRow);
        }
        List<Object[]> list = query.getResultList();
        List<IsdnViewDetailDTO> listResult = Lists.newArrayList();
        for (Object[] ob : list) {
            int index = 0;
            IsdnViewDetailDTO rs = new IsdnViewDetailDTO();
            rs.setIsdn(DataUtil.safeToString(ob[index++]));
            rs.setIsdnType(DataUtil.safeToString(ob[index++]));
            rs.setCodeShopStaff(DataUtil.safeToString(ob[index++]));
            rs.setStatus(DataUtil.safeToString(ob[index++]));
            rs.setProName(DataUtil.safeToString(ob[index++]));
            rs.setRuleName(DataUtil.safeToString(ob[index++]));
            rs.setTelecomServiceId(DataUtil.safeToLong(ob[index++]));
            rs.setProOfferId(DataUtil.safeToLong(ob[index++]));
            rs.setOwnerId(DataUtil.safeToLong(ob[index++]));
            rs.setOwnerType(DataUtil.safeToString(ob[index++]));
            rs.setOwnerCode(DataUtil.safeToString(ob[index++]));
            rs.setOwnerName(DataUtil.safeToString(ob[index++]));
            rs.setExchangeName(DataUtil.safeToString(ob[index++]));
//            rs.setPrice(DataUtil.safeToString(ob[index++]));
            rs.setEffectDateTime(DateUtil.date2ddMMyyyyHHMMss((Date) ob[index++]));
            rs.setCreateDateTime(DateUtil.date2ddMMyyyyHHMMss((Date) ob[index++]));
            rs.setLockByStaff(DataUtil.safeToLong(ob[index]));
            rs.setHasPermissionView(checkPermissionViewIsdn(rs.getOwnerCode(), requiredRoleMap, listSpecialShop));
            listResult.add(rs);
        }
        return listResult;
    }

    private boolean checkPermissionViewIsdn(String ownerCode, RequiredRoleMap requiredRoleMap, List<OptionSetValueDTO> listSpecialShop) {
        if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_LOOK_UP_SPECIAL_STOCK)) {
            for (OptionSetValueDTO option : listSpecialShop) {
                if (option.getValue().equals(ownerCode)) {
                    return false;
//                } else {
//                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public List<InfoStockIsdnDto> viewInfoStockIsdn(Long ownerId, Long ownerType) throws Exception {
        List<InfoStockIsdnDto> listInfo = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT   a.telecom_service_id,");
        sql.append("  (select name from option_set_value where option_set_id = (select id from option_set where code='TELCO_FOR_NUMBER') and value = to_char(a.telecom_service_id)),");
        sql.append("           b.code,");
        sql.append("           b.name,");
        sql.append("           COUNT (1)");
        sql.append("    FROM   stock_number a, product_offering b");
        sql.append("   WHERE       a.prod_offer_id = b.prod_offer_id");
        sql.append("           AND a.owner_id = #ownerId");
        sql.append("           AND a.owner_type = #ownerType");
        sql.append("           AND a.status IN (1, 3)");
        sql.append(" GROUP BY   a.telecom_service_id, b.code, b.name ");
        sql.append(" ORDER BY   a.telecom_service_id");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List<Object[]> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            for (Object[] ob : lst) {
                InfoStockIsdnDto info = new InfoStockIsdnDto();
                int index = 0;
                info.setTelecomServiceId(DataUtil.safeToLong(ob[index++]));
                info.setTelecomServiceName(DataUtil.safeToString(ob[index++]));
                info.setProductCode(DataUtil.safeToString(ob[index++]));
                info.setProductName(DataUtil.safeToString(ob[index++]));
                info.setQuantity(DataUtil.safeToLong(ob[index]));
                listInfo.add(info);
            }
        }
        return listInfo;
    }

    @Override
    public boolean checkValidIsdn(String isdn) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT   status");
        strQuery.append("  FROM   bccs_im.stock_number");
        strQuery.append(" WHERE   TO_NUMBER (isdn) = TO_NUMBER (#isdn) AND status IN (1, 3)");
        strQuery.append("   AND   telecom_service_id = 1 ");

        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("isdn", isdn);
        List lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return true;
        }

        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public void lockOrUnlockIsdn(String isdn, Long newStatus, List<Long> lstStatus, StaffDTO staff) throws LogicException, Exception {
        //Cap nhat so
        StringBuilder strUpdateQuery = new StringBuilder("");
        HashMap<String, Object> listParameter = new HashMap<>();

        strUpdateQuery.append("UPDATE   stock_number ");

        strUpdateQuery.append("   SET   status = #newStatus, ");
        listParameter.put("newStatus", newStatus);

        if (DataUtil.isNullObject(staff)) {
            strUpdateQuery.append("  lock_by_staff = null, ");
        } else {
            strUpdateQuery.append("  lock_by_staff = #lockByStaff, last_update_user = #lastUpdateUser, ");
            listParameter.put("lockByStaff", staff.getStaffId());
            listParameter.put("lastUpdateUser", staff.getStaffCode());
        }

        strUpdateQuery.append("  last_update_time = sysdate ");

        strUpdateQuery.append(" WHERE  to_number(isdn) = to_number(#isdn) ");
        listParameter.put("isdn", isdn);

        strUpdateQuery.append("  AND  status " + DbUtil.createInQuery("status", lstStatus));

        strUpdateQuery.append("  AND  telecom_service_id= #telecomServiceId");
        listParameter.put("telecomServiceId", 1L);

        Query query = em.createNativeQuery(strUpdateQuery.toString());
        for (HashMap.Entry<String, Object> param : listParameter.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        DbUtil.setParamInQuery(query, "status", lstStatus);
        int result = query.executeUpdate();

        if (result == 0) {
            throw new LogicException("", "hotline.update.isdn.fail");
        }

        //cap nhat giao dich IM1
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            stockIsdnIm1Service.lockOrUnlockIsdn(isdn, newStatus, lstStatus, staff);
        }

    }

    @Override
    public List<String> lookupIsdn(String fromIsdn, String toIsdn, List<Long> lsOwnerId, List<String> lsStatus, Long maxRow) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT isdn");
        strQuery.append("  FROM (SELECT isdn");
        strQuery.append(" FROM  stock_number ");
        strQuery.append("   WHERE     telecom_service_id = 1 ");
        strQuery.append("         AND owner_id " + DbUtil.createInQuery("owner_id", lsOwnerId));
        strQuery.append("         AND owner_type = #ownerType ");
        strQuery.append("         AND status " + DbUtil.createInQuery("status", lsStatus));
        strQuery.append("         AND TO_NUMBER (isdn) >= TO_NUMBER (#fromIsdn) ");
        strQuery.append("         AND TO_NUMBER (isdn) <= TO_NUMBER (#toIsdn) ");
        strQuery.append("         ORDER BY DBMS_RANDOM.VALUE) ");
        strQuery.append("    WHERE ROWNUM <= #rowNum ");

        Query query = em.createNativeQuery(strQuery.toString());
        DbUtil.setParamInQuery(query, "owner_id", lsOwnerId);
        query.setParameter("ownerType", Const.OWNER_TYPE.SHOP);
        DbUtil.setParamInQuery(query, "status", lsStatus);
        query.setParameter("fromIsdn", fromIsdn);
        query.setParameter("toIsdn", toIsdn);
        query.setParameter("rowNum", maxRow);
        List lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return lst;
        }
        return null;
    }

    public List<Long> getListNumberFromRevokeShop(List<Long> lstOwnerId, Long day) throws Exception {
        List<Long> lstId = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select id, owner_id from stock_number ");
        sql.append(" where owner_id " + DbUtil.createInQuery("owner_id", lstOwnerId));
        sql.append(" and owner_type = 1 ");
//        sql.append(" and telecom_service_id = 1");
        sql.append(" and status = 3");
        sql.append(" and filter_rule_id is not null");
        sql.append(" and last_update_time < trunc(sysdate - #day) ");
        Query query = em.createNativeQuery(sql.toString());
        DbUtil.setParamInQuery(query, "owner_id", lstOwnerId);
        query.setParameter("day", day);
        List listResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(listResult)) {
            for (Object o : listResult) {
                Object[] obj = (Object[]) o;
                lstId.add(DataUtil.safeToLong(obj[0]));
            }
        }
        return lstId;
    }


    public Long updateNumberForReuse(List<Long> lstNumberId, List<Long> lstRevokeShopReuseId, String updateUser, String updateIp, Connection conn) throws Exception {
        PreparedStatement updateNumber = null;
        try {
            StringBuilder strUpdate = new StringBuilder();
            strUpdate.append("UPDATE STOCK_NUMBER SET ");
            strUpdate.append(" owner_id = ?, ");
            strUpdate.append(" status = 1, ");
            strUpdate.append(" last_update_time = sysdate, ");
            strUpdate.append(" last_update_user = ?, ");
            strUpdate.append(" last_update_ip_address = ? ");
            strUpdate.append(" where id = ?");

            updateNumber = conn.prepareStatement(strUpdate.toString());
            Long numberBatch = 0L;
            Long count = 0L;
            for (int i = 0; i < lstNumberId.size(); i++) {
                updateNumber.setLong(1, lstRevokeShopReuseId.get(0));
                updateNumber.setString(2, updateUser);
                updateNumber.setString(3, updateIp);
                updateNumber.setLong(4, lstNumberId.get(i));
                updateNumber.addBatch();
                count++;
                if (i % Const.DEFAULT_BATCH_SIZE == 0 && i > 0) {
                    try {
                        updateNumber.executeBatch();
                        //so ban ghi insert thanh cong
                        conn.commit();
                        ++numberBatch;
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        conn.rollback();
                    }
                }
            }

            //Xu ly voi so ban ghi con lai
            Long numberRemainRecord = lstNumberId.size() - numberBatch * Const.DEFAULT_BATCH_SIZE;
            if (numberRemainRecord > 0) {
                try {
                    updateNumber.executeBatch();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    conn.rollback();
                }
            }
            return count;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            if (updateNumber != null) {
                updateNumber.close();
            }
        }
    }

    public List<Long> getListNumberOwnerTDN(Long shopId, Long day) throws Exception {
        List<Long> lstId = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select id, owner_id from stock_number ");
        sql.append(" where owner_id = #shopId");
        sql.append(" and owner_type = 1 ");
        sql.append(" and status in (1,3)");
        sql.append(" and last_update_time < trunc(sysdate - #day) ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("shopId", shopId);
        query.setParameter("day", day);
        List listResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(listResult)) {
            for (Object o : listResult) {
                Object[] obj = (Object[]) o;
                lstId.add(DataUtil.safeToLong(obj[0]));
            }
        }
        return lstId;
    }

    public Long updateNumberRevokeOwner(List<Long> lstNumberId, Long shopId, String updateUser, Connection conn) throws Exception {
        PreparedStatement updateNumber = null;
        try {
            StringBuilder strUpdate = new StringBuilder();
            strUpdate.append("UPDATE STOCK_NUMBER SET ");
            strUpdate.append(" owner_id = ?, ");
            strUpdate.append(" last_update_time = sysdate, ");
            strUpdate.append(" last_update_user = ?");
            strUpdate.append(" where id = ?");

            updateNumber = conn.prepareStatement(strUpdate.toString());
            Long numberBatch = 0L;
            Long count = 0L;
            for (int i = 0; i < lstNumberId.size(); i++) {
                updateNumber.setLong(1, shopId);
                updateNumber.setString(2, updateUser);
                updateNumber.setLong(3, lstNumberId.get(i));
                updateNumber.addBatch();
                count++;
                if (i % Const.BATCH_SIZE_1000 == 0 && i > 0) {
                    try {
                        updateNumber.executeBatch();
                        //so ban ghi insert thanh cong
                        conn.commit();
                        ++numberBatch;
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        conn.rollback();
                    }
                }
            }

            //Xu ly voi so ban ghi con lai
            Long numberRemainRecord = lstNumberId.size() - numberBatch * Const.BATCH_SIZE_1000;
            if (numberRemainRecord > 0) {
                try {
                    updateNumber.executeBatch();
                    conn.commit();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    conn.rollback();
                }
            }
            return count;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            if (updateNumber != null) {
                updateNumber.close();
            }
        }
    }

    public List<StockIsdnVtShopDTO> getLstIsdnExpriedVtShop() throws Exception {
        List<StockIsdnVtShopDTO> lstStockIsdnVTShopDTOs = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select isdn,request_id from stock_isdn_vt_shop ");
        sql.append(" where status = 0");
        sql.append(" and expired_date < SYSDATE  ");
        Query query = em.createNativeQuery(sql.toString());
        List listResult = query.getResultList();
        if (!DataUtil.isNullOrEmpty(listResult)) {
            for (Object o : listResult) {
                Object[] obj = (Object[]) o;
                StockIsdnVtShopDTO stockIsdnVTShopDTO = new StockIsdnVtShopDTO();
                stockIsdnVTShopDTO.setIsdn(DataUtil.safeToString(obj[0]));
                stockIsdnVTShopDTO.setRequestId(DataUtil.safeToLong(obj[1]));
                lstStockIsdnVTShopDTOs.add(stockIsdnVTShopDTO);
            }
        }
        return lstStockIsdnVTShopDTOs;
    }

    public Long updateIsdnExpriedVtShop(List<StockIsdnVtShopDTO> lstIsdn) throws Exception {
        List<String> lst = Lists.newArrayList();
        for (StockIsdnVtShopDTO stockIsdnVtShopDTO : lstIsdn) {
            lst.add(stockIsdnVtShopDTO.getIsdn());
        }
        StringBuilder strUpdate = new StringBuilder();
        strUpdate.append("UPDATE stock_isdn_vt_shop ");
        strUpdate.append(" SET status=2,");
        strUpdate.append(" last_modify = sysdate ");
        strUpdate.append(" where isdn " + DbUtil.createInQuery("isdn", lst));
        Query query = em.createNativeQuery(strUpdate.toString());
        DbUtil.setParamInQuery(query, "isdn", lst);
        return DataUtil.safeToLong(query.executeUpdate());
    }

    public Long unlockIsdnExpriedVtShop(List<StockIsdnVtShopDTO> lstIsdn) throws Exception {
        List<String> lst = Lists.newArrayList();
        for (StockIsdnVtShopDTO stockIsdnVtShopDTO : lstIsdn) {
            lst.add(stockIsdnVtShopDTO.getIsdn());
        }
        String updateUser = lstIsdn != null && lstIsdn.size() > 1 ? lstIsdn.get(0).getUpdateUser() : "SYS";

        StringBuilder strUpdate = new StringBuilder();
        strUpdate.append("UPDATE stock_number ");
        strUpdate.append(" set status=1,");
        strUpdate.append(" lock_by_staff =null, ");
        strUpdate.append(" last_update_user =#updateUser, ");

        strUpdate.append(" last_update_time=sysdate  ");
        strUpdate.append(" where to_number(isdn) " + DbUtil.createInQuery("isdn", lst) + " and status = 5");
        Query query = em.createNativeQuery(strUpdate.toString());
        DbUtil.setParamInQuery(query, "isdn", lst);
        query.setParameter("updateUser", DataUtil.isNullOrEmpty(updateUser) ? "SYS" : updateUser);
        return DataUtil.safeToLong(query.executeUpdate());
    }

    public Long unlockIsdnExpriedVtShopIM1(List<StockIsdnVtShopDTO> lstIsdn) throws Exception {
        List<String> lst = Lists.newArrayList();
        for (StockIsdnVtShopDTO stockIsdnVtShopDTO : lstIsdn) {
            lst.add(stockIsdnVtShopDTO.getIsdn());
        }
        String updateUser = lstIsdn != null && lstIsdn.size() > 1 ? lstIsdn.get(0).getUpdateUser() : "SYS";

        StringBuilder strUpdate = new StringBuilder();
        strUpdate.append("UPDATE bccs_im.stock_isdn_mobile ");
        strUpdate.append(" set status=1,");
        strUpdate.append(" lock_by_staff =null, ");
        strUpdate.append(" last_update_user =#updateUser, ");
        strUpdate.append(" last_update_time=sysdate  ");
        strUpdate.append(" where to_number(isdn) " + DbUtil.createInQuery("isdn", lst) + " and status = 5");

        Query query = em1.createNativeQuery(strUpdate.toString());
        DbUtil.setParamInQuery(query, "isdn", lst);
        query.setParameter("updateUser", DataUtil.isNullOrEmpty(updateUser) ? "SYS" : updateUser);
        return DataUtil.safeToLong(query.executeUpdate());
    }

    public List<StockNumberDTO> getAllReleaseNumber(Double keepTimeHour, List<Long> lstShopId) throws Exception {
        StringBuilder sql = new StringBuilder("select * FROM stock_number ");
        sql.append(" WHERE update_number < to_number(to_char(SYSDATE - #param,'YYYYMMDDHH24MISS')) AND status = 5");
        sql.append(" AND owner_type=1 ");
        sql.append(" AND telecom_service_id=1 ");
        sql.append(" AND owner_id " + DbUtil.createInQuery("owner_id", lstShopId));
        Query query = em.createNativeQuery(sql.toString(), StockNumber.class);
        query.setParameter("param", keepTimeHour / 24);
        DbUtil.setParamInQuery(query, "owner_id", lstShopId);
        return mapper.toDtoBean(query.getResultList());
    }

    public List<Long> getLstShopIdFromOptionSet(String code) throws Exception {
        StringBuilder sql = new StringBuilder("select shop_id ");
        sql.append(" from shop where shop_code in (select a.value from option_Set_value a, option_set b");
        sql.append(" where  a.option_set_id = b.id  ");
        sql.append(" and a.status = 1 and b.status = 1 ");
        sql.append(" and b.code = #code) ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("code", code);
        return query.getResultList();
    }

    public Long countLockIsdnByStaffId(Long staffId, List<Long> lstShopId) throws Exception {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT count(*) FROM stock_number ");
        sql.append("  WHERE status = 5 AND lock_by_staff =#staffId AND telecom_service_id=1  ");

        if (!DataUtil.isNullOrEmpty(lstShopId)) {
            sql.append(" AND owner_type = 1  ");
            sql.append(" AND owner_id " + DbUtil.createInQuery("owner_id", lstShopId));
        }

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("staffId", staffId);
        if (!DataUtil.isNullOrEmpty(lstShopId)) {
            DbUtil.setParamInQuery(query, "owner_id", lstShopId);
        }
        BigDecimal result = (BigDecimal) query.getSingleResult();
        if (result != null) {
            return result.longValue();
        }
        return 0L;
    }

    public StockNumber findStockNumberByIsdn(String isdn) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM stock_number ");
        sql.append("  WHERE 1=1   ");
        sql.append("  AND to_number(isdn) = TO_NUMBER(#isdn) ");

        Query query = em.createNativeQuery(sql.toString(), StockNumber.class);
        query.setParameter("isdn", isdn);
        List<StockNumber> lsIsdn = (List<StockNumber>) query.getResultList();
        if (!DataUtil.isNullOrEmpty(lsIsdn)) {
            return lsIsdn.get(0);
        }
        return null;
    }

    public Long getConfigQuantityNiceNumberByStaff(String staffCode, String type) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT quantity ");
        sql.append(" FROM CONFIG_LOCK_NICE_NUMBER ");
        sql.append("  WHERE 1=1   ");
        sql.append("  AND lower(staff_code) = lower(#staffCode) ");
        sql.append("  AND type = #type ");
        sql.append("  AND status = 1 ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("staffCode", staffCode);
        query.setParameter("type", type);
        List lsIsdn = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lsIsdn)) {
            return ((BigDecimal) lsIsdn.get(0)).longValue();
        }
        return -1L;
    }

    @Override
    public boolean checkIsdnHaveLockedByStaffId(String isdn, String userCreate) throws Exception {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT   COUNT (1) ");
        sql.append("   FROM   number_action ");
        sql.append("  WHERE       from_isdn = #isdn ");
        sql.append("          AND to_isdn = #isdn ");
        sql.append("          AND action_type = 9 ");
        sql.append("          AND upper(user_create) = #userCreate ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("isdn", isdn);
        query.setParameter("userCreate", userCreate.toUpperCase());

        BigDecimal result = (BigDecimal) query.getSingleResult();
        if (result != null) {
            if (!DataUtil.isNullOrZero(result)) {
                return true;
            }
        }
        return false;
    }
}
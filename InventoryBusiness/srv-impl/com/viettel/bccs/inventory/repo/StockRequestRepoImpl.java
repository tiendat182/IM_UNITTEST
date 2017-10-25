package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.bccs.inventory.model.StockRequest;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.StockRequest.COLUMNS;
import com.viettel.bccs.inventory.model.QStockRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class StockRequestRepoImpl implements StockRequestRepoCustom {

    public static final Logger logger = Logger.getLogger(StockRequestRepoCustom.class);

    private final BaseMapper<StockRequest, StockRequestDTO> mapper = new BaseMapper(StockRequest.class, StockRequestDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QStockRequest stockRequest = QStockRequest.stockRequest;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(stockRequest.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(stockRequest.createUser, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(stockRequest.note, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(stockRequest.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createLongExpression(stockRequest.ownerType, filter);
                        break;
                    case REQUESTCODE:
                        expression = DbUtil.createStringExpression(stockRequest.requestCode, filter);
                        break;
                    case REQUESTTYPE:
                        expression = DbUtil.createLongExpression(stockRequest.requestType, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(stockRequest.status, filter);
                        break;
                    case STOCKREQUESTID:
                        expression = DbUtil.createLongExpression(stockRequest.stockRequestId, filter);
                        break;
                    case STOCKTRANSID:
                        expression = DbUtil.createLongExpression(stockRequest.stockTransId, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(stockRequest.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(stockRequest.updateUser, filter);
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
    public Long getMaxId() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("select MAX (stock_request_id) stockRequestId FROM stock_request");
        Query query = em.createNativeQuery(builder.toString());
        List lsMaxId = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lsMaxId)) {
            return DataUtil.safeToLong(lsMaxId.get(0)) + 1L;
        }
        return 1L;
    }

    @Override
    public List<StockRequestDTO> getLsSearchStockRequest(String requestCode, Date fromDate, Date toDate, String status, Long shopIdLogin, Long ownerId, Long ownerType) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT   sr.stock_request_id,sr.request_code, ");
        sql.append(" sr.owner_type,owner_id,sr.status,sr.stock_trans_id, ");
        sql.append(" sr.create_user,sr.create_datetime,sr.request_type ");

        if (Const.OWNER_TYPE.SHOP_LONG.equals(ownerType)) {
            sql.append(" ,s.shop_code owner_code, s.name owner_name ");
            sql.append(" FROM   stock_request sr, shop s ");
            sql.append(" WHERE   sr.owner_id=s.shop_id ");
            //sql.append(" AND   (sr.owner_id in (select shop_id from shop where parent_shop_id=:shopIdLogin and status = 1) OR sr.owner_id=:shopIdLogin)");
            sql.append(" AND   (sr.owner_id in (select sh.shop_id from shop sh where sh.shop_path like (select psh.shop_path from shop psh where psh.shop_id=#shopIdLogin)||'$_%' ESCAPE '$' " +
                    "or sh.shop_path = (select psh.shop_path from shop psh where psh.shop_id=#shopIdLogin)) OR sr.owner_id=#shopIdLogin)");
        } else if (Const.OWNER_TYPE.STAFF_LONG.equals(ownerType)) {
            sql.append("  ,staff_code owner_code, s.name owner_name ");
            sql.append(" FROM   stock_request sr, staff s ");
            sql.append(" WHERE   sr.owner_id=s.staff_id ");
            sql.append(" AND   (sr.owner_id in (select staff_id from staff where shop_id=#shopIdLogin and status = 1) OR sr.owner_id=#shopIdLogin) ");
        }

        sql.append("		 AND sr.owner_type=#ownerType ");
        if (ownerId != null) {
            sql.append("		 AND sr.owner_id=#ownerId ");
        }
        sql.append("		 AND sr.create_datetime >= TRUNC (#fromDate) ");
        sql.append("		 AND sr.create_datetime <= TRUNC (#endDate) + 1 ");
        if (!DataUtil.isNullOrEmpty(status)) {
            sql.append("		 AND sr.status = #status ");
        }
        if (!DataUtil.isNullOrEmpty(requestCode)) {
            sql.append("		 AND sr.request_code like #requestCode ");
        }
        sql.append("		 ORDER BY sr.create_datetime DESC ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("fromDate", fromDate);
        query.setParameter("endDate", toDate);
        query.setParameter("ownerType", ownerType);
        query.setParameter("shopIdLogin", shopIdLogin);
        if (!DataUtil.isNullOrEmpty(status)) {
            query.setParameter("status", status);
        }
        if (!DataUtil.isNullOrEmpty(requestCode)) {
            query.setParameter("requestCode", "%" + requestCode.trim().toUpperCase() + "%");
        }
        if (ownerId != null) {
            query.setParameter("ownerId", ownerId);
        }
        List<Object[]> lsRequest = query.getResultList();

        List<StockRequestDTO> lsResult = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lsRequest)) {
            for (Object[] obj : lsRequest) {
                int index = 0;
                StockRequestDTO stockRequestDTO = new StockRequestDTO();
                stockRequestDTO.setStockRequestId(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setRequestCode(DataUtil.safeToString(obj[index++]));
                stockRequestDTO.setOwnerType(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setOwnerId(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setStatus(DataUtil.safeToString(obj[index++]));
                stockRequestDTO.setStatusName(IMServiceUtil.getStatusRequestName(stockRequestDTO.getStatus()));
                stockRequestDTO.setStockTransId(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setCreateUser(DataUtil.safeToString(obj[index++]));
                Object date1 = obj[index++];
                stockRequestDTO.setCreateDatetime(date1 != null ? (Date) date1 : null);
                stockRequestDTO.setRequestType(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setOwnerCode(DataUtil.safeToString(obj[index++]));
                stockRequestDTO.setOwnerName(DataUtil.safeToString(obj[index]));
                lsResult.add(stockRequestDTO);
            }
        }
        return lsResult;
    }

    @Override
    public List<StockRequestDTO> getLsSearchStockRequestTTBH(String requestCode, Date fromDate, Date toDate, String status, Long ownerId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("	SELECT sr.stock_request_id, ");
        sql.append("		   sr.request_code, ");
        sql.append("		   sr.owner_type, ");
        sql.append("		   sr.owner_id, ");
        sql.append("		   sr.status, ");
        sql.append("		   sr.stock_trans_id, ");
        sql.append("		   sr.create_user, ");
        sql.append("		   sr.create_datetime, ");
        sql.append("		   sr.request_type, ");
        sql.append("		   (select shop_code owner_code from shop where shop_id=sr.owner_id) shop_code, ");
        sql.append("		   (select name owner_code from shop where shop_id=sr.owner_id) shop_name ");
        sql.append("	FROM   stock_request sr ");
        sql.append("	WHERE       1=1 ");
        sql.append("		   AND sr.owner_type = 1 ");
        sql.append("		   AND sr.request_type = 2 ");
        if (!DataUtil.isNullOrEmpty(status)) {
            sql.append("		 AND sr.status = #status ");
        }
        if (!DataUtil.isNullOrEmpty(requestCode)) {
            sql.append("		 AND sr.request_code like #requestCode ");
        }
        if (ownerId != null) {
            sql.append("		 AND sr.owner_id=#ownerId ");
        }
        sql.append("		   AND sr.create_datetime >= TRUNC (#fromDate) ");
        sql.append("		   AND sr.create_datetime <= TRUNC (#endDate) + 1 ");
        sql.append("	ORDER BY   sr.create_datetime DESC ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("fromDate", fromDate);
        query.setParameter("endDate", toDate);
        if (!DataUtil.isNullOrEmpty(status)) {
            query.setParameter("status", status);
        }
        if (!DataUtil.isNullOrEmpty(requestCode)) {
            query.setParameter("requestCode", "%" + requestCode.trim().toUpperCase() + "%");
        }
        if (ownerId != null) {
            query.setParameter("ownerId", ownerId);
        }
        List<Object[]> lsRequest = query.getResultList();

        List<StockRequestDTO> lsResult = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lsRequest)) {
            for (Object[] obj : lsRequest) {
                int index = 0;
                StockRequestDTO stockRequestDTO = new StockRequestDTO();
                stockRequestDTO.setStockRequestId(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setRequestCode(DataUtil.safeToString(obj[index++]));
                stockRequestDTO.setOwnerType(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setOwnerId(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setStatus(DataUtil.safeToString(obj[index++]));
                stockRequestDTO.setStatusName(IMServiceUtil.getStatusRequestName(stockRequestDTO.getStatus()));
                stockRequestDTO.setStockTransId(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setCreateUser(DataUtil.safeToString(obj[index++]));
                Object date1 = obj[index++];
                stockRequestDTO.setCreateDatetime(date1 != null ? (Date) date1 : null);
                stockRequestDTO.setRequestType(DataUtil.safeToLong(obj[index++]));
                stockRequestDTO.setOwnerCode(DataUtil.safeToString(obj[index++]));
                stockRequestDTO.setOwnerName(DataUtil.safeToString(obj[index]));
                lsResult.add(stockRequestDTO);
            }
        }
        return lsResult;
    }


}
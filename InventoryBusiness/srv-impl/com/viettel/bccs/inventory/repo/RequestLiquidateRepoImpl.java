package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.RequestLiquidateDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.QRequestLiquidate;
import com.viettel.bccs.inventory.model.RequestLiquidate;
import com.viettel.bccs.inventory.model.RequestLiquidate.COLUMNS;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public class RequestLiquidateRepoImpl implements RequestLiquidateRepoCustom {

    public static final Logger logger = Logger.getLogger(RequestLiquidateRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    StaffService staffService;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QRequestLiquidate requestLiquidate = QRequestLiquidate.requestLiquidate;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(requestLiquidate.createDatetime, filter);
                        break;
                    case CREATESHOPID:
                        expression = DbUtil.createLongExpression(requestLiquidate.createShopId, filter);
                        break;
                    case CREATESTAFFID:
                        expression = DbUtil.createLongExpression(requestLiquidate.createStaffId, filter);
                        break;
                    case REQUESTCODE:
                        expression = DbUtil.createStringExpression(requestLiquidate.requestCode, filter);
                        break;
                    case REQUESTLIQUIDATEID:
                        expression = DbUtil.createLongExpression(requestLiquidate.requestLiquidateId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(requestLiquidate.status, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(requestLiquidate.updateDatetime, filter);
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
    public List<RequestLiquidate> getLstRequestLiquidate(RequestLiquidateDTO requestLiquidateDTO) throws Exception {
        List params = Lists.newArrayList();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   * ");
        strQuery.append("   FROM   request_liquidate ");
        strQuery.append("  WHERE       1 = 1 ");
        strQuery.append("          AND create_datetime >= ? ");
        params.add(requestLiquidateDTO.getFromDate());
        strQuery.append("          AND create_datetime <= ? + 1 ");
        params.add(requestLiquidateDTO.getToDate());
        if (!DataUtil.isNullObject(requestLiquidateDTO.getRequestCode())) {
            strQuery.append("      AND lower(request_code) like ? ");
            params.add("%" + requestLiquidateDTO.getRequestCode().trim().toLowerCase() + "%");
        }
        if (!DataUtil.isNullObject(requestLiquidateDTO.getStatus())) {
            strQuery.append("      AND status = ? ");
            params.add(requestLiquidateDTO.getStatus());
        }
        if (!DataUtil.isNullObject(requestLiquidateDTO.getCreateShopId())) {
            strQuery.append("      AND (create_shop_id = ? or create_shop_id in (select owner_id from v_shop_staff where shop_path like ? escape '$'))");
            params.add(requestLiquidateDTO.getCreateShopId());
            params.add("%$_" + requestLiquidateDTO.getCreateShopId() + "$_%");
        } else {
            StaffDTO staffDTO = staffService.findOne(requestLiquidateDTO.getApproveStaffId());
            strQuery.append("      AND (create_shop_id = ? or create_shop_id in (select owner_id from v_shop_staff where shop_path like ? escape '$'))");
            params.add(staffDTO.getShopId());
            params.add("%$_" + staffDTO.getShopId() + "$_%");
        }
        strQuery.append("  ORDER BY request_code ASC ");
        Query query = em.createNativeQuery(strQuery.toString(), RequestLiquidate.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }

    @Override
    public String getRequestCode() throws Exception {
        StringBuilder builder = new StringBuilder("select distinct nvl(max(request_liquidate_id),1) from request_liquidate");
        Query query = em.createNativeQuery(builder.toString());
        Object obj = query.getSingleResult();
        return Const.PrefixCode.importPartnerRequestCode.value() + (DataUtil.safeToLong(obj) + 1L);
    }

    @Override
    public byte[] getFileContent(Long requestLiquidateId) throws Exception {
        StringBuilder builder = new StringBuilder("select document_content from request_liquidate where request_liquidate_id= #requestLiquidateId");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("requestLiquidateId", requestLiquidateId);
        List<Object> lstResult = query.getResultList();
        if (!DataUtil.isNullObject(lstResult.get(0))) {
            return (byte[]) lstResult.get(0);
        }
        return new byte[]{};
    }

    @Override
    public boolean checkProdExist(Long shopId, Long prodOfferId) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   * ");
        strQuery.append("   FROM   request_liquidate a ");
        strQuery.append("  WHERE   create_shop_id = #shopId AND status = #status ");
        strQuery.append("          AND EXISTS ");
        strQuery.append("                 (SELECT   'X' ");
        strQuery.append("                    FROM   request_liquidate_detail ");
        strQuery.append("                   WHERE   prod_offer_id = #prodOfferId ");
        strQuery.append("                           AND request_liquidate_id = a.request_liquidate_id) ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("shopId", shopId);
        query.setParameter("status", Const.LIQUIDATE_STATUS.NEW);
        query.setParameter("prodOfferId", prodOfferId);
        List lst = query.getResultList();
        if (DataUtil.isNullOrEmpty(lst)) {
            return false;
        } else {
            return true;
        }
    }

}
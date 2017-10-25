package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.model.ImportPartnerRequest;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ImportPartnerRequest.COLUMNS;
import com.viettel.bccs.inventory.model.QImportPartnerRequest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ImportPartnerRequestRepoImpl implements ImportPartnerRequestRepoCustom {

    public static final Logger logger = Logger.getLogger(ImportPartnerRequestRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QImportPartnerRequest importPartnerRequest = QImportPartnerRequest.importPartnerRequest;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APPROVESTAFFID:
                        expression = DbUtil.createLongExpression(importPartnerRequest.approveStaffId, filter);
                        break;
                    case CONTRACTCODE:
                        expression = DbUtil.createStringExpression(importPartnerRequest.contractCode, filter);
                        break;
                    case CONTRACTDATE:
                        expression = DbUtil.createDateExpression(importPartnerRequest.contractDate, filter);
                        break;
                    case CONTRACTIMPORTDATE:
                        expression = DbUtil.createDateExpression(importPartnerRequest.contractImportDate, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(importPartnerRequest.createDate, filter);
                        break;
                    case CREATESHOPID:
                        expression = DbUtil.createLongExpression(importPartnerRequest.createShopId, filter);
                        break;
                    case CREATESTAFFID:
                        expression = DbUtil.createLongExpression(importPartnerRequest.createStaffId, filter);
                        break;
                    case CURRENCYTYPE:
                        expression = DbUtil.createStringExpression(importPartnerRequest.currencyType, filter);
                        break;
                    case DELIVERYLOCATION:
                        expression = DbUtil.createStringExpression(importPartnerRequest.deliveryLocation, filter);
                        break;
                    case DOCUMENTCONTENT:
                        expression = DbUtil.createStringExpression(importPartnerRequest.documentContent, filter);
                        break;
                    case DOCUMENTNAME:
                        expression = DbUtil.createStringExpression(importPartnerRequest.documentName, filter);
                        break;
                    case IMPORTDATE:
                        expression = DbUtil.createDateExpression(importPartnerRequest.importDate, filter);
                        break;
                    case IMPORTPARTNERREQUESTID:
                        expression = DbUtil.createLongExpression(importPartnerRequest.importPartnerRequestId, filter);
                        break;
                    case IMPORTSTAFFID:
                        expression = DbUtil.createLongExpression(importPartnerRequest.importStaffId, filter);
                        break;
                    case LASTMODIFY:
                        expression = DbUtil.createDateExpression(importPartnerRequest.lastModify, filter);
                        break;
                    case PARTNERID:
                        expression = DbUtil.createLongExpression(importPartnerRequest.partnerId, filter);
                        break;
                    case POCODE:
                        expression = DbUtil.createStringExpression(importPartnerRequest.poCode, filter);
                        break;
                    case PODATE:
                        expression = DbUtil.createDateExpression(importPartnerRequest.poDate, filter);
                        break;
                    case REASON:
                        expression = DbUtil.createStringExpression(importPartnerRequest.reason, filter);
                        break;
                    case REQUESTCODE:
                        expression = DbUtil.createStringExpression(importPartnerRequest.requestCode, filter);
                        break;
                    case REQUESTDATE:
                        expression = DbUtil.createDateExpression(importPartnerRequest.requestDate, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(importPartnerRequest.status, filter);
                        break;
                    case TOOWNERID:
                        expression = DbUtil.createLongExpression(importPartnerRequest.toOwnerId, filter);
                        break;
                    case TOOWNERTYPE:
                        expression = DbUtil.createLongExpression(importPartnerRequest.toOwnerType, filter);
                        break;
                    case UNITPRICE:
                        expression = DbUtil.createLongExpression(importPartnerRequest.unitPrice, filter);
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
    public List<ImportPartnerRequest> findImportPartnerRequest(ImportPartnerRequestDTO importPartnerRequestDTO) throws Exception {
        List params = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("SELECT * FROM IMPORT_PARTNER_REQUEST WHERE 1=1");

        if (!DataUtil.isNullOrEmpty(importPartnerRequestDTO.getRequestCode())) {
            if (importPartnerRequestDTO.isCheckData()) {
                builder.append(" AND REQUEST_CODE=?");
                params.add(importPartnerRequestDTO.getRequestCode());
            } else {
                builder.append(" AND REQUEST_CODE like ?");
                params.add("%" + importPartnerRequestDTO.getRequestCode().toUpperCase() + "%");
            }
        }

        if (!DataUtil.isNullObject(importPartnerRequestDTO.getStatus())) {
            builder.append(" AND STATUS=?");
            params.add(importPartnerRequestDTO.getStatus());
        }

        if (importPartnerRequestDTO.getFromDate() != null) {
            builder.append(" AND trunc(CREATE_DATE) >= ?");
            params.add(importPartnerRequestDTO.getFromDate());
        }

        if (importPartnerRequestDTO.getToDate() != null) {
            builder.append(" AND trunc(CREATE_DATE) <= ?");
            params.add(importPartnerRequestDTO.getToDate());
        }

        if (importPartnerRequestDTO.getToOwnerId() != null) {
            builder.append(" AND  TO_OWNER_ID=?");
            params.add(importPartnerRequestDTO.getToOwnerId());
        }

        if (importPartnerRequestDTO.getPartnerId() != null) {
            builder.append(" AND  PARTNER_ID=?");
            params.add(importPartnerRequestDTO.getPartnerId());
        }
        builder.append(" order by CREATE_DATE desc");
        Query query = em.createNativeQuery(builder.toString(), ImportPartnerRequest.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }

    @Override
    public Long getSequence() throws Exception {
        return DbUtil.getSequence(em, "IMPORT_PARTNER_REQUEST_SEQ");
    }

    @Override
    public String getRequestCode() throws Exception {
        StringBuilder builder = new StringBuilder("select  distinct nvl(max(import_partner_request_id),1) from import_partner_request");
        Query query = em.createNativeQuery(builder.toString());
        Object obj = query.getSingleResult();
        return Const.PrefixCode.importPartnerRequestCode.value() + (DataUtil.safeToLong(obj) + 1L);
    }

    @Override
    public byte[] getFileContent(Long importPartnerRequestID) throws Exception {
        StringBuilder builder = new StringBuilder("select document_content from import_partner_request where import_partner_request_id= #requestId");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("requestId", importPartnerRequestID);
        List<Object> lstResult = query.getResultList();
        if (!DataUtil.isNullObject(lstResult.get(0))) {
            return (byte[]) lstResult.get(0);
        }
        return new byte[]{};
    }

    @Override
    public void validateStock(Long staffID, Long shopID) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder("select * from staff where shop_id=(select shop_id from shop where shop_id=? and status=?)");
        builder.append(" and status=?");
        builder.append(" and staff_id=?");
        Query check1 = em.createNativeQuery(builder.toString());
        check1.setParameter(1, shopID);
        check1.setParameter(2, Const.STATUS_ACTIVE);
        check1.setParameter(3, Const.STATUS_ACTIVE);
        check1.setParameter(4, staffID);
        List lstCheck = check1.getResultList();
        if (DataUtil.isNullOrEmpty(lstCheck)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.stockUser");
        }
        //check chi nhanh cua VT
        builder = new StringBuilder("SELECT   1");
        builder.append("  FROM   shop sh");
        builder.append(" WHERE   1 = 1");
        builder.append("         AND sh.parent_shop_id = ? ");
        builder.append("         AND sh.status = 1 ");
        builder.append("         AND sh.shop_type = 2");
        builder.append("         AND sh.shop_id NOT IN");
        builder.append("                    (210451,");
        builder.append("                     105501,");
        builder.append("                     19404,");
        builder.append("                     8136,");
        builder.append("                     8138,");
        builder.append("                     210149,");
        builder.append("                     105540,");
        builder.append("                     107794)");
        builder.append("         AND shop_id=?");
        Query check2 = em.createNativeQuery(builder.toString());
        check2.setParameter(1, Const.L_VT_SHOP_ID);
        check2.setParameter(2, shopID);
        lstCheck = check2.getResultList();
        if (DataUtil.isNullOrEmpty(lstCheck)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.branch");
        }
    }

    @Override
    public void approve(ImportPartnerRequestDTO importPartnerRequestDTO) throws LogicException, Exception {
        StringBuilder builder = new StringBuilder("update import_partner_request set status=?, last_modify=sysdate, approve_staff_id=?");
        if (Const.LongSimpleItem.importPartnerRequestStatusRejected.getValue().equals(importPartnerRequestDTO.getStatus())) {
            if (DataUtil.isNullOrEmpty(importPartnerRequestDTO.getReason())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "");
            }
            builder.append(",reason=?, document_name=null, document_content=null");
        }
        builder.append(" where import_partner_request_id =?");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter(1, importPartnerRequestDTO.getStatus());
        query.setParameter(2, importPartnerRequestDTO.getApproveStaffId());

        if (Const.LongSimpleItem.importPartnerRequestStatusRejected.getValue().equals(importPartnerRequestDTO.getStatus())) {
            query.setParameter(3, importPartnerRequestDTO.getReason());
            query.setParameter(4, importPartnerRequestDTO.getImportPartnerRequestId());
        } else {
            query.setParameter(3, importPartnerRequestDTO.getImportPartnerRequestId());
        }
        query.executeUpdate();
    }
}
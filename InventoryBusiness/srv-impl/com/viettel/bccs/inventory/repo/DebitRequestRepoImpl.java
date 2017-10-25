package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import com.viettel.bccs.inventory.model.DebitRequest;
import com.viettel.bccs.inventory.model.DebitRequest.COLUMNS;
import com.viettel.bccs.inventory.model.QDebitRequest;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public class DebitRequestRepoImpl implements DebitRequestRepoCustom {

    public static final Logger logger = Logger.getLogger(DebitRequestRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager emSale;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDebitRequest debitRequest = QDebitRequest.debitRequest;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(debitRequest.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(debitRequest.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(debitRequest.description, filter);
                        break;
                    case FILENAME:
                        expression = DbUtil.createStringExpression(debitRequest.fileName, filter);
                        break;
                    case LASTUPDATETIME:
                        expression = DbUtil.createDateExpression(debitRequest.lastUpdateTime, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(debitRequest.lastUpdateUser, filter);
                        break;
                    case REQUESTCODE:
                        expression = DbUtil.createStringExpression(debitRequest.requestCode, filter);
                        break;
                    case REQUESTID:
                        expression = DbUtil.createLongExpression(debitRequest.requestId, filter);
                        break;
                    case REQUESTOBJECTTYPE:
                        expression = DbUtil.createStringExpression(debitRequest.requestObjectType, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(debitRequest.status, filter);
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
    public byte[] getAttachFileContent(Long debitRequestId) throws Exception {
        StringBuilder builder = new StringBuilder("select file_content from debit_request where request_id= #requestId");
        Query query = em.createNativeQuery(builder.toString());
        query.setParameter("requestId", debitRequestId);
        List<Object> lstResult = query.getResultList();
        if (!DataUtil.isNullObject(lstResult.get(0))) {
            return (byte[]) lstResult.get(0);
        }
        return new byte[]{};
    }

    @Override
    public int deleteDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        StringBuilder dBuilder = new StringBuilder("update debit_request set status=#status, last_update_time= (select sysdate from dual), last_update_user=#user where request_id=#requestId");
        Query dQuery = em.createNativeQuery(dBuilder.toString());
        dQuery.setParameter("status", Const.DEBIT_REQUEST_STATUS.STATUS_REJECT);
        dQuery.setParameter("requestId", debitRequestDTO.getRequestId());
        dQuery.setParameter("user", debitRequestDTO.getCurrentStaff());
        dQuery.executeUpdate();
        return Const.RESULT_CODE.SUCCESS;
    }

    @Override
    public List<DebitRequest> findDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        List params = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        builder.append("select a.REQUEST_ID, a.REQUEST_CODE, a.REQUEST_OBJECT_TYPE, a.STATUS, a.FILE_NAME,")
                .append(" null as FILE_CONTENT, a.CREATE_DATE, a.CREATE_USER, a.LAST_UPDATE_TIME,")
                .append(" a.LAST_UPDATE_USER, a.DESCRIPTION")
                .append(" from debit_request a where 1=1");

        //loc theo shop va shop con cua user dang nhap
        if (!DataUtil.isNullObject(debitRequestDTO.getCurrentShopId())) {
            builder.append(" and upper(a.create_user) in(select upper(st.staff_code)")
                    .append(" from staff st, shop sh ")
                    .append(" where st.shop_id=sh.shop_id")
                    .append(" and (shop_path LIKE '%$_' || #P").append(params.size())
                    .append(" || '$_%' ESCAPE '$'")
                    .append(" or shop_path LIKE '%$_' || #P").append((params.size() + 1))
                    .append(" ESCAPE '$'))");
            params.add(debitRequestDTO.getCurrentShopId());
            params.add(debitRequestDTO.getCurrentShopId());
        }
        //end loc theo shop va shop con cua user dang nhap

        if (!DataUtil.isNullOrEmpty(debitRequestDTO.getCreateUser())) {
            builder.append(" and upper(a.create_user)=#P").append(params.size());
            params.add(debitRequestDTO.getCreateUser().toUpperCase());
        }
        if (!DataUtil.isNullOrEmpty(debitRequestDTO.getRequestObjectType())) {
            builder.append(" and a.request_object_type=#P").append(params.size());
            params.add(DataUtil.safeToLong(debitRequestDTO.getRequestObjectType()));
        }
        if (!DataUtil.isNullOrEmpty(debitRequestDTO.getRequestCode())) {
            builder.append(" and upper(a.REQUEST_CODE) like #P").append(params.size());
            params.add("%" + debitRequestDTO.getRequestCode().toUpperCase() + "%");
        }
        if (!DataUtil.isNullOrEmpty(debitRequestDTO.getStatus())) {
            builder.append(" and a.STATUS=#P").append(params.size());
            params.add(debitRequestDTO.getStatus());
        }

        if (!DataUtil.isNullObject(debitRequestDTO.getFromDate())) {
            builder.append(" and a.CREATE_DATE >= trunc(#P").append(params.size()).append(")");
            params.add(debitRequestDTO.getFromDate());
        }
        if (!DataUtil.isNullObject(debitRequestDTO.getToDate())) {
            builder.append(" and a.CREATE_DATE <= trunc(#P").append(params.size()).append(") +1");
            params.add(debitRequestDTO.getToDate());
        }
        builder.append(" order by LAST_UPDATE_TIME desc");
        Query sQuery = em.createNativeQuery(builder.toString(), DebitRequest.class);
        for (int i = 0; i < params.size(); i++) {
            sQuery.setParameter(("P" + i), params.get(i));
        }
        return sQuery.getResultList();
    }

    @Override
    public Long getSequence() throws Exception {
        return DbUtil.getSequence(em, "debit_request_seq");
    }

    public Long getRevenueInMonth(Long ownerId, String ownerType) throws Exception {
        StringBuilder builder = new StringBuilder(" ");

        builder.append("select  round (sum(decode (a.sale_trans_type, 41, -amount_tax, amount_tax))/30,-5 ) amount ");
        builder.append("  from sale_trans a");
        builder.append(" where sale_trans_date >= sysdate - 30");
        builder.append("   and status in (2,3)");
        if (Const.OWNER_TYPE.SHOP.equals(ownerType)) {
            builder.append("   and shop_id=#ownerId");
        } else {
            builder.append("   and staff_id=#ownerId");
        }

        Query query = emSale.createNativeQuery(builder.toString());
        query.setParameter("ownerId", ownerId);

        BigDecimal bi = (BigDecimal) query.getSingleResult();
        if (bi != null) {
            return bi.longValue();
        }
        return 0L;
    }


}
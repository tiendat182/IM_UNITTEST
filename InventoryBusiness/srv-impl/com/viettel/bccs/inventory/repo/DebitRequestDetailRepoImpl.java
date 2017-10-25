package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import com.viettel.bccs.inventory.dto.DebitRequestDetailDTO;
import com.viettel.bccs.inventory.dto.DebitRequestReportDTO;
import com.viettel.bccs.inventory.model.DebitRequest;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.DebitRequestDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QDebitRequestDetail;

import java.util.Date;
import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class DebitRequestDetailRepoImpl implements DebitRequestDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(DebitRequestDetailRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QDebitRequestDetail debitRequestDetail = QDebitRequestDetail.debitRequestDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case DEBITDAYTYPE:
                        expression = DbUtil.createStringExpression(debitRequestDetail.debitDayType, filter);
                        break;
                    case DEBITVALUE:
                        expression = DbUtil.createLongExpression(debitRequestDetail.debitValue, filter);
                        break;
                    case FINANCETYPE:
                        expression = DbUtil.createStringExpression(debitRequestDetail.financeType, filter);
                        break;
                    case NOTE:
                        expression = DbUtil.createStringExpression(debitRequestDetail.note, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(debitRequestDetail.ownerId, filter);
                        break;
                    case PAYMENTGROUPID:
                        expression = DbUtil.createLongExpression(debitRequestDetail.paymentGroupId, filter);
                        break;
                    case PAYMENTGROUPSERVICEID:
                        expression = DbUtil.createLongExpression(debitRequestDetail.paymentGroupServiceId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createStringExpression(debitRequestDetail.ownerType, filter);
                        break;
                    case REQUESTDATE:
                        expression = DbUtil.createDateExpression(debitRequestDetail.requestDate, filter);
                        break;
                    case REQUESTDETAILID:
                        expression = DbUtil.createLongExpression(debitRequestDetail.requestDetailId, filter);
                        break;
                    case REQUESTID:
                        expression = DbUtil.createLongExpression(debitRequestDetail.requestId, filter);
                        break;
                    case REQUESTTYPE:
                        expression = DbUtil.createStringExpression(debitRequestDetail.requestType, filter);
                        break;
                    case PAYMENTTYPE:
                        expression = DbUtil.createStringExpression(debitRequestDetail.paymentType, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(debitRequestDetail.status, filter);
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
    public List<DebitRequestDetailDTO> findDebitRequestDetailByDebitRequset(DebitRequest debitRequest) throws Exception {
        StringBuilder builder = new StringBuilder();
        List params = Lists.newArrayList();
        builder.append("select a.REQUEST_DETAIL_ID, a.REQUEST_TYPE, ")
                .append(" a.DEBIT_DAY_TYPE, a.REQUEST_DATE, a.OWNER_ID, a.OWNER_TYPE,")
                .append(" a.DEBIT_VALUE, a.STATUS, a.NOTE, a.FINANCE_TYPE, a.REQUEST_ID,")
                .append(" s.finance_type as oldFinanceType, s.debit_day_type as oldDayType, ")
                .append(" s.debit_value oldDebitValue, (select to_char(sh.shop_code)||'-'|| to_char(sh.name) from shop sh, staff st where a.owner_type= '2' and sh.shop_id=st.shop_id and st.staff_id=a.owner_id) as shop_code")
                .append(" ,case a.owner_type")
                .append(" when '1' then (select to_char(sh.shop_code) ||'-'||to_char(sh.name) from shop sh where sh.shop_id=a.owner_id)")
                .append(" else (select to_char(st.staff_code)||'-'|| to_char(st.name) from staff st where st.staff_id=a.owner_id)")
                .append(" end as owner_code,")
                .append(" (select osv.name||'('||ft.day_num||')'")
                .append("                    from finance_type ft, option_set os, option_set_value osv")
                .append("                    where ft.finance_type=osv.value")
                .append("                    and os.id=osv.option_set_id and osv.value=a.finance_type")
                .append("                    and os.code='FINANCE_TYPE' and os.status=1 and osv.status=1 and ft.status=1 and rownum<2) finance_name,")
                .append(" (select osv.name||'('||  trim(TO_CHAR(nvl(dlv.debit_amount,0),'999G999G999G999','NLS_NUMERIC_CHARACTERS = '',.'''))  ||')'")
                .append("                    from debit_level dlv, option_set os, option_set_value osv")
                .append("                    where ")
                .append("                    dlv.debit_level=osv.value")
                .append("                    and os.id=osv.option_set_id and to_char(osv.value)=to_char(a.debit_value) and dlv.debit_day_type=a.debit_day_type")
                .append("                    and os.code='DEBIT_LEVEL' ")
                .append("                    and os.status=1 and osv.status=1 and dlv.status=1 and rownum<2) debit_level_name,")
                .append(" (select osv.name||'('||ft.day_num||')'")
                .append("                    from finance_type ft, option_set os, option_set_value osv")
                .append("                    where ft.finance_type=osv.value")
                .append("                    and os.id=osv.option_set_id and osv.value=s.finance_type")
                .append("                    and os.code='FINANCE_TYPE' and os.status=1 and osv.status=1 and ft.status=1 and rownum<2) old_finance_name,")
                .append(" (select osv.name||'('||  trim(TO_CHAR(nvl(dlv.debit_amount,0),'999G999G999G999','NLS_NUMERIC_CHARACTERS = '',.'''))  ||')'")
                .append("                    from debit_level dlv, option_set os, option_set_value osv")
                .append("                    where ")
                .append("                    dlv.debit_level=osv.value")
                .append("                    and os.id=osv.option_set_id and to_char(osv.value)=to_char(s.debit_value) and dlv.debit_day_type=s.debit_day_type")
                .append("                    and os.code='DEBIT_LEVEL' ")
                .append("                    and os.status=1 and osv.status=1 and dlv.status=1 and rownum<2) old_debit_level_name")

                .append(" from DEBIT_REQUEST_DETAIL a, STOCK_DEBIT s ")
                .append(" where a.OWNER_ID=s.OWNER_ID(+) and a.OWNER_TYPE=s.OWNER_TYPE(+) and a.DEBIT_DAY_TYPE=s.DEBIT_DAY_TYPE(+)")
                .append(" and a.REQUEST_ID=#P0");
        params.add(debitRequest.getRequestId());
        Query query = em.createNativeQuery(builder.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(("P" + i), params.get(i));
        }
        List result = query.getResultList();
        List<DebitRequestDetailDTO> detailDTOs = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object object : result) {
                Object[] resource = (Object[]) object;
                DebitRequestDetailDTO detailDTO = new DebitRequestDetailDTO();
                detailDTO.setRequestDetailId(DataUtil.safeToLong(resource[0]));
                detailDTO.setRequestType(DataUtil.safeToString(resource[1]));
                detailDTO.setDebitDayType(DataUtil.safeToString(resource[2]));
                detailDTO.setRequestDate((Date) resource[3]);
                detailDTO.setOwnerId(DataUtil.safeToLong(resource[4]));
                detailDTO.setOwnerType(DataUtil.safeToString(resource[5]));
                detailDTO.setDebitValue(DataUtil.safeToLong(resource[6]));
                detailDTO.setStatus(DataUtil.safeToString(resource[7]));
                detailDTO.setNote(DataUtil.safeToString(resource[8]));
                detailDTO.setFinanceType(DataUtil.safeToString(resource[9]));
                detailDTO.setRequestId(DataUtil.safeToLong(resource[10]));
                detailDTO.setOldFinanceType(DataUtil.safeToString(resource[11]));
                detailDTO.setOldDebitDayType(DataUtil.safeToString(resource[12]));
                detailDTO.setOldDebitValue(DataUtil.safeToString(resource[13]));
                detailDTO.setShopCode(DataUtil.safeToString(resource[14]));
                detailDTO.setOwnerCode(DataUtil.safeToString(resource[15]));
                detailDTO.setFinanceName(DataUtil.safeToString(resource[16]));
                detailDTO.setDebitLevelName(DataUtil.safeToString(resource[17]));
                detailDTO.setOldFinanceName(DataUtil.safeToString(resource[18]));
                detailDTO.setOldDebitLevelName(DataUtil.safeToString(resource[19]));
                detailDTOs.add(detailDTO);
            }
        }
        return detailDTOs;
    }

    @Override
    public int deleteDebitRequestDetailByDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        StringBuilder cBuilder = new StringBuilder("select * from debit_request_detail a where a.request_id=#requestId and status=#status");
        Query qCheck = em.createNativeQuery(cBuilder.toString(), DebitRequestDetail.class);
        qCheck.setParameter("requestId", debitRequestDTO.getRequestId());
        qCheck.setParameter("status", Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED);
        List<DebitRequestDetail> lstCheck = qCheck.getResultList();
        if (!DataUtil.isNullOrEmpty(lstCheck)) {
            return Const.RESULT_CODE.CONSTAINT;
        }

        StringBuilder builder = new StringBuilder("update debit_request_detail  set status=#status");
        builder.append(" where request_id=#requestId");
        Query qUpdate = em.createNativeQuery(builder.toString());
        qUpdate.setParameter("status", Const.DEBIT_REQUEST_STATUS.STATUS_REJECT);
        qUpdate.setParameter("requestId", debitRequestDTO.getRequestId());
        qUpdate.executeUpdate();
        return Const.RESULT_CODE.SUCCESS;
    }

    public List<DebitRequestReportDTO> getLstDetailForReport(Long requestId) throws Exception {
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" SELECT   b.owner_code,");
        sql.append("          b.owner_name,");
        sql.append("          c.name AS channel_name,");
        sql.append("          (SELECT   DECODE (");
        sql.append("                        owner_type,");
        sql.append("                        2,");
        sql.append("                        (SELECT   z.name");
        sql.append("                           FROM   option_set y, option_set_value z");
        sql.append("                          WHERE       y.id = z.option_set_id");
        sql.append("                                  AND y.status = 1");
        sql.append("                                  AND z.status = 1");
        sql.append("                                  AND y.code = 'DEBIT_LEVEL'");
        sql.append("                                  AND z.VALUE = x.debit_value AND ROWNUM = 1),");
        sql.append("                        NULL)");
        sql.append("             FROM   debit_request_detail x");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND debit_day_type = 1");
        sql.append("                    AND payment_type = 1)");
        sql.append("              AS loai_han_muc_kho,");
        sql.append("          (SELECT   DECODE (");
        sql.append("                        owner_type,");
        sql.append("                        2,");
        sql.append("                        (SELECT   z.debit_amount");
        sql.append("                           FROM   debit_level z");
        sql.append("                          WHERE       z.status = 1");
        sql.append("                                  AND z.debit_level = debit_value");
        sql.append("                                  AND z.debit_day_type = 1),");
        sql.append("                        debit_value)");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND debit_day_type = 1");
        sql.append("                    AND payment_type = 1)");
        sql.append("              hm_kho_thuong,");
        sql.append("          (SELECT   DECODE (");
        sql.append("                        owner_type,");
        sql.append("                        2,");
        sql.append("                        (SELECT   z.debit_amount");
        sql.append("                           FROM   debit_level z");
        sql.append("                          WHERE       z.status = 1");
        sql.append("                                  AND z.debit_level = debit_value");
        sql.append("                                  AND z.debit_day_type = 2),");
        sql.append("                        debit_value)");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND debit_day_type = 2");
        sql.append("                    AND payment_type = 1)");
        sql.append("              hm_kho_km,");
        sql.append("          (SELECT   payment_group_name");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND payment_type = 2 AND ROWNUM = 1)");
        sql.append("              loai_hm_congno,");
        sql.append("          (SELECT   payment_debit_value");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND debit_day_type = 1");
        sql.append("                    AND payment_type = 2)");
        sql.append("              cong_no_thuong,");
        sql.append("          (SELECT   payment_debit_value");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND debit_day_type = 2");
        sql.append("                    AND payment_type = 2)");
        sql.append("              cong_no_km,");
        sql.append("          (SELECT   payment_debit_value");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND debit_day_type = 3");
        sql.append("                    AND payment_type = 2)");
        sql.append("              cong_no_nghi,");
        sql.append("          (SELECT   payment_group_service_name");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND payment_type = 2 AND ROWNUM = 1)");
        sql.append("              nhom_nop_tien,");
        sql.append("          (SELECT   distance");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND rownum = 1)");
        sql.append("              khoang_cach,");
        sql.append("          (SELECT   note");
        sql.append("             FROM   debit_request_detail");
        sql.append("            WHERE       request_id = #requestId");
        sql.append("                    AND status = 1");
        sql.append("                    AND owner_id = a.owner_id");
        sql.append("                    AND owner_type = a.owner_type");
        sql.append("                    AND rownum = 1)");
        sql.append("              note,");
        sql.append("              b.owner_id,");
        sql.append("              b.owner_type");
        sql.append("   FROM   (  SELECT   owner_id, owner_type");
        sql.append("               FROM   debit_request_detail");
        sql.append("              WHERE   request_id = #requestId AND status = 1");
        sql.append("           GROUP BY   owner_id, owner_type) a, mv_shop_staff b, channel_type c");
        sql.append("  WHERE       a.owner_id = b.owner_id");
        sql.append("          AND a.owner_type = b.owner_type");
        sql.append("          AND b.channel_type_id = c.channel_type_id");
        List<DebitRequestReportDTO> lstDetail = Lists.newArrayList();
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("requestId", requestId);
        List result = query.getResultList();
        if (!DataUtil.isNullOrEmpty(result)) {
            for (Object object : result) {
                Object[] resource = (Object[]) object;
                DebitRequestReportDTO detailDTO = new DebitRequestReportDTO();
                int index = 0;
                detailDTO.setStaffBCCS(DataUtil.safeToString(resource[index++]));
                detailDTO.setStaffName(DataUtil.safeToString(resource[index++]));
                detailDTO.setChannelName(DataUtil.safeToString(resource[index++]));
                detailDTO.setDebitDayType(DataUtil.safeToString(resource[index++]));
                detailDTO.setDebitValueUsual(DataUtil.safeToLong(resource[index++]));
                detailDTO.setDebitValuePromotion(DataUtil.safeToLong(resource[index++]));
                detailDTO.setPaymentDayType(DataUtil.safeToString(resource[index++]));
                detailDTO.setPaymentValueUsual(DataUtil.safeToLong(resource[index++]));
                detailDTO.setPaymentValuePromotion(DataUtil.safeToLong(resource[index++]));
                detailDTO.setPaymentValueHoliday(DataUtil.safeToLong(resource[index++]));
                detailDTO.setPaymentGroupServiceName(DataUtil.safeToString(resource[index++]));
                detailDTO.setDistance(DataUtil.safeToString(resource[index++]));
                detailDTO.setNote(DataUtil.safeToString(resource[index++]));
                detailDTO.setOwnerId(DataUtil.safeToLong(resource[index++]));
                detailDTO.setOwnerType(DataUtil.safeToString(resource[index]));
                lstDetail.add(detailDTO);
            }
        }
        return lstDetail;
    }

    public Long checkExsitRequestDetail(DebitRequestDetailDTO debitRequestDetailDTO) throws Exception {
        StringBuilder sqlQuery = new StringBuilder(" ");
        sqlQuery.append(" select request_id, request_detail_id from debit_request_detail");
        sqlQuery.append(" where owner_id = #ownerId");
        sqlQuery.append(" and owner_type = #ownerType");
        sqlQuery.append(" and status = 1");

        StringBuilder sqlQueryDebit = new StringBuilder(" ");
        sqlQueryDebit.append(" and debit_value is not null");
        if (DataUtil.safeEqual(debitRequestDetailDTO.getOwnerType(), Const.OWNER_TYPE.SHOP_LONG)
                && !DataUtil.isNullOrEmpty(debitRequestDetailDTO.getDebitDayType())) {
            sqlQueryDebit.append(" and (debit_day_type = #debitDayType or payment_group_id is not null)");
        }

        StringBuilder sqlQueryPayment = new StringBuilder(" ");
        sqlQueryPayment.append(" and payment_group_id is not null");
        if (DataUtil.safeEqual(debitRequestDetailDTO.getOwnerType(), Const.OWNER_TYPE.SHOP_LONG)
                && !DataUtil.isNullOrEmpty(debitRequestDetailDTO.getDebitDayType())) {
            sqlQueryPayment.append(" and (debit_day_type = #debitDayType or debit_value is not null)");
        }

        if (!DataUtil.isNullOrZero(debitRequestDetailDTO.getDebitValue())) {
            Query query = em.createNativeQuery(sqlQuery.toString() + sqlQueryDebit.toString());
            query.setParameter("ownerId", debitRequestDetailDTO.getOwnerId());
            query.setParameter("ownerType", debitRequestDetailDTO.getOwnerType());
            if (DataUtil.safeEqual(debitRequestDetailDTO.getOwnerType(), Const.OWNER_TYPE.SHOP_LONG)
                    && !DataUtil.isNullOrEmpty(debitRequestDetailDTO.getDebitDayType())) {
                query.setParameter("debitDayType", debitRequestDetailDTO.getDebitDayType());
            }
            List result = query.getResultList();
            if (!DataUtil.isNullOrEmpty(result)) {
                for (Object object : result) {
                    Object[] resource = (Object[]) object;
                    return DataUtil.safeToLong(resource[0]);
                }
            }
        }
        if (!DataUtil.isNullOrZero(debitRequestDetailDTO.getPaymentGroupId())) {
            Query query = em.createNativeQuery(sqlQuery.toString() + sqlQueryPayment.toString());
            query.setParameter("ownerId", debitRequestDetailDTO.getOwnerId());
            query.setParameter("ownerType", debitRequestDetailDTO.getOwnerType());
            if (DataUtil.safeEqual(debitRequestDetailDTO.getOwnerType(), Const.OWNER_TYPE.SHOP_LONG)
                    && !DataUtil.isNullOrEmpty(debitRequestDetailDTO.getDebitDayType())) {
                query.setParameter("debitDayType", debitRequestDetailDTO.getDebitDayType());
            }
            List result = query.getResultList();
            if (!DataUtil.isNullOrEmpty(result)) {
                for (Object object : result) {
                    Object[] resource = (Object[]) object;
                    return DataUtil.safeToLong(resource[0]);
                }
            }
        }
        return null;
    }
}
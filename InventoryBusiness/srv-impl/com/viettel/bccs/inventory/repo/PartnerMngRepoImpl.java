package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.dto.PartnerInputSearch;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.bccs.inventory.model.QPartner;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartnerMngRepoImpl implements PartnerMngRepoCustom {

    public static final Logger logger = Logger.getLogger(ImsiInfoRepoCustom.class);

    @PersistenceContext(
            unitName = "BCCS_IM"
    )
    private EntityManager em;
    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPartner partner = QPartner.partner;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                Partner.COLUMNS column = Partner.COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case ADDRESS:
                        expression = DbUtil.createStringExpression(partner.address, filter); 
                        break;
                    case CONTACTNAME:
                        expression = DbUtil.createStringExpression(partner.contactName, filter); 
                        break;
                    case ENDDATE:
                        expression = DbUtil.createDateExpression(partner.endDate, filter); 
                        break;
                    case FAX:
                        expression = DbUtil.createStringExpression(partner.fax, filter); 
                        break;
                    case PARTNERCODE:
                        expression = DbUtil.createStringExpression(partner.partnerCode, filter); 
                        break;
                    case PARTNERID:
                        expression = DbUtil.createLongExpression(partner.partnerId, filter); 
                        break;
                    case PARTNERNAME:
                        expression = DbUtil.createStringExpression(partner.partnerName, filter); 
                        break;
                    case PARTNERTYPE:
                        expression = DbUtil.createLongExpression(partner.partnerType, filter); 
                        break;
                    case PHONE:
                        expression = DbUtil.createStringExpression(partner.phone, filter); 
                        break;
                    case STADATE:
                        expression = DbUtil.createDateExpression(partner.staDate, filter); 
                        break;
                    case STATUS:
                        expression = DbUtil.createLongExpression(partner.status, filter); 
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
    public List<Partner> searchPartner(PartnerInputSearch partnerInputSearch) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM PARTNER WHERE 1 = 1 AND STATUS != 2 ");
        Map<String, Object> params = new HashMap<String, Object>();

        if (!DataUtil.isNullOrEmpty(partnerInputSearch.getPartnerCode())) {
            sql.append(" AND upper(PARTNER_CODE) LIKE #partnerCode ");
            params.put("partnerCode", "%" + partnerInputSearch.getPartnerCode().trim().toUpperCase() + "%");
        }

        if (!DataUtil.isNullOrEmpty(partnerInputSearch.getPartnerName())) {
            sql.append(" AND upper(PARTNER_NAME) LIKE #partnerName ");
            params.put("partnerName", "%" + partnerInputSearch.getPartnerName().trim().toUpperCase() + "%");
        }

        if (partnerInputSearch.getPartnerType() != null) {
            sql.append(" AND PARTNER_TYPE = #partnerType ");
            params.put("partnerType", partnerInputSearch.getPartnerType());
        }

        if (partnerInputSearch.getStatus() != null) {
            sql.append(" AND STATUS = #status ");
            params.put("status", partnerInputSearch.getStatus());
        }

        if (!DataUtil.isNullOrEmpty(partnerInputSearch.getPhone())) {
            sql.append(" AND upper(PHONE) LIKE #phone ");
            params.put("phone", "%" + partnerInputSearch.getPhone().trim().toUpperCase() + "%");
        }

        if (!DataUtil.isNullOrEmpty(partnerInputSearch.getFax())) {
            sql.append(" AND upper(FAX) LIKE #fax ");
            params.put("fax", "%" + partnerInputSearch.getFax().trim().toUpperCase() + "%");
        }

        if (!DataUtil.isNullOrEmpty(partnerInputSearch.getAddress())) {
            sql.append(" AND upper(ADDRESS) LIKE #address ");
            params.put("address", "%" + partnerInputSearch.getAddress().trim().toUpperCase() + "%");
        }

        if (!DataUtil.isNullOrEmpty(partnerInputSearch.getA4Key())) {
            sql.append(" AND upper(A4KEY) LIKE #a4Key ");
            params.put("a4Key", "%" + partnerInputSearch.getA4Key().trim().toUpperCase() + "%");
        }
        if (!DataUtil.isNullObject(partnerInputSearch.getStartDate())) {
            sql.append("AND trunc(sta_date) >= to_date(#startDate,'DD-MON-YYYY') ");
            params.put("startDate", DateUtil.dateToStringWithPattern(partnerInputSearch.getStartDate(), "dd-MMM-yyyy"));
        }

        if (!DataUtil.isNullObject(partnerInputSearch.getEndDate())) {
            sql.append("AND trunc(end_date) <= to_date(#endDate,'DD-MON-YYYY') ");
            params.put("endDate", DateUtil.dateToStringWithPattern(partnerInputSearch.getEndDate(), "dd-MMM-yyyy"));
        }

        sql.append(" order by nlssort(partner_name,'nls_sort=Vietnamese') asc");
        Query query = em.createNativeQuery(sql.toString(), Partner.class);
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        return query.getResultList();
    }


    public Long countPartnerByPartnerCode(String partnerCode) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM PARTNER WHERE 1 = 1 AND STATUS != 2 ");
        sql.append(" AND upper(PARTNER_CODE) = #partnerCode");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("partnerCode", DataUtil.safeToString(partnerCode).trim().toUpperCase());
        Long count = DataUtil.safeToLong(query.getSingleResult());
        return count;
    }

    public Long countPartnerByA4Key(String a4Key) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM PARTNER WHERE 1 = 1 AND STATUS != 2");
        sql.append(" AND upper(A4KEY) = #a4Key");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("a4Key", DataUtil.safeToString(a4Key).trim().toUpperCase());
        Long count = DataUtil.safeToLong(query.getSingleResult());
        return count;
    }

}
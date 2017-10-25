package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.Partner.COLUMNS;
import com.viettel.bccs.inventory.model.QPartner;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class PartnerRepoImpl implements PartnerRepoCustom {

    public static final Logger logger = Logger.getLogger(PartnerRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QPartner partner = QPartner.partner;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
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
    public List<Partner> findPartner(PartnerDTO partnerDTO) {
        if (DataUtil.isNullObject(partnerDTO)) {
            partnerDTO = new PartnerDTO();
        }
        List params = Lists.newArrayList(Const.STATUS_ACTIVE);
        StringBuilder builder = new StringBuilder("SELECT * FROM PARTNER WHERE STATUS =?");
        if (!DataUtil.isNullOrZero(partnerDTO.getPartnerId())) {
            builder.append(" AND PARTNER_ID=?");
            params.add(partnerDTO.getPartnerId());
        }
        if (!DataUtil.isNullOrZero(partnerDTO.getPartnerType())) {
            builder.append(" AND PARTNER_TYPE=?");
            params.add(partnerDTO.getPartnerType());
        }
        if (!DataUtil.isNullOrEmpty(partnerDTO.getPartnerCode())) {
            if (partnerDTO.isSearchDTO()) {
                builder.append(" AND upper(PARTNER_CODE) like ?");
                params.add("%" + partnerDTO.getPartnerCode().toUpperCase() + "%");
            } else {
                builder.append(" AND PARTNER_CODE=?");
                params.add(partnerDTO.getPartnerCode());
            }
        }
        if (!DataUtil.isNullOrEmpty(partnerDTO.getPartnerName())) {
            if (partnerDTO.isSearchDTO()) {
                builder.append(" AND upper(PARTNER_NAME) like ?");
                params.add("%" + partnerDTO.getPartnerName().toUpperCase() + "%");
            } else {
                builder.append(" AND PARTNER_NAME=?");
                params.add(partnerDTO.getPartnerName());
            }
        }
        builder.append(" ORDER BY   NLSSORT (PARTNER_NAME, 'nls_sort=Vietnamese') ASC ");
        Query query = em.createNativeQuery(builder.toString(), Partner.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }
}
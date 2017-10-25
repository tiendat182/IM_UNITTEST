package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.bccs.inventory.model.ImportPartnerDetail;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.model.ImportPartnerDetail.COLUMNS;
import com.viettel.bccs.inventory.model.QImportPartnerDetail;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ImportPartnerDetailRepoImpl implements ImportPartnerDetailRepoCustom {

    public static final Logger logger = Logger.getLogger(ImportPartnerDetailRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QImportPartnerDetail importPartnerDetail = QImportPartnerDetail.importPartnerDetail;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(importPartnerDetail.createDate, filter);
                        break;
                    case IMPORTPARTNERDETAILID:
                        expression = DbUtil.createLongExpression(importPartnerDetail.importPartnerDetailId, filter);
                        break;
                    case IMPORTPARTNERREQUESTID:
                        expression = DbUtil.createLongExpression(importPartnerDetail.importPartnerRequestId, filter);
                        break;
                    case PRODOFFERID:
                        expression = DbUtil.createLongExpression(importPartnerDetail.prodOfferId, filter);
                        break;
                    case QUANTITY:
                        expression = DbUtil.createLongExpression(importPartnerDetail.quantity, filter);
                        break;
                    case STATEID:
                        expression = DbUtil.createLongExpression(importPartnerDetail.stateId, filter);
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
    public List<ImportPartnerDetail> findImportPartnerDetail(ImportPartnerDetailDTO importPartnerDetailDTO) throws Exception {
        List params = Lists.newArrayList();
        StringBuilder builder = new StringBuilder("SELECT * FROM IMPORT_PARTNER_DETAIL WHERE 1=1");
        if (!DataUtil.isNullOrZero(importPartnerDetailDTO.getImportPartnerRequestId())) {
            builder.append(" AND IMPORT_PARTNER_REQUEST_ID=?");
            params.add(importPartnerDetailDTO.getImportPartnerRequestId());
        }
        if (!DataUtil.isNullOrZero(importPartnerDetailDTO.getStateId())) {
            builder.append(" AND STATE_ID=?");
            params.add(importPartnerDetailDTO.getStateId());
        }
        if (!DataUtil.isNullObject(importPartnerDetailDTO.getCreateDate())) {
            builder.append(" AND trunc(CREATE_DATE)=trunc(?)");
            params.add(importPartnerDetailDTO.getCreateDate());
        }

        if (!DataUtil.isNullOrZero(importPartnerDetailDTO.getImportPartnerDetailId())) {
            builder.append(" AND IMPORT_PARTNER_DETAIL_ID=?");
            params.add(importPartnerDetailDTO.getImportPartnerDetailId());
        }
        Query query = em.createNativeQuery(builder.toString(), ImportPartnerDetail.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }
}
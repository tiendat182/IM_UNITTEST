package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.bccs.inventory.model.InvoiceTemplate.COLUMNS;
import com.viettel.bccs.inventory.model.QInvoiceTemplate;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class InvoiceTemplateRepoImpl implements InvoiceTemplateRepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceTemplateRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceTemplate invoiceTemplate = QInvoiceTemplate.invoiceTemplate;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AMOUNT:
                        expression = DbUtil.createLongExpression(invoiceTemplate.amount, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceTemplate.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceTemplate.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(invoiceTemplate.description, filter);
                        break;
                    case FROMSHOPID:
                        expression = DbUtil.createLongExpression(invoiceTemplate.fromShopId, filter);
                        break;
                    case INVOICETEMPLATEID:
                        expression = DbUtil.createLongExpression(invoiceTemplate.invoiceTemplateId, filter);
                        break;
                    case INVOICETEMPLATETYPEID:
                        expression = DbUtil.createLongExpression(invoiceTemplate.invoiceTemplateTypeId, filter);
                        break;
                    case OWNERID:
                        expression = DbUtil.createLongExpression(invoiceTemplate.ownerId, filter);
                        break;
                    case OWNERTYPE:
                        expression = DbUtil.createStringExpression(invoiceTemplate.ownerType, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceTemplate.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(invoiceTemplate.updateUser, filter);
                        break;
                    case USEDAMOUNT:
                        expression = DbUtil.createLongExpression(invoiceTemplate.usedAmount, filter);
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
    public boolean checkObjectHaveInvoiceTemplate(Long ownerId, Long ownerType) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append(" select 1 from bccs_im.invoice_template  ");
        strQuery.append(" where owner_id = #ownerId and owner_type = #ownerType and amount > 0  ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("ownerType", ownerType);
        List<Object[]> objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            return true;
        }
        return false;
    }

}
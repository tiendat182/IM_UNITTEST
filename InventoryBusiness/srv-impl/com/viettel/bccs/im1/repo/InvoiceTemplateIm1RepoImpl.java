package com.viettel.bccs.im1.repo;


import com.viettel.bccs.im1.model.InvoiceTemplateIm1;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.im1.model.InvoiceTemplateIm1.COLUMNS;
import com.viettel.bccs.im1.model.QInvoiceTemplateIm1;

import java.util.List;

import com.viettel.fw.common.util.DbUtil;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class InvoiceTemplateIm1RepoImpl implements InvoiceTemplateIm1RepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceTemplateIm1RepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceTemplateIm1 invoiceTemplateIm1 = QInvoiceTemplateIm1.invoiceTemplateIm1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case AMOUNT:
                        expression = DbUtil.createLongExpression(invoiceTemplateIm1.amount, filter);
                        break;
                    case BOOKTYPEID:
                        expression = DbUtil.createLongExpression(invoiceTemplateIm1.bookTypeId, filter);
                        break;
                    case CREATEDATE:
                        expression = DbUtil.createDateExpression(invoiceTemplateIm1.createDate, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceTemplateIm1.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(invoiceTemplateIm1.description, filter);
                        break;
                    case INVOICETEMPLATEID:
                        expression = DbUtil.createLongExpression(invoiceTemplateIm1.invoiceTemplateId, filter);
                        break;
                    case LASTUPDATEDATE:
                        expression = DbUtil.createDateExpression(invoiceTemplateIm1.lastUpdateDate, filter);
                        break;
                    case LASTUPDATEUSER:
                        expression = DbUtil.createStringExpression(invoiceTemplateIm1.lastUpdateUser, filter);
                        break;
                    case PREAMOUNT:
                        expression = DbUtil.createLongExpression(invoiceTemplateIm1.preAmount, filter);
                        break;
                    case SERIALNO:
                        expression = DbUtil.createStringExpression(invoiceTemplateIm1.serialNo, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(invoiceTemplateIm1.shopId, filter);
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(invoiceTemplateIm1.staffId, filter);
                        break;
                    case TYPE:
                        expression = DbUtil.createLongExpression(invoiceTemplateIm1.type, filter);
                        break;
                    case USEDAMOUNT:
                        expression = DbUtil.createLongExpression(invoiceTemplateIm1.usedAmount, filter);
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
    public InvoiceTemplateIm1 getInvoiceTemplate(Long ownerId, Long type) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        if (DataUtil.safeEqual(type, Const.OWNER_TYPE.SHOP_LONG)) {
            strQuery.append("select * from bccs_im.Invoice_Template where shop_Id = #ownerId and type = #type order by invoice_Template_Id desc ");
        } else if (DataUtil.safeEqual(type, Const.OWNER_TYPE.STAFF_LONG)) {
            strQuery.append("select * from bccs_im.Invoice_Template where staff_Id = #ownerId and type = #type " +
                    " and shop_Id in (select shop_Id from bccs_im.Staff where staff_Id = #ownerId) order by invoice_Template_Id desc");
        }
        Query query = emIM.createNativeQuery(strQuery.toString(), InvoiceTemplateIm1.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("type", type);
        List<InvoiceTemplateIm1> invoiceTemplate = query.getResultList();
        if (!invoiceTemplate.isEmpty()) {
            return invoiceTemplate.get(0);
        }
        return null;
    }

    @Override
    public List<InvoiceTemplateIm1> getInvoiceTemplateShop(Long shopId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from bccs_im.invoice_template where shop_id = #shopId " +
                " and type = 1 ");
        Query query = emIM.createNativeQuery(strQuery.toString(), InvoiceTemplateIm1.class);
        query.setParameter("shopId", shopId);
        List invoiceTemplate = query.getResultList();
        return invoiceTemplate;
    }
}
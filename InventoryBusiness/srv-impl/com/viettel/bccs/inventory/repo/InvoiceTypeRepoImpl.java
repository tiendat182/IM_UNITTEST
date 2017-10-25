package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.bccs.inventory.model.InvoiceType;
import com.viettel.bccs.inventory.model.InvoiceType.COLUMNS;
import com.viettel.bccs.inventory.model.QInvoiceType;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class InvoiceTypeRepoImpl implements InvoiceTypeRepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceTypeRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<InvoiceType, InvoiceTypeDTO> mapper = new BaseMapper(InvoiceType.class, InvoiceTypeDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceType invoiceType = QInvoiceType.invoiceType1;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case BLOCKNOLENGTH:
                        expression = DbUtil.createLongExpression(invoiceType.blockNoLength, filter);
                        break;
                    case BOOKTYPE:
                        expression = DbUtil.createStringExpression(invoiceType.bookType, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceType.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceType.createUser, filter);
                        break;
                    case INVOICENAME:
                        expression = DbUtil.createStringExpression(invoiceType.invoiceName, filter);
                        break;
                    case INVOICENOLENGTH:
                        expression = DbUtil.createLongExpression(invoiceType.invoiceNoLength, filter);
                        break;
                    case INVOICETYPE:
                        expression = DbUtil.createStringExpression(invoiceType.invoiceType, filter);
                        break;
                    case INVOICETYPEID:
                        expression = DbUtil.createLongExpression(invoiceType.invoiceTypeId, filter);
                        break;
                    case NUMINVOICE:
                        expression = DbUtil.createLongExpression(invoiceType.numInvoice, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(invoiceType.status, filter);
                        break;
                    case TYPE:
                        expression = DbUtil.createStringExpression(invoiceType.type, filter);
                        break;
                    case TYPEINV:
                        expression = DbUtil.createStringExpression(invoiceType.typeInv, filter);
                        break;
                    case UPDATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceType.updateDatetime, filter);
                        break;
                    case UPDATEUSER:
                        expression = DbUtil.createStringExpression(invoiceType.updateUser, filter);
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
    public List<InvoiceTypeDTO> getInvoiceType(Long type) throws Exception, LogicException {
        List<InvoiceTypeDTO> result = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT INVOICE_TYPE_ID, TYPE_INV, INVOICE_NAME FROM INVOICE_TYPE WHERE INVOICE_TYPE = ? AND STATUS = 1 ORDER BY NLSSORT(TYPE_INV, 'NLS_SORT=VIETNAMESE')");
        List<Object> lstParam = Lists.newArrayList();
        lstParam.add(type);
        Query query = em.createNativeQuery(sql.toString());
        int i = 0;
        for (Object object : lstParam) {
            query.setParameter(++i, object);
        }
        List<Object[]> lstResult = query.getResultList();
        if (lstResult != null && lstResult.size() > 0) {
            for (Object[] object : lstResult) {
                int index = 0;
                InvoiceTypeDTO invoiceListDTO = new InvoiceTypeDTO();
                invoiceListDTO.setInvoiceTypeId(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceListDTO.setTypeInv(DataUtil.safeToString(object[index++]));
                invoiceListDTO.setInvoiceName(DataUtil.safeToString(object[index]));

                result.add(invoiceListDTO);
            }
        }
        return result;
    }
}
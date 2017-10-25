package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceListDTO;
import com.viettel.bccs.inventory.model.InvoiceList;
import com.viettel.bccs.inventory.model.InvoiceList.COLUMNS;
import com.viettel.bccs.inventory.model.QInvoiceList;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class InvoiceListRepoImpl implements InvoiceListRepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceListRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<InvoiceList, InvoiceListDTO> mapper = new BaseMapper(InvoiceList.class, InvoiceListDTO.class);
    @Autowired
    private OptionSetValueService optionSetValueSv;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceList invoiceList = QInvoiceList.invoiceList;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case APPROVEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceList.approveDatetime, filter);
                        break;
                    case APPROVEUSER:
                        expression = DbUtil.createStringExpression(invoiceList.approveUser, filter);
                        break;
                    case BLOCKNO:
                        expression = DbUtil.createStringExpression(invoiceList.blockNo, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceList.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceList.createUser, filter);
                        break;
                    case CURRINVOICE:
                        expression = DbUtil.createLongExpression(invoiceList.currInvoice, filter);
                        break;
                    case DESTROYDATE:
                        expression = DbUtil.createDateExpression(invoiceList.destroyDate, filter);
                        break;
                    case DESTROYREASONID:
                        expression = DbUtil.createStringExpression(invoiceList.destroyReasonId, filter);
                        break;
                    case DESTROYUSER:
                        expression = DbUtil.createStringExpression(invoiceList.destroyUser, filter);
                        break;
                    case FROMINVOICE:
                        expression = DbUtil.createLongExpression(invoiceList.fromInvoice, filter);
                        break;
                    case INVOICELISTID:
                        expression = DbUtil.createLongExpression(invoiceList.invoiceListId, filter);
                        break;
                    case INVOICESERIALID:
                        expression = DbUtil.createLongExpression(invoiceList.invoiceSerialId, filter);
                        break;
                    case SERIALNO:
                        expression = DbUtil.createStringExpression(invoiceList.serialNo, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(invoiceList.shopId, filter);
                        break;
                    case STAFFID:
                        expression = DbUtil.createLongExpression(invoiceList.staffId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(invoiceList.status, filter);
                        break;
                    case TOINVOICE:
                        expression = DbUtil.createLongExpression(invoiceList.toInvoice, filter);
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
    public List<InvoiceListDTO> getAllInvoiceListByShopId(Long shopId) throws Exception {
        List<InvoiceList> lst;
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from invoice_list where shop_id = #shopId and status = 1 and curr_invoice < to_invoice");
        //execute query
        Query query = em.createNativeQuery(strQuery.toString(), InvoiceList.class);
        query.setParameter("shopId", shopId);
        lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return null;
    }

    @Override
    public List<InvoiceListDTO> getAllInvoiceListBySerialCode(String serialCode) throws Exception {
        List<InvoiceList> lst;
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from invoice_list where serial_no = #serialCode and status = 1 and curr_invoice < to_invoice");
        //execute query
        Query query = em.createNativeQuery(strQuery.toString(), InvoiceList.class);
        query.setParameter("serialCode", serialCode);
        lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return null;
    }

    @Override
    public List<InvoiceListDTO> getAllInvoiceListByInvoiceListId(Long invoiceListId) throws Exception {
        List<InvoiceList> lst;
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select * from invoice_list where invoice_list_id = #invoiceListId for update nowait");
        //execute query
        Query query = em.createNativeQuery(strQuery.toString(), InvoiceList.class);
        query.setParameter("invoiceListId", invoiceListId);
        lst = query.getResultList();
        if (lst != null && !lst.isEmpty()) {
            return mapper.toDtoBean(lst);
        }
        return null;
    }

    @Override
    public BaseMessage updateInvoiceListByInvoiceListId(Long invoiceListId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        BaseMessage baseMessage = new BaseMessage(false);
        strQuery.append("update invoice_list set curr_invoice = curr_invoice + 1 where invoice_list_id = #invoiceListId");
        //execute query
        Query query = em.createNativeQuery(strQuery.toString(), InvoiceList.class);
        query.setParameter("invoiceListId", invoiceListId);
        int result = query.executeUpdate();
        if (result > 0) {
            baseMessage.setSuccess(true);
        }
        return baseMessage;
    }

    @Override
    public List<InvoiceListDTO> searchInvoiceList(InvoiceListDTO invoiceListDTO) throws Exception {
        List<InvoiceListDTO> result = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   a.invoice_list_id,");
        sql.append("         b.invoice_type_id,");
        sql.append("         a.invoice_serial_id,");
        sql.append("         b.invoice_type,");
        sql.append("         b.type_inv || ' - ' || b.invoice_name,");
        sql.append("         a.serial_no,");
        sql.append("         a.from_invoice,");
        sql.append("         a.to_invoice,");
        sql.append("         a.curr_invoice,");
        sql.append("         d.name,");
        sql.append("          (SELECT   osv.name");
        sql.append("            FROM   option_set os, option_set_value osv");
        sql.append("            WHERE       os.id = osv.option_set_id");
        sql.append("                   AND os.code = 'INVOICE_TYPE'");
        sql.append("                   AND osv.VALUE = b.invoice_type)");
        sql.append("             invoice_type_name");
        sql.append("  FROM   invoice_list a, invoice_type b, invoice_serial c, shop d");
        sql.append(" WHERE   a.invoice_serial_id = c.invoice_serial_id AND c.invoice_type_id = b.invoice_type_id");
        sql.append("         AND d.shop_id = a.shop_id AND a.status = 1");
        sql.append("         AND b.status = 1 AND c.status = 1");
        List<Object> lstParam = Lists.newArrayList();
        if (!DataUtil.isNullObject(invoiceListDTO.getShopId())) {
            sql.append("         AND a.shop_id = ?");
            lstParam.add(invoiceListDTO.getShopId().toString().trim());
        }
        if (!DataUtil.isNullObject(invoiceListDTO.getInvoiceType())) {
            sql.append("         AND b.invoice_type = ?");
            lstParam.add(invoiceListDTO.getInvoiceType().trim());
        }
        if (!DataUtil.isNullObject(invoiceListDTO.getInvoiceTypeId())) {
            sql.append("         AND b.invoice_type_id = ?");
            lstParam.add(invoiceListDTO.getInvoiceTypeId().toString().trim());
        }
        if (!DataUtil.isNullObject(invoiceListDTO.getInvoiceSerialId())) {
            sql.append("         AND a.invoice_serial_id = ?");
            lstParam.add(invoiceListDTO.getInvoiceSerialId().toString().trim());
        }
        if (!DataUtil.isNullObject(invoiceListDTO.getFromInvoice())) {
            sql.append("         AND a.to_invoice >= ?");
            lstParam.add(invoiceListDTO.getFromInvoice().toString().trim());
        }
        if (!DataUtil.isNullObject(invoiceListDTO.getToInvoice())) {
            sql.append("         AND a.from_invoice <= ?");
            lstParam.add(invoiceListDTO.getToInvoice().toString().trim());
        }
        if (!DataUtil.isNullObject(invoiceListDTO.getInvoiceTrans())) {
            sql.append("         AND nvl(c.invoice_trans,1) = ? ");
            lstParam.add(invoiceListDTO.getInvoiceTrans().toString().trim());
        }
        sql.append("           AND ROWNUM < 1000");
        sql.append(" ORDER BY   a.create_datetime DESC");
        Query query = em.createNativeQuery(sql.toString());
        int i = 0;
        for (Object object : lstParam) {
            query.setParameter(++i, object);
        }
        List<Object[]> lstResult = query.getResultList();
        if (lstResult != null && lstResult.size() > 0) {
            for (Object[] object : lstResult) {
                int index = 0;
                InvoiceListDTO invoiceListDTO1 = new InvoiceListDTO();
                invoiceListDTO1.setInvoiceListId(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceListDTO1.setInvoiceTypeId(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceListDTO1.setInvoiceSerialId(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceListDTO1.setInvoiceType(DataUtil.safeToString(object[index++]));
                invoiceListDTO1.setInvoiceInv(DataUtil.safeToString(object[index++]));
                invoiceListDTO1.setSerialNo(DataUtil.safeToString(object[index++]));
                invoiceListDTO1.setFromInvoice(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceListDTO1.setToInvoice(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceListDTO1.setCurrInvoice(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceListDTO1.setShopName(DataUtil.safeToString(object[index++]));
                invoiceListDTO1.setInvoiceTypeName(DataUtil.safeToString(object[index]));
                result.add(invoiceListDTO1);
            }
        }
        return result;
    }

    @Override
    public boolean checkStaffHaveInvoiceList(Long staffId) throws Exception {
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append(" select 1 from bccs_im.invoice_list  ");
        strQuery.append(" where staff_id = #staffId and status = 3 ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("staffId", staffId);
        List<Object[]> objects = query.getResultList();
        if (!DataUtil.isNullOrEmpty(objects)) {
            return true;
        }
        return false;
    }
}
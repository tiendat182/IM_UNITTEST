package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceSerialDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.model.InvoiceSerial.COLUMNS;
import com.viettel.bccs.inventory.model.QInvoiceSerial;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class InvoiceSerialRepoImpl implements InvoiceSerialRepoCustom {

    public static final Logger logger = Logger.getLogger(InvoiceSerialRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private final BaseMapper<Shop, ShopDTO> mapper = new BaseMapper(Shop.class, ShopDTO.class);

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        logger.info("Entering predicates :: " + filters);
        QInvoiceSerial invoiceSerial = QInvoiceSerial.invoiceSerial;
        BooleanExpression result = BooleanTemplate.create("1 = 1");
        try {
            for (FilterRequest filter : filters) {
                COLUMNS column = COLUMNS.valueOf(filter.getProperty().toUpperCase());
                BooleanExpression expression = null;
                switch (column) {
                    case SHOPUSEDID:
                        expression = DbUtil.createLongExpression(invoiceSerial.shopUsedId, filter);
                        break;
                    case CREATEDATETIME:
                        expression = DbUtil.createDateExpression(invoiceSerial.createDatetime, filter);
                        break;
                    case CREATEUSER:
                        expression = DbUtil.createStringExpression(invoiceSerial.createUser, filter);
                        break;
                    case DESCRIPTION:
                        expression = DbUtil.createStringExpression(invoiceSerial.description, filter);
                        break;
                    case INVOICESERIALID:
                        expression = DbUtil.createLongExpression(invoiceSerial.invoiceSerialId, filter);
                        break;
                    case INVOICETYPEID:
                        expression = DbUtil.createLongExpression(invoiceSerial.invoiceTypeId, filter);
                        break;
                    case SERIALNO:
                        expression = DbUtil.createStringExpression(invoiceSerial.serialNo, filter);
                        break;
                    case SHOPID:
                        expression = DbUtil.createLongExpression(invoiceSerial.shopId, filter);
                        break;
                    case STATUS:
                        expression = DbUtil.createStringExpression(invoiceSerial.status, filter);
                        break;
                    case INVOICETRANS:
                        expression = DbUtil.createLongExpression(invoiceSerial.invoiceTrans, filter);
                        break;
                }
                if (expression != null) {
                    result = result.and(expression);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.info("Result Predicate :: " + (result != null ? result.toString() : ""));
        logger.info("Exiting predicates");
        return result;
    }

    @Override
    public List<InvoiceSerialDTO> searchInvoiceSerial(InvoiceSerialDTO invoiceSerialDTO) throws Exception {
        List<InvoiceSerialDTO> result = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   a.invoice_serial_id,");
        sql.append("         a.invoice_type_id,");
        sql.append("         a.serial_no,");
        sql.append("         a.description,");
        sql.append("         a.shop_id,");
        sql.append("         a.create_user,");
        sql.append("         to_char(a.create_datetime, 'dd/MM/yyyy'),");
        sql.append("         c.type_inv || ' - ' || c.invoice_name,");
        sql.append("         b.name, ");
        sql.append("         (SELECT   sv.name");
        sql.append("           FROM   option_set o, option_set_value sv");
        sql.append("          WHERE       o.id = sv.option_set_id");
        sql.append("                  AND o.code = ?");
        sql.append("                  AND o.status = 1");
        sql.append("                  AND sv.status = 1");
        sql.append("                  AND sv.VALUE = c.invoice_type and rownum = 1");
        sql.append("         ) invoiceTypeName");
        sql.append("  FROM   invoice_serial a, shop b, invoice_type c");
        sql.append(" WHERE   a.invoice_type_id = c.invoice_type_id AND b.shop_id = a.shop_used_id");
        sql.append("         AND a.status = 1");
        List<Object> lstParam = Lists.newArrayList();
        lstParam.add(Const.INVOICE_SERIAL.INVOICE_TYPE);
        if (!DataUtil.isNullObject(invoiceSerialDTO.getShopId())) {
            sql.append("         AND a.shop_id = ?");
            lstParam.add(invoiceSerialDTO.getShopId().toString().trim());
        }
        if (!DataUtil.isNullObject(invoiceSerialDTO.getShopUsedId())) {
            sql.append("         AND a.shop_used_id = ?");
            lstParam.add(invoiceSerialDTO.getShopUsedId());
        }
        if (!DataUtil.isNullObject(invoiceSerialDTO.getInvoiceType())) {
            sql.append("         AND c.invoice_type = ?");
            lstParam.add(invoiceSerialDTO.getInvoiceType().toString().trim());
        }
        if (!DataUtil.isNullObject(invoiceSerialDTO.getInvoiceTypeId())) {
            sql.append("         AND a.invoice_type_id = ?");
            lstParam.add(invoiceSerialDTO.getInvoiceTypeId().toString().trim());
        }
        if (!DataUtil.isNullObject(invoiceSerialDTO.getSerialNo())) {
            sql.append("         AND lower(a.serial_no) like ?");
            lstParam.add("%" + invoiceSerialDTO.getSerialNo().trim().toLowerCase() + "%");
        }
        if (!DataUtil.isNullObject(invoiceSerialDTO.getDescription())) {
            sql.append("         AND lower(a.description)  like ?");
            lstParam.add("%" + invoiceSerialDTO.getDescription().trim().toLowerCase() + "%");
        }
        if (!DataUtil.isNullObject(invoiceSerialDTO.getInvoiceTrans())) {
            sql.append("         AND  nvl(a.invoice_trans,1) = ? ");
            lstParam.add(invoiceSerialDTO.getInvoiceTrans());
        }
        sql.append("           AND ROWNUM < 1000");
        sql.append("ORDER BY   a.create_datetime DESC");
        Query query = em.createNativeQuery(sql.toString());
        int i = 0;
        for (Object object : lstParam) {
            query.setParameter(++i, object);
        }
        List<Object[]> lstResult = query.getResultList();
        if (lstResult != null && lstResult.size() > 0) {
            for (Object[] object : lstResult) {
                int index = 0;
                InvoiceSerialDTO invoiceSerialDTO1 = new InvoiceSerialDTO();
                invoiceSerialDTO1.setInvoiceSerialId(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceSerialDTO1.setInvoiceTypeId(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceSerialDTO1.setSerialNo(DataUtil.safeToString(object[index++]));
                invoiceSerialDTO1.setDescription(DataUtil.safeToString(object[index++]));
                invoiceSerialDTO1.setShopId(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceSerialDTO1.setCreateUser(DataUtil.safeToString(object[index++]));
                invoiceSerialDTO1.setCreateDatetime(DateUtil.string2Date(DataUtil.safeToString(object[index++])));
                invoiceSerialDTO1.setInvoiceTypeText(DataUtil.safeToString(object[index++]));
                invoiceSerialDTO1.setShopName(DataUtil.safeToString(object[index++]));
                invoiceSerialDTO1.setInvoiceTypeName(DataUtil.safeToString(object[index]));
                result.add(invoiceSerialDTO1);
            }
        }
        return result;
    }

    public List<InvoiceSerialDTO> getAllSerialByInvoiceType(Long shopId, Long invoiceTypeId) throws Exception {
        List<InvoiceSerialDTO> result = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT INVOICE_SERIAL_ID, SERIAL_NO FROM INVOICE_SERIAL WHERE INVOICE_TYPE_ID = ? AND SHOP_USED_ID = ? AND STATUS = 1 ORDER BY NLSSORT(SERIAL_NO, 'NLS_SORT=VIETNAMESE')");
        List<Object> lstParam = Lists.newArrayList();
        lstParam.add(invoiceTypeId);
        lstParam.add(shopId);
        Query query = em.createNativeQuery(sql.toString());
        int i = 0;
        for (Object object : lstParam) {
            query.setParameter(++i, object);
        }
        List<Object[]> lstResult = query.getResultList();
        if (lstResult != null && lstResult.size() > 0) {
            for (Object[] object : lstResult) {
                int index = 0;
                InvoiceSerialDTO invoiceSerialDTO = new InvoiceSerialDTO();
                invoiceSerialDTO.setInvoiceSerialId(Long.valueOf(DataUtil.safeToString(object[index++])));
                invoiceSerialDTO.setSerialNo(DataUtil.safeToString(object[index]));
                result.add(invoiceSerialDTO);
            }
        }
        return result;
    }
}
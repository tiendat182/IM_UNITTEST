package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.LookupInvoiceUsedDTO;
import com.viettel.bccs.inventory.model.InvoiceUsed;
import com.viettel.bccs.inventory.model.QInvoiceUsed;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pham on 9/26/2016.
 */
public class InvoiceUsedRepoImpl implements InvoiceUsedRepoCustom {
    public static final Logger logger = Logger.getLogger(InvoiceUsedRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager sale;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public LookupInvoiceUsedDTO getInvoiceUsed(String serial, Date fromDate, Date toDate) throws Exception {
        StringBuilder queryString = new StringBuilder();
        String fromDateInstant = convertDateToString(fromDate) + " 00:00:00";
        String toDateInstant = convertDateToString(toDate) + " 23:59:59";
        queryString.append(" SELECT MIN (invoice_no) , MAX (invoice_no)  ");
        queryString.append(" FROM invoice_used ");
        queryString.append(" WHERE serial_no = #serial");
        queryString.append(" AND invoice_datetime >= to_date(#fromDateInstant, 'YYYY-MM-DD HH24:MI:SS')");
        queryString.append(" AND invoice_datetime <= to_date(#toDateInstant, 'YYYY-MM-DD HH24:MI:SS')");
        Query query = sale.createNativeQuery(queryString.toString());
        query.setParameter("serial", serial);
        query.setParameter("fromDateInstant", fromDateInstant);
        query.setParameter("toDateInstant", toDateInstant);
        List queryResult = query.getResultList();//luon return list.size() = 1
        //if (!queryResult.isEmpty()) {
        Object[] objects = (Object[]) queryResult.get(0);
        LookupInvoiceUsedDTO lookupInvoiceUsedDTO = new LookupInvoiceUsedDTO();
        lookupInvoiceUsedDTO.setFromInvoice(DataUtil.safeToString(objects[0]));
        lookupInvoiceUsedDTO.setToInvoice(DataUtil.safeToString(objects[1]));
        if (DataUtil.isNullOrEmpty(lookupInvoiceUsedDTO.getFromInvoice()))
            return null;
        return lookupInvoiceUsedDTO;
    }

    @Override
    public List<LookupInvoiceUsedDTO> getAllSerial() throws Exception {
        StringBuilder query = new StringBuilder();
        List<LookupInvoiceUsedDTO> list = new ArrayList<>();
        //query.append("select a.invoice_name block_name, b.serial_no||'-'|| ");
        //query.append("(select output_value from  sync_mapping ");
        //query.append("where input_value = b.shop_id ");
        //query.append("and key = 'SERIAL_CODE') serial_code ");
        //query.append("from invoice_type a, invoice_serial  b ");
        //query.append("where a.invoice_type_id = b.invoice_type_id order by NLSSORT(b.serial_no, 'NLS_SORT=vietnamese') asc");

        query.append("select DISTINCT serial_code, block_name from ( select a.invoice_name block_name, b.serial_no || ");
        query.append("               (SELECT   nvl2(output_value, '-' || output_value, '') from  sync_mapping ");
        query.append("       where input_value = b.shop_id ");
        query.append("       and key = 'SERIAL_CODE') serial_code ");
        query.append("from invoice_type a, invoice_serial  b ");
        query.append("where a.invoice_type_id = b.invoice_type_id) tmp_result order by NLSSORT(serial_code, 'NLS_SORT=vietnamese') asc ");

        Query sql = em.createNativeQuery(query.toString());
        List queryResult = sql.getResultList();
        for (Object obj : queryResult) {
            Object[] objects = (Object[]) obj;
            LookupInvoiceUsedDTO lookupInvoiceUsedDTO = new LookupInvoiceUsedDTO();
            lookupInvoiceUsedDTO.setSerial(DataUtil.safeToString(objects[0]));
            lookupInvoiceUsedDTO.setName(DataUtil.safeToString(objects[1]));
            list.add(lookupInvoiceUsedDTO);
        }
        return list;
    }

    public String convertDateToString(Date date) throws Exception {
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}


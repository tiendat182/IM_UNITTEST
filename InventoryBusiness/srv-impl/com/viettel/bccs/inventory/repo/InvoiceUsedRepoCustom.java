package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.LookupInvoiceUsedDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by pham on 9/26/2016.
 */
public interface InvoiceUsedRepoCustom {

    public LookupInvoiceUsedDTO getInvoiceUsed(String serial, Date fromDate, Date toDate) throws Exception;

    public List<LookupInvoiceUsedDTO> getAllSerial() throws Exception;
}

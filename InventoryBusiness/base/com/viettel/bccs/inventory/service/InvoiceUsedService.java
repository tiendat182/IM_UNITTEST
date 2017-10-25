package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.LookupInvoiceUsedDTO;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

/**
 * Created by pham on 9/26/2016.
 */
@WebService
public interface InvoiceUsedService {

    @WebMethod
    public LookupInvoiceUsedDTO getInvoiceUsed(String serial, Date fromDate, Date toDate) throws Exception;

    @WebMethod
    public List<LookupInvoiceUsedDTO> getAllSerial() throws Exception;
}

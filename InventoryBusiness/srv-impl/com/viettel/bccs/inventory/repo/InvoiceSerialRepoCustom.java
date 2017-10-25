package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.InvoiceSerialDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface InvoiceSerialRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<InvoiceSerialDTO> searchInvoiceSerial(InvoiceSerialDTO invoiceSerialDTO) throws Exception;

    public List<InvoiceSerialDTO> getAllSerialByInvoiceType(Long shopId, Long invoiceTypeId) throws Exception, LogicException;
}
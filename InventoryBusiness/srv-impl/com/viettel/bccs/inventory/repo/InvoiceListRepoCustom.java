package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.InvoiceListDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import java.util.List;

public interface InvoiceListRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<InvoiceListDTO> getAllInvoiceListByShopId(Long shopId) throws Exception;

    public List<InvoiceListDTO> getAllInvoiceListBySerialCode(String serialCode) throws Exception;

    public List<InvoiceListDTO> getAllInvoiceListByInvoiceListId(Long invoiceListId) throws Exception;

    public BaseMessage updateInvoiceListByInvoiceListId(Long invoiceListId) throws Exception;

    public List<InvoiceListDTO> searchInvoiceList(InvoiceListDTO invoiceListDTO) throws Exception;

    boolean checkStaffHaveInvoiceList(Long staffId) throws Exception;
}
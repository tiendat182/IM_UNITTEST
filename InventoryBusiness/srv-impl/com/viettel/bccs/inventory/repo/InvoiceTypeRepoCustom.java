package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface InvoiceTypeRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);
    public List<InvoiceTypeDTO> getInvoiceType(Long type) throws Exception, LogicException;
}
package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface InvoiceTemplateRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    boolean checkObjectHaveInvoiceTemplate(Long ownerId, Long ownerType) throws Exception;
}
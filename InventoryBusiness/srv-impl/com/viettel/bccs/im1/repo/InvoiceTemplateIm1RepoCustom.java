package com.viettel.bccs.im1.repo;

import com.viettel.bccs.im1.model.InvoiceTemplateIm1;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

public interface InvoiceTemplateIm1RepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public InvoiceTemplateIm1 getInvoiceTemplate( Long ownerId, Long type) throws Exception;

    public List<InvoiceTemplateIm1> getInvoiceTemplateShop(Long shopId) throws Exception;
}
package com.viettel.bccs.im1.repo;

import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

public interface InvoiceTemplateLogIm1RepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

}
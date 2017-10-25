package com.viettel.bccs.sale.repo;

import com.viettel.bccs.sale.dto.PaymentGroupServiceDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.sale.model.PaymentGroupService;

public interface PaymentGroupServiceRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public PaymentGroupServiceDTO getPaymentGroupServiceByName(String name) throws Exception;
}
package com.viettel.bccs.sale.repo;

import com.viettel.bccs.sale.dto.PaymentGroupDTO;
import com.viettel.bccs.sale.dto.PaymentGroupDayTypeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.sale.model.PaymentGroup;

public interface PaymentGroupRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public PaymentGroupDTO getPaymentGroupByName(String name) throws Exception;

    public List<PaymentGroupDTO> getLstPaymentGroup() throws Exception;

    public List<PaymentGroupDayTypeDTO> getLstDayTypeByPaymentGroupId(Long paymentGroupId) throws Exception;
}
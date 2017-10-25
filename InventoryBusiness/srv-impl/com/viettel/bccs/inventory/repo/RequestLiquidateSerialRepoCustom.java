package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.model.RequestLiquidateSerial;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface RequestLiquidateSerialRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<RequestLiquidateSerial> getLstRequestLiquidateSerialDTO(Long requestLiquidateDetailId);

}
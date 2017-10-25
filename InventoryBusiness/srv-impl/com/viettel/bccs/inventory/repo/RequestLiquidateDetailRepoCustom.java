package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.RequestLiquidateDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface RequestLiquidateDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<RequestLiquidateDetailDTO> getLstRequestLiquidateDetailDTO(Long requestLiquidateId) throws Exception;
}
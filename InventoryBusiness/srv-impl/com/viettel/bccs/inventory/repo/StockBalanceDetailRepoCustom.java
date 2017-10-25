package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockBalanceDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockBalanceDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockBalanceDetailDTO> getListStockBalanceDetailDTO(Long requestID) throws Exception;
}
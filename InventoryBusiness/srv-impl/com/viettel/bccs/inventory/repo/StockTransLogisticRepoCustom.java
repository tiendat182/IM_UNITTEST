package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockTransLogisticDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransLogisticRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockTransLogisticDTO> getLstOrderLogistics(Long id, Long maxRetry, Long maxDay, Long numberThread) throws Exception;
}
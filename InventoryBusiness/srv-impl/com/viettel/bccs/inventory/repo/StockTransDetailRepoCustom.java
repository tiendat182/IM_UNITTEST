package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockTransDetailDTO> getDetailByStockTransId(Long stockTransId) throws Exception;

    public List<StockTransDetailDTO> getStockTransDetailByStockTransId(Long stockTransId, Long toOwnerType) throws Exception;
}
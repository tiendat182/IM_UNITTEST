package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockOrderDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockOrderDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockOrderDetailDTO> getListStockOrderDetail(Long stockOrderId) throws Exception;
}
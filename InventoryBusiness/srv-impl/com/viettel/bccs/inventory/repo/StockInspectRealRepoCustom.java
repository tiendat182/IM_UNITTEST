package com.viettel.bccs.inventory.repo;

import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.StockInspectReal;

public interface StockInspectRealRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockInspectReal> getStockInspectReal(Long stockInspectId) throws Exception;

    public int deleteStockInspectReal(Long stockInspectId) throws Exception;
}
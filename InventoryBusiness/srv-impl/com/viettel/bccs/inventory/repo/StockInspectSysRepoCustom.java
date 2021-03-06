package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockInspectSysRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 public int deleteStockInspectSys(Long stockInspectId) throws Exception;
}
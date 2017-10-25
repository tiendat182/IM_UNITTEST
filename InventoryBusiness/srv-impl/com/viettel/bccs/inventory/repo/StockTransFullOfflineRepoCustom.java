package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockTransFullOfflineDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransFullOfflineRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 public List<StockTransFullOfflineDTO> findByActionId(Long actionId);
}
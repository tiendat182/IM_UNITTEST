package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ManageTransStockDto;
import com.viettel.bccs.inventory.model.StockIsdnTrans;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockIsdnTransRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 public List<StockIsdnTrans> searchHistory(ManageTransStockDto searchDTO) throws LogicException, Exception;
}
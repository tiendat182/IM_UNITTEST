package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransIm1RepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters); 

 public int updateStatusStockTrans(Long stockTransID, Long status) throws Exception;
}
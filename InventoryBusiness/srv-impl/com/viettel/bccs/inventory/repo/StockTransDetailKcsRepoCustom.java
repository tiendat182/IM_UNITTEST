package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;
import com.viettel.bccs.inventory.model.StockTransDetailKcs;
public interface StockTransDetailKcsRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters); 

}
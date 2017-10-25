package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;
public interface EquipmentRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters); 

}
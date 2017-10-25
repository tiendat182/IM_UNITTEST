package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface ReqActivateKitRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters); 

}
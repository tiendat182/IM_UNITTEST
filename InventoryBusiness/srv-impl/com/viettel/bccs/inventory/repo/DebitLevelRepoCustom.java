package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.DebitLevelDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;
public interface DebitLevelRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);
 public  List<DebitLevelDTO> searchDebitlevel(DebitLevelDTO debitLevelDTO);
}
package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.NumberActionDTO;
import com.viettel.bccs.inventory.dto.SearchNumberRangeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;
public interface NumberActionRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 public Boolean checkOverlap(Long fromIsdn,Long toIsdn,String telecomServiceId) throws Exception;

 public List<NumberActionDTO> search(SearchNumberRangeDTO searchDTO) throws Exception;

}
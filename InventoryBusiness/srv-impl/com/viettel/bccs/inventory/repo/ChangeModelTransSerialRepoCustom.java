package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ChangeModelTransSerialDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;
public interface ChangeModelTransSerialRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 List<ChangeModelTransSerialDTO> findModelTransSerialByChangeModelTransDetailId(Long changeModelTransDetailId) throws Exception;
}
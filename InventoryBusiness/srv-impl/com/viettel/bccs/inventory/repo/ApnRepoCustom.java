package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ApnDTO;
import com.viettel.bccs.inventory.model.Apn;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface ApnRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 public List<Apn> searchApnAutoComplete(String input) throws Exception;

 List<Apn> searchApnCorrect(ApnDTO apnDTO);

 List<Apn> searchApn(ApnDTO apnDTO);
}
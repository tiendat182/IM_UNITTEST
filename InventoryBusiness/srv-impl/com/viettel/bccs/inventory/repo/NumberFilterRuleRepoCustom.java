package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;
public interface NumberFilterRuleRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 /**
  * ham xu ly tim kiem loc so dep
  * @author HungDV24
  * @param numberFilterRule
  * @param extract
  * @return
  */
 public List<NumberFilterRuleDTO> findRuleAggregate(NumberFilterRuleDTO numberFilterRule, boolean extract) throws Exception;

 /**
  * ham check ton tai dieu kien dich vu
  * @author HungDV24
  * @param numberFilterRuleDTO
  * @return
  * @throws Exception
  */
 public Long checkInsertConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception;
 public Long checkEditConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception;

 // anhnv33 loc so dep
 public List searchHightOrderRule(Long telecomServiceId, Long minOrder) throws Exception;
}
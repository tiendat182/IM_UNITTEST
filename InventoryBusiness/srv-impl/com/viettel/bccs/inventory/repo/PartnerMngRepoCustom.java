package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.PartnerInputSearch;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface PartnerMngRepoCustom {
  public Predicate toPredicate(List<FilterRequest> filters);


  public List<Partner> searchPartner(PartnerInputSearch partnerInputSearch) throws Exception;


  public Long countPartnerByPartnerCode(String partnerCode) throws Exception;

  public Long countPartnerByA4Key(String a4Key) throws Exception;
}
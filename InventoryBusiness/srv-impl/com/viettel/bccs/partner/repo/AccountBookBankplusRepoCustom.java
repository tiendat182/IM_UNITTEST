package com.viettel.bccs.partner.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;

public interface AccountBookBankplusRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters); 

}
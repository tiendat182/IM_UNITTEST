package com.viettel.bccs.partner.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.partner.model.AccountBalance;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface AccountBalanceRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);
 public List<AccountBalance> getAccountBalance(Long ownerType, Long ownerId, Long balanceType) throws Exception;

 public AccountBalance findLock(Long accountBalanceId) throws Exception;
}
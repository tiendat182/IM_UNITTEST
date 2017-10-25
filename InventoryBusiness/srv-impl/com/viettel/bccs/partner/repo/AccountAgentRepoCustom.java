package com.viettel.bccs.partner.repo;

import com.viettel.bccs.im1.model.AccountAgent;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

public interface AccountAgentRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<AccountAgent> getAccountAgentFromOwnerId(Long ownerId, Long ownerType) throws Exception;
}
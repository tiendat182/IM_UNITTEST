package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface MtRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public void deleteMessage(Long id) throws LogicException, Exception;
}
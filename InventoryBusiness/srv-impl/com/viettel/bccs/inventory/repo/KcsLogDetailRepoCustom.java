package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
public interface KcsLogDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);
}

package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ThreadCloseStockDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface ThreadCloseStockRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public ThreadCloseStockDTO getThreadCloseStockByName(String threadName) throws Exception;

    public ThreadCloseStockDTO getThreadAvailable(String threadName) throws Exception;
}
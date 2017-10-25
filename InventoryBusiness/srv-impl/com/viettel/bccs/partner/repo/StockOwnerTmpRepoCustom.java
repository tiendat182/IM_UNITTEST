package com.viettel.bccs.partner.repo;

import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.partner.model.StockOwnerTmp;

public interface StockOwnerTmpRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockOwnerTmp> getStockOwnerTmpByOwnerId(Long ownerId, Long ownerType) throws Exception;
}
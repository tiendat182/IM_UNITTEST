package com.viettel.bccs.inventory.repo;

import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.StockIsdnVtShopLock;

public interface StockIsdnVtShopLockRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public void deleteShopLock(String isdn) throws Exception;

}
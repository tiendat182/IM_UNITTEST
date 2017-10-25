package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.StockIsdnVtShop;

public interface StockIsdnVtShopRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockIsdnVtShop> findRequestList(List<Long> requestList);

}
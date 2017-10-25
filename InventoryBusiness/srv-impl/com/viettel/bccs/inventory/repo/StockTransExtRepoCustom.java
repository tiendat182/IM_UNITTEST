package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockTransExtDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.StockTransExt;

public interface StockTransExtRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public boolean checkDuplicateTrans(String key, String value);

    public StockTransExtDTO getStockTransId(String extKey, String extValue) throws Exception;

}
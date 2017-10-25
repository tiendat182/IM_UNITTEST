package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockTransDetailRescueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransDetailRescueRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);


    public List<StockTransDetailRescueDTO> viewDetail(Long  stockTransId) throws Exception;

    public List<StockTransDetailRescueDTO> getCountLstDetail(Long stockTransId) throws LogicException, Exception;
}
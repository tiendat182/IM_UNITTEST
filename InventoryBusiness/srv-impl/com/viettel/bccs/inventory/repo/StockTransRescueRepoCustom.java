package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockTransRescueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransRescueRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockTransRescueDTO> searchStockRescue(StockTransRescueDTO stockTransRescueDTO) throws LogicException, Exception;

    public Long getMaxId() throws LogicException, Exception;

    public Long getReasonId() throws LogicException, Exception;

    public boolean isDulicateRequestCode(String requestCode) throws Exception;
}
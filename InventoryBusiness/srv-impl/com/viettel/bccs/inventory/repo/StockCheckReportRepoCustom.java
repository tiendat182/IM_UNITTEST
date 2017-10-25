package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockCheckReportDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface StockCheckReportRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockCheckReportDTO> onSearch(Long shopId, Date checkDate) throws Exception;

    public boolean isDuplicate(Long shopId, Date checkDate) throws Exception;
}
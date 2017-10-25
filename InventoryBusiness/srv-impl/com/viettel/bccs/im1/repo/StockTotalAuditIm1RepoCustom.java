package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.im1.dto.StockTotalAuditIm1DTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTotalAuditIm1RepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public int createStockTotalAudit(StockTotalAuditIm1DTO dto) throws LogicException, Exception;
}
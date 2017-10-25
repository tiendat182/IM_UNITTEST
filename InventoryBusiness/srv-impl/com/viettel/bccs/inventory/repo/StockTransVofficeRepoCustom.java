package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransVofficeRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public StockTransVoffice findStockTransVofficeByRequestId(String requestId);

    public StockTransVoffice findStockTransVofficeByActionId(Long actionId);

    public List<StockTransVoffice> searchStockTransVoffice(StockTransVofficeDTO stockTransVofficeDTO) throws Exception;

    public List<StockTransVoffice> getLstVofficeSigned(Long maxDay, String prefixTemplate) throws Exception;

    public StockTransVoffice findOneById(String stockTransVofficeId) throws Exception;
}
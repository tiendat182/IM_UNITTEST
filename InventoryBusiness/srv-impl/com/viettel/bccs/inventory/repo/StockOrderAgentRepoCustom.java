package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StockOrderAgentDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.bccs.inventory.dto.StockOrderAgentForm;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.StockOrderAgent;

public interface StockOrderAgentRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockOrderAgent> getStockOrderAgent(StockOrderAgentDTO dto) throws Exception, LogicException;

    public List<StockOrderAgent> getStockOrderAgentIM1(StockOrderAgentDTO dto) throws Exception, LogicException;

    public List<StockOrderAgent> search(StockOrderAgentDTO dto) throws Exception, LogicException;

    public Long getMaxStockOrderAgentId() throws Exception, LogicException;

    public List<StockOrderAgentForm> getStockOrderAgentForm(StockOrderAgentDTO searchForm, StaffDTO currentStaff);

    public List checkShopAgent(Long shopId);
}
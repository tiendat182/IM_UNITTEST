package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockOrderDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.StockOrder;

public interface StockOrderRepoCustom {
    public List<StockOrderDTO> getListStockOrder(Long shopId) throws Exception;

    public StockOrderDTO getStockOrderByCode(String stockOrderCode) throws Exception;

    public List<StockOrderDTO> getStockOrderList(Long staffId, Date createDate) throws Exception;

    public StockOrderDTO getStockOrderStaff(Long staffId) throws Exception;
}
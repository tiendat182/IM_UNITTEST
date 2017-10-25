package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransOfflineDTO;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockTransOfflineService {


    public StockTransOfflineDTO findOne(Long id) throws Exception;


    public Long count(List<FilterRequest> filters) throws Exception;


    public List<StockTransOfflineDTO> findAll() throws Exception;

    //
    public List<StockTransOfflineDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    //
    public StockTransOfflineDTO save(StockTransOfflineDTO stockTransOfflineDTO) throws Exception;

    public void processExpStockOffine(StockTrans stockTrans) throws Exception, LogicException;

    public StockTransOfflineDTO getStockTransOfflineById(Long stockTransId) throws Exception;
}
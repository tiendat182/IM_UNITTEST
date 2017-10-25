package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransLogisticDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransLogisticService {

    @WebMethod
    public StockTransLogisticDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransLogisticDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransLogisticDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransLogisticDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockTransLogisticDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public StockTransLogisticDTO save(StockTransLogisticDTO stockTransLogisticDTO) throws Exception;

    @WebMethod
    public StockTransLogisticDTO findByStockTransId(Long stockTransId) throws Exception;

    public List<StockTransLogisticDTO> getLstOrderLogistics(Long id, Long maxRetry, Long maxDay, Long numberThread) throws Exception;

    public void updateStockTransLogistics(Long stockTransLogistic, Long status, BaseMessage baseMessage) throws Exception;

    public void updateStockTransLogisticRetry(Long maxRetry, Long stockTransLogistic, BaseMessage baseMessage) throws Exception;

}
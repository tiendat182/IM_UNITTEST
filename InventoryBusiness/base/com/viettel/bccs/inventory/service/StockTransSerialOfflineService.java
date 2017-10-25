package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransSerialOfflineDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransSerialOfflineService {

    @WebMethod
    public StockTransSerialOfflineDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransSerialOfflineDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransSerialOfflineDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransSerialOfflineDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockTransSerialOfflineDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public StockTransSerialOfflineDTO save (StockTransSerialOfflineDTO stockTransSerialOfflineDTO) throws Exception;
}
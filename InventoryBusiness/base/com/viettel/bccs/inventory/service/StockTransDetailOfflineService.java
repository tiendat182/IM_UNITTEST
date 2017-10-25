package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDetailOfflineDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransDetailOfflineService {

    @WebMethod
    public StockTransDetailOfflineDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransDetailOfflineDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransDetailOfflineDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransDetailOfflineDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockTransDetailOfflineDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public StockTransDetailOfflineDTO save (StockTransDetailOfflineDTO stockTransDetailOfflineDTO) throws Exception;
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransFullOfflineDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransFullOfflineService {

    @WebMethod
    public StockTransFullOfflineDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransFullOfflineDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransFullOfflineDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransFullOfflineDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockTransFullOfflineDTO productSpecCharacterDTO) throws Exception;

}
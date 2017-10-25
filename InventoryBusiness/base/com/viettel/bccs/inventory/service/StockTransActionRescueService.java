package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransActionRescueDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransActionRescueService {

    @WebMethod
    public StockTransActionRescueDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransActionRescueDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransActionRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransActionRescueDTO stockTransActionRescueDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockTransActionRescueDTO stockTransActionRescueDTO) throws Exception;

    @WebMethod
    public StockTransActionRescueDTO save(StockTransActionRescueDTO stockTransActionRescueDTO) throws Exception;

}
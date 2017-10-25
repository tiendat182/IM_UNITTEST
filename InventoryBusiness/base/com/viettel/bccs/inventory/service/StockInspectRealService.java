package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockInspectRealDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockInspectRealService {

    @WebMethod
    public StockInspectRealDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockInspectRealDTO> findAll() throws Exception;

    @WebMethod
    public List<StockInspectRealDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockInspectRealDTO save(StockInspectRealDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockInspectRealDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage delete(StockInspectRealDTO stockInspectRealDTO) throws LogicException, Exception;

    @WebMethod
    public List<StockInspectRealDTO> getStockInspectReal(Long stockInspectId) throws LogicException, Exception;

    @WebMethod
    public BaseMessage deleteStockInspect(Long stockInspectId) throws LogicException, Exception;
}
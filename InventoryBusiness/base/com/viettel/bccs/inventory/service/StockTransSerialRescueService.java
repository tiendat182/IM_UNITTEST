package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransSerialRescueService {

    @WebMethod
    public StockTransSerialRescueDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransSerialRescueDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransSerialRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransSerialRescueDTO stockTransSerialRescueDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockTransSerialRescueDTO stockTransSerialRescueDTO) throws Exception;

    @WebMethod
    public StockTransSerialRescueDTO save(StockTransSerialRescueDTO stockTransSerialRescueDTO) throws Exception;

    @WebMethod
    public List<StockTransSerialRescueDTO> viewDetailSerail(Long stockTransId, Long prodOfferId, Long prodOfferIdReturn) throws LogicException, Exception;

    @WebMethod
    public List<StockTransSerialRescueDTO> viewDetailSerailByStockTransId(Long stockTransId) throws LogicException, Exception;

    @WebMethod
    public List<StockTransSerialRescueDTO> getListDetailSerial(Long stockTransId) throws LogicException, Exception;


}
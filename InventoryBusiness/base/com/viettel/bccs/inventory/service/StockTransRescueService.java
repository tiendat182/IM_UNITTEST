package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransRescueDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.bccs.inventory.model.StockTransSerialRescue;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransRescueService {

    @WebMethod
    public StockTransRescueDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransRescueDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransRescueDTO stockTransRescueDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockTransRescueDTO stockTransRescueDTO) throws Exception;

    @WebMethod
    public List<StockTransRescueDTO> searchStockRescue(StockTransRescueDTO stockTransRescueDTO) throws LogicException, Exception;

    @WebMethod
    public Long getMaxId() throws LogicException, Exception;

    @WebMethod
    public Long getReasonId() throws LogicException, Exception;

    @WebMethod
    public BaseMessage createRequest(StockTransRescueDTO stockTransRescueAdd, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public BaseMessage deleteStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public BaseMessage receiveStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public BaseMessage acceptStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public BaseMessage cancelStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public BaseMessage returnStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap, List<StockTransSerialRescueDTO> lstSerial) throws LogicException, Exception;


}
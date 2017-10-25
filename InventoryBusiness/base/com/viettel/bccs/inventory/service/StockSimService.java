package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.SimKITDTO;
import com.viettel.bccs.inventory.dto.StockSimDTO;

import java.util.List;

import com.viettel.bccs.inventory.dto.StockSimMessage;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface StockSimService {

    @WebMethod
    public StockSimDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockSimDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public SimKITDTO findStockSim(@WebParam(name = "imsi") String imsi, @WebParam(name = "serial") String serial) throws Exception;

    @WebMethod
    public SimKITDTO findStockKit(@WebParam(name = "serial") String serial) throws Exception;

    @WebMethod
    public StockSimDTO getSimInfor(String msisdn) throws LogicException, Exception;


    @WebMethod
    public boolean isCaSim(String serial);

    @WebMethod
    boolean checkSimElegibleExists(String fromSerial, String toSerial) throws Exception, LogicException;
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.DivideSerialActionDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface DivideSerialActionService {

    @WebMethod
    public DivideSerialActionDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<DivideSerialActionDTO> findAll() throws Exception;

    @WebMethod
    public List<DivideSerialActionDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public DivideSerialActionDTO save(DivideSerialActionDTO divideSerialActionDTO) throws Exception;

    @WebMethod
    public BaseMessage update(DivideSerialActionDTO divideSerialActionDTO) throws Exception;

    @WebMethod
    public List<StockTransFullDTO> divideSerial(DivideSerialActionDTO divideSerialActionDTO) throws LogicException, Exception;

    @WebMethod
    public List<StockTransFullDTO> searchStockTrans(DivideSerialActionDTO divideSerialActionDTO) throws LogicException, Exception;

    @WebMethod
    public List<StockTransFullDTO> getListSerial(DivideSerialActionDTO divideSerialActionDTO) throws LogicException, Exception;
}
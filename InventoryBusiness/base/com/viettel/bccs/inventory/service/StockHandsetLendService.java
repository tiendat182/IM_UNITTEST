package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockHandsetLendDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockHandsetLendService {

    @WebMethod
    public StockHandsetLendDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockHandsetLendDTO> findAll() throws Exception;

    @WebMethod
    public List<StockHandsetLendDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockHandsetLendDTO productSpecCharacterDTO) throws Exception, LogicException;
    @WebMethod
    public BaseMessage update(StockHandsetLendDTO productSpecCharacterDTO) throws Exception, LogicException;

}
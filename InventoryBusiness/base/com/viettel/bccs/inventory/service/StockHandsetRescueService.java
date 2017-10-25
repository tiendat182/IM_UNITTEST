package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockHandsetRescueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockHandsetRescueService {

    @WebMethod
    public StockHandsetRescueDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockHandsetRescueDTO> findAll() throws Exception;

    @WebMethod
    public List<StockHandsetRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockHandsetRescueDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockHandsetRescueDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<StockHandsetRescueDTO> getListHansetRescue(Long ownerId) throws LogicException, Exception;

    @WebMethod
    public List<StockHandsetRescueDTO> getListProductForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception;

    @WebMethod
    public List<StockHandsetRescueDTO> getListSerialForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception;

    @WebMethod
    public int updateStatusSerialForRescue(Long statusAffer,Long statusBefor, Long ownerId, String serial, Long prodOfferId) throws LogicException, Exception;

}
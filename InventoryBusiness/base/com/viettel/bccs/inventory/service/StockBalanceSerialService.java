package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockBalanceSerialDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockBalanceSerialService {

    @WebMethod
    public StockBalanceSerialDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockBalanceSerialDTO> findAll() throws Exception;

    @WebMethod
    public List<StockBalanceSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockBalanceSerialDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public StockBalanceSerialDTO save(StockBalanceSerialDTO dto) throws Exception;

    @WebMethod
    public List<StockBalanceSerialDTO> getListStockBalanceSerialDTO(Long stockBalanceDetailID) throws Exception;

}
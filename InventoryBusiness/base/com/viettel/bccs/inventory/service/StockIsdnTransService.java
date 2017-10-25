package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ManageTransStockDto;
import com.viettel.bccs.inventory.dto.StockIsdnTransDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface StockIsdnTransService {

    @WebMethod
    public StockIsdnTransDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockIsdnTransDTO> findAll() throws Exception;

    @WebMethod
    public List<StockIsdnTransDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockIsdnTransDTO create(StockIsdnTransDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockIsdnTransDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<StockIsdnTransDTO> search(ManageTransStockDto searchDTO) throws Exception;

    @WebMethod
    public List<StockIsdnTransDTO> searchHistory(ManageTransStockDto searchDTO) throws LogicException,Exception;
}
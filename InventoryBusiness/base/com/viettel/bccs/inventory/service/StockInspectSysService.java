package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockInspectSysDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockInspectSysService {

    @WebMethod
    public StockInspectSysDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockInspectSysDTO> findAll() throws Exception;

    @WebMethod
    public List<StockInspectSysDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockInspectSysDTO save(StockInspectSysDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockInspectSysDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage delete(Long stockInspectId) throws Exception;

    @WebMethod
    public StockInspectSysDTO findByStockInspectId(Long stockInspectId) throws Exception;

    public BaseMessage deleteStockInspectSys(Long stockInspectId) throws Exception;
}
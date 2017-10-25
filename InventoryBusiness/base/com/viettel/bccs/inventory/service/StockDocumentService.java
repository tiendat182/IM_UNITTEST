package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockDocumentDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockDocumentService {

    @WebMethod
    public StockDocumentDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockDocumentDTO> findAll() throws Exception;

    @WebMethod
    public List<StockDocumentDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockDocumentDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockDocumentDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public List<StockDocumentDTO> getListStockDocumentDTOByActionId(Long actionId) throws Exception;


}
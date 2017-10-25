package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockIsdnTransDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface StockIsdnTransDetailService {

    @WebMethod
    public StockIsdnTransDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockIsdnTransDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<StockIsdnTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockIsdnTransDetailDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockIsdnTransDetailDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<StockIsdnTransDetailDTO> searchByStockIsdnTransId(Long stockIsdnTransId) throws Exception;
}
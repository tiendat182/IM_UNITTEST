package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockBalanceDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockBalanceDetailService {

    @WebMethod
    public StockBalanceDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockBalanceDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<StockBalanceDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockBalanceDetailDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public StockBalanceDetailDTO save(StockBalanceDetailDTO dto) throws Exception;

    @WebMethod
    public List<StockBalanceDetailDTO> getListStockBalanceDetail(Long requestID) throws Exception;

}
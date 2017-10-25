package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDetailDTO;

import java.util.List;

import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface StockTransDetailService {

    @WebMethod
    public StockTransDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockTransDetailDTO save(StockTransDetailDTO stockTransDetailDTO) throws Exception;

    @WebMethod
    public List<StockTransDetailDTO> findByStockTransId(Long stockTransId) throws Exception;

    @WebMethod
    public StockTransDetailDTO getSingleDetail(Long stockTransId, Long prodOfferId, Long stateId) throws Exception;

}
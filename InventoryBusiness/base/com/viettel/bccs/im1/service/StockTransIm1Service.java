package com.viettel.bccs.im1.service;


import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransIm1Service {

    @WebMethod
    public StockTransIm1DTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransIm1DTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public int updateStatusStockTrans(StockTransIm1DTO dto) throws Exception;

    @WebMethod
    public StockTransIm1DTO update(StockTransIm1DTO stockTransIm1DTO) throws Exception;

    @WebMethod
    public StockTransIm1DTO save(StockTransIm1DTO stockTransIm1DTO) throws Exception;

    @WebMethod
    public StockTransIm1DTO findOneLock(Long id) throws Exception;

    @WebMethod
    public StockTransIm1DTO findFromStockTransIdLock(Long id) throws Exception;

}
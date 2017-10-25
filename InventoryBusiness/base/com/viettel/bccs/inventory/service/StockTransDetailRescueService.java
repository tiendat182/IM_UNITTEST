package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDetailRescueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransDetailRescueService {

    @WebMethod
    public StockTransDetailRescueDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransDetailRescueDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransDetailRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransDetailRescueDTO stockTransDetailRescueDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockTransDetailRescueDTO stockTransDetailRescueDTO) throws Exception;

    @WebMethod
    public StockTransDetailRescueDTO save(StockTransDetailRescueDTO stockTransDetailRescueDTO) throws Exception;

    @WebMethod
    public List<StockTransDetailRescueDTO> viewDetail(Long stockTransId) throws LogicException, Exception;

    @WebMethod
    public List<StockTransDetailRescueDTO> getCountLstDetail(Long stockTransId) throws LogicException, Exception;

}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockRequestOrderTransDTO;
import com.viettel.bccs.inventory.model.StockRequestOrderTrans;

import java.util.List;

import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface StockRequestOrderTransService {

    @WebMethod
    public StockRequestOrderTransDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockRequestOrderTransDTO> findAll() throws Exception;

    @WebMethod
    public List<StockRequestOrderTransDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockRequestOrderTransDTO save(StockRequestOrderTransDTO stockRequestOrderTransDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockRequestOrderTransDTO stockRequestOrderTransDTO) throws Exception;

    public StockRequestOrderTransDTO getOrderTrans(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId, Long type) throws Exception;

    public StockRequestOrderTransDTO getOrderTransByStockTransId(Long stockTransId) throws Exception;

}
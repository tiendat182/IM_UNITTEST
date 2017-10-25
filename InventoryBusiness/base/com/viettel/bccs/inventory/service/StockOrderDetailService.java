package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockOrderDetailDTO;
import com.viettel.bccs.inventory.model.StockOrderDetail;

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
public interface StockOrderDetailService {

    @WebMethod
    public StockOrderDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockOrderDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<StockOrderDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockOrderDetailDTO save(StockOrderDetailDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockOrderDetailDTO productSpecCharacterDTO) throws Exception;

}
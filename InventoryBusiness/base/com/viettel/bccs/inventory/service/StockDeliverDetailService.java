package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.bccs.inventory.model.StockDeliverDetail;

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
public interface StockDeliverDetailService {

    @WebMethod
    public StockDeliverDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockDeliverDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<StockDeliverDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockDeliverDetailDTO save(StockDeliverDetailDTO stockDeliverDetailDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockDeliverDetailDTO stockDeliverDetailDTO) throws Exception;

    @WebMethod
    public List<StockDeliverDetailDTO> viewStockTotalFull(Long ownerId, Long ownerType) throws Exception;

    @WebMethod
    public List<StockDeliverDetailDTO> getLstDetailByStockDeliverId(Long stockDeliverId) throws Exception;

}
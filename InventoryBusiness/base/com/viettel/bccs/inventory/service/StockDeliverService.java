package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockDeliverDTO;
import com.viettel.bccs.inventory.model.StockDeliver;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.StockDeliverDetail;
import com.viettel.fw.Exception.LogicException;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface StockDeliverService {

    @WebMethod
    public StockDeliverDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockDeliverDTO> findAll() throws Exception;

    @WebMethod
    public List<StockDeliverDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockDeliverDTO save(StockDeliverDTO stockDeliverDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockDeliverDTO stockDeliverDTO) throws Exception;

    @WebMethod
    public void deliverStock(StockDeliverDTO stockDeliverDTO) throws LogicException, Exception;

    @WebMethod
    public StockDeliverDTO getStockDeliverByOwnerIdAndStatus(Long ownerId, Long status) throws LogicException, Exception;

    @WebMethod
    public List<Long> getAllStockForDelete() throws LogicException, Exception;

    @WebMethod
    public List<StockDeliverDTO> searchHistoryStockDeliver(String code, Date fromDate, Date toDate, String status, Long ownerId, Long ownerType) throws Exception;

    @WebMethod
    public Long getMaxId() throws LogicException, Exception;

}
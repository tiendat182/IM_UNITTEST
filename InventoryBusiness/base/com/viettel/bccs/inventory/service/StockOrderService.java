package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockOrderDTO;
import com.viettel.bccs.inventory.model.StockOrder;

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
public interface StockOrderService {

    @WebMethod
    public StockOrderDTO findOne(Long id) throws Exception;

    @WebMethod
    public List<StockOrderDTO> findAll() throws Exception;

    @WebMethod
    public StockOrderDTO saveStockOrder(StockOrderDTO stockOrderDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockOrderDTO productSpecCharacterDTO) throws Exception;

}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockDepositDTO;
import com.viettel.bccs.inventory.model.StockDeposit;

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
public interface StockDepositService {

    @WebMethod
    public StockDepositDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockDepositDTO> findAll() throws Exception;

    @WebMethod
    public List<StockDepositDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockDepositDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockDepositDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public StockDepositDTO getStockDeposit(Long prodOfferId, Long channelTypeId, Long transType) throws Exception;

}
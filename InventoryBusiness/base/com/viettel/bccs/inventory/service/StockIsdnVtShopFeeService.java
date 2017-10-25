package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopFeeDTO;
import com.viettel.bccs.inventory.model.StockIsdnVtShopFee;

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
public interface StockIsdnVtShopFeeService {

    @WebMethod
    public StockIsdnVtShopFeeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockIsdnVtShopFeeDTO> findAll() throws Exception;

    @WebMethod
    public List<StockIsdnVtShopFeeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockIsdnVtShopFeeDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockIsdnVtShopFeeDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public StockIsdnVtShopFeeDTO save(StockIsdnVtShopFeeDTO dto) throws Exception;

    @WebMethod
    public void deleteFee(String isdn) throws Exception ;

}
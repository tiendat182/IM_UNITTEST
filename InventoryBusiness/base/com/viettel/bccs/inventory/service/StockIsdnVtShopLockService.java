package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopLockDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockIsdnVtShopLockService {

    @WebMethod
    public StockIsdnVtShopLockDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockIsdnVtShopLockDTO> findAll() throws Exception;

    @WebMethod
    public List<StockIsdnVtShopLockDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockIsdnVtShopLockDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockIsdnVtShopLockDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public StockIsdnVtShopLockDTO save(StockIsdnVtShopLockDTO dto) throws Exception;

    @WebMethod
    public void deleteShopLock(String isdn) throws Exception ;

}
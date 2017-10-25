package com.viettel.bccs.partner.service;

import com.viettel.bccs.partner.dto.StockOwnerTmpDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockOwnerTmpService {

    @WebMethod
    public StockOwnerTmpDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockOwnerTmpDTO> findAll() throws Exception;

    @WebMethod
    public List<StockOwnerTmpDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockOwnerTmpDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockOwnerTmpDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<StockOwnerTmpDTO> getStockOwnerTmpByOwnerId(Long ownerId, Long ownerType) throws Exception, LogicException;
}
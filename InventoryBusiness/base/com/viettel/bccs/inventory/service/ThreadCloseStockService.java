package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ThreadCloseStockDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ThreadCloseStockService {

    @WebMethod
    public ThreadCloseStockDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ThreadCloseStockDTO> findAll() throws Exception;

    @WebMethod
    public List<ThreadCloseStockDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ThreadCloseStockDTO dto) throws Exception;

    @WebMethod
    public BaseMessage update(ThreadCloseStockDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public boolean isExecute(ThreadCloseStockDTO dto) throws Exception;
}
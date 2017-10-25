package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ExchangeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ExchangeService {

    @WebMethod
    public ExchangeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ExchangeDTO> findAll() throws Exception;

    @WebMethod
    public List<ExchangeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ExchangeDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(ExchangeDTO productSpecCharacterDTO) throws Exception;

}
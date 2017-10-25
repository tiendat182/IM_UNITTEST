package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.PstnIsdnExchangeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface PstnIsdnExchangeService {

    @WebMethod
    public PstnIsdnExchangeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PstnIsdnExchangeDTO> findAll() throws Exception;

    @WebMethod
    public List<PstnIsdnExchangeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(PstnIsdnExchangeDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(PstnIsdnExchangeDTO productSpecCharacterDTO) throws Exception;

}
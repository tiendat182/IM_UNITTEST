package com.viettel.bccs.partner.service;

import com.viettel.bccs.partner.dto.AccountAgentDTO;

import java.util.List;

import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.viettel.fw.dto.BaseMessage;

@WebService
public interface AccountAgentService {

    @WebMethod
    public AccountAgentDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<AccountAgentDTO> findAll() throws Exception;

    @WebMethod
    public List<AccountAgentDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(AccountAgentDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(AccountAgentDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<AccountAgentDTO> getAccountAgentFromOwnerId(Long ownerId, Long ownerType) throws LogicException, Exception;

}
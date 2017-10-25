package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.DomainDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface DomainService {

    @WebMethod
    public DomainDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<DomainDTO> findAll() throws Exception;

    @WebMethod
    public List<DomainDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(DomainDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(DomainDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public DomainDTO findByDomain(String domain) throws Exception, LogicException;

    @WebMethod
    public List<DomainDTO> findAllAction() throws Exception, LogicException;

    @WebMethod
    public DomainDTO findByDomainId(Long domainId) throws Exception, LogicException;


}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ReasonGroupDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ReasonGroupService {

    @WebMethod
    public ReasonGroupDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ReasonGroupDTO> findAll() throws Exception;

    @WebMethod
    public List<ReasonGroupDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ReasonGroupDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(ReasonGroupDTO productSpecCharacterDTO) throws Exception;

}
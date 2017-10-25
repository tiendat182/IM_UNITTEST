package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ApLockTransDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ApLockTransDetailService {

    @WebMethod
    public ApLockTransDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ApLockTransDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<ApLockTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ApLockTransDetailDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(ApLockTransDetailDTO productSpecCharacterDTO) throws Exception;

}
package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.VcRequestDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface VcRequestService {

    @WebMethod
    public VcRequestDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<VcRequestDTO> findAll() throws Exception;

    @WebMethod
    public List<VcRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public VcRequestDTO create(VcRequestDTO vcRequestDTO) throws Exception;

    @WebMethod
    public BaseMessage update(VcRequestDTO productSpecCharacterDTO) throws Exception;

}
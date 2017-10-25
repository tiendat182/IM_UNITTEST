package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.ReqActivateKitDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ReqActivateKitService {

    @WebMethod
    public ReqActivateKitDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ReqActivateKitDTO> findAll() throws Exception;

    @WebMethod
    public List<ReqActivateKitDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public ReqActivateKitDTO create(ReqActivateKitDTO dto) throws Exception;

    @WebMethod
    public BaseMessage update(ReqActivateKitDTO productSpecCharacterDTO) throws Exception;

}
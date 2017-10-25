package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ApTransInfoDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ApTransInfoService {

    @WebMethod
    public ApTransInfoDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ApTransInfoDTO> findAll() throws Exception;

    @WebMethod
    public List<ApTransInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ApTransInfoDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(ApTransInfoDTO productSpecCharacterDTO) throws Exception;

}
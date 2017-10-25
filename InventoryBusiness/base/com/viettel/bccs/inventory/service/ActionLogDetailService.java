package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ActionLogDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ActionLogDetailService {

    @WebMethod
    public ActionLogDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ActionLogDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<ActionLogDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ActionLogDetailDTO actionLogDetailDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ActionLogDetailDTO actionLogDetailDTO) throws Exception;

    @WebMethod
    public ActionLogDetailDTO save(ActionLogDetailDTO actionLogDetailDTO) throws Exception;

}
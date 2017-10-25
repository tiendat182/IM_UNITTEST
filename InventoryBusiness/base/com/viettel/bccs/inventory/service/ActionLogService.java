package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ActionLogDTO;
import com.viettel.bccs.inventory.dto.ActionLogDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ActionLogService {

    @WebMethod
    public ActionLogDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ActionLogDTO> findAll() throws Exception;

    @WebMethod
    public List<ActionLogDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ActionLogDTO actionLogDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ActionLogDTO actionLogDTO) throws Exception;

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ActionLogDTO saveForProcessStock(ActionLogDTO actionLogDTO, List<ActionLogDetailDTO> lstDetail) throws Exception;

}
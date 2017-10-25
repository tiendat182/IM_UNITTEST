package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.RequestLiquidateDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface RequestLiquidateService {

    @WebMethod
    public RequestLiquidateDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<RequestLiquidateDTO> findAll() throws Exception;

    @WebMethod
    public List<RequestLiquidateDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public RequestLiquidateDTO save(RequestLiquidateDTO dto) throws Exception;

    @WebMethod
    public BaseMessage doSaveLiquidate(RequestLiquidateDTO dto) throws LogicException,Exception;

    @WebMethod
    public List<RequestLiquidateDTO> getLstRequestLiquidateDTO(RequestLiquidateDTO requestLiquidateDTO) throws LogicException,Exception;

    @WebMethod
    public byte[] getAttachFileContent(Long requestID) throws LogicException, Exception;

    @WebMethod
    public String getRequestCode() throws LogicException,Exception;

    @WebMethod
    public void updateRequest(RequestLiquidateDTO requestLiquidateDTO) throws LogicException,Exception;
}
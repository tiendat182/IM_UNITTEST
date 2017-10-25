package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.RequestLiquidateSerialDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface RequestLiquidateSerialService {

    @WebMethod
    public RequestLiquidateSerialDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<RequestLiquidateSerialDTO> findAll() throws Exception;

    @WebMethod
    public List<RequestLiquidateSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public RequestLiquidateSerialDTO save(RequestLiquidateSerialDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<RequestLiquidateSerialDTO> getLstRequestLiquidateSerialDTO(Long requestLiquidateDetailId) throws Exception;
}
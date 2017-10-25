package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.RequestLiquidateDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface RequestLiquidateDetailService {

    @WebMethod
    public RequestLiquidateDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<RequestLiquidateDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<RequestLiquidateDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public RequestLiquidateDetailDTO save(RequestLiquidateDetailDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<RequestLiquidateDetailDTO> getLstRequestLiquidateDetailDTO(Long requestLiquidateId) throws Exception;

}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.RevokeTransDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface RevokeTransDetailService {

    @WebMethod
    public RevokeTransDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<RevokeTransDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<RevokeTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(RevokeTransDetailDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(RevokeTransDetailDTO productSpecCharacterDTO) throws Exception;

    public RevokeTransDetailDTO save(RevokeTransDetailDTO revokeTransDetailDTO) throws Exception;

}
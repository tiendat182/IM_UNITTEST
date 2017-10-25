package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.MtDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface MtService {

    @WebMethod
    public MtDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<MtDTO> findAll() throws Exception;

    @WebMethod
    public List<MtDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(MtDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(MtDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage saveSms(String isdn, String content) throws Exception;

    public void delete(Long mtID) throws LogicException, Exception;

}
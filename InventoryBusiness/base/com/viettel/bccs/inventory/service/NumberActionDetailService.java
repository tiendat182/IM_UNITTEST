package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.NumberActionDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface NumberActionDetailService {

    @WebMethod
    public NumberActionDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<NumberActionDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<NumberActionDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(NumberActionDetailDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(NumberActionDetailDTO productSpecCharacterDTO) throws Exception;

}
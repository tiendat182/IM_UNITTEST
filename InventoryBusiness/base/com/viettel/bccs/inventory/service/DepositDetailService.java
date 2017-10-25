package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.DepositDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface DepositDetailService {

    @WebMethod
    public DepositDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<DepositDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<DepositDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public DepositDetailDTO create(DepositDetailDTO depositDetailDTO) throws Exception;
    @WebMethod
    public BaseMessage update(DepositDetailDTO productSpecCharacterDTO) throws Exception;

}
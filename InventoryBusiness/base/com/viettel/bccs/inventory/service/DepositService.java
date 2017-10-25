package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.DepositDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface DepositService {

    @WebMethod
    public DepositDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<DepositDTO> findAll() throws Exception;

    @WebMethod
    public List<DepositDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public DepositDTO create(DepositDTO depositDTO) throws Exception;

    @WebMethod
    public DepositDTO update(Long depositId, String status) throws Exception;

}
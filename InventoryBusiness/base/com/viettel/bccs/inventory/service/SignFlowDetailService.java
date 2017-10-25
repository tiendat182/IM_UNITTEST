package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface SignFlowDetailService {

    @WebMethod
    public SignFlowDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<SignFlowDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<SignFlowDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public SignFlowDetailDTO create(SignFlowDetailDTO dto) throws Exception,LogicException;
    @WebMethod
    public SignFlowDetailDTO update(SignFlowDetailDTO dto) throws Exception;

    /**
     * ham tim kiem
     * @author Thetm1
     * @param signFlowId
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<SignFlowDetailDTO> findBySignFlowId(Long signFlowId) throws Exception;

    /**
     * ham tim kiem
     * @author Tuydv1
     * @param vofficeRoleId
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<SignFlowDetailDTO> findByVofficeRoleId(Long vofficeRoleId) throws Exception;

}
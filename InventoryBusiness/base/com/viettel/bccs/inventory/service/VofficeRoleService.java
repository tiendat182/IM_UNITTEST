package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.VofficeRoleDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface VofficeRoleService {

    @WebMethod
    public VofficeRoleDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<VofficeRoleDTO> findAll() throws Exception;

    @WebMethod
    public List<VofficeRoleDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(VofficeRoleDTO productSpecCharacterDTO) throws Exception;

    /**
     * Xoa danh sach
     * author Tuydv1
     *
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException;

    /**
     * Tim kiem danh sach
     * author Tuydv1
     *
     * @param vofficeRoleDTO
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<VofficeRoleDTO> search(VofficeRoleDTO vofficeRoleDTO, boolean flag) throws Exception, LogicException;

    @WebMethod
    public VofficeRoleDTO createNewVofficeRole(VofficeRoleDTO vofficeRoleDTO, String staffCode) throws Exception, LogicException;

    @WebMethod
    public BaseMessage update(VofficeRoleDTO vofficeRoleDTO, String staffCode) throws Exception, LogicException;

}
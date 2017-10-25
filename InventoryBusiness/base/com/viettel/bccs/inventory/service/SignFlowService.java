package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.SignFlowDTO;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface SignFlowService {

    @WebMethod
    public SignFlowDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<SignFlowDTO> findAll() throws Exception;

    @WebMethod
    public List<SignFlowDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(SignFlowDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(SignFlowDTO signFlowDTO,List<SignFlowDetailDTO> list, String staffCode) throws Exception;

    /**
     * Ham tra ve danh sanh luong trinh ky theo shopId
     * @author ThanhNT
     * @param shopId
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<SignFlowDTO> getSignFlowByShop(Long shopId) throws Exception;

    @WebMethod
    public List<SignFlowDTO> search(SignFlowDTO signFlowDTO) throws Exception, LogicException;

    @WebMethod
    public SignFlowDTO createNewSignFlow(SignFlowDTO signFlowDTO,List<SignFlowDetailDTO> list, String staffCode) throws Exception, LogicException;

    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException;

}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ApnDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ApnService {

    @WebMethod
    public ApnDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ApnDTO> findAll() throws Exception;

    @WebMethod
    public List<ApnDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ApnDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ApnDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<ApnDTO> checkDuplicateApn(List<ApnDTO> listApn,String typeCheck);

    @WebMethod
    public BaseMessage insertListAPN(List<ApnDTO> listApn) throws Exception;

    @WebMethod
    public List<ApnDTO> searchApn(ApnDTO apnDTO)throws Exception;

    List<ApnDTO> searchApnCorrect(ApnDTO apnDTO) throws Exception;

    @WebMethod
    public List<ApnDTO> searchApnAutoComplete(String input)throws Exception;

    @WebMethod
    ApnDTO findApnById(Long apnId) throws Exception;
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ApnIpDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ApnIpService {

    @WebMethod
    public ApnIpDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ApnIpDTO> findAll() throws Exception;

    @WebMethod
    public List<ApnIpDTO> searchApnIp(ApnIpDTO apnIp) throws Exception;

    @WebMethod
    public List<ApnIpDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ApnIpDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ApnIpDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage insertListAPNIP(List<ApnIpDTO> listApnIp) throws Exception;

    @WebMethod
    public List<ApnIpDTO> checkDuplicateApnIp(List<ApnIpDTO> listApn);

    @WebMethod
    public ApnIpDTO findApnIpById(Long apnIpID) throws Exception;
}
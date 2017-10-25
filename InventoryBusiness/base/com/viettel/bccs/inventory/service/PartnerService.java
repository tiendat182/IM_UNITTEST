package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface PartnerService {

    @WebMethod
    public PartnerDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PartnerDTO> findAll() throws Exception;

    @WebMethod
    public List<PartnerDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(PartnerDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(PartnerDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<PartnerDTO> findPartner(PartnerDTO partnerDTO) throws Exception;

    @WebMethod
    public List<PartnerDTO> getAllPartner() throws Exception;

}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.PartnerContractDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface PartnerContractService {

    @WebMethod
    public PartnerContractDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PartnerContractDTO> findAll() throws Exception;

    @WebMethod
    public List<PartnerContractDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(PartnerContractDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(PartnerContractDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public PartnerContractDTO findByStockTransID(Long stockTransID) throws LogicException, Exception;

    PartnerContractDTO save(PartnerContractDTO partnerContractDTO) throws Exception;
}
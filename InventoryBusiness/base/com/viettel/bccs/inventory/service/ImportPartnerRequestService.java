package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ImportPartnerRequestService {

    @WebMethod
    public ImportPartnerRequestDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ImportPartnerRequestDTO> findAll() throws Exception;

    @WebMethod
    public List<ImportPartnerRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage saveImportPartnerRequest(ImportPartnerRequestDTO productSpecCharacterDTO) throws LogicException, Exception;

    @WebMethod
    public List<ImportPartnerRequestDTO> findImportPartnerRequest(ImportPartnerRequestDTO importPartnerRequestDTO) throws Exception;

    @WebMethod
    public Long getSequence() throws Exception;

    @WebMethod
    public String getRequestCode() throws Exception;
    @WebMethod
    public byte [] getAttachFileContent(Long requestID) throws LogicException, Exception;
}
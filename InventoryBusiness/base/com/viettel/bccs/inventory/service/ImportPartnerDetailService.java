package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ImportPartnerDetailService {

    @WebMethod
    public ImportPartnerDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ImportPartnerDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<ImportPartnerDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage saveImportPartnerDetail(ImportPartnerDetailDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<ImportPartnerDetailDTO> findImportPartnerDetail(ImportPartnerDetailDTO detailDTO) throws Exception;

    @WebMethod
    public void doValidateImportPartnerDetail(ImportPartnerDetailDTO importPartnerDetailDTO) throws LogicException, Exception;
}
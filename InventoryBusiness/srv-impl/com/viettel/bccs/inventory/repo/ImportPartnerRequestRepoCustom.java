package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.ImportPartnerRequest;

public interface ImportPartnerRequestRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<ImportPartnerRequest> findImportPartnerRequest(ImportPartnerRequestDTO importPartnerRequestDTO) throws Exception;

    public Long getSequence() throws Exception;

    public String getRequestCode() throws Exception;

    public byte[] getFileContent(Long importPartnerRequestID) throws Exception;

    public void validateStock(Long staffID, Long shopID) throws LogicException, Exception;

    public void approve(ImportPartnerRequestDTO importPartnerRequestDTO) throws LogicException, Exception;
}
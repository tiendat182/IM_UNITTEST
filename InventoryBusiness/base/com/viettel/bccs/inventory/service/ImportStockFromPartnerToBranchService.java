package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ImportStockFromPartnerToBranchService {
    @WebMethod
    public Long executeStockTrans(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public List<String> getErrorDetails(Long transID) throws LogicException, Exception;
}
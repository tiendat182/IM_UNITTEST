package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * servicee lap lenh nhap hang tu doi tac
 * Created by thanhnt77 on 29/05/2016.
 */
@WebService
public interface CreateUnderlyingFromPartnerService {
    @WebMethod
    public void executeStockTransForPartner(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;


}

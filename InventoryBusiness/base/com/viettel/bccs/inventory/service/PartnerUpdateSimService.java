package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by hoangnt14 on 5/24/2016.
 */
@WebService
public interface PartnerUpdateSimService {

    @WebMethod
    public Long executeStockTrans(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap, String sessionId) throws LogicException, Exception;

    @WebMethod
    public List<String> getErrorDetails(String sessionId, Long productOfferId) throws LogicException, Exception;

}

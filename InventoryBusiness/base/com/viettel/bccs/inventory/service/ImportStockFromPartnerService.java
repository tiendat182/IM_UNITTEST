package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by hoangnt14 on 5/10/2016.
 */
@WebService
public interface ImportStockFromPartnerService {

    @WebMethod
    public Long executeStockTransForPartner(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public List<ImportPartnerDetailDTO> executeStockTransForPartnerByFile(ImportPartnerRequestDTO importPartnerRequestDTO, StaffDTO staffDTO) throws LogicException, Exception;
}

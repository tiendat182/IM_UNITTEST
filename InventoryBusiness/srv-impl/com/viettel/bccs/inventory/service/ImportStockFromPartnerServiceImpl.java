package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockImportFromPartnerByFileService;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockImportFromPartnerService;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockImportFromPartnerToBranchService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hoangnt14 on 5/10/2016.
 */
@Service
public class ImportStockFromPartnerServiceImpl extends BaseServiceImpl implements ImportStockFromPartnerService {

    Logger logger = Logger.getLogger(ImportStockFromPartnerServiceImpl.class);

    @Autowired
    private BaseStockImportFromPartnerService baseStockImportFromPartnerService;

    @Autowired
    private BaseStockImportFromPartnerByFileService baseStockImportFromPartnerByFileService;

    @Override
    public Long executeStockTransForPartner(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        try {
            return baseStockImportFromPartnerService.executeStockTransForPartner(importPartnerRequestDTO, requiredRoleMap);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<ImportPartnerDetailDTO> executeStockTransForPartnerByFile(ImportPartnerRequestDTO importPartnerRequestDTO, StaffDTO staffDTO) throws LogicException, Exception {
        try {
            return baseStockImportFromPartnerByFileService.executeStockTransForPartner(importPartnerRequestDTO, staffDTO);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}

package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockImportFromPartnerToBranchService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HaiNT41 on 2/24/2016.
 */
@Service
public class ImportStockFromPartnerToBranchServiceImpl extends BaseServiceImpl implements ImportStockFromPartnerToBranchService {

    Logger logger = Logger.getLogger(ImportStockFromPartnerToBranchServiceImpl.class);
    @Autowired
    private BaseStockImportFromPartnerToBranchService executor;


    @Override
    public Long executeStockTrans(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        try {
            return executor.executeStockTrans(importPartnerRequestDTO, requiredRoleMap);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<String> getErrorDetails(Long transID) throws LogicException, Exception {
        return executor.getErrorDetails(transID);
    }
}

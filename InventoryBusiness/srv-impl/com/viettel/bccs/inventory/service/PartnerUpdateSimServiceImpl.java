package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by hoangnt14 on 5/24/2016.
 */
@Service
public class PartnerUpdateSimServiceImpl extends BaseServiceImpl implements PartnerUpdateSimService {
    Logger logger = Logger.getLogger(PartnerUpdateSimServiceImpl.class);
    @Autowired
    private BaseStockPartnerUpdateSimService executor;

    @Override
    public Long executeStockTrans(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap, String sessonId) throws LogicException, Exception {
        try {
            return executor.executeStockTrans(importPartnerRequestDTO, requiredRoleMap, sessonId);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<String> getErrorDetails(String sessionId, Long productOfferId) throws LogicException, Exception {
        return executor.getErrorDetails(sessionId, productOfferId);
    }
}

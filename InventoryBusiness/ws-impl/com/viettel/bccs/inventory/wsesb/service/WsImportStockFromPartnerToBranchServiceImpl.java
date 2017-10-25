package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.service.ImportStockFromPartnerToBranchService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsImportStockFromPartnerToBranchServiceImpl")
public class WsImportStockFromPartnerToBranchServiceImpl implements ImportStockFromPartnerToBranchService {

    public static final Logger logger = Logger.getLogger(WsImportStockFromPartnerToBranchServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ImportStockFromPartnerToBranchService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ImportStockFromPartnerToBranchService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public Long executeStockTrans(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.executeStockTrans(importPartnerRequestDTO, requiredRoleMap);
    }

    @Override
    @WebMethod
    public List<String> getErrorDetails(Long transID) throws LogicException, Exception {
        return client.getErrorDetails(transID);
    }

}
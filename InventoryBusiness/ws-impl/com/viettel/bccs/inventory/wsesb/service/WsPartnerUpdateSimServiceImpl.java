package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.service.ImportStockFromPartnerToBranchService;
import com.viettel.bccs.inventory.service.PartnerUpdateSimService;
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

/**
 * Created by hoangnt14 on 5/24/2016.
 */
@Service("WsPartnerUpdateSimServiceImpl")
public class WsPartnerUpdateSimServiceImpl implements PartnerUpdateSimService {
    public static final Logger logger = Logger.getLogger(WsPartnerUpdateSimServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PartnerUpdateSimService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(PartnerUpdateSimService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public Long executeStockTrans(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap, String sessonId) throws LogicException, Exception {
        return client.executeStockTrans(importPartnerRequestDTO, requiredRoleMap, sessonId);
    }

    @Override
    @WebMethod
    public List<String> getErrorDetails(String sessionId, Long productOfferId) throws LogicException, Exception {
        return client.getErrorDetails(sessionId, productOfferId);
    }
}

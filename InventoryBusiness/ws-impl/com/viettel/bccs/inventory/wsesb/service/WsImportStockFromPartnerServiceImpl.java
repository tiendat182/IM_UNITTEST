package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.ImportStockFromPartnerService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by hoangnt14 on 5/10/2016.
 */
@Service("WsImportStockFromPartnerServiceImpl")
public class WsImportStockFromPartnerServiceImpl implements ImportStockFromPartnerService {

    public static final Logger logger = Logger.getLogger(WsImportStockFromPartnerServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ImportStockFromPartnerService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ImportStockFromPartnerService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public Long executeStockTransForPartner(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.executeStockTransForPartner(importPartnerRequestDTO, requiredRoleMap);
    }

    @Override
    public List<ImportPartnerDetailDTO> executeStockTransForPartnerByFile(ImportPartnerRequestDTO importPartnerRequestDTO, StaffDTO staffDTO) throws LogicException, Exception {
        return client.executeStockTransForPartnerByFile(importPartnerRequestDTO, staffDTO);
    }
}

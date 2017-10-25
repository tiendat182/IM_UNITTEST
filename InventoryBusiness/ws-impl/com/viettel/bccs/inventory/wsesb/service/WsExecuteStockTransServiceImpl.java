package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsExecuteStockTransServiceImpl")
public class WsExecuteStockTransServiceImpl implements ExecuteStockTransService {

    public static final Logger logger = Logger.getLogger(WsExecuteStockTransServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ExecuteStockTransService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ExecuteStockTransService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public BaseMessageStockTrans executeStockTrans(String mode, String type, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        return client.executeStockTrans(mode, type, stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
    }

    @Override
    public List<BaseExtMessage> executeStockTransList(String mode, String type, List<StockTransFileDTO> lsStockTransFileDTOs, RequiredRoleMap requiredRoleMap) {
        return client.executeStockTransList(mode, type, lsStockTransFileDTOs, requiredRoleMap);
    }

    @Override
    public BasePartnerMessage createExportToPartner(String mode, String type, StockTransFullDTO stockTransFullDTO, StaffDTO staffDTO, String typeExport, String serialList, RequiredRoleMap requiredRoleMap) {
        return client.createExportToPartner(mode, type, stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
    }
}
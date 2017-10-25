package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.PartnerContractWsDTO;
import com.viettel.bccs.inventory.dto.PartnerShipmentWsDTO;
import com.viettel.bccs.inventory.service.KttsService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsKttsServiceImpl")
public class WsKttsServiceImpl implements KttsService {

    public static final Logger logger = Logger.getLogger(WsKttsServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private KttsService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(KttsService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public List<PartnerContractWsDTO> searchContractBCCS(String fromDate, String toDate, String contractCode) throws LogicException, Exception {
        return client.searchContractBCCS(fromDate, toDate, contractCode);
    }

    @Override
    @WebMethod
    public List<PartnerShipmentWsDTO> searchShipmentBCCS(String contractCode) throws LogicException, Exception {
        return client.searchShipmentBCCS(contractCode);
    }

    @Override
    @WebMethod
    public BaseMessage getResultImpShipment(String reportKCSCode, String bccsStatus) throws LogicException, Exception {
        return client.getResultImpShipment(reportKCSCode, bccsStatus);
    }
}
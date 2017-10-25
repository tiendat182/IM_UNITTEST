package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.service.CommonService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsCommonServiceImpl")
public class WsCommonServiceImpl implements CommonService {

    public static final Logger logger = Logger.getLogger(WsCommonServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private CommonService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(CommonService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public String getStockReportTemplate(Long ownerId, String ownerType) throws LogicException, Exception {
        return client.getStockReportTemplate(ownerId, ownerType);
    }

    @Override
    @WebMethod
    public List<String> getChannelTypes(String ownerType) throws LogicException, Exception {
        return client.getChannelTypes(ownerType);
    }

    @Override
    @WebMethod
    public String getOwnerName(String ownerID, String ownerType) throws Exception {
        return client.getOwnerName(ownerID, ownerType);
    }

    @Override
    @WebMethod
    public List<Long> getChannelTypeByVtUnit(String ownerType, Long vtUnit) throws LogicException, Exception {
        return client.getChannelTypeByVtUnit(ownerType, vtUnit);
    }

    @Override
    @WebMethod
    public boolean checkIsdnVietel(String sNumber) throws LogicException, Exception {
        return client.checkIsdnVietel(sNumber);
    }

    @Override
    public String getProvince(Long shopId) throws Exception {
        return client.getProvince(shopId);
    }

    @Override
    public String getOwnerAddress(String ownerID, String ownerType) throws Exception {
        return client.getOwnerAddress(ownerID, ownerType);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.KcsDTO;
import com.viettel.bccs.inventory.dto.ProductInStockDTO;
import com.viettel.bccs.inventory.model.KcsLog;
import com.viettel.bccs.inventory.model.KcsLogDetail;
import com.viettel.bccs.inventory.model.KcsLogSerial;
import com.viettel.bccs.inventory.repo.KcsLogRepo;
import com.viettel.bccs.inventory.service.KcsService;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
@Service("WsKcsServiceImpl")
class WsKcsServiceImpl implements KcsService {

    public static final Logger logger = Logger.getLogger(WsKcsServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private KcsService client;

    @PostConstruct
    public void init() {
        try {
            client = wsClientFactory.createWsClient(KcsService.class);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    @Override
    @WebMethod
    public List<ProductInStockDTO> findProductInStock(String code, String serial) throws Exception {
        return client.findProductInStock(code, serial);
    }

    @Override
    public List<ProductInStockDTO> findProductInStockIm1(String code, String serial) throws Exception {
        return client.findProductInStockIm1(code,serial);
    }

    /*@Override
    @WebMethod
    public BaseMessage insertKcsLogDetail(KcsLogDetail kcsLogDetail) throws Exception {
        return client.insertKcsLogDetail(kcsLogDetail);
    }*/

    /*@Override
    @WebMethod
    public BaseMessage insertKcsLogSerial(KcsLogSerial kcsLogSerial) throws Exception {
        return client.insertKcsLogSerial(kcsLogSerial);
    }

    @Override
    @WebMethod
    public BaseMessage insertKcsLog(KcsLog kcsLog) throws Exception {
        return client.insertKcsLog(kcsLog);
    }*/

    @Override
    @WebMethod
    public BaseMessage importKcs(Long userId, Long ownerId, HashMap<String, KcsDTO> mapKCS) throws Exception {
        return client.importKcs(userId, ownerId, mapKCS);
    }
}

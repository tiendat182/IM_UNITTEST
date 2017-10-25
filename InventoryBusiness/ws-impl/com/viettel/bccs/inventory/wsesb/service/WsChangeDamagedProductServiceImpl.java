package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ChangeDamagedProductDTO;
import com.viettel.bccs.inventory.dto.ErrorChangeProductDTO;
import com.viettel.bccs.inventory.service.ChangeDamagedProductService;
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

@Service("WsChangeDamagedProductServiceImpl")
public class WsChangeDamagedProductServiceImpl implements ChangeDamagedProductService {

    public static final Logger logger = Logger.getLogger(WsChangeDamagedProductServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ChangeDamagedProductService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ChangeDamagedProductService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public List<ChangeDamagedProductDTO> checkNewSerialInput(String newSerial, Long staffId) throws LogicException, Exception {
        return client.checkNewSerialInput(newSerial, staffId);
    }

    @Override
    @WebMethod
    public BaseMessage saveChangeDamagedProduct(Long productOfferTypeId, Long prodOfferId, Long reasonId, List<ChangeDamagedProductDTO> lstChangeProduct, Long staffId) throws LogicException, Exception {
        return client.saveChangeDamagedProduct(productOfferTypeId, prodOfferId, reasonId, lstChangeProduct, staffId);
    }

    @Override
    @WebMethod
    public List<ErrorChangeProductDTO> saveChangeDamagedProductFile(Long productOfferTypeId, Long prodOfferId, Long reasonId, List<ChangeDamagedProductDTO> lstChangeProduct, Long staffId) throws LogicException, Exception {
        return client.saveChangeDamagedProductFile(productOfferTypeId, prodOfferId, reasonId, lstChangeProduct, staffId);
    }
}
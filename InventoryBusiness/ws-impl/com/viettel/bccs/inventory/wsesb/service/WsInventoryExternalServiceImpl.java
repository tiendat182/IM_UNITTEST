package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.InventoryExternalService;
import com.viettel.bccs.inventory.service.InvoiceListService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

@Service("WsInventoryExternalServiceImpl")
public class WsInventoryExternalServiceImpl implements InventoryExternalService {

    public static final Logger logger = Logger.getLogger(WsInventoryExternalServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private InventoryExternalService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(InventoryExternalService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockSimMessage getSimInfor(String msisdn) {
        return client.getSimInfor(msisdn);
    }


    @Override
    @WebMethod
    public boolean isCaSim(String serial) {
        return client.isCaSim(serial);
    }

    @Override
    @WebMethod
    public StockHandsetDTO getStockModelSoPin(String serial) {
        return client.getStockModelSoPin(serial);
    }

    @Override
    @WebMethod
    public StockTotalResultDTO getQuantityInEcomStock(String prodOfferCode) throws LogicException, Exception {
        return client.getQuantityInEcomStock(prodOfferCode);
    }

    @Override
    @WebMethod
    public BaseMessage verifyUnlockG6(String imei) throws LogicException, Exception {
        return client.verifyUnlockG6(imei);
    }

    @Override
    @WebMethod
    public BaseMessage unlockG6(String imei) throws LogicException, Exception {
        return client.unlockG6(imei);
    }

    @Override
    @WebMethod
    public StockDeliveringResultDTO getLstStockDeliveringBill(Date startTime, Date endTime) throws LogicException, Exception {
        return client.getLstStockDeliveringBill(startTime, endTime);
    }

    @Override
    @WebMethod
    public BaseMessage serialCardIsSaledOnBCCS(String serial) throws LogicException, Exception {
        return client.serialCardIsSaledOnBCCS(serial);
    }

    @Override
    @WebMethod
    public BaseMessage checkTransferCondition(Long staffId, boolean checkCollaborator) throws LogicException, Exception {
        return client.checkTransferCondition(staffId, checkCollaborator);
    }

    @Override
    @WebMethod
    public StampInforDTO getStampInformation(List<StampListDTO> lstStampInput) throws LogicException, Exception {
        return client.getStampInformation(lstStampInput);
    }

    @Override
    @WebMethod
    public StockDeliverResultDTO checkStaffHaveStockDeliver(List<String> staffCode) throws LogicException, Exception {
        return client.checkStaffHaveStockDeliver(staffCode);
    }
}
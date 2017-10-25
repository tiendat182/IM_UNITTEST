package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ImpExpStockDTO;
import com.viettel.bccs.inventory.message.BaseWarrantyMessage;
import com.viettel.bccs.inventory.service.WarrantyService;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

@Service("WsWarrantyServiceImpl")
public class WsWarrantyServiceImpl implements WarrantyService {

    public static final Logger logger = Logger.getLogger(WsWarrantyServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private WarrantyService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(WarrantyService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage findStockHandSet(Long stockHandsetId, Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, Long status) {
        return client.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Override
    @WebMethod
    public Date findExportDateBySerial(String prodOfferCode, String serial) {
        return client.findExportDateBySerial(prodOfferCode, serial);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage viewStockDetail(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) {
        return client.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage exportStock(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        return client.exportStock(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage impportStock(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        return client.impportStock(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage exportImportStock(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType, String staffCode) {
        return client.exportImportStock(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType, staffCode);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage exportStockWarranty(List<ImpExpStockDTO> listSerial, Long newStateId, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        return client.exportStockWarranty(listSerial, newStateId, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage importStockGPON(List<ImpExpStockDTO> listSerial, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType) {
        return client.importStockGPON(listSerial, fromOwnerId, fromOwnerType, toOwnerId, toOwnerType);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage getWarrantyStockLog(Long logId, String methodName, String paramsInput, String responseCode, String description, Date fromDate, Date endDate) {
        return client.getWarrantyStockLog(logId, methodName, paramsInput, responseCode, description, fromDate, endDate);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage viewStockModelWarranty(Long ownerType, Long ownerId, String stockModelCode, String stockModelName, Long stateId) {
        return client.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Override
    @WebMethod
    public BaseWarrantyMessage viewStockKV(String shopCode, Long prodOfferId, Long stateId) {
        return client.viewStockKV(shopCode, prodOfferId, stateId);
    }

    @Override
    public BaseWarrantyMessage getInventoryInfoByListProdCode(List<String> listProdOfferCode) {
        return client.getInventoryInfoByListProdCode(listProdOfferCode);
    }
}
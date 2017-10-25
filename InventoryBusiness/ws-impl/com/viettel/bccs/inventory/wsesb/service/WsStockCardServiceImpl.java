package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockCard;
import com.viettel.bccs.inventory.service.StockCardService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service("WsStockCardServiceImpl")
public class WsStockCardServiceImpl implements StockCardService {

    public static final Logger logger = Logger.getLogger(WsStockCardServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockCardService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockCardService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockCardDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockCardDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public void updateStockCard(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, StockCardDTO stockCard, String fromSerial, String toSerial, Long newStatus, Long newOwnerType, Long newOwnerId) throws LogicException, Exception {
        client.updateStockCard(flagStockDTO, stockTransDTO, stockTransActionDTO, stockCard, fromSerial, toSerial, newStatus, newOwnerType, newOwnerId);
    }

    @Override
    @WebMethod
    public void doSaveStockCard(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, StockTransDetailDTO stockTransDetail, Long newStatus, Long oldStatus, Long newOwnerType, Long oldOwnerType, Long newOwnerId, Long oldOwnerId) throws LogicException, Exception {
        client.doSaveStockCard(flagStockDTO, stockTransDTO, stockTransActionDTO, stockTransDetail, newStatus, oldStatus, newOwnerType, oldOwnerType, newOwnerId, oldOwnerId);
    }

    @Override
    @WebMethod
    public List<StockCard> findStockCard(StockTransDetailDTO stockTransDetail, Long oldStatus, Long oldOwnerType, Long oldOwnerId, String fromSerial, String toSerial) throws LogicException, Exception {
        return client.findStockCard(stockTransDetail, oldStatus, oldOwnerType, oldOwnerId, fromSerial, toSerial);
    }

    @Override
    @WebMethod
    public boolean checkNotExistInVC(Long prodOfferId, String fromSerial, String toSerial) throws LogicException, Exception {
        return client.checkNotExistInVC(prodOfferId, fromSerial, toSerial);
    }

    @Override
    @WebMethod
    public void insertStockCardFromSaled(Long prodOfferId, String serial) throws LogicException, Exception {
        client.insertStockCardFromSaled(prodOfferId, serial);
    }

    @Override
    @WebMethod
    public boolean checkStockCardIm1(Long prodOfferId, String serial) throws LogicException, Exception {
        return client.checkStockCardIm1(prodOfferId, serial);
    }

    @Override
    @WebMethod
    public void insertStockCardFromSaledIm1(Long prodOfferId, String serial) throws LogicException, Exception {
        client.insertStockCardFromSaledIm1(prodOfferId, serial);
    }

    @Override
    @WebMethod
    public void updatePincodeForStockCard(Long ownerId, String serial, Long prodOfferId, String pincode, String updateType, StaffDTO staffDTO) throws LogicException, Exception {
        client.updatePincodeForStockCard(ownerId, serial, prodOfferId, pincode, updateType, staffDTO);
    }

    @Override
    @WebMethod
    public int updateListPincodeForStockCard(List<UpdatePincodeDTO> lstPincodes, Long ownerId, Long prodOfferId, String updateType, StaffDTO staffDTO) throws LogicException, Exception {
        return client.updateListPincodeForStockCard(lstPincodes, ownerId, prodOfferId, updateType, staffDTO);
    }

    @Override
    @WebMethod
    public boolean checkInfoStockCard(String pincode, Long ownerId, String serial, Long prodOfferId, String updateType, Long stateId) throws Exception {
        return client.checkInfoStockCard(pincode, ownerId, serial, prodOfferId, updateType, stateId);
    }

    @Override
    public StockTotalResultDTO getQuantityInEcomStock(String prodOfferCode) throws LogicException, Exception {
        return client.getQuantityInEcomStock(prodOfferCode);
    }

    @Override
    @WebMethod
    public List<Long> getStatusSerialCardSale(String serial) throws LogicException, Exception {
        return client.getStatusSerialCardSale(serial);
    }

    @Override
    @WebMethod
    public BaseMessage unlockG6(String imei) throws LogicException, Exception {
        return client.unlockG6(imei);
    }

    @Override
    @WebMethod
    public void exchangeCardBankplus(ExchangeCardBankplusDTO exchangeCardBankplusDTO, Long shopId, StaffDTO staffDTO) throws LogicException, Exception {
        client.exchangeCardBankplus(exchangeCardBankplusDTO, shopId, staffDTO);
    }

    @Override
    @WebMethod
    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serial) throws Exception {
        return client.getSerialList(serial);
    }
}
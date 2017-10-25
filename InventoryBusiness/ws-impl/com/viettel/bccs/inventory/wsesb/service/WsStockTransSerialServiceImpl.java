package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.service.StockTransSerialService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service("WsStockTransSerialServiceImpl")
public class WsStockTransSerialServiceImpl implements StockTransSerialService {

    public static final Logger logger = Logger.getLogger(WsStockTransSerialServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransSerialService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockTransSerialService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockTransSerialDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public StockTransSerialDTO save(StockTransSerialDTO stockTransSerialDTO) throws Exception {
        return client.save(stockTransSerialDTO);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> findByStockTransDetailId(Long stockTransDetaiId) throws LogicException, Exception {
        return client.findByStockTransDetailId(stockTransDetaiId);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> findByStockTransId(Long stockTransId) throws LogicException, Exception {
        return client.findByStockTransId(stockTransId);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> findStockTransSerial(List<Long> stockActionIds) throws LogicException, Exception {
        return client.findStockTransSerial(stockActionIds);
    }

    @Override
    public List<StockTransSerialDTO> findStockTransSerialByDTO(StockTransSerialDTO stockTransSerialDTO) throws LogicException, Exception {
        return client.findStockTransSerialByDTO(stockTransSerialDTO);
    }

    @Override
    public List<StockTransSerialDTO> getRangeSerial(Long ownerType, Long ownerId, Long productOfferId, String tableName, Long stateId, String channelTypeId, Long serialStatus) throws Exception {
        return client.getRangeSerial(ownerType, ownerId, productOfferId, tableName, stateId, channelTypeId, serialStatus);
    }

    @Override
    public List<StockTransSerialDTO> getRangeSerialFifo(Long ownerType, Long ownerId, Long productOfferId, String tableName, Long stateId, String channelTypeId, Long serialStatus) throws Exception {
        return client.getRangeSerialFifo(ownerType, ownerId, productOfferId, tableName, stateId, channelTypeId, serialStatus);
    }

    @Override
    public List<String> getListSerialValid(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId, String fromSerial, String toSerial, Long rowNum, Long serialStatus) throws Exception {
        return client.getListSerialValid(ownerType, ownerId, tableName, productOfferId, stateId, fromSerial, toSerial, rowNum, serialStatus);
    }

    @Override
    public List<String> getRangeSerialStockCardValid(Long ownerType, Long ownerId, Long productOfferId, Long stateId, String fromSerial, String toSerial, Long quantity, String status) throws Exception {
        return client.getRangeSerialStockCardValid(ownerType, ownerId, productOfferId, stateId, fromSerial, toSerial, quantity, status);
    }

    @Override
    public List<StockTransSerialDTO> getListSerialSelect(StockTransFullDTO stockTransFullDTO) throws LogicException, Exception {
        return client.getListSerialSelect(stockTransFullDTO);
    }

    @Override
    public List<StockTransSerialDTO> getListSerialGroup(List<String> listSerials) throws Exception {
        return client.getListSerialGroup(listSerials);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> findStockTransSerialByProductOfferType(WareHouseInfoDTO infoDTO) throws Exception {
        return client.findStockTransSerialByProductOfferType(infoDTO);


    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> getListSerialFromTable(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName) throws LogicException, Exception {
        return client.getListSerialFromTable(prodOfferId, ownerId, ownerType, stateId, tableName);
    }

    @Override
    @WebMethod
    public List<StockInspectCheckDTO> getListGatherSerial(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName, String fromSerial, String toSerial) throws LogicException, Exception {
        return client.getListGatherSerial(productOfferTypeId, prodOfferId, ownerId, ownerType, stateId, tableName, fromSerial, toSerial);
    }

    @Override
    @WebMethod
    public List<StockInspectCheckDTO> getListSerialFromList(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName, List<String> serial2D) throws LogicException, Exception {
        return client.getListSerialFromList(productOfferTypeId, prodOfferId, ownerId, ownerType, stateId, tableName, serial2D);
    }

    @Override
    @WebMethod
    public int updateStatusStockSerial(Long ownerId, Long ownerType, Long prodOfferId, String fromSerial, String toSerial, Long newStatus, Long oldStatus) throws Exception {
        return client.updateStatusStockSerial(ownerId, ownerType, prodOfferId, fromSerial, toSerial, newStatus, oldStatus);
    }

    @Override
    @WebMethod
    public Long getDepostiPriceFromStockX(Long ownerId, Long ownerType, Long prodOfferId, String serial) throws Exception {
        return client.getDepostiPriceFromStockX(ownerId, ownerType, prodOfferId, serial);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> getListSerialQuantity(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String status, String tableName, String fromSerial, String toSerial) throws LogicException, Exception {
        return client.getListSerialQuantity(prodOfferId, ownerId, ownerType, stateId, status, tableName, fromSerial, toSerial);
    }

    @Override
    public List<LookupSerialByFileDTO> searchSerialExact(Long productOfferTypeId, List<String> serial, String serialGpon, boolean isSearchNew) throws Exception {
        return client.searchSerialExact(productOfferTypeId, serial, serialGpon, isSearchNew);
    }

    @Override
    public List<ViewSerialHistoryDTO> listLookUpSerialHistory(String serial, Long prodOfferId, Date fromDate, Date toDate, boolean isOffline) throws Exception {
        return client.listLookUpSerialHistory(serial, prodOfferId, fromDate, toDate, isOffline);
    }

    @Override
    @WebMethod
    public List<ChangeDamagedProductDTO> getListSerialProduct(String serial, Long ownerType, Long ownerId) throws LogicException, Exception {
        return client.getListSerialProduct(serial, ownerType, ownerId);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> getListStockCardSaledBySerial(Long prodOfferId, String serial) throws LogicException, Exception {
        return client.getListStockCardSaledBySerial(prodOfferId, serial);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> getListBySerial(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, String tableName) throws LogicException, Exception {
        return client.getListBySerial(prodOfferId, serial, ownerType, ownerId, status, tableName);
    }

    @Override
    @WebMethod
    public Long getDepostiPriceForRetrieve(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String serial) throws Exception {
        return client.getDepostiPriceForRetrieve(ownerId, ownerType, prodOfferId, stateId, serial);
    }

    @Override
    @WebMethod
    public Long getDepostiPriceByRangeSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String fromSerial, String toSerial) throws Exception {
        return client.getDepostiPriceByRangeSerial(ownerId, ownerType, prodOfferId, stateId, fromSerial, toSerial);
    }

    @Override
    public List<StockTransSerialDTO> getStockTransSerialByStockTransId(Long stockTransId, Long prodOfferId, Long toOwnerType) throws Exception {
        return client.getStockTransSerialByStockTransId(stockTransId, prodOfferId, toOwnerType);
    }

    @Override
    @WebMethod
    public int updateStockBalance(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String strFromSerial, String strToSerial, Long stockTransId, Long newStatus, Long oldStatus) throws Exception {
        return client.updateStockBalance(ownerId, ownerType, prodOfferId, stateId, strFromSerial, strToSerial, stockTransId, newStatus, oldStatus);
    }

    @Override
    public List<StockDeviceDTO> getLstStockDevice(Long stockTransActionId) throws LogicException, Exception {
        return client.getLstStockDevice(stockTransActionId);
    }

    @Override
    public List<StockDeviceTransferDTO> getLstDeviceTransfer(Long prodOfferId, Long stateId, Long stockRequestId) throws LogicException, Exception {
        return client.getLstDeviceTransfer(prodOfferId, stateId, stockRequestId);
    }
}
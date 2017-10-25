package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StockHandsetService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service("WsStockHandsetServiceImpl")
public class WsStockHandsetServiceImpl implements StockHandsetService {

    public static final Logger logger = Logger.getLogger(WsStockHandsetServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockHandsetService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockHandsetService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockHandsetDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockHandsetDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public StockHandsetDTO checkSerialSale(Long productOfferingId, String serialRecover, String status) throws Exception {
        return client.checkSerialSale(productOfferingId, serialRecover, status);
    }

    @Override
    @WebMethod
    public StockHandsetDTO getStockHandsetById(Long productOfferingId) throws Exception {
        return client.getStockHandsetById(productOfferingId);
    }

    @Override
    @WebMethod
    public BaseMessage update(StockHandsetDTO stockHandsetDTO) throws Exception {
        return client.update(stockHandsetDTO);
    }

    @Override
    @WebMethod
    public BaseMessage updateUctt(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException {
        return client.updateUctt(productOfferId, serialRecover, ownerId, ownerType, productOfferIdUCTT, updateDatetime);
    }

    @Override
    @WebMethod
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                 FlagStockDTO flagStockDTO) throws Exception, LogicException {
        client.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
    }

    @Override
    @WebMethod
    public int updateForStockRescue(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception {
        return client.updateForStockRescue(stateId, ownerId, serial, prodOfferId);
    }

    @Override
    @WebMethod
    public int updateForStockRescueIM1(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception {
        return client.updateForStockRescueIM1(stateId, ownerId, serial, prodOfferId);
    }

    @Override
    @WebMethod
    public List<UpdateSerialGponDTO> updateSerialGpon(String updateType, List<UpdateSerialGponDTO> lstSerial) throws LogicException, Exception {
        return client.updateSerialGpon(updateType, lstSerial);
    }

    @Override
    @WebMethod
    public StockHandsetDTO updateLicenseKey(UpdateLicenseDTO updateLicenseDTO) throws LogicException, Exception {
        return client.updateLicenseKey(updateLicenseDTO);
    }

    @Override
    @WebMethod
    public StockHandsetDTO checkExsitStockHandset(Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, String updateType) throws LogicException, Exception {
        return client.checkExsitStockHandset(prodOfferId, serial, ownerId, ownerType, stateId, updateType);
    }

    @Override
    @WebMethod
    public int updateListLicenseKey(List<UpdateLicenseDTO> lstUpdateLicenseDTOs, Long ownerId, Long prodOfferId, String updateType) throws LogicException, Exception {
        return client.updateListLicenseKey(lstUpdateLicenseDTOs, ownerId, prodOfferId, updateType);
    }

    @Override
    public List<StockHandsetDTO> findStockHandSet(Long stockHandsetId, Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, Long status) throws LogicException, Exception {
        return client.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Override
    public Date findExportDateBySerial(String productOfferCode, String serial) throws Exception, LogicException {
        return client.findExportDateBySerial(productOfferCode, serial);
    }

    @Override
    @WebMethod
    public Long getQuantityStockX(Long ownerId, Long ownerType, Long prodOfferId, List<Serial> lstSerial, Long status, Long stateId) throws LogicException, Exception {
        return client.getQuantityStockX(ownerId, ownerType, prodOfferId, lstSerial, status, stateId);
    }

    @Override
    public StockHandsetDTO getStockModelSoPin(String serial) {
        return client.getStockModelSoPin(serial);
    }

    @Override
    public boolean checkExistSerial(String serial) {
        return client.checkExistSerial(serial);
    }

    @Override
    public boolean checkUnlockSerial(String serial) {
        return client.checkUnlockSerial(serial);
    }

    @Override
    public Object[] getInfomationSerial(String serial) {
        return client.getInfomationSerial(serial);
    }

    @Override
    public boolean saveUnlockG6(String serial, Long prodOfferId) {
        return client.saveUnlockG6(serial, prodOfferId);
    }

    @Override
    public StockHandsetDTO findStockHandsetByProdIdAndSerial(Long prodOfferId, String serial) throws Exception {
        return client.findStockHandsetByProdIdAndSerial(prodOfferId, serial);
    }

    @Override
    @WebMethod
    public StockHandsetDTO create(StockHandsetDTO stockHandsetDTO) throws Exception {
        return client.create(stockHandsetDTO);
    }

    @Override
    @WebMethod
    public BaseMessage updateUcttIm1(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException {
        return client.updateUcttIm1(productOfferId, serialRecover, ownerId, ownerType, productOfferIdUCTT, updateDatetime);
    }
}
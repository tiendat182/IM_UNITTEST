package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.StockHandsetRescueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.StockHandsetRescueService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsStockHandsetRescueServiceImpl")
public class WsStockHandsetRescueServiceImpl implements StockHandsetRescueService {

    public static final Logger logger = Logger.getLogger(WsStockHandsetRescueServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockHandsetRescueService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockHandsetRescueService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public StockHandsetRescueDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<StockHandsetRescueDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockHandsetRescueDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockHandsetRescueDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockHandsetRescueDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<StockHandsetRescueDTO> getListHansetRescue(Long ownerId) throws LogicException, Exception {
        return client.getListHansetRescue(ownerId);
    }

    @Override
    @WebMethod
    public List<StockHandsetRescueDTO> getListProductForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception {
        return client.getListProductForRescue(ownerId, lstSerial);
    }

    @Override
    @WebMethod
    public List<StockHandsetRescueDTO> getListSerialForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception {
        return client.getListSerialForRescue(ownerId, lstSerial);
    }

    @Override
    @WebMethod
    public int updateStatusSerialForRescue(Long statusAffer, Long statusBefor, Long ownerId, String serial, Long prodOfferId) throws LogicException, Exception {
        return client.updateStatusSerialForRescue(statusAffer, statusBefor, ownerId, serial, prodOfferId);
    }
}
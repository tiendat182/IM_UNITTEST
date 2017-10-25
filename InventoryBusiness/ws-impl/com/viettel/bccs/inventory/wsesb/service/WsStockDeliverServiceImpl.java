package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.StockDeliverDTO;
import com.viettel.bccs.inventory.model.StockDeliverDetail;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;


import com.viettel.bccs.inventory.service.StockDeliverService;

import java.util.Date;
import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsStockDeliverServiceImpl")
public class WsStockDeliverServiceImpl implements StockDeliverService {

    public static final Logger logger = Logger.getLogger(WsStockDeliverServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockDeliverService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockDeliverService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public StockDeliverDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<StockDeliverDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockDeliverDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public StockDeliverDTO save(StockDeliverDTO dto) throws Exception {
        return client.save(dto);
    }

    @Override
    public BaseMessage update(StockDeliverDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public void deliverStock(StockDeliverDTO stockDeliverDTO) throws LogicException, Exception {
        client.deliverStock(stockDeliverDTO);
    }

    @Override
    @WebMethod
    public StockDeliverDTO getStockDeliverByOwnerIdAndStatus(Long ownerId, Long status) throws LogicException, Exception {
        return client.getStockDeliverByOwnerIdAndStatus(ownerId, status);
    }

    @Override
    @WebMethod
    public List<Long> getAllStockForDelete() throws LogicException, Exception {
        return client.getAllStockForDelete();
    }

    @Override
    @WebMethod
    public List<StockDeliverDTO> searchHistoryStockDeliver(String code, Date fromDate, Date toDate, String status, Long ownerId, Long ownerType) throws Exception {
        return client.searchHistoryStockDeliver(code, fromDate, toDate, status, ownerId, ownerType);
    }

    @Override
    @WebMethod
    public Long getMaxId() throws LogicException, Exception {
        return client.getMaxId();
    }
}
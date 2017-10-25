package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.StockTransLogisticDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.StockTransLogisticService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockTransLogisticServiceImpl")
public class WsStockTransLogisticServiceImpl implements StockTransLogisticService {

    public static final Logger logger = Logger.getLogger(WsStockTransLogisticServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransLogisticService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTransLogisticService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public StockTransLogisticDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockTransLogisticDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockTransLogisticDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(StockTransLogisticDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(StockTransLogisticDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public StockTransLogisticDTO save(StockTransLogisticDTO stockTransLogisticDTO) throws Exception {
        return client.save(stockTransLogisticDTO);
    }

    @Override
    @WebMethod
    public StockTransLogisticDTO findByStockTransId(Long stockTransId) throws Exception {
        return client.findByStockTransId(stockTransId);
    }

    @Override
    public List<StockTransLogisticDTO> getLstOrderLogistics(Long id, Long maxRetry, Long maxDay, Long numberThread) throws Exception {
        return client.getLstOrderLogistics(id, maxRetry, maxDay, numberThread);
    }

    @Override
    public void updateStockTransLogistics(Long stockTransLogistic, Long status, BaseMessage baseMessage) throws Exception {
        client.updateStockTransLogistics(stockTransLogistic, status, baseMessage);
    }

    @Override
    public void updateStockTransLogisticRetry(Long maxRetry, Long stockTransLogistic, BaseMessage baseMessage) throws Exception {
        client.updateStockTransLogisticRetry(maxRetry, stockTransLogistic, baseMessage);
    }
}
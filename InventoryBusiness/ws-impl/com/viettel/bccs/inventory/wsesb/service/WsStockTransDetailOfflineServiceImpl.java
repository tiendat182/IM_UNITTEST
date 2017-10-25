package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailOfflineDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.StockTransDetailOfflineService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockTransDetailOfflineServiceImpl")
public class WsStockTransDetailOfflineServiceImpl implements StockTransDetailOfflineService {

    public static final Logger logger = Logger.getLogger(WsStockTransDetailOfflineServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransDetailOfflineService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTransDetailOfflineService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockTransDetailOfflineDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockTransDetailOfflineDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockTransDetailOfflineDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockTransDetailOfflineDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockTransDetailOfflineDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public StockTransDetailOfflineDTO save(StockTransDetailOfflineDTO stockTransDetailOfflineDTO) throws Exception {
        return client.save(stockTransDetailOfflineDTO);
    }
}
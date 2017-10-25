package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.StockTransDetailOfflineDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialOfflineDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.StockTransSerialOfflineService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockTransSerialOfflineServiceImpl")
public class WsStockTransSerialOfflineServiceImpl implements StockTransSerialOfflineService {

    public static final Logger logger = Logger.getLogger(WsStockTransSerialOfflineServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransSerialOfflineService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTransSerialOfflineService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockTransSerialOfflineDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockTransSerialOfflineDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockTransSerialOfflineDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockTransSerialOfflineDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockTransSerialOfflineDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    public StockTransSerialOfflineDTO save(StockTransSerialOfflineDTO dto) throws Exception  {
        return client.save(dto);
    }

}
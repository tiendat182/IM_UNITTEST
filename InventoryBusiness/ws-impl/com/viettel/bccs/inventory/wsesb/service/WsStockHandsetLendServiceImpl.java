package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.StockHandsetLendDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.StockHandsetLendService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockHandsetLendServiceImpl")
public class WsStockHandsetLendServiceImpl implements StockHandsetLendService {

    public static final Logger logger = Logger.getLogger(WsStockHandsetLendServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockHandsetLendService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockHandsetLendService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockHandsetLendDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockHandsetLendDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockHandsetLendDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockHandsetLendDTO dto) throws Exception, LogicException {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockHandsetLendDTO dto) throws Exception, LogicException  {
        return client.update(dto);
    }

}
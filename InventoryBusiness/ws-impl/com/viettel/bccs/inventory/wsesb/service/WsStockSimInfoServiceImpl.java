package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.StockSimInfoDTO;
import com.viettel.bccs.inventory.service.StockSimInfoService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service("WsStockSimInfoServiceImpl")
public class WsStockSimInfoServiceImpl implements StockSimInfoService {

    public static final Logger logger = Logger.getLogger(WsStockSimInfoServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockSimInfoService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockSimInfoService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public StockSimInfoDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockSimInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }
}
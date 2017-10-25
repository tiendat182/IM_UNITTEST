package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopLockDTO;
import com.viettel.bccs.inventory.service.StockIsdnVtShopLockService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;
@Service("WsStockIsdnVtShopLockServiceImpl")
public class WsStockIsdnVtShopLockServiceImpl implements StockIsdnVtShopLockService {

    public static final Logger logger = Logger.getLogger(WsStockIsdnVtShopLockServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockIsdnVtShopLockService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockIsdnVtShopLockService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockIsdnVtShopLockDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockIsdnVtShopLockDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockIsdnVtShopLockDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockIsdnVtShopLockDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockIsdnVtShopLockDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public void deleteShopLock(String isdn) throws Exception {
        client.deleteShopLock(isdn);
    }

    @Override
    @WebMethod
    public StockIsdnVtShopLockDTO save(StockIsdnVtShopLockDTO dto) throws Exception {
        return client.save(dto);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopFeeDTO;
import com.viettel.bccs.inventory.service.StockIsdnVtShopFeeService;
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
@Service("WsStockIsdnVtShopFeeServiceImpl")
public class WsStockIsdnVtShopFeeServiceImpl implements StockIsdnVtShopFeeService {

    public static final Logger logger = Logger.getLogger(WsStockIsdnVtShopFeeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockIsdnVtShopFeeService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockIsdnVtShopFeeService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockIsdnVtShopFeeDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockIsdnVtShopFeeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockIsdnVtShopFeeDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockIsdnVtShopFeeDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockIsdnVtShopFeeDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public StockIsdnVtShopFeeDTO save(StockIsdnVtShopFeeDTO dto) throws Exception {
        return client.save(dto);
    }

    @Override
    @WebMethod
    public void deleteFee(String isdn) throws Exception {
        client.deleteFee(isdn);
    }
}
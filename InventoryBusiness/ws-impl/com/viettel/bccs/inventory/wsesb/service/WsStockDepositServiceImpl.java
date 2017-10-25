package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.StockDepositDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.StockDepositService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockDepositServiceImpl")
public class WsStockDepositServiceImpl implements StockDepositService {

    public static final Logger logger = Logger.getLogger(WsStockDepositServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockDepositService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockDepositService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockDepositDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockDepositDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockDepositDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockDepositDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockDepositDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public StockDepositDTO getStockDeposit(Long prodOfferId, Long channelTypeId, Long transType) throws Exception {
        return client.getStockDeposit(prodOfferId, channelTypeId, transType);
    }
}
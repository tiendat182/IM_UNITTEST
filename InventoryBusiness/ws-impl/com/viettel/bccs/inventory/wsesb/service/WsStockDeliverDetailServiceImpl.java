package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.StockDeliverDetailService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockDeliverDetailServiceImpl")
public class WsStockDeliverDetailServiceImpl implements StockDeliverDetailService {

    public static final Logger logger = Logger.getLogger(WsStockDeliverDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockDeliverDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockDeliverDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockDeliverDetailDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockDeliverDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockDeliverDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public StockDeliverDetailDTO save(StockDeliverDetailDTO dto) throws Exception  {
        return client.save(dto);
    }

    @Override
    public BaseMessage update(StockDeliverDetailDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<StockDeliverDetailDTO> viewStockTotalFull(Long ownerId, Long ownerType) throws Exception {
        return client.viewStockTotalFull(ownerId, ownerType);
    }

    @Override
    @WebMethod
    public List<StockDeliverDetailDTO> getLstDetailByStockDeliverId(Long stockDeliverId) throws Exception {
        return client.getLstDetailByStockDeliverId(stockDeliverId);
    }
}
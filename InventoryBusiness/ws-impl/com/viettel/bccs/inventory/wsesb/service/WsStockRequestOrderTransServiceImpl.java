package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.StockRequestOrderTransDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.StockRequestOrderTransService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockRequestOrderTransServiceImpl")
public class WsStockRequestOrderTransServiceImpl implements StockRequestOrderTransService {

    public static final Logger logger = Logger.getLogger(WsStockRequestOrderTransServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockRequestOrderTransService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockRequestOrderTransService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockRequestOrderTransDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockRequestOrderTransDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockRequestOrderTransDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public StockRequestOrderTransDTO save(StockRequestOrderTransDTO dto) throws Exception  {
        return client.save(dto);
    }

    @Override
    public BaseMessage update(StockRequestOrderTransDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    public StockRequestOrderTransDTO getOrderTrans(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId, Long type) throws Exception {
        return client.getOrderTrans(stockRequestOrderId, fromOwnerId, toOwnerId, type);
    }

    @Override
    public StockRequestOrderTransDTO getOrderTransByStockTransId(Long stockTransId) throws Exception {
        return client.getOrderTransByStockTransId(stockTransId);
    }
}
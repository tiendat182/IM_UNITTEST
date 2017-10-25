package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.partner.dto.StockOwnerTmpDTO;
import com.viettel.bccs.partner.service.StockOwnerTmpService;
import com.viettel.fw.Exception.LogicException;
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

@Service("WsStockOwnerTmpServiceImpl")
public class WsStockOwnerTmpServiceImpl implements StockOwnerTmpService {

    public static final Logger logger = Logger.getLogger(WsStockOwnerTmpServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockOwnerTmpService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockOwnerTmpService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockOwnerTmpDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockOwnerTmpDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockOwnerTmpDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockOwnerTmpDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockOwnerTmpDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<StockOwnerTmpDTO> getStockOwnerTmpByOwnerId(Long ownerId, Long ownerType) throws Exception, LogicException {
        return client.getStockOwnerTmpByOwnerId(ownerId, ownerType);
    }
}
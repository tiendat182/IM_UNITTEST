package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.StockDocumentDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.StockDocumentService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockDocumentServiceImpl")
public class WsStockDocumentServiceImpl implements StockDocumentService {

    public static final Logger logger = Logger.getLogger(WsStockDocumentServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockDocumentService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockDocumentService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockDocumentDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockDocumentDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockDocumentDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockDocumentDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockDocumentDTO dto) throws Exception  {
        return client.update(dto);
    }
    @Override
    public List<StockDocumentDTO> getListStockDocumentDTOByActionId(Long actionId) throws Exception {
        return client.getListStockDocumentDTOByActionId(actionId);
    }

}
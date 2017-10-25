package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockTransExtDTO;
import com.viettel.bccs.inventory.service.StockTransExtService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsStockTransExtServiceImpl")
public class WsStockTransExtServiceImpl implements StockTransExtService {

    public static final Logger logger = Logger.getLogger(WsStockTransExtServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransExtService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTransExtService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockTransExtDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockTransExtDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockTransExtDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockTransExtDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockTransExtDTO dto) throws Exception  {
        return client.update(dto);
    }

}
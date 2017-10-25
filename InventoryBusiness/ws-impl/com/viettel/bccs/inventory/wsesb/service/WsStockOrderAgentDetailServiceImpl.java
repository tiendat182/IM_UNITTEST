package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.StockOrderAgentDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.StockOrderAgentDetailService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsStockOrderAgentDetailServiceImpl")
public class WsStockOrderAgentDetailServiceImpl implements StockOrderAgentDetailService {

    public static final Logger logger = Logger.getLogger(WsStockOrderAgentDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockOrderAgentDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockOrderAgentDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockOrderAgentDetailDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockOrderAgentDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockOrderAgentDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public StockOrderAgentDetailDTO create(StockOrderAgentDetailDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockOrderAgentDetailDTO dto) throws Exception  {
        return client.update(dto);
    }
    @Override
    public List<StockOrderAgentDetailDTO> getByStockOrderAgentId(Long id) throws Exception {
        return client.getByStockOrderAgentId(id);
    }

@Override
    @WebMethod
    public List<StockTransFullDTO> getListGood(Long orderAgentId) {
        return client.getListGood(orderAgentId);
    }
}
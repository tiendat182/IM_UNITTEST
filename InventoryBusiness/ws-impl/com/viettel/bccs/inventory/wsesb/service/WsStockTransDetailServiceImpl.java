package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service("WsStockTransDetailServiceImpl")
public class WsStockTransDetailServiceImpl implements StockTransDetailService {

    public static final Logger logger = Logger.getLogger(WsStockTransDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTransDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public StockTransDetailDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public StockTransDetailDTO save(StockTransDetailDTO stockTransDetailDTO) throws Exception {
        return client.save(stockTransDetailDTO);
    }

    @Override
    @WebMethod
    public List<StockTransDetailDTO> findByStockTransId(Long stockTransId) throws Exception{
        return client.findByStockTransId(stockTransId);
    }

    @Override
    @WebMethod
    public StockTransDetailDTO getSingleDetail(Long stockTransId, Long prodOfferId, Long stateId) throws Exception {
        return client.getSingleDetail(stockTransId, prodOfferId, stateId);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockTransDetailRescueDTO;
import com.viettel.bccs.inventory.service.StockTransDetailRescueService;
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
@Service("WsStockTransDetailRescueServiceImpl")
public class WsStockTransDetailRescueServiceImpl implements StockTransDetailRescueService {

    public static final Logger logger = Logger.getLogger(WsStockTransDetailRescueServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransDetailRescueService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTransDetailRescueService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockTransDetailRescueDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockTransDetailRescueDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockTransDetailRescueDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockTransDetailRescueDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockTransDetailRescueDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<StockTransDetailRescueDTO> viewDetail(Long stockTransId) throws LogicException, Exception {
        return client.viewDetail(stockTransId);
    }

    @Override
    @WebMethod
    public StockTransDetailRescueDTO save(StockTransDetailRescueDTO stockTransDetailRescueDTO) throws Exception {
        return client.save(stockTransDetailRescueDTO);
    }

    @Override
    @WebMethod
    public List<StockTransDetailRescueDTO> getCountLstDetail(Long stockTransId) throws LogicException, Exception {
        return client.getCountLstDetail(stockTransId);
    }
}
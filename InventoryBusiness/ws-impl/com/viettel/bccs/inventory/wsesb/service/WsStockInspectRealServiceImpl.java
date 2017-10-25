package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockInspectRealDTO;
import com.viettel.bccs.inventory.service.StockInspectRealService;
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
@Service("WsStockInspectRealServiceImpl")
public class WsStockInspectRealServiceImpl implements StockInspectRealService {

    public static final Logger logger = Logger.getLogger(WsStockInspectRealServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockInspectRealService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockInspectRealService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @WebMethod
    @Override
    public StockInspectRealDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @WebMethod
    @Override
    public List<StockInspectRealDTO> findAll() throws Exception {
        return client.findAll();
    }

    @WebMethod
    @Override
    public List<StockInspectRealDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    @WebMethod
    public StockInspectRealDTO save(StockInspectRealDTO productSpecCharacterDTO) throws Exception {
        return client.save(productSpecCharacterDTO);
    }

    @WebMethod
    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @WebMethod
    @Override
    public BaseMessage update(StockInspectRealDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public BaseMessage delete(StockInspectRealDTO stockInspectRealDTO) throws LogicException, Exception {
        return client.delete(stockInspectRealDTO);
    }

    @Override
    @WebMethod
    public List<StockInspectRealDTO> getStockInspectReal(Long stockInspectId) throws LogicException, Exception {
        return client.getStockInspectReal(stockInspectId);
    }

    @Override
    @WebMethod
    public BaseMessage deleteStockInspect(Long stockInspectId) throws LogicException, Exception {
        return client.deleteStockInspect(stockInspectId);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockInspectSysDTO;
import com.viettel.bccs.inventory.service.StockInspectSysService;
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
@Service("WsStockInspectSysServiceImpl")
public class WsStockInspectSysServiceImpl implements StockInspectSysService {

    public static final Logger logger = Logger.getLogger(WsStockInspectSysServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockInspectSysService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockInspectSysService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @WebMethod
    @Override
    public StockInspectSysDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @WebMethod
    @Override
    public List<StockInspectSysDTO> findAll() throws Exception {
        return client.findAll();
    }

    @WebMethod
    @Override
    public List<StockInspectSysDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    @WebMethod
    public StockInspectSysDTO save(StockInspectSysDTO productSpecCharacterDTO) throws Exception {
        return client.save(productSpecCharacterDTO);
    }

    @WebMethod
    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @WebMethod
    @Override
    public BaseMessage update(StockInspectSysDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public BaseMessage delete(Long stockInspectId) throws Exception {
        return client.delete(stockInspectId);
    }

    @Override
    @WebMethod
    public StockInspectSysDTO findByStockInspectId(Long stockInspectId) throws Exception {
        return client.findByStockInspectId(stockInspectId);
    }

    @Override
    public BaseMessage deleteStockInspectSys(Long stockInspectId) throws Exception {
        return client.deleteStockInspectSys(stockInspectId);
    }
}
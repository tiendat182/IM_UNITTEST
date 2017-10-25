package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockTotalAuditDTO;
import com.viettel.bccs.inventory.service.StockTotalAuditService;
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
@Service("WsStockTotalAuditServiceImpl")
public class WsStockTotalAuditServiceImpl implements StockTotalAuditService {

    public static final Logger logger = Logger.getLogger(WsStockTotalAuditServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTotalAuditService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(StockTotalAuditService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public StockTotalAuditDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<StockTotalAuditDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockTotalAuditDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockTotalAuditDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockTotalAuditDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public StockTotalAuditDTO save(StockTotalAuditDTO stockTotalAuditDTO) throws Exception {
        return client.save(stockTotalAuditDTO);
    }
}
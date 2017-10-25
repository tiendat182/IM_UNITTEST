package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ExchangeDTO;
import com.viettel.bccs.inventory.service.ExchangeService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsExchangeServiceImpl")
public class WsExchangeServiceImpl implements ExchangeService {

    public static final Logger logger = Logger.getLogger(WsExchangeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ExchangeService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(ExchangeService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public ExchangeDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<ExchangeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ExchangeDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(ExchangeDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(ExchangeDTO dto) throws Exception  {
        return client.update(dto);
    }

}
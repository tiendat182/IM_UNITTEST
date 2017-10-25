package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.PstnIsdnExchangeDTO;
import com.viettel.bccs.inventory.service.PstnIsdnExchangeService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsPstnIsdnExchangeServiceImpl")
public class WsPstnIsdnExchangeServiceImpl implements PstnIsdnExchangeService {

    public static final Logger logger = Logger.getLogger(WsPstnIsdnExchangeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PstnIsdnExchangeService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(PstnIsdnExchangeService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public PstnIsdnExchangeDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<PstnIsdnExchangeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<PstnIsdnExchangeDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(PstnIsdnExchangeDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(PstnIsdnExchangeDTO dto) throws Exception  {
        return client.update(dto);
    }

}
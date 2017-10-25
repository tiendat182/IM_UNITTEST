package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.partner.dto.AccountAgentDTO;
import com.viettel.bccs.partner.service.AccountAgentService;
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
@Service("WsAccountAgentServiceImpl")
public class WsAccountAgentServiceImpl implements AccountAgentService {

    public static final Logger logger = Logger.getLogger(WsAccountAgentServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private AccountAgentService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(AccountAgentService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public AccountAgentDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<AccountAgentDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<AccountAgentDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(AccountAgentDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(AccountAgentDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<AccountAgentDTO> getAccountAgentFromOwnerId(Long ownerId, Long ownerType) throws LogicException, Exception {
        return client.getAccountAgentFromOwnerId(ownerId, ownerType);
    }
}
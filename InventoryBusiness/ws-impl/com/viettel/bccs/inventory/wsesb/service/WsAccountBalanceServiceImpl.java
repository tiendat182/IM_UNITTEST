package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.partner.dto.AccountBalanceDTO;
import com.viettel.bccs.partner.service.AccountBalanceService;
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
@Service("WsAccountBalanceServiceImpl")
public class WsAccountBalanceServiceImpl implements AccountBalanceService {

    public static final Logger logger = Logger.getLogger(WsAccountBalanceServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private AccountBalanceService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(AccountBalanceService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public AccountBalanceDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<AccountBalanceDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<AccountBalanceDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    @WebMethod
    public AccountBalanceDTO create(AccountBalanceDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    @WebMethod
    public AccountBalanceDTO update(AccountBalanceDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public AccountBalanceDTO getAccountBalanceBankplus(Long ownerType, Long ownerId) throws LogicException, Exception {
        return client.getAccountBalanceBankplus(ownerType, ownerId);
    }

    @Override
    @WebMethod
    public AccountBalanceDTO findLock(Long accountBalanceId) throws LogicException, Exception {
        return client.findLock(accountBalanceId);
    }
}
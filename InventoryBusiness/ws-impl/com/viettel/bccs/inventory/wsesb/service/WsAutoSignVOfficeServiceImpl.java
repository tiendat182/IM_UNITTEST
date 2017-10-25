package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.service.AutoSignVOfficeService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

@Service("WsAutoSignVOfficeServiceImpl")
public class WsAutoSignVOfficeServiceImpl implements AutoSignVOfficeService {

    public static final Logger logger = Logger.getLogger(WsAutoSignVOfficeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private AutoSignVOfficeService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(AutoSignVOfficeService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public boolean checkAccount(String userName, String passWord) throws LogicException, Exception {
        return client.checkAccount(userName, passWord);
    }

    @Override
    @WebMethod
    public String getSignStatus(String transCode) throws LogicException, Exception {
        return client.getSignStatus(transCode);
    }
}
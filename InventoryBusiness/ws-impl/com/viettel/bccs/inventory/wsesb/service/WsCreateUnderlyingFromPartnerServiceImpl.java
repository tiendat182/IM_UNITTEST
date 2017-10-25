package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.CreateUnderlyingFromPartnerService;
import com.viettel.bccs.inventory.service.PartnerContractDetailService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

@Service("WsCreateUnderlyingFromPartnerServiceImpl")
public class WsCreateUnderlyingFromPartnerServiceImpl implements CreateUnderlyingFromPartnerService {

    public static final Logger logger = Logger.getLogger(WsCreateUnderlyingFromPartnerServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private CreateUnderlyingFromPartnerService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(CreateUnderlyingFromPartnerService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public void executeStockTransForPartner(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        client.executeStockTransForPartner(importPartnerRequestDTO, requiredRoleMap);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.RevokeKitTransDTO;
import com.viettel.bccs.inventory.service.RevokeKitTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsRevokeKitTransServiceImpl")
public class WsRevokeKitTransServiceImpl implements RevokeKitTransService {

    public static final Logger logger = Logger.getLogger(WsRevokeKitTransServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private RevokeKitTransService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(RevokeKitTransService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public RevokeKitTransDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public RevokeKitTransDTO save(RevokeKitTransDTO dto) throws LogicException, Exception {
        return client.save(dto);
    }
}
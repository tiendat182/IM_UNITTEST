package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ReasonDTO;
import com.viettel.bccs.inventory.service.ReasonService;
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
@Service("WsReasonServiceImpl")
public class WsReasonServiceImpl implements ReasonService {

    public static final Logger logger = Logger.getLogger(WsReasonServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ReasonService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(ReasonService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public ReasonDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<ReasonDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<ReasonDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(ReasonDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(ReasonDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    public List<ReasonDTO> getLsReasonByType(String reasonType) throws LogicException, Exception {
        return client.getLsReasonByType(reasonType);
    }
    @Override
    public List<ReasonDTO> getLsReasonByCode(String reasonCode) throws LogicException, Exception {
        return client.getLsReasonByType(reasonCode);
    }

    @Override
    public ReasonDTO getReason(String reasonCode, String reasonType) throws LogicException, Exception {
        return client.getReason(reasonCode, reasonType);
    }
}
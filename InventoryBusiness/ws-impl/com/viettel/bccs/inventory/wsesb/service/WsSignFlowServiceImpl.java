package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.SignFlowDTO;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.SignFlowService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsSignFlowServiceImpl")
public class WsSignFlowServiceImpl implements SignFlowService {

    public static final Logger logger = Logger.getLogger(WsSignFlowServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private SignFlowService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(SignFlowService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public SignFlowDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<SignFlowDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<SignFlowDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(SignFlowDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(SignFlowDTO dto,List<SignFlowDetailDTO> list, String staffCode) throws Exception  {
        return client.update(dto, list, staffCode);
    }

    @Override
    @WebMethod
    public List<SignFlowDTO> getSignFlowByShop(Long shopId) throws Exception {
        return client.getSignFlowByShop(shopId);
    }
    @Override
    @WebMethod
    public List<SignFlowDTO> search(SignFlowDTO dto) throws Exception,LogicException {
        return client.search(dto);
    }

    @Override
    @WebMethod
    public SignFlowDTO createNewSignFlow(SignFlowDTO dto,List<SignFlowDetailDTO> list, String staffCode) throws Exception, LogicException {
        return client.createNewSignFlow(dto, list, staffCode);
    }

    @Override
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException{
        return client.delete( lstId, staffCode);
    }
}
package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.MtDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.MtService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsMtServiceImpl")
public class WsMtServiceImpl implements MtService {

    public static final Logger logger = Logger.getLogger(WsMtServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private MtService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(MtService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public MtDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<MtDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<MtDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(MtDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(MtDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public BaseMessage saveSms(String isdn, String content) throws Exception {
        return client.saveSms(isdn, content);
    }

    @Override
    public void delete(Long mtID) throws LogicException, Exception {
        client.delete(mtID);
    }
}
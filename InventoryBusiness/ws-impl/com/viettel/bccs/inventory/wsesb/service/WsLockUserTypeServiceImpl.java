package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.LockUserInfoMsgDTO;
import com.viettel.bccs.inventory.dto.LockUserTypeDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.LockUserTypeService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsLockUserTypeServiceImpl")
public class WsLockUserTypeServiceImpl implements LockUserTypeService {

    public static final Logger logger = Logger.getLogger(WsLockUserTypeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private LockUserTypeService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(LockUserTypeService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public LockUserTypeDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<LockUserTypeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<LockUserTypeDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(LockUserTypeDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(LockUserTypeDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    public List<LockUserTypeDTO> getAllUserLockType() throws Exception {
        return client.getAllUserLockType();
    }

    @Override
    public List<LockUserInfoMsgDTO> getLockUserInfo(Long staffId) throws Exception {
        return client.getLockUserInfo(staffId);
    }
}
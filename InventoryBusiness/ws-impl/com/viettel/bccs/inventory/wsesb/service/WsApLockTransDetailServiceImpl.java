package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ApLockTransDetailDTO;
import com.viettel.bccs.inventory.service.ApLockTransDetailService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsApLockTransDetailServiceImpl")
public class WsApLockTransDetailServiceImpl implements ApLockTransDetailService {

    public static final Logger logger = Logger.getLogger(WsApLockTransDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ApLockTransDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(ApLockTransDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public ApLockTransDetailDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<ApLockTransDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ApLockTransDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(ApLockTransDetailDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(ApLockTransDetailDTO dto) throws Exception  {
        return client.update(dto);
    }

}
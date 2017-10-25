package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ApTransInfoDTO;
import com.viettel.bccs.inventory.service.ApTransInfoService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsApTransInfoServiceImpl")
public class WsApTransInfoServiceImpl implements ApTransInfoService {

    public static final Logger logger = Logger.getLogger(WsApTransInfoServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ApTransInfoService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(ApTransInfoService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public ApTransInfoDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<ApTransInfoDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ApTransInfoDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(ApTransInfoDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(ApTransInfoDTO dto) throws Exception  {
        return client.update(dto);
    }

}
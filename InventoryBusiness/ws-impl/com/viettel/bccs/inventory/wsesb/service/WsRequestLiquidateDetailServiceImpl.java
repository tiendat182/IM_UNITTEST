package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.RequestLiquidateDTO;
import com.viettel.bccs.inventory.dto.RequestLiquidateDetailDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.RequestLiquidateDetailService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsRequestLiquidateDetailServiceImpl")
public class WsRequestLiquidateDetailServiceImpl implements RequestLiquidateDetailService {

    public static final Logger logger = Logger.getLogger(WsRequestLiquidateDetailServiceImpl.class);
    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private RequestLiquidateDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(RequestLiquidateDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public RequestLiquidateDetailDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<RequestLiquidateDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<RequestLiquidateDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public RequestLiquidateDetailDTO save(RequestLiquidateDetailDTO dto) throws Exception  {
        return client.save(dto);
    }

    @Override
    public List<RequestLiquidateDetailDTO> getLstRequestLiquidateDetailDTO(Long requestLiquidateId) throws Exception {
        return client.getLstRequestLiquidateDetailDTO(requestLiquidateId);
    }

}
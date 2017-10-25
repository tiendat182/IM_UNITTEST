package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.RequestLiquidateDetailDTO;
import com.viettel.bccs.inventory.dto.RequestLiquidateSerialDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.RequestLiquidateSerialService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsRequestLiquidateSerialServiceImpl")
public class WsRequestLiquidateSerialServiceImpl implements RequestLiquidateSerialService {

    public static final Logger logger = Logger.getLogger(WsRequestLiquidateSerialServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private RequestLiquidateSerialService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(RequestLiquidateSerialService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public RequestLiquidateSerialDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<RequestLiquidateSerialDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<RequestLiquidateSerialDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public RequestLiquidateSerialDTO save(RequestLiquidateSerialDTO dto) throws Exception  {
        return client.save(dto);
    }

    @Override
    public List<RequestLiquidateSerialDTO> getLstRequestLiquidateSerialDTO(Long requestLiquidateDetailId) throws Exception {
        return client.getLstRequestLiquidateSerialDTO(requestLiquidateDetailId);
    }


}
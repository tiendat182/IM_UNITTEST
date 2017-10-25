package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.NumberActionDTO;
import com.viettel.bccs.inventory.dto.SearchNumberRangeDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.NumberActionService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsNumberActionServiceImpl")
public class WsNumberActionServiceImpl implements NumberActionService {

    public static final Logger logger = Logger.getLogger(WsNumberActionServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private NumberActionService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(NumberActionService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public NumberActionDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<NumberActionDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<NumberActionDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public NumberActionDTO create(NumberActionDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(NumberActionDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    public Boolean checkOverlap(Long fromIsdn,Long toIsdn,String telecomServiceId) throws Exception {
        return client.checkOverlap(fromIsdn,toIsdn,telecomServiceId);
    }

    @Override
    @WebMethod
    public List<NumberActionDTO> search(SearchNumberRangeDTO searchDto) throws Exception {
        return client.search(searchDto);
    }
}
package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.ChangeModelTransSerialDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.ChangeModelTransSerialService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsChangeModelTransSerialServiceImpl")
public class WsChangeModelTransSerialServiceImpl implements ChangeModelTransSerialService {

    public static final Logger logger = Logger.getLogger(WsChangeModelTransSerialServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ChangeModelTransSerialService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(ChangeModelTransSerialService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public ChangeModelTransSerialDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<ChangeModelTransSerialDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ChangeModelTransSerialDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public ChangeModelTransSerialDTO save(ChangeModelTransSerialDTO dto) throws Exception  {
        return client.save(dto);
    }

    @Override
    public BaseMessage update(ChangeModelTransSerialDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<ChangeModelTransSerialDTO> viewDetailSerial(Long changeModelTransDetailId) throws Exception {
        return client.viewDetailSerial(changeModelTransDetailId);
    }
}
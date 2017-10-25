package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.bccs.inventory.service.AreaService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@Service("WsAreaServiceImpl")
public class WsAreaServiceImpl implements AreaService {

    public static final Logger logger = Logger.getLogger(WsAreaServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private AreaService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(AreaService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public AreaDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<AreaDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<AreaDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(AreaDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(AreaDTO dto) throws Exception  {
        return client.update(dto);
    }


    @Override
    @WebMethod
    public List<AreaDTO> getAllProvince() throws Exception {
        return client.getAllProvince();
    }

    @Override
    @WebMethod
    public AreaDTO findByCode(String areaCode) throws Exception {
        return client.findByCode(areaCode);
    }

    @Override
    @WebMethod
    public List<AreaDTO> search(AreaDTO searchDto) throws Exception {
        return client.search(searchDto);
    }
}
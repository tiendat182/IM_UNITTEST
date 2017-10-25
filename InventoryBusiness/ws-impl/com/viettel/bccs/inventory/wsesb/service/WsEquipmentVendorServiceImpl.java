package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.EquipmentVendorDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.EquipmentVendorService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsEquipmentVendorServiceImpl")
public class WsEquipmentVendorServiceImpl implements EquipmentVendorService {

    public static final Logger logger = Logger.getLogger(WsEquipmentVendorServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private EquipmentVendorService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(EquipmentVendorService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public EquipmentVendorDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<EquipmentVendorDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<EquipmentVendorDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(EquipmentVendorDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(EquipmentVendorDTO dto) throws Exception  {
        return client.update(dto);
    }

}
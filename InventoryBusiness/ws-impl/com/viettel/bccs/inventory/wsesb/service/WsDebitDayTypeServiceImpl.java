package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.DebitDayTypeDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.DebitDayTypeService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsDebitDayTypeServiceImpl")
public class WsDebitDayTypeServiceImpl implements DebitDayTypeService {

    public static final Logger logger = Logger.getLogger(WsDebitDayTypeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DebitDayTypeService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(DebitDayTypeService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public DebitDayTypeDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<DebitDayTypeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<DebitDayTypeDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(DebitDayTypeDTO dto, StaffDTO staffDTO) throws Exception  {
        return client.create(dto, staffDTO);
    }

    @Override
    public BaseMessage update(DebitDayTypeDTO dto,StaffDTO staffDTO) throws Exception  {
        return client.update(dto, staffDTO);
    }

    @Override
    public BaseMessage delete(List<DebitDayTypeDTO> lstDebitDayTypeDTOs, StaffDTO staffDTO) throws Exception  {
        return client.delete(lstDebitDayTypeDTOs, staffDTO);
    }

    @Override
    public List<DebitDayTypeDTO> searchDebitDayType(DebitDayTypeDTO dto) throws Exception{
        return client.searchDebitDayType(dto);
    }

    @Override
    public byte[] getAttachFileContent(Long requestId) throws Exception {
        return client.getAttachFileContent(requestId);
    }
}
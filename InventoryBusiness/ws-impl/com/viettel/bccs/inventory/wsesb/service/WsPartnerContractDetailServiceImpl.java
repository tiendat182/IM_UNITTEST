package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.PartnerContractDetailDTO;
import com.viettel.bccs.inventory.service.PartnerContractDetailService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Service("WsPartnerContractDetailServiceImpl")
public class WsPartnerContractDetailServiceImpl implements PartnerContractDetailService {

    public static final Logger logger = Logger.getLogger(WsPartnerContractDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PartnerContractDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(PartnerContractDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public PartnerContractDetailDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<PartnerContractDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<PartnerContractDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(PartnerContractDetailDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(PartnerContractDetailDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    public PartnerContractDetailDTO save(PartnerContractDetailDTO partnerContractDetailDTO) throws Exception {
        return client.save(partnerContractDetailDTO);
    }
}
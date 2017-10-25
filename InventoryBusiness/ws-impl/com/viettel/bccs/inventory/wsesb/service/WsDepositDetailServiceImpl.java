package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.DepositDetailDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.bccs.inventory.service.DepositDetailService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsDepositDetailServiceImpl")
public class WsDepositDetailServiceImpl implements DepositDetailService {

    public static final Logger logger = Logger.getLogger(WsDepositDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DepositDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(DepositDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public DepositDetailDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<DepositDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<DepositDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public DepositDetailDTO create(DepositDetailDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(DepositDetailDTO dto) throws Exception  {
        return client.update(dto);
    }

}
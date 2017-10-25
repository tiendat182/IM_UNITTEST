package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import com.viettel.bccs.inventory.service.SignFlowDetailService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsSignFlowDetailServiceImpl")
public class WsSignFlowDetailServiceImpl implements SignFlowDetailService {

    public static final Logger logger = Logger.getLogger(WsSignFlowDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private SignFlowDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(SignFlowDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    @WebMethod
    public SignFlowDetailDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<SignFlowDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<SignFlowDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public SignFlowDetailDTO create(SignFlowDetailDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    @WebMethod
    public SignFlowDetailDTO update(SignFlowDetailDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<SignFlowDetailDTO> findBySignFlowId(Long signFlowId) throws Exception {
        return client.findBySignFlowId(signFlowId);
    }
    @Override
    @WebMethod
    public List<SignFlowDetailDTO> findByVofficeRoleId(Long vofficeRoleId) throws Exception {
        return client.findByVofficeRoleId(vofficeRoleId);
    }
}

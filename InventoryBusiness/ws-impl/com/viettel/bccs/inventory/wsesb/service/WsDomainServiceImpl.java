package com.viettel.bccs.inventory.wsesb.service;
import com.viettel.bccs.inventory.dto.DomainDTO;
import com.viettel.bccs.inventory.service.DomainService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;
@Service("WsDomainServiceImpl")
public class WsDomainServiceImpl implements DomainService {

    public static final Logger logger = Logger.getLogger(WsDomainServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DomainService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(DomainService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @WebMethod
    @Override
    public DomainDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @WebMethod
    @Override
    public List<DomainDTO> findAll() throws Exception {
        return client.findAll();
    }

    @WebMethod
    @Override
    public List<DomainDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @WebMethod
    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @WebMethod
    @Override
    public BaseMessage create(DomainDTO dto) throws Exception  {
        return client.create(dto);
    }

    @WebMethod
    @Override
    public BaseMessage update(DomainDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public DomainDTO findByDomain(String domain) throws Exception, LogicException {
        return client.findByDomain(domain);
    }

    @Override
    @WebMethod
    public List<DomainDTO> findAllAction() throws Exception, LogicException {
        return client.findAllAction();
    }

    @Override
    public DomainDTO findByDomainId(Long domainId) throws Exception, LogicException {
        return client.findByDomainId(domainId);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.VofficeRoleDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.VofficeRoleService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsVofficeRoleServiceImpl")
public class WsVofficeRoleServiceImpl implements VofficeRoleService {

    public static final Logger logger = Logger.getLogger(WsVofficeRoleServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private VofficeRoleService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(VofficeRoleService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public VofficeRoleDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<VofficeRoleDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<VofficeRoleDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(VofficeRoleDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException {
        return client.delete(lstId, staffCode);
    }

    @Override
    @WebMethod
    public List<VofficeRoleDTO> search(VofficeRoleDTO dto, boolean flag) throws Exception,LogicException {
        return client.search(dto, flag);
    }

    @Override
    @WebMethod
    public VofficeRoleDTO createNewVofficeRole(VofficeRoleDTO vofficeRoleDTO, String staffCode) throws Exception, LogicException {
        return client.createNewVofficeRole(vofficeRoleDTO, staffCode);
    }
    @Override
    @WebMethod
    public BaseMessage update(VofficeRoleDTO vofficeRoleDTO, String staffCode) throws Exception, LogicException {
        return client.update(vofficeRoleDTO, staffCode);
    }


}
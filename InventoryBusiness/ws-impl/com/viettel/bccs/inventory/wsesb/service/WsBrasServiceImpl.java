package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.BrasDTO;
import com.viettel.bccs.inventory.service.BrasService;
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

@Service("WsBrasServiceImpl")
public class WsBrasServiceImpl implements BrasService {

    public static final Logger logger = Logger.getLogger(WsBrasServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private BrasService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(BrasService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public BrasDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<BrasDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<BrasDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(BrasDTO brasDTO) throws Exception {
        return client.create(brasDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(BrasDTO brasDTO, String staffCode) throws Exception, LogicException {
        return client.update(brasDTO, staffCode);
    }

    @Override
    @WebMethod
    public BrasDTO createNewBras(BrasDTO brasDTO, String staffCode) throws Exception, LogicException {
        return client.createNewBras(brasDTO, staffCode);
    }

    @Override
    @WebMethod
    public BaseMessage delete(List<Long> lstId) throws Exception, LogicException {
        return client.delete(lstId);
    }

    @Override
    @WebMethod
    public List<BrasDTO> search(BrasDTO brasDTO) throws Exception, LogicException {
        return client.search(brasDTO);
    }

    @Override
    @WebMethod
    public BrasDTO findByBrasIp(String ipBras) throws Exception, LogicException {
        return client.findByBrasIp(ipBras);
    }

    @Override
    public List<BrasDTO> findAllAction() throws Exception {
        return client.findAllAction();
    }

    @Override
    @WebMethod
    public BrasDTO findByBrasId(Long brasId) throws Exception, LogicException {
        return client.findByBrasId(brasId);
    }

    @Override
    public BrasDTO createBras(BrasDTO brasDTO, String staffCode) throws Exception, LogicException {
        return client.createBras(brasDTO, staffCode);
    }
}
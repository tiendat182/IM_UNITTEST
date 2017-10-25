package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.BrasIppoolDTO;
import com.viettel.bccs.inventory.service.BrasIppoolService;
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

@Service("WsBrasIppoolServiceImpl")
public class WsBrasIppoolServiceImpl implements BrasIppoolService {

    public static final Logger logger = Logger.getLogger(WsBrasIppoolServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private BrasIppoolService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(BrasIppoolService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public BrasIppoolDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<BrasIppoolDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<BrasIppoolDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(BrasIppoolDTO brasIppoolDTO) throws Exception, LogicException {
        return client.create(brasIppoolDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(BrasIppoolDTO brasIppoolDTO, String staffCode) throws Exception, LogicException {
        return client.update(brasIppoolDTO, staffCode);
    }

    @Override
    @WebMethod
    public BaseMessage updateStatus(BrasIppoolDTO brasIppoolDTO, String staffCode) throws Exception, LogicException {
        return client.updateStatus(brasIppoolDTO, staffCode);
    }

    @Override
    @WebMethod
    public BaseMessage delete(List<Long> lstId) throws Exception, LogicException {
        return client.delete(lstId);
    }

    @Override
    @WebMethod
    public List<BrasIppoolDTO> search(BrasIppoolDTO brasIppoolDTO) throws Exception, LogicException {
        return client.search(brasIppoolDTO);
    }

    @Override
    @WebMethod
    public List<BrasIppoolDTO> searchByIp(BrasIppoolDTO brasIppoolDTO) throws Exception, LogicException {
        return client.searchByIp(brasIppoolDTO);
    }

    @WebMethod
    public boolean checkDulicateBrasIpPool(String ip) throws Exception, LogicException {
        return client.checkDulicateBrasIpPool(ip);
    }

    @Override
    @WebMethod
    public BrasIppoolDTO createNewBrasIppool(BrasIppoolDTO brasIppoolDTO, String staffCode) throws Exception, LogicException {
        return client.createNewBrasIppool(brasIppoolDTO, staffCode);
    }

    @WebMethod
    public boolean checkBrasIdExist(Long brasId) throws Exception, LogicException {
        return client.checkBrasIdExist(brasId);
    }

    @Override
    @WebMethod
    public BaseMessage lockIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException {
        return client.lockIppool(ippool, shopId, staffId);
    }

    @Override
    @WebMethod
    public BaseMessage unlockIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException {
        return client.unlockIppool(ippool, shopId, staffId);
    }

    @Override
    @WebMethod
    public List<String> getListStaticIPProvince(String domain, String province, Long ipType) throws Exception, LogicException {
        return client.getListStaticIPProvince(domain, province, ipType);
    }

    @Override
    @WebMethod
    public List<String> getListStaticIpSpecialProvince(String domain, String province, Long ipType, Long specialType) throws Exception, LogicException {
        return client.getListStaticIpSpecialProvince(domain, province, ipType, specialType);
    }

    @Override
    @WebMethod
    public BaseMessage registerIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException {
        return client.registerIppool(ippool, shopId, staffId);
    }

    @Override
    @WebMethod
    public BaseMessage releaseIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException {
        return client.releaseIppool(ippool, shopId, staffId);
    }

    @Override
    @WebMethod
    public BrasIppoolDTO getBrasViewIpFromCM(String ippool) throws Exception, LogicException {
        return client.getBrasViewIpFromCM(ippool);
    }
}
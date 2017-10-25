package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ApnIpDTO;
import com.viettel.bccs.inventory.service.ApnIpService;
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

@Service("WsApnIpServiceImpl")
public class WsApnIpServiceImpl implements ApnIpService {

    public static final Logger logger = Logger.getLogger(WsApnIpServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ApnIpService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ApnIpService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ApnIpDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ApnIpDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ApnIpDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(ApnIpDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(ApnIpDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public BaseMessage insertListAPNIP(List<ApnIpDTO> listApnIp) throws Exception {
        return client.insertListAPNIP(listApnIp);
    }

    @Override
    @WebMethod
    public List<ApnIpDTO> searchApnIp(ApnIpDTO apnIp) throws Exception {
        return client.searchApnIp(apnIp);
    }

    @Override
    @WebMethod
    public List<ApnIpDTO> checkDuplicateApnIp(List<ApnIpDTO> listApn) {
        return client.checkDuplicateApnIp(listApn);
    }

    @Override
    @WebMethod
    public ApnIpDTO findApnIpById(Long apnIpID) throws Exception {
        return client.findApnIpById(apnIpID);
    }
}
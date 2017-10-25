package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.DebitLevelDTO;
import com.viettel.bccs.inventory.service.DebitLevelService;
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

@Service("WsDebitLevelServiceImpl")
public class WsDebitLevelServiceImpl implements DebitLevelService {

    public static final Logger logger = Logger.getLogger(WsDebitLevelServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DebitLevelService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(DebitLevelService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public DebitLevelDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<DebitLevelDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<DebitLevelDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(DebitLevelDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(DebitLevelDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    public List<DebitLevelDTO> searchDebitlevel(DebitLevelDTO dto) throws Exception {
        return client.searchDebitlevel(dto);
    }

    @Override
    public List<DebitLevelDTO> getLstDebitByLevel(Long level) throws Exception {
        return client.getLstDebitByLevel(level);
    }
}
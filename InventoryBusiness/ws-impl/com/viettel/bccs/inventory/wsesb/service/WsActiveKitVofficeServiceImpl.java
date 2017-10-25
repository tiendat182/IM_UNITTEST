package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ActiveKitVofficeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.ActiveKitVofficeService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsActiveKitVofficeServiceImpl")
public class WsActiveKitVofficeServiceImpl implements ActiveKitVofficeService {

    public static final Logger logger = Logger.getLogger(WsActiveKitVofficeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ActiveKitVofficeService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ActiveKitVofficeService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ActiveKitVofficeDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ActiveKitVofficeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ActiveKitVofficeDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public ActiveKitVofficeDTO save(ActiveKitVofficeDTO dto) throws Exception {
        return client.save(dto);
    }

    @Override
    public BaseMessage create(ActiveKitVofficeDTO dto) throws Exception {
        return client.create(dto);
    }
    
    @Override
    public BaseMessage update(ActiveKitVofficeDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    public ActiveKitVofficeDTO findByChangeModelTransId(Long changeModelTransId) throws Exception {
        return client.findByChangeModelTransId(changeModelTransId);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.ChangeModelTransDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.ChangeModelTransDetailService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsChangeModelTransDetailServiceImpl")
public class WsChangeModelTransDetailServiceImpl implements ChangeModelTransDetailService {

    public static final Logger logger = Logger.getLogger(WsChangeModelTransDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ChangeModelTransDetailService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ChangeModelTransDetailService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ChangeModelTransDetailDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ChangeModelTransDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ChangeModelTransDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public ChangeModelTransDetailDTO save(ChangeModelTransDetailDTO dto) throws Exception {
        return client.save(dto);
    }

    @Override
    public BaseMessage update(ChangeModelTransDetailDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<ChangeModelTransDetailDTO> viewDetail(Long changeModelTransId) throws Exception {
        return client.viewDetail(changeModelTransId);
    }

    @Override
    @WebMethod
    public Long getNewProdOfferId(Long oldProdOfferId, Long changeModelTransId, Long stateId) throws Exception {
        return client.getNewProdOfferId(oldProdOfferId, changeModelTransId, stateId);
    }

    @Override
    @WebMethod
    public List<ChangeModelTransDetailDTO> getLstDetailByChangeModelTransId(Long changeModelTransId) throws Exception {
        return client.getLstDetailByChangeModelTransId(changeModelTransId);
    }
}
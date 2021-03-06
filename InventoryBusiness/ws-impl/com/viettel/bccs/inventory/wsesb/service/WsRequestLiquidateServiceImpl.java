package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.RequestLiquidateDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.RequestLiquidateService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsRequestLiquidateServiceImpl")
public class WsRequestLiquidateServiceImpl implements RequestLiquidateService {

    public static final Logger logger = Logger.getLogger(WsRequestLiquidateServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private RequestLiquidateService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(RequestLiquidateService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public RequestLiquidateDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<RequestLiquidateDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<RequestLiquidateDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public RequestLiquidateDTO save(RequestLiquidateDTO dto) throws Exception {
        return client.save(dto);
    }

    @Override
    public BaseMessage doSaveLiquidate(RequestLiquidateDTO dto) throws LogicException, Exception {
        return client.doSaveLiquidate(dto);
    }

    public List<RequestLiquidateDTO> getLstRequestLiquidateDTO(RequestLiquidateDTO requestLiquidateDTO) throws LogicException,Exception {
        return client.getLstRequestLiquidateDTO(requestLiquidateDTO);
    }

    @Override
    public byte[] getAttachFileContent(Long requestLiquidateID) throws LogicException, Exception {
        return client.getAttachFileContent(requestLiquidateID);
    }

    @Override
    public String getRequestCode() throws LogicException,Exception {
        return client.getRequestCode();
    }

    @Override
    public void updateRequest(RequestLiquidateDTO requestLiquidateDTO) throws LogicException,Exception {
        client.updateRequest(requestLiquidateDTO);
    }
}
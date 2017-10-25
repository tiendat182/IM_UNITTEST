package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.ActionLogDTO;
import com.viettel.bccs.inventory.dto.ActionLogDetailDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.ActionLogService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Service("WsActionLogServiceImpl")
public class WsActionLogServiceImpl implements ActionLogService {

    public static final Logger logger = Logger.getLogger(WsActionLogServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ActionLogService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ActionLogService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ActionLogDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ActionLogDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ActionLogDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(ActionLogDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(ActionLogDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ActionLogDTO saveForProcessStock(ActionLogDTO actionLogDTO, List<ActionLogDetailDTO> lstDetail) throws Exception {
        return client.saveForProcessStock(actionLogDTO, lstDetail);
    }
}
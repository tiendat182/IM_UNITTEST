package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.DepositDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.DepositService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsDepositServiceImpl")
public class WsDepositServiceImpl implements DepositService {

    public static final Logger logger = Logger.getLogger(WsDepositServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DepositService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(DepositService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public DepositDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<DepositDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<DepositDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public DepositDTO create(DepositDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public DepositDTO update(Long depositId, String status) throws Exception {
        return client.update(depositId, status);
    }

}
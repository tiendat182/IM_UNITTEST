package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.InvoiceActionLogDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.InvoiceActionLogService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsInvoiceActionLogServiceImpl")
public class WsInvoiceActionLogServiceImpl implements InvoiceActionLogService {

    public static final Logger logger = Logger.getLogger(WsInvoiceActionLogServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private InvoiceActionLogService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(InvoiceActionLogService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public InvoiceActionLogDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<InvoiceActionLogDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<InvoiceActionLogDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(InvoiceActionLogDTO dto) throws Exception, LogicException {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(InvoiceActionLogDTO dto) throws Exception {
        return client.update(dto);
    }

}
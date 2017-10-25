package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.InvoiceTemplateLogDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.InvoiceTemplateLogService;

import java.util.Date;
import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsInvoiceTemplateLogServiceImpl")
public class WsInvoiceTemplateLogServiceImpl implements InvoiceTemplateLogService {

    public static final Logger logger = Logger.getLogger(WsInvoiceTemplateLogServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private InvoiceTemplateLogService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(InvoiceTemplateLogService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public InvoiceTemplateLogDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<InvoiceTemplateLogDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<InvoiceTemplateLogDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(InvoiceTemplateLogDTO dto, String staffCode, Date createDate) throws Exception, LogicException {
        return client.create(dto, staffCode, createDate);
    }

    @Override
    public BaseMessage update(InvoiceTemplateLogDTO dto) throws Exception {
        return client.update(dto);
    }

}
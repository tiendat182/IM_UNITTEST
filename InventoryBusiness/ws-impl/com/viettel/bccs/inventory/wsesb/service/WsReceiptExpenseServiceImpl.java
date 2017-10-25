package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.ReceiptExpenseDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.ReceiptExpenseService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsReceiptExpenseServiceImpl")
public class WsReceiptExpenseServiceImpl implements ReceiptExpenseService {

    public static final Logger logger = Logger.getLogger(WsReceiptExpenseServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ReceiptExpenseService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ReceiptExpenseService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ReceiptExpenseDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ReceiptExpenseDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ReceiptExpenseDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public ReceiptExpenseDTO create(ReceiptExpenseDTO dto, String staffCode) throws Exception, LogicException {
        return client.create(dto, staffCode);
    }

    @Override
    public ReceiptExpenseDTO update(ReceiptExpenseDTO dto, String status) throws Exception {
        return client.update(dto, status);
    }

    @Override
    @WebMethod
    public ReceiptExpenseDTO findByStockTransIdAndType(Long stockTransId, String type) throws Exception, LogicException {
        return client.findByStockTransIdAndType(stockTransId, type);
    }

    @Override
    @WebMethod
    public String generateReceiptNo(String shopCode) throws Exception, LogicException {
        return client.generateReceiptNo(shopCode);
    }
}
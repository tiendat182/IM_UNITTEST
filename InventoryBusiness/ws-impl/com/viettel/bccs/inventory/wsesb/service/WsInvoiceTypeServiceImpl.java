package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.InvoiceTypeService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsInvoiceTypeServiceImpl")
public class WsInvoiceTypeServiceImpl implements InvoiceTypeService {

    public static final Logger logger = Logger.getLogger(WsInvoiceTypeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private InvoiceTypeService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(InvoiceTypeService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public InvoiceTypeDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<InvoiceTypeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<InvoiceTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(InvoiceTypeDTO invoiceTypeDTO) throws Exception {
        return client.create(invoiceTypeDTO);
    }

    @Override
    @WebMethod
    public InvoiceTypeDTO createNewInvoiceType(InvoiceTypeDTO invoiceTypeDTO, String staffCode) throws Exception, LogicException {
        return client.createNewInvoiceType(invoiceTypeDTO, staffCode);
    }

    @Override
    @WebMethod
    public List<InvoiceTypeDTO> getInvoiceType(Long type) throws Exception,LogicException {
        return client.getInvoiceType(type);
    }

    @Override
    @WebMethod
    public BaseMessage update(InvoiceTypeDTO invoiceTypeDTO, String staffCode) throws Exception, LogicException {
        return client.update(invoiceTypeDTO, staffCode);
    }

    @Override
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException {
        return client.delete(lstId, staffCode);
    }

    @Override
    @WebMethod
    public List<InvoiceTypeDTO> search(InvoiceTypeDTO invoiceTypeDTO) throws Exception,LogicException {
        return client.search(invoiceTypeDTO);
    }
}
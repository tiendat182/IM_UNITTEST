package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.InvoiceSerialDTO;
import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.InvoiceSerialService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsInvoiceSerialServiceImpl")
public class WsInvoiceSerialServiceImpl implements InvoiceSerialService {

    public static final Logger logger = Logger.getLogger(WsInvoiceSerialServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private InvoiceSerialService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(InvoiceSerialService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public InvoiceSerialDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<InvoiceSerialDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<InvoiceSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(InvoiceSerialDTO invoiceSerialDTO) throws Exception {
        return client.create(invoiceSerialDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(InvoiceSerialDTO invoiceSerialDTO, String staffCode) throws Exception, LogicException {
        return client.update(invoiceSerialDTO, staffCode);
    }

    @Override
    @WebMethod
    public List<InvoiceSerialDTO> createNewInvoiceSerial(List<InvoiceSerialDTO> lst, String staffCode) throws Exception, LogicException {
        return client.createNewInvoiceSerial(lst, staffCode);
    }

    @Override
    public List<InvoiceSerialDTO> getAllSerialByInvoiceType(Long shopId, Long invoiceTypeId) throws Exception, LogicException {
        return client.getAllSerialByInvoiceType(shopId, invoiceTypeId);
    }

    @Override
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException {
        return client.delete(lstId, staffCode);
    }

    @Override
    @WebMethod
    public List<InvoiceSerialDTO> searchInvoiceSerial(InvoiceSerialDTO invoiceSerialDTO) throws Exception, LogicException {
        return client.searchInvoiceSerial(invoiceSerialDTO);
    }

    @Override
    @WebMethod
    public List<InvoiceSerialDTO> getInvoiceSerialFromInvoiceType(Long invoiceTypeId) throws Exception, LogicException {
        return client.getInvoiceSerialFromInvoiceType(invoiceTypeId);
    }
}
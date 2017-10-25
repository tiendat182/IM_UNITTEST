package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.InvoiceListDTO;
import com.viettel.bccs.inventory.dto.InvoiceSerialDTO;
import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.InvoiceListService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsInvoiceListServiceImpl")
public class WsInvoiceListServiceImpl implements InvoiceListService {

    public static final Logger logger = Logger.getLogger(WsInvoiceListServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private InvoiceListService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(InvoiceListService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public InvoiceListDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<InvoiceListDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<InvoiceListDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(InvoiceListDTO invoiceListDTO) throws Exception {
        return client.create(invoiceListDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(InvoiceListDTO invoiceListDTO, String staffCode) throws Exception, LogicException {
        return client.update(invoiceListDTO, staffCode);
    }

    @Override
    @WebMethod
    public InvoiceListDTO createNewInvoiceList(InvoiceListDTO invoiceListDTO, String staffCode) throws Exception, LogicException {
        return client.createNewInvoiceList(invoiceListDTO, staffCode);
    }

    @Override
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException {
        return client.delete(lstId, staffCode);
    }

    @Override
    @WebMethod
    public List<InvoiceSerialDTO> getAllSerialByInvoiceType(Long shopId, Long invoiceTypeId) throws Exception, LogicException {
        return client.getAllSerialByInvoiceType(shopId, invoiceTypeId);
    }

    @Override
    @WebMethod
    public List<InvoiceTypeDTO> getAllInvoiceTypeByType(Long type) throws Exception, LogicException {
        return client.getAllInvoiceTypeByType(type);
    }

    @Override
    @WebMethod
    public List<InvoiceListDTO> getAllInvoiceListByShopId(Long shopId) throws Exception, LogicException {
        return client.getAllInvoiceListByShopId(shopId);
    }

    @Override
    @WebMethod
    public List<InvoiceListDTO> getAllInvoiceListBySerialCode(String serialCode) throws Exception, LogicException {
        return client.getAllInvoiceListBySerialCode(serialCode);
    }

    @Override
    @WebMethod
    public List<InvoiceListDTO> getAllInvoiceListByInvoiceListId(Long invoiceListId) throws Exception, LogicException {
        return client.getAllInvoiceListByInvoiceListId(invoiceListId);
    }

    @Override
    @WebMethod
    public BaseMessage updateInvoiceListByInvoiceListId(Long invoiceListId) throws Exception, LogicException {
        return client.updateInvoiceListByInvoiceListId(invoiceListId);
    }

    @Override
    @WebMethod
    public List<InvoiceListDTO> searchInvoiceList(InvoiceListDTO invoiceListDTO) throws Exception, LogicException {
        return client.searchInvoiceList(invoiceListDTO);
    }

    @Override
    @WebMethod
    public List<InvoiceListDTO> getInvoiceListFromSerial(Long invoiceSerialId) throws Exception, LogicException {
        return client.getInvoiceListFromSerial(invoiceSerialId);
    }
}
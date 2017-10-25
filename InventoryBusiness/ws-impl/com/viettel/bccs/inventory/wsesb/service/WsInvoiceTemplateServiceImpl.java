package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.InvoiceTemplateService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsInvoiceTemplateServiceImpl")
public class WsInvoiceTemplateServiceImpl implements InvoiceTemplateService {

    public static final Logger logger = Logger.getLogger(WsInvoiceTemplateServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private InvoiceTemplateService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(InvoiceTemplateService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public InvoiceTemplateDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<InvoiceTemplateDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<InvoiceTemplateDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(InvoiceTemplateDTO invoiceTemplateDTO) throws Exception {
        return client.create(invoiceTemplateDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(InvoiceTemplateDTO invoiceTemplateDTO, String staffCode) throws Exception, LogicException {
        return client.update(invoiceTemplateDTO, staffCode);
    }

    @Override
    @WebMethod
    public InvoiceTemplateDTO createNewInvoiceTemplate(InvoiceTemplateDTO invoiceTemplateDTO, String staffCode, String typeAddNew) throws Exception, LogicException {
        return client.createNewInvoiceTemplate(invoiceTemplateDTO, staffCode, typeAddNew);
    }

    @Override
    @WebMethod
    public List<InvoiceTemplateDTO> search(InvoiceTemplateDTO invoiceTemplateDTO) throws Exception, LogicException {
        return client.search(invoiceTemplateDTO);
    }

    @Override
    @WebMethod
    public BaseMessage deliveryInvoiceTempalte(Long invoiceDeliveryId, List<InvoiceTemplateDTO> lstInvoiceReceive, String staffCode) throws Exception, LogicException {
        return client.deliveryInvoiceTempalte(invoiceDeliveryId, lstInvoiceReceive, staffCode);
    }

    @Override
    @WebMethod
    public BaseMessage retrieveInvoiceTempalte(Long invoiceDeliveryId, List<InvoiceTemplateDTO> lstInvoiceReceive, String staffCode) throws Exception, LogicException {
        return client.retrieveInvoiceTempalte(invoiceDeliveryId, lstInvoiceReceive, staffCode);
    }

    @Override
    @WebMethod
    public List<InvoiceTemplateDTO> getAllReceiveInvoiceTemplateByShopId(Long shopId) throws Exception, LogicException {
        return client.getAllReceiveInvoiceTemplateByShopId(shopId);
    }

    @Override
    @WebMethod
    public List<InvoiceTemplateDTO> getInvoiceTemplateList(Long shopId) throws Exception, LogicException {
        return client.getInvoiceTemplateList(shopId);
    }

    @Override
    @WebMethod
    public List<InvoiceTemplateDTO> getInvoiceTemplateExsitByOwnerType(Long ownerId, Long invoiceTemplateTypeId, String ownerType) throws Exception, LogicException {
        return client.getInvoiceTemplateExsitByOwnerType(ownerId, invoiceTemplateTypeId, ownerType);
    }

}
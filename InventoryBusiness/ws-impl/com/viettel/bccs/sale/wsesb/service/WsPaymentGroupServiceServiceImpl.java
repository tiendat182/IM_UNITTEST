package com.viettel.bccs.sale.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.sale.dto.PaymentGroupServiceDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;


import com.viettel.bccs.sale.service.PaymentGroupServiceService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsPaymentGroupServiceServiceImpl")
public class WsPaymentGroupServiceServiceImpl implements PaymentGroupServiceService {

    public static final Logger logger = Logger.getLogger(WsPaymentGroupServiceServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PaymentGroupServiceService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(PaymentGroupServiceService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public PaymentGroupServiceDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<PaymentGroupServiceDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<PaymentGroupServiceDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(PaymentGroupServiceDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(PaymentGroupServiceDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<PaymentGroupServiceDTO> getLstGroupService() throws Exception {
        return client.getLstGroupService();
    }

    @Override
    public PaymentGroupServiceDTO getPaymentGroupServiceByName(String name) throws Exception {
        return client.getPaymentGroupServiceByName(name);
    }
}
package com.viettel.bccs.sale.wsesb.service;

import com.viettel.bccs.sale.dto.PaymentDebitDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.sale.service.PaymentDebitService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsPaymentDebitServiceImpl")
public class WsPaymentDebitServiceImpl implements PaymentDebitService {

    public static final Logger logger = Logger.getLogger(WsPaymentDebitServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PaymentDebitService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(PaymentDebitService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public PaymentDebitDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<PaymentDebitDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<PaymentDebitDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public PaymentDebitDTO create(PaymentDebitDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(PaymentDebitDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    public PaymentDebitDTO getPaymentDebit(Long ownerId, Long ownerType) throws Exception {
        return client.getPaymentDebit(ownerId, ownerType);
    }
}
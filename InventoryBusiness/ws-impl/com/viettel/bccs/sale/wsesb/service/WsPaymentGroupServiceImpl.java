package com.viettel.bccs.sale.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.sale.dto.PaymentGroupDTO;
import com.viettel.bccs.sale.dto.PaymentGroupDayTypeDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;


import com.viettel.bccs.sale.service.PaymentGroupService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsPaymentGroupServiceImpl")
public class WsPaymentGroupServiceImpl implements PaymentGroupService {

    public static final Logger logger = Logger.getLogger(WsPaymentGroupServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PaymentGroupService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(PaymentGroupService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public PaymentGroupDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<PaymentGroupDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<PaymentGroupDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(PaymentGroupDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(PaymentGroupDTO dto) throws Exception  {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<PaymentGroupDTO> getLstPaymentGroup() throws Exception {
        return client.getLstPaymentGroup();
    }

    @Override
    public PaymentGroupDTO getPaymentGroupByName(String name) throws Exception {
        return client.getPaymentGroupByName(name);
    }

    @Override
    public List<PaymentGroupDayTypeDTO> getLstDayTypeByPaymentGroupId(Long paymentGroupId) throws Exception {
        return client.getLstDayTypeByPaymentGroupId(paymentGroupId);
    }
}
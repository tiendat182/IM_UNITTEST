package com.viettel.bccs.sale.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.sale.dto.PaymentGroupServiceDetailDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;


import com.viettel.bccs.sale.service.PaymentGroupServiceDetailService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsPaymentGroupServiceDetailServiceImpl")
public class WsPaymentGroupServiceDetailServiceImpl implements PaymentGroupServiceDetailService {

    public static final Logger logger = Logger.getLogger(WsPaymentGroupServiceDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PaymentGroupServiceDetailService client;

    @PostConstruct
    public void init() throws Exception {
      try{
         client = wsClientFactory.createWsClient(PaymentGroupServiceDetailService.class);
      } catch (Exception ex) {
          logger.error("init", ex);
      }
    }

    @Override
    public PaymentGroupServiceDetailDTO findOne(Long id) throws Exception  {
        return client.findOne(id);
    }

    @Override
    public List<PaymentGroupServiceDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<PaymentGroupServiceDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception  {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(PaymentGroupServiceDetailDTO dto) throws Exception  {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(PaymentGroupServiceDetailDTO dto) throws Exception  {
        return client.update(dto);
    }

}
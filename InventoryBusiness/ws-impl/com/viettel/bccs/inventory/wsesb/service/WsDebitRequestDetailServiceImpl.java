package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.DebitRequestDetailDTO;
import com.viettel.bccs.inventory.dto.DebitRequestReportDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.DebitRequestDetailService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsDebitRequestDetailServiceImpl")
public class WsDebitRequestDetailServiceImpl implements DebitRequestDetailService {

    public static final Logger logger = Logger.getLogger(WsDebitRequestDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DebitRequestDetailService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(DebitRequestDetailService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @WebMethod(exclude = true)
    @Override
    public DebitRequestDetailDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @WebMethod(exclude = true)
    @Override
    public List<DebitRequestDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @WebMethod(exclude = true)
    @Override
    public List<DebitRequestDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @WebMethod(exclude = true)
    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @WebMethod(exclude = true)
    @Override
    public BaseMessage create(DebitRequestDetailDTO dto) throws Exception {
        return client.create(dto);
    }

    @WebMethod(exclude = true)
    @Override
    public BaseMessage update(DebitRequestDetailDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public BaseMessage validate(DebitRequestDetailDTO debitRequestDetailDTO) throws LogicException,Exception {
        return client.validate(debitRequestDetailDTO);
    }


    @Override
    @WebMethod
    public List<DebitRequestDetailDTO> findByDebitRequestDetail(DebitRequestDetailDTO searcher) throws Exception {
        return client.findByDebitRequestDetail(searcher);
    }


    @Override
    public BaseMessage validateInternal(DebitRequestDetailDTO checked, List<DebitRequestDetailDTO> lstChecked, BaseMessage msg) {
        return client.validateInternal(checked, lstChecked, msg);
    }

    @Override
    public List<DebitRequestDetailDTO> getLstDetailByRequestId(Long requestId) throws Exception {
        return client.getLstDetailByRequestId(requestId);
    }

    @Override
    public List<DebitRequestReportDTO> getLstDetailForReport(Long requestId) throws Exception {
        return client.getLstDetailForReport(requestId);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import com.viettel.bccs.inventory.dto.DebitRequestDetailDTO;
import com.viettel.bccs.inventory.message.DebitRequestResponse;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.DebitRequestService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsDebitRequestServiceImpl")
public class WsDebitRequestServiceImpl implements DebitRequestService {

    public static final Logger logger = Logger.getLogger(WsDebitRequestServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DebitRequestService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(DebitRequestService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public DebitRequestDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<DebitRequestDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<DebitRequestDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }


    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    @WebMethod
    public BaseMessage save(DebitRequestDTO debitRequestDTO) throws LogicException, Exception {
        return client.save(debitRequestDTO);
    }

    @Override
    public BaseMessage create(DebitRequestDTO dto) throws LogicException, Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(DebitRequestDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public byte[] getAttachFileContent(Long requestId) throws Exception {
        return client.getAttachFileContent(requestId);
    }

    @Override
    @WebMethod
    public DebitRequestResponse approveDebitRequest(DebitRequestDTO dto) throws LogicException, Exception {
        return client.approveDebitRequest(dto);
    }

    @Override
    @WebMethod
    public List<DebitRequestDetailDTO> findDebitRequestDetailByDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        return client.findDebitRequestDetailByDebitRequest(debitRequestDTO);
    }

    @Override
    @WebMethod
    public BaseMessage deleteDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        return client.deleteDebitRequest(debitRequestDTO);
    }

    @Override
    @WebMethod
    public List<DebitRequestDTO> findDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        return client.findDebitRequest(debitRequestDTO);
    }

    @Override
    @WebMethod
    public Long getRevenueInMonth(Long ownerId, String ownerType) throws Exception {
        return client.getRevenueInMonth(ownerId, ownerType);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.PartnerContractDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.PartnerContractService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsPartnerContractServiceImpl")
public class WsPartnerContractServiceImpl implements PartnerContractService {

    public static final Logger logger = Logger.getLogger(WsPartnerContractServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PartnerContractService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(PartnerContractService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public PartnerContractDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<PartnerContractDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<PartnerContractDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(PartnerContractDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(PartnerContractDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public PartnerContractDTO findByStockTransID(Long stockTransID) throws LogicException, Exception {
        return client.findByStockTransID(stockTransID);
    }

    @Override
    @WebMethod
    public PartnerContractDTO save(PartnerContractDTO partnerContractDTO) throws Exception {
        return client.save(partnerContractDTO);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.PartnerService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsPartnerServiceImpl")
public class WsPartnerServiceImpl implements PartnerService {

    public static final Logger logger = Logger.getLogger(WsPartnerServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PartnerService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(PartnerService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public PartnerDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<PartnerDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<PartnerDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(PartnerDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(PartnerDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<PartnerDTO> findPartner(PartnerDTO partnerDTO) throws Exception {
        return client.findPartner(partnerDTO);
    }

    @Override
    @WebMethod
    public List<PartnerDTO> getAllPartner() throws Exception {
        return client.getAllPartner();
    }
}
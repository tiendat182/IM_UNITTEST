package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.ImportPartnerRequestService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsImportPartnerRequestServiceImpl")
public class WsImportPartnerRequestServiceImpl implements ImportPartnerRequestService {

    public static final Logger logger = Logger.getLogger(WsImportPartnerRequestServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ImportPartnerRequestService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ImportPartnerRequestService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ImportPartnerRequestDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ImportPartnerRequestDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ImportPartnerRequestDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage saveImportPartnerRequest(ImportPartnerRequestDTO dto) throws LogicException, Exception {
        return client.saveImportPartnerRequest(dto);
    }

    @Override
    @WebMethod
    public List<ImportPartnerRequestDTO> findImportPartnerRequest(ImportPartnerRequestDTO importPartnerRequestDTO) throws Exception {
        return client.findImportPartnerRequest(importPartnerRequestDTO);
    }

    @Override
    @WebMethod
    public Long getSequence() throws Exception {
        return client.getSequence();
    }

    @Override
    @WebMethod
    public String getRequestCode() throws Exception {
        return client.getRequestCode();
    }

    @Override
    @WebMethod
    public byte[] getAttachFileContent(Long requestID) throws LogicException, Exception {
        return client.getAttachFileContent(requestID);
    }
}
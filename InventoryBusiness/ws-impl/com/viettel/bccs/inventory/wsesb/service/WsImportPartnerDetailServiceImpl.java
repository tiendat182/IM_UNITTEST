package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.bccs.inventory.service.ImportPartnerDetailService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsImportPartnerDetailServiceImpl")
public class WsImportPartnerDetailServiceImpl implements ImportPartnerDetailService {

    public static final Logger logger = Logger.getLogger(WsImportPartnerDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ImportPartnerDetailService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ImportPartnerDetailService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public ImportPartnerDetailDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<ImportPartnerDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<ImportPartnerDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage saveImportPartnerDetail(ImportPartnerDetailDTO dto) throws Exception {
        return client.saveImportPartnerDetail(dto);
    }

    @Override
    @WebMethod
    public List<ImportPartnerDetailDTO> findImportPartnerDetail(ImportPartnerDetailDTO detailDTO) throws Exception {
        return client.findImportPartnerDetail(detailDTO);
    }

    @Override
    @WebMethod
    public void doValidateImportPartnerDetail(ImportPartnerDetailDTO importPartnerDetailDTO) throws LogicException, Exception {
        client.doValidateImportPartnerDetail(importPartnerDetailDTO);
    }
}
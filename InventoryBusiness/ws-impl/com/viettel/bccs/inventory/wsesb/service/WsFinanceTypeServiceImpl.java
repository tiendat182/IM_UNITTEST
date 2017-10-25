package com.viettel.bccs.inventory.wsesb.service;
import java.util.ArrayList;
import com.viettel.bccs.inventory.dto.FinanceTypeDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.FinanceTypeService;
import java.util.List;
import java.lang.Long;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
@Service("WsFinanceTypeServiceImpl")
public class WsFinanceTypeServiceImpl implements FinanceTypeService {

    public static final Logger logger = Logger.getLogger(WsFinanceTypeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private FinanceTypeService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(FinanceTypeService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public FinanceTypeDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<FinanceTypeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<FinanceTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(FinanceTypeDTO productSpecCharacterDTO, StaffDTO staffDTO) throws Exception {
        return client.create(productSpecCharacterDTO, staffDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(FinanceTypeDTO productSpecCharacterDTO, StaffDTO staffDTO) throws Exception {
        return client.update(productSpecCharacterDTO, staffDTO);
    }

    @Override
    @WebMethod
    public BaseMessage deleteListFinance(List<FinanceTypeDTO> lsFinanceTypeDTOs, StaffDTO staffDTO) throws Exception {
        return client.deleteListFinance(lsFinanceTypeDTOs, staffDTO);
    }

    @Override
    @WebMethod
    public List<FinanceTypeDTO> searchLsFinanceTypeDto(FinanceTypeDTO searchFinanceTypeDTO) throws LogicException, Exception {
        return client.searchLsFinanceTypeDto(searchFinanceTypeDTO);
    }
}
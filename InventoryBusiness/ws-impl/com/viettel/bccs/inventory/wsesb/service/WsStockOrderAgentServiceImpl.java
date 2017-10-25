package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.StockOrderAgentService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsStockOrderAgentServiceImpl")
public class WsStockOrderAgentServiceImpl implements StockOrderAgentService {

    public static final Logger logger = Logger.getLogger(WsStockOrderAgentServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockOrderAgentService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockOrderAgentService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public StockOrderAgentDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<StockOrderAgentDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockOrderAgentDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public StockOrderAgentDTO update(StockOrderAgentDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    public Long getMaxStockOrderAgentId() throws Exception {
        return client.getMaxStockOrderAgentId();
    }

    @Override
    public List<StockOrderAgentDTO> search(StockOrderAgentDTO dto) throws Exception, LogicException {
        return client.search(dto);
    }

    @Override
    public List<StockOrderAgentDTO> getStockOrderAgent(Long shopId, Long stockTransId) throws Exception, LogicException {
        return client.getStockOrderAgent(shopId, stockTransId);
    }

    @Override
    @WebMethod
    public List<StockOrderAgentForm> getStockOrderAgentForm(StockOrderAgentDTO searchForm, StaffDTO currentStaff) {
        return client.getStockOrderAgentForm(searchForm, currentStaff);
    }

    @Override
    public List checkShopAgent(Long shopId) {
        return client.checkShopAgent(shopId);
    }

    @Override
    @WebMethod
    public void createRequestStockOrder(StockOrderAgentDTO dto, List<StockOrderAgentDetailDTO> listDTODetail, StaffDTO currentStaff) throws Exception, LogicException {
        client.createRequestStockOrder(dto, listDTODetail, currentStaff);
    }
}
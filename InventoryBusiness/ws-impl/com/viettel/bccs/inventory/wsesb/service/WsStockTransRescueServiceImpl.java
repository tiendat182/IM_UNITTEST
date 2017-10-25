package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockTransRescueDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.bccs.inventory.service.StockTransRescueService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsStockTransRescueServiceImpl")
public class WsStockTransRescueServiceImpl implements StockTransRescueService {

    public static final Logger logger = Logger.getLogger(WsStockTransRescueServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransRescueService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockTransRescueService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public StockTransRescueDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<StockTransRescueDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockTransRescueDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockTransRescueDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockTransRescueDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<StockTransRescueDTO> searchStockRescue(StockTransRescueDTO stockTransRescueDTO) throws LogicException, Exception {
        return client.searchStockRescue(stockTransRescueDTO);
    }

    @Override
    @WebMethod
    public Long getMaxId() throws LogicException, Exception {
        return client.getMaxId();
    }

    @Override
    @WebMethod
    public Long getReasonId() throws LogicException, Exception {
        return client.getReasonId();
    }

    @Override
    @WebMethod
    public BaseMessage createRequest(StockTransRescueDTO stockTransRescueAdd, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.createRequest(stockTransRescueAdd, requiredRoleMap);
    }

    @Override
    @WebMethod
    public BaseMessage deleteStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.deleteStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Override
    @WebMethod
    public BaseMessage receiveStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Override
    @WebMethod
    public BaseMessage acceptStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.acceptStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Override
    @WebMethod
    public BaseMessage cancelStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.cancelStockRescue(stockTransRescueAction, requiredRoleMap);
    }

    @Override
    @WebMethod
    public BaseMessage returnStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap, List<StockTransSerialRescueDTO> lstSerial) throws LogicException, Exception {
        return client.returnStockRescue(stockTransRescueAction, requiredRoleMap, lstSerial);
    }
}
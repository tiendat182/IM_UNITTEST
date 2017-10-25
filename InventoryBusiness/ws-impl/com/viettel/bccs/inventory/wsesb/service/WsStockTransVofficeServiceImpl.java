package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.StockTransVofficeService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsStockTransVofficeServiceImpl")
public class WsStockTransVofficeServiceImpl implements StockTransVofficeService {

    public static final Logger logger = Logger.getLogger(WsStockTransVofficeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransVofficeService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockTransVofficeService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockTransVofficeDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockTransVofficeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockTransVofficeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        return client.create(stockTransVofficeDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        return client.update(stockTransVofficeDTO);
    }

    @Override
    @WebMethod
    public StockTransVofficeDTO save(StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        return client.save(stockTransVofficeDTO);
    }

    @Override
    @WebMethod
    public void doSignedVofficeValidate(StockTransActionDTO stockTransActionDTO) throws Exception, LogicException {
        client.doSignedVofficeValidate(stockTransActionDTO);
    }

    @Override
    @WebMethod
    public StockTransVofficeDTO findStockTransVofficeByRequestId(String requestId) throws LogicException, Exception {
        return client.findStockTransVofficeByRequestId(requestId);
    }

    @Override
    @WebMethod
    public StockTransVofficeDTO findStockTransVofficeByActionId(Long actionId) throws LogicException, Exception {
        return client.findStockTransVofficeByActionId(actionId);
    }

    @Override
    public List<StockTransVofficeDTO> searchStockTransVoffice(StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        return client.searchStockTransVoffice(stockTransVofficeDTO);
    }

    @Override
    public void updateVofficeDOA(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        client.updateVofficeDOA(stockTransVoffice);
    }

    @Override
    public List<StockTransVoffice> getLstVofficeSigned(Long maxDay, String prefixTemplate) throws Exception {
        return client.getLstVofficeSigned(maxDay, prefixTemplate);
    }

    @Override
    public void doSignVOffice(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception {
        client.doSignVOffice(stockTransVofficeDTO);
    }

    @Override
    public void updateVofficeDeliver(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        client.updateVofficeDeliver(stockTransVoffice);
    }

    @Override
    public void updateVofficeDevice(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        client.updateVofficeDevice(stockTransVoffice);
    }

    @Override
    public void updateVofficeDebit(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        client.updateVofficeDebit(stockTransVoffice);
    }
}
package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.SimKITDTO;
import com.viettel.bccs.inventory.dto.StockSimDTO;
import com.viettel.bccs.inventory.service.StockSimService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service("WsStockSimServiceImpl")
public class WsStockSimServiceImpl implements StockSimService {

    public static final Logger logger = Logger.getLogger(WsStockSimServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockSimService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockSimService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockSimDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockSimDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @WebMethod
    @Override
    /**
     * return null;
     */
    public SimKITDTO findStockSim(@WebParam(name = "imsi") String imsi, @WebParam(name = "serial") String serial) throws Exception {
        return client.findStockSim(imsi, serial);
    }

    @WebMethod
    @Override
    /**
     * return null;
     */
    public SimKITDTO findStockKit(@WebParam(name = "serial") String serial) throws Exception {
        return client.findStockKit(serial);
    }

    @Override
    @WebMethod
    public StockSimDTO getSimInfor(String msisdn) throws Exception {
        return client.getSimInfor(msisdn);
    }

    @Override
    @WebMethod
    public boolean isCaSim(String serial) {
        return client.isCaSim(serial);
    }

    @Override
    @WebMethod
    public boolean checkSimElegibleExists(String fromSerial, String toSerial) throws Exception, LogicException {
        return client.checkSimElegibleExists(fromSerial, toSerial);
    }
}
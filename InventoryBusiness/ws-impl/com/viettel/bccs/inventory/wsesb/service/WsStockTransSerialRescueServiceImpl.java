package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.bccs.inventory.service.StockTransSerialRescueService;
import com.viettel.fw.Exception.LogicException;
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

@Service("WsStockTransSerialRescueServiceImpl")
public class WsStockTransSerialRescueServiceImpl implements StockTransSerialRescueService {

    public static final Logger logger = Logger.getLogger(WsStockTransSerialRescueServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransSerialRescueService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockTransSerialRescueService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public StockTransSerialRescueDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<StockTransSerialRescueDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockTransSerialRescueDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockTransSerialRescueDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockTransSerialRescueDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    @WebMethod
    public List<StockTransSerialRescueDTO> viewDetailSerail(Long stockTransId, Long prodOfferId, Long prodOfferIdReturn) throws LogicException, Exception {
        return client.viewDetailSerail(stockTransId, prodOfferId, prodOfferIdReturn);
    }

    @Override
    @WebMethod
    public StockTransSerialRescueDTO save(StockTransSerialRescueDTO stockTransSerialRescueDTO) throws Exception {
        return client.save(stockTransSerialRescueDTO);
    }

    @Override
    @WebMethod
    public List<StockTransSerialRescueDTO> viewDetailSerailByStockTransId(Long stockTransId) throws LogicException, Exception {
        return client.viewDetailSerailByStockTransId(stockTransId);
    }

    @Override
    @WebMethod
    public List<StockTransSerialRescueDTO> getListDetailSerial(Long stockTransId) throws LogicException, Exception {
        return client.getListDetailSerial(stockTransId);
    }
}
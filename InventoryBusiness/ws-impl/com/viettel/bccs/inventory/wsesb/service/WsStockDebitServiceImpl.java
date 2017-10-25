package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockDebitDTO;
import com.viettel.bccs.inventory.service.StockDebitService;
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

@Service("WsStockDebitServiceImpl")
public class WsStockDebitServiceImpl implements StockDebitService {

    public static final Logger logger = Logger.getLogger(WsStockDebitServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockDebitService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockDebitService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockDebitDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockDebitDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockDebitDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(StockDebitDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(StockDebitDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public List<StockDebitDTO> findStockDebit(StockDebitDTO stockDebitDTO) throws Exception {
        return client.findStockDebit(stockDebitDTO);
    }

    @Override
    @WebMethod
    public Long totalPriceStock(Long ownerId, String ownerType) throws Exception {
        return client.totalPriceStock(ownerId, ownerType);
    }

    @Override
    @WebMethod
    public StockDebitDTO findStockDebitValue(Long ownerId, String ownerType) throws Exception {
        return client.findStockDebitValue(ownerId, ownerType);
    }

    @Override
    @WebMethod
    public StockDebitDTO findStockDebitByStockTrans(Long stockTransId) throws LogicException, Exception {
        return client.findStockDebitByStockTrans(stockTransId);
    }

    @Override
    @WebMethod
    public List<StockDebitDTO> findStockDebitNative(Long ownerId, String ownerType) throws Exception, LogicException {
        return client.findStockDebitNative(ownerId, ownerType);
    }
}
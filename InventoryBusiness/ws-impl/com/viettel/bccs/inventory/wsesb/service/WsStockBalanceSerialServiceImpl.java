package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockBalanceSerialDTO;
import com.viettel.bccs.inventory.service.StockBalanceSerialService;
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

@Service("WsStockBalanceSerialServiceImpl")
public class WsStockBalanceSerialServiceImpl implements StockBalanceSerialService {

    public static final Logger logger = Logger.getLogger(WsStockBalanceSerialServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockBalanceSerialService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockBalanceSerialService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @WebMethod
    @Override
    public StockBalanceSerialDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @WebMethod
    @Override
    public List<StockBalanceSerialDTO> findAll() throws Exception {
        return client.findAll();
    }

    @WebMethod
    @Override
    public List<StockBalanceSerialDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @WebMethod
    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @WebMethod
    @Override
    public BaseMessage create(StockBalanceSerialDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    @WebMethod
    public StockBalanceSerialDTO save(StockBalanceSerialDTO dto) throws Exception {
        return client.save(dto);
    }

    @Override
    @WebMethod
    public List<StockBalanceSerialDTO> getListStockBalanceSerialDTO(Long stockBalanceDetailID) throws Exception {
        return client.getListStockBalanceSerialDTO(stockBalanceDetailID);
    }
}
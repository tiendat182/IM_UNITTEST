package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockBalanceDetailDTO;
import com.viettel.bccs.inventory.service.StockBalanceDetailService;
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

@Service("WsStockBalanceDetailServiceImpl")
public class WsStockBalanceDetailServiceImpl implements StockBalanceDetailService {

    public static final Logger logger = Logger.getLogger(WsStockBalanceDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockBalanceDetailService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockBalanceDetailService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @WebMethod
    @Override
    public StockBalanceDetailDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @WebMethod
    @Override
    public List<StockBalanceDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @WebMethod
    @Override
    public List<StockBalanceDetailDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @WebMethod
    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @WebMethod
    @Override
    public BaseMessage create(StockBalanceDetailDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    @WebMethod
    public StockBalanceDetailDTO save(StockBalanceDetailDTO dto) throws Exception {
        return client.save(dto);
    }

    @Override
    @WebMethod
    public List<StockBalanceDetailDTO> getListStockBalanceDetail(Long requestID) throws Exception {
        return client.getListStockBalanceDetail(requestID);
    }
}
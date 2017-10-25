package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockTransOfflineDTO;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.service.StockTransOfflineService;
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

@Service("WsStockTransOfflineServiceImpl")
public class WsStockTransOfflineServiceImpl implements StockTransOfflineService {

    public static final Logger logger = Logger.getLogger(WsStockTransOfflineServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTransOfflineService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockTransOfflineService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public StockTransOfflineDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<StockTransOfflineDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockTransOfflineDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    //    @Override
//    public BaseMessage create(StockTransOfflineDTO dto) throws Exception  {
//        return client.create(dto);
//    }
//
//    @Override
//    public BaseMessage update(StockTransOfflineDTO dto) throws Exception  {
//        return client.update(dto);
//    }
//
    @Override
    @WebMethod
    public StockTransOfflineDTO save(StockTransOfflineDTO stockTransOfflineDTO) throws Exception {
        return client.save(stockTransOfflineDTO);
    }

    @Override
    public void processExpStockOffine(StockTrans stockTrans) throws Exception, LogicException {
        client.processExpStockOffine(stockTrans);
    }

    @Override
    public StockTransOfflineDTO getStockTransOfflineById(Long stockTransId) throws Exception {
        return client.getStockTransOfflineById(stockTransId);
    }
}
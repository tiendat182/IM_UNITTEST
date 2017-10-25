package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockRequestOrderDetailDTO;
import com.viettel.bccs.inventory.service.StockRequestOrderDetailService;
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

@Service("WsStockRequestOrderDetailServiceImpl")
public class WsStockRequestOrderDetailServiceImpl implements StockRequestOrderDetailService {

    public static final Logger logger = Logger.getLogger(WsStockRequestOrderDetailServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockRequestOrderDetailService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockRequestOrderDetailService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockRequestOrderDetailDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockRequestOrderDetailDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockRequestOrderDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public StockRequestOrderDetailDTO save(StockRequestOrderDetailDTO stockRequestOrderDetailDTO) throws Exception {
        return client.save(stockRequestOrderDetailDTO);
    }

    @Override
    public List<StockRequestOrderDetailDTO> findSearchStockRequestOrder(Long stockRequestOrderId, String status) throws Exception {
        return client.findSearchStockRequestOrder(stockRequestOrderId, status);
    }

    @Override
    public List<StockRequestOrderDetailDTO> getLstByStockRequestId(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId) throws Exception {
        return client.getLstByStockRequestId(stockRequestOrderId, fromOwnerId, toOwnerId);
    }

    @Override
    public List<StockRequestOrderDetailDTO> getOrderDetailByStockTransId(Long stockTransId) throws Exception {
        return client.getOrderDetailByStockTransId(stockTransId);
    }

    @Override
    public List<StockRequestOrderDetailDTO> getLstDetailTemplate(Long stockRequestOrderId) throws Exception {
        return client.getLstDetailTemplate(stockRequestOrderId);
    }

    @Override
    public List<StockRequestOrderDetailDTO> getLstDetailToExport() throws Exception {
        return client.getLstDetailToExport();
    }

    @Override
    public int updateCancelNote(Long stockRequestOrderId) throws Exception {
        return client.updateCancelNote(stockRequestOrderId);
    }
}
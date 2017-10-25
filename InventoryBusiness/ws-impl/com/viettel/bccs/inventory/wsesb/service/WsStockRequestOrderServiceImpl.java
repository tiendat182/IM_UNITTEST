package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockRequestOrderDTO;
import com.viettel.bccs.inventory.service.StockRequestOrderService;
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
import java.util.Date;
import java.util.List;

@Service("WsStockRequestOrderServiceImpl")
public class WsStockRequestOrderServiceImpl implements StockRequestOrderService {

    public static final Logger logger = Logger.getLogger(WsStockRequestOrderServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockRequestOrderService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockRequestOrderService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockRequestOrderDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockRequestOrderDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockRequestOrderDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    public StockRequestOrderDTO save(StockRequestOrderDTO stockRequestOrderDTO) throws Exception {
        return client.save(stockRequestOrderDTO);
    }

    @Override
    public StockRequestOrderDTO update(StockRequestOrderDTO stockRequestOrderDTO) throws Exception {
        return client.update(stockRequestOrderDTO);
    }

    @Override
    @WebMethod
    public StockRequestOrderDTO createGoodOrderFromProvince(StockRequestOrderDTO stockRequestOrderDTO) throws LogicException, Exception {
        return client.createGoodOrderFromProvince(stockRequestOrderDTO);
    }

    @Override
    public List<StockRequestOrderDTO> findSearhStockOrder(String orderCode, Date fromDate, Date endDate, String status, Long ownerId, Long ownerType) throws Exception {
        return client.findSearhStockOrder(orderCode, fromDate, endDate, status, ownerId, ownerType);
    }

    @Override
    public void approveStockRequestOrder(StockRequestOrderDTO stockRequestOrderDTO) throws LogicException, Exception {
        client.approveStockRequestOrder(stockRequestOrderDTO);
    }

    @Override
    public List<StockRequestOrderDTO> getLstApproveAndSignVoffice() throws Exception {
        return client.getLstApproveAndSignVoffice();
    }

    @Override
    public void processCreateExpNote(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId) throws LogicException, Exception {
        client.processCreateExpNote(stockRequestOrderId, fromOwnerId, toOwnerId);
    }

    @Override
    public void processImportStock(Long stockTransId) throws LogicException, Exception {
        client.processImportStock(stockTransId);
    }
}
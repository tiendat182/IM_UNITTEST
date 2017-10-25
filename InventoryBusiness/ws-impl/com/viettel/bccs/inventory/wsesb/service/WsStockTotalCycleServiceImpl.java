package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.bccs.inventory.service.StockTotalCycleService;
import com.viettel.bccs.inventory.service.StockTotalService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

@Service("WsStockTotalCycleServiceImpl")
public class WsStockTotalCycleServiceImpl implements StockTotalCycleService {

    public static final Logger logger = Logger.getLogger(WsStockTotalCycleServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTotalCycleService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockTotalCycleService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockTotalCycleDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockTotalCycleDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockTotalCycleDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(StockTotalCycleDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(StockTotalCycleDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public List<StockTotalCycleReportDTO> getStockCycle(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception {
        return client.getStockCycle(fromDate, toDate, stateId, productOfferTypeId, prodOfferId, ownerType, ownerId);
    }

    @Override
    public List<StockTotalCycleDTO> getStockCycleExport(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception {
        return client.getStockCycleExport(fromDate, toDate, stateId, productOfferTypeId, prodOfferId, ownerType, ownerId);
    }

    @Override
    public List<String> getListSerialStockXByDayRemain(Long prodOfferId, Long ownerId, Long ownerType, Long typeCycle,
                                                       String tableName, Long dayStockRemain, Long stateId) throws Exception {
        return client.getListSerialStockXByDayRemain(prodOfferId, ownerId, ownerType, typeCycle, tableName, dayStockRemain, stateId);
    }

    @Override
    public void doStockTotalCycle(Logger logger, Long ownerType, Long productOfferTypeId, ThreadCloseStockDTO threadCloseStockDTO) throws Exception {
        client.doStockTotalCycle(logger, ownerType, productOfferTypeId, threadCloseStockDTO);
    }

    @Override
    public List<StockTotalCycleDTO> findStockOver(Long ownerId, Long ownerType) throws Exception {
        return client.findStockOver(ownerId, ownerType);
    }

    @Override
    public List<StockTotalCycleDTO> findStockOverByStateId(Long ownerId, Long ownerType) throws Exception {
        return client.findStockOverByStateId(ownerId, ownerType);
    }

    @Override
    public List<StockTotalCycleDTO> findStockOverByTypeId(Long ownerId, Long ownerType) throws Exception {
        return client.findStockOverByTypeId(ownerId, ownerType);
    }
}
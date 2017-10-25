package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ActionLogDTO;
import com.viettel.bccs.inventory.dto.ActionLogDetailDTO;
import com.viettel.bccs.inventory.dto.StockTotalDTO;
import com.viettel.bccs.inventory.dto.StockTotalMessage;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.bccs.inventory.service.StockTotalService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("WsStockTotalServiceImpl")
public class WsStockTotalServiceImpl implements StockTotalService {

    public static final Logger logger = Logger.getLogger(WsStockTotalServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockTotalService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockTotalService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockTotalDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockTotalDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public List<StockTotalDTO> searchStockTotal(StockTotalDTO stockTotalDTO) throws LogicException, Exception {
        return client.searchStockTotal(stockTotalDTO);
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTotalDTO save(StockTotalDTO stockTotalDTO) throws Exception {
        return client.save(stockTotalDTO);
    }

    @Override
    public Long getTotalValueStock(Long ownerId, String ownerType) throws LogicException, Exception {
        return client.getTotalValueStock(ownerId, ownerType);
    }

    @Override
    @WebMethod
    public Long checkAction(StockTotalDTO stockTotalDTO) throws LogicException, Exception {
        return client.checkAction(stockTotalDTO);
    }

    @Override
    public StockTotalMessage changeStockTotal(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, Long qty, Long qtyIssue, Long qtyHang, Long userId, Long reasonId, Long transId, Date transDate, String transCode, String transType, Const.SourceType sourceType) throws LogicException, Exception {
        return client.changeStockTotal(ownerId, ownerType, prodOfferId, stateId, qty, qtyIssue, qtyHang, userId, reasonId, transId, transDate, transCode, transType, sourceType);
    }

    @Override
    public List<StockTotalWsDTO> getStockTotalDetail(Long ownerId, Long ownerType, String prodOfferName) throws Exception {
        return client.getStockTotalDetail(ownerId, ownerType, prodOfferName);
    }

    @Override
    public StockTotalDTO getStockTotalForProcessStock(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        return client.getStockTotalForProcessStock(ownerId, ownerType, prodOfferId, stateId);
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTotalMessage saveForProcessStockNoSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, Long qty, Long qtyIssue, Long qtyHang, Long userId, Long reasonId, Long transId, Date transDate, String transCode, String transType, Const.SourceType sourceType, ActionLogDTO dto, List<ActionLogDetailDTO> lstDetail) throws Exception {
        return client.saveForProcessStockNoSerial(ownerId, ownerType, prodOfferId, stateId, qty, qtyIssue, qtyHang, userId, reasonId, transId, transDate, transCode, transType, sourceType, dto, lstDetail);
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTotalDTO saveForProcessStockSerial(StockTotalDTO stockTotalDTO, ActionLogDTO dto, List<ActionLogDetailDTO> lstDetail) throws Exception {
        return client.saveForProcessStockSerial(stockTotalDTO, dto, lstDetail);
    }

    @Override
    public List<StockTotalDTO> getStockTotalAvailable(Long ownerType, Long productOfferTypeId) throws Exception {
        return client.getStockTotalAvailable(ownerType, productOfferTypeId);
    }

    @Override
    public List<Long> getTotalQuantityStockTotal(Long prodOfferId, Long ownerId) throws Exception {
        return client.getTotalQuantityStockTotal(prodOfferId, ownerId);
    }

    @Override
    @WebMethod
    public boolean checkStockTotal(Long ownerId, Long prodOfferId, Long qty) throws Exception {
        return client.checkStockTotal(ownerId, prodOfferId, qty);
    }

    @Override
    @WebMethod
    public boolean checkStockTotalIm1(Long ownerId, Long prodOfferId, Long qty) throws Exception {
        return client.checkStockTotalIm1(ownerId, prodOfferId, qty);
    }

    @Override
    @WebMethod
    public StockTotalIm1DTO getStockTotalForProcessStockIm1(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        return client.getStockTotalForProcessStockIm1(ownerId, ownerType, prodOfferId, stateId);
    }

    @Override
    @WebMethod
    public boolean checkHaveProductInStockTotal(Long ownerId, Long ownerType) throws Exception {
        return client.checkHaveProductInStockTotal(ownerId, ownerType);
    }
}
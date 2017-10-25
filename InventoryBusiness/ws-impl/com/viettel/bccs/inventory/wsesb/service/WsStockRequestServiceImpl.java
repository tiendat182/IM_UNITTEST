package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.bccs.inventory.dto.StockTotalAuditDTO;
import com.viettel.bccs.inventory.dto.StockTotalDTO;
import com.viettel.bccs.inventory.service.StockRequestService;
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

@Service("WsStockRequestServiceImpl")
public class WsStockRequestServiceImpl implements StockRequestService {

    public static final Logger logger = Logger.getLogger(WsStockRequestServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockRequestService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockRequestService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockRequestDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockRequestDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(StockRequestDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(StockRequestDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public String getRequest() throws Exception {
        return client.getRequest();
    }

    @Override
    @WebMethod
    public List<StockRequestDTO> findStockRequestByCode(String requestCode) throws Exception {
        return client.findStockRequestByCode(requestCode);
    }

    @Override
    @WebMethod
    public List<StockRequestDTO> getLsSearchStockRequest(String requestCode, Date fromDate, Date toDate, String status, Long shopIdLogin, Long ownerId, Long ownerType) throws Exception {
        return client.getLsSearchStockRequest(requestCode, fromDate, toDate, status, shopIdLogin, ownerId, ownerType);
    }

    @Override
    @WebMethod
    public void excuteRequestTrans(StockRequestDTO stockRequestDTO) throws LogicException, Exception {
        client.excuteRequestTrans(stockRequestDTO);
    }

    @Override
    public void addTotalTTBH(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerType, Long ownerId, Long prodOfferId, Long oldStateId, Long quantity, Date sysdate, boolean isCheckIm1) throws Exception {
        client.addTotalTTBH(totalDTOSearch, totalAuditInput, ownerType, ownerId, prodOfferId, oldStateId, quantity, sysdate, isCheckIm1);
    }

    @Override
    public void updateStockXTTBH(String fromSerial, String toSerial, Date updateDateTime, Long prodOfferId, Long productOfferTypeId,
                                 Long newOwnerType, Long newOwnerId, Long oldOwnerType, Long oldOwnerId, Long newStatus, Long oldStatus, Long oldStateId, boolean isCheckIm1) throws Exception {
        client.updateStockXTTBH(fromSerial, toSerial, updateDateTime, prodOfferId, productOfferTypeId,
                newOwnerType, newOwnerId, oldOwnerType, oldOwnerId, newStatus, oldStatus, oldStateId, isCheckIm1);
    }

    @Override
    @WebMethod
    public Long getAvailableQuantity(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        return client.getAvailableQuantity(ownerId, ownerType, prodOfferId, stateId);
    }

    @Override
    @WebMethod
    public BaseMessage save(StockRequestDTO stockRequestDTO) throws Exception {
        return client.save(stockRequestDTO);
    }

    @Override
    @WebMethod
    public StockRequestDTO getStockRequestByStockTransId(Long stockTransId) throws Exception {
        return client.getStockRequestByStockTransId(stockTransId);
    }

    @Override
    @WebMethod
    public void excuteImportTTBGRequestTrans(Long requestId, String status, StaffDTO staffDTO) throws LogicException, Exception {
        client.excuteImportTTBGRequestTrans(requestId, status, staffDTO);
    }

    @Override
    @WebMethod
    public List<StockRequestDTO> getLsSearchStockRequestTTBH(String requestCode, Date fromDate, Date toDate, String status, Long ownerId) throws Exception {
        return client.getLsSearchStockRequestTTBH(requestCode, fromDate, toDate, status, ownerId);
    }
}
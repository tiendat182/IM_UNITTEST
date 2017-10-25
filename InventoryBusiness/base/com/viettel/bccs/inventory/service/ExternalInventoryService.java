package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface ExternalInventoryService {
    @WebMethod
    public ChangeResultDTO change2gTo3g(String serial2G, Long prodOfferId2G, String serial3G, Long prodOfferId3G, Long staffId, Long collStaffId, String custName, String custTel) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferingDTO> getProductInStock(Long ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception;

    @WebMethod
    public StockHandsetDTO getStockHandset(Long prodOfferId, String serial) throws LogicException, Exception;

    @WebMethod
    public List<StockTotalCycleReportDTO> getStockTotalCycle(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception;

    @WebMethod
    public BaseMessage cancelChange2gTo3g(@WebParam(name = "stockTransId2G") Long stockTransId2G, @WebParam(name = "stockTransId3G") Long stockTransId3G) throws LogicException, Exception;
}
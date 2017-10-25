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
public interface InventoryExternalService {

    @WebMethod
    public StockSimMessage getSimInfor(@WebParam(name = "msisdn") String msisdn);

    @WebMethod
    public boolean isCaSim(@WebParam(name = "serial") String serial);

    @WebMethod
    public StockHandsetDTO getStockModelSoPin(@WebParam(name = "serial") String serial);

    @WebMethod
    public StockTotalResultDTO getQuantityInEcomStock(@WebParam(name = "prodOfferCode") String prodOfferCode) throws LogicException, Exception;

    @WebMethod
    public BaseMessage verifyUnlockG6(@WebParam(name = "imei") String imei) throws LogicException, Exception;

    @WebMethod
    public BaseMessage unlockG6(@WebParam(name = "imei") String imei) throws LogicException, Exception;

    @WebMethod
    public StockDeliveringResultDTO getLstStockDeliveringBill(@WebParam(name = "startTime") Date startTime, @WebParam(name = "endTime") Date endTime) throws LogicException, Exception;

    @WebMethod
    public BaseMessage serialCardIsSaledOnBCCS(@WebParam(name = "serial") String serial) throws LogicException, Exception;

    @WebMethod
    public BaseMessage checkTransferCondition(@WebParam(name = "staffId") Long staffId, @WebParam(name = "checkCollaborator") boolean checkCollaborator) throws LogicException, Exception;

    @WebMethod
    public StampInforDTO getStampInformation(List<StampListDTO> lstStampInput) throws LogicException, Exception;

    @WebMethod
    public StockDeliverResultDTO checkStaffHaveStockDeliver(List<String> lstStaffCode) throws LogicException, Exception;


}
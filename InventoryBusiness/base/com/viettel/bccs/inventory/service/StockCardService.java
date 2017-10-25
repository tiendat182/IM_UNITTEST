package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;

import java.util.List;

import com.viettel.bccs.inventory.model.StockCard;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface StockCardService {

    @WebMethod
    public StockCardDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockCardDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockCard> findStockCard(StockTransDetailDTO stockTransDetail, Long oldStatus, Long oldOwnerType,
                                         Long oldOwnerId, String fromSerial, String toSerial) throws LogicException, Exception;

    @WebMethod
    public void updateStockCard(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, StockCardDTO stockCard, String fromSerial, String toSerial,
                                Long newStatus, Long newOwnerType, Long newOwnerId) throws LogicException, Exception;

    @WebMethod
    public void doSaveStockCard(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, StockTransDetailDTO stockTransDetail, Long newStatus, Long oldStatus,
                                Long newOwnerType, Long oldOwnerType, Long newOwnerId, Long oldOwnerId) throws LogicException, Exception;

    @WebMethod
    public boolean checkNotExistInVC(Long prodOfferId, String fromSerial, String toSerial) throws LogicException, Exception;

    @WebMethod
    public void insertStockCardFromSaled(Long prodOfferId, String serial) throws LogicException, Exception;

    @WebMethod
    public void insertStockCardFromSaledIm1(Long prodOfferId, String serial) throws LogicException, Exception;

    @WebMethod
    public boolean checkStockCardIm1(Long prodOfferId, String serial) throws LogicException, Exception;

    @WebMethod
    public void updatePincodeForStockCard(Long ownerId, String serial, Long prodOfferId, String pincode, String updateType, StaffDTO staffDTO) throws LogicException, Exception;

    @WebMethod
    public int updateListPincodeForStockCard(List<UpdatePincodeDTO> lstPincodes, Long ownerId, Long prodOfferId, String updateType, StaffDTO staffDTO) throws LogicException, Exception;

    @WebMethod
    public boolean checkInfoStockCard(String pincode, Long ownerId, String serial, Long prodOfferId, String updateType, Long stateId) throws Exception;

    @WebMethod
    public StockTotalResultDTO getQuantityInEcomStock(String prodOfferCode) throws LogicException, Exception;

    @WebMethod
    public List<Long> getStatusSerialCardSale(String serial) throws LogicException, Exception;

    @WebMethod
    public BaseMessage unlockG6(String imei) throws LogicException, Exception;

    @WebMethod
    public void exchangeCardBankplus(ExchangeCardBankplusDTO exchangeCardBankplusDTO, Long shopId, StaffDTO staffDTO) throws LogicException, Exception;

    @WebMethod
    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serial) throws Exception;
}
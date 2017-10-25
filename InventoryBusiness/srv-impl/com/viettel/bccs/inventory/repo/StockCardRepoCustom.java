package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockCard;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.sql.Connection;
import java.util.List;

public interface StockCardRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockCard> findStockCard(StockTransDetailDTO stockTransDetail, Long oldStatus, Long oldOwnerType,
                                         Long oldOwnerId, String fromSerial, String toSerial) throws LogicException, Exception;

    public Long checkNotExistInVC(Long prodOfferId, String fromSerial, String toSerial) throws LogicException, Exception;

    public void insertStockCardFromSaled(Long prodOfferId, String serial) throws LogicException, Exception;

    public void insertStockCardFromSaledIm1(Long prodOfferId, String serial) throws LogicException, Exception;

    public boolean checkStockCardIm1(Long prodOfferId, String serial) throws LogicException, Exception;

    public int updatePincodeForStockCard(Long ownerId, String serial, Long prodOfferId, String pincode, String updateType) throws LogicException, Exception;

    public int updatePincodeForStockCardIM1(Long ownerId, String serial, Long prodOfferId, String pincode, String updateType) throws LogicException, Exception;

    public int saveActionLog(Long actionId, String actionType, String serial, String actionUser, String actionIp, Long prodOfferId) throws Exception;

    public int saveActionLogDetail(Long actionId, String pincode, String serial) throws Exception;

    public int updateListPincodeForStockCard(Connection conn, List<UpdatePincodeDTO> lstPincodes, Long ownerId, Long prodOfferId, String updateType) throws LogicException, Exception;

    public int updateListPincodeForStockCardIM1(Connection conn, List<UpdatePincodeDTO> lstPincodes, Long ownerId, Long prodOfferId, String updateType, StaffDTO staffDTO) throws LogicException, Exception;

    public boolean checkInfoStockCard(String pincode, Long ownerId, String serial, Long prodOfferId, String updateType, Long stateId) throws Exception;

    public StockTotalResultDTO getQuantityInEcomStock(String prodOfferCode) throws LogicException, Exception;

    public List<Long> getStatusSerialCardSale(String serial) throws LogicException, Exception;

    public List<LockupTransactionCardDTO> getSaleTranInfo(ExchangeCardBankplusDTO exchangeCardBankplusDTO) throws Exception;

    public StockCardDTO getInfoStockCard(String serial, Long ownerId, Long status, Long stateId) throws Exception;

    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serialList) throws Exception;
}
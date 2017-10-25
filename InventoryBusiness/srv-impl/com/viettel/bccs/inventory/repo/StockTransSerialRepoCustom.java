package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface StockTransSerialRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockTransSerialDTO> getListStockTransSerial(Long stockTransActionId) throws Exception;

    public List<StockTransSerialDTO> findStockTransSerial(StockTransSerialDTO stockTransSerialDTO) throws Exception;

    /**
     * get danh sach serial
     *
     * @param ownerType
     * @param ownerId
     * @param tableName
     * @param productOfferId
     * @param stateId
     * @param channelTypeId
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    List<StockTransSerialDTO> getRangeSerial(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId, String channelTypeId, Long serialStatus) throws Exception;

    /**
     * get danh sach serial
     *
     * @param ownerType
     * @param ownerId
     * @param tableName
     * @param productOfferId
     * @param stateId
     * @param channelTypeId
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    List<StockTransSerialDTO> getRangeSerialFifo(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId, String channelTypeId, Long serialStatus) throws Exception;

    /**
     * thaont19
     *
     * @param productOfferType
     * @return
     * @throws Exception
     */
    public List<StockTransSerialDTO> findStockTransSerialByProductOfferType(WareHouseInfoDTO infoDTO) throws Exception;

    /**
     * ham tra ve list danh sach serial hop le
     *
     * @param ownerType
     * @param ownerId
     * @param tableName
     * @param productOfferId
     * @param stateId
     * @param fromSerial
     * @param toSerial
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    List<String> getListSerialValid(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId,
                                    String fromSerial, String toSerial, Long rowNum, Long serialStatus) throws Exception;

    /**
     * ham lay danh sach dai serial stockcard
     *
     * @param ownerType
     * @param ownerId
     * @param productOfferId
     * @param stateId
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    List<String> getRangeSerialStockCardValid(Long ownerType, Long ownerId, Long productOfferId, Long stateId,
                                              String fromSerial, String toSerial, Long quantity, String status) throws Exception;

    List<String> getRangeSerialStockCardValidWithoutOwner(Long productOfferId, Long stateId,
                                                          String fromSerial, String toSerial, Long quantity, String status) throws Exception;

    public List<StockTransSerialDTO> getListSerialFromTable(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName) throws Exception;

    public List<StockInspectCheckDTO> getListGatherSerial(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName, String fromSerial, String toSerial) throws Exception;

    public List<StockInspectCheckDTO> getListSerialFromList(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName, List<String> lstSerial) throws Exception;

    public int updateStatusStockSerial(Long ownerId, Long ownerType, Long prodOfferId, String fromSerial, String toSerial, Long newStatus, Long oldStatus) throws Exception;

    public Long getDepostiPriceFromStockX(Long ownerId, Long ownerType, Long prodOfferId, String serial) throws Exception;

    public Long getDepostiPriceForRetrieve(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String serial) throws Exception;

    public Long getDepostiPriceByRangeSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String fromSerial, String toSerial) throws Exception;

    public List<StockTransSerialDTO> getListSerialQuantity(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String status,
                                                           String tableName, String fromSerial, String toSerial) throws Exception;

    /**
     * ham xu ly tim kiem serial theo loai mat hang
     *
     * @param prodductOfferTypeId
     * @param serial
     * @param isSearchNew
     * @return
     * @throws Exception
     * @author thanhNT
     */
    public List<LookupSerialByFileDTO> searchSerialExact(Long prodductOfferTypeId, List<String> lsSerial, String serialGpon, boolean isSearchNew) throws Exception;

    /**
     * ham xu ly lay danh sach lich su serial
     *
     * @param serial
     * @param prodOfferId
     * @param fromDate
     * @param toDate
     * @param isOffline
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    public List<ViewSerialHistoryDTO> listLookUpSerialHistory(String serial, Long prodOfferId, Date fromDate, Date toDate, boolean isOffline) throws Exception;

    public List<ChangeDamagedProductDTO> getListSerialProduct(String serial, Long ownerType, Long ownerId) throws Exception;

    public List<StockTransSerialDTO> getListBySerial(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, String tableName) throws Exception;

    public List<StockTransSerialDTO> getListStockCardSaledBySerial(Long prodOfferId, String serial) throws Exception;

    public List<StockTransSerial> getStockTransSerialByStockTransId(Long stockTransId, Long prodOfferId, Long toOwnerType) throws Exception;

    public List<SmartPhoneDTO> getListIsdnMbccsByRangeIsdnOwnerId(Long prodOfferTypeId, String fromIsdn, String toIsdn,
                                                                  Long ownerId, Long ownerType, Long status, boolean isCTV, StaffDTO staffDTO) throws Exception;

    List<StockTransSerialDTO> getGatherSerial(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId) throws Exception;

    List<StockSerialDTO> getStockSerialInfomation(Long productOfferTypeId, String tableName, String serial) throws Exception;

    /**
     * so luong serial thuc te trong DB
     *
     * @param fromSerial
     * @param toSerial
     * @param prodOfferId
     * @param ownerId
     * @param ownerType
     * @param tableName
     * @param status
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<String> getListSerialExist(String fromSerial, String toSerial, Long prodOfferId, Long ownerId, Long ownerType, String tableName, Long status) throws Exception;

    public List<StockTransSerial> getSerialByStockTransId(Long stockTransId) throws Exception;

    public int updateStockBalance(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String strFromSerial, String strToSerial, Long stockTransId,Long newStatus, Long oldStatus) throws Exception;

    public List<StockDeviceDTO> getLstStockDevice(Long stockTransActionId) throws LogicException, Exception;

    public List<StockDeviceTransferDTO> getLstDeviceTransfer(Long prodOfferId, Long stateId, Long stockRequestId) throws LogicException, Exception;
}
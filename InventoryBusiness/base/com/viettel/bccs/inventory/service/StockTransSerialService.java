package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@WebService
public interface StockTransSerialService {

    @WebMethod
    public StockTransSerialDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockTransSerialDTO save(StockTransSerialDTO stockTransSerialDTO) throws Exception;

    @WebMethod
    public List<StockTransSerialDTO> findByStockTransDetailId(Long stockTransDetaiId) throws LogicException, Exception;

    @WebMethod
    public List<StockTransSerialDTO> findByStockTransId(Long stockTransId) throws LogicException, Exception;

    /**
     * @param stockActionIds danh sach actionId can xem chi tiet serial
     * @return danh sach serial tuong ung hoac exception
     * @throws LogicException
     * @throws Exception
     * @author LuanNT23
     */
    @WebMethod
    public List<StockTransSerialDTO> findStockTransSerial(List<Long> stockActionIds) throws LogicException, Exception;

    /**
     * @param stockTransSerialDTO
     * @return
     * @throws LogicException
     * @throws Exception
     */
    public List<StockTransSerialDTO> findStockTransSerialByDTO(StockTransSerialDTO stockTransSerialDTO) throws LogicException, Exception;

    /**
     * get danh sach serial
     *
     * @param ownerType
     * @param ownerId
     * @param productOfferId
     * @param stateId
     * @param channelTypeId
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    public List<StockTransSerialDTO> getRangeSerial(Long ownerType, Long ownerId, Long productOfferId, String tableName, Long stateId, String channelTypeId, Long serialStatus) throws Exception;

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
    List<StockTransSerialDTO> getRangeSerialFifo(Long ownerType, Long ownerId, Long productOfferId, String tableName, Long stateId, String channelTypeId, Long serialStatus) throws Exception;

    /**
     * ham tra ve danh sach serial hop le trong DB
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
    public List<String> getListSerialValid(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId, String fromSerial, String toSerial, Long rowNum, Long serialStatus) throws Exception;


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
    public List<String> getRangeSerialStockCardValid(Long ownerType, Long ownerId, Long productOfferId, Long stateId,
                                                     String fromSerial, String toSerial, Long quantity, String status) throws Exception;

    /**
     * ham tra ve dai serial thoa man dieu kien
     *
     * @param stockTransFullDTO
     * @return
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT
     */
    public List<StockTransSerialDTO> getListSerialSelect(StockTransFullDTO stockTransFullDTO) throws LogicException, Exception;

    /**
     * ham xu ly ghep dai serial, dau vao la chuoi serial , da ra la danh sach serial
     *
     * @param listSerials
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    public List<StockTransSerialDTO> getListSerialGroup(List<String> listSerials) throws Exception;

    @WebMethod
    public List<StockTransSerialDTO> findStockTransSerialByProductOfferType(WareHouseInfoDTO infoDTO) throws Exception;

    /**
     * ham tra ve danh sach list serial hop le(list dai serial)
     *
     * @param stockTransFullDTO
     * @param stockTransSerialDTOs
     * @return
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT77
     */

    @WebMethod
    public List<StockTransSerialDTO> getListSerialFromTable(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName) throws LogicException, Exception;

    @WebMethod
    public List<StockInspectCheckDTO> getListGatherSerial(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType,
                                                          Long stateId, String tableName, String fromSerial, String toSerial) throws LogicException, Exception;

    @WebMethod
    public List<StockInspectCheckDTO> getListSerialFromList(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType,
                                                            Long stateId, String tableName, List<String> serial2D) throws LogicException, Exception;

    @WebMethod
    public int updateStatusStockSerial(Long ownerId, Long ownerType, Long prodOfferId, String fromSerial, String toSerial, Long newStatus, Long oldStatus) throws Exception;

    @WebMethod
    public Long getDepostiPriceFromStockX(Long ownerId, Long ownerType, Long prodOfferId, String serial) throws Exception;

    @WebMethod
    public Long getDepostiPriceForRetrieve(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String serial) throws Exception;

    @WebMethod
    public Long getDepostiPriceByRangeSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String fromSerial, String toSerial) throws Exception;

    @WebMethod
    public List<StockTransSerialDTO> getListSerialQuantity(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String status,
                                                           String tableName, String fromSerial, String toSerial) throws LogicException, Exception;

    /**
     * ham xu ly tim kiem serial theo loai mat hang
     *
     * @param productOfferTypeId
     * @param serial
     * @param isSearchNew
     * @return
     * @throws Exception
     * @author thanhNT
     */
    public List<LookupSerialByFileDTO> searchSerialExact(Long productOfferTypeId, List<String> serial, String serialGpon, boolean isSearchNew) throws Exception;

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

    @WebMethod
    public List<ChangeDamagedProductDTO> getListSerialProduct(String serial, Long ownerType, Long ownerId) throws LogicException, Exception;

    @WebMethod
    public List<StockTransSerialDTO> getListBySerial(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, String tableName) throws LogicException, Exception;

    @WebMethod
    public List<StockTransSerialDTO> getListStockCardSaledBySerial(Long prodOfferId, String serial) throws LogicException, Exception;

    public List<StockTransSerialDTO> getStockTransSerialByStockTransId(Long stockTransId, Long prodOfferId, Long toOwnerType) throws Exception;

    @WebMethod
    public int updateStockBalance(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String strFromSerial, String strToSerial, Long stockTransId, Long newStatus, Long oldStatus) throws Exception;

    public List<StockDeviceDTO> getLstStockDevice(Long stockTransActionId) throws LogicException, Exception;

    public List<StockDeviceTransferDTO> getLstDeviceTransfer(Long prodOfferId, Long stateId, Long stockRequestId) throws LogicException, Exception;
}
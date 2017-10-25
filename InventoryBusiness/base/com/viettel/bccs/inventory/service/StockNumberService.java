package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockNumber;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


@WebService
public interface StockNumberService {

    @WebMethod
    public StockNumberDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockNumberDTO> findAll() throws Exception;

    @WebMethod
    public List<StockNumberDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockNumberDTO productSpecCharacterDTO) throws LogicException, Exception;

    @WebMethod
    public BaseMessage update(StockNumberDTO productSpecCharacterDTO) throws LogicException, Exception;

    @WebMethod
    public NumberActionDTO insertBatch(SearchNumberRangeDTO searchDTO) throws LogicException, Exception;

    @WebMethod
    public boolean checkExistNumberByListFilterRuleId(List<Long> listFilterRuleId) throws Exception;

    //anhnv33 loc so
    @WebMethod
    public int updateRuleForRefilter(BigDecimal startNumber, BigDecimal endNumber, List<Long> lstSelectedRule) throws Exception;

    @WebMethod
    public List<StockNumberDTO> getListNumberFilter(Long telecomServiceId, BigDecimal startNumber, BigDecimal endNumber, Long minNumber, Long ownerId, List<String> lstStatus) throws Exception;
    //anhnv33 end loc so

    @WebMethod
    public List<TableNumberRangeDTO> getListRangeGrant(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception;

    @WebMethod
    public List<TableNumberRangeDTO> getListRangeGrantFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception;

    @WebMethod
    public List<String> checkListStockNumber(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception;

    @WebMethod
    public long updateRangeGrant(List<TableNumberRangeDTO> listRangeUpdate, StaffDTO staff, String ip, SearchNumberRangeDTO searchNumberRangeDTO) throws LogicException, Exception;

    @WebMethod
    public List<TableNumberRangeDTO> previewFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception, LogicException;

    @WebMethod
    public List<TableNumberRangeDTO> previewFromRange(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception, LogicException;

    @WebMethod
    public long updateStockNumberByProOfferId(List<TableNumberRangeDTO> listRangeUpdate, String staffCode, String ip, ProductOfferingTotalDTO productOfferingTotalDTO, boolean isFromFile) throws LogicException, Exception;

    @WebMethod
    public List<Object[]> previewDistriButeNumber(InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public String distriButeNumber(List<Object[]> listRangePreviewed, InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public List<DeleteNumberDTO> previewDeleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO deleteStockNumberDTO) throws LogicException, Exception;

    @WebMethod
    public List<DeleteNumberDTO> deleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO dto, String user, String ip) throws LogicException, Exception;

    @WebMethod
    public List<TransferNGNDTO> previewTransfer(List<TransferNGNDTO> lstDelete, TransferNGNDTO dto) throws LogicException, Exception;

    @WebMethod
    public List<TransferNGNDTO> transferNGN(List<TransferNGNDTO> lstDelete, TransferNGNDTO dto, String userCreate, String userIpCreate) throws LogicException, Exception;

    //anhnv33 start Phan phoi so
    @WebMethod
    public List<StockTransSerialDTO> findRangeForExportNote(String fromIsdn, String toIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception;

    public List<StockTransSerialDTO> findByListIsdn(List<String> lstIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception;

    //anhnv33 end Phan phoi so
    @WebMethod
    public HashMap<String, String> checkIsdnBeforeDistribute(List<String[]> listIsdn, String currentOwnerId, String currentShopPath, String telecomServiceId, RequiredRoleMap requiredRoleMap);

    @WebMethod
    public List<Object[]> previewDistriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public HashMap<String, String> distriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public List<IsdnViewDetailDTO> searchIsdn(InfoSearchIsdnDTO infoSearch, int rowNum, int firstRow) throws LogicException, Exception;

    @WebMethod
    public List<InfoStockIsdnDto> viewInfoStockIsdn(Long ownerId, Long ownerType) throws Exception;

    public int revokeNumber(List<Long> lstRevokeShopId, Long day, List<Long> lstRevokeShopReuseId, String updateUser, String updateIp) throws LogicException, Exception;

    public int revokeNumberOwner(Long shopId, Long day, Long revokeShopId, String updateUser) throws LogicException, Exception;

    public int cancelRequestVTShop() throws LogicException, Exception;

    public void releaseNumber(StockNumberDTO stockNumberDTO) throws Exception;

    public List<StockNumberDTO> getAllListReleaseNumber(Double keepTimeHour, List<Long> lstShopId) throws Exception;

    public List<Long> getLstShopIdFromOptionSet(String code) throws Exception;


    /**
     * ham dem so thue bao bi lock giu so theo id nhan vien
     * @author thanhnt77
     * @param staffId
     * @return
     * @throws Exception
     */
    public Long countLockIsdnByStaffId(Long staffId, List<Long> lsShopId) throws Exception;

    /**
     * ham unlock isdn
     * @author thanhnt77
     * @param staffId
     * @param isdn
     * @param ipAddress
     * @return
     * @throws LogicException
     * @throws Exception
     */
    public String unlockIsdnByStaff(Long staffId, String isdn, String ipAddress) throws LogicException, Exception;

    /**
     * ham lock giu so isdn
     * @author thanhnt77
     * @param staffId
     * @param isdn
     * @param ipAddress
     * @param requiredRoleMap
     * @throws LogicException
     * @throws Exception
     */
    public String lockIsdnByStaff(Long staffId, String isdn, String ipAddress, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    /**
     * ham lock giu so đep isdn
     * @author thanhnt77
     * @param staffId
     * @param isdn
     * @param ipAddress
     * @throws LogicException
     * @throws Exception
     */
    public String lockNiceIsdnByStaff(Long staffId, String isdn, String ipAddress) throws LogicException, Exception;

    /**
     * ham tim kiem so theo isdn
     * @author thanhnt77
     * @param isdn
     * @return
     * @throws Exception
     */
    public StockNumber findStockNumberByIsdn(String isdn) throws Exception;
}
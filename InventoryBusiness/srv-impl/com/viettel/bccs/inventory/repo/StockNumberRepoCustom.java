package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockNumber;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public interface StockNumberRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<TableNumberRangeDTO> getListRangeGrant(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception;

    public List<TableNumberRangeDTO> getListRangeGrantFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception;

    public long updateRangeGrant(List<TableNumberRangeDTO> listRangeUpdate, StaffDTO staff, String ip, SearchNumberRangeDTO searchNumberRangeDTO) throws LogicException, Exception;

    //chungnv len danh sach cap nhat trang thai
    public List<String> checkListStockNumber(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception;

    //anhnv33 loc so
    public List<StockNumberDTO> getListNumberFilter(Long telecomServiceId, BigDecimal startNumber, BigDecimal endNumber, Long minNumber, Long ownerId, List<String> lstStatus) throws Exception;
    //anhnv33 end loc so

    public List<TableNumberRangeDTO> previewFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception;

    public List<TableNumberRangeDTO> previewFromRange(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception;

    public long updateStockNumberByProOfferId(List<TableNumberRangeDTO> listRangeUpdate, String staffCode, String ip, Date currentDate, ProductOfferingTotalDTO productOfferingTotalDTO, boolean isFromFile) throws LogicException, Exception;

    public List<Object[]> previewDistriButeNumber(InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    public String distriButeNumber(List<Object[]> listRangePreviewed, InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    //chungnv add deleteNumber
    public List<DeleteNumberDTO> previewDeleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO deleteStockNumberDTO) throws LogicException, Exception;

    public List<DeleteNumberDTO> deleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO dto, String user, String ip) throws LogicException, Exception;

    public List<TransferNGNDTO> previewTransfer(List<TransferNGNDTO> lstDelete, TransferNGNDTO dto) throws LogicException, Exception;

    public List<TransferNGNDTO> transferNGN(List<TransferNGNDTO> lstDelete, TransferNGNDTO dto, String userCreate, String userIpCreate) throws LogicException, Exception;

    //anhnv33 start phan phoi so
    public List<StockTransSerialDTO> findRangeForExportNote(String fromIsdn, String toIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception;

    public HashMap<String, String> checkIsdnBeforeDistribute(List<String[]> listIsdn, String currentOwnerId, String currentShopPath, String telecomServiceId, RequiredRoleMap requiredRoleMap);

    public List<StockTransSerialDTO> findByListIsdn(List<String> lstIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception;

    //anhnv33 end phan phoi so
    public List<Object[]> previewDistriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    public HashMap<String, String> distriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    public List<IsdnViewDetailDTO> searchIsdn(InfoSearchIsdnDTO infoSearch, int rowNum, int firstRow) throws LogicException, Exception;

    public List<InfoStockIsdnDto> viewInfoStockIsdn(Long ownerId, Long ownerType) throws Exception;

    public boolean checkValidIsdn(String isdn) throws Exception;

    public void lockOrUnlockIsdn(String isdn, Long newStatus, List<Long> lstStatus, StaffDTO staff) throws LogicException, Exception;

    public List<String> lookupIsdn(String fromIsdn, String toIsdn, List<Long> lsOwnerId, List<String> lsStatus, Long maxRow) throws Exception;

    public List<Long> getListNumberFromRevokeShop(List<Long> lstOwnerId, Long day) throws Exception;

    public Long updateNumberForReuse(List<Long> lstNumberId, List<Long> lstRevokeShopReuseId, String updateUser, String updateIp, Connection conn) throws Exception;

    public List<Long> getListNumberOwnerTDN(Long shopId, Long day) throws Exception;

    public Long updateNumberRevokeOwner(List<Long> lstNumberId, Long shopId, String updateUser, Connection conn) throws Exception;

    public List<StockIsdnVtShopDTO> getLstIsdnExpriedVtShop() throws Exception;

    public Long updateIsdnExpriedVtShop(List<StockIsdnVtShopDTO> lstIsdn) throws Exception;

    public Long unlockIsdnExpriedVtShopIM1(List<StockIsdnVtShopDTO> lstIsdn) throws Exception;

    public Long unlockIsdnExpriedVtShop(List<StockIsdnVtShopDTO> lstIsdn) throws Exception;

    public List<StockNumberDTO> getAllReleaseNumber(Double keepTimeHour, List<Long> lstShopId) throws Exception;

    public List<Long> getLstShopIdFromOptionSet(String code) throws Exception;

    /**
     * ham dem so thue bao bi lock giu so theo id nhan vien
     * @author thanhnt77
     * @param staffId
     * @param lstShopId
     * @return
     * @throws Exception
     */
    public Long countLockIsdnByStaffId(Long staffId, List<Long> lstShopId) throws Exception;

    /**
     * ham tim kiem so theo isdn
     * @author thanhnt77
     * @param isdn
     * @return
     * @throws Exception
     */
    public StockNumber findStockNumberByIsdn(String isdn) throws Exception;

    /**
     * ham tra ve cau hinh so luong so dep cua nhan vien
     * @author thanhnt77
     * @param staffCode
     * @param type
     * @return
     * @throws Exception
     */
    public Long getConfigQuantityNiceNumberByStaff(String staffCode, String type) throws Exception;

    boolean checkIsdnHaveLockedByStaffId(String isdn, String userCreate) throws Exception;
}
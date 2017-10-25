package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockNumber;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service("WsStockNumberServiceImpl")
public class WsStockNumberServiceImpl implements StockNumberService {

    public static final Logger logger = Logger.getLogger(WsStockNumberServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockNumberService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockNumberService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockNumberDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockNumberDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockNumberDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(StockNumberDTO productSpecCharacterDTO) throws LogicException, Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(StockNumberDTO productSpecCharacterDTO) throws LogicException, Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public NumberActionDTO insertBatch(SearchNumberRangeDTO searchDTO) throws LogicException, Exception {
        return client.insertBatch(searchDTO);
    }

    @Override
    @WebMethod
    public boolean checkExistNumberByListFilterRuleId(List<Long> listFilterRuleId) throws Exception {
        return client.checkExistNumberByListFilterRuleId(listFilterRuleId);
    }

    @Override
    @WebMethod
    public int updateRuleForRefilter(BigDecimal startNumber, BigDecimal endNumber, List<Long> lstSelectedRule) throws Exception {
        return client.updateRuleForRefilter(startNumber, endNumber, lstSelectedRule);
    }

    @Override
    @WebMethod
    public List<StockNumberDTO> getListNumberFilter(Long telecomServiceId, BigDecimal startNumber, BigDecimal endNumber, Long minNumber, Long ownerId, List<String> lstStatus) throws Exception {
        return client.getListNumberFilter(telecomServiceId, startNumber, endNumber, minNumber, ownerId, lstStatus);
    }

    @Override
    @WebMethod
    public List<TableNumberRangeDTO> getListRangeGrant(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        return client.getListRangeGrant(searchNumberRangeDTO);
    }

    @Override
    @WebMethod
    public List<TableNumberRangeDTO> getListRangeGrantFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        return client.getListRangeGrantFromFile(searchNumberRangeDTO);
    }

    @Override
    @WebMethod
    public long updateRangeGrant(List<TableNumberRangeDTO> listRangeUpdate, StaffDTO staff, String ip, SearchNumberRangeDTO searchNumberRangeDTO) throws LogicException, Exception {
        return client.updateRangeGrant(listRangeUpdate, staff, ip, searchNumberRangeDTO);
    }

    @Override
    @WebMethod
    public List<TableNumberRangeDTO> previewFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception, LogicException {
        return client.previewFromFile(searchNumberRangeDTO);
    }

    @Override
    @WebMethod
    public List<TableNumberRangeDTO> previewFromRange(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception, LogicException {
        return client.previewFromRange(searchNumberRangeDTO);
    }

    @Override
    @WebMethod
    public List<String> checkListStockNumber(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        return client.checkListStockNumber(searchNumberRangeDTO);
    }

    @Override
    @WebMethod
    public long updateStockNumberByProOfferId(List<TableNumberRangeDTO> listRangeUpdate, String staffCode, String ip, ProductOfferingTotalDTO productOfferingTotalDTO, boolean isFromFile) throws LogicException, Exception {
        return client.updateStockNumberByProOfferId(listRangeUpdate, staffCode, ip, productOfferingTotalDTO, isFromFile);
    }

    @Override
    @WebMethod
    public String distriButeNumber(List<Object[]> listRangePreviewed, InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.distriButeNumber(listRangePreviewed, inputByRange, requiredRoleMap);
    }

    @Override
    @WebMethod
    public List<Object[]> previewDistriButeNumber(InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.previewDistriButeNumber(inputByRange, requiredRoleMap);
    }

//    @Override
//    @WebMethod
//    public HashMap<String, String> previewDistriButeNumberByFile(String telecomServiceId, byte[] byteContent, String fromOwnerId, String fromOwnerType, String stockType, String fromOwnerPath) throws LogicException, Exception {
//        return client.previewDistriButeNumberByFile(telecomServiceId, byteContent, fromOwnerId, fromOwnerType, stockType, fromOwnerPath);
//    }
//
//    @Override
//    @WebMethod
//    public HashMap<String, String> distriButeNumberByFile(String telecomServiceId, byte[] byteContent, String stockType, Long reasonId, String notes, String userCreate, String userIpCreate, String fromOwnerId, String fromOwnerType, String fromOwnerCode, String fromOwnerPath) throws LogicException, Exception {
//        return client.distriButeNumberByFile(telecomServiceId, byteContent, stockType, reasonId, notes, userCreate, userIpCreate, fromOwnerId, fromOwnerType, fromOwnerCode, fromOwnerPath);
//    }

    @Override
    @WebMethod
    public List<DeleteNumberDTO> previewDeleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO deleteStockNumberDTO) throws LogicException, Exception {
        return client.previewDeleteNumber(lstDelete, deleteStockNumberDTO);
    }

    @Override
    @WebMethod
    public List<DeleteNumberDTO> deleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO dto, String user, String ip) throws LogicException, Exception {
        return client.deleteNumber(lstDelete, dto, user, ip);
    }

    @Override
    @WebMethod
    public List<TransferNGNDTO> previewTransfer(List<TransferNGNDTO> lstDelete, TransferNGNDTO dto) throws LogicException, Exception {
        return client.previewTransfer(lstDelete, dto);
    }

    @Override
    @WebMethod
    public List<TransferNGNDTO> transferNGN(List<TransferNGNDTO> lstDelete, TransferNGNDTO dto, String userCreate, String userIpCreate) throws LogicException, Exception {
        return client.transferNGN(lstDelete, dto, userCreate, userIpCreate);
    }

    @Override
    @WebMethod
    public List<StockTransSerialDTO> findRangeForExportNote(String fromIsdn, String toIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception {
        return client.findRangeForExportNote(fromIsdn, toIsdn, ownerType, ownerId, prodOfferId);
    }

    @Override
    public HashMap<String, String> checkIsdnBeforeDistribute(List<String[]> listIsdn, String fromOwnerId, String fromOwnerType, String telecomServiceId, RequiredRoleMap requiredRoleMap) {
        return client.checkIsdnBeforeDistribute(listIsdn, fromOwnerId, fromOwnerType, telecomServiceId, requiredRoleMap);
    }

    @Override
    @WebMethod
    public List<Object[]> previewDistriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.previewDistriButeNumberByFile(listIsdn, inputByFile, requiredRoleMap);
    }

    @Override
    @WebMethod
    public HashMap<String, String> distriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.distriButeNumberByFile(listIsdn, inputByFile, requiredRoleMap);
    }

    @WebMethod
    public List<StockTransSerialDTO> findByListIsdn(List<String> lstIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception {
        return client.findByListIsdn(lstIsdn, ownerType, ownerId, prodOfferId);
    }

    @Override
    @WebMethod
    public List<IsdnViewDetailDTO> searchIsdn(InfoSearchIsdnDTO infoSearch, int rowNum, int firstRow) throws LogicException, Exception {
        return client.searchIsdn(infoSearch, rowNum, firstRow);
    }

    @Override
    @WebMethod
    public List<InfoStockIsdnDto> viewInfoStockIsdn(Long ownerId, Long ownerType) throws Exception {
        return client.viewInfoStockIsdn(ownerId, ownerType);
    }

    @Override
    public int cancelRequestVTShop() throws LogicException, Exception {
        return client.cancelRequestVTShop();
    }

    @Override
    public int revokeNumberOwner(Long shopId, Long day, Long revokeShopId, String updateUser) throws LogicException, Exception {
        return client.revokeNumberOwner(shopId, day, revokeShopId, updateUser);
    }

    @Override
    public int revokeNumber(List<Long> lstRevokeShopId, Long day, List<Long> lstRevokeShopReuseId, String updateUser, String updateIp) throws LogicException, Exception {
        return client.revokeNumber(lstRevokeShopId, day, lstRevokeShopReuseId, updateUser, updateIp);
    }

    @Override
    public void releaseNumber(StockNumberDTO stockNumberDTO) throws Exception {
        client.releaseNumber(stockNumberDTO);
    }

    @Override
    public List<Long> getLstShopIdFromOptionSet(String code) throws Exception {
        return client.getLstShopIdFromOptionSet(code);
    }

    @Override
    public List<StockNumberDTO> getAllListReleaseNumber(Double keepTimeHour, List<Long> lstShopId) throws Exception{
        return client.getAllListReleaseNumber(keepTimeHour, lstShopId);
    }

    @Override
    public Long countLockIsdnByStaffId(Long staffId, List<Long> lstShopId) throws Exception {
        return client.countLockIsdnByStaffId(staffId, lstShopId);
    }

    @Override
    public String unlockIsdnByStaff(Long staffId, String isdn, String ipAddress) throws LogicException, Exception {
        return client.unlockIsdnByStaff(staffId, isdn, ipAddress);
    }

    @Override
    public String lockIsdnByStaff(Long staffId, String isdn, String ipAddress, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return client.lockIsdnByStaff(staffId, isdn, ipAddress, requiredRoleMap);
    }

    @Override
    public String lockNiceIsdnByStaff(Long staffId, String isdn, String ipAddress) throws LogicException, Exception {
        return client.lockNiceIsdnByStaff(staffId, isdn, ipAddress);
    }

    @Override
    public StockNumber findStockNumberByIsdn(String isdn) throws Exception {
        return client.findStockNumberByIsdn(isdn);
    }
}
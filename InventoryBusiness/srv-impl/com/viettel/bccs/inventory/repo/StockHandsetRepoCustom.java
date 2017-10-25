package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockHandset;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public interface StockHandsetRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public Date findExportDateBySerial(Long productOfferId, String serial) throws Exception, LogicException;

    public BaseMessage updateUctt(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException;

    public BaseMessage updateUcttIm1(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException;

    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                 FlagStockDTO flagStockDTO) throws Exception, LogicException;

    public int updateForStockRescue(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception;

    public int updateForStockRescueIM1(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception;

    public int updateStockHandsetIM1(Long stateIdAf, Long ownerIdAf, Long ownerTypeAf, Long statusAf, Long stateId, Long ownerId, Long ownerType, Long status, String serial, Long prodOfferId) throws Exception;

    public boolean updateSerialGpon(String serial, String stockGPon, Long prodOfferId) throws Exception;

    public boolean updateSerialGponIM1(String serial, String stockGPon, Long stockModelId) throws Exception;

    public boolean updateSerialMultiScreen(String serial, String cadId, String macId, Long prodOfferId) throws Exception;

    public boolean updateSerialMultiScreenIM1(String serial, String cadId, String macId, Long stockModelId) throws Exception;

    public StockHandsetDTO checkExsitStockHandset(Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, String updateType) throws LogicException, Exception;

    public int updateListLicenseKey(Connection conn, List<UpdateLicenseDTO> lstUpdateLicenseDTOs, Long ownerId, Long prodOfferId, String updateType) throws LogicException, Exception;

    public StockHandsetDTO getStockHandset(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long stateId) throws LogicException, Exception;


    public Long getQuantityStockX(Long ownerId, Long ownerType, Long prodOfferId, List<Serial> lstSerial, Long status, Long stateId) throws LogicException, Exception;

    public StockHandsetDTO getStockModelSoPin(String serial);

    public boolean checkExistSerial(String serial);

    public boolean checkUnlockSerial(String serial);

    public Object[] getInfomationSerial(String serial);

    public boolean saveUnlockG6(String serial, Long prodOfferId);

    public void insertStockHandset(StockHandsetDTO stockHandsetDTO) throws LogicException, Exception;

    public int deleteStockHandset(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, Long stateId) throws LogicException, Exception;

    public int deleteStockHandsetIM1(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, Long stateId) throws LogicException, Exception;

    public StockHandset findBySerialForUpdate(String serial, Long prodOfferId, Long ownerId) throws Exception;

    public StampDTO getStampInformation(Long prodOfferId, String fromSerial);
}
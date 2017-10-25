package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface StockHandsetService {

    @WebMethod
    public StockHandsetDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockHandsetDTO> findByFilter(List<FilterRequest> filters) throws Exception;


    /**
     * Ham kiem tra mat hang co serial hang thu hoi co trang thai ban khong (ban: status!=0)
     *
     * @param productOfferingId
     * @param serialRecover
     * @param status
     * @return
     * @throws Exception
     */
    @WebMethod
    public StockHandsetDTO checkSerialSale(Long productOfferingId, String serialRecover, String status) throws Exception;

    @WebMethod
    public StockHandsetDTO getStockHandsetById(Long productOfferingId) throws Exception;

    /**
     * Ham cap nhat tu mat hang thu hoi sang UCTT state_id -->5
     *
     * @param stockHandsetDTO
     * @return
     * @throws Exception
     */
    @WebMethod
    public BaseMessage update(StockHandsetDTO stockHandsetDTO) throws Exception;

    @WebMethod
    public StockHandsetDTO create(StockHandsetDTO stockHandsetDTO) throws Exception;

    @WebMethod
    public BaseMessage updateUctt(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException;

    @WebMethod
    public BaseMessage updateUcttIm1(Long productOfferId, String serialRecover, Long ownerId, Long ownerType, Long productOfferIdUCTT, Date updateDatetime) throws Exception, LogicException;

    @WebMethod
    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                 FlagStockDTO flagStockDTO) throws LogicException, Exception;

    @WebMethod
    public int updateForStockRescue(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception;

    @WebMethod
    public int updateForStockRescueIM1(Long stateId, Long ownerId, String serial, Long prodOfferId) throws Exception;

    @WebMethod
    public List<UpdateSerialGponDTO> updateSerialGpon(String updateType, List<UpdateSerialGponDTO> lstSerial) throws LogicException, Exception;

    @WebMethod
    public StockHandsetDTO updateLicenseKey(UpdateLicenseDTO updateLicenseDTO) throws LogicException, Exception;

    @WebMethod
    public StockHandsetDTO checkExsitStockHandset(Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, String updateType) throws LogicException, Exception;

    @WebMethod
    public int updateListLicenseKey(List<UpdateLicenseDTO> lstUpdateLicenseDTOs, Long ownerId, Long prodOfferId, String updateType) throws LogicException, Exception;

    public List<StockHandsetDTO> findStockHandSet(Long stockHandsetId, Long prodOfferId, String serial, Long ownerId, Long ownerType, Long stateId, Long status) throws LogicException, Exception;

    public Date findExportDateBySerial(String productOfferCode, String serial) throws Exception, LogicException;

    @WebMethod
    public Long getQuantityStockX(Long ownerId, Long ownerType, Long prodOfferId, List<Serial> lstSerial, Long status, Long stateId) throws LogicException, Exception;

    public StockHandsetDTO getStockModelSoPin(String serial);

    public boolean checkExistSerial(String serial);

    public boolean checkUnlockSerial(String serial);

    public Object[] getInfomationSerial(String serial);

    public boolean saveUnlockG6(String serial, Long prodOfferId);

    public StockHandsetDTO findStockHandsetByProdIdAndSerial(Long prodOfferId, String serial) throws Exception;

}
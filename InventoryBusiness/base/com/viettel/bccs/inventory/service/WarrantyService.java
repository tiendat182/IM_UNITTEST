package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImpExpStockDTO;
import com.viettel.bccs.inventory.message.BaseWarrantyMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface WarrantyService {


    /**
     * ham tra cuu serial thiet bi
     *
     * @param stockHandsetId
     * @param prodOfferId
     * @param serial
     * @param ownerId
     * @param ownerType
     * @param stateId
     * @param status
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage findStockHandSet(@WebParam(name = "stockHandsetId") Long stockHandsetId, @WebParam(name = "prodOfferId") Long prodOfferId,
                                                @WebParam(name = "serial") String serial, @WebParam(name = "ownerId") Long ownerId,
                                                @WebParam(name = "ownerType") Long ownerType, @WebParam(name = "stateId") Long stateId,
                                                @WebParam(name = "status") Long status);

    @WebMethod
    public Date findExportDateBySerial(@WebParam(name = "productOfferCode") String prodOfferCode, @WebParam(name = "serial") String serial);

    /**
     * ham tra cuu kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param stateId
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage viewStockDetail(@WebParam(name = "ownerId") Long ownerId, @WebParam(name = "ownerType") Long ownerType,
                                               @WebParam(name = "prodOfferId") Long prodOfferId, @WebParam(name = "stateId") Long stateId);


    /**
     * ham xuat kho
     *
     * @param listSerial
     * @param fromOwnerId
     * @param fromOwnerType
     * @param toOwnerId
     * @param toOwnerType
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage exportStock(@WebParam(name = "listSerial") List<ImpExpStockDTO> listSerial, @WebParam(name = "fromOwnerId") Long fromOwnerId, @WebParam(name = "fromOwnerType") Long fromOwnerType,
                                           @WebParam(name = "toOwnerId") Long toOwnerId, @WebParam(name = "toOwnerType") Long toOwnerType);

    /**
     * ham nhap kho
     *
     * @param listSerial
     * @param fromOwnerId
     * @param fromOwnerType
     * @param toOwnerId
     * @param toOwnerType
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage impportStock(@WebParam(name = "listSerial") List<ImpExpStockDTO> listSerial, @WebParam(name = "fromOwnerId") Long fromOwnerId,
                                            @WebParam(name = "fromOwnerType") Long fromOwnerType, @WebParam(name = "toOwnerId") Long toOwnerId, @WebParam(name = "toOwnerType") Long toOwnerType);

    /**
     * ham xuat nhap kho cho don vi/nhan vien
     *
     * @param listSerial
     * @param fromOwnerId
     * @param fromOwnerType
     * @param toOwnerId
     * @param toOwnerType
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage exportImportStock(@WebParam(name = "listSerial") List<ImpExpStockDTO> listSerial, @WebParam(name = "fromOwnerId") Long fromOwnerId,
                                                 @WebParam(name = "fromOwnerType") Long fromOwnerType, @WebParam(name = "toOwnerId") Long toOwnerId,
                                                 @WebParam(name = "toOwnerType") Long toOwnerType, @WebParam(name = "staffCode") String staffCode);

    /**
     * ham xuat kho co doi trang thai id
     *
     * @param listSerial
     * @param fromOwnerId
     * @param fromOwnerType
     * @param toOwnerId
     * @param toOwnerType
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage exportStockWarranty(@WebParam(name = "listSerial") List<ImpExpStockDTO> listSerial, @WebParam(name = "newStateId") Long newStateId, @WebParam(name = "fromOwnerId") Long fromOwnerId,
                                                   @WebParam(name = "fromOwnerType") Long fromOwnerType, @WebParam(name = "toOwnerId") Long toOwnerId, @WebParam(name = "toOwnerType") Long toOwnerType);

    /**
     * nhap kho mat hang gpon
     *
     * @param listSerial
     * @param fromOwnerId
     * @param fromOwnerType
     * @param toOwnerId
     * @param toOwnerType
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage importStockGPON(@WebParam(name = "listSerial") List<ImpExpStockDTO> listSerial, @WebParam(name = "fromOwnerId") Long fromOwnerId,
                                               @WebParam(name = "fromOwnerType") Long fromOwnerType, @WebParam(name = "toOwnerId") Long toOwnerId, @WebParam(name = "toOwnerType") Long toOwnerType);

    /**
     * tra cuu log giao dich
     *
     * @param logId
     * @param methodName
     * @param paramsInput
     * @param responseCode
     * @param description
     * @param fromDate
     * @param endDate
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage getWarrantyStockLog(@WebParam(name = "logId") Long logId, @WebParam(name = "methodName") String methodName, @WebParam(name = "paramsInput") String paramsInput,
                                                   @WebParam(name = "responseCode") String responseCode, @WebParam(name = "description") String description, @WebParam(name = "fromDate") Date fromDate, @WebParam(name = "endDate") Date endDate);


    /**
     * ham view tra cuu cac mat hang bao hanh
     *
     * @param ownerType
     * @param ownerId
     * @param stockModelCode
     * @param stockModelName
     * @param stateId
     * @return
     * @author thanhnt77
     */
    @WebMethod
    public BaseWarrantyMessage viewStockModelWarranty(@WebParam(name = "ownerType") Long ownerType, @WebParam(name = "ownerId") Long ownerId,
                                                      @WebParam(name = "stockModelCode") String stockModelCode, @WebParam(name = "stockModelName") String stockModelName, @WebParam(name = "stateId") Long stateId);

    @WebMethod
    public BaseWarrantyMessage viewStockKV(@WebParam(name = "shopCode") String shopCode, @WebParam(name = "prodOfferId") Long prodOfferId, @WebParam(name = "stateId") Long stateId);

    @WebMethod
    public BaseWarrantyMessage getInventoryInfoByListProdCode(@WebParam(name = "listProdOfferCode") List<String> listProdOfferCode);

}
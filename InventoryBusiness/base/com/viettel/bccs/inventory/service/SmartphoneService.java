package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.ProductOfferingSM;
import com.viettel.bccs.inventory.model.StockNumber;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface SmartphoneService {

    @WebMethod
    public ImResult confirmNote(Long staffId, String expNoteCode, String stockTransId, Date createExpDate, String impNoteCode) throws LogicException, Exception;

    @WebMethod
    public List<StockTransActionDTO> getLstStockTrans(String toOwnerCode) throws LogicException, Exception;

    @WebMethod
    public List<StockTransDetailDTO> getLstStockTransDetail(Long stockTransId) throws LogicException, Exception;

    @WebMethod
    public List<StockTransSerialDTO> getLstStockTransSerial(Long stockTransId, Long prodOfferId) throws LogicException, Exception;

    @WebMethod
    public List<SmartPhoneDTO> getListStockByStaffCode(String staffCode) throws LogicException, Exception;

    @WebMethod
    public List<SmartPhoneDTO> getListIsdnMbccsByRangeIsdnOwnerId(Long productOfferTypeId, String fromIsdn, String toIsdn, Long ownerId, Long ownerType, Long status, String staffCode) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferingDTO> getStockStaffDetail(String staffCode, Long prodOfferId, Long prodOfferTypeId) throws LogicException, Exception;

    @WebMethod
    public BaseMessage staffExportStockToShop(@WebParam(name = "staffCode") String staffCode,
                                              @WebParam(name = "productOfferingDTOs") List<ProductOfferingSM> productOfferingDTOs,
                                              @WebParam(name = "isRoleStockNumShop") boolean isRoleStockNumShop) throws LogicException, Exception;

    @WebMethod
    public List<StockOrderDTO> getListStockOrder(String staffCode) throws LogicException, Exception;

    @WebMethod
    public List<StockOrderDetailDTO> getListStockOrderDetail(Long stockOrderId) throws LogicException, Exception;

    @WebMethod
    public BaseMessage approveOrder(Long shopId, Long staffId, String stockOrderCode, List<StockOrderDetailDTO> listApprove) throws LogicException, Exception;

    @WebMethod
    public BaseMessage refuseOrder(Long shopId, Long staffId, String stockOrderCode) throws LogicException, Exception;

    @WebMethod
    public BaseMessage requestOrder(Long shopId, Long staffId, List<StockOrderDetailDTO> listOrderDetail) throws LogicException, Exception;

    @WebMethod
    public BaseMessage cancelOrder(Long staffId) throws LogicException, Exception;

    /**
     * tim kiem kho lenh theo staffId
     *
     * @param staffId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    public StockOrderDTO getStockOrderStaff(Long staffId) throws Exception;

    @WebMethod
    public StockCollaboratorDTO exportStockCollaborator(@WebParam(name = "staffCode") String staffCode,
                                                        @WebParam(name = "collCode") String collCode,
                                                        @WebParam(name = "lstProductOfferingDTOs") List<ProductOfferingSM> lstProductOfferingDTOs) throws LogicException, Exception;

    @WebMethod
    public List<String> getListStaticIPProvince(@WebParam(name = "domain") String domain,
                                                @WebParam(name = "province") String province,
                                                @WebParam(name = "ipType") Long ipType) throws LogicException, Exception;

    @WebMethod
    public LookupIsdnDTO lookupIsdn(@WebParam(name = "fromIsdn") String fromIsdn,
                                    @WebParam(name = "toIsdn") String toIsdn,
                                    @WebParam(name = "stockCode") String stockCode,
                                    @WebParam(name = "maxRow") Long maxRow) throws LogicException, Exception;

    @WebMethod
    public List<StockSerialDTO> getStockSerialInfomation(@WebParam(name = "productOfferTypeId") Long productOfferTypeId,
                                                         @WebParam(name = "serial") String serial) throws LogicException, Exception;


    /**
     * ham gen,ma phieu xuat
     *
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    @WebMethod
    public String getTransCodeExportNote(String staffCode) throws Exception;

    /**
     * Ham tra ve danh sanh reasonDTO phu thuoc vao reasonType
     *
     * @param reasonType
     * @return
     * @author ThanhNT
     */
    @WebMethod
    public List<ReasonDTO> getLsReasonByType(@WebParam(name = "reasonType") String reasonType) throws LogicException, Exception;

    /**
     * Ham tra ve danh sanh luong trinh ky theo shopId
     *
     * @param shopId
     * @return
     * @throws Exception
     * @author ThanhNT
     */
    @WebMethod
    public List<SignFlowDTO> getSignFlowByShop(@WebParam(name = "shopId") Long shopId) throws Exception;

    /**
     * ham tim kiem
     *
     * @param signFlowId
     * @return
     * @throws Exception
     * @author Thetm1
     */
    @WebMethod
    public List<SignFlowDetailDTO> findBySignFlowId(@WebParam(name = "signFlowId") Long signFlowId) throws Exception;

    /**
     * danh sach nhan vien nhan
     *
     * @return
     * @throws LogicException
     * @throws Exception
     * @shopId shopId cua user dang nhap
     */
    @WebMethod
    public List<VShopStaffDTO> getListStaffSearch(@WebParam(name = "inputStaff") String inputStaff,
                                                  @WebParam(name = "shopId") Long shopId) throws LogicException, Exception;

    /**
     * danh sach cua hang
     *
     * @param inputSearch
     * @param shopId
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    public List<VShopStaffDTO> getListShopSearch(@WebParam(name = "inputSearch") String inputSearch,
                                                 @WebParam(name = "shopId") Long shopId) throws LogicException, Exception;

    /**
     * ham danh sach mat hang phu thuoc vao user dang nhap
     *
     * @param input
     * @param stateId
     * @param ownerType
     * @param ownerId
     * @return
     * @author thanhnt77
     */
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTO(@WebParam(name = "input") String input,
                                                                 @WebParam(name = "stateId") String stateId,
                                                                 @WebParam(name = "ownerType") String ownerType,
                                                                 @WebParam(name = "ownerId") Long ownerId) throws Exception;

    /**
     * ham lap lenh xuat kho
     *
     * @param stockTransDTO
     * @param stockTransActionDTO
     * @param lstStockTransDetail
     * @param requiredRoleMap
     * @return
     * @author thanhnt77
     */
    public BaseMessage executeStockTrans(@WebParam(name = "statusAutoOrder") String statusAutoOrder,
                                         @WebParam(name = "stockTransDTO") StockTransDTO stockTransDTO,
                                         @WebParam(name = "stockTransActionDTO") StockTransActionDTO stockTransActionDTO,
                                         @WebParam(name = "lstStockTransDetail") List<StockTransDetailDTO> lstStockTransDetail,
                                         @WebParam(name = "requiredRoleMap") RequiredRoleMap requiredRoleMap) throws Exception;

    /**
     * ham tra ve danh sach chuc danh all
     *
     * @return
     * @throws Exception
     * @throws LogicException
     * @author thanhnt77
     */
    public List<VofficeRoleDTO> getAllListOfficeRole() throws Exception, LogicException;

    /**
     * ham giu so
     *
     * @param staffId
     * @param isdn
     * @param ipAddress
     * @param requiredRoleMap
     * @return
     * @author thanhnt77
     */
    public BaseMessage lockIsdnByStaff(@WebParam(name = "staffId") Long staffId, @WebParam(name = "isdn") String isdn, @WebParam(name = "ipAddress") String ipAddress,
                                       @WebParam(name = "requiredRoleMap") RequiredRoleMap requiredRoleMap);

    /**
     * ham mo so
     *
     * @param staffId
     * @param isdn
     * @param ipAddress
     * @return
     * @author thanhnt77
     */
    public BaseMessage unLockIsdnByStaff(@WebParam(name = "staffId") Long staffId, @WebParam(name = "isdn") String isdn, @WebParam(name = "ipAddress") String ipAddress);

    public SearchIsdnDTO searchIsdn(@WebParam(name = "fromIsdn") String fromIsdn,
                                    @WebParam(name = "toIsdn") String toIsdn,
                                    @WebParam(name = "typeIsdn") String typeIsdn,
                                    @WebParam(name = "status") Long status,
                                    @WebParam(name = "ownerId") Long ownerId,
                                    @WebParam(name = "ownerType") Long ownerType,
                                    @WebParam(name = "telecomServiceId") Long telecomServiceId,
                                    @WebParam(name = "startRow") Long startRow,
                                    @WebParam(name = "maxRows") Long maxRows) throws LogicException, Exception;

    public StockNumber findStockNumberByIsdn(String isdn) throws Exception;
    
    public List<ProductOfferPriceDTO> viewIsdnCommitment(@WebParam(name = "prodOfferId") Long prodOfferId);
}
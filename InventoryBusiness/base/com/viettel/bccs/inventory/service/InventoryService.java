package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.RevokeMessage;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.StockTotalResult;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

/**
 * Description: Service thuc hien xuat nhap kho
 *
 * @author TheTM1
 * @date 09/12/2015
 */

@WebService
public interface InventoryService {

    /**
     * @param
     * @param
     * @return
     * @throws Exception
     * @author Thaont19
     */
    @WebMethod
    public StockTotalResult getStockTotalShop(String departmentCode, Long type, String prodOfferName) throws Exception;

    @WebMethod
    public StockTotalResult getStockTotalStaffByIdNo(String staffIdNo, Long type, String prodOfferName) throws Exception;

    @WebMethod
    public StockTotalResult getStockTotalDetailByArea(String departmentCode, List<Long> lstProdOfferId) throws Exception;

    @WebMethod
    public StockTotalResult getStockTotalDetail(String departmentCode, List<Long> lstProdOfferId) throws Exception;

    /**
     * ham thu hoi
     *
     * @param telecomSerivce
     * @param posCodeVTN
     * @param staffIdNo
     * @param stockList
     * @param contractNo
     * @param accountId
     * @return
     * @author thetm1
     */
    public RevokeMessage revoke(String telecomSerivce, String posCodeVTN, String staffIdNo,
                                List<StockWarrantyDTO> stockList, String contractNo, String accountId);

    /**
     * ham tru kho xu ly su co
     *
     * @param staffIdNo
     * @param account
     * @param troubleType
     * @param lstStockTransDetail
     * @return
     * @author thetm1
     */
    public BaseMessage saveAPDeploymentByIdNo(String staffIdNo, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId);

    /**
     * ham xu ly cong kho xu co, restore lai du lieu
     *
     * @param staffIdNo
     * @param account
     * @param troubleType
     * @param lstStockTransDetail
     * @return
     * @author thanhnt77
     */
    public BaseMessage restoreAPDeploymentByIdNo(@WebParam(name = "staffIdNo") String staffIdNo,
                                                 @WebParam(name = "account") String account,
                                                 @WebParam(name = "troubleType") String troubleType,
                                                 @WebParam(name = "lstStockTransDetail") List<StockTransDetailDTO> lstStockTransDetail,
                                                 @WebParam(name = "transId") String transId);

    public BaseMessage saveAPDeploymentByColl(String staffCode, List<StockTransDetailDTO> lstStockTransDetail);

    /**
     * ham tra ve danh sach vat tu/hang hoa
     *
     * @param areaCode
     * @param productType
     * @param inputSearch
     * @return
     * @author thanhnt77
     */
    public StockTotalResult getListSearchProductByArea(@WebParam(name = "areaCode") String areaCode,
                                                       @WebParam(name = "productType") String productType,
                                                       @WebParam(name = "inputSearch") String inputSearch);

    /**
     * ham tra ve danh sach serial theo ma mat hang
     *
     * @param prodOfferCode
     * @param ownerId
     * @param ownerType
     * @return
     * @author thanhnt77
     */
    public StockTotalResult getListSerialByProductCode(@WebParam(name = "prodOfferCode") String prodOfferCode,
                                                       @WebParam(name = "ownerId") Long ownerId,
                                                       @WebParam(name = "ownerType") Long ownerType);

    /**
     * ham tra ve danh sach serial theo ma mat hang, them trang thai cac kieu
     *
     * @param prodOfferCode
     * @param ownerId
     * @param ownerType
     * @return
     * @author thanhnt77
     */
    public StockTotalResult getListFullSerialByProductCode(@WebParam(name = "prodOfferCode") String prodOfferCode,
                                                       @WebParam(name = "ownerId") Long ownerId,
                                                       @WebParam(name = "ownerType") Long ownerType);

    /**
     * ham xu ly check ton tai shop ao
     *
     * @param shopId
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    public boolean checkExistVirtualShop(Long shopId) throws LogicException, Exception;

    /**
     * ham xu ly xuat hang qua tang
     * @author thanhnt77
     * @param staffCode
     * @param lsTransDetailDTOs
     * @throws LogicException
     * @throws Exception
     */
    public BaseMessage excuteCreateTransGift(@WebParam(name = "staffCode")String staffCode,
                                      @WebParam(name = "lsTransDetailDTOs") List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception;

    /**
     * ham validate so luong hang ton trong kho
     * @author thanhnt77
     * @param staffCode
     * @param lsTransDetailDTOs
     * @throws LogicException
     * @throws Exception
     */
    public BaseMessage validateStockTotal(@WebParam(name = "staffCode")String staffCode,
                                          @WebParam(name = "lsTransDetailDTOs") List<StockTransDetailDTO> lsTransDetailDTOs) throws LogicException, Exception;


    /**
     * ham xu ly tim kiem hop dong
     * @author thanhnt77
     * @param contractCode
     * @param prodOfferCode
     * @param fromDate
     * @param toDate
     * @return
     * @throws Exception
     */
    public List<String> searchContract(@WebParam(name = "contractCode")String contractCode,@WebParam(name = "prodOfferCode") String prodOfferCode,
                                       @WebParam(name = "fromDate")Date fromDate,@WebParam(name = "toDate") Date toDate) throws Exception;

    /**
     * xu ly tim kiem mat hang vat tu
     * @author thanhnt77
     * @param inputSearch
     * @return
     * @throws Exception
     */
    public List<ProductOfferingTotalDTO> searchProductMaterial(@WebParam(name = "inputSearch")String inputSearch) throws Exception;

    @WebMethod
    public StockTotalResult validateStockTotalByStaffIdNo(String staffIdNo, List<StockTotalFullDTO> lsStockTotalFullDTO) throws Exception;
}
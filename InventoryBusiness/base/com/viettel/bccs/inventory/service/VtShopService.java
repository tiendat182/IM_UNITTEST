package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface VtShopService {

    @WebMethod
    public BaseMessage saveOrderIsdn(@WebParam(name = "isdnOrder") String isdnOrder, @WebParam(name = "otp") String otp, @WebParam(name = "idNo") String idNo,
                                     @WebParam(name = "viettelshopId") String viettelshopId, @WebParam(name = "customerName") String customerName,
                                     @WebParam(name = "contactIsdn") String contactIsdn, @WebParam(name = "address") String address,
                                     @WebParam(name = "expiredDate") Date expiredDate, @WebParam(name = "payStatus") String payStatus,
                                     @WebParam(name = "lstShopFeeeDTO") List<VtShopFeeDTO> vtShopFeeeDTOs,
                                     @WebParam(name = "lstShopVtPost") List<String> lstShopVtPost);


    @WebMethod
    public BaseMessage increaseExpiredDateOrder(@WebParam(name = "isdnOrder") String isdnOrder, @WebParam(name = "expiredDate") Date expiredDate);

    @WebMethod
    public List<StockIsdnVtShopFeeDTO> findVtShopFeeByIsdn(@WebParam(name = "isdn") String isdn);

    @WebMethod
    public BaseMessage cancelOrderIsdn(@WebParam(name = "isdn") String isdn);

    @WebMethod
    public StockIsdnVtShopDTO findOrderIsdn(@WebParam(name = "isdn") String isdn);

    @WebMethod
    public List<StockTotalFullDTO> findStockByArea(@WebParam(name = "province") String province, @WebParam(name = "district") String district, @WebParam(name = "productOfferCode") String productOfferCode);

    @WebMethod
    public BaseMessage editOrderIsdn(@WebParam(name = "isdnOrder") String isdnOrder, @WebParam(name = "oldIdNo") String oldIdNo, @WebParam(name = "newIdNo") String newIdNo);

    @WebMethod
    public List<StockTotalFullDTO> findStockDigital(@WebParam(name = "shopCode") String shopCode, @WebParam(name = "productOfferCode") String productOfferCode) throws Exception;

    @WebMethod
    public SearchIsdnDTO searchIsdn(@WebParam(name = "isdn") String isdn, @WebParam(name = "telecomServiceId") Long telecomServiceId, @WebParam(name = "areaCode") String areaCode,
                                    @WebParam(name = "startRow") Long startRow, @WebParam(name = "maxRows") Long maxRows);
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopDTO;
import com.viettel.bccs.inventory.dto.StockIsdnVtShopFeeDTO;
import com.viettel.bccs.inventory.dto.StockTotalFullDTO;
import com.viettel.bccs.inventory.dto.VtShopFeeDTO;
import com.viettel.bccs.inventory.model.StockIsdnVtShop;

import java.util.Date;
import java.util.List;

import com.viettel.fw.Exception.LogicException;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface StockIsdnVtShopService {

    @WebMethod
    public StockIsdnVtShopDTO findOne(String isdn) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockIsdnVtShopDTO> findAll() throws Exception;

    @WebMethod
    public List<StockIsdnVtShopDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public void saveOrderIsdn(String isdnOrder, String otp, String idNo, String viettelshopId, String customerName, String contactIsdn, String address,
                              Date expiredDate, String payStatus, List<VtShopFeeDTO> vtShopFeeeDTOs, List<String> lstShopVtPost) throws LogicException, Exception;

    @WebMethod
    public void increaseExpiredDateOrder(String isdnOrder, Date expiredDate) throws LogicException, Exception;

    @WebMethod
    public List<StockIsdnVtShopFeeDTO> findVtShopFeeByIsdn(String isdn) throws Exception;

    @WebMethod
    public void lockIsdnVtshop(String isdn, Long requestId, String staffCode) throws LogicException, Exception;

    @WebMethod
    public List<StockIsdnVtShopDTO> findRequestList(List<Long> requestList);

    @WebMethod
    public void cancelOrderIsdn(String isdn) throws LogicException, Exception;

    @WebMethod
    public List<StockTotalFullDTO> findStockByArea(Long shopId, Long parentShopId, Long prodOfferId) throws Exception;

    @WebMethod
    public List<StockTotalFullDTO> findStockDigital(Long shopId, Long prodOfferId) throws Exception;

    @WebMethod
    public void lockIsdnHotline(String isdn, String staffCode) throws LogicException, Exception;

    @WebMethod
    public void unlockIsdnHotline(String isdn) throws LogicException, Exception;

    @WebMethod
    public void editOrderIsdn(String isdnOrder, String oldIdNo, String newIdNo) throws LogicException, Exception;
}
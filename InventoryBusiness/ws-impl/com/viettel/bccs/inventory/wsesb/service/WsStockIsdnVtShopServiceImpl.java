package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopDTO;
import com.viettel.bccs.inventory.dto.StockIsdnVtShopFeeDTO;
import com.viettel.bccs.inventory.dto.StockTotalFullDTO;
import com.viettel.bccs.inventory.dto.VtShopFeeDTO;
import com.viettel.bccs.inventory.service.StockIsdnVtShopService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

@Service("WsStockIsdnVtShopServiceImpl")
public class WsStockIsdnVtShopServiceImpl implements StockIsdnVtShopService {

    public static final Logger logger = Logger.getLogger(WsStockIsdnVtShopServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockIsdnVtShopService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockIsdnVtShopService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockIsdnVtShopDTO findOne(String isdn) throws Exception {
        return client.findOne(isdn);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockIsdnVtShopDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<StockIsdnVtShopDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public void saveOrderIsdn(String isdnOrder, String otp, String idNo, String viettelshopId, String customerName, String contactIsdn, String address, Date expiredDate, String payStatus, List<VtShopFeeDTO> vtShopFeeeDTOs, List<String> lstShopVtPost) throws LogicException, Exception {
        client.saveOrderIsdn(isdnOrder, otp, idNo, viettelshopId, customerName, contactIsdn, address, expiredDate, payStatus, vtShopFeeeDTOs, lstShopVtPost);
    }

    @Override
    @WebMethod
    public void increaseExpiredDateOrder(String isdnOrder, Date expiredDate) throws LogicException, Exception {
        client.increaseExpiredDateOrder(isdnOrder, expiredDate);
    }

    @Override
    @WebMethod
    public List<StockIsdnVtShopFeeDTO> findVtShopFeeByIsdn(String isdn) throws Exception {
        return client.findVtShopFeeByIsdn(isdn);
    }

    @Override
    @WebMethod
    public void lockIsdnVtshop(String isdn, Long requestId, String staffCode) throws LogicException, Exception {
        client.lockIsdnVtshop(isdn, requestId, staffCode);
    }

    @Override
    @WebMethod
    public List<StockIsdnVtShopDTO> findRequestList(List<Long> requestList) {
        return client.findRequestList(requestList);
    }

    @Override
    @WebMethod
    public void cancelOrderIsdn(String isdn) throws LogicException, Exception {
        client.cancelOrderIsdn(isdn);
    }

    @Override
    @WebMethod
    public List<StockTotalFullDTO> findStockByArea(Long shopId, Long parentShopId, Long prodOfferId) throws Exception {
        return client.findStockByArea(shopId, parentShopId, prodOfferId);
    }

    @Override
    @WebMethod
    public List<StockTotalFullDTO> findStockDigital(Long shopId, Long prodOfferId) throws Exception {
        return client.findStockDigital(shopId, prodOfferId);
    }

    @Override
    @WebMethod
    public void lockIsdnHotline(String isdn, String staffCode) throws LogicException, Exception {
        client.lockIsdnHotline(isdn, staffCode);
    }

    @Override
    @WebMethod
    public void unlockIsdnHotline(String isdn) throws LogicException, Exception {
        client.unlockIsdnHotline(isdn);
    }

    @Override
    @WebMethod
    public void editOrderIsdn(String isdnOrder, String oldIdNo, String newIdNo) throws LogicException, Exception {
        client.editOrderIsdn(isdnOrder, oldIdNo, newIdNo);
    }
}
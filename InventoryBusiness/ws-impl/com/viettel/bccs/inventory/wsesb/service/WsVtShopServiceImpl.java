package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.VtShopService;
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

@Service("WsVtShopServiceImpl")
public class WsVtShopServiceImpl implements VtShopService {

    public static final Logger logger = Logger.getLogger(WsVtShopServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private VtShopService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(VtShopService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public BaseMessage increaseExpiredDateOrder(String isdnOrder, Date expiredDate) {
        return client.increaseExpiredDateOrder(isdnOrder, expiredDate);
    }

    @Override
    @WebMethod
    public List<StockIsdnVtShopFeeDTO> findVtShopFeeByIsdn(String isdn) {
        return client.findVtShopFeeByIsdn(isdn);
    }

    @Override
    @WebMethod
    public BaseMessage saveOrderIsdn(String isdnOrder, String otp, String idNo, String viettelshopId, String customerName, String contactIsdn, String address, Date expiredDate, String payStatus, List<VtShopFeeDTO> vtShopFeeeDTOs, List<String> lstShopVtPost) {
        return client.saveOrderIsdn(isdnOrder, otp, idNo, viettelshopId, customerName, contactIsdn, address, expiredDate, payStatus, vtShopFeeeDTOs, lstShopVtPost);
    }

    @Override
    @WebMethod
    public BaseMessage cancelOrderIsdn(String isdn) {
        return client.cancelOrderIsdn(isdn);
    }

    @Override
    @WebMethod
    public StockIsdnVtShopDTO findOrderIsdn(String isdn) {
        return client.findOrderIsdn(isdn);
    }

    @Override
    @WebMethod
    public List<StockTotalFullDTO> findStockByArea(String province, String district, String productOfferCode) {
        return client.findStockByArea(province, district, productOfferCode);
    }

    @Override
    @WebMethod
    public BaseMessage editOrderIsdn(String isdnOrder, String oldIdNo, String newIdNo) {
        return client.editOrderIsdn(isdnOrder, oldIdNo, newIdNo);
    }
    @Override
    @WebMethod
    public List<StockTotalFullDTO> findStockDigital(String shopCode, String productOfferCode) throws Exception {
        return client.findStockDigital(shopCode, productOfferCode);
    }

    @Override
    @WebMethod
    public SearchIsdnDTO searchIsdn(String isdn, Long telecomServiceId, String areaCode, Long startRow, Long maxRows) {
        return client.searchIsdn(isdn, telecomServiceId, areaCode, startRow, maxRows);
    }
}
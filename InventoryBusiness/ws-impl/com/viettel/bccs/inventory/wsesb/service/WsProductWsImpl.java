package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.AddGoodSaleMessage;
import com.viettel.bccs.inventory.service.ProductWs;
import com.viettel.bccs.inventory.service.SaleWs;
import com.viettel.fw.Exception.LogicException;
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

@Service("WsProductWsImpl")
public class WsProductWsImpl implements ProductWs {

    public static final Logger logger = Logger.getLogger(WsProductWsImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ProductWs client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ProductWs.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public ShopMapDTO findByShopCodeVTN(String areaCodeVTN) throws Exception {
        return client.findByShopCodeVTN(areaCodeVTN);
    }

    @Override
    @WebMethod
    public ShopMapDTO findByShopCodeVTNOrig(String areaCodeVTN) throws Exception {
        return client.findByShopCodeVTNOrig(areaCodeVTN);
    }

    @Override
    @WebMethod
    public ProfileDTO getProfileByProductOfferId(Long productOfferId) throws LogicException, Exception {
        return client.getProfileByProductOfferId(productOfferId);
    }

    @Override
    @WebMethod
    public List<ProductOfferPriceDTO> getListPriceByStaff(Long staffId, Long shopId, Long productOfferId) throws LogicException, Exception {
        return client.getListPriceByStaff(staffId, shopId, productOfferId);
    }

    @Override
    public String findTeamCodeByShopCode(String shopCode) throws LogicException, Exception {
        return client.findTeamCodeByShopCode(shopCode);
    }
}
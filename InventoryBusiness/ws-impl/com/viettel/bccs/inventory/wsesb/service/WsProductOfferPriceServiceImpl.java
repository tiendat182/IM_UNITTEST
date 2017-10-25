package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.ProductOfferPriceService;

import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsProductOfferPriceServiceImpl")
public class WsProductOfferPriceServiceImpl implements ProductOfferPriceService {

    public static final Logger logger = Logger.getLogger(WsProductOfferPriceServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ProductOfferPriceService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ProductOfferPriceService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public ProductOfferPriceDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<ProductOfferPriceDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<ProductOfferPriceDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(ProductOfferPriceDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(ProductOfferPriceDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public Long getPriceAmount(Long prodOfferId, Long priceTypeId, Long pricePolicyId) throws LogicException, Exception {
        return client.getPriceAmount(prodOfferId, priceTypeId, pricePolicyId);
    }

    @Override
    @WebMethod
    public List<ProductOfferPriceDTO> getPriceDepositAmount(Long prodOfferId, Long shopId, Long pricePolicyId, Long channelTypeId, Long ownerType, Long ownerId) throws LogicException, Exception {
        return client.getPriceDepositAmount(prodOfferId, shopId, pricePolicyId, channelTypeId, ownerType, ownerId);
    }

    @Override
    public List<ProductOfferPriceDTO> getProductOfferPrice(Long prodOfferId, Long priceTypeId, Long pricePolicyId) throws LogicException, Exception {
        return client.getProductOfferPrice(prodOfferId, priceTypeId, pricePolicyId);
    }

    @Override
    @WebMethod
    public List<ProductOfferPriceDTO> getPriceSaleAmount(Long prodOfferId, Long shopId, Long pricePolicyId, Long channelTypeId, Long ownerType, Long ownerId) throws LogicException, Exception {
        return client.getPriceSaleAmount(prodOfferId, shopId, pricePolicyId, channelTypeId, ownerType, ownerId);
    }

    @Override
    @WebMethod
    public List<ProductOfferPriceDTO> getProductOfferPriceByProdOfferId(Long prodOfferId) throws Exception {
        return client.getProductOfferPriceByProdOfferId(prodOfferId);
    }
}
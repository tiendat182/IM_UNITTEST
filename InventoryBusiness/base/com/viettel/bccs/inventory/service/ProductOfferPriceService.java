package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ProductOfferPriceService {

    @WebMethod
    public ProductOfferPriceDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ProductOfferPriceDTO> findAll() throws Exception;

    @WebMethod
    public List<ProductOfferPriceDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ProductOfferPriceDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ProductOfferPriceDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public Long getPriceAmount(Long prodOfferId, Long priceTypeId, Long pricePolicyId) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferPriceDTO> getPriceDepositAmount(Long prodOfferId, Long shopId, Long pricePolicyId, Long channelTypeId, Long ownerType, Long ownerId) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferPriceDTO> getPriceSaleAmount(Long prodOfferId, Long shopId, Long pricePolicyId, Long channelTypeId, Long ownerType, Long ownerId) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferPriceDTO> getProductOfferPrice(Long prodOfferId, Long priceTypeId, Long pricePolicyId) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferPriceDTO> getProductOfferPriceByProdOfferId(Long prodOfferId) throws Exception;
}
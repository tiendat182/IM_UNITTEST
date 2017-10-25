package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.bccs.inventory.model.ProductOfferPrice;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface ProductOfferPriceRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<ProductOfferPriceDTO> getPriceAmount(Long prodOfferId, Long priceTypeId, Long pricePolicyId) throws LogicException, Exception;

    public List<ProductOfferPriceDTO> getProductOfferPrice(Long prodOfferId, Long branchId, Long pricePolicyId, Long channelTypeId, Long priceTypeId, Long ownerType, Long ownerId) throws LogicException, Exception;

    public List<ProductOfferPrice> getProductOfferPriceByProdOfferId(Long prodOfferId) throws Exception;

}
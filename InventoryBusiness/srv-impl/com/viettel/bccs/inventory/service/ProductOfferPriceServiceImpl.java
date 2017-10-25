package com.viettel.bccs.inventory.service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductOfferPriceDTO;
import com.viettel.bccs.inventory.model.ProductOfferPrice;
import com.viettel.bccs.inventory.repo.ProductOfferPriceRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class ProductOfferPriceServiceImpl extends BaseServiceImpl implements ProductOfferPriceService {

    private final BaseMapper<ProductOfferPrice, ProductOfferPriceDTO> mapper = new BaseMapper(ProductOfferPrice.class, ProductOfferPriceDTO.class);

    @Autowired
    private ProductOfferPriceRepo repository;
    public static final Logger logger = Logger.getLogger(ProductOfferPriceService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ProductOfferPriceDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ProductOfferPriceDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ProductOfferPriceDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ProductOfferPriceDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ProductOfferPriceDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public Long getPriceAmount(Long prodOfferId, Long priceTypeId, Long pricePolicyId) throws LogicException, Exception {
        List<ProductOfferPriceDTO> lstPrice = repository.getPriceAmount(prodOfferId, priceTypeId, pricePolicyId);
        Long result = 0L;
        if (!DataUtil.isNullOrEmpty(lstPrice) && !DataUtil.isNullObject(lstPrice.get(0))) {
            return DataUtil.safeToLong(lstPrice.get(0).getPrice());
        }
        return result;
    }

    @WebMethod
    @Override
    @Cacheable(cacheName = "productOfferPriceServiceImpl.getPriceDepositAmount")
    public List<ProductOfferPriceDTO> getPriceDepositAmount(Long prodOfferId, Long shopId, Long pricePolicyId, Long channelTypeId, Long ownerType, Long ownerId) throws LogicException, Exception {
        return repository.getProductOfferPrice(prodOfferId, shopId, pricePolicyId, channelTypeId, Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_DEPOSIT, ownerType, ownerId);
    }

    @Override
    @Cacheable(cacheName = "productOfferPriceServiceImpl.getPriceSaleAmount")
    public List<ProductOfferPriceDTO> getPriceSaleAmount(Long prodOfferId, Long shopId, Long pricePolicyId, Long channelTypeId, Long ownerType, Long ownerId) throws LogicException, Exception {
        return repository.getProductOfferPrice(prodOfferId, shopId, pricePolicyId, channelTypeId, Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_PAY, ownerType, ownerId);
    }

    @Override
    public List<ProductOfferPriceDTO> getProductOfferPrice(Long prodOfferId, Long priceTypeId, Long pricePolicyId) throws LogicException, Exception {
        return repository.getPriceAmount(prodOfferId, priceTypeId, pricePolicyId);
    }

    @Override
    public List<ProductOfferPriceDTO> getProductOfferPriceByProdOfferId(Long prodOfferId) throws Exception {
        return mapper.toDtoBean(repository.getProductOfferPriceByProdOfferId(prodOfferId));
    }
}

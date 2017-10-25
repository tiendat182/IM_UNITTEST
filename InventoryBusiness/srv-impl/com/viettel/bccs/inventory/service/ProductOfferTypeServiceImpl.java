package com.viettel.bccs.inventory.service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductOfferTypeDTO;
import com.viettel.bccs.inventory.model.ProductOfferType;
import com.viettel.bccs.inventory.repo.ProductOfferTypeRepo;
import com.viettel.fw.Exception.LogicException;
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
public class ProductOfferTypeServiceImpl extends BaseServiceImpl implements ProductOfferTypeService {

    private final BaseMapper<ProductOfferType, ProductOfferTypeDTO> mapper = new BaseMapper(ProductOfferType.class, ProductOfferTypeDTO.class);

    @Autowired
    private ProductOfferTypeRepo repository;
    public static final Logger logger = Logger.getLogger(ProductOfferTypeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }


    @WebMethod
    public ProductOfferTypeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ProductOfferTypeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ProductOfferTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ProductOfferTypeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ProductOfferTypeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    @Cacheable(cacheName = "productOfferTypeServiceImpl.getListProductOfferType")
    public List<ProductOfferTypeDTO> getListProductOfferType() throws Exception {
        return mapper.toDtoBean(repository.getListProductOfferType());
    }

    @Override
    @Cacheable(cacheName = "productOfferTypeServiceImpl.getAllProductOfferTypePhone")
    public List<ProductOfferTypeDTO> getAllProductOfferTypePhone(Long ownerId) throws LogicException, Exception {
        return repository.getAllProductOfferTypePhone(ownerId);
    }

    @Override
    @Cacheable(cacheName = "productOfferTypeServiceImpl.getListProductOfferTypeByOwnerId")
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerId(Long ownerId, String ownerType, Long typeNoSerial) throws Exception {
        return repository.getListProductOfferTypeByOwnerId(ownerId, ownerType, typeNoSerial);
    }

    @Override
    @Cacheable(cacheName = "productOfferTypeServiceImpl.getListProductOfferTypeByOwnerIdAndTableName")
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdAndTableName(Long ownerId, String ownerType, String tableName) throws Exception {
        return repository.getListProductOfferTypeByOwnerIdAndTableName(ownerId, ownerType, tableName);
    }

    @Override
    @Cacheable(cacheName = "productOfferTypeServiceImpl.getListByOwnerIdOwnerTypeState")
    public List<ProductOfferTypeDTO> getListByOwnerIdOwnerTypeState(Long ownerId, String ownerType, Long stateId) throws Exception {
        return repository.getListByOwnerIdOwnerTypeState(ownerId, ownerType, stateId);
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdMaterial(Long ownerId, String ownerType, Long prodOfferTypeId) throws Exception {
        return repository.getListProductOfferTypeByOwnerIdMaterial(ownerId, ownerType, prodOfferTypeId);
    }

    @Override
    @Cacheable(cacheName = "productOfferTypeServiceImpl.getListProductOfferTypeByDeposit")
    public List<ProductOfferTypeDTO> getListProductOfferTypeByDeposit(Long ownerId, String ownerType, Long channelTypeId, Long branchId) throws Exception {
        return repository.getListProductOfferTypeChannelAndShop(ownerId, ownerType, Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_DEPOSIT, channelTypeId, branchId);
    }

    @Override
    @Cacheable(cacheName = "productOfferTypeServiceImpl.getListProductOfferTypeByPay")
    public List<ProductOfferTypeDTO> getListProductOfferTypeByPay(Long ownerId, String ownerType, Long channelTypeId, Long branchId) throws Exception {
        return repository.getListProductOfferTypeChannelAndShop(ownerId, ownerType, Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_PAY, channelTypeId, branchId);
    }

    @Override
    public List<ProductOfferTypeDTO> getListProduct() throws Exception {
        return repository.getListProduct();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeById(List<Long> productOfferTypeId) throws Exception {
        return repository.getListProductOfferTypeById(productOfferTypeId);
    }

    public List<ProductOfferTypeDTO> getListOfferTypeSerial() throws Exception {
        return repository.getListOfferTypeSerial();
    }

}

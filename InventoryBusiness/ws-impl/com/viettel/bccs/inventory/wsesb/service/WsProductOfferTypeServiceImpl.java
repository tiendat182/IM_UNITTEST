package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ProductOfferTypeDTO;
import com.viettel.bccs.inventory.service.ProductOfferTypeService;
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
import java.util.List;

@Service("WsProductOfferTypeServiceImpl")
public class WsProductOfferTypeServiceImpl implements ProductOfferTypeService {

    public static final Logger logger = Logger.getLogger(WsProductOfferTypeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ProductOfferTypeService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ProductOfferTypeService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public ProductOfferTypeDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<ProductOfferTypeDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<ProductOfferTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(ProductOfferTypeDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(ProductOfferTypeDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferType() throws LogicException, Exception {
        return client.getListProductOfferType();
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerId(Long ownerId, String ownerType, Long typeNoSerial) throws Exception {
        return client.getListProductOfferTypeByOwnerId(ownerId, ownerType, typeNoSerial);
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdAndTableName(Long ownerId, String ownerType, String tableName) throws Exception {
        return client.getListProductOfferTypeByOwnerIdAndTableName(ownerId, ownerType, tableName);
    }

    @Override
    public List<ProductOfferTypeDTO> getAllProductOfferTypePhone(Long ownerId) throws LogicException, Exception {
        return client.getAllProductOfferTypePhone(ownerId);
    }

    @Override
    public List<ProductOfferTypeDTO> getListByOwnerIdOwnerTypeState(Long ownerId, String ownerType, Long stateId) throws Exception {
        return client.getListByOwnerIdOwnerTypeState(ownerId, ownerType, stateId);
    }

    @Override
    @WebMethod
    public List<ProductOfferTypeDTO> getListProductOfferTypeByDeposit(Long ownerId, String ownerType, Long channelTypeId, Long branchId) throws Exception {
        return client.getListProductOfferTypeByDeposit(ownerId, ownerType, channelTypeId, branchId);
    }

    @Override
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdMaterial(Long ownerId, String ownerType, Long prodOfferTypeId) throws Exception {
        return client.getListProductOfferTypeByOwnerIdMaterial(ownerId, ownerType, prodOfferTypeId);
    }

    @Override
    @WebMethod
    public List<ProductOfferTypeDTO> getListProductOfferTypeByPay(Long ownerId, String ownerType, Long channelTypeId, Long branchId) throws Exception {
        return client.getListProductOfferTypeByPay(ownerId, ownerType, channelTypeId, branchId);
    }

    @Override
    @WebMethod
    public List<ProductOfferTypeDTO> getListProduct() throws Exception {
        return client.getListProduct();
    }

    @Override
    @WebMethod
    public List<ProductOfferTypeDTO> getListProductOfferTypeById(List<Long> productOfferTypeId) throws Exception {
        return client.getListProductOfferTypeById(productOfferTypeId);
    }

    @Override
    @WebMethod
    public List<ProductOfferTypeDTO> getListOfferTypeSerial() throws Exception {
        return client.getListOfferTypeSerial();
    }
}
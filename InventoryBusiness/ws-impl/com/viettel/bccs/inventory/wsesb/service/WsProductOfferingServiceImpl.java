package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferingService;
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

@Service("WsProductOfferingServiceImpl")
public class WsProductOfferingServiceImpl implements ProductOfferingService {

    public static final Logger logger = Logger.getLogger(WsProductOfferingServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ProductOfferingService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ProductOfferingService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public ProductOfferingDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<ProductOfferingDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<ProductOfferingDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public BaseMessage create(ProductOfferingDTO productSpecCharacterDTO) throws Exception {
        return client.create(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(ProductOfferingDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTO(String inputSearch, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId, List<Long> prodOfferIdList) throws Exception {
        return client.getLsProductOfferingDTO(inputSearch, lsProductOfferTypeIds, ownerId, ownerType, stateId, prodOfferIdList);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTOMaterial(String input, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId, List<Long> prodOfferIdList, String stockModelType) throws Exception {
        return client.getLsProductOfferingDTOMaterial(input, lsProductOfferTypeIds, ownerId, ownerType, stateId, prodOfferIdList, stockModelType);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingForChangeProduct(String input, Long prodOfferIdOld, boolean isDuplicate) throws Exception {
        return client.getLsProductOfferingForChangeProduct(input, prodOfferIdOld, isDuplicate);
    }

    @Override
    public List<ProductOfferingTotalDTO> getAllLsProductOfferingDTO(String input) throws Exception {
        return client.getAllLsProductOfferingDTO(input);
    }

    @Override
    public List<ProductOfferingTotalDTO> getAllLsProductOfferingDTOForProcessStock(String input, List<Long> lsProductOfferTypeIds) throws Exception {
        return client.getAllLsProductOfferingDTOForProcessStock(input, lsProductOfferTypeIds);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOffering(String input) throws Exception {
        return client.getLsProductOffering(input);
    }

    @Override
    public List<ProductOfferingDTO> getLsProductOfferDTOByType(String input, List<Long> lsProductOfferTypeIds) throws Exception {
        return client.getLsProductOfferDTOByType(input, lsProductOfferTypeIds);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductType(String input, Long prodTypeId) throws Exception {
        return client.getLsProductOfferingByProductType(input, prodTypeId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductTypeAndCode(String input, Long prodTypeId, List<String> lstCodes) throws Exception {
        return client.getLsProductOfferingByProductTypeAndCode(input, prodTypeId, lstCodes);
    }

    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingByProductType(String input, Long prodTypeId) throws Exception {
        return client.getListProductOfferingByProductType(input, prodTypeId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingForUpdateSerialGpon(String input, String updateType) throws Exception {
        return client.getListProductOfferingForUpdateSerialGpon(input, updateType);
    }

    @Override
    public ProductOfferingTotalDTO getProductOffering(Long id) throws Exception {
        return client.getProductOffering(id);
    }

    @Override
    public List<ProductOfferingDTO> getListProductOffering() throws LogicException, Exception {
        return client.getListProductOffering();
    }

    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingViewer(String ownerType, String ownerId, Long prodOfferId) throws Exception {
        return client.getListProductOfferingViewer(ownerType, ownerId, prodOfferId);
    }

    @Override
    public List<ProductOfferingDTO> getListProductOfferTypeViewer(String ownerType, String ownerId, Long prodOfferId, Long stateId) throws Exception {
        return client.getListProductOfferTypeViewer(ownerType, ownerId, prodOfferId, stateId);
    }

    @Override
    public List<ProductOfferingDTO> getListProductOfferingByProOfTypeId(Long productOfferTypeId, Long ownerId, String ownerType) throws LogicException, Exception {
        return client.getListProductOfferingByProOfTypeId(productOfferTypeId, ownerId, ownerType);
    }

    @Override
    public List<ProductOfferingDTO> findByProductOfferTypeId(Long productOfferTypeId, Long id) throws Exception {
        return client.findByProductOfferTypeId(productOfferTypeId, id);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingIsdnDTO(List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId) throws Exception {
        return client.getLsProductOfferingIsdnDTO(lsProductOfferTypeIds, ownerId, ownerType, stateId);
    }

    @Override
    public ProductOfferingDTO findByProductOfferCode(String productOfferCode, String status) throws Exception {
        return client.findByProductOfferCode(productOfferCode, status);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTOByPrice(String inputSearch, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId, Long priceTypeId, Long branchId, Long channelTypeId) throws Exception {
        return client.getLsProductOfferingDTOByPrice(inputSearch, lsProductOfferTypeIds, ownerId, ownerType, stateId, priceTypeId, branchId, channelTypeId);
    }

    @Override
    @WebMethod
    public List<ProductOfferingTotalDTO> getListProductForTag(String input, ConfigProductTagDTO config) {
        return client.getListProductForTag(input, config);
    }

    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingTotalDistinct(String ownerType, String ownerId, String stockCode) throws Exception {
        return client.getListProductOfferingTotalDistinct(ownerType, ownerId, stockCode);
    }

    @Override
    @WebMethod
    public ProfileDTO getProfileByProductId(Long productOfferId) throws Exception {
        return client.getProfileByProductId(productOfferId);
    }

    @Override
    @WebMethod
    public List<ProductOfferingTotalDTO> getProductOfferingTotalDTO(String input, Long productOfferTypeId, Long ownerId, Long ownerType, Long stateId) throws Exception {
        return client.getProductOfferingTotalDTO(input, productOfferTypeId, ownerId, ownerType, stateId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductOfferingMaterial(String input) throws Exception {
        return client.getProductOfferingMaterial(input);
    }

    @Override
    public List<ProductOfferingTotalDTO> getListProductOfferingForProcessStock(Long ownerType, Long ownerId, Long proOfferId, Long stateId) throws Exception {
        return client.getListProductOfferingForProcessStock(ownerType, ownerId, proOfferId, stateId);
    }

    @Override
    public List<ProductOfferingDTO> getListProductOfferTypeForProcessStock(String ownerType, String ownerId, Long productOfferId, Long stateId) throws Exception {
        return client.getListProductOfferTypeForProcessStock(ownerType, ownerId, productOfferId, stateId);
    }

    @Override
    public String getStockStateName(Long stateId) {
        return client.getStockStateName(stateId);
    }

    @Override
    public ProductOfferingDTO getProdOfferDtoByCodeAndStock(String prodOfferCode, Long ownerId, Long stateId) {
        return client.getProdOfferDtoByCodeAndStock(prodOfferCode, ownerId, stateId);
    }

    @Override
    public ProductOfferingDTO getProdOfferDtoByCodeAndStockNew(String prodOfferCode, Long ownerId, Long ownerType, Long stateId) {
        return client.getProdOfferDtoByCodeAndStockNew(prodOfferCode, ownerId, ownerType, stateId);
    }

    @Override
    @WebMethod
    public List<Long> getProdOfferIdForRequestBalanceExport(Long ownerId, Long ownerType, Long prodOfferId) {
        return client.getProdOfferIdForRequestBalanceExport(ownerId, ownerType, prodOfferId);
    }

    @Override
    @WebMethod
    public List<Long> getProdOfferIdForRequestBalanceImport(Long ownerId, Long ownerType, Long prodOfferId) {
        return client.getProdOfferIdForRequestBalanceImport(ownerId, ownerType, prodOfferId);
    }

    @Override
    @WebMethod
    public List<ProductOfferingTotalDTO> getProductOfferingByListId(List<Long> prodOfferIds) throws Exception {
        return client.getProductOfferingByListId(prodOfferIds);
    }

    @Override
    @WebMethod
    public List<ProductOfferingDTO> getLsProductOfferDtoByTypeId(Long productOfferTypeId) throws Exception {
        return client.getLsProductOfferDtoByTypeId(productOfferTypeId);
    }

    @Override
    @WebMethod
    public List<ProductInfoDTO> getLstProductInfo(Long stockTransId) throws Exception {
        return client.getLstProductInfo(stockTransId);
    }

    @Override
    @WebMethod
    public ProductOfferingDTO getProductByCode(String prodOfferCode) throws Exception {
        return client.getProductByCode(prodOfferCode);
    }

    @Override
    public List<ProductOfferingLogisticDTO> getProductOfferingLogistic(Long stockTransId) throws Exception {
        return client.getProductOfferingLogistic(stockTransId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsRequestProductByShop(Long ownerType, Long ownerId, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception {
        return client.getLsRequestProductByShop(ownerType, ownerId, prodOfferTypeId, prodOfferId, stateId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getLsProOfferTotalDTORequest(String input, Long prodOfferTypeId, Long stateId) throws Exception {
        return client.getLsProOfferTotalDTORequest(input, prodOfferTypeId, stateId);
    }

    @Override
    @WebMethod
    public ProductOfferingDTO getProductByCodeIm1(String prodOfferCode) throws Exception {
        return client.getProductByCodeIm1(prodOfferCode);
    }

    @Override
    public List<ProductOfferingDTO> getInventoryInfoWarranty(List<Long> listProdOfferId) throws Exception {
        return client.getInventoryInfoWarranty(listProdOfferId);
    }

    @Override
    public List<ProductOfferingTotalDTO> getProductExportDemo(String input, Long ownerId, String ownerType, String stateId) throws Exception {
        return client.getProductExportDemo(input, ownerId, ownerType, stateId);
    }
}
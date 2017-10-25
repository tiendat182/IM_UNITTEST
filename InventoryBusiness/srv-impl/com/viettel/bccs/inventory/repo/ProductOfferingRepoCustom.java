package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ProductInfoDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingLogisticDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.StockTotalFullDTO;
import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface ProductOfferingRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    /**
     * tuydv1
     *
     * @return
     */
    public List<ProductOffering> getListProductOffering() throws Exception;

    /**
     * tuydv1
     *
     * @return
     */
    public List<ProductOffering> getListProductOfferingByProOfTypeId(Long productOfferTypeId, Long ownerId, String ownerType) throws Exception;


    /**
     * thaont19
     *
     * @param ownerType
     * @param ownerId
     * @param stockCode
     * @return
     * @throws Exception
     */
    public List<ProductOfferingDTO> getListProductOfferTypeViewer(String ownerType, String ownerId, Long prodOfferId, Long stateId) throws Exception;

    public List<ProductOfferingDTO> getListProductOfferTypeForProcessStock(String ownerType, String ownerId, Long productOfferId, Long stateId) throws Exception;

    /**
     * thaont19
     *
     * @param ownerType
     * @param ownerId
     * @param stockCode
     * @return
     * @throws Exception
     */
    public List<ProductOfferingTotalDTO> getListProductOfferingViewer(String ownerType, String ownerId, Long prodOfferId) throws Exception;

    public String getStockModelPrefixById(Long prodOfferId) throws Exception;

    /**
     * ham tra ve danh sach mat hang theo shop
     *
     * @param ownerType
     * @param ownerId
     * @param prodOfferTypeId
     * @param prodOfferId
     * @param stateId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    List<ProductOfferingTotalDTO> getLsRequestProductByShop(Long ownerType, Long ownerId, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception;

    /**
     * thaont19
     *
     * @param ownerType
     * @param ownerId
     * @param stockCode
     * @return
     * @throws Exception
     */
    public List<ProductOfferingTotalDTO> getListProductOfferingTotalDistinct(String ownerType, String ownerId, String stockCode) throws Exception;

    public List<ProductOfferingTotalDTO> getListProductOfferingForProcessStock(Long ownerType, Long ownerId, Long proOfferId, Long stateId) throws Exception;

    /**
     * ham tra ve thong tin mat hang tu code, kho xuat, trang thai mat hang (phuc vu cho action upload tu file )
     *
     * @param prodOfferCode
     * @param ownerId
     * @param stateId
     * @return
     * @author thanhnt
     */
    public ProductOfferingDTO getProdOfferDtoByCodeAndStock(String prodOfferCode, Long ownerId, Long stateId);

    /**
     * ham tra ve thong tin mat hang tu code, kho xuat
     *
     * @param prodOfferCode
     * @param ownerId
     * @param ownerType
     * @param stateId
     * @return
     * @author thanhnt
     */
    public ProductOfferingDTO getProdOfferDtoByCodeAndStockNew(String prodOfferCode, Long ownerId, Long ownerType, Long stateId);

    public List<Long> getProdOfferIdForRequestBalanceExport(Long ownerId, Long ownerType, Long prodOfferId);

    public List<Long> getProdOfferIdForRequestBalanceImport(Long ownerId, Long ownerType, Long prodOfferId);

    public List<ProductOfferingTotalDTO> getProductOfferingByListId(List<Long> prodOfferIds) throws Exception;

    /**
     * ham tra ve danh sach hang hoa theo loai mat hang
     *
     * @param productOfferTypeId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<ProductOfferingDTO> getLsProductOfferDtoByTypeId(Long productOfferTypeId) throws Exception;

    public ProductOfferingDTO getProductOfferingStockById(Long prodOfferId) throws Exception;

    public List<ProductInfoDTO> getLstProductInfo(Long stockTransId) throws Exception;

    public List<ProductOfferingLogisticDTO> getProductOfferingLogistic(Long stockTransId) throws Exception;

    public List<StockTotalFullDTO> getListSearchProductByArea(String province, String district, String productType, String inputSearch) throws Exception;

    public ProductOfferingDTO getProductByCodeIm1(String prodOfferCode) throws Exception;

    public List<ProductOfferingDTO> getInventoryInfoWarranty(List<Long> listProdOfferId) throws Exception;

    ProductOffering getProductByCode(String prodOfferCode) throws Exception;
}
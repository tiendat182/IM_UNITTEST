package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ProductOfferingService {

    @WebMethod
    public ProductOfferingDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ProductOfferingDTO> findAll() throws Exception;

    @WebMethod
    public List<ProductOfferingDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ProductOfferingDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ProductOfferingDTO productSpecCharacterDTO) throws Exception;

    /**
     * Ham tra ve toan bo danh sach mat hang
     *
     * @param lsProductOfferTypeIds
     * @return
     * @author ThanhNT
     */
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTO(String input, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId, List<Long> prodOfferIdList) throws Exception;

    /**
     * ham hien thi danh sach mat hang (vat tu)
     *
     * @param input
     * @param lsProductOfferTypeIds
     * @param ownerId
     * @param ownerType
     * @param stateId
     * @param prodOfferIdList
     * @param stockModelType
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<ProductOfferingTotalDTO> getLsProductOfferingDTOMaterial(String input, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId, List<Long> prodOfferIdList, String stockModelType) throws Exception;

    public List<ProductOfferingTotalDTO> getLsProductOfferingForChangeProduct(String input, Long prodOfferIdOld, boolean isDuplicate) throws Exception;

    public List<ProductOfferingTotalDTO> getLsProductOfferingDTOByPrice(String input, List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId, Long priceTypeId, Long branchId, Long channelTypeId) throws Exception;

    public List<ProductOfferingTotalDTO> getLsProductOfferingIsdnDTO(List<Long> lsProductOfferTypeIds, Long ownerId, String ownerType, String stateId) throws Exception;

    /**
     * Ham tra ve toan bo danh sach mat hang voi status = 1
     *
     * @return
     * @author ThanhNT
     */
    public List<ProductOfferingTotalDTO> getAllLsProductOfferingDTO(String input) throws Exception;

    public List<ProductOfferingTotalDTO> getAllLsProductOfferingDTOForProcessStock(String input, List<Long> lsProductOfferTypeIds) throws Exception;

    /**
     * ham tra ve list danh sach tat ca mat hang(ko quan trong kho)
     *
     * @param input
     * @param lsProductOfferTypeIds
     * @return
     * @throws Exception
     */
    public List<ProductOfferingDTO> getLsProductOfferDTOByType(String input, List<Long> lsProductOfferTypeIds) throws Exception;

    /**
     * Ham tra ve toan bo danh sach mat hang voi status = 1 va la dien thoai
     *
     * @return
     * @author Tuydv1
     */
    public List<ProductOfferingDTO> getListProductOffering() throws LogicException, Exception;

    public List<ProductOfferingTotalDTO> getLsProductOffering(String input) throws Exception;

    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductType(String input, Long prodTypeId) throws Exception;

    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductTypeAndCode(String input, Long prodTypeId, List<String> lstCodes) throws Exception;

    public List<ProductOfferingTotalDTO> getListProductOfferingByProductType(String input, Long prodTypeId) throws Exception;

    public List<ProductOfferingTotalDTO> getListProductOfferingForUpdateSerialGpon(String input, String updateType) throws Exception;

    public ProductOfferingTotalDTO getProductOffering(Long id) throws Exception;

    /**
     * Lay thong tin hang hoa
     *
     * @return
     * @throws LogicException
     * @throws Exception
     * @author Tuydv1
     */
    public List<ProductOfferingDTO> getListProductOfferingByProOfTypeId(Long productOfferTypeId, Long ownerId, String ownerType) throws LogicException, Exception;

    public List<ProductOfferingDTO> getListProductOfferTypeViewer(String ownerType, String ownerId, Long prodOfferId, Long stateId) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferingDTO> getListProductOfferTypeForProcessStock(String ownerType, String ownerId, Long productOfferId, Long stateId) throws Exception;

    public List<ProductOfferingTotalDTO> getListProductOfferingViewer(String ownerType, String ownerId, Long prodOfferId) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferingDTO> findByProductOfferTypeId(Long productOfferTypeId, Long id) throws Exception;

    @WebMethod
    public ProductOfferingDTO findByProductOfferCode(String productOffderCode, String status) throws Exception;

    public List<ProductOfferingTotalDTO> getListProductOfferingTotalDistinct(String ownerType, String ownerId, String stockCode) throws LogicException, Exception;

    @WebMethod
    public List<ProductOfferingTotalDTO> getListProductForTag(String input, ConfigProductTagDTO config);

    @WebMethod
    public ProfileDTO getProfileByProductId(Long productOfferId) throws Exception;

    @WebMethod
    public List<ProductOfferingTotalDTO> getProductOfferingTotalDTO(String input, Long productOfferTypeId, Long ownerId, Long ownerType, Long stateId) throws Exception;

    /**
     * danh sach mat hang vat tu
     *
     * @param input
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<ProductOfferingTotalDTO> getProductOfferingMaterial(String input) throws Exception;

    public List<ProductOfferingTotalDTO> getListProductOfferingForProcessStock(Long ownerType, Long ownerId, Long proOfferId, Long stateId) throws Exception;

    public String getStockStateName(Long stateId);

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

    @WebMethod
    public List<Long> getProdOfferIdForRequestBalanceExport(Long ownerId, Long ownerType, Long prodOfferId);

    @WebMethod
    public List<Long> getProdOfferIdForRequestBalanceImport(Long ownerId, Long ownerType, Long prodOfferId);

    @WebMethod
    public List<ProductOfferingTotalDTO> getProductOfferingByListId(List<Long> prodOfferIds) throws Exception;

    @WebMethod
    public List<ProductOfferingDTO> getLsProductOfferDtoByTypeId(Long productOfferTypeId) throws Exception;

    @WebMethod
    public List<ProductInfoDTO> getLstProductInfo(Long stockTransId) throws Exception;

    @WebMethod
    public ProductOfferingDTO getProductByCode(String prodOfferCode) throws Exception;


    public List<ProductOfferingLogisticDTO> getProductOfferingLogistic(Long stockTransId) throws Exception;

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
     * ham tra ve danh sach mat hang theo loai mat hang, co the kem trang thai
     *
     * @param input
     * @param prodOfferTypeId
     * @param stateId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<ProductOfferingTotalDTO> getLsProOfferTotalDTORequest(String input, Long prodOfferTypeId, Long stateId) throws Exception;

    @WebMethod
    public ProductOfferingDTO getProductByCodeIm1(String prodOfferCode) throws Exception;

    List<ProductOfferingDTO> getInventoryInfoWarranty(List<Long> listProdOfferId) throws Exception;

    public List<ProductOfferingTotalDTO> getProductExportDemo(String input, Long ownerId, String ownerType, String stateId) throws Exception;
}
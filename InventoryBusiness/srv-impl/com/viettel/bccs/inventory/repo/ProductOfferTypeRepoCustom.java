package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ProductOfferTypeDTO;
import com.viettel.bccs.inventory.model.ProductOfferType;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface ProductOfferTypeRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    /**
     * tuydv1
     *
     * @return
     */
    public List<ProductOfferType> getListProductOfferType() throws Exception;

    /**
     * ham tra ve list loai hang hoa co mat hang theo ownerid,ownerType,
     *
     * @param ownerId : shopId cua user dang nhap
     * @return list ProductOfferType
     * @throws Exception
     * @author ThanhNT77
     */
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerId(Long ownerId, String ownerType, Long typeNoSerial) throws Exception;

    /**
     * ham tra ve list loai hang hoa vat tu
     *
     * @param ownerId : shopId cua user dang nhap
     * @return list ProductOfferType
     * @throws Exception
     * @author ThanhNT77
     */
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdMaterial(Long ownerId, String ownerType, Long prodOfferTypeId) throws Exception;

    public List<ProductOfferTypeDTO> getAllProductOfferTypePhone(Long ownerId) throws Exception;

    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdAndTableName(Long ownerId, String ownerType, String tableName) throws Exception;

    public List<ProductOfferTypeDTO> getListByOwnerIdOwnerTypeState(Long ownerId, String ownerType, Long StateId) throws Exception;

    public List<ProductOfferTypeDTO> getListProductOfferTypeChannelAndShop(Long ownerId, String ownerType, Long priceType, Long channelTypeId, Long branchId) throws Exception;

    public List<ProductOfferTypeDTO> getListProduct() throws Exception;

    public List<ProductOfferTypeDTO> getListProductOfferTypeById(List<Long> productOfferTypeId) throws Exception;

    /**
     * ham tra ve danh dach loai mat hang phu thuoc vao ownerId, ownerType, prodOfferId
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<ProductOfferTypeDTO> getListProdOffeTypeForWarranty(Long ownerId, Long ownerType, Long prodOfferId) throws Exception;

    public List<ProductOfferTypeDTO> getListOfferTypeSerial() throws Exception;
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ProductOfferTypeDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ProductOfferTypeService {

    @WebMethod
    public ProductOfferTypeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ProductOfferTypeDTO> findAll() throws Exception;

    @WebMethod
    public List<ProductOfferTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ProductOfferTypeDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ProductOfferTypeDTO productSpecCharacterDTO) throws Exception;

    /**
     * Ham tra ve  toan bo danh sach kho hang hoa
     *
     * @return
     * @author Tuydv1
     */
    public List<ProductOfferTypeDTO> getListProductOfferType() throws LogicException, Exception;

    /**
     * ham tra ve list loai hang hoa co mat hang theo ownerid
     *
     * @param ownerId : shopId cua user dang nhap
     * @return list ProductOfferType
     * @throws Exception
     * @author ThanhNT77
     */
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerId(Long ownerId, String ownerType, Long typeNoSerial) throws Exception;

    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdAndTableName(Long ownerId, String ownerType, String tableName) throws Exception;

    public List<ProductOfferTypeDTO> getAllProductOfferTypePhone(Long ownerId) throws LogicException, Exception;

    /**
     * Tuydv1
     *
     * @param ownerId
     * @param ownerType
     * @param stateId
     * @return
     * @throws Exception
     */
    public List<ProductOfferTypeDTO> getListByOwnerIdOwnerTypeState(Long ownerId, String ownerType, Long stateId) throws Exception;

    /**
     * ham tra ve list loai hang hoa vat tu
     *
     * @param ownerId : shopId cua user dang nhap
     * @return list ProductOfferType
     * @throws Exception
     * @author ThanhNT77
     */
    public List<ProductOfferTypeDTO> getListProductOfferTypeByOwnerIdMaterial(Long ownerId, String ownerType, Long prodOfferTypeId) throws Exception;

    /**
     * Lay product_offerType dat coc theo kenh
     *
     * @param ownerId
     * @param ownerType
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<ProductOfferTypeDTO> getListProductOfferTypeByDeposit(Long ownerId, String ownerType, Long channelTypeId, Long branchId) throws Exception;

    /**
     * Lay product_offer ban dut theo kenh va chi nhanh
     *
     * @param ownerId
     * @param ownerType
     * @param channelTypeId
     * @param branchId
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<ProductOfferTypeDTO> getListProductOfferTypeByPay(Long ownerId, String ownerType, Long channelTypeId, Long branchId) throws Exception;

    @WebMethod
    public List<ProductOfferTypeDTO> getListProduct() throws Exception;

    @WebMethod
    public List<ProductOfferTypeDTO> getListProductOfferTypeById(List<Long> productOfferTypeId) throws Exception;

    @WebMethod
    public List<ProductOfferTypeDTO> getListOfferTypeSerial() throws Exception;
}
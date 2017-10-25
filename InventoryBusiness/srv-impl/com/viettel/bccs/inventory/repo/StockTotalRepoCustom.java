package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.StockManagementForWarranty;
import com.viettel.bccs.inventory.dto.StockTotalDTO;
import com.viettel.bccs.inventory.dto.StockTotalFullDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTotalRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    /**
     * ham lay tong so hang trong kho nhan
     *
     * @param ownerId
     * @return
     * @author ThanhNT
     */
    public Long getTotalValueStock(Long ownerId, String ownerType) throws LogicException, Exception;


    public Long checkAction(StockTotalDTO stockTotalDTO) throws Exception, LogicException;

    public List<ProductOfferingDTO> getProductInStockTotal(Long ownerType, Long ownerId, Long prodOfferId, Long productOfferTypeId) throws Exception, LogicException;

    /**
     * ham tim kiem list mat hang trong kho
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<StockTotalFullDTO> getListStockStockTotalFullForWarranty(Long ownerId, Long ownerType, Long prodOfferTypeId, Long prodOfferId, Long stateId) throws Exception;

    public List<StockTotalDTO> findOneStockTotal(StockTotalDTO stockTotalDTO) throws LogicException, Exception;

    public List<StockManagementForWarranty> viewStockKV(Long shopId, Long prodOfferId, Long stateId) throws Exception;

    public List<StockTotalDTO> getStockTotalAvailableStaff(Long productOfferTypeId) throws Exception, LogicException;

    public List<StockTotalDTO> getStockTotalAvailableShop(Long productOfferTypeId) throws Exception, LogicException;

	/**
     * ham tra ve tong so current_quantity, available_quantity
     * @author thanhnt77
     * @param prodOfferId
     * @param ownerId
     * @return
     * @throws Exception
     */
    public List<Long> getTotalQuantityStockTotal(Long prodOfferId, Long ownerId) throws Exception;

    public boolean checkHaveProductInStockTotal(Long ownerId, Long ownerType) throws Exception;

    public List<StockTotalDTO> getStockTotalAvailable(Long productOfferTypeId) throws Exception, LogicException;

}
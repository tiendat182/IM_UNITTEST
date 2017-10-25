package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.StockBalanceRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.StockBalanceRequest;

public interface StockBalanceRequestRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockBalanceRequest> searchStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws Exception;

    public List<ProductOfferingTotalDTO> getProductOfferingExport(Long ownerType, Long ownerId, Long prodOfferId) throws Exception;

    public List<ProductOfferingTotalDTO> getProductOfferingImport(Long ownerType, Long ownerId, Long prodOfferId) throws Exception;

    public Long getMaxId() throws LogicException, Exception;

    public boolean checkStockBalanceDetail(Long ownerType, Long ownerId, List<Long> prodOfferId) throws Exception;

    public boolean doValidateContraints(Long ownerID, Long staffId) throws Exception;

    public boolean isDulicateRequestCode(String requestCode) throws Exception;
}
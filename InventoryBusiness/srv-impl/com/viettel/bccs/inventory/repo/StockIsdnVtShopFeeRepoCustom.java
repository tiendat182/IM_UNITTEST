package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockIsdnVtShopFeeDTO;
import com.viettel.bccs.inventory.dto.StockTotalFullDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockIsdnVtShopFeeRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockIsdnVtShopFeeDTO> findVtShopFeeByIsdn(String isdn) throws Exception;

    public List<StockTotalFullDTO> findStockByArea(Long shopId, Long parentShopId, Long prodOfferId) throws Exception;

    public List<StockTotalFullDTO> findStockDigital(Long shopId, Long prodOfferId) throws Exception;

    public void deleteFee(String isdn) throws Exception;

}
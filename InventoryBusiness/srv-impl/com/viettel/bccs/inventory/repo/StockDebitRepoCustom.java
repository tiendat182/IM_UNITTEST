package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockDebitDTO;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.StockDebit;

public interface StockDebitRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public StockDebit buildStockDebitFromDebitRequestDetail(DebitRequestDetail debitDetail, String staff) throws Exception;

    public Long totalPriceStock(Long ownerId, String ownerType) throws Exception;

    public StockDebitDTO findLimitStock(Long ownerId, String ownerType, Date createDate) throws Exception;

    public List<StockDebitDTO> searchStockDebitForUnit(Long shopId) throws Exception;

    public List<StockDebitDTO> searchStockDebitForStaff(Long staffId) throws Exception;

    public void updateFinanceType (Long staffId, String financeType);
}
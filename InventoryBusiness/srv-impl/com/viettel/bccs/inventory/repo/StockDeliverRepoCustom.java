package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockDeliverDTO;
import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.StockDeliver;

public interface StockDeliverRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public StockDeliverDTO getStockDeliverByOwnerIdAndStatus(Long ownerId, Long status) throws LogicException, Exception;

    public List<Long> getAllStockForDelete() throws LogicException, Exception;

    public List<StockDeliverDTO> searchHistoryStockDeliver(String code, Date fromDate, Date toDate, String status, Long ownerId, Long ownerType) throws Exception;

    public Long getMaxId() throws LogicException, Exception;

    public boolean isDulicateCode(String requestCode) throws Exception;

}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.StockDeliverDetail;

public interface StockDeliverDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockDeliverDetailDTO> viewStockTotalFull(Long ownerId, Long ownerType) throws Exception;

    public List<StockDeliverDetailDTO> getLstDetailByStockDeliverId(Long stockDeliverId) throws Exception;

}
package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockTransSerialRescueRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockTransSerialRescueDTO> viewDetailSerail(Long stockTransId, Long prodOfferId, Long prodOfferIdReturn) throws LogicException, Exception;

    public List<StockTransSerialRescueDTO> getListDetailSerial(Long stockTransId) throws LogicException, Exception;
}
package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockHandsetRescueDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockHandsetRescueRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockHandsetRescueDTO> getListHansetRescue(Long ownerId) throws LogicException, Exception;

    public List<StockHandsetRescueDTO> getListProductForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception;

    public List<StockHandsetRescueDTO> getListSerialForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception;

    public int updateStatusSerialForRescue(Long statusAffer,Long statusBefor,Long ownerId, String serial, Long prodOfferId) throws LogicException, Exception;
}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.RequestLiquidateDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.RequestLiquidate;

public interface RequestLiquidateRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<RequestLiquidate> getLstRequestLiquidate(RequestLiquidateDTO requestLiquidateDTO) throws Exception;

    public String getRequestCode() throws Exception;

    public byte[] getFileContent(Long requestLiquidateId) throws Exception;

    public boolean checkProdExist(Long shopId, Long prodOfferId) ;

}
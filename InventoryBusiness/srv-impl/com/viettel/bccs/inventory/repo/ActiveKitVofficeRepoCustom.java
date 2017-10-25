package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ActiveKitVofficeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.ActiveKitVoffice;

public interface ActiveKitVofficeRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public ActiveKitVoffice findByChangeModelTransId(Long changeModelTransId) throws Exception;

}
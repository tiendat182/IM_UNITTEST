package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ChangeModelTransDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.ChangeModelTransDetail;

public interface ChangeModelTransDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<ChangeModelTransDetailDTO> viewDetail(Long changeModelTransId) throws Exception;

    List<ChangeModelTransDetailDTO> findModelTransDetailByChangeModelTransId(Long changeModelTransId) throws Exception;
}
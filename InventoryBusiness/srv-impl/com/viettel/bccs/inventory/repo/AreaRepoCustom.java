package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface AreaRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<AreaDTO> getAllProvince() throws Exception;

    public AreaDTO getProcinve(String province) throws Exception;
}
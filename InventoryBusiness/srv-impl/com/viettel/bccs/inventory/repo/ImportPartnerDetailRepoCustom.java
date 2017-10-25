package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.ImportPartnerDetail;

public interface ImportPartnerDetailRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<ImportPartnerDetail> findImportPartnerDetail(ImportPartnerDetailDTO importPartnerDetailDTO) throws Exception;
}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.Partner;

public interface PartnerRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    /**
     * neu muon tim kiem like thi set searchDTO=true
     *
     * @param partnerDTO
     * @return
     */
    public List<Partner> findPartner(PartnerDTO partnerDTO);
}
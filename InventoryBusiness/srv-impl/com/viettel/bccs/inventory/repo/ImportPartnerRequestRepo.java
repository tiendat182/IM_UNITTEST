package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ImportPartnerRequest;
import com.viettel.fw.persistence.BaseRepository;

public interface ImportPartnerRequestRepo extends BaseRepository<ImportPartnerRequest, Long>, ImportPartnerRequestRepoCustom {

}
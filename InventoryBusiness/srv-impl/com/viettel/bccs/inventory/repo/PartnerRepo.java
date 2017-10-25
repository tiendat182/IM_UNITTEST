package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Partner;
import com.viettel.fw.persistence.BaseRepository;

public interface PartnerRepo extends BaseRepository<Partner, Long>, PartnerRepoCustom {

}
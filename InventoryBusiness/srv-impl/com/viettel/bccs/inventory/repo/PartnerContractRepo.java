package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.PartnerContract;
import com.viettel.fw.persistence.BaseRepository;

public interface PartnerContractRepo extends BaseRepository<PartnerContract, Long>, PartnerContractRepoCustom {

}
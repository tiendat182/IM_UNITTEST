package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.FinanceType;
import com.viettel.fw.persistence.BaseRepository;

public interface FinanceTypeRepo extends BaseRepository<FinanceType, Long>, FinanceTypeRepoCustom {

}
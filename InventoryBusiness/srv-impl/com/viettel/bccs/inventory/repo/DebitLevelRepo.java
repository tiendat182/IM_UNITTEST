package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.DebitLevel;
import com.viettel.fw.persistence.BaseRepository;

public interface DebitLevelRepo extends BaseRepository<DebitLevel, Long>, DebitLevelRepoCustom {

}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.NumberAction;
import com.viettel.fw.persistence.BaseRepository;

public interface NumberActionRepo extends BaseRepository<NumberAction, Long>, NumberActionRepoCustom {
}
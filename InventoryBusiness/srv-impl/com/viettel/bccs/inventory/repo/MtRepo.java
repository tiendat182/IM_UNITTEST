package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Mt;
import com.viettel.fw.persistence.BaseRepository;

public interface MtRepo extends BaseRepository<Mt, Long>, MtRepoCustom {
}
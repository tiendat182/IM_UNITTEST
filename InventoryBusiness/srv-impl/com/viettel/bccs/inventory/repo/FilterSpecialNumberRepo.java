package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.FilterSpecialNumber;
import com.viettel.fw.persistence.BaseRepository;

public interface FilterSpecialNumberRepo extends BaseRepository<FilterSpecialNumber, Long>, FilterSpecialNumberRepoCustom {
}
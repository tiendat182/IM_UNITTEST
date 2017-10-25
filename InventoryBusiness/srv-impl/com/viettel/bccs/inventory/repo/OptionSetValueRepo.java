package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.fw.persistence.BaseRepository;

public interface OptionSetValueRepo extends BaseRepository<OptionSetValue, Long>, OptionSetValueRepoCustom {

}
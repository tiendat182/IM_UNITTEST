package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.DebitDayType;
import com.viettel.fw.persistence.BaseRepository;

public interface DebitDayTypeRepo extends BaseRepository<DebitDayType, Long>, DebitDayTypeRepoCustom {

}
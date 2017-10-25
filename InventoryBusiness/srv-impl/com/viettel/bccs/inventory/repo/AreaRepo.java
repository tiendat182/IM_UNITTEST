package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Area;
import com.viettel.fw.persistence.BaseRepository;

public interface AreaRepo extends BaseRepository<Area, Long>, AreaRepoCustom {

}
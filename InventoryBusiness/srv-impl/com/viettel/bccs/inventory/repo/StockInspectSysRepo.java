package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockInspectSys;
import com.viettel.fw.persistence.BaseRepository;

public interface StockInspectSysRepo extends BaseRepository<StockInspectSys, Long>, StockInspectSysRepoCustom {

}
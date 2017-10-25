package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockInspect;
import com.viettel.fw.persistence.BaseRepository;

public interface StockInspectRepo extends BaseRepository<StockInspect, Long>, StockInspectRepoCustom {

}
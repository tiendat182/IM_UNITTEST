package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockSimInfo;
import com.viettel.fw.persistence.BaseRepository;

public interface StockSimInfoRepo extends BaseRepository<StockSimInfo, Long>, StockSimInfoRepoCustom {

}
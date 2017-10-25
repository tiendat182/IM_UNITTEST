package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockSim;
import com.viettel.fw.persistence.BaseRepository;

public interface StockSimRepo extends BaseRepository<StockSim, Long>, StockSimRepoCustom {

}
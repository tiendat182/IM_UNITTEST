package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTotal;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTotalRepo extends BaseRepository<StockTotal, Long>, StockTotalRepoCustom {

}
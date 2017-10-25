package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTransRescue;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransRescueRepo extends BaseRepository<StockTransRescue, Long>, StockTransRescueRepoCustom {

}
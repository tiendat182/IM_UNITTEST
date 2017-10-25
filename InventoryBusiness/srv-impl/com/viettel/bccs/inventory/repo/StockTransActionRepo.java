package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransActionRepo extends BaseRepository<StockTransAction, Long>, StockTransActionRepoCustom {
}
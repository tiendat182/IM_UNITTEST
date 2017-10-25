package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTransOffline;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransOfflineRepo extends BaseRepository<StockTransOffline, Long>, StockTransOfflineRepoCustom {

}
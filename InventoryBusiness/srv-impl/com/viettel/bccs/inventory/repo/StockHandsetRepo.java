package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockHandset;
import com.viettel.fw.persistence.BaseRepository;

public interface StockHandsetRepo extends BaseRepository<StockHandset, Long>, StockHandsetRepoCustom {

}
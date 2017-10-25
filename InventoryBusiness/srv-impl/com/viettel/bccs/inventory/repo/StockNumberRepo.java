package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockNumber;
import com.viettel.fw.persistence.BaseRepository;

public interface StockNumberRepo extends BaseRepository<StockNumber, Long>, StockNumberRepoCustom {

}
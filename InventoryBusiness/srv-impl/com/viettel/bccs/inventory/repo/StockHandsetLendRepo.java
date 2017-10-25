package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockHandsetLend;
import com.viettel.fw.persistence.BaseRepository;

public interface StockHandsetLendRepo extends BaseRepository<StockHandsetLend, Long>, StockHandsetLendRepoCustom {
    public Long getSequence() throws Exception;

}
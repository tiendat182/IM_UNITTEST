package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockDebit;
import com.viettel.fw.persistence.BaseRepository;

public interface StockDebitRepo extends BaseRepository<StockDebit, Long>, StockDebitRepoCustom {

}
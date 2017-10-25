package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockBalanceSerial;
import com.viettel.fw.persistence.BaseRepository;

public interface StockBalanceSerialRepo extends BaseRepository<StockBalanceSerial, Long>, StockBalanceSerialRepoCustom {

}
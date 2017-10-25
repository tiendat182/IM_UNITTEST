package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransSerialRepo extends BaseRepository<StockTransSerial, Long>, StockTransSerialRepoCustom {

}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransRepo extends BaseRepository<StockTrans, Long>, StockTransRepoCustom {

}
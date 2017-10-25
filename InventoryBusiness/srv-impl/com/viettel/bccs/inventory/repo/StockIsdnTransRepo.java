package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockIsdnTrans;
import com.viettel.fw.persistence.BaseRepository;

public interface StockIsdnTransRepo extends BaseRepository<StockIsdnTrans, Long>, StockIsdnTransRepoCustom {

}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockIsdnTransDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface StockIsdnTransDetailRepo extends BaseRepository<StockIsdnTransDetail, Long>, StockIsdnTransDetailRepoCustom {

}
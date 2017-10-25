package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransDetailRepo extends BaseRepository<StockTransDetail, Long>, StockTransDetailRepoCustom {

}
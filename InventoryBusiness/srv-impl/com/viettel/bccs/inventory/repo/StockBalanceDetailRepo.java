package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockBalanceDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface StockBalanceDetailRepo extends BaseRepository<StockBalanceDetail, Long>, StockBalanceDetailRepoCustom {

}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockBalanceRequest;
import com.viettel.fw.persistence.BaseRepository;

public interface StockBalanceRequestRepo extends BaseRepository<StockBalanceRequest, Long>, StockBalanceRequestRepoCustom {

}
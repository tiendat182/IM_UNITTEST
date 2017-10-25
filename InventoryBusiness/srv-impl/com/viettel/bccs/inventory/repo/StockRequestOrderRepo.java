package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockRequestOrder;
import com.viettel.fw.persistence.BaseRepository;

public interface StockRequestOrderRepo extends BaseRepository<StockRequestOrder, Long>, StockRequestOrderRepoCustom {
}
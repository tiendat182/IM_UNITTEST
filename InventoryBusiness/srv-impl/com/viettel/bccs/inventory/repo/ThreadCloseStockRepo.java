package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ThreadCloseStock;
import com.viettel.fw.persistence.BaseRepository;

public interface ThreadCloseStockRepo extends BaseRepository<ThreadCloseStock, Long>, ThreadCloseStockRepoCustom {

}
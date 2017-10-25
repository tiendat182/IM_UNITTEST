package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockOrderAgentDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface StockOrderAgentDetailRepo extends BaseRepository<StockOrderAgentDetail, Long>, StockOrderAgentDetailRepoCustom {

}
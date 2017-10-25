package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockOrderAgent;
import com.viettel.fw.persistence.BaseRepository;

public interface StockOrderAgentRepo extends BaseRepository<StockOrderAgent, Long>, StockOrderAgentRepoCustom {


}
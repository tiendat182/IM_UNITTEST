package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockCard;
import com.viettel.fw.persistence.BaseRepository;

public interface StockCardRepo extends BaseRepository<StockCard, Long>, StockCardRepoCustom {

}
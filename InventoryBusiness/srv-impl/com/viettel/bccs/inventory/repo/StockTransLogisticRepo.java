
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTransLogistic;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransLogisticRepo extends BaseRepository<StockTransLogistic, Long>, StockTransLogisticRepoCustom {

}
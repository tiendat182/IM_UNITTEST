package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockRequest;
import com.viettel.fw.persistence.BaseRepository;

/**
 * Created by vanho on 23/03/2017.
 */
public interface OrderDivideDeviceRepo extends BaseRepository<StockRequest, Long>, OrderDivideDeviceRepoCustom {
}

package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockRequest;
import com.viettel.fw.persistence.BaseRepository;

/**
 * Created by vanho on 26/03/2017.
 */
public interface ApproachOrderDivideDeviceRepo extends BaseRepository<StockRequest, Long>, ApproachOrderDivideDeviceRepoCustom {
}

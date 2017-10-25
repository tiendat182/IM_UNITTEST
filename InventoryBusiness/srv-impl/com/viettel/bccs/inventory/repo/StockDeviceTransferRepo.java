package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockDeviceTransfer;
import com.viettel.fw.persistence.BaseRepository;

public interface StockDeviceTransferRepo extends BaseRepository<StockDeviceTransfer, Long>, StockDeviceTransferRepoCustom {

}
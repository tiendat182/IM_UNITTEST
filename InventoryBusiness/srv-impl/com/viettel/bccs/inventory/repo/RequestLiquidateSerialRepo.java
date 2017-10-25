package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.RequestLiquidateSerial;
import com.viettel.fw.persistence.BaseRepository;

public interface RequestLiquidateSerialRepo extends BaseRepository<RequestLiquidateSerial, Long>, RequestLiquidateSerialRepoCustom {

}
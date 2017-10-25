package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.EquipmentVendor;
import com.viettel.fw.persistence.BaseRepository;

public interface EquipmentVendorRepo extends BaseRepository<EquipmentVendor, Long>, EquipmentVendorRepoCustom {

}
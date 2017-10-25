package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Equipment;
import com.viettel.fw.persistence.BaseRepository;

public interface EquipmentRepo extends BaseRepository<Equipment, Long>, EquipmentRepoCustom {

}
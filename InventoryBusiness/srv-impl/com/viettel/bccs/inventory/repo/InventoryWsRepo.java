package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.fw.persistence.BaseRepository;

public interface InventoryWsRepo extends BaseRepository<ProductOffering, Long>, InventoryWsRepoCustom {

}
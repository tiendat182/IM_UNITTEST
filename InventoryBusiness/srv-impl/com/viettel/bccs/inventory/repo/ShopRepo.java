package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Shop;
import com.viettel.fw.persistence.BaseRepository;

public interface ShopRepo extends BaseRepository<Shop, Long>, ShopRepoCustom {

}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ProductErp;
import com.viettel.fw.persistence.BaseRepository;

public interface ProductErpRepo extends BaseRepository<ProductErp, Long>, ProductErpRepoCustom {

}
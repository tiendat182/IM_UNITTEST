package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ProductOfferType;
import com.viettel.fw.persistence.BaseRepository;

public interface ProductOfferTypeRepo extends BaseRepository<ProductOfferType, Long>, ProductOfferTypeRepoCustom {

}
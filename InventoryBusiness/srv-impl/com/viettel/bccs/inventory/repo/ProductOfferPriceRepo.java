package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ProductOfferPrice;
import com.viettel.fw.persistence.BaseRepository;

public interface ProductOfferPriceRepo extends BaseRepository<ProductOfferPrice, Long>, ProductOfferPriceRepoCustom {

}
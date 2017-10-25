package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.Exchange;
import com.viettel.fw.persistence.BaseRepository;

public interface ExchangeRepo extends BaseRepository<Exchange, Long>, ExchangeRepoCustom {

}
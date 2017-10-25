package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransVofficeRepo extends BaseRepository<StockTransVoffice, Long>, StockTransVofficeRepoCustom {



}
package com.viettel.bccs.im1.repo;

import com.viettel.bccs.im1.model.StockDebitIm1;
import com.viettel.bccs.inventory.model.StockDebit;
import com.viettel.fw.persistence.BaseRepository;

public interface StockDebitIm1Repo extends BaseRepository<StockDebitIm1, Long>, StockDebitIm1RepoCustom {

}
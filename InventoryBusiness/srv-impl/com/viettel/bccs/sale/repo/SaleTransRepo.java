package com.viettel.bccs.sale.repo;

import com.viettel.bccs.sale.model.SaleTrans;
import com.viettel.fw.persistence.BaseRepository;

public interface SaleTransRepo extends BaseRepository<SaleTrans, Long>, SaleTransRepoCustom {
}
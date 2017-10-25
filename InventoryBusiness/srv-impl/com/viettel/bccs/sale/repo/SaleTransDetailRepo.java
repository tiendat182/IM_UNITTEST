package com.viettel.bccs.sale.repo;

import com.viettel.bccs.sale.model.SaleTransDetail;
import com.viettel.fw.persistence.BaseRepository;

public interface SaleTransDetailRepo extends BaseRepository<SaleTransDetail, Long>, SaleTransDetailRepoCustom {
}
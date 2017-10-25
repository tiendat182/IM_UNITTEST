package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.ReportChangeGoods;
import com.viettel.fw.persistence.BaseRepository;

public interface ReportChangeGoodsRepo extends BaseRepository<ReportChangeGoods, Long>, ReportChangeGoodsRepoCustom {

}
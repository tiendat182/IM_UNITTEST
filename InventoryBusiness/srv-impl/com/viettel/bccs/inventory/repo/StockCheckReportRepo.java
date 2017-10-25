package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import com.viettel.bccs.inventory.model.StockCheckReport;

public interface StockCheckReportRepo extends BaseRepository<StockCheckReport, Long>, StockCheckReportRepoCustom {

}
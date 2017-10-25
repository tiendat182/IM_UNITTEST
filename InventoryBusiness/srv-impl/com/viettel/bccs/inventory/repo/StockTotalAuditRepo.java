package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTotalAudit;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTotalAuditRepo extends BaseRepository<StockTotalAudit, Long>, StockTotalAuditRepoCustom {

}
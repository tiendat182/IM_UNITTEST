package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.InvoiceActionLog;
import com.viettel.fw.persistence.BaseRepository;

public interface InvoiceActionLogRepo extends BaseRepository<InvoiceActionLog, Long>, InvoiceActionLogRepoCustom {

}
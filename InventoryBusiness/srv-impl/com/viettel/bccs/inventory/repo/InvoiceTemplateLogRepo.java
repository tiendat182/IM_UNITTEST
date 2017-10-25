package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.InvoiceTemplateLog;
import com.viettel.fw.persistence.BaseRepository;

public interface InvoiceTemplateLogRepo extends BaseRepository<InvoiceTemplateLog, Long>, InvoiceTemplateLogRepoCustom {

}
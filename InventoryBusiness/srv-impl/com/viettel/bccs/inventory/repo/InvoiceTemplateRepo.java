package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.InvoiceTemplate;
import com.viettel.fw.persistence.BaseRepository;

public interface InvoiceTemplateRepo extends BaseRepository<InvoiceTemplate, Long>, InvoiceTemplateRepoCustom {

}
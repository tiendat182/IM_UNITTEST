package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.InvoiceTemplateType;
import com.viettel.fw.persistence.BaseRepository;

public interface InvoiceTemplateTypeRepo extends BaseRepository<InvoiceTemplateType, Long>, InvoiceTemplateTypeRepoCustom {

}
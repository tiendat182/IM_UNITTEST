package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.InvoiceType;
import com.viettel.fw.persistence.BaseRepository;

public interface InvoiceTypeRepo extends BaseRepository<InvoiceType, Long>, InvoiceTypeRepoCustom {


}
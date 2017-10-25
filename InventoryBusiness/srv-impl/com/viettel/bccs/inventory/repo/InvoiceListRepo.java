package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.InvoiceList;
import com.viettel.fw.persistence.BaseRepository;

public interface InvoiceListRepo extends BaseRepository<InvoiceList, Long>, InvoiceListRepoCustom {

}
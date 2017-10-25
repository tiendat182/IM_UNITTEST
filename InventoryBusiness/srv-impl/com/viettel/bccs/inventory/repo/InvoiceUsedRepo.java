package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.InvoiceUsed;
import com.viettel.fw.persistence.BaseRepository;

/**
 * Created by pham on 9/26/2016.
 */
public interface InvoiceUsedRepo extends BaseRepository<InvoiceUsed,Long>, InvoiceUsedRepoCustom {
}

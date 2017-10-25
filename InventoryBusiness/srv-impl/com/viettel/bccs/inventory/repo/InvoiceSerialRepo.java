package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.InvoiceSerial;
import com.viettel.fw.persistence.BaseRepository;

public interface InvoiceSerialRepo extends BaseRepository<InvoiceSerial, Long>, InvoiceSerialRepoCustom {

}
package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockDocument;
import com.viettel.fw.persistence.BaseRepository;

public interface StockDocumentRepo extends BaseRepository<StockDocument, Long>, StockDocumentRepoCustom {

}
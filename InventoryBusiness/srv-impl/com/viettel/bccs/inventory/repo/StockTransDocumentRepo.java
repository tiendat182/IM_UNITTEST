package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.model.StockTransDocument;
import com.viettel.fw.persistence.BaseRepository;

public interface StockTransDocumentRepo extends BaseRepository<StockTransDocument, Long>, StockTransDocumentRepoCustom {

}
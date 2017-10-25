package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.lang.Long;
import com.viettel.bccs.inventory.model.StockRequest;

public interface StockRequestRepo extends BaseRepository<StockRequest, Long>, StockRequestRepoCustom {

}
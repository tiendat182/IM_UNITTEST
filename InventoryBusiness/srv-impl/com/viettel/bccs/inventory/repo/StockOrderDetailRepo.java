package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Long;
import com.viettel.bccs.inventory.model.StockOrderDetail;

public interface StockOrderDetailRepo extends BaseRepository<StockOrderDetail, Long>, StockOrderDetailRepoCustom {

}
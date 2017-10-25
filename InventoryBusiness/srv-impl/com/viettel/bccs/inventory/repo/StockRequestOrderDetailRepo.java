package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Long;
import com.viettel.bccs.inventory.model.StockRequestOrderDetail;

public interface StockRequestOrderDetailRepo extends BaseRepository<StockRequestOrderDetail, Long>, StockRequestOrderDetailRepoCustom {

}
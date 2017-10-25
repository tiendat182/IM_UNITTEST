package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.math.BigInteger;
import com.viettel.bccs.inventory.model.StockTransExt;

public interface StockTransExtRepo extends BaseRepository<StockTransExt, Long>, StockTransExtRepoCustom {

}
package com.viettel.bccs.inventory.repo;
import com.viettel.fw.persistence.BaseRepository;
import java.util.List;
import java.lang.Long;
import com.viettel.bccs.inventory.model.StockIsdnVtShopLock;

public interface StockIsdnVtShopLockRepo extends BaseRepository<StockIsdnVtShopLock, Long>, StockIsdnVtShopLockRepoCustom {

}
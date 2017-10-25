package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.dto.StockDepositDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;
import com.viettel.bccs.inventory.model.StockDeposit;
public interface StockDepositRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 public StockDepositDTO getStockDeposit(Long prodOfferId, Long channelTypeId, Long transType) throws Exception;
}
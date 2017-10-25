package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.dto.LookupSerialCardAndKitByFileDTO;
import com.viettel.bccs.inventory.dto.StockKitDTO;
import com.viettel.bccs.inventory.dto.StockKitIm1DTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;
import com.viettel.bccs.inventory.model.StockKit;
public interface StockKitRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 StockKitDTO getStockKitBySerial(String serial);

 List<StockKitDTO> getStockKitBySerialAndProdOfferId(Long staffId, String fromSerial, String toSerial, Long prodOfferId);

 public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serialList) throws Exception;

 List<StockKitIm1DTO> findStockKitAndHisStockKitBySerial(String serial);

 List<StockKitDTO> getStatusStockKit(Long prodOfferId, String serial);
}
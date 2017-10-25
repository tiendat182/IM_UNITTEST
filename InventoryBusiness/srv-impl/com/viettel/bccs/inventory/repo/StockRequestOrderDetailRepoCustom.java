package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.dto.StockRequestOrderDetailDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;
import com.viettel.bccs.inventory.model.StockRequestOrderDetail;
public interface StockRequestOrderDetailRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 /**
  * ham tim kiem chi tiet yeu cau
  * @author thanhnt77
  * @param stockRequestOrderId
  * @param status
  * @return
  * @throws Exception
     */
 public List<StockRequestOrderDetailDTO> getListStockRequestOrderDetail(Long stockRequestOrderId, String status) throws Exception;

 public List<StockRequestOrderDetailDTO> getLstDetailTemplate(Long stockRequestOrderId) throws Exception;

 public List<StockRequestOrderDetailDTO> getLstDetailToExport() throws Exception;

 public int updateCancelNote(Long stockRequestOrderId) throws Exception;
 
 /**
  * xoa order detail
  * @author thanhnt77
  * @param stockRequestOrderId
  * @return
  * @throws Exception
     */
 public int deleteStockRequestOrderDetai(Long stockRequestOrderId, String status) throws Exception;

}
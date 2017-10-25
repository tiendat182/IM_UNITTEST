package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;
import com.viettel.bccs.inventory.model.StockRequest;
public interface StockRequestRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 /**
  * hien thi max request
  * @author thanhnt77
  * @return
  * @throws LogicException
  * @throws Exception
  */
 public Long getMaxId() throws Exception;

 /**
  * ham xu ly tim kiem yeu cau doa
  * @author thanhnt77
  * @param requestCode
  * @param fromDate
  * @param toDate
  * @param status
  * @param ownerId
  * @param ownerType
  * @return
  * @throws Exception
  */
 List<StockRequestDTO> getLsSearchStockRequest(String requestCode, Date fromDate, Date toDate, String status, Long shopIdLogin, Long ownerId, Long ownerType) throws Exception;


 /**
  * ham tra ve danh sach tim kiem yeu cau, dung cho trung tam bao hanh
  * @author thanhnt77
  * @param requestCode
  * @param fromDate
  * @param toDate
  * @return
     * @throws Exception
     */
 public List<StockRequestDTO> getLsSearchStockRequestTTBH(String requestCode, Date fromDate, Date toDate, String status, Long ownerId) throws Exception;

}
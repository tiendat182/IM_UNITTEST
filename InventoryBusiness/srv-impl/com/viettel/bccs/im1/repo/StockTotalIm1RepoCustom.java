package com.viettel.bccs.im1.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.im1.dto.*;
import com.viettel.bccs.inventory.dto.StockHandsetDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;
public interface StockTotalIm1RepoCustom {

 public Predicate toPredicate(List<FilterRequest> filters);

 public List<StockTransDetailIm1DTO> findStockTransDetail(Long stockTransId) throws LogicException,Exception;

 public List<StockTotalIm1DTO> findOneStockTotal(StockTotalIm1DTO stockTotalDTO) throws LogicException,Exception;

 public StockModelIm1DTO findOneStockModel(Long stockModelId) throws LogicException,Exception;

 /**
  * tim kiem stockModelIM1
  * @author thanhnt77
  * @param prodOfferId
  * @param serial
  * @param ownerType
  * @param ownerId
  * @param stateId
  * @return
  * @throws LogicException
     * @throws Exception
     */
 public StockHandsetDTO getStockHandsetIm1(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long stateId) throws LogicException, Exception;

 public StockTypeIm1DTO findOneStockType(Long stockModelId) throws LogicException,Exception;

 public List<StockTransSerialIm1DTO> findSerialByStockTransDetail(StockTransDetailIm1DTO stockTransDetail) throws LogicException,Exception;

 public int createStockTotal(StockTotalIm1DTO dto) throws LogicException, Exception;

 public int updateStockTotal(StockTotalIm1DTO dto) throws LogicException, Exception;

}
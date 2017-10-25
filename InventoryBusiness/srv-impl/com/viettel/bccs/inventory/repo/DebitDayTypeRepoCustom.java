package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.DebitDayTypeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;
public interface DebitDayTypeRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 public  List<DebitDayTypeDTO> searchDebitDayType(DebitDayTypeDTO debitDayTypeDTO);

 /**
  * @param debitRequestId debit request id
  * @return mảng byte chứa nội dung file
  * @throws Exception
  */
 public byte[] getAttachFileContent(Long debitRequestId) throws Exception;

}
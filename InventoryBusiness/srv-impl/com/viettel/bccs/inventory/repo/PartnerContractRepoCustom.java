package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

public interface PartnerContractRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 /**
  * ham xu ly tim kiem hop dong
  * @author thanhnt77
  * @param contractCode
  * @param prodOfferCode
  * @param fromDate
  * @param toDate
  * @return
     * @throws Exception
     */
 public List<String> searchContract(String contractCode, String prodOfferCode, Date fromDate, Date toDate) throws Exception;

}
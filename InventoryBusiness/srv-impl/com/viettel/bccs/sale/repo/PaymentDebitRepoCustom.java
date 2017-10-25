package com.viettel.bccs.sale.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;
import com.viettel.bccs.sale.model.PaymentDebit;
public interface PaymentDebitRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters); 

}
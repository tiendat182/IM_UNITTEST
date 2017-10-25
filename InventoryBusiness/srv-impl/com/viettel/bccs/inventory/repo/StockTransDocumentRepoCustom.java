package com.viettel.bccs.inventory.repo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;
import com.viettel.bccs.inventory.model.StockTransDocument;
public interface StockTransDocumentRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 /**
  * ham tra ve noi dung file upload
  * @author thanhnt77
  * @param stockTransId
  * @return
  * @throws Exception
     */
 public byte[] getAttachFileContent(Long stockTransId) throws Exception;

}
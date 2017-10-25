package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.dto.ReportChangeHandsetDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;
import com.viettel.bccs.inventory.model.ReportChangeHandset;
public interface ReportChangeHandsetRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 /**
  * ham tra ve danh sach doi hang dat coc
  * @author thanhnt77
  * @param prodOfferId
  * @param serial
  * @param shopPath
  * @return
  * @throws Exception
  */
 public List<ReportChangeHandsetDTO> getLsReportChangeHandsetTerminal(Long prodOfferId, String serial, String shopPath) throws Exception;
}
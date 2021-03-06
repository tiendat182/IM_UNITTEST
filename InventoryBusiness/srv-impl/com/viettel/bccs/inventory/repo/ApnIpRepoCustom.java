package com.viettel.bccs.inventory.repo;
import com.viettel.bccs.inventory.dto.ApnIpDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;
import java.util.List;
import com.viettel.bccs.inventory.model.ApnIp;
public interface ApnIpRepoCustom {
 public Predicate toPredicate(List<FilterRequest> filters);

 public List<ApnIp> checkDuplicateApnIp(List<ApnIpDTO> listApn);

 public List<ApnIp> search(ApnIp apnIp) throws Exception;

}
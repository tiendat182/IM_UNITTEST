package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.ImsiInfoDTO;
import com.viettel.bccs.inventory.dto.ImsiInfoInputSearch;
import com.viettel.bccs.inventory.dto.UpdateImsiInfoDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface ImsiInfoRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<ImsiInfoDTO> search(ImsiInfoInputSearch imsiInfoInputSearch) throws Exception;

    public Long countImsiRange(String fromImsi, String toImsi) throws Exception;

    public Long countImsiRangeToCheckDelete(String fromImsi, String toImsi) throws Exception;

    public void deleteImsi(String fromImsi, String toImsi) throws Exception;

    public void updateBatch(ImsiInfoDTO imsiInfoDTO) throws Exception;

    public List<UpdateImsiInfoDTO> getTransactionToUpdateImsi() throws Exception;

    public List<UpdateImsiInfoDTO> getImsiHasFillPartner() throws Exception;

    public void updateImsiInfoToHasUsedSim(UpdateImsiInfoDTO updateImsiInfoDTOS) throws Exception;

    public void updateImsiInfoHasConnectSim(UpdateImsiInfoDTO updateImsiInfoDTOS) throws Exception;


}
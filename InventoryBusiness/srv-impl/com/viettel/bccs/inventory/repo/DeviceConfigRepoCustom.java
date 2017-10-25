package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.DeviceConfigDTO;
import com.viettel.bccs.inventory.model.DeviceConfig;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface DeviceConfigRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);
    public List<DeviceConfigDTO> searchDeviceByProductOfferIdAndStateId(Long productOfferId, Long stateId) throws Exception;
    public boolean checkProductIdConfig(Long id,Long probOfferId, Long stateId, Long newProbOfferId) throws Exception;
    public boolean checkProductIsConfigWithState(Long probOfferId, Long stateId) throws Exception;

    public boolean checkProductIsConfigWithState(Long probOfferId, Long stateId, boolean isUpdate) throws Exception;
    public boolean checkCanDelete(Long probOfferId, Long stateId) throws Exception;
    public String getStateStr(Long stateId);
    public List<DeviceConfigDTO> getDeviceConfigInfo();
/*    public List<DeviceConfig> getDeviceConfigByProductAndState(Long probOfferId, Long stateId);*/
}
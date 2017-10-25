package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.LockUserInfoMsgDTO;
import com.viettel.bccs.inventory.dto.LockUserTypeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.List;

import com.viettel.bccs.inventory.model.LockUserType;

public interface LockUserTypeRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<LockUserTypeDTO> getAllUserLockType() throws Exception;

    public List<LockUserInfoMsgDTO> getLockUserInfo(Long staffId) throws Exception;
}
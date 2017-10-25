package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.dto.LockUserTypeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.LockUserInfo;

public interface LockUserInfoRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<LockUserInfoDTO> getLockUserInfo(String sql, Long lockTypeId, Long validRange, Long checkRange) throws Exception;

    public boolean checkExistLockUserInfo(Long transId, Long transType, Long transStatus) throws Exception;

    public List<LockUserInfoDTO> getLstLockUserInfo() throws Exception;

    public boolean canUnlock(String sql, Long transId, Date transDate) throws Exception;

    public void deleteLockUser(List<Long> lstId) throws Exception;

    public List<LockUserInfo> findByStaffId(Long staffId) throws Exception;

    public List<LockUserInfoDTO> getListLockUserV2(Long staffId, String lockTypeId, String stockTransType) throws Exception;

    public void unlockUserStockInspect() throws Exception;
}
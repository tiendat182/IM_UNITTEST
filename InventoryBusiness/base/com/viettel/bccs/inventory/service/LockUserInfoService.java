package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.model.LockUserInfo;

import java.util.Date;
import java.util.List;

import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface LockUserInfoService {

    @WebMethod
    public LockUserInfoDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<LockUserInfoDTO> findAll() throws Exception;

    @WebMethod
    public List<LockUserInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(LockUserInfoDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(LockUserInfoDTO productSpecCharacterDTO) throws Exception;

    public BaseMessage deleteLockUser(List<Long> lstId) throws Exception;

    public List<LockUserInfoDTO> getLockUserInfo(String sql, Long lockTypeId, Long validRange, Long checkRange) throws Exception;

    public boolean checkExistLockUserInfo(Long transId, Long transType, Long transStatus) throws Exception;

    public List<LockUserInfoDTO> getLstLockUserInfo() throws Exception;

    public boolean canUnlock(String sql, Long transId, Date transDate) throws Exception;

    @WebMethod
    // Tim kiem giao dich treo bi khoa
    public List<LockUserInfoDTO> getListLockUser(String staffCode, String lockTypeId, String stockTransType) throws Exception;

    @WebMethod
        // xoa giao dich treo
    BaseMessage delete(String deleteId, String userId) throws Exception;

    public void processProcedureLockUserDamage(String procedureName);

    public void unlockUserStockInspect() throws Exception;

}
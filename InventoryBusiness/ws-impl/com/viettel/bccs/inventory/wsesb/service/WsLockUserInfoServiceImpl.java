package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import com.viettel.bccs.inventory.service.LockUserInfoService;

import java.util.Date;
import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsLockUserInfoServiceImpl")
public class WsLockUserInfoServiceImpl implements LockUserInfoService {

    public static final Logger logger = Logger.getLogger(WsLockUserInfoServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private LockUserInfoService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(LockUserInfoService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public LockUserInfoDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<LockUserInfoDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<LockUserInfoDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(LockUserInfoDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(LockUserInfoDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    public BaseMessage deleteLockUser(List<Long> lstId) throws Exception {
        return client.deleteLockUser(lstId);
    }

    @Override
    public List<LockUserInfoDTO> getLockUserInfo(String sql, Long lockTypeId, Long validRange, Long checkRange) throws Exception {
        return client.getLockUserInfo(sql, lockTypeId, validRange, checkRange);
    }

    @Override
    public boolean checkExistLockUserInfo(Long transId, Long transType, Long transStatus) throws Exception {
        return client.checkExistLockUserInfo(transId, transType, transStatus);
    }

    @Override
    public List<LockUserInfoDTO> getLstLockUserInfo() throws Exception {
        return client.getLstLockUserInfo();
    }

    @Override
    public boolean canUnlock(String sql, Long transId, Date transDate) throws Exception {
        return client.canUnlock(sql, transId, transDate);
    }

    @Override
    public List<LockUserInfoDTO> getListLockUser(String staffCode, String lockTypeId, String stockTransType) throws Exception {
        return client.getListLockUser(staffCode, lockTypeId, stockTransType);
    }

    @Override
    public BaseMessage delete(String deleteId, String userId) throws Exception {
        return client.delete(deleteId, userId);
    }

    @Override
    public void processProcedureLockUserDamage(String procedureName) {
        client.processProcedureLockUserDamage(procedureName);
    }

    @Override
    public void unlockUserStockInspect() throws Exception {
        client.unlockUserStockInspect();
    }
}
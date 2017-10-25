package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.StockInspect;
import com.viettel.fw.dto.BaseMessage;

public interface StockInspectRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<ApproveStockInspectDTO> searchApproveInspect(Long approveUserId, Long ownerId, Long ownerType, Long approveStatus, Long prodOfferTypeId, String code, Date fromDate, Date toDate) throws Exception;

    public List<Long> getStockTotalMinusStockInspect(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate, boolean isCheckStockTotal) throws Exception;

    public boolean checkExistSpecNotFinish(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws Exception;

    public List<StockInspect> getStockInspect(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId,
                                              Long prodOfferId, Date fromDate, Date toDate, Long isFinsh, Long approveStatus) throws Exception;

    public List<StockInspectCheckDTO> getStockInspectForQuantity(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws Exception;

    public List<StockInspectCheckDTO> getStockInspectForNoSerial(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws Exception;

    public List<StockInspectCheckDTO> getStockInspectForSerial(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws Exception;

    public Long getQuantityStockTotal(Long ownerType, Long ownerId, Long productOfferTypeId, Long prodOfferId, Long stateId) throws Exception;

    public void updateStockInspectAllFinish(Long ownerType, Long ownerId, Date fromDate, Date toDate, Long finishStatus) throws Exception;

    public StockInspectExportDTO exportStockInspect(Date fromDate, Date toDate, Long ownerType, Long ownerId, Long prodOfferTypeId, Long stateId, boolean isSpecConfirmQuantity) throws Exception;

    public int updateApproveStaffNew(Long ownerType, Long ownerId, Long productOfferTypeId, Long prodOfferId, Long stateId, Date fromDate, Date toDate, StaffDTO staffDTO) throws Exception;

    public BaseMessage updateApprove(List<StockInspectDTO> lsStockInspectDTO) throws LogicException, Exception;
}
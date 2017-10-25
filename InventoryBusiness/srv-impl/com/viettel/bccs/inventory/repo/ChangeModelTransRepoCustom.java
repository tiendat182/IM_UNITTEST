package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.ApproveChangeProductDTO;
import com.viettel.bccs.inventory.dto.ChangeModelTransDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.ChangeModelTrans;

public interface ChangeModelTransRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<ApproveChangeProductDTO> getLstChangeStockModel(ApproveChangeProductDTO searchInputDTO) throws Exception;

    public List<ChangeModelTrans> getLstCancelRequestThread(Long maxDay) throws LogicException, Exception;

    public List<ChangeModelTrans> getLstApproveRequestThread(Long maxDay) throws LogicException, Exception;
    
    List<ChangeModelTransDTO> findApprovedRequestProductExchangeKit(Long shopId, Date fromDate, Date toDate) throws Exception, LogicException;
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ApproveChangeProductDTO;
import com.viettel.bccs.inventory.dto.ChangeModelTransDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.model.ChangeModelTrans;

import java.util.List;

import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface ChangeModelTransService {

    @WebMethod
    public ChangeModelTransDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ChangeModelTransDTO> findAll() throws Exception;

    @WebMethod
    public List<ChangeModelTransDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public ChangeModelTransDTO save(ChangeModelTransDTO changeModelTransDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ChangeModelTransDTO changeModelTransDTO) throws Exception;

    @WebMethod
    public List<ApproveChangeProductDTO> getLstChangeStockModel(ApproveChangeProductDTO searchInputDTO) throws LogicException, Exception;

    @WebMethod
    void approveChangeProductKit(Long changeModelTransId, Long staffId, StockTransDTO stockTransVoficeDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public void approveChangeProduct(Long changeModelTransId, StaffDTO approveStaffDTO, StockTransDTO stockTransVoficeDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception;

    @WebMethod
    public void cancelChangeProduct(Long changeModelTransId, StaffDTO approveStaffDTO) throws LogicException, Exception;

    @WebMethod
    public List<ChangeModelTrans> getLstCancelRequestThread(Long maxDay) throws LogicException, Exception;

    @WebMethod
    public List<ChangeModelTrans> getLstApproveRequestThread(Long maxDay) throws LogicException, Exception;

    public void processCancelRequest(Long changeModelTransId) throws LogicException, Exception;

    public void processApproveRequest(Long changeModelTransId) throws LogicException, Exception;
    
    @WebMethod
    ChangeModelTransDTO updateChangeModelTransStatus(Long changeModelTransId, Long changeModelTransStatus) throws Exception, LogicException;
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface StockInspectService {

    @WebMethod
    public StockInspectDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockInspectDTO> findAll() throws Exception;

    @WebMethod
    public List<StockInspectDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockInspectDTO save(StockInspectDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockInspectDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage updateApprove(List<StockInspectDTO> lsStockInspectDTO) throws LogicException, Exception;

    @WebMethod
    public List<ApproveStockInspectDTO> searchApproveInspect(Long approveUserId, Long ownerId, Long ownerType, Long approveStatus, Long prodOfferTypeId, String code, Date fromDate, Date toDate) throws Exception;

    @WebMethod
    public List<StockInspectCheckDTO> checkStockInspect(StockInspectDTO stockInspectDTO, StaffDTO staffDTO, boolean isInspectQuantity) throws LogicException, Exception;

    @WebMethod
    public void validateConfigDate(boolean isInspectQuantity) throws LogicException, Exception;

    @WebMethod
    public void validateStockInspect(StockInspectDTO stockInspectDTO, StaffDTO staffDTO, Date fromDate, Date toDate, boolean isInspectQuantity) throws LogicException, Exception;

    @WebMethod
    public boolean isFinishStockInspect(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws LogicException, Exception;

    public boolean isFinishStockInspectNotStockTotal(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws LogicException, Exception;

    @WebMethod
    public boolean isFinishNotCheckStockTotal(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate, Long approveStatus) throws LogicException, Exception;

    @WebMethod
    public Long getQuantityStockTotal(Long ownerType, Long ownerId, Long productOfferTypeId, Long prodOfferId, Long stateId) throws LogicException, Exception;

    @WebMethod
    public void validateSaveStockInspect(Long prodOfferId, Long productOfferTypeId, Long stateId, Long ownerType, Long ownerId,
                                         List<StockInspectCheckDTO> stockInspectList, StaffDTO userLogin, StaffDTO approveStaff,
                                         Date fromDate, Date toDate, boolean isInspectQuantity, boolean isApprover) throws LogicException, Exception;

    @WebMethod
    public void saveStockInspect(Long prodOfferId, Long productOfferTypeId, Long stateId, Long ownerType, Long ownerId,
                                 List<StockInspectCheckDTO> stockInspectList, boolean isApprover,
                                 StaffDTO staffDTO, StaffDTO approveStaff, boolean isInspectQuantity) throws LogicException, Exception;

    @WebMethod
    public List<StockInspectCheckDTO> addSerialToListManual(Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId, Long stateId,
                                                            String singleSerial, String fromSerial, String toSerial, List<StockInspectCheckDTO> lstSerial) throws LogicException, Exception;

    @WebMethod
    public void validateAddSerialToList(String singleSerial, String fromSerial, String toSerial, List<StockInspectCheckDTO> lstSerial, String tableName) throws LogicException, Exception;

    @WebMethod
    public List<StockInspectCheckDTO> addSerialToList2D(Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId, Long stateId,
                                                        String serial2D, List<StockInspectCheckDTO> lstSerial) throws LogicException, Exception;

    @WebMethod
    public void validateAddSerialToList2D(List<String> lstSerialInput, List<StockInspectCheckDTO> lstSerialTable, String tableName) throws LogicException, Exception;

    @WebMethod
    public List<Date> getFromDateToDateCheck(boolean isSpecConfirmQuantity) throws Exception;

    @WebMethod
    public StockInspectExportDTO exportStockInspect(Long ownerType, Long ownerId, Long prodOfferTypeId, Long stateId, boolean isSpecConfirmQuantity) throws LogicException, Exception;

    @WebMethod
    public void updateApproveStaffNew(Long productOfferTypeId, Long prodOfferId, Long stateId, Long ownerType, Long ownerId, StaffDTO staffDTO,
                                      StaffDTO approveStaff, boolean isSpecConfirmQuantity) throws LogicException, Exception;

}
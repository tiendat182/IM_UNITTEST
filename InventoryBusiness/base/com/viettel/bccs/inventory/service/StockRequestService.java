package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface StockRequestService {

    @WebMethod
    public StockRequestDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockRequestDTO> findAll() throws Exception;

    @WebMethod
    public List<StockRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockRequestDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockRequestDTO productSpecCharacterDTO) throws Exception;

    /**
     * ham lay ra max request id dung lam ma yeu cau
     *
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    public String getRequest() throws Exception;

    /**
     * ham lay ra max request id dung lam ma yeu cau
     *
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    public List<StockRequestDTO> findStockRequestByCode(String requestCode) throws Exception;

    /**
     * tim kiem yeu cau
     *
     * @param requestCode
     * @param fromDate
     * @param toDate
     * @param status
     * @param shopIdLogin
     * @param ownerId
     * @param ownerType
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    List<StockRequestDTO> getLsSearchStockRequest(String requestCode, Date fromDate, Date toDate, String status, Long shopIdLogin,
                                                  Long ownerId, Long ownerType) throws Exception;

    /**
     * xu ly lap yeu cau DOA
     *
     * @param stockRequestDTO
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    void excuteRequestTrans(StockRequestDTO stockRequestDTO) throws LogicException, Exception;

    void addTotalTTBH(StockTotalDTO totalDTOSearch, StockTotalAuditDTO totalAuditInput, Long ownerType, Long ownerId, Long prodOfferId,
                      Long oldStateId, Long quantity, Date sysdate, boolean isCheckIm1) throws Exception;

    void updateStockXTTBH(String fromSerial, String toSerial, Date updateDateTime, Long prodOfferId, Long productOfferTypeId, Long newOwnerType, Long newOwnerId, Long oldOwnerType, Long oldOwnerId, Long newStatus, Long oldStatus, Long oldStateId, boolean isCheckIm1) throws Exception;

    @WebMethod
    Long getAvailableQuantity(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception;

    @WebMethod
    public BaseMessage save(StockRequestDTO stockRequestDTO) throws Exception;

    @WebMethod
    public StockRequestDTO getStockRequestByStockTransId(Long stockTransId) throws Exception;

    /**
     * ham trung tam bao hanh nhan hang hong tu kho
     *
     * @param requestId
     * @param status
     * @param staffDTO
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    void excuteImportTTBGRequestTrans(Long requestId, String status, StaffDTO staffDTO) throws LogicException, Exception;

    /**
     * ham tra ve danh sach tim kiem yeu cau, dung cho trung tam bao hanh
     *
     * @param requestCode
     * @param fromDate
     * @param toDate
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    public List<StockRequestDTO> getLsSearchStockRequestTTBH(String requestCode, Date fromDate, Date toDate, String status, Long ownerId) throws Exception;

}
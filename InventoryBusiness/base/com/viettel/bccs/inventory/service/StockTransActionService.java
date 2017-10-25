package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.FieldExportFileDTO;
import com.viettel.bccs.inventory.dto.StockDeliveringBillDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.dto.VActionGoodsStatisticDTO;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface StockTransActionService {

    @WebMethod
    public StockTransActionDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransActionDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockTransActionDTO save(StockTransActionDTO stockTransActionDTO) throws Exception;

    @WebMethod
    public Long getSequence() throws Exception;

    @WebMethod
    public boolean checkExist(List<FieldExportFileDTO> lstFieldExportIsdn) throws LogicException, Exception;

    @WebMethod
    public StockTransActionDTO getStockTransActionDTOById(Long actionId) throws Exception;

    /**
     * @param stockTransActionDTO
     * @return
     * @required create_datetime
     */
    @WebMethod
    public StockTransActionDTO findStockTransActionDTO(StockTransActionDTO stockTransActionDTO);

    @WebMethod
    public void insertStockTransActionNative(StockTransActionDTO stockTransActionDTO) throws Exception;

    @WebMethod
    public List<StockTransActionDTO> getStockTransActionByToOwnerId(Long toOnwerId, Long toOwnerType) throws Exception;

    @WebMethod
    public StockTransActionDTO getStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception;

    @WebMethod
    public int unlockUserInfo(Long stockTransActionId) throws Exception;

    @WebMethod
    public int deleteStockTransActionByIdAndStatus(Long stockTransId, List<String> lstStatus) throws Exception;

    public List<StockDeliveringBillDetailDTO> getStockDeliveringResult(Date startTime, Date endTime) throws LogicException, Exception;

    /**
     * ham xu ly tim kiem gom kho dieu chuyen
     * @author thanhnt77
     * @param actionCode
     * @param ownerId
     * @param ownerType
     * @param ownerLoginId
     * @param ownerLoginType
     * @param fromDate
     * @param toDate
     * @param transType
     * @param typeOrderNote
     * @return
     * @throws LogicException
     * @throws Exception
     */
    public List<VActionGoodsStatisticDTO> getListVActionStatistic(String actionCode, Long ownerId, Long ownerType, Long ownerLoginId,
                                                                  Long ownerLoginType, Date fromDate, Date toDate, String transType, String typeOrderNote) throws LogicException, Exception;

    public int deleteStockTransAction(Long stockTransActionId) throws Exception;
}
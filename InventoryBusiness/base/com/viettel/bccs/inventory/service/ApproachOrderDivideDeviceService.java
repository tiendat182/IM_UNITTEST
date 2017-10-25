package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

/**
 * Created by vanho on 26/03/2017.
 */
@WebService
public interface ApproachOrderDivideDeviceService {

    @WebMethod
    List<StockRequestDTO> getListOrderDivideDevicePendingApproach(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception;

    @WebMethod
    void approachOrderDivide(StockRequestDTO stockRequestDTOSelected) throws LogicException, Exception;

    @WebMethod
    void rejectOrderDivide(StockRequestDTO stockRequestDTOSelected, List<StockTransFullDTO> stockTransFullDTOList, Long userRejectId) throws LogicException, Exception;
}

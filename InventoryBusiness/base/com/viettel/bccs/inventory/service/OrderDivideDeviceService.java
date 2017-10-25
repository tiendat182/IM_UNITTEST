package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

/**
 * Created by vanho on 23/03/2017.
 */
@WebService
public interface OrderDivideDeviceService {

    @WebMethod
    List<StockRequestDTO> getListOrderDivideDevice(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception;

    @WebMethod
    List<ProductOfferingTotalDTO> getLsProductOfferingDTO(String inputSearch, Long ownerId, String ownerType, String stateId) throws Exception;

    @WebMethod
    void createOrderDivideDevice(StockRequestDTO stockRequestDTO) throws LogicException, Exception;

    @WebMethod
    byte[] getAttachFileContent(Long stockRequestId) throws Exception;

    @WebMethod
    ProductOfferingDTO getProductByCodeAndProbType(String code, Long probTypeId);

}

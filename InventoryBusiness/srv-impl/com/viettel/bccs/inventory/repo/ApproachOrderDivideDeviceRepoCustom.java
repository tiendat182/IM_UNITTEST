package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockRequestDTO;

import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

/**
 * Created by vanho on 26/03/2017.
 */
public interface ApproachOrderDivideDeviceRepoCustom {

    @WebMethod
    List<StockRequestDTO> getListOrderDivideDevicePendingApproach(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception;


}

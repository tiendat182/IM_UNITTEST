package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.dto.StockRequestDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by vanho on 23/03/2017.
 */
public interface OrderDivideDeviceRepoCustom {

    List<StockRequestDTO> getListOrderDivideDevice(String shopPath, Date fromDate, Date toDate, Long ownerId, String requestCode, String status) throws Exception;
    byte[] getAttachFileContent(Long stockRequestId) throws Exception;
}

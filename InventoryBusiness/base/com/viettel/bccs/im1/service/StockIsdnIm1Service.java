package com.viettel.bccs.im1.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockIsdnTrans;
import com.viettel.bccs.inventory.model.StockIsdnTransDetail;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.util.List;

/**
 * Created by sinhhv on 3/7/2016.
 */
@Service
public interface StockIsdnIm1Service {
    @WebMethod
    public BaseMessage doSaveStockIsdn(List<StockIsdnTransDTO> lstStockIsdnTrans, List<StockTransDetailDTO> lstStockTransSerial, List<StockIsdnTransDetailDTO> lstStockIsdnTransDetail) throws LogicException, Exception;

    @WebMethod
    public BaseMessage doDistributeIsdn(InputDistributeByRangeDTO inputByRange, List<Object[]> listRangePreviewed, List<StockIsdnTrans> lstStockIsdnTrans, List<StockIsdnTransDetail> lstStockIsdnTransDetail) throws LogicException, Exception;

    @WebMethod
    public BaseMessage doDistriButeIsdnByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, List<StockIsdnTrans> lstStockIsdnTrans, List<StockIsdnTransDetail> lstStockIsdnTransDetail) throws LogicException, Exception;

    @WebMethod
    public void lockOrUnlockIsdn(String isdn, Long newStatus, List<Long> lstStatus, StaffDTO staff) throws LogicException, Exception ;
}

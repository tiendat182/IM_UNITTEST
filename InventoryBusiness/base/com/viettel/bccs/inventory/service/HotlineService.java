package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface HotlineService {

    @WebMethod
    public BaseMessage lockIsdn(@WebParam(name = "isdn") String isdn, @WebParam(name = "requestId") Long requestId,
                                @WebParam(name = "staffCode") String staffCode);

    @WebMethod
    public List<StockIsdnVtShopDTO> findRequestList(@WebParam(name = "requestList") List<Long> requestList);

    @WebMethod
    public List<LookupIsdnDTO> getListLookupIsdn(@WebParam(name = "fromIsdn") String fromIsdn, @WebParam(name = "toIsdn") String toIsdn,
                                                 @WebParam(name = "maxRow") Long maxRow) throws LogicException, Exception;

    @WebMethod
    public BaseMessage lockIsdnHotline(@WebParam(name = "isdn") String isdn, @WebParam(name = "staffCode") String staffCode);

    @WebMethod
    public BaseMessage unlockIsdnHotline(@WebParam(name = "isdn") String isdn);
}
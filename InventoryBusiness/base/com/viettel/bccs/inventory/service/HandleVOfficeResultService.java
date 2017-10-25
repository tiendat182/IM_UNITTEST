package com.viettel.bccs.inventory.service;

import com.viettel.fw.dto.BaseMessage;
import com.viettel.voffice.vo.ReceiveFromVOffice;
import com.viettel.voffice.vo.ReceiveFromVOfficeList;
import com.viettel.voffice.vo.ResultObj;
import com.viettel.voffice.vo.ResultObjList;

import javax.jws.WebMethod;
import javax.jws.WebService;

public interface HandleVOfficeResultService {


    @WebMethod
    public BaseMessage returnSignResult(ResultObj resultObj) throws Exception;

}
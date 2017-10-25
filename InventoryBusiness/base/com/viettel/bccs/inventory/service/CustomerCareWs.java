package com.viettel.bccs.inventory.service;

import com.viettel.bccs.cc.dto.HistoryInforCardNumberDTO;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.AddGoodSaleMessage;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface CustomerCareWs {
    @WebMethod
    public CardNumberLockDTO getHistoryCardNumber(String serial) throws Exception, LogicException;

    @WebMethod
    public HistoryInforCardNumberDTO getInforCardNumber(String serial, String regType, boolean isCheck, Date fromDate, Date toDate) throws LogicException, Exception;

    @WebMethod
    public BaseMessage lockedCard(String user, String serial, String shopCode, String reason, String isdn, String ip, String valueCard) throws Exception;
}
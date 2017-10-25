package com.viettel.bccs.inventory.service;

import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface AutoSignVOfficeService {
    /**
     * ham check user and pass
     *
     * @param userName
     * @param passWord
     * @return
     * @author ThanhNt77
     */
    @WebMethod
    public boolean checkAccount(String userName, String passWord) throws LogicException, Exception;

    /**
     * ham tra ve trang thai ky vOffice
     *
     * @param transCode
     * @return
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    @WebMethod
    public String getSignStatus(String transCode) throws LogicException, Exception;
}
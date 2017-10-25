package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.StaffIm1DTO;
import com.viettel.fw.Exception.LogicException;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface StaffIm1Service {
    @WebMethod
    public StaffIm1DTO findOne(Long id) throws Exception, LogicException;

    @WebMethod
    public StaffIm1DTO findOneStaff(Long id) throws Exception, LogicException;

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StaffIm1DTO save(StaffIm1DTO staffIm1DTO) throws Exception;
}
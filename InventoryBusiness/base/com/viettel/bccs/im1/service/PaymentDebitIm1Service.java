package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.PaymentDebitIm1DTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface PaymentDebitIm1Service {

    @WebMethod
    public PaymentDebitIm1DTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PaymentDebitIm1DTO> findAll() throws Exception;

    @WebMethod
    public List<PaymentDebitIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(PaymentDebitIm1DTO PaymentDebitIm1DTO) throws Exception;

    @WebMethod
    public BaseMessage update(PaymentDebitIm1DTO PaymentDebitIm1DTO) throws Exception;

    public PaymentDebitIm1DTO getPaymentDebit(Long ownerId, Long ownerType) throws Exception;

}
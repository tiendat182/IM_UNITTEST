package com.viettel.bccs.sale.service;

import com.viettel.bccs.sale.dto.PaymentDebitDTO;
import com.viettel.bccs.sale.model.PaymentDebit;

import java.util.List;

import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface PaymentDebitService {

    @WebMethod
    public PaymentDebitDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PaymentDebitDTO> findAll() throws Exception;

    @WebMethod
    public List<PaymentDebitDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public PaymentDebitDTO create(PaymentDebitDTO paymentDebitDTO) throws Exception;

    @WebMethod
    public BaseMessage update(PaymentDebitDTO paymentDebitDTO) throws Exception;

    public PaymentDebitDTO getPaymentDebit(Long ownerId, Long ownerType) throws Exception;

}
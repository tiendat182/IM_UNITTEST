package com.viettel.bccs.sale.service;

import com.viettel.bccs.sale.dto.PaymentGroupServiceDTO;
import com.viettel.bccs.sale.model.PaymentGroupService;

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
public interface PaymentGroupServiceService {

    @WebMethod
    public PaymentGroupServiceDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PaymentGroupServiceDTO> findAll() throws Exception;

    @WebMethod
    public List<PaymentGroupServiceDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(PaymentGroupServiceDTO paymentGroupServiceDTO) throws Exception;

    @WebMethod
    public BaseMessage update(PaymentGroupServiceDTO paymentGroupServiceDTO) throws Exception;

    @WebMethod
    public List<PaymentGroupServiceDTO> getLstGroupService() throws Exception;

    public PaymentGroupServiceDTO getPaymentGroupServiceByName(String name) throws Exception;

}
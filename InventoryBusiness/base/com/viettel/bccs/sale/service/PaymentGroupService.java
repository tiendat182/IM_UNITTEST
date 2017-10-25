package com.viettel.bccs.sale.service;

import com.viettel.bccs.sale.dto.PaymentGroupDTO;
import com.viettel.bccs.sale.dto.PaymentGroupDayTypeDTO;
import com.viettel.bccs.sale.model.PaymentGroup;

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
public interface PaymentGroupService {

    @WebMethod
    public PaymentGroupDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PaymentGroupDTO> findAll() throws Exception;

    @WebMethod
    public List<PaymentGroupDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(PaymentGroupDTO paymentGroupDTO) throws Exception;

    @WebMethod
    public BaseMessage update(PaymentGroupDTO paymentGroupDTO) throws Exception;

    @WebMethod
    public List<PaymentGroupDTO> getLstPaymentGroup() throws Exception;

    public PaymentGroupDTO getPaymentGroupByName(String name) throws Exception;

    public List<PaymentGroupDayTypeDTO> getLstDayTypeByPaymentGroupId(Long paymentGroupId) throws Exception;


}
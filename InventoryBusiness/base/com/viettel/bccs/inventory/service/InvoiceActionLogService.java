package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.InvoiceActionLogDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface InvoiceActionLogService {

    @WebMethod
    public InvoiceActionLogDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceActionLogDTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceActionLogDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceActionLogDTO invoiceActionLogDTO) throws Exception,LogicException;
    @WebMethod
    public BaseMessage update(InvoiceActionLogDTO invoiceActionLogDTO) throws Exception;

}
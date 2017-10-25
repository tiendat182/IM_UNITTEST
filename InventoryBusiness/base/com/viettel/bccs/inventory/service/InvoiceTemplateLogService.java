package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.InvoiceTemplateLogDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface InvoiceTemplateLogService {

    @WebMethod
    public InvoiceTemplateLogDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceTemplateLogDTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceTemplateLogDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceTemplateLogDTO invoiceTemplateLogDTO, String staffCode, Date createDate) throws Exception, LogicException;

    @WebMethod
    public BaseMessage update(InvoiceTemplateLogDTO invoiceTemplateLogDTO) throws Exception;

}
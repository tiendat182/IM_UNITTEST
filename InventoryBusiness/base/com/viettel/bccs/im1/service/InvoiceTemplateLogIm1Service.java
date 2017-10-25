package com.viettel.bccs.im1.service;

import java.util.List;

import com.viettel.bccs.im1.dto.InvoiceTemplateLogIm1DTO;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface InvoiceTemplateLogIm1Service {

    @WebMethod
    public InvoiceTemplateLogIm1DTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceTemplateLogIm1DTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceTemplateLogIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceTemplateLogIm1DTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(InvoiceTemplateLogIm1DTO productSpecCharacterDTO) throws Exception;

}
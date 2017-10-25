package com.viettel.bccs.im1.service;

import java.util.List;

import com.viettel.bccs.im1.dto.InvoiceTemplateIm1DTO;
import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.Exception.LogicException;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface InvoiceTemplateIm1Service {

    @WebMethod
    public InvoiceTemplateIm1DTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceTemplateIm1DTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceTemplateIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceTemplateIm1DTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(InvoiceTemplateIm1DTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public void deliverInvoice(Long invoiceDeliveryId, List<InvoiceTemplateDTO> lstInvoiceReceive, String staffCode, Long amountDelivery) throws LogicException, Exception;

    @WebMethod
    public void revokeInvoice(Long invoiceReceive, List<InvoiceTemplateDTO> lstInvoiceRevoke, String staffCode, Long amountReceive) throws LogicException, Exception;
}
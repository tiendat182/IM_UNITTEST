package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.InvoiceTemplateTypeDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface InvoiceTemplateTypeService {

    @WebMethod
    public InvoiceTemplateTypeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceTemplateTypeDTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceTemplateTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceTemplateTypeDTO productSpecCharacterDTO) throws Exception;

    @WebMethod

    public BaseMessage update(InvoiceTemplateTypeDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    /**
     * Lay danh sach phoi hoa don theo loai hoa don
     * author HoangNT
     */
    public List<InvoiceTemplateTypeDTO> getInvoiceTemplateType() throws Exception;
}
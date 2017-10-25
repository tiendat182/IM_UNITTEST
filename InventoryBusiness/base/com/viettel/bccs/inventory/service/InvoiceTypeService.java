package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface InvoiceTypeService {

    @WebMethod
    public InvoiceTypeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceTypeDTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceTypeDTO invoiceTypeDTO) throws Exception;

    /**
     * Them moi loai hoa don
     * author HoangNT
     *
     * @param invoiceTypeDTO
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public InvoiceTypeDTO createNewInvoiceType(InvoiceTypeDTO invoiceTypeDTO, String staffCode) throws Exception, LogicException;

    /**
     * Lay danh sach loai hoa don theo loai hoa don
     * author HoangNT
     *
     * @param type
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceTypeDTO> getInvoiceType(Long type) throws Exception, LogicException;

    /**
     * Cap nhat loai hoa don
     * author HoangNT
     *
     * @param invoiceTypeDTO
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public BaseMessage update(InvoiceTypeDTO invoiceTypeDTO, String staffCode) throws Exception, LogicException;

    /**
     * Xoa danh sach loai hoa don
     * author HoangNT
     *
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException;

    /**
     * Tim kiem danh sach loai hoa don
     * author HoangNT
     *
     * @param invoiceTypeDTO
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceTypeDTO> search(InvoiceTypeDTO invoiceTypeDTO) throws Exception, LogicException;

}
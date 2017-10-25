package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.InvoiceSerialDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface InvoiceSerialService {

    @WebMethod
    public InvoiceSerialDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceSerialDTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceSerialDTO invoiceSerialDTO) throws Exception;

    @WebMethod
    public BaseMessage update(InvoiceSerialDTO invoiceSerialDTO, String staffCode) throws Exception, LogicException;

    /**
     * Them moi danh sach serial hoa don
     *
     * @param lst
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public List<InvoiceSerialDTO> createNewInvoiceSerial(List<InvoiceSerialDTO> lst, String staffCode) throws Exception, LogicException;

    @WebMethod
    public List<InvoiceSerialDTO> getAllSerialByInvoiceType(Long shopId, Long invoiceTypeId) throws Exception, LogicException;

    /**
     * Xoa danh sach serial hoa don
     * author HoangNT
     *
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException;

    /**
     * DungPT16
     * Tim kiem khong dung filterRequest
     *
     * @param invoiceSerialDTO
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceSerialDTO> searchInvoiceSerial(InvoiceSerialDTO invoiceSerialDTO) throws Exception, LogicException;

    /**
     * DungPT16
     * Lay thong tin invoiceSerial tu loai hoa don.
     *
     * @param invoiceTypeId
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public List<InvoiceSerialDTO> getInvoiceSerialFromInvoiceType(Long invoiceTypeId) throws Exception, LogicException;
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.InvoiceListDTO;
import com.viettel.bccs.inventory.dto.InvoiceSerialDTO;
import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface InvoiceListService {

    @WebMethod
    public InvoiceListDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceListDTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceListDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceListDTO invoiceListDTO) throws Exception;

    @WebMethod
    public BaseMessage update(InvoiceListDTO invoiceListDTO, String staffCode) throws Exception, LogicException;

    /**
     * Them moi dai hoa don
     * author HoangNT
     *
     * @param invoiceListDTO
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public InvoiceListDTO createNewInvoiceList(InvoiceListDTO invoiceListDTO, String staffCode) throws Exception, LogicException;

    /**
     * Xoa dai hoa don
     * author HoangNT
     *
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException;

    /**
     * Lay danh sach serial theo loai hoa don
     * auhtor HoangNT
     *
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceSerialDTO> getAllSerialByInvoiceType(Long shopId, Long invoiceTypeId) throws Exception, LogicException;


    @WebMethod
    public List<InvoiceTypeDTO> getAllInvoiceTypeByType(Long type) throws Exception, LogicException;

    /**
     * Lay thong tin dai hoa don theo don vi
     * author HoangNT
     *
     * @param shopId
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceListDTO> getAllInvoiceListByShopId(Long shopId) throws Exception, LogicException;

    /**
     * Lay thong tin  dai hoa don theo serial code
     * author HoangNT
     *
     * @param serialCode
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceListDTO> getAllInvoiceListBySerialCode(String serialCode) throws Exception, LogicException;

    /**
     * Lay thong tin dai hoa don theo id dai hoa don
     * author HoangNT
     *
     * @param invoiceListId
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceListDTO> getAllInvoiceListByInvoiceListId(Long invoiceListId) throws Exception, LogicException;

    /**
     * Cap nhat thong tin dai hoa don
     * author HoangNT
     *
     * @param invoiceListId
     * @return
     * @throws Exception
     */
    @WebMethod
    public BaseMessage updateInvoiceListByInvoiceListId(Long invoiceListId) throws Exception, LogicException;

    @WebMethod
    public List<InvoiceListDTO> searchInvoiceList(InvoiceListDTO invoiceListDTO) throws Exception, LogicException;

    /**
     * DungPT16
     * Lay Thong tin dai hoa don dua vao serial
     * @param invoiceSerialId
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public List<InvoiceListDTO> getInvoiceListFromSerial(Long invoiceSerialId) throws Exception, LogicException;
}
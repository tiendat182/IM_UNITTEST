package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface InvoiceTemplateService {

    @WebMethod
    public InvoiceTemplateDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<InvoiceTemplateDTO> findAll() throws Exception;

    @WebMethod
    public List<InvoiceTemplateDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(InvoiceTemplateDTO invoiceTemplateDTO) throws Exception;

    /**
     * Cap nhap thong tin phoi hoa don
     * author HoangNT
     *
     * @param invoiceTemplateDTO
     * @return
     * @throws Exception
     */
    @WebMethod
    public BaseMessage update(InvoiceTemplateDTO invoiceTemplateDTO, String staffCode) throws Exception, LogicException;

    /**
     * Them moi phoi hoa don va so luong tuong ung
     * author Hoang NT
     *
     * @param invoiceTemplateDTO
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    public InvoiceTemplateDTO createNewInvoiceTemplate(InvoiceTemplateDTO invoiceTemplateDTO, String staffCode, String typeAddNew) throws Exception, LogicException;

    /**
     * Tim kiem danh sach phoi hoa don
     * author HoangNT
     *
     * @param invoiceTemplateDTO
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceTemplateDTO> search(InvoiceTemplateDTO invoiceTemplateDTO) throws Exception, LogicException;

    /**
     * Giao phoi hoa don
     * author HoangNT
     *
     * @param invoiceDeliveryId
     * @param lstInvoiceReceive
     * @return
     * @throws Exception
     */
    @WebMethod
    public BaseMessage deliveryInvoiceTempalte(Long invoiceDeliveryId, List<InvoiceTemplateDTO> lstInvoiceReceive, String staffCode) throws Exception, LogicException;

    /**
     * Thu hoi phoi hoa don
     * author HoangNT
     *
     * @param invoiceDeliveryId
     * @param lstInvoiceReceive
     * @return
     * @throws Exception
     */
    @WebMethod
    public BaseMessage retrieveInvoiceTempalte(Long invoiceDeliveryId, List<InvoiceTemplateDTO> lstInvoiceReceive, String staffCode) throws Exception, LogicException;

    /**
     * Lay danh sach don vi va so luong phoi hoa don da duoc giao tuong ung
     * author HoangNT
     *
     * @param shopId
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceTemplateDTO> getAllReceiveInvoiceTemplateByShopId(Long shopId) throws Exception, LogicException;

    @WebMethod
    public List<InvoiceTemplateDTO> getInvoiceTemplateList(Long shopId) throws Exception, LogicException;

    @WebMethod
    public List<InvoiceTemplateDTO> getInvoiceTemplateExsitByOwnerType(Long ownerId, Long invoiceTemplateTypeId, String ownerType) throws Exception, LogicException;

}
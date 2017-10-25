package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ReceiptExpenseDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ReceiptExpenseService {

    @WebMethod
    public ReceiptExpenseDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ReceiptExpenseDTO> findAll() throws Exception;

    @WebMethod
    public List<ReceiptExpenseDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public ReceiptExpenseDTO create(ReceiptExpenseDTO receiptExpenseDTO, String staffCode) throws Exception, LogicException;

    @WebMethod
    public ReceiptExpenseDTO update(ReceiptExpenseDTO receiptExpenseDTO, String status) throws Exception;

    @WebMethod
    public ReceiptExpenseDTO findByStockTransIdAndType(Long stockTransId, String type) throws Exception, LogicException;

    @WebMethod
    public String generateReceiptNo(String shopCode) throws Exception, LogicException;
}
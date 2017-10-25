package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import com.viettel.bccs.inventory.dto.DebitRequestDetailDTO;
import com.viettel.bccs.inventory.message.DebitRequestResponse;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface DebitRequestService {

    public DebitRequestDTO findOne(Long id) throws Exception;

    public Long count(List<FilterRequest> filters) throws Exception;

    public List<DebitRequestDTO> findAll() throws Exception;

    public List<DebitRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage save(DebitRequestDTO debitRequestDTO) throws LogicException, Exception;

    @WebMethod
    public BaseMessage create(DebitRequestDTO debitRequestDTO) throws LogicException, Exception;

    @WebMethod
    public BaseMessage update(DebitRequestDTO debitRequestDTO) throws Exception;

    @WebMethod
    public byte[] getAttachFileContent(Long requestId) throws Exception;

    @WebMethod
    public DebitRequestResponse approveDebitRequest(DebitRequestDTO debitRequestDTO) throws LogicException, Exception;

    @WebMethod
    public List<DebitRequestDetailDTO> findDebitRequestDetailByDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception;

    @WebMethod
    public BaseMessage deleteDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception;

    @WebMethod
    public List<DebitRequestDTO> findDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception;

    @WebMethod
    public Long getRevenueInMonth(Long ownerId, String ownerType) throws Exception;

}
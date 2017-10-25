package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ReasonDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ReasonService {

    @WebMethod
    public ReasonDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ReasonDTO> findAll() throws Exception;

    @WebMethod
    public List<ReasonDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ReasonDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(ReasonDTO productSpecCharacterDTO) throws Exception;

    /**
     * Ham tra ve danh sanh reasonDTO phu thuoc vao reasonType
     * @author ThanhNT
     * @param reasonType
     * @return
     */
    public List<ReasonDTO> getLsReasonByType(String reasonType) throws LogicException, Exception;
    /**
     * Ham tra ve danh sanh reasonDTO phu thuoc vao reasonCode
     * @author Tuydv1
     * @param reasonCode
     * @return
     */
    public List<ReasonDTO> getLsReasonByCode(String reasonCode) throws LogicException, Exception;

    public ReasonDTO getReason(String reasonCode, String reasonType) throws LogicException, Exception;
}
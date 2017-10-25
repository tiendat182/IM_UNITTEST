package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.DebitDayTypeDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface DebitDayTypeService {

    @WebMethod
    public DebitDayTypeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<DebitDayTypeDTO> findAll() throws Exception;

    @WebMethod
    public List<DebitDayTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(DebitDayTypeDTO createDTO, StaffDTO staffDTO) throws Exception;

    @WebMethod
    public BaseMessage update(DebitDayTypeDTO editDTO, StaffDTO staffDTO) throws Exception;

    @WebMethod
    public BaseMessage delete(List<DebitDayTypeDTO> lstDebitDayTypeDTOs, StaffDTO staffDTO) throws Exception;

    @WebMethod
    public List<DebitDayTypeDTO> searchDebitDayType(DebitDayTypeDTO dto) throws Exception;

    @WebMethod
    public byte[] getAttachFileContent(Long requestId) throws Exception;
}
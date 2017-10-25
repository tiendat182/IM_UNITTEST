package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.EquipmentVendorDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface EquipmentVendorService {

    @WebMethod
    public EquipmentVendorDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<EquipmentVendorDTO> findAll() throws Exception;

    @WebMethod
    public List<EquipmentVendorDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(EquipmentVendorDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(EquipmentVendorDTO productSpecCharacterDTO) throws Exception;

}
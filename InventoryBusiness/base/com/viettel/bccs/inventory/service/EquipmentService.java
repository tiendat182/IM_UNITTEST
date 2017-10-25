package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.EquipmentDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;


@WebService
public interface EquipmentService {

    @WebMethod
    public EquipmentDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<EquipmentDTO> findAll() throws Exception;

    @WebMethod
    public List<EquipmentDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(EquipmentDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(EquipmentDTO productSpecCharacterDTO) throws Exception;

    /**
     * Ham lay danh sach cac Equipment voi type = 2
     * author HoangNT
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<EquipmentDTO> getEquipmentBras() throws Exception;

}
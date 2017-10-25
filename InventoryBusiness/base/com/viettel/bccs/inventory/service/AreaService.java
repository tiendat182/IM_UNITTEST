package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface AreaService {

    @WebMethod
    public AreaDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<AreaDTO> findAll() throws Exception;

    @WebMethod
    public List<AreaDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public AreaDTO findByCode(String areaCode) throws Exception ;

    @WebMethod
    public BaseMessage create(AreaDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(AreaDTO productSpecCharacterDTO) throws Exception;


    @WebMethod
    public List<AreaDTO> getAllProvince() throws Exception;

    @WebMethod
    public List<AreaDTO> search(AreaDTO searchDto) throws Exception;

}
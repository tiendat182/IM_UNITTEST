package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ChangeModelTransSerialDTO;
import com.viettel.bccs.inventory.model.ChangeModelTransSerial;

import java.util.List;

import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface ChangeModelTransSerialService {

    @WebMethod
    public ChangeModelTransSerialDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ChangeModelTransSerialDTO> findAll() throws Exception;

    @WebMethod
    public List<ChangeModelTransSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public ChangeModelTransSerialDTO save(ChangeModelTransSerialDTO changeModelTransSerialDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ChangeModelTransSerialDTO changeModelTransSerialDTO) throws Exception;

    @WebMethod
    public List<ChangeModelTransSerialDTO> viewDetailSerial(Long changeModelTransDetailId) throws Exception;

}
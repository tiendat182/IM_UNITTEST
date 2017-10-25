package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ActiveKitVofficeDTO;
import com.viettel.bccs.inventory.model.ActiveKitVoffice;

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
public interface ActiveKitVofficeService {

    @WebMethod
    public ActiveKitVofficeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ActiveKitVofficeDTO> findAll() throws Exception;

    @WebMethod
    public List<ActiveKitVofficeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public ActiveKitVofficeDTO save(ActiveKitVofficeDTO activeKitVofficeDTO) throws Exception;
    
    @WebMethod
    public BaseMessage create(ActiveKitVofficeDTO activeKitVofficeDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ActiveKitVofficeDTO activeKitVofficeDTO) throws Exception;

    public ActiveKitVofficeDTO findByChangeModelTransId(Long changeModelTransId) throws Exception;

}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ChangeModelTransDTO;
import com.viettel.bccs.inventory.dto.ChangeModelTransDetailDTO;
import com.viettel.bccs.inventory.model.ChangeModelTransDetail;

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
public interface ChangeModelTransDetailService {

    @WebMethod
    public ChangeModelTransDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ChangeModelTransDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<ChangeModelTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public ChangeModelTransDetailDTO save(ChangeModelTransDetailDTO changeModelTransDetailDTO) throws Exception;

    @WebMethod
    public BaseMessage update(ChangeModelTransDetailDTO changeModelTransDetailDTO) throws Exception;

    @WebMethod
    public List<ChangeModelTransDetailDTO> viewDetail(Long changeModelTransId) throws Exception;

    @WebMethod
    public Long getNewProdOfferId(Long oldProdOfferId, Long changeModelTransId, Long stateId) throws Exception;

    @WebMethod
    public List<ChangeModelTransDetailDTO> getLstDetailByChangeModelTransId(Long changeModelTransId) throws Exception;


}
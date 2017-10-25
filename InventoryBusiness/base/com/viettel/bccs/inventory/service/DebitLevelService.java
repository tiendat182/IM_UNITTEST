package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.DebitLevelDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface DebitLevelService {

    @WebMethod
    public DebitLevelDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<DebitLevelDTO> findAll() throws Exception;

    @WebMethod
    public List<DebitLevelDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(DebitLevelDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(DebitLevelDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<DebitLevelDTO> searchDebitlevel(DebitLevelDTO productSpecCharacterDTO) throws Exception;

    public List<DebitLevelDTO> getLstDebitByLevel(Long level) throws Exception;

}
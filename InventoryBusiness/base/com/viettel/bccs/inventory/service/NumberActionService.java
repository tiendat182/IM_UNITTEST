package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.NumberActionDTO;
import com.viettel.bccs.inventory.dto.SearchNumberRangeDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface NumberActionService {

    @WebMethod
    public NumberActionDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<NumberActionDTO> findAll() throws Exception;

    @WebMethod
    public List<NumberActionDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public NumberActionDTO create(NumberActionDTO productSpecCharacterDTO) throws LogicException,Exception;

    @WebMethod
    public BaseMessage update(NumberActionDTO productSpecCharacterDTO) throws LogicException,Exception;

    @WebMethod
    public Boolean checkOverlap(Long fromIsdn,Long toIsdn,String telecomServiceId) throws LogicException,Exception;

    @WebMethod
    public List<NumberActionDTO> search(SearchNumberRangeDTO searchDto) throws Exception;
}
package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.FilterSpecialNumberDTO;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.StockNumberDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.List;
@WebService
public interface FilterSpecialNumberService {

    @WebMethod
    public FilterSpecialNumberDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<FilterSpecialNumberDTO> findAll() throws Exception;

    @WebMethod
    public List<FilterSpecialNumberDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(FilterSpecialNumberDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(FilterSpecialNumberDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage insertBatch(List<StockNumberDTO> listNumber) throws Exception;
	
	 @WebMethod
    public BaseMessage filterNumber(String userCode, boolean refilter, Long minNumber, List<NumberFilterRuleDTO> lstSelectedRule, Long ownerId,
                                    List<String> lstStatus, Long telecomServiceId, BigDecimal startNumberConvert, BigDecimal endNumberConvert) throws  Exception, LogicException;
									
    @WebMethod
    public List<Object[]> getResultFilter() throws  LogicException, Exception;

    @WebMethod
    public List<FilterSpecialNumberDTO> getListSprecialNumberByRule(Long ruleId) throws  LogicException, Exception;

    @WebMethod
    public BaseMessage updateFiltered(List<FilterSpecialNumberDTO> listNumberFiltered,List<FilterSpecialNumberDTO> listNumberOK,String userUpdateCode,String userIp) throws LogicException,Exception;

    @WebMethod
    public BaseMessage updateAllFiltered(String userUpdateCode,String userIp,List<Long> listFilterId) throws LogicException,Exception;
}
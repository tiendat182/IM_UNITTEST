package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface NumberFilterRuleService {

    @WebMethod
    public NumberFilterRuleDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<NumberFilterRuleDTO> findAll() throws Exception;

    @WebMethod
    public List<NumberFilterRuleDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(NumberFilterRuleDTO productSpecCharacterDTO, StaffDTO staffDTO) throws Exception;
    @WebMethod
    public BaseMessage update(NumberFilterRuleDTO productSpecCharacterDTO, StaffDTO staffDTO) throws Exception;


    @WebMethod
    public Long checkInsertConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception;

    @WebMethod
    public Long checkEditConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception;

    @WebMethod
    public Boolean checkExistNameNumberFilter(String name, Long filterId) throws Exception;

    //add query tap luat
    @WebMethod
    public List<NumberFilterRuleDTO> findRuleAggregate(NumberFilterRuleDTO numberFilterRule, boolean extract) throws Exception;

    @WebMethod
    public BaseMessage deleteListNumberFiler(List<NumberFilterRuleDTO> lsFinanceTypeDTOs, StaffDTO staffDTO) throws Exception;

    //anhnv33 loc so
    @WebMethod
    public List<NumberFilterRuleDTO> searchHightOrderRule(Long telecomServiceId, Long minNumber) throws Exception;
	
	public List<NumberFilterRuleDTO> getListNumerFilterRule(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception;

    public boolean checkBeforeDelete(Long groupFilterId) throws Exception;
}
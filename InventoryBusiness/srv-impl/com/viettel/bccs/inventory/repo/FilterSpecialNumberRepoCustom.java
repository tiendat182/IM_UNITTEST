package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.FilterSpecialNumberDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import java.util.List;

public interface FilterSpecialNumberRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<Object[]> getResultFilter();

    public BaseMessage updateFiltered(List<FilterSpecialNumberDTO> listNumberFiltered, List<FilterSpecialNumberDTO> listNumberOK, String userUpdateCode, String userIp)throws LogicException,Exception;

    public BaseMessage updateAllFiltered(String userUpdateCode, String userIp,List<Long> listFilterId)throws LogicException,Exception;

    public List<FilterSpecialNumberDTO> getListSprecialNumberByRule(Long ruleId) throws LogicException, Exception;

}